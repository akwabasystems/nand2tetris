
package com.akwabasystems.model;


/**
 * A class that represents an unconditional jump command (goto label). It outputs the code for performing a jump
 * to the unique label defined for the target instruction.
 */
public final class GotoCommand extends AbstractVMCommand {
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public GotoCommand(String command) {
        super(CommandType.C_GOTO, command);
    }
    
    
    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    public String toAssemblyCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("@%s$%s\n", getContext(), getArgument1()))
               .append("0;JMP");
        
        return builder.toString();
    }

}
