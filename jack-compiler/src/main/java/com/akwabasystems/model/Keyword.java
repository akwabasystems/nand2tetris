
package com.akwabasystems.model;

import java.util.stream.Stream;


/**
 * An enumeration of the different keyword types of the Jack language. Keywords are considered terminal lexical
 * elements.
 */
public enum Keyword {
    
    CLASS {

        @Override
        public String toString() {
            return "class";
        }
        
    },
    
    
    METHOD {
        
        @Override
        public String toString() {
            return "method";
        }
        
    },
    
    
    FUNCTION {

        @Override
        public String toString() {
            return "function";
        }
        
    },
    
    
    CONSTRUCTOR {
        
        @Override
        public String toString() {
            return "constructor";
        }

    },


    INT {
        @Override
        public String toString() {
            return "int";
        }
    },
    
    
    BOOLEAN {
        
        @Override
        public String toString() {
            return "boolean";
        }
    },
    
    
    CHAR {
        
        @Override
        public String toString() {
            return "char";
        }
        
    },
    
    
    VOID {
        
        @Override
        public String toString() {
            return "void";
        }
        
    },
    
    
    VAR {
        
        @Override
        public String toString() {
            return "var";
        }
        
    },
    
    
    STATIC {
        
        @Override
        public String toString() {
            return "static";
        }
        
    },
    
    
    FIELD {
        
        @Override
        public String toString() {
            return "field";
        }

    },
    
    
    LET {
        
        @Override
        public String toString() {
            return "let";
        }
        
    },
    
    
    DO {
        
        @Override
        public String toString() {
            return "do";
        }
        
    },
    
    
    IF {
        
        @Override
        public String toString() {
            return "if";
        }
        
    },
    
    
    ELSE {
        
        @Override
        public String toString() {
            return "else";
        }
        
    },
    
    
    WHILE {
        
        @Override
        public String toString() {
            return "while";
        }
        
    },
    
    
    RETURN {
        
        @Override
        public String toString() {
            return "return";
        }
        
    },
    
    
    TRUE {
        
        @Override
        public String toString() {
            return "true";
        }
        
    },
    
    
    FALSE {
        
        @Override
        public String toString() {
            return "false";
        }
        
    },
    
    
    NULL {
        
        @Override
        public String toString() {
            return "null";
        }
        
    },
    
    
    THIS {
        
        @Override
        public String toString() {
            return "this";
        }
        
    };
    

    /**
     * Returns the enum constant whose string value matches the given text.
     * 
     * @param text          the text for which to find the enum constant
     * @return the enum constant whose string value matches the given text
     */
    public static Keyword fromText(String text) {
        return Stream.of(values())
                    .filter((keyword) -> keyword.toString().equals(text))
                    .findFirst()
                    .orElse(null);
    }

}
