
package com.akwabasystems.parsing;


import com.akwabasystems.model.Token;
import java.util.regex.Pattern;


/**
 * A class that processes code written in the Jack language, and breaks it into tokens as specified by the Jack grammar.
 * The tokens may be separated by an arbitrary number of space characters, newline characters, and comments, which are
 * ignored.
 * 
 * It defines an API for querying the current token and/or token index, for advancing to the next token, if possible, 
 * for matching a given token, for outputting the token stream to a file, and more.
 * 
 * This class is abstract and defines specific methods to be implemented by any subclass.
 */
public abstract class Tokenizer {
    public char currentToken;
    public int currentIndex = 0;
    public String input;
    protected static final char EOF = (char) -1;


    /**
     * Constructor. Sets the input for this tokenizer to the specified string, and initializes the state of the
     * current token and token index,
     * 
     * @param input         the input to set for this tokenizer
     */
    public Tokenizer(String input) {
        this.input = input;
        currentIndex = 0;
        currentToken = input.charAt(currentIndex);
    }

    
    /**
     * Returns the input for this tokenizer
     * 
     * @return the input for this tokenizer
     */
    public String getInput() {
      return input;
    }


    /**
     * Returns true if the current token is alphanumeric (consisting of letters, digits, and/or the "underscore"
     * character); otherwise, returns false.
     * 
     * @return true if the current token is alphanumeric; otherwise, returns false
     */
    protected boolean isAlphanumeric() {
        return Pattern.matches("[a-zA-Z_0-9]", String.valueOf(currentToken));
    }


    /**
     * Returns true if the current token is a digit (consisting of numbers only); otherwise, returns false.
     * 
     * @return true if the current token is a digit; otherwise, returns false
     */
    protected boolean isDigit() {
        return Pattern.matches("[0-9]", String.valueOf(currentToken));
    }
    
    
    /**
     * Advances the current index and updates the current token. It handles the case where the ";" token is the last
     * token on the line (for instance, `var x = 123;`) by only advancing the index only if the length of the input 
     * string has not been reached.
     */
    public void advance() {
        if(currentIndex <= input.length() - 1) {
            currentIndex++;
        }

        currentToken = (currentIndex >= input.length()) ? EOF : input.charAt(currentIndex);
    }
    
    
    /**
     * Matches the given character and advances past it
     * 
     * @param c         the character to match 
     */
    public void match(char c) {
        if(currentToken == c) {
            advance();
        } else {
            throw new Error(String.format("Expecting %s; found '%s'", c, currentToken));
        }
    }

    
    /**
     * Returns true if this tokenizer has more tokens; otherwise, returns false
     * 
     * @return true if this tokenizer has more tokens; otherwise, returns false
     */
    public boolean hasMoreTokens() {
        return (currentIndex <= input.length() - 1);
    }

    
    /**
     * Returns the next token for this tokenizer
     * 
     * @return the next token for this tokenizer
     */
    public abstract Token nextToken();
    
    
    /**
     * Returns the XML representation of the tokens for this tokenizer. The "nodeName" parameter specifies the
     * name of the top-level element.
     * 
     * @param nodeName          the name of the top-level XML node
     * @return the XML representation of the tokens for this tokenizer.
     */
    public abstract String toXML(String nodeName);

}
