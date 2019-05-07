
package com.akwabasystems.model;



/**
 * An enumeration of the different token types in the Jack language.
 */
public enum IdentifierKind {
    
    VAR {
        
        @Override
        public String text() {
            return "var";
        }
        
    },
    
    
    ARGUMENT {
        
        @Override
        public String text() {
            return "argument";
        }
        
    },
    
    
    FIELD {

        @Override
        public String text() {
            return "field";
        }
        
    },


    STATIC {        

        @Override
        public String text() {
            return "static";
        }
        
    },
    
    
    NONE {
        
        @Override
        public String text() {
            return "none";
        }
        
    };

    
    /**
     * Returns the text for this symbol
     *
     * @return the text for this symbol
     */
    public abstract String text();


    /**
     * Returns the enum constant whose command matches the given command
     * 
     * @param text      the command for which to find the enum constant
     * @return the enum constant whose command matches the given command
     */
    public static IdentifierKind fromText(String text) {
        
        if(text == null) {
            return IdentifierKind.NONE;
        }

        for(IdentifierKind type : values()) {
            if(type.text().equals(text)) {
                return type;
            }
        }

        return IdentifierKind.NONE;
    }
    
}
