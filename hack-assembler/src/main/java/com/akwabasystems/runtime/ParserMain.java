
package com.akwabasystems.runtime;


import com.akwabasystems.asm.AssemblyParser;
import com.akwabasystems.asm.Parser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;


/**
 * The entry file for the HackAssembler program. It reads an assembly file (with a ".asm" extension), parses it, and
 * output its binary code in a new file with the same name and the ".hack" extension.
 * 
 * Usage:
 *          java -jar HackAssembler-jar-with-dependencies <inputFile>
 * 
 */
public class ParserMain {
    
    public static void main(String[] args) throws Exception {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        
        if(args.length < 1) {
            StringBuilder buffer = new StringBuilder("\nUsage:\n");
            buffer.append("\tjava -jar HackAssembler-jar-with-dependencies <inputFile>\n");
            System.out.println(buffer.toString());
            return;
        }
        
        try {
            
            String inputFilePath = args[0];
            File file = new File(inputFilePath);
            
            if(!file.isFile()) {
                System.out.printf("'%s' doesn't appear to be a valid file\n", inputFilePath);
                return;
            }
            
            System.out.printf("Processing '%s'...\n", file.getName());
            String[] parts = StringUtils.split(file.getName(), ".");
            String outputFileName = String.format("%s.hack", parts[0]);
            String outputFilePath = inputFilePath.replace(file.getName(), outputFileName);
            
            Parser parser = new AssemblyParser();
            reader = new BufferedReader(new FileReader(file));
            String line;

            while((line = reader.readLine()) != null) {
                parser.parse(line);
            }
            
            File outputFile = new File(outputFilePath);
            
            if(!outputFile.exists()) {
                outputFile.createNewFile();
                outputFile.setWritable(true, false);
            }
            
            writer = new BufferedWriter(new FileWriter(outputFilePath));
            writer.write(parser.binaryCode());
            System.out.printf("The binary code for '%s' has been saved successfully to '%s'\n", 
                    inputFilePath, outputFilePath);
            
        } catch(IOException cannotRead) {
            System.out.printf("Couldn't access input or output files - Cause: %s\n", cannotRead.getMessage());
        } finally {
            if(reader != null) {
                reader.close();
            }
            
            if(writer != null) {
                writer.close();
            }
        }
    }
    
}
