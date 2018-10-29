
package com.akwabasystems.asm;


import com.akwabasystems.model.CompMnemonic;
import com.akwabasystems.model.DestMnemonic;
import com.akwabasystems.model.JumpMnemonic;
import com.akwabasystems.model.Mnemonic;
import com.akwabasystems.model.ComputeA;
import com.akwabasystems.model.ComputeM;
import com.akwabasystems.model.Destination;
import com.akwabasystems.model.Jump;


/**
 * A class that translates assembly language mnemonics into their binary code representation. It contains methods for
 * returning the binary representation of the different fields in a C-instruction (dest, comp, and jump).
 * 
 */
public final class Code {
    
    private Mnemonic destMnemonic;
    private Mnemonic compMnemonic;
    private Mnemonic jumpMnemonic;
    
    
    /**
     * Default constructor
     */
    public Code() {
        super();
    }
    
    
    /**
     * Initializes this code instance with the given "dest", "comp", and "jump" values
     * 
     * @param dest          the value of the "dest" field for this code
     * @param comp          the value of the "comp" field for this code
     * @param jump          the value of the "jump" field for this code
     */
    public Code(String dest, String comp, String jump) {
        this.destMnemonic = new DestMnemonic(dest);
        this.compMnemonic = new CompMnemonic(comp);
        this.jumpMnemonic = new JumpMnemonic(jump);
    }
    
    
    /**
     * Returns the bit string of the "dest" field for this code
     * 
     * @return the bit string of the "dest" field for this code
     */
    public String dest() {
        return (destMnemonic == null)? Destination.NONE.toBitString() : 
                Destination.fromMnemonic(destMnemonic.getValue()).toBitString();
    }
    
    
    /**
     * Returns the bit string of the "comp" field for this code
     * 
     * @return the bit string of the "comp" field for this code
     */
    public String comp() {
        if(compMnemonic == null) {
            return "";
        }

        boolean usesMemory = compMnemonic.getValue().contains("M");
        String computeField;

        if(usesMemory) {
            ComputeM computeM = ComputeM.fromMnemonic(compMnemonic.getValue());
            computeField = computeM.toBitString();
        } else {
            ComputeA computeA = ComputeA.fromMnemonic(compMnemonic.getValue());
            computeField = computeA.toBitString();
        }

        return computeField;
    }


    /**
     * Returns the bit string of the "jump" field for this code
     * 
     * @return the bit string of the "jump" field for this code
     */
    public String jump() {
        return (jumpMnemonic == null)? Jump.NO_JUMP.toBitString() :
                Jump.fromMnemonic(jumpMnemonic.getValue()).toBitString();
    }

}
