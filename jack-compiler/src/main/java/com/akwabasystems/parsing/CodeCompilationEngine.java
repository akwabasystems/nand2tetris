
package com.akwabasystems.parsing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.akwabasystems.model.Grammar;
import com.akwabasystems.model.Identifier;
import com.akwabasystems.model.IdentifierKind;
import com.akwabasystems.model.Keyword;
import com.akwabasystems.model.Scope;
import com.akwabasystems.model.Segment;
import com.akwabasystems.model.StandardLibrary;
import com.akwabasystems.model.SymbolTable;
import com.akwabasystems.model.Token;
import com.akwabasystems.model.TokenType;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;


/**
 *
 * A class that outputs the compiled code. It gets an input from a tokenizer, and emits the parsed structure in XML
 * format, which will later be written to a destination file.
 * 
 * The output is generated by a series of "compilexxx" routines, one for every syntactic element "xxx" of the 
 * Jack grammar.
 * 
 * This implementation follows the LL(1) pattern. It uses a look-ahead token that holds the value of the next token. It
 * then takes the appropriate based on the value of that look-ahead token.
 */
public final class CodeCompilationEngine {
    private final Tokenizer tokenizer;
    private Token lookahead;
    private Token currentToken;
    private String className;
    private final SymbolTable symbolTable = new SymbolTable();
    private final VMCodeWriter codeWriter;
    private final StringBuilder output = new StringBuilder();

    /** Maps that keep track of WHILE and IF iteration counts for subroutines */
    private final Map<String,Integer> ifCounts = new ConcurrentHashMap<>();
    private final Map<String,Integer> whileCounts = new ConcurrentHashMap<>();

    /**
     * Matches the following:
     * - function void main()
     * - constructor void Main()
     * - method Main main(int a, int b, int c)
     */
    private static final String FUNCTION_REGEXP = "(function|constructor|method)\\s+(void|\\w+)\\s+([a-z]\\w+)\\s*\\(([^\\)]*)\\)";
    private static Pattern pattern = Pattern.compile(FUNCTION_REGEXP, Pattern.MULTILINE);

    /** Keeps track of argument counts for each method invocation expression */
    private static int expressionArgCount = 0;

    /**
     * Constructor. Initializes this instance with the specified tokenizer
     * 
     * @param tokenizer         the tokenizer to set for this engine
     * @param codeWriter        the code writer for this engine
     */
    public CodeCompilationEngine(Tokenizer tokenizer, VMCodeWriter codeWriter) {
        this.tokenizer = tokenizer;
        this.codeWriter = codeWriter;

        lookahead = tokenizer.nextToken();
        currentToken = lookahead;
        
        StandardLibrary.getInstance().initialize();
    }


    /**
     * Advances the lookahead to the next token
     */
    private void advance() {
        lookahead = tokenizer.nextToken();
    }


    /**
     * Matches the type of the given token
     * 
     * @param token             the token whose type to match
     */
    private void matchType(Token token) {
        if(token.getType() == TokenType.KEYWORD) {
            match(TokenType.KEYWORD);
        } else {
            match(TokenType.IDENTIFIER);
        }
    }
    

    /**
     * Matches the given token type
     * 
     * @param type              the token type to match
     */
    private void match(TokenType type) {
        if(lookahead.getType() == type) {
            currentToken = lookahead;
            advance();
        } else {
            throw new Error(String.format("Expecting %s; found '%s'\n", type.text(), lookahead.getText()));
        }
    }

    
    /**
     * Returns the symbol table for this compilation engine
     * 
     * @return the symbol table for this compilation engine
     */
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
    
    /**
     * Returns the generated code for this compilation engine
     * 
     * @return the generated code for this compilation engine
     */
    public String generateCode() {
        return output.toString();
    }


    /**
     * Compiles a complete class
     * 
     * class: 'class' className '{' classVarDec* subroutineDec* '}'
     */
    public void compileClass() {
        
        if(lookahead.getType() != TokenType.KEYWORD) {
           throw new Error("This code does not seem to contain a valid class declaration.");
        }

        match(TokenType.KEYWORD);
        match(TokenType.IDENTIFIER);
        
        /** Define the top-level scope for the current class */
        className = currentToken.getText();
        symbolTable.startClass(className);

        /** Match the opening curly brace */
        match(TokenType.SYMBOL);

        /** Compile class var declarations and subroutines */
        compileClassVarDec();
        compileSubroutine();

        /** Match the closing curly brace */
        match(TokenType.SYMBOL);

        codeWriter.close();
    }


    /**
     * Compiles a static declaration or a field declaration
     * 
     * classVarDec: ('static' | 'field') type varName(',' varName)* ';'
     * 
     * type: 'int' | 'char' | 'boolean' | className
     * 
     * className: identifier
     * 
     * varName: identifier
     */
    public void compileClassVarDec() {

        while(Grammar.predictsClassVarDeclarationFrom(lookahead)) {
            String type;
            IdentifierKind kind;
            String identifier;
            
            match(TokenType.KEYWORD);
            kind = IdentifierKind.fromText(currentToken.getText());
            matchType(lookahead);
            type = currentToken.getText();
            match(TokenType.IDENTIFIER);
            identifier = currentToken.getText();

            symbolTable.define(identifier, type, kind);

            /** Check whether there is a comma-separated list of variables */
            while (Grammar.predictsCommaFrom(lookahead)) {
                match(TokenType.SYMBOL);
                match(TokenType.IDENTIFIER);
                identifier = currentToken.getText();
                symbolTable.define(identifier, type, kind);
            }

            match(TokenType.SYMBOL);
        }

    }

    
    /**
     * Compiles a complete method, function, or constructor
     * 
     * subroutineDec:   ('constructor' | 'function' | 'method') 
     *                  ('void' | type) subroutineName '(' parameterList ')' 
     *                  subroutineBody
     * 
     * subroutineBody: '{' varDec* statements '}'
     * 
     * subroutineName: identifier
     */
    public void compileSubroutine() {
        /**
         * The current logic uses a two-pass approach: first, it iterates over all the methods in the current class
         * and registers their respective symbols (name, type, argument count, etc). This step allows for forward reference of
         * those methods. Second, it proceeds with the normal compilation of those methods.
         */
        Matcher functionMatcher = pattern.matcher(this.tokenizer.getInput());

        while (functionMatcher.find()) {
            MatchResult currentMatch = functionMatcher.toMatchResult();
            String subroutineType = currentMatch.group(1);
            String returnType = currentMatch.group(2);
            String methodName = currentMatch.group(3);
            String qualifiedName = String.format("%s.%s", className, methodName);
            String arguments = currentMatch.group(4);
            int argumentCount = (arguments.length() == 0) ? 0 : arguments.split(",").length;

            /** 
             * Define the current method in the class scope, and store its attributes such as argument count and return type
             */
            Map<String,Object> attributes = new HashMap<>();
            attributes.put("type", subroutineType);
            attributes.put("returnType", returnType);
            attributes.put("qualifiedName", qualifiedName);
            attributes.put("argumentCount", argumentCount);
            symbolTable.define(methodName, qualifiedName, IdentifierKind.SUBROUTINE, new JSONObject(attributes));
        }

        while(Grammar.predictsSubroutineFrom(lookahead)) {
            /** Process the subroutine header declaration */
            match(TokenType.KEYWORD);
            matchType(lookahead);
            match(TokenType.IDENTIFIER);

            /** Start a subroutine scope for the current method, setting the "this" keyword to the current class */
            String subroutineName = currentToken.getText();

            Identifier identifier = symbolTable.currentClassScope().resolve(subroutineName);
            boolean isConstructor = false;
            boolean isMethod = false;
            
            if (identifier != null) {
                JSONObject attributes = identifier.getAttributes();

                if (attributes.has("type")) {
                    isConstructor = attributes.getString("type").equals("constructor");
                    isMethod = attributes.getString("type").equals("method");
                }
            }

            /** Start a new function scope for the current subroutine */
            symbolTable.startSubroutine(subroutineName);
            ifCounts.put(subroutineName, 0);
            whileCounts.put(subroutineName, 0);

            /** Process parameter list */
            match(TokenType.SYMBOL);

            /**
             * If this is a method, define "this" as the first argument so that other arguments can be offset by 1
             */
            if (isMethod) {
                symbolTable.define("this", className, IdentifierKind.ARGUMENT);
            }

            compileParameterList();
            match(TokenType.SYMBOL);

            /** Process the subroutine body */
            match(TokenType.SYMBOL);
            
            /** Process variable declarations */
            compileVarDec();

            /** Output the VM code for the current subroutine along with its number of local variables */
            Scope scope = symbolTable.currentSubroutineScope();
            int localVariables = scope.varCount(IdentifierKind.VAR);
            codeWriter.writeFunction(String.format("%s.%s", className, subroutineName), localVariables);

            if (isConstructor) {
                Scope classScope = symbolTable.currentClassScope();
                int fields = classScope.varCount(IdentifierKind.FIELD);
                codeWriter.initializeConstructor(fields);
            } else if (isMethod) {
                codeWriter.initializeThis();
            }

            /** Process statements, if any */
            if(Grammar.predictsStatementsFrom(lookahead)) {
                outputStatements();
            }

            match(TokenType.SYMBOL);
        }
 
    }
    
    
    /**
     * Outputs code for the current set of statements
     */
    private void outputStatements() {
        while(Grammar.predictsStatementsFrom(lookahead)) {
            compileStatements();
        }
    }


    /**
     * Compiles a (possibly empty) parameter list, not including the enclosing "()"
     * 
     * parameterList: ( (type varName) (',' type varName)* )?
     */
    public void compileParameterList() {
        
        while(!Grammar.predictsClosingParenthesisFrom(lookahead)) {
            String paramType;
            String paramID;
            
            matchType(lookahead);
            paramType = currentToken.getText();
            match(TokenType.IDENTIFIER);
            paramID = currentToken.getText();

            symbolTable.define(paramID, paramType, IdentifierKind.ARGUMENT);

            while (Grammar.predictsCommaFrom(lookahead)) {
                match(TokenType.SYMBOL);
                matchType(lookahead);
                paramType = currentToken.getText();
                match(TokenType.IDENTIFIER);
                paramID = currentToken.getText();

                symbolTable.define(paramID, paramType, IdentifierKind.ARGUMENT);
            }
        }
    }


    /**
     * Compiles a "var" declaration
     * 
     * varDec: 'var' type varName (',' varName)* ';'
     */
    public void compileVarDec() {

        while (Grammar.predictsVarDeclarationFrom(lookahead)) {
            String type;
            String identifier;

            match(TokenType.KEYWORD);
            matchType(lookahead);
            type = currentToken.getText();
            match(TokenType.IDENTIFIER);
            identifier = currentToken.getText();
            
            symbolTable.define(identifier, type, IdentifierKind.VAR);

            while (Grammar.predictsCommaFrom(lookahead)) {
                match(TokenType.SYMBOL);
                match(TokenType.IDENTIFIER);
                identifier = currentToken.getText();
                symbolTable.define(identifier, type, IdentifierKind.VAR);
            }

            match(TokenType.SYMBOL);
        }

    }


    /**
     * Compiles a sequence of statements, not including the enclosing "{}"
     * 
     * statements: statement*
     * 
     * statement: letStatement
     *            | ifStatement
     *            | whileStatement
     *            | doStatement
     *            | returnStatement
     *            ;
     */
    public void compileStatements() {
        Keyword keyword = Keyword.fromText(lookahead.getText());

        switch(keyword) {

            case LET:
                compileLet();
                break;

            case IF:
                compileIf();
                break;

            case WHILE:
                compileWhile();
                break;

            case DO:
                compileDo();
                break;

            default:
                compileReturn();
                break;

        }

    }


    /**
     * Compiles a let statement
     * 
     * letStatement: 'let' varName ('[' expression ']')? '=' expression ';'
     */
    public void compileLet() {
        match(TokenType.KEYWORD);
        match(TokenType.IDENTIFIER);
        String identifierName = currentToken.getText();
        Identifier identifier = symbolTable.currentSubroutineScope().resolve(identifierName);
        boolean isArrayExpression = false;
        
        /** Check whether it is an array property access statement (i.e let a[i] = j) */
        if(Grammar.predictsArrayEntryFrom(lookahead)) {
            isArrayExpression = true;
            Identifier arrayVariable = symbolTable.currentSubroutineScope().resolve(currentToken.getText());
            
            if (arrayVariable == null) {
                throw new Error(String.format("Unable to find symbol '%s'\n", currentToken.getText()));
            }

            match(TokenType.SYMBOL);
            compileExpression();

            /** Output code for a[i], which can be rewritten as *(a + i) */
            String segment = segmentFromIdentifier(arrayVariable);
            codeWriter.writeToFile(String.format("push %s %s\nadd\n", segment, arrayVariable.getIndex()));

            match(TokenType.SYMBOL);
        }

        /** Match the "=" sign */
        match(TokenType.SYMBOL);
        compileExpression();
        String output = "\n";

        if (identifier == null) {
            output = String.format("<Exception: Could not find the following symbol: '%s'>\n", identifierName);
        } else {
            if (isArrayExpression) {
                /**
                 * For an array expression such as "a[i] = j", which is equivalent to *(a + i) = j, the result of
                 * evaluating (a + i) has already been pushed onto the stack. And since we're also pushing the result
                 * of evaluating j, we need to pop that result to a temp segment in order to access the memory
                 * pointer for "that", which is used to store a[i].
                 */
                StringBuilder builder = new StringBuilder();
                builder.append("pop temp 0\n")
                       .append("pop pointer 1\n")
                       .append("push temp 0\n")
                       .append("pop that 0\n");
                output = builder.toString();
            } else {
                String segment = segmentFromIdentifier(identifier);
                output = String.format("pop %s %s\n", segment, identifier.getIndex());
            }
        }

        codeWriter.writeToFile(output);

        match(TokenType.SYMBOL);
    }


    /**
     * Compiles an if statement, possibly with a trailing "else" clause
     * 
     * ifStatement: 'if' '(' expression ')' '{' statements '}' 
     *              ('else' '{' statements '}')?
     * 
     * Flow:
     * 
     *     compute expression
     *     if-goto IF_TRUE0
     *     goto IF_FALSE0
     *     label IF_TRUE0
     *     ... output statements for IF condition
     *     goto IF_END0
     *     label IF_FALSE0
     *     ... output statements for ELSE condition, if any
     *     label IF_END0
     */
    public void compileIf() {
        String currentSubroutine = symbolTable.currentSubroutineScope().getName();
        int ifIndex = (int) ifCounts.get(currentSubroutine);
        int sequence = ifIndex;
        ifCounts.put(currentSubroutine, ++ifIndex);

        String ifTrue = String.format("IF_TRUE%s", sequence);
        String ifFalse = String.format("IF_FALSE%s", sequence);
        String ifEnd = String.format("IF_END%s", sequence);

        match(TokenType.KEYWORD);
        match(TokenType.SYMBOL);

        /**
         * Evaluate the expression and output code for the 'if' condition
         */
        compileExpression();
        codeWriter.writeIfGoto(ifTrue);

        /** 
         * Output the code for jumping to the 'else' clause, even if there is none. It will simply
         * contain an empty statement list in that case.
         */
        codeWriter.writeGoto(ifFalse);

        codeWriter.writeLabel(ifTrue);
        match(TokenType.SYMBOL);
        match(TokenType.SYMBOL);
  
        if(Grammar.predictsStatementsFrom(lookahead)) {
            outputStatements();
        }

        match(TokenType.SYMBOL);

        /**
         * Output the "goto IF_END" label prior to outputting the "IF_FALSE" statement. This is
         * needed to handle nested "if/else" statements.
         */
        codeWriter.writeGoto(ifEnd);

        /** Output the statements for the 'else' clause, if any */
        codeWriter.writeLabel(ifFalse);

        if(Grammar.predictsElseClauseFrom(lookahead)) {
            match(TokenType.KEYWORD);
            match(TokenType.SYMBOL);

            if(Grammar.predictsStatementsFrom(lookahead)) {
                outputStatements();
            }

            match(TokenType.SYMBOL);
        }

        /** Output the end for this "if/else" statement */
        codeWriter.writeLabel(ifEnd);
    }


    /**
     * Compiles a while statement
     * 
     * whileStatement: 'while' '(' expression ')' '{' statements '}'
     * 
     * Flow:
     * 
     * label WHILE_EXP
     *     compute expression
     *     not
     *     if-goto WHILE_END
     *     compute statements
     *     goto WHILE_EXP
     * label WHILE_END
     * 
     */
    public void compileWhile() {
        String currentSubroutine = symbolTable.currentSubroutineScope().getName();
        int whileIndex = (int) whileCounts.get(currentSubroutine);
        int sequence = whileIndex;
        whileCounts.put(currentSubroutine, ++whileIndex);

        String startLabel = String.format("WHILE_EXP%s", sequence);
        String endLabel = String.format("WHILE_END%s", sequence);

        match(TokenType.KEYWORD);
        match(TokenType.SYMBOL);

        codeWriter.writeLabel(startLabel);
        compileExpression();
        codeWriter.writeUnaryOperator("~");
        codeWriter.writeIfGoto(endLabel);
        match(TokenType.SYMBOL);
        match(TokenType.SYMBOL);

        if(Grammar.predictsStatementsFrom(lookahead)) {
            outputStatements();
        }

        codeWriter.writeGoto(startLabel);
        codeWriter.writeLabel(endLabel);

        match(TokenType.SYMBOL);
    }


    /**
     * Compiles a do statement
     * 
     * doStatement: 'do' subroutineCall ';'
     */
    public void compileDo() {
        match(TokenType.KEYWORD);
        subroutineCall();

        /**
         * Since a "do" statement doesn't expect a return value, pop the default return value ("0")
         * from the stack
         */
        codeWriter.writePop(Segment.TEMP, 0);
        match(TokenType.SYMBOL);
    }


    /**
     * Compiles a subroutine call
     * 
     * subroutineCall: subroutineName '(' expressionList ')'
     *                 | (className | varName) '.' subroutineName '(' expressionList ')'
     *                 ;
     */
    private void subroutineCall() {
        match(TokenType.IDENTIFIER);
        String className = symbolTable.currentClassScope().getName();
        String objectName = className;
        String functionName = currentToken.getText();

        /** Check whether it is a method invocation on an object (Object.fn()) */
        if (Grammar.predictsDotFrom(lookahead)) {
            objectName = currentToken.getText();
            match(TokenType.SYMBOL);
            match(TokenType.IDENTIFIER);
            functionName = currentToken.getText();
        }

        boolean isFromCurrentClass = objectName.equals(className);
        String subroutineName = !StringUtils.isEmpty(objectName) ? String.format("%s.%s", objectName, functionName) :
                functionName;
        boolean isMethod = false;
        boolean isInstanceVariable = false;
        JSONObject attributes = new JSONObject();
        Identifier identifier = null;

        if (isFromCurrentClass) {
            identifier = symbolTable.currentClassScope().resolve(functionName);
        }

        /**
         * Check whether it is a method of the current class and, if so, push "this" as the first argument
         * to the invocation
         */
        if (isFromCurrentClass) {
            if (identifier == null) {
                throw new Error(String.format("Method '%s' not found in this class\n", subroutineName));
            }

            attributes = identifier.getAttributes();
            isMethod = (attributes != null && attributes.getString("type").equals("method"));
        }

        if (isMethod) {
            codeWriter.writeThisReference();
        } else {
            /** 
             * Check whether we're calling a method on an instance variable (for instance, game.run()) and, if so,
             * resolve the identifier to the appropriate variable
             */
            identifier = symbolTable.currentSubroutineScope().resolve(objectName);
            isInstanceVariable = (
                (identifier != null) &&
                (identifier.getKind() == IdentifierKind.VAR ||
                 identifier.getKind() == IdentifierKind.FIELD ||
                 identifier.getKind() == IdentifierKind.STATIC)
            );
        }
        
        /** 
         * If we're calling a method on an instance variable, we first need to push its reference, which
         * will point to the 'this' keyword.
         */
        if (isInstanceVariable) {
            String segment = segmentFromIdentifier(identifier);
            String output = String.format("push %s %s\n", segment, identifier.getIndex());
            codeWriter.writeToFile(output);
        }

        match(TokenType.SYMBOL);
        compileExpressionList();
        match(TokenType.SYMBOL);

        /** 
         * Check whether we're using OS methods and, if so, retrieve the expected argument count; otherwise, resolve
         * the subroutine in the scope chain. Then output the VM code.
         */
        if (StandardLibrary.getInstance().isStandardLibraryObject(objectName)) {
            Integer argumentCount = StandardLibrary.getInstance().getExpectedArguments(objectName, functionName);
            codeWriter.writeCall(subroutineName, argumentCount.intValue());
        } else {
            if (identifier != null) {
                if (isMethod) {
                    int argumentCount = attributes.getInt("argumentCount");
                    argumentCount = isMethod? argumentCount + 1 : argumentCount;
                    codeWriter.writeCall(identifier.getType(), argumentCount);
                } else if (isInstanceVariable) {
                    /**
                     * This is an instance variable, and we've already pushed the reference to 'this' onto the stack.
                     * So we need to increment the argument count by 1.
                     */
                    codeWriter.writeCall(String.format("%s.%s", identifier.getType(), functionName), expressionArgCount + 1);
                }
            } else {
              /** Default case: simply output the method call as "call object.methodName args" */
              codeWriter.writeCall(String.format("%s.%s", objectName, functionName), expressionArgCount);
            }
        }
    }


    /**
     * Compiles a return statement
     * 
     * returnStatement: 'return' expression? ';'
     */
    public void compileReturn() {
        match(TokenType.KEYWORD);

        /**
         * If there is no expression after the "return" keyword, then push "0" to the stack. Otherwise, compile the 
         * expression. 
         */
        if (Grammar.predictsSemicolonFrom(lookahead)) {
            match(TokenType.SYMBOL);
            codeWriter.writePushConstant(0);
            codeWriter.writeReturn();
        } else {
            if (lookahead.getText().equals(Keyword.THIS.toString())) {
                /** Handle the case for 'return this' */
                match(TokenType.KEYWORD);
                codeWriter.writeThisReference();
            } else {
              compileExpression();
            }

            match(TokenType.SYMBOL);
            codeWriter.writeReturn();
        }
    }


    /**
     * Compiles an empty or comma-separated list of expressions
     * 
     * expressionList: ( expression ( ',' expression)* )? ;
     */
    public void compileExpressionList() {
        /** 
         * Reset the argument count for each expression list. This is needed to output the argument
         * count in each "call methodName args" instruction.
         */
        expressionArgCount = 0;

        while(!Grammar.predictsClosingParenthesisFrom(lookahead)) {
            compileExpression();
            expressionArgCount++;

            while (Grammar.predictsCommaFrom(lookahead)) {
                match(TokenType.SYMBOL);
                compileExpression();
                expressionArgCount++;
            }
        }
    }


    /**
     * Compiles an expression
     * 
     * expression: term (op term)*
     * 
     */
    public void compileExpression() {
        compileTerm();
        
        /** Check whether the next token is an operator and, if so, compile the additional expression(s) */
        while(Grammar.isOperator(lookahead.getText())) {
            String operator = lookahead.getText();
            match(TokenType.SYMBOL);
            compileTerm();
            codeWriter.writeOperator(operator);
        }
    }
    
    
    /**
     * Compiles a term. If the current token is an identifier, this method uses the lookahead token in order to 
     * decide between the three alternative parsing rules ("[" implies an array entry, "(" implies a method call, 
     * and "." implies a property (variable). Any token that is not part of this term should not be advanced over.
     * 
     * term: integerConstant
     *       | stringConstant
     *       | keywordConstant
     *       | varName
     *       | varName '[' expression ']'
     *       | subroutineCall
     *       | '(' expression ')'
     *       | unaryOpterm
     *       ;
     * 
     * subroutineCall: subroutineName '(' expressionList ')'
     *                 | (className | varName) '.' subroutineName '(' expressionList ')'
     *                 ;
     * 
     * expressionList: ( expression ( ',' expression)* )?
     *
     * op: '+' | '-' | '*' | '/' | '&' | '|' | '<' | '>' | '='
     *
     * unaryOp: '-' | '~'
     *
     * keywordConstant: 'true' | 'false' | 'null' | 'this'
     */
    public void compileTerm() {
        /** Handle the case for terminal elements or keyword constants */
        if(Grammar.isTerminal(lookahead.getText()) || Grammar.isKeywordConstant(lookahead.getText())) {
            match(lookahead.getType());

            if (Grammar.isKeywordConstant(currentToken.getText())) {
                codeWriter.writeKeywordConstant(currentToken.getText());
            }

            return;
        }

        /** Handle the case for unary operators */
        if (Grammar.isUnaryOperator(lookahead.getText())) {
            match(TokenType.SYMBOL);
            boolean isNegationSymbol = Grammar.isMinusSign(currentToken);

            compileTerm();

            codeWriter.writeUnaryOperator(!isNegationSymbol ? "~" : "-");
            return;
        }
        
        /** Handle the case for parentheses */
        if (Grammar.predictsOpeningParenthesisFrom(lookahead)) {
            match(TokenType.SYMBOL);
            compileExpression();
            match(TokenType.SYMBOL);
            return;
        }
        
        /** 
         * At this stage, we're dealing with an identifier, which offers three alternatives: a variable name, an
         * array entry, or a method invocation. Take the appropriate action based on the value of the lookahead token.
         */
        match(lookahead.getType());
        String variable = currentToken.getText();
        
        boolean isMethodInvocation = (Grammar.predictsDotFrom(lookahead) || 
                Grammar.predictsOpeningParenthesisFrom(lookahead));
        
        if(Grammar.predictsArrayEntryFrom(lookahead)) {
            Identifier arrayVariable = symbolTable.currentSubroutineScope().resolve(currentToken.getText());

            if (arrayVariable == null) {
                throw new Error(String.format("Unable to find symbol '%s'\n", currentToken.getText()));
            }
  
            match(TokenType.SYMBOL);
            compileExpression();
            match(TokenType.SYMBOL);

            /** Output code for the array access expression (right hand side) of an expression such as let b = a[i] */
            String segment = segmentFromIdentifier(arrayVariable);
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("push %s %s\n", segment, arrayVariable.getIndex()))
                   .append("add\n")
                   .append("pop pointer 1\n")
                   .append("push that 0\n");
            codeWriter.writeToFile(builder.toString());

        } else if(isMethodInvocation) {
            /**
             * A method invocation can be a fully qualified string such as "Object.method(args)", or an
             * implicit string such as "method(args)". In the former case, we need to resolve the target object,
             * which can be the current class or an instance variable. To handle those use cases, the current
             * logic first initializes the target object to the current class; it then adjusts it to the 
             * appropriate object if the next token is a dot.
             */
            String className = symbolTable.currentClassScope().getName();
            String object = className;
            String methodName = currentToken.getText();

            if(Grammar.predictsDotFrom(lookahead)) {
                if (!currentToken.getText().equals("this") || !currentToken.getText().equals(className)) {
                    object = currentToken.getText();
                }
                
                match(TokenType.SYMBOL);
                match(TokenType.IDENTIFIER);
                methodName = currentToken.getText();
            }

            /**
             * Resolve the method name in the current class scope only if it is a class method
             */
            Identifier identifier = null;
            Identifier instanceIdentifier = null;
            
            if (object.equals(className)) {
                identifier = symbolTable.currentClassScope().resolve(methodName);
            } else {
                instanceIdentifier = symbolTable.currentSubroutineScope().resolve(object);
            }

            /** 
             * If we're calling a method on an instance variable, we first need to push its reference, which
             * will point to the 'this' keyword.
             */
            if (instanceIdentifier != null) {
                String segment = segmentFromIdentifier(instanceIdentifier);
                String output = String.format("push %s %s\n", segment, instanceIdentifier.getIndex());
                codeWriter.writeToFile(output);
            }

            match(TokenType.SYMBOL);
            compileExpressionList();
            match(TokenType.SYMBOL);

            if (identifier != null) {
                int argumentCount = identifier.getAttributes().getInt("argumentCount");
                codeWriter.writeCall(identifier.getType(), argumentCount);
            } else {
                /**
                 * Check whether it is an OS method invocation and, if so, output it accordingly
                 */
                boolean isOSMethod = StandardLibrary.getInstance().isStandardLibraryObject(object);

                if (isOSMethod) {
                    Integer argumentCount = StandardLibrary.getInstance().getExpectedArguments(object, methodName);
                    methodName = String.format("%s.%s", object, methodName);
                    codeWriter.writeCall(methodName, argumentCount.intValue());
                } else {
                  /**
                     * At this point, it is safe to assume that we're dealing with a method invocation such such 
                     * as "SquareGame.new()" or "ball.move()". 
                   */
                  if (instanceIdentifier != null) {
                        codeWriter.writeCall(String.format("%s.%s", instanceIdentifier.getType(), methodName),
                                expressionArgCount + 1);
                  } else {
                      methodName = String.format("%s.%s", object, methodName);
                      codeWriter.writeCall(methodName, expressionArgCount);
                  }
                }
            }

        } else {
            boolean isString = currentToken.getType() == TokenType.STRING_CONSTANT;
            boolean isInteger = currentToken.getType() == TokenType.INT_CONSTANT;

            if (isString) {
                codeWriter.writeStringConstant(variable);
            } else if (isInteger) {
                codeWriter.writePushConstant(Integer.parseInt(variable));
                
            } else {
                Identifier identifier = symbolTable.currentSubroutineScope().resolve(variable);

                if (identifier != null) {
                    String segment = segmentFromIdentifier(identifier);
                    String output = String.format("push %s %s\n", segment, identifier.getIndex());
                    codeWriter.writeToFile(output);
                } else {
                    /** Last resort: just output a string */
                    codeWriter.writeStringConstant(variable);
                }
            }
        }
    }


    /**
     * Returns the segment for the specified identifier, or an empty string if its kind is unknown
     * 
     * @param identifier      the identifier for which to return the segment
     * @return the segment for the specified identifier, or an empty string if its kind is unknown
     */
    private String segmentFromIdentifier(Identifier identifier) {
      return (identifier.getKind() == IdentifierKind.FIELD)? "this" :
                    (identifier.getKind() == IdentifierKind.VAR)? "local" :
                    (identifier.getKind() == IdentifierKind.ARGUMENT)? "argument" :
                    (identifier.getKind() == IdentifierKind.STATIC)? "static" : "";
    }

}
