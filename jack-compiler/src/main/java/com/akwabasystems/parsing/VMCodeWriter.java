
package com.akwabasystems.parsing;


import com.akwabasystems.model.ArithmeticCommand;
import com.akwabasystems.model.Segment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * An implementation of the CodeWriter interface that writes Jack code to a given destination file. It expects the
 * destination file to exist, and to have an extension that matches its content type. However, as a safety measure, it 
 * still checks for the existence of the file, and creates it accordingly. 
 */
public class VMCodeWriter {

    private final String fileName;
    private File outputFile;
    private BufferedWriter writer;
    
    
    public VMCodeWriter(String fileName) {
        this.fileName = fileName;
        prepareForWriting();
    }
    

    private void prepareForWriting() {
        System.out.println("------ VMCodeWriter: preparing for writing...");
        if(outputFile == null || writer == null) {
            
            try {
            
                outputFile = new File(fileName);

                if(!outputFile.exists()) {
                    outputFile.createNewFile();
                    outputFile.setWritable(true, false);
                }
                
                writer = new BufferedWriter(new FileWriter(outputFile));
                
            } catch(IOException notCreated) {
            }
        }
    }
    
    
    public void writeClass(String name) {
        System.out.println("WRITE_CLASS - Name: " + name);
        writeToFile(name);
    }
    
    
    public void initializeThis() {
        writeToFile("push argument 0\npop pointer 0\n");
    }
    
    
    public void writePush(Segment segment, int index) {
        System.out.printf("WRITE_PUSH - Segment: %s - index: %s\n", segment, index);
    }
    
    
    public void writePushConstant(int constant) {
        System.out.printf("WRITE_PUSH_CONSTANT - : %s\n", constant);
        writeToFile(String.format("push constant %s\n", constant));
    }
    
    
    public void writePop(Segment segment, int index) {
        System.out.printf("WRITE_POP - Segment: %s - index: %s\n", segment, index);
    }
    
    
    public void writeArithmetic(ArithmeticCommand command) {
        System.out.printf("WRITE_ARITHMETIC - Command: %s\n", command);
    }
    
    
    public void writeLabel(String label) {
        System.out.printf("WRITE_LABEL: %s\n", label);
    }
    
    
    public void writeGoto(String label) {
        System.out.printf("WRITE_GOTO: %s\n", label);
    }
    
    
    public void writeIf(String label) {
        System.out.printf("WRITE_IF: %s\n", label);
    }
    
    
    public void writeCall(String name, int nArgs) {
        System.out.printf("WRITE_CALL - Name: %s - nAgrs: %s\n", name, nArgs);
        writeToFile(String.format("call %s %s\n", name, nArgs));
    }
    
    
    public void writeFunction(String name, int nLocals) {
        System.out.printf("WRITE_FUNCTION - Name: %s - nLocals: %s\n", name, nLocals);
        String statement = String.format("function %s %s\n", name, nLocals);
        writeToFile(statement);
    }
    
    
    public void writeExpression(String expression) {
        System.out.printf("WRITE_EXPRESSION: %s\n", expression);
        // String statement = String.format("function %s %s\n", name, nLocals);
        // writeToFile(statement);
    }
    
    
    public void writeOperator(String op) {
        switch (op) {
        
            case "+":
                writeToFile("add\n");
                break;
                
            case "-":
                writeToFile("sub\n");
                break;
                
            case "*":
                writeCall("Math.multiply", 2);
                break;
                
            default:
                break;
        }
    }


    public void writeReturn() {
        System.out.println("WRITE_RETURN...");
        writeToFile("return\n");
    }
    
    
    /**
     * Writes the given code to the file with the specified name
     * 
     * @param code              the code to write
     * @param fileName          the name of the file to which to write the code
     */
    //@Override
    public void writeToFile(String text) {
        
        try {

            writer.append(text);

        } catch(IOException cannotWrite) {
            System.out.println("Could not write to file - Exception: " + cannotWrite);
        }

    }
    
    
    
    public void close() {
        System.out.println("<<<---- Closing writer...");

        if (writer != null) {
            try {
                
                writer.close();
                
            } catch(IOException cannotClose) {
            }
        }
    }
    

}
