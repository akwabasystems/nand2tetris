
package com.akwabasystems.parsing;

import com.akwabasystems.model.Keyword;
import com.akwabasystems.model.Token;
import com.akwabasystems.model.TokenType;
import com.akwabasystems.utils.VMUtils;


/**
 * A class that processes code written in the Jack language, and breaks it into tokens as specified by the Jack grammar.
 * The tokens may be separated by an arbitrary number of space characters, newline characters, and comments, which are
 * ignored.
 * 
 * This class also defines an API for querying the current token and/or token index, for advancing to the next token,
 * if possible, for matching a given token, for outputting the token stream to a file, and more.
 * 
 * It implements the logic for returning the next token, if available, as well as the logic for outputting the
 * XML or VM representations of the code to a given file.
 * 
 * This class is marked as final and cannot be subclassed.
 */
public final class JackTokenizer extends Tokenizer {

    /**
     * Constructor. Initializes this instance with the given input.
     * 
     * @param input         the input file for this instance 
     */
    public JackTokenizer(String input) {
        super(VMUtils.stripComments(input));
    }
    

    /**
     * Returns the next token for this tokenizer, if any. If no more tokens are available, it returns a generic token
     * that signals the end of the current file.
     * 
     * @return the next token for this tokenizer, if any.
     */
    @Override
    public Token nextToken() {
        while(currentToken != EOF) {
            
            switch(currentToken) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    WS();
                    continue;
                
                case '{':
                    advance();
                    return new Token(TokenType.SYMBOL, "{");
                    
                case '}':
                    advance();
                    return new Token(TokenType.SYMBOL, "}");
                
                case '(':
                    advance();
                    return new Token(TokenType.SYMBOL, "(");
                    
                case ')':
                    advance();
                    return new Token(TokenType.SYMBOL, ")");
                
                case '[':
                    advance();
                    return new Token(TokenType.SYMBOL, "[");
                    
                case ']':
                    advance();
                    return new Token(TokenType.SYMBOL, "]");
                    
                case '.':
                    advance();
                    return new Token(TokenType.SYMBOL, ".");
                
                case ',':
                    advance();
                    return new Token(TokenType.SYMBOL, ",");
                    
                case ';':
                    advance();
                    return new Token(TokenType.SYMBOL, ";");
                    
                case '+':
                    advance();
                    return new Token(TokenType.SYMBOL, "+");
                    
                case '-':
                    advance();
                    return new Token(TokenType.SYMBOL, "-");
                    
                case '*':
                    advance();
                    return new Token(TokenType.SYMBOL, "*");
                    
                case '/':
                    advance();
                    return new Token(TokenType.SYMBOL, "/");
                    
                case '&':
                    advance();
                    return new Token(TokenType.SYMBOL, "&");
                    
                case '|':
                    advance();
                    return new Token(TokenType.SYMBOL, "|");
                    
                case '<':
                    advance();
                    return new Token(TokenType.SYMBOL, "<");
                
                case '>':
                    advance();
                    return new Token(TokenType.SYMBOL, ">");
                
                case '=':
                    advance();
                    return new Token(TokenType.SYMBOL, "=");
                
                case '~':
                    advance();
                    return new Token(TokenType.SYMBOL, "~");
                    
                case '"':
                    return stringConstant();
                
                default:
                    if(isDigit()) {
                        return integerConstant();
                    } else if(isAlphanumeric()) {
                        return keywordOrIdentifier();
                    }
                    
                    throw new Error(String.format("Invalid character: '%s'", currentToken));

            }
        }

        return new Token(TokenType.END_OF_FILE, "<EOF>");
    }
    
    
    /**
     * Processes and skips whitespace sequences
     * 
     * WS: (' '|'\t'|'\n'|'\r')* ;
     */
    private void WS() {
        while (currentToken == ' ' ||
               currentToken == '\t' || 
               currentToken == '\n' ||
               currentToken == '\r') {
            advance();
        }
    }

    
    /**
     * Processes a keyword or identifier token.
     * 
     * identifier: (['a'..'z'|'A'..'Z'|_]+[0..9]+ ;
     * 
     * @return a keyword or identifier token
     */
    private Token keywordOrIdentifier() {
        StringBuilder builder = new StringBuilder();
        
        do {
            
            builder.append(currentToken);
            advance();
            
        } while (isAlphanumeric());
        
        String text = builder.toString();
        
        /** First, check whether it is a keyword */
        Keyword keyword = Keyword.fromText(text);
        
        if(keyword != null) {
            return new Token(TokenType.KEYWORD, text);
        }
        
        /** At this point, it is safe to assume that it is an identifier */
        return new Token(TokenType.IDENTIFIER, text);
    }
    
    
    /**
     * Processes an integer constant token.
     * 
     * intConstant: [0..9]+ ;
     * 
     * @return an integer constant token.
     */
    private Token integerConstant() {
        StringBuilder builder = new StringBuilder();
        
        while (isDigit()) {
            builder.append(currentToken);
            advance();
        }

        return new Token(TokenType.INT_CONSTANT, builder.toString());
    }


    /**
     * Processes a string constant token.
     * 
     * stringConstant: ['a'..'z'|'A'..'Z']+ ;
     * 
     * @return a string constant token
     */
    private Token stringConstant() {
        StringBuilder builder = new StringBuilder();

        /** Advance past the opening double-quote */
        advance();
        
        while (currentToken != '"') {
            builder.append(currentToken);
            advance();
        }

        /** Advance past the closing double-quote */
        advance();

        return new Token(TokenType.STRING_CONSTANT, builder.toString());
    }


    /**
     * Returns the XML representation of the tokens for this tokenizer. The "nodeName" parameter specifies the
     * name of the top-level element.
     * 
     * @param nodeName          the name of the top-level XML node
     * @return the XML representation of the tokens for this tokenizer.
     */
    @Override
    public String toXML(String nodeName) {
        StringBuilder builder = new StringBuilder();
        
        builder.append(String.format("<%s>\n", nodeName));
        
        while(hasMoreTokens()) {
            Token token = nextToken();
            builder.append(token.toXML()).append("\n");
        }

        builder.append(String.format("</%s>", nodeName));

        return builder.toString();
    }

}
