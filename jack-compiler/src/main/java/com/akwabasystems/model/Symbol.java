
package com.akwabasystems.model;


/**
 * An enumeration of the different symbol types of the Jack language. Symbols are considered terminal lexical
 * elements.
 */
public enum Symbol {
    
    LEFT_CURLY_BRACE {

        @Override
        public char toChar() {
            return '{';
        }
        
    },
    
    
    RIGHT_CURLY_BRACE {
        
        @Override
        public char toChar() {
            return '}';
        }
        
    },
    
    
    LEFT_PARENTHESIS {

        @Override
        public char toChar() {
            return '(';
        }
        
    },
    
    
    RIGHT_PARENTHESIS {
        
        @Override
        public char toChar() {
            return ')';
        }

    },


    LEFT_BRACKET {
        @Override
        public char toChar() {
            return '[';
        }
    },
    
    
    RIGHT_BRACKET {
        
        @Override
        public char toChar() {
            return ']';
        }
    },
    
    
    DOT {
        
        @Override
        public char toChar() {
            return '.';
        }
        
    },
    
    
    COMMA {
        
        @Override
        public char toChar() {
            return ',';
        }
        
    },
    
    
    SEMICOLON {
        
        @Override
        public char toChar() {
            return ';';
        }
        
    },
    
    
    PLUS {
        
        @Override
        public char toChar() {
            return '+';
        }
        
    },
    
    
    MINUS {
        
        @Override
        public char toChar() {
            return '-';
        }
        
    },
    
    
    MULTIPLY {
        
        @Override
        public char toChar() {
            return '*';
        }
        
    },
    
    
    DIVIDE {
        
        @Override
        public char toChar() {
            return '/';
        }
        
    },
    
    
    AMPERSAND {
        
        @Override
        public char toChar() {
            return '&';
        }
        
        @Override
        public String toXML() {
            return "&amp;";
        }
        
    },
    
    
    PIPE {
        
        @Override
        public char toChar() {
            return '|';
        }
        
    },
    
    
    LESS_THAN {
        
        @Override
        public char toChar() {
            return '<';
        }
        
        @Override
        public String toXML() {
            return "&lt;";
        }
    },
    
    
    GREATER_THAN {
        
        @Override
        public char toChar() {
            return '>';
        }
        
        @Override
        public String toXML() {
            return "&gt;";
        }
        
    },
    
    
    EQUAL {
        
        @Override
        public char toChar() {
            return '=';
        }
        
    },
    
    
    TILDE {
        
        @Override
        public char toChar() {
            return '~';
        }
        
    };
    

    /**
     * Returns the character that corresponds to this symbol
     *
     * @return the character that corresponds to this symbol
     */
    public abstract char toChar();
    
    
    /**
     * Returns the XML representation of the text for this symbol
     *
     * @return the XML representation of the text for this symbol
     */
    public String toXML() {
        return String.valueOf(toChar());
    }
    

    /**
     * Returns the enum constant whose string value matches the given text.
     * 
     * @param text          the text for which to find the enum constant
     * @return the enum constant whose string value matches the given text
     */
    public static Symbol fromText(String text) {
        
        if(text == null) {
            return null;
        }
        
        for(Symbol keyword : values()) {
            if(String.valueOf(keyword.toChar()).equals(text)) {
                return keyword;
            }
        }

        return null;
    }

}
