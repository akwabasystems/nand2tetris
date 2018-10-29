
package com.akwabasystems.asm;


/**
 * A class that represents an A-instruction, which is used to set the A register to a 15-bit value.
 */
public final class AInstruction extends Instruction {
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public AInstruction(String command) {
        super(CommandType.A_COMMAND, command);
    }

    
    /**
     * Returns the binary code for this A-instruction
     * 
     * @return the binary code for this A-instruction
     */
    @Override
    public String binaryCode() {
        boolean isValidCommand = (command.startsWith("@") && command.length() > 1);
        
        if(!isValidCommand) {
            return "0000000000000000";
        }

        String value = command.substring(1);
        String result = Integer.toBinaryString(Integer.parseInt(value));

        return String.format("0%1$15s", result).replace(" ", "0");
    }
}
