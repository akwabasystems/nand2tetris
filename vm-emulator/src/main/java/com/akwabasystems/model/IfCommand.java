
package com.akwabasystems.model;


/**
 * A class that represents a conditional jump command (if-goto label). It outputs the code for performing a jump
 * to the unique label defined in its command only if the top-most value of the stack is true (either 1 or -1).
 */
public final class IfCommand extends AbstractVMCommand {
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public IfCommand(String command) {
        super(CommandType.C_IF, command);
    }
    
    
    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    @Override
    public String toAssemblyCode() {
        StringBuilder builder = new StringBuilder();

        builder.append("@SP\n")
               .append("AM=M-1\n")
               .append("D=M\n")
               .append(String.format("@%s$%s\n", getContext(), getArgument1()))
               .append("D;JNE");
        
        return builder.toString();
    }

}
