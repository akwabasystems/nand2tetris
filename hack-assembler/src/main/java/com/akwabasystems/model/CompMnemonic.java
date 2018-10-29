
package com.akwabasystems.model;


/**
 * A class that represents a comp mnemonic, which corresponds to bits 6 through 12 (a, c1, c2, c3, c4, c5, c6) of 
 * a C-instruction.
 */
public final class CompMnemonic extends Mnemonic {
    
    
    /**
     * Initializes the instance of this Mnemonic class with the given value
     * 
     * @param value         the value to set for this mnemonic instance
     */
    public CompMnemonic(String value) {
        super(MnemonicType.COMP, value);
    }
 
}
