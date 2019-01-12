
package com.akwabasystems.vm;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * A class that implements the methods for writing the translated VM code (the corresponding Hack assembly code) to
 * a destination file. The name of the output file is derived from that of the processed VM file or directory, but 
 * with the ".asm" extension.
 */
public final class VMCodeWriter implements CodeWriter {
    
    private String fileName;
    private BufferedWriter writer;
    
    
    /**
     * Sets the file name for this code writer
     * 
     * @param fileName          the name to set for the code writer
     * @return a reference to the CodeWriter instance
     */
    @Override
    public CodeWriter setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    
    
    /**
     * Writes the specified assembly code to the given file
     * 
     * @param code              the code to write to file
     * @return a reference to the CodeWriter instance
     * @throws IOException if the file cannot be written
     */
    @Override
    public CodeWriter writeAssemblyCode(String code) throws IOException {
        File outputFile = new File(fileName);
        
        if(!outputFile.exists()) {
            outputFile.createNewFile();
            outputFile.setWritable(true, false);
        }

        if(writer == null) {
            writer = new BufferedWriter(new FileWriter(outputFile));
        }
        
        writer.write(code);
        System.out.printf("Assembly code saved successfully to '%s'\n", fileName);

        return this;
    }


    /**
     * Closes the output stream for this code writer instance
     */
    @Override
    public void close() {
        if(writer != null) {
            
            try {
                
                writer.close();
                
            } catch(IOException notClosed) {
                System.out.printf("Couldn't close output file - Cause: %s\n", notClosed.getMessage());
            }
        }
    }

}
