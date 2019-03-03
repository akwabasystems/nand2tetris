
package com.akwabasystems.model;


/**
 * A class that represents a command, which is an instruction to be executed.
 * 
 * A command can be an arithmetic or logical, which pops values from the stack, performs some computation on them, and
 * pushes the result back onto the stack. A command can also be a memory access command, which allows the ability to
 * access memory segments at given indices.
 */
public abstract class Command {
    
    protected CommandType type = CommandType.C_ARITHMETIC;
    protected String command;
    
    
    /**
     * Default constructor
     */
    public Command() {
        super();
    }
    
    
    /**
     * Initializes this instruction with the given command
     * 
     * @param command       the command for this instruction
     */
    public Command(String command) {
        this.command = command;
    }
    
    
    /**
     * Initializes this instruction with the given type and command
     * 
     * @param type          the type for this instruction
     * @param command       the command for this instruction
     */
    public Command(CommandType type, String command) {
        this.type = type;
        this.command = command;
    }
    
    
    /**
     * Returns the type for this instruction
     * 
     * @return the type for this instruction
     */
    public CommandType getType() {
        return type;
    }

    
    /**
     * Sets the type for this instruction
     * 
     * @param type          the type to set for this instruction
     */
    public void setType(CommandType type) {
        this.type = type;
    }

    
    /**
     * Returns the command for this instruction
     * 
     * @return the command for this instruction
     */
    public String getCommand() {
        return command;
    }

    
    /**
     * Sets the command for this instruction
     * 
     * @param command       the command to set for this instruction
     */
    public void setCommand(String command) {
        this.command = command;
    }
    
    
    /**
     * Returns a string representation of this instruction
     * 
     * @return a string representation of this instruction
     */
    @Override
    public String toString() {
        return String.format("Command: { %s: %s }", getType(), getCommand());
    }

    
    /**
     * Returns the segment (argument 1) for this command
     * 
     * @return the segment for this command
     */
    public abstract String getArgument1();
    
    
    /**
     * Returns the index (argument 2) for this command
     * 
     * @return the index for this command
     */
    public abstract int getArgument2();
    
}
