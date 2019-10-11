
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
import com.akwabasystems.utils.VMUtils;
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
    private final Map<String,Integer> whileCounts  = new ConcurrentHashMap<>();

    /**
     * Matches the following:
     * - function void main()
     * - constructor void Main()
     * - method Main main(int a, int b, int c)
     */
    private static final String FUNCTION_REGEXP = "(?:function|constructor|method)\\s+(void|\\w+)\\s+([a-z]\\w+)\\s*\\(([^\\)]*)\\)";
    private static Pattern pattern = Pattern.compile(FUNCTION_REGEXP, Pattern.MULTILINE);


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
            throw new Error(String.format("Expecting '%s'; found '%s'\n", type.text(), lookahead.getText()));
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
            String methodName = currentMatch.group(2);
            String methodType = String.format("%s.%s", className, methodName);
            String arguments = currentMatch.group(3);
            int argumentCount = (arguments.length() == 0) ? 0 : arguments.split(",").length;
            
            /** 
             * Define the current method in the class scope, and store its attributes such as argument count and return type
             */
            Map<String,Object> attributes = new HashMap<>();
            attributes.put("argumentCount", argumentCount);
            symbolTable.define(methodName, methodType, IdentifierKind.METHOD, new JSONObject(attributes));
        }

        while(Grammar.predictsSubroutineFrom(lookahead)) {
            /** Process the subroutine header declaration */
            match(TokenType.KEYWORD);
            matchType(lookahead);
            match(TokenType.IDENTIFIER);

            /** Start a subroutine scope for the current method, setting the "this" keyword to the current class */
            String subroutineName = currentToken.getText();

            /** Start a new function scope for the current subroutine */
            symbolTable.startSubroutine(subroutineName);
            ifCounts.put(subroutineName, 0);
            whileCounts.put(subroutineName, 0);

            /** Process parameter list */
            match(TokenType.SYMBOL);
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
        
        /** Check whether it is an array property access statement (i.e let a[i] = j) */
        if(Grammar.predictsArrayEntryFrom(lookahead)) {
            match(TokenType.SYMBOL);
            compileExpression();
            match(TokenType.SYMBOL);
        }

        /** Match the "=" sign */
        match(TokenType.SYMBOL);
        compileExpression();
        String output = "\n";

        if (identifier == null) {
            output = String.format("<Exception: Could not find the following symbol: '%s'>\n", identifierName);
        } else {
            String segment = (identifier.getKind() == IdentifierKind.FIELD)? "this" :
                (identifier.getKind() == IdentifierKind.VAR)? "local" :
                (identifier.getKind() == IdentifierKind.ARGUMENT)? "argument" :
                (identifier.getKind() == IdentifierKind.STATIC)? "static" : "";
            
            output = String.format("pop %s %s\n", segment, identifier.getIndex());
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

        String ifLabel = String.format("IF_TRUE%s", sequence);
        String elseLabel = String.format("IF_FALSE%s", sequence);
        String endLabel = String.format("IF_END%s", sequence);

        match(TokenType.KEYWORD);
        match(TokenType.SYMBOL);

        /**
         * Evaluate the expression and output code for the 'if' condition
         */
        compileExpression();
        codeWriter.writeIfGoto(ifLabel);

        /** 
         * Output the code for jumping to the 'else' clause, even if there is none. It will simply
         * contain an empty statement list in that case.
         */
        codeWriter.writeGoto(elseLabel);

        codeWriter.writeLabel(ifLabel);
        match(TokenType.SYMBOL);
        match(TokenType.SYMBOL);
  
        if(Grammar.predictsStatementsFrom(lookahead)) {
            outputStatements();
        }

        match(TokenType.SYMBOL);

        /** End the 'if' clause by jumping to the 'end' label */
        codeWriter.writeGoto(endLabel);

        /** Output the statements for the 'else' clause, if any */
        codeWriter.writeLabel(elseLabel);

        if(Grammar.predictsElseClauseFrom(lookahead)) {
            match(TokenType.KEYWORD);
            match(TokenType.SYMBOL);

            if(Grammar.predictsStatementsFrom(lookahead)) {
                outputStatements();
            }

            match(TokenType.SYMBOL);
        }

        /** End the 'if/else' clause */
        codeWriter.writeLabel(endLabel);
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
        whileCounts.put(currentSubroutine, whileIndex++);

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
        String objectName = "";
        String functionName = currentToken.getText();

        /** Check whether it is a method invocation on an object (Object.fn()) */
        if (Grammar.predictsDotFrom(lookahead)) {
            objectName = currentToken.getText();
            match(TokenType.SYMBOL);
            match(TokenType.IDENTIFIER);
            functionName = currentToken.getText();
        }

        boolean isClassMethod = objectName.equals(symbolTable.currentClassScope().getName());
        String subroutineName = !StringUtils.isEmpty(objectName) ? String.format("%s.%s", objectName, functionName) :
                functionName;

        Identifier identifier = symbolTable.currentClassScope().resolve(functionName);

        if (isClassMethod && identifier == null) {
            throw new Error(String.format("Method '%s' not found in this class\n", subroutineName));
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
                int argumentCount = identifier.getAttributes().getInt("argumentCount");
                codeWriter.writeCall(identifier.getType(), argumentCount);
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
        if(Grammar.predictsSymbolFrom(lookahead)) {
            match(TokenType.SYMBOL);
            codeWriter.writePushConstant(0);
            codeWriter.writeReturn();
        } else {
            compileExpression();
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
        while(!Grammar.predictsClosingParenthesisFrom(lookahead)) {
            compileExpression();

            while (Grammar.predictsCommaFrom(lookahead)) {
                match(TokenType.SYMBOL);
                compileExpression();
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
        /** Check whether this is a terminal element or keyword constant */
        if(Grammar.isTerminal(lookahead.getText()) || Grammar.isKeywordConstant(lookahead.getText())) {
            match(lookahead.getType());

            if (Grammar.isKeywordConstant(currentToken.getText())) {
                codeWriter.writeKeywordConstant(currentToken.getText());
            }

            return;
        }

        /** Handle the case for unary operators */
        if(Grammar.isUnaryOperator(lookahead.getText())) {
            match(TokenType.SYMBOL);
            boolean isNegativeNumber = Grammar.isMinusSign(currentToken) && VMUtils.isNumber(lookahead.getText());

            compileTerm();

            if (isNegativeNumber) {
                codeWriter.writeUnaryOperator("-");
            } else {
                codeWriter.writeUnaryOperator("~");
            }

            return;
        }
        
        /** Handle the case for parentheses */
        if(Grammar.predictsOpeningParenthesisFrom(lookahead)) {
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
            match(TokenType.SYMBOL);
            compileExpression();
            match(TokenType.SYMBOL);
        } else if(isMethodInvocation) {
            /**
             * A method invocation can be a fully qualified string such as "Object.method(args)", or an
             * implicit string such as "method(args)". In the latter case, we need to resolve the target object to the
             * current class. To handle both use cases, the current logic initializes the target object to the
             * current class, and it adjusts it to the specified object if the next token is a dot and the
             * left side of the dot is not "this".
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
             * Resolve the method in the class scope
             */
            Identifier identifier = symbolTable.currentClassScope().resolve(methodName);

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
                }
            }

        } else {
            if (VMUtils.isNumber(variable)) {
                codeWriter.writePushConstant(Integer.parseInt(variable));
                
            } else {
                Identifier identifier = symbolTable.currentSubroutineScope().resolve(variable);

                if (identifier != null) {
                    String segment = (identifier.getKind() == IdentifierKind.FIELD) ? "this" : 
                        (identifier.getKind() == IdentifierKind.ARGUMENT) ? "argument" : 
                        (identifier.getKind() == IdentifierKind.VAR) ? "local" : "";
                    String output = String.format("push %s %s\n", segment, identifier.getIndex());
                    codeWriter.writeToFile(output);
                }
            }
        }

    }

}
