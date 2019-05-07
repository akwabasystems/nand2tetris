
package com.akwabasystems.parsing;

import com.akwabasystems.model.Grammar;
import com.akwabasystems.model.Identifier;
import com.akwabasystems.model.IdentifierKind;
import com.akwabasystems.model.Keyword;
import com.akwabasystems.model.Scope;
import com.akwabasystems.model.SymbolTable;
import com.akwabasystems.model.Token;
import com.akwabasystems.model.TokenType;
import com.akwabasystems.utils.VMUtils;


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
    private Tokenizer tokenizer;
    private Token lookahead;
    private Token currentToken;
    private String className;
    private SymbolTable symbolTable = new SymbolTable();
    private VMCodeWriter codeWriter;
    private final StringBuilder output = new StringBuilder();


    /**
     * Constructor. Initializes this instance with the specified tokenizer
     * 
     * @param tokenizer         the tokenizer to set for this engine
     */
    public CodeCompilationEngine(Tokenizer tokenizer, VMCodeWriter codeWriter) {
        this.tokenizer = tokenizer;
        this.codeWriter = codeWriter;

        lookahead = tokenizer.nextToken();
        currentToken = lookahead;
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
            
            if(Grammar.isTerminal(type.text())) {
                // output.append(currentToken.toXML()).append("\n");
            }

            advance();

        } else {
            throw new Error(String.format("Expecting %s; found '%s'\n", type.text(), lookahead.getText()));
        }
    }

    
    /**
     * Returns the symbol table for this compilation engine
     * 
     * @return the symbol 
     */
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
    
    /**
     * Returns the XML structure of the parsed code
     * 
     * @return the XML structure of the parsed code
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
        
        String nodeName = currentToken.getText();
        //output.append(String.format("<%s>\n", nodeName));

        match(TokenType.KEYWORD);
        match(TokenType.IDENTIFIER);
        
        /** Define the top-level scope for the current class */
        className = currentToken.getText();
        symbolTable.startClass(className);
        //codeWriter.writeClass(className);

        match(TokenType.SYMBOL);
 
        compileClassVarDec();
        System.out.println("STEP 1: Done processing class vars");
        //symbolTable.describe();
        
        compileSubroutine();
        System.out.println("STEP 2: Done processing subroutines");
        //symbolTable.describe();
        
        match(TokenType.SYMBOL);
        // output.append(String.format("</%s>", nodeName));

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
            //output.append("<classVarDec>\n");

            String type;
            IdentifierKind kind;
            String identifier;
            
            match(TokenType.KEYWORD);
            kind = IdentifierKind.fromText(currentToken.getText());
            matchType(lookahead);
            type = currentToken.getText();
            match(TokenType.IDENTIFIER);
            identifier = currentToken.getText();

            System.out.printf("Processed type: %s - kind: %s - identifier: %s\n", type, kind, identifier);
            symbolTable.define(identifier, type, kind);

            /** Check whether there is a comma-separated list of variables */
            while (Grammar.predictsCommaFrom(lookahead)) {
                match(TokenType.SYMBOL);
                match(TokenType.IDENTIFIER);
                identifier = currentToken.getText();
                symbolTable.define(identifier, type, kind);
                // System.out.printf("*** Additional var identifier: %s (type: %s, kind: %s)\n", identifier, type, kind);
            }

            match(TokenType.SYMBOL);

            System.out.println("--- Done processing class vars, symbolTable: " + symbolTable.toString());
            //output.append("</classVarDec>\n");
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
 
        while(Grammar.predictsSubroutineFrom(lookahead)) {
            //output.append("<subroutineDec>\n");

            /** Process the subroutine header declaration */
            match(TokenType.KEYWORD);
            matchType(lookahead);
            match(TokenType.IDENTIFIER);
            
            /** Start a subroutine scope for the current method, setting the "this" keyword to the current class */
            String subroutineName = currentToken.getText();
            symbolTable.startSubroutine(subroutineName);
            symbolTable.define("this", className, IdentifierKind.ARGUMENT);

            /** Process parameter list */
            match(TokenType.SYMBOL);
            compileParameterList();
            match(TokenType.SYMBOL);

            /** Process the subroutine body */
            //output.append("<subroutineBody>\n");
            match(TokenType.SYMBOL);

            /** Process variable declarations */
            compileVarDec();
            
            /** Output the VM code for the current subroutine and number of local variables */
            Scope scope = symbolTable.currentSubroutineScope();
            int localVariables = scope.varCount(IdentifierKind.VAR);
            codeWriter.writeFunction(String.format("%s.%s", className, subroutineName), localVariables);
            
            /** Initialize "this" to point to the current object */
            codeWriter.initializeThis();

            /** Process statements, if any */
            if(Grammar.predictsStatementsFrom(lookahead)) {
                outputStatements();
            }

            match(TokenType.SYMBOL);
            
            codeWriter.writeReturn();
            //output.append("</subroutineBody>\n");
            //output.append("</subroutineDec>\n");
        }
 
    }
    
    
    /**
     * Outputs the structure of the current set of statements
     */
    private void outputStatements() {
        //output.append("<statements>\n");

        while(Grammar.predictsStatementsFrom(lookahead)) {
            compileStatements();
        }

        //output.append("</statements>\n");
    }


    /**
     * Compiles a (possibly empty) parameter list, not including the enclosing "()"
     * 
     * parameterList: ( (type varName) (',' type varName)* )?
     */
    public void compileParameterList() {
        //output.append("<parameterList>\n");
        
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

        //output.append("</parameterList>\n");
    }


    /**
     * Compiles a "var" declaration
     * 
     * varDec: 'var' type varName (',' varName)* ';'
     */
    public void compileVarDec() {

        while (Grammar.predictsVarDeclarationFrom(lookahead)) {
            String type;
            IdentifierKind kind;
            String identifier;
            
            //output.append("<varDec>\n");
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
            //output.append("</varDec>\n");
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
        //output.append("<letStatement>\n");
        
        match(TokenType.KEYWORD);
        match(TokenType.IDENTIFIER);
        String identifierName = currentToken.getText();
        System.out.println("LET: Matched identifier: " + identifierName);
        
        Identifier identifier = symbolTable.currentSubroutineScope().resolve(identifierName);
        System.out.println("==== Resolved identifier: " + identifier);
        
        /** Check whether it is a property access statement (i.e let a[i] = j) */
        if(Grammar.predictsArrayEntryFrom(lookahead)) {
            match(TokenType.SYMBOL);
            compileExpression();
            match(TokenType.SYMBOL);
        }
        
        match(TokenType.SYMBOL);
        compileExpression();
        
        String output = "\n";

        if (identifier == null) {
            output = String.format("<Exception: Could not find the following symbol: '%s'>\n", identifierName);
        } else {
        
            if (identifier.getKind() == IdentifierKind.FIELD) {
                output = String.format("pop this %s\n", identifier.getIndex());
            }
        }

        codeWriter.writeToFile(output);

        match(TokenType.SYMBOL);

        //output.append("</letStatement>\n");
    }


    /**
     * Compiles an if statement, possibly with a trailing "else" clause
     * 
     * ifStatement: 'if' '(' expression ')' '{' statements '}' 
     *              ('else' '{' statements '}')?
     */
    public void compileIf() {
        //output.append("<ifStatement>\n");
        
        match(TokenType.KEYWORD);
        match(TokenType.SYMBOL);
        compileExpression();
        match(TokenType.SYMBOL);
        match(TokenType.SYMBOL);
        
        if(Grammar.predictsStatementsFrom(lookahead)) {
            outputStatements();
        }
        
        match(TokenType.SYMBOL);

        if(Grammar.predictsElseClauseFrom(lookahead)) {
            match(TokenType.KEYWORD);
            match(TokenType.SYMBOL);

            if(Grammar.predictsStatementsFrom(lookahead)) {
                outputStatements();
            }

            match(TokenType.SYMBOL);
        }

        //output.append("</ifStatement>\n");
    }


    /**
     * Compiles a while statement
     * 
     * whileStatement: 'while' '(' expression ')' '{' statements '}'
     */
    public void compileWhile() {
        //output.append("<whileStatement>\n");
        
        match(TokenType.KEYWORD);
        match(TokenType.SYMBOL);
        compileExpression();
        match(TokenType.SYMBOL);
        match(TokenType.SYMBOL);
        
        if(Grammar.predictsStatementsFrom(lookahead)) {
            outputStatements();
        }
        
        match(TokenType.SYMBOL);

        //output.append("</whileStatement>\n");
    }


    /**
     * Compiles a do statement
     * 
     * doStatement: 'do' subroutineCall ';'
     */
    public void compileDo() {
        //output.append("<doStatement>\n");

        match(TokenType.KEYWORD);
        subroutineCall();
        match(TokenType.SYMBOL);

        //output.append("</doStatement>\n");
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

        /** Check whether it is a method invocation on an object (Object.fn()) */
        if(Grammar.predictsDotFrom(lookahead)) {
            match(TokenType.SYMBOL);
            match(TokenType.IDENTIFIER);
        }

        match(TokenType.SYMBOL);
        compileExpressionList();
        match(TokenType.SYMBOL);
    }


    /**
     * Compiles a return statement
     * 
     * returnStatement: 'return' expression? ';'
     */
    public void compileReturn() {
        //output.append("<returnStatement>\n");
        match(TokenType.KEYWORD);

        if(Grammar.predictsSymbolFrom(lookahead)) {
            match(TokenType.SYMBOL);
        } else {
            compileExpression();
            match(TokenType.SYMBOL);
        }

        //output.append("</returnStatement>\n");
    }


    /**
     * Compiles a (possibly empty) comma-separated list of expressions
     * 
     * expressionList: ( expression ( ',' expression)* )? ;
     */
    public void compileExpressionList() {
        //output.append("<expressionList>\n");

        while(!Grammar.predictsClosingParenthesisFrom(lookahead)) {
            compileExpression();

            while (Grammar.predictsCommaFrom(lookahead)) {
                match(TokenType.SYMBOL);
                compileExpression();
            }
        }

        //output.append("</expressionList>\n");
    }
    
    
    /**
     * Compiles an expression
     * 
     * expression: term (op term)*
     * 
     */
    public void compileExpression() {
        System.out.println("--- Starting expression compilation...");
        //output.append("<expression>\n");
        compileTerm();
        
        /** Check whether the next token is an operator and, if so, compile the additional expression(s) */
        while(Grammar.isOperator(lookahead.getText())) {
            String operator = lookahead.getText();
            System.out.printf("\n ------ COMPILE_EXPRESSION: OPERATOR %s\n", operator);
            match(TokenType.SYMBOL);
            compileTerm();
            codeWriter.writeOperator(operator);
        }

        //output.append("</expression>\n");
    }
    
    
    /**
     * Compiles a term. If the current token is an identifier, this method uses the lookahead token in order to 
     * decide between the three alternative parsing rules ("[" implies an array entry, "(" implies a method call, 
     * an "." implies a property (variable). Any token that is not part of this term should not be advanced over.
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
//        System.out.printf("\n\t 1. COMPILE_TERM: lookAhead: %s - isTerminal? %s - isKeywordConstant? %s \n", 
//                lookahead.getText(),
//                Grammar.isTerminal(lookahead.getText()), Grammar.isKeywordConstant(lookahead.getText()));
        /** Check whether this is a terminal element or keyword constant */
        if(Grammar.isTerminal(lookahead.getText()) || Grammar.isKeywordConstant(lookahead.getText())) {
            //output.append("<term>\n");
            match(lookahead.getType());
            // System.out.println("==== TERMINAL: " + currentToken.getText());
            Identifier identifier = symbolTable.currentSubroutineScope().resolve(currentToken.getText());
            // System.out.println("Identifier: " + identifier);
            //output.append("</term>\n");
            return;
        }

//        System.out.printf("\n\t\t 2. COMPILE_TERM: lookAhead: %s - isUnary? %s \n", 
//                lookahead.getText(), Grammar.isUnaryOperator(lookahead.getText()));
        
        /** Handle the case for unary operators */
        if(Grammar.isUnaryOperator(lookahead.getText())) {
            //output.append("<term>\n");
            match(TokenType.SYMBOL);
            compileTerm();
            //output.append("</term>\n");
            return;
        }

//        System.out.printf("\n\t\t\t3. COMPILE_TERM: lookAhead: %s - predictsParenthesis? %s \n", 
//                lookahead.getText(), Grammar.predictsOpeningParenthesisFrom(lookahead));
        
        /** Handle the case for parentheses */
        if(Grammar.predictsOpeningParenthesisFrom(lookahead)) {
            //output.append("<term>\n");
            match(TokenType.SYMBOL);
            compileExpression();
            match(TokenType.SYMBOL);
            //output.append("</term>\n");
            return;
        }

//        System.out.printf("\n\t\t\t\t4. COMPILE_TERM - Dealing with identifier: %s \n", 
//                lookahead.getText());
        
        /** 
         * At this stage, we're dealing with an identifier, which offers three alternatives: a variable name, an
         * array entry, or a method invocation. Take the appropriate action based on the value of the lookahead token.
         */
        //output.append("<term>\n");
        match(lookahead.getType());
        String variable = currentToken.getText();
        System.out.println("====== IdentifierName: " + variable);
        
        boolean isMethodInvocation = (Grammar.predictsDotFrom(lookahead)|| 
                Grammar.predictsOpeningParenthesisFrom(lookahead));
        
        if(Grammar.predictsArrayEntryFrom(lookahead)) {
            match(TokenType.SYMBOL);
            compileExpression();
            match(TokenType.SYMBOL);
        } else if(isMethodInvocation) {
            if(Grammar.predictsDotFrom(lookahead)) {
                match(TokenType.SYMBOL);
                match(TokenType.IDENTIFIER);
            }

            match(TokenType.SYMBOL);
            compileExpressionList();
            match(TokenType.SYMBOL);
        } else {
            if (VMUtils.isNumber(variable)) {
                codeWriter.writePushConstant(Integer.parseInt(variable));
                
            } else {
                Identifier identifier = symbolTable.currentSubroutineScope().resolve(variable);
                System.out.println("Processing identifier: " + identifier);
                if (identifier != null) {
                    String segment = (identifier.getKind() == IdentifierKind.FIELD) ? "this" : 
                        (identifier.getKind() == IdentifierKind.ARGUMENT) ? "argument" : 
                        (identifier.getKind() == IdentifierKind.VAR) ? "local" : "";
                    String output = String.format("push %s %s\n", segment, identifier.getIndex());
                    codeWriter.writeToFile(output);
                }
                
            }
        }

        //output.append("</term>\n");
    }

}
