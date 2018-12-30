
package com.akwabasystems.model;


/**
 * A class that represents an arithmetic or logical command. Some of those commands are binary: they pop items off the
 * stack, compute a binary function on them, and push the result back onto the stack. Others are unary: they pop a
 * single item off the stack, compute a unary function on it, and push it back onto the stack
 */
public final class ArithmeticCommand extends AbstractVMCommand {
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public ArithmeticCommand(String command) {
        super(CommandType.C_ARITHMETIC, command);
    }

    
    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    @Override
    public String toAssemblyCode() {
        ArithmeticCommandType commandType = ArithmeticCommandType.fromCommand(getCommand());
        return commandType.toAssemblyCode(getCommand());
    }
    
}
