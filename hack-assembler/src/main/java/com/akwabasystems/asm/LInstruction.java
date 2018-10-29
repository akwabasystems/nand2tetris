
package com.akwabasystems.asm;


/**
 * A class that represents an L-command (or label), which uniquely identifies the location a given instruction, and is 
 * used for jump commands.
 */
public final class LInstruction extends Instruction {
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public LInstruction(String command) {
        super(CommandType.L_COMMAND, command);
    }

    
    /**
     * Returns the binary code for this L-command
     * 
     * @return the binary code for this L-command
     */
    @Override
    public String binaryCode() {
        return "";
    }

}
