
package com.akwabasystems.parsing;


import com.akwabasystems.model.Segment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * An implementation of the CodeWriter interface that writes Jack code to a given destination file. It expects the
 * destination file to exist, and to have an extension that matches its content type. However, as a safety measure, it 
 * still checks for the existence of the file and creates it if it doesn't exist. 
 */
public class VMCodeWriter {

    private final String fileName;
    private File outputFile;
    private BufferedWriter writer;
    

    public VMCodeWriter(String fileName) {
        this.fileName = fileName;
        prepareForWriting();
    }
    

    /**
     * Prepares this class for writing by initializing its I/O resources
     */
    private void prepareForWriting() {
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
    
    
    /**
     * Outputs VM code for a class name
     *
     * @param name    the name of the class to output
     */
    public void writeClass(String name) {
        writeToFile(name);
    }
    
    
    /**
     * Outputs VM code for initializing the "this" keyword inside an instance method
     */
    public void initializeThis() {
        writeToFile("push argument 0\npop pointer 0\n");
    }
    
    
    /**
     * Outputs VM code for pushing a given segment index on the stack
     *
     * @param segment     the segment whose index to push
     * @param index       the index to push
     */
    public void writePush(Segment segment, int index) {
        writeToFile(String.format("push %s %s\n", segment.argument(), index));
    }
    
    
    /**
     * Outputs VM code for pushing an integer constant on the stack
     *
     * @param constant      the constant to push
     */
    public void writePushConstant(int constant) {
        writeToFile(String.format("push constant %s\n", constant));
    }
    
    
    /**
     * Outputs VM code for popping a given segment index from the stack
     *
     * @param segment     the segment whose index to pop
     * @param index       the index to pop
     */
    public void writePop(Segment segment, int index) {
        writeToFile(String.format("pop %s %s\n", segment.argument(), index));
    }
    

    /**
     * Outputs VM code for declaring a label
     *
     * @param label       the label to declare
     */
    public void writeLabel(String label) {
        writeToFile(String.format("label %s\n", label));
    }
    
    
    /**
     * Outputs VM code for an "if-goto" label
     *
     * @param label       the "if-goto" label to output
     */
    public void writeIfGoto(String label) {
        writeToFile(String.format("if-goto %s\n", label));
    }
    

    /**
     * Outputs VM code for a "goto" label
     *
     * @param label       the "goto" label to output
     */
    public void writeGoto(String label) {
        writeToFile(String.format("goto %s\n", label));
    }
    
    
    /**
     * Outputs VM code for invoking a function
     *
     * @param functionName      the name of the function to invoke
     * @param nArgs             the number of arguments to pass to the function
     */
    public void writeCall(String functionName, int nArgs) {
        writeToFile(String.format("call %s %s\n", functionName, nArgs));
    }
    
    
    /**
     * Outputs VM code for declaring a function
     *
     * @param functionName      the name of the function to declare
     * @param nLocals           the number of local variables for the function
     */
    public void writeFunction(String functionName, int nLocals) {
        String statement = String.format("function %s %s\n", functionName, nLocals);
        writeToFile(statement);
    }
    
    
    /**
     * Outputs VM code for the specified arithmetic or logical operator ("+", "-", "<", ">", etc)
     *
     * @param op          the operator for which to output the code
     */
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
                
            case "/":
                writeCall("Math.divide", 2);
                break;

            case "&":
              writeToFile("and\n");
              break;

            case "|": 
              writeToFile("or\n");
              break;
              
            case "<":
              writeToFile("lt\n");
              break;

            case ">":
              writeToFile("gt\n");
              break;
              
            case "=":
              writeToFile("eq\n");
              break;
                
            default:
                break;
        }
    }


    /**
     * Outputs VM code for pushing a keyword constant on the stack ("true", "false", "this", or "null")
     *
     * @param constant      the constant to push
     */
    public void writeKeywordConstant(String constant) {

        switch (constant) {
        
            case "true":
                writeToFile("push constant 0\nnot\n");
                break;
                
            case "false":
                writeToFile("push constant 0\n");
                break;
                
            case "null":
                /** To-Do: To be implemented */
                writeToFile("[null]\n");
                break;

            case "this":
                /** To-Do: To be implemented */
                writeToFile("[this]\n");
                break;

            default:
                break;
        }
    }


    /**
     * Outputs VM code for the specified unary operator
     *
     * @param op          the unary operator for which to output the code
     */
    public void writeUnaryOperator(String op) {
        writeToFile(op.equals("-")? "neg\n" : "not\n");
    }


    /**
     * Outputs VM code for a return statement
     */
    public void writeReturn() {
        writeToFile("return\n");
    }
    
    
    /**
     * Writes the given text to the output file
     * 
     * @param text              the text to write
     */
    public void writeToFile(String text) {
        
        try {

            writer.append(text);

        } catch(IOException cannotWrite) {
            System.out.println("Could not write to file - Exception: " + cannotWrite);
        }

    }
    

    /**
     * Closes the output stream for this writer
     */
    public void close() {

        if (writer != null) {
            try {
                
                writer.close();
                
            } catch(IOException cannotClose) {
            }
        }
    }

}
