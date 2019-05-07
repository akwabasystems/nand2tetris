
package com.akwabasystems.model;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;


/**
 * An enumeration of the different command types. Each constant has a method for returning its command list, as well
 * as the instance of the VMCommand associated with it.
 */
public enum ArithmeticCommand {
    
    ADD {
        
        @Override
        public String syntax() {
            return "+";
        }
    },
    
    SUB {
        @Override
        public String syntax() {
            return "-";
        }
    },
    
    NEG {
        @Override
        public String syntax() {
            return "-";
        }
    },
    
    EQ {
        @Override
        public String syntax() {
            return "==";
        }
    },
    
    
    GT {
        @Override
        public String syntax() {
            return ">";
        }
    },
    
    
    LT {
        @Override
        public String syntax() {
            return "<";
        }
    },
    
    
    AND {
        @Override
        public String syntax() {
            return "&&";
        }
    },
    
    
    OR {
        @Override
        public String syntax() {
            return "||";
        }
    },
    
    
    NOT {
        @Override
        public String syntax() {
            return "!";
        }
    };
    
    
    private static final AtomicLong counter = new AtomicLong(0);
    
    
    /**
     * Returns the assembly code for the given command
     * 
     * @param command           the command for which to return the assembly code
     * @return the assembly code for the given command
     */
    public abstract String syntax();
    

    /**
     * Returns the enum constant whose command matches the given command. It removes all comments from the command
     * prior to processing.
     * 
     * @param command          the command for which to find the enum constant
     * @return the enum constant whose command matches the given command
     */
    public static ArithmeticCommand fromSyntax(String syntax) {

        return Stream.of(values())
                     .filter((command) -> command.syntax().equalsIgnoreCase(syntax))
                     .findFirst()
                     .orElse(null);
    }

}
