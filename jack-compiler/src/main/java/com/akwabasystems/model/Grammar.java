
package com.akwabasystems.model;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


/**
 * A collection of static methods related to the Jack grammar
 */
public class Grammar {
    private static final List<String> NON_TERMINAL_ELEMENTS = Arrays.asList(new String[]{
        "class", 
        "classVarDec", 
        "subroutineDec", 
        "parameterList", 
        "subroutineBody", 
        "varDec", 
        "statements", 
        "whileSatement",
        "ifStatement", 
        "returnStatement", 
        "letStatement", 
        "doStatement",
        "expression", 
        "term", 
        "expressionList"
    });

    private static final List<String> TERMINAL_ELEMENTS = Arrays.asList(new String[] {
        "keyword", 
        "symbol",
        "integerConstant",
        "stringConstant",
        "identifier"
    });

    private static final List<String> OPERATORS = Arrays.asList(new String[] {
        "+",
        "-",
        "*",
        "/",
        "&",
        "|",
        "<",
        ">",
        "="
    });
    
    private static final List<String> UNARY_OPERATORS = Arrays.asList(new String[] {
        "-",
        "~"
    });
    
    private static final List<String> KEYWORD_CONSTANTS = Arrays.asList(new String[] {
        "true",
        "false",
        "null",
        "this"
    });
    
    private static final List<String> STATEMENTS = Arrays.asList(new String[] {
        "let", 
        "if", 
        "while", 
        "do", 
        "return"
    });


    /**
     * Returns true if the given element is a terminal one; otherwise, returns false
     * 
     * @param element           the element to check
     * @return true if the given element is a terminal one; otherwise, returns false
     */
    public static boolean isTerminal(String element) {
        return TERMINAL_ELEMENTS.contains(element);
    }


    /**
     * Returns true if the given element is a non-terminal one; otherwise, returns false
     * 
     * @param element           the element to check
     * @return true if the given element is a non-terminal one; otherwise, returns false
     */
    public static boolean isNonTerminal(String element) {
        return NON_TERMINAL_ELEMENTS.contains(element);
    }
    
    
    /**
     * Returns true if the given element is an operator; otherwise, returns false
     * 
     * @param element           the element to check
     * @return true if the given element is an operator; otherwise, returns false
     */
    public static boolean isOperator(String element) {
        return OPERATORS.contains(element);
    }

    
    /**
     * Returns true if the given element is a unary operator; otherwise, returns false
     * 
     * @param element           the element to check
     * @return true if the given element is a unary operator; otherwise, returns false
     */
    public static boolean isUnaryOperator(String element) {
        return UNARY_OPERATORS.contains(element);
    }
    

    /**
     * Returns true if the given token represents a minus sign; otherwise, returns false
     * 
     * @param token         the token to check
     * @return true if the given token represents a minus sign; otherwise, returns false
     */
    public static boolean isMinusSign(Token token) {
        return token.getText().equals("-");
    }

    
    /**
     * Returns true if the given element is a keyword constant; otherwise, returns false
     * 
     * @param element           the element to check
     * @return true if the given element is a keyword constant; otherwise, returns false
     */
    public static boolean isKeywordConstant(String element) {
        return KEYWORD_CONSTANTS.contains(element);
    }
    
    
    /**
     * Returns true if a class var declaration can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a class var declaration can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsClassVarDeclarationFrom(Token token) {
        return Pattern.matches("static|field", token.getText());
    }
    
    
    /**
     * Returns true if a subroutine declaration can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a subroutine declaration can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsSubroutineFrom(Token token)  {
        return Pattern.matches("constructor|function|method", token.getText());
    }

    
    /**
     * Returns true if a semicolon can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a semicolon can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsSemicolonFrom(Token token)  {
        return (token != null && token.getText().equals(";"));
    }
    
    
    /**
     * Returns true if a keyword can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a keyword can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsKeywordFrom(Token token)  {
        return (token.getType() == TokenType.KEYWORD);
    }
    
    
    /**
     * Returns true if a symbol can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a symbol can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsSymbolFrom(Token token)  {
        return (token.getType() == TokenType.SYMBOL);
    }

    
    /**
     * Returns true if a comma can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a comma can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsCommaFrom(Token token)  {
        return token.getText().equals(",");
    }
    
    
    /**
     * Returns true if a variable declaration can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a variable declaration can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsVarDeclarationFrom(Token token) {
        return token.getText().equals("var");
    }
    
    
    /**
     * Returns true if statement declarations can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if statement declarations can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsStatementsFrom(Token token) {
        return STATEMENTS.contains(token.getText());
    }
    
    
    /**
     * Returns true if a dot can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a dot can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsDotFrom(Token token) {
        return token.getText().equals(".");
    }
    
    
    /**
     * Returns true if an array entry can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if an array entry can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsArrayEntryFrom(Token token) {
        return token.getText().equals("[");
    }
    
    
    /**
     * Returns true if an "else" clause can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if an "else" clause can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsElseClauseFrom(Token token) {
        return token.getText().equals("else");
    }
    
    
    /**
     * Returns true if an opening parenthesis can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if an opening parenthesis can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsOpeningParenthesisFrom(Token token) {
        return token.getText().equals("(");
    }
    
    
    /**
     * Returns true if a closing parenthesis can be predicted from the specified token; otherwise, returns false
     * 
     * @param token         the token from which to make the prediction
     * @return true if a closing parenthesis can be predicted from the specified token; otherwise, returns false
     */
    public static boolean predictsClosingParenthesisFrom(Token token) {
        return token.getText().equals(")");
    }
    
    
}
