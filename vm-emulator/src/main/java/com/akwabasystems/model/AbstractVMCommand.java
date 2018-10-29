
package com.akwabasystems.model;


/**
 * An abstract implementation of the VMCommand class. It provides default implementations of the interface methods,
 * which can later be overridden by subclasses.
 */
public abstract class AbstractVMCommand implements VMCommand {
    
    protected CommandType type = CommandType.C_ARITHMETIC;
    protected String command;
    protected String argument1 = "";
    protected int argument2 = 0;
    protected String fileName;
    protected String context;
    
    
    /**
     * Default constructor
     */
    public AbstractVMCommand() {
        super();
    }
    
    
    /**
     * Initializes this command with the given syntax
     * 
     * @param command       the command string for this command
     */
    public AbstractVMCommand(String command) {
        this.command = command;
        parseArguments();
    }


    /**
     * Initializes this command with the given type and command
     * 
     * @param type          the type for this command
     * @param command       the command string for this command
     */
    public AbstractVMCommand(CommandType type, String command) {
        this.type = type;
        this.command = command;

        parseArguments();
    }


    /**
     * Parses the arguments for the given command, splitting them into first and second arguments if necessary
     */
    protected final void parseArguments() {
        String[] parts = command.split(" ");
        
        if(parts.length == 2) {
            argument1 = parts[1];
        } else if(parts.length >= 2) {
            argument1 = parts[1];
            
            try {
                
                argument2 = Integer.decode(parts[2]);
                
            } catch(NumberFormatException invalidNumber) {
                argument2 = 0;
            }
        }
    }


    /**
     * Returns the type for this command
     * 
     * @return the type for this command
     */
    public CommandType getType() {
        return type;
    }

    
    /**
     * Sets the type for this command
     * 
     * @param type          the type to set for this command
     */
    public void setType(CommandType type) {
        this.type = type;
    }

    
    /**
     * Returns the command string for this command
     * 
     * @return the command string for this command
     */
    public String getCommand() {
        return command;
    }

    
    /**
     * Sets the command string for this instruction
     * 
     * @param command       the command string to set for this command
     */
    public void setCommand(String command) {
        this.command = command;
    }
    
    
    /**
     * Returns the first argument for this command
     * 
     * @return the first argument for this command
     */
    public String getArgument1() {
        return argument1;
    };
    
    
    /**
     * Returns the second argument for this command
     * 
     * @return the second argument for this command
     */
    public int getArgument2() {
        return argument2;
    }
    
    
    /**
     * Sets the file name for this command
     * 
     * @param fileName       the file name to set for this command
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    
    /**
     * Returns the file name for this command
     * 
     * @return the file name for this command
     */
    public String getFileName() {
        return fileName;
    }
    
    
    /**
     * Sets the context for this command, which is that of the enclosing function, and is used to output a unique
     * symbols
     * 
     * @param context          the context to set for this command
     */
    public void setContext(String context) {
        this.context = context;
    }
    
    
    /**
     * Returns the context for this command
     * 
     * @return the context for this command
     */
    protected String getContext() {
        return context;
    }
    
    
    /**
     * Returns a string representation of this command
     * 
     * @return a string representation of this command
     */
    @Override
    public String toString() {
        return String.format("Command: { %s: %s }", getType(), getCommand());
    }
    
}
