
package com.akwabasystems.parsing;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * An implementation of the CodeWriter interface that writes Jack code to a given destination file. It expects the
 * destination file to exist, and to have an extension that matches its content type. However, as a safety measure, it 
 * still checks for the existence of the file, and creates it accordingly. 
 */
public final class JackCodeWriter implements CodeWriter {


    /**
     * Writes the given code to the file with the specified name
     * 
     * @param code              the code to write
     * @param fileName          the name of the file to which to write the code
     */
    public void writeToFile(String code, String fileName) {

        try {

            File outputFile = new File(fileName);

            if(!outputFile.exists()) {
                outputFile.createNewFile();
                outputFile.setWritable(true, false);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(code);
            writer.close();

            System.out.printf("Code written successfully to '%s'\n", fileName);

        } catch(IOException cannotWrite) {
        }

    }

}
