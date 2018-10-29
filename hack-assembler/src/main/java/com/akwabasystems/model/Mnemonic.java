
package com.akwabasystems.model;


/**
 * A class that represents a mnemonic, a symbolic label that corresponds to a given instruction. A mnemonic is
 * generally created for compute instructions (C-instructions), and can be of 3 types: dest, comp, or jump. Those types
 * are represented by their respective enum classes.
 */
public abstract class Mnemonic {
    
    private MnemonicType type;
    private String value;
    
    
    /** 
     * Default constructor
     */
    public Mnemonic() {
        super();
    }
    
    
    /**
     * Overloaded constructor that initializes a mnemonic with the given type and value
     * 
     * @param type          the type to set for this mnemonic
     * @param value         the value to set for this mnemonic        
     */
    public Mnemonic(MnemonicType type, String value) {
        this.type = type;
        this.value = value;
    }

    
    /**
     * Returns the type of this mnemonic
     * 
     * @return the type of this mnemonic
     */
    public MnemonicType getType() {
        return type;
    }

    
    /**
     * Sets the type for this mnemonic
     * 
     * @param type          the type to set for this mnemonic
     */
    public void setType(MnemonicType type) {
        this.type = type;
    }

    
    /**
     * Returns the value of this mnemonic
     * 
     * @return the value of this mnemonic
     */
    public String getValue() {
        return value;
    }

    
    /**
     * Sets the value of this mnemonic
     * 
     * @param value         the value to set for this mnemonic
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    
    /**
     * Returns the string representation of this mnemonic
     * 
     * @return the string representation of this mnemonic 
     */
    @Override
    public String toString() {
        return String.format("Mnemonic { %s: %s }", this.type, this.value);
    }
    
}
