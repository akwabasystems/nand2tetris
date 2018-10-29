
package com.akwabasystems.model;



/**
 * An enumeration of the different token types in the Jack language.
 */
public enum TokenType {
    
    KEYWORD {
        
        @Override
        public String text() {
            return "keyword";
        }
        
    },
    
    
    SYMBOL {
        
        @Override
        public String text() {
            return "symbol";
        }
        
    },
    
    
    IDENTIFIER {

        @Override
        public String text() {
            return "identifier";
        }
        
    },


    INT_CONSTANT {        

        @Override
        public String text() {
            return "integerConstant";
        }
        
    },


    STRING_CONSTANT {
        
        @Override
        public String text() {
            return "stringConstant";
        }
        
    },
    
    
    END_OF_FILE {

        @Override
        public String text() {
            return "eof";
        }
        
    };

    
    /**
     * Returns the text for this symbol
     *
     * @return the text for this symbol
     */
    public abstract String text();
    
}
