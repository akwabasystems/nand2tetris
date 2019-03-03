
package com.akwabasystems.model;


/**
 * A class that represents a push command, which is an arithmetic command that pushes items onto the stack.
 */
public final class PushCommand extends AbstractVMCommand {
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public PushCommand(String command) {
        super(CommandType.C_PUSH, command);
    }


    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    @Override
    public String toAssemblyCode() {
        Segment segment = Segment.fromArgument(getArgument1());
        boolean isStaticSegment = (segment == Segment.STATIC);

        return (isStaticSegment)? Segment.pushToStaticSegmentIndex(getFileName(), getArgument2()) : 
                segment.pushAssemblyCode(getArgument2()); 
    }

}
