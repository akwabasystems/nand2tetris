
package com.akwabasystems.model;


/**
 * A class that represents a jump mnemonic, which corresponds to bits 0 through 2 (j1, j2, j3) of a C-instruction.
 */
public final class JumpMnemonic extends Mnemonic {
    
    
    /**
     * Initializes the instance of this Mnemonic class with the given value
     * 
     * @param value         the value to set for this mnemonic instance
     */
    public JumpMnemonic(String value) {
        super(MnemonicType.JUMP, value);
    }
 
}
