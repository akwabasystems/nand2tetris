
package com.akwabasystems.model;

import java.util.stream.Stream;


/**
 * An enumeration of the different jump fields in a C-instruction. Each constant has a method for returning its 
 * bit string.
 * 
 * The enum class also contains method for returning the enum constant whose bit string matches a given string. If no
 * matching constant is found, the default "NO_JUMP" constant is returned.
 */
public enum Jump {
    
    NO_JUMP {
        @Override
        public String toBitString() {
            return "000";
        }
    },
    
    JGT {
        @Override
        public String toBitString() {
            return "001";
        }
    },
    
    JEQ {
        @Override
        public String toBitString() {
            return "010";
        }
    },
    
    JGE {
        @Override
        public String toBitString() {
            return "011";
        }
    },
    
    JLT {
        @Override
        public String toBitString() {
            return "100";
        }
    },
    
    JNE {
        @Override
        public String toBitString() {
            return "101";
        }
    },
    
    JLE {
        @Override
        public String toBitString() {
            return "110";
        }
    },
    
    JMP {
        @Override
        public String toBitString() {
            return "111";
        }
    };
    
    
    /**
     * Returns the enum constant whose name matches the given mnemonic
     * 
     * @param mnemonic          the name of the mnemonic to find
     * @return the enum constant whose name matches the given mnemonic
     */
    public static Jump fromMnemonic(String mnemonic) {
        
        return Stream.of(values())
                     .filter((jump) -> jump.name().equalsIgnoreCase(mnemonic))
                     .findFirst()
                     .orElse(NO_JUMP);
        
    }
    
    
    /**
     * Returns the enum constant whose bit string matches the given bits
     * 
     * @param bits          the bit string of the mnemonic to find
     * @return the enum constant whose bit string matches the given bits
     */
    public static Jump fromBits(String bits) {
        
        return Stream.of(values())
                     .filter((jump) -> jump.toBitString().equals(bits))
                     .findFirst()
                     .orElse(NO_JUMP);

    }
    
    
    /**
     * Returns the string representation of the bits for this enum constant
     * 
     * @return the string representation of the bits for this enum constant
     */
    public abstract String toBitString();
    
}
