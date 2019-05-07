
package com.akwabasystems.model;


import com.akwabasystems.utils.VMUtils;
import java.util.stream.Stream;


/**
 * An enumeration of the different types of segments used in memory access commands. Each constant has a method for
 * returning its argument based on a given string, as well as the assembly codes associated with its operations.
 */
public enum Segment {
    
    CONSTANT {

        @Override
        public String argument() {
            return "constant";
        }

    },

    
    LOCAL {
        
        @Override
        public String argument() {
            return "local";
        }

    },
    
    
    ARGUMENT {
        
        @Override
        public String argument() {
            return "argument";
        }

    },
    
    
    THIS {
        
        @Override
        public String argument() {
            return "this";
        }
       
    },
    
    
    THAT {
        
        @Override
        public String argument() {
            return "that";
        }
        
    },
    
    
    POINTER {
        
        @Override
        public String argument() {
            return "pointer";
        }

    },
    
    
    TEMP {
        
        @Override
        public String argument() {
            return "temp";
        }

    },
    
    
    STATIC {
        
        @Override
        public String argument() {
            return "static";
        }
        
    };


    /**
     * Returns the argument for this enum constant
     * 
     * @return the argument for this enum constant
     */
    public abstract String argument();
    
    

    /**
     * Returns the enum constant whose argument matches the given argument
     * 
     * @param argument              the argument for which to find the enum constant
     * @return the enum constant whose argument matches the given argument
     */
    public static Segment fromArgument(String argument) {
        
        return Stream.of(values())
                     .filter((segment) -> segment.argument().equalsIgnoreCase(argument))
                     .findFirst()
                     .orElse(null);

    }

}
