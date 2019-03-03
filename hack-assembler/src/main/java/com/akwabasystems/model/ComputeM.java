
package com.akwabasystems.model;

import java.util.stream.Stream;


/**
 * An enumeration of the different computation commands of a C-instruction that use the memory (M).
 * Each command (or constant) has a method for returning its bit string.
 * 
 * The enum class also contains method for returning the enum constant whose bit string matches a given string. If no
 * matching constant is found, a null reference is returned.
 */
public enum ComputeM {
    
    M {
        @Override
        public String toBitString() {
            return "110000";
        }
        
        @Override
        public String actualSyntax() {
            return "M";
        }
    },
    
    NOT_M {
        @Override
        public String toBitString() {
            return "110001";
        }
        
        @Override
        public String actualSyntax() {
            return "!M";
        }
    },
    
    MINUS_M {
        @Override
        public String toBitString() {
            return "110011";
        }
        
        @Override
        public String actualSyntax() {
            return "-M";
        }
    },
    
    M_PLUS_ONE {
        @Override
        public String toBitString() {
            return "110111";
        }
        
        @Override
        public String actualSyntax() {
            return "M+1";
        }
    },
    
    M_MINUS_ONE {
        @Override
        public String toBitString() {
            return "110010";
        }
        
        @Override
        public String actualSyntax() {
            return "M-1";
        }
    },
    
    D_PLUS_M {
        @Override
        public String toBitString() {
            return "000010";
        }
        
        @Override
        public String actualSyntax() {
            return "D+M";
        }
    },
    
    D_MINUS_M {
        @Override
        public String toBitString() {
            return "010011";
        }
        
        @Override
        public String actualSyntax() {
            return "D-M";
        }
    },
    
    M_MINUS_D {
        @Override
        public String toBitString() {
            return "000111";
        }
        
        @Override
        public String actualSyntax() {
            return "M-D";
        }
    },
    
    D_AND_M {
        @Override
        public String toBitString() {
            return "000000";
        }
        
        @Override
        public String actualSyntax() {
            return "D&M";
        }
    },
    
    D_OR_M {
        @Override
        public String toBitString() {
            return "010101";
        }
        
        @Override
        public String actualSyntax() {
            return "D|M";
        }
    };
    
    
    /**
     * Returns the enum constant whose name matches the given mnemonic
     * 
     * @param mnemonic          the name of the mnemonic to find
     * @return the enum constant whose name matches the given mnemonic
     */
    public static ComputeM fromMnemonic(String mnemonic) {
        
        return Stream.of(values())
                    .filter((compute) -> compute.actualSyntax().equalsIgnoreCase(mnemonic))
                    .findFirst()
                    .orElse(null);

    }
    
    
    /**
     * Returns the enum constant whose bit string matches the given bits
     * 
     * @param bits          the bit string of the mnemonic to find
     * @return the enum constant whose bit string matches the given bits
     */
    public static ComputeM fromBits(String bits) {
        
        return Stream.of(values())
                    .filter((compute) -> compute.toBitString().equals(bits))
                    .findFirst()
                    .orElse(null);

    }
    
    
    /**
     * Returns the string representation of the bits for this enum constant
     * 
     * @return the string representation of the bits for this enum constant
     */
    public abstract String toBitString();
    
    
    /**
     * Returns the actual syntax for this enum constant
     * 
     * @return the actual syntax for this enum constant
     */
    public abstract String actualSyntax();
    
}
