
package com.akwabasystems.asm;


/**
 * A class that represents an instruction, which is a command to be executed by the processor. This can be
 * an arithmetic or logic operation, a memory access or storage, a register operation, and so on.
 * 
 * An instruction can be of two different types. An A-instruction is used to set the A register to a 15-bit value,
 * while a C-instruction specifies what to compute, where to store the computed value, and what to do next (proceed to
 * the next instruction or jump to a specified instruction). In addition, there is also an L-instruction (or label),
 * which uniquely identifies the location of a given instruction, and is used for jump commands.
 */
public abstract class Instruction {
    
    protected CommandType type = CommandType.A_COMMAND;
    protected String command;
    
    
    /**
     * Default constructor
     */
    public Instruction() {
        super();
    }
    
    
    /**
     * Initializes this instruction with the given command
     * 
     * @param command       the command for this instruction
     */
    public Instruction(String command) {
        this.command = command;
    }
    
    
    /**
     * Initializes this instruction with the given type and command
     * 
     * @param type          the type for this instruction
     * @param command       the command for this instruction
     */
    public Instruction(CommandType type, String command) {
        this.type = type;
        this.command = command;
    }

    
    /**
     * Returns the binary code for this instruction
     * 
     * @return the binary code for this instruction
     */
    public abstract String binaryCode();
    
    
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
     * Returns true if this instruction is an A-instruction; otherwise, returns false
     * 
     * @return true if this instruction is an A-instruction; otherwise, returns false
     */
    public boolean isAInstruction() {
        return (type == CommandType.A_COMMAND);
    }
    
    
    /**
     * Returns true if this instruction is a C-instruction; otherwise, returns false
     * 
     * @return true if this instruction is a C-instruction; otherwise, returns false
     */
    public boolean isCInstruction() {
        return (type == CommandType.C_COMMAND);
    }
    
    
    /**
     * Returns true if this instruction is an L-command; otherwise, returns false
     * 
     * @return true if this instruction is an L-command; otherwise, returns false
     */
    public boolean isLCommand() {
        return (type == CommandType.L_COMMAND);
    }
                
    
    /**
     * Returns a string representation of this instruction
     * 
     * @return a string representation of this instruction
     */
    @Override
    public String toString() {
        return String.format("Instruction: { %s: %s }", getType(), getCommand());
    }

}
