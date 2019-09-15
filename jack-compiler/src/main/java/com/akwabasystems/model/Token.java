
package com.akwabasystems.model;


/**
 * A token is a sequence of characters as defined by the syntax of the Jack language. A token is the basic atom of 
 * a program. It is the result of the lexical analysis process, which groups characters into tokens while ignoring
 * white space and comments.
 * 
 * Tokens fall into different categories, or types: some are keywords, others are symbols, and so on. In general, 
 * each programming language specifies the types of tokens it allows, as well as the exact syntax rules for combining
 * them into valid programmatic structures. The Jack language follows that same logic.
 */
public final class Token {
    private final TokenType type;
    private final String text;
    
    
    /**
     * Constructor. Initializes this instance with the given type and text
     * 
     * @param type      the type for this token
     * @param text      the text for this token
     */
    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }
    
    
    /**
     * Returns the type for this token
     * 
     * @return the type for this token 
     */
    public TokenType getType() {
        return type;
    }
    
    
    /**
     * Returns the text for this token
     * 
     * @return the text for this token 
     */
    public String getText() {
        return text;
    }
    
    
    /**
     * Returns the string representation for this token
     * 
     * @return the string representation for this token
     */
    @Override
    public String toString() {
        return String.format("<%s, '%s'>", type, text);
    }
    
    
    /**
     * Returns the XML string representation for this token
     * 
     * @return the XML string representation for this token
     */
    public String toXML() {
        boolean isSymbol = (type == TokenType.SYMBOL);
        String xmlText = (!isSymbol)? getText() : Symbol.fromText(getText()).toXML();
        return String.format("<%s> %s </%s>", type.text(), xmlText, type.text());
    }

}
