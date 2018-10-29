
package com.akwabasystems.model;


/**
 * An enumeration of the different computation commands of a C-instruction that use the A register.
 * Each command (or constant) has a method for returning its bit string.
 * 
 * The enum class also contains method for returning the enum constant whose bit string matches a given string. If no
 * matching constant is found, a null reference is returned.
 */
public enum ComputeA {
    
    ZERO {
        @Override
        public String toBitString() {
            return "101010";
        }
        
        @Override
        public String actualSyntax() {
            return "0";
        }
    },
    
    ONE {
        @Override
        public String toBitString() {
            return "111111";
        }
        
        @Override
        public String actualSyntax() {
            return "1";
        }
    },
    
    MINUS_ONE {
        @Override
        public String toBitString() {
            return "111010";
        }
        
        @Override
        public String actualSyntax() {
            return "-1";
        }
    },
    
    D {
        @Override
        public String toBitString() {
            return "001100";
        }
        
        @Override
        public String actualSyntax() {
            return "D";
        }
    },
    
    A {
        @Override
        public String toBitString() {
            return "110000";
        }
        
        @Override
        public String actualSyntax() {
            return "A";
        }
    },
    
    NOT_D {
        @Override
        public String toBitString() {
            return "001101";
        }
        
        @Override
        public String actualSyntax() {
            return "!D";
        }
    },
    
    NOT_A {
        @Override
        public String toBitString() {
            return "110001";
        }
        
        @Override
        public String actualSyntax() {
            return "!A";
        }
    },
    
    MINUS_D {
        @Override
        public String toBitString() {
            return "001111";
        }
        
        @Override
        public String actualSyntax() {
            return "-D";
        }
    },
    
    MINUS_A {
        @Override
        public String toBitString() {
            return "110011";
        }
        
        @Override
        public String actualSyntax() {
            return "-A";
        }
    },
    
    D_PLUS_ONE {
        @Override
        public String toBitString() {
            return "011111";
        }
        
        @Override
        public String actualSyntax() {
            return "D+1";
        }
    },
    
    A_PLUS_ONE {
        @Override
        public String toBitString() {
            return "110111";
        }
        
        @Override
        public String actualSyntax() {
            return "A+1";
        }
    },
    
    D_MINUS_ONE {
        @Override
        public String toBitString() {
            return "001110";
        }
        
        @Override
        public String actualSyntax() {
            return "D-1";
        }
    },
    
    A_MINUS_ONE {
        @Override
        public String toBitString() {
            return "110010";
        }
        
        @Override
        public String actualSyntax() {
            return "A-1";
        }
    },
    
    D_PLUS_A {
        @Override
        public String toBitString() {
            return "000010";
        }
        
        @Override
        public String actualSyntax() {
            return "D+A";
        }
    },
    
    D_MINUS_A {
        @Override
        public String toBitString() {
            return "010011";
        }
        
        @Override
        public String actualSyntax() {
            return "D-A";
        }
    },
    
    A_MINUS_D {
        @Override
        public String toBitString() {
            return "000111";
        }
        
        @Override
        public String actualSyntax() {
            return "A-D";
        }
    },
    
    D_AND_A {
        @Override
        public String toBitString() {
            return "000000";
        }
        
        @Override
        public String actualSyntax() {
            return "D&A";
        }
    },
    
    D_OR_A {
        @Override
        public String toBitString() {
            return "010101";
        }
        
        @Override
        public String actualSyntax() {
            return "D|A";
        }
    };
    
    
    /**
     * Returns the enum constant whose name matches the given mnemonic
     * 
     * @param mnemonic          the name of the mnemonic to find
     * @return the enum constant whose name matches the given mnemonic
     */
    public static ComputeA fromMnemonic(String mnemonic) {
        if(mnemonic == null) {
            return null;
        }
        
        for(ComputeA compute : values()) {
            if(compute.actualSyntax().equals(mnemonic.toUpperCase())) {
                return compute;
            }
        }
        
        return null;
    }
    
    
    /**
     * Returns the enum constant whose bit string matches the given bits
     * 
     * @param bits          the bit string of the mnemonic to find
     * @return the enum constant whose bit string matches the given bits
     */
    public static ComputeA fromBits(String bits) {
        if(bits == null) {
            return null;
        }
        
        for(ComputeA compute : values()) {
            if(compute.toBitString().equals(bits)) {
                return compute;
            }
        }
        
        return null;
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
