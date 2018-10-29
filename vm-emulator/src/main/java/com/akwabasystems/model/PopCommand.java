
package com.akwabasystems.model;


/**
 * A class that represents a pop command, which is an arithmetic command that pops items off the stack in order to
 * compute a binary function on them.
 */
public final class PopCommand extends AbstractVMCommand {
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public PopCommand(String command) {
        super(CommandType.C_POP, command);
    }
    
    
    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    public String toAssemblyCode() {
        Segment segment = Segment.fromArgument(getArgument1());
        boolean isStaticSegment = (segment == Segment.STATIC);
        
        return (isStaticSegment)? Segment.popFromStaticSegmentIndex(getFileName(), getArgument2()) : 
                segment.popAssemblyCode(getArgument2()); 
    }
    
}
