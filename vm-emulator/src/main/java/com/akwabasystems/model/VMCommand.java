
package com.akwabasystems.model;


/**
 * An interface that represents a Virtual Machine command.
 * 
 * A command is of a given type (arithmetic, push, pop, etc), and contains a name as well as a first or second
 * argument based on that type. This class provides the necessary methods for returning those arguments; it also
 * contains methods for setting the name of the file being processed, and for generating the assembly code for a
 * given command.
 */
public interface VMCommand {
    
    /**
     * Returns the type for this command
     * 
     * @return the type for this command
     */
    CommandType getType();

    
    /**
     * Returns the syntax for this command
     * 
     * @return the syntax for this command
     */
    String getCommand();

    
    /**
     * Returns the first argument for this command
     * 
     * @return the first argument for this command
     */
    String getArgument1();
    
    
    /**
     * Returns the second argument for this command
     * 
     * @return the second argument for this command
     */
    int getArgument2();


    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    String toAssemblyCode();
    
    
    /**
     * Sets the name of the file for this command
     * 
     * @param fileName          the name of the file to set for this command
     */
    void setFileName(String fileName);
    
    
    /**
     * Sets the context for this command, which is that of the enclosing function, and is used to output unique
     * symbols and/or labels
     * 
     * @param context          the context to set for this command
     */
    void setContext(String context);

}
