
package com.akwabasystems.asm;


/**
 * An enumeration of the different instruction types. Each constant has a method for returning its symbol pattern.
 */
public enum CommandType {
    
    A_COMMAND {
        @Override
        public String getSymbol() {
            return "@Xxx";
        }
    },
    
    C_COMMAND {
        @Override
        public String getSymbol() {
            return "dest=comp;jump";
        }
    },
    
    L_COMMAND {
        @Override
        public String getSymbol() {
            return "(Xxx)";
        }
    };
    
    
    /**
     * Returns the symbol pattern for this enum constant
     * 
     * @return the symbol pattern for this enum constant
     */
    public abstract String getSymbol();
    
}
