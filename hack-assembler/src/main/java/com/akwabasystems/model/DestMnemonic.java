
package com.akwabasystems.model;


/**
 * A class that represents a destination mnemonic, which corresponds to bits 3 through 5 (d1, d2, d3) of a C-instruction.
 */
public final class DestMnemonic extends Mnemonic {
    
    
    /**
     * Initializes the instance of this Mnemonic class with the given value
     * 
     * @param value         the value to set for this mnemonic instance
     */
    public DestMnemonic(String value) {
        super(MnemonicType.DEST, value);
    }
 
}
