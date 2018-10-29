
package com.akwabasystems.vm;

import java.io.IOException;


/**
 * An interface that defines the methods necessary for writing the translated VM code (the corresponding Hack 
 * assembly code) to a destination file. The name of the output file is derived from that of the processed VM file or
 * directory, but with the ".asm" extension.
 */
public interface CodeWriter {
    
    
    /**
     * Sets the file name for this code writer
     * 
     * @param fileName          the name to set for the code writer
     * @return a reference to the CodeWriter instance
     */
    CodeWriter setFileName(String fileName);


    /**
     * Writes the assembly code for the given code into the file for this instance
     * 
     * @param code              the code to write to file
     * @return a reference to the CodeWriter instance
     * @throws IOException if the file cannot be written
     */
    CodeWriter writeAssemblyCode(String code) throws IOException;
    
    
    /**
     * Closes the output stream for this instance
     */
    void close();
    
}
