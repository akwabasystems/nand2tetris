
package com.akwabasystems.parsing;


/**
 * An interface that defines the methods necessary for writing code to a destination file. The file writing method
 * does not specify any exception, meaning that the implementation code must handle those possible exceptions 
 * internally.
 */
public interface CodeWriter {

    /**
     * Writes the given code to the file with the specified name
     * 
     * @param code              the code to write
     * @param fileName          the name of the file to which to write the code
     */
    void writeToFile(String code, String fileName);

}
