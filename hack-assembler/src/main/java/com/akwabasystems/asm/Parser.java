
package com.akwabasystems.asm;


import java.util.List;


/**
 * An interface that defines the methods implemented by assembly parsers
 */
public interface Parser {
    
    
    /**
     * Parses the given command and adds it to the instruction list
     * 
     * @param command       the command to parse 
     */
    void parse(String command);
    
    
    /**
     * Returns the instruction list from the parsed commands of an assembly code
     * 
     * @return the instruction list from the parsed commands of an assembly code
     */
    List<Instruction> getInstructions();
    
    
    /**
     * Returns the binary code of an assembly program
     * 
     * @return the binary code of an assembly program
     */
    String binaryCode();
    
}
