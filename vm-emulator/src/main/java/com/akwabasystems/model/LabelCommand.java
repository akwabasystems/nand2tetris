
package com.akwabasystems.model;


/**
 * A class that represents a label command (label name). It outputs the code for declaring a unique label that is
 * used for function invocations or jumps.
 */
public final class LabelCommand extends AbstractVMCommand {
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public LabelCommand(String command) {
        super(CommandType.C_LABEL, command);
    }

    
    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    @Override
    public String toAssemblyCode() {
        return String.format("(%s$%s)", getContext(), getArgument1());
    }

}
