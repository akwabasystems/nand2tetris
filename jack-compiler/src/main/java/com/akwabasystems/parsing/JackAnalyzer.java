
package com.akwabasystems.parsing;


import com.akwabasystems.model.OutputType;
import com.akwabasystems.utils.VMUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;



/**
 * A syntax analyzer for the Jack language.
 * 
 * This implementation operates on a given source, where that source is either a Jack file or a directory containing 
 * one or more such files. It creates a tokenizer for each file, which processes its contents, and then outputs
 * the required contents (XML tokens, XML abstract tree, generated VM code) to a destination file derived from 
 * the input file.
 *
 * It also uses an instance of an executor service in order to process several files concurrently. As a result, it
 * shuts down that executor instance after a given delay (TIMEOUT_IN_SECONDS) when all files have been processed, 
 * or when an exception occurs during processing. That delay can be fine-tuned to match a given environment.
 * 
 * This class removes all comments in the files prior to processing. It is also marked final, so it cannot
 * be subclassed.
 */
public final class JackAnalyzer implements Analyzer {
    private OutputType outputType = OutputType.CODE_GENERATION;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final int TIMEOUT_IN_SECONDS = 2;


    /**
     * Sets the output type for this analyzer, which specifies whether to output XML tokens, the XML tree of the code,
     * or the generate code.
     * 
     * @param outputType            the output type to set for this analyzer
     * @return a reference to this class instance
     */
    @Override
    public Analyzer setOutputType(OutputType outputType) {
        this.outputType = outputType;
        return this;
    }
    

    /**
     * Analyzes the given file
     * 
     * @param inputFile     the file to analyze
     * @return a reference to this class instance
     */
    @Override
    public Analyzer parse(final File inputFile) {

        if(!inputFile.exists()) {
            return this;
        }

        List<File> files = new ArrayList<>();

        if(inputFile.isDirectory()) {
            for(final File file : inputFile.listFiles()) {
                if(VMUtils.hasExtension(file, "jack")) {
                    files.add(file);
                }
            }
        } else {
            if(VMUtils.hasExtension(inputFile, "jack")) {
                files.add(inputFile);
            }
        }

        files.stream().forEach((file) -> {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleFile(file);
                }
            };
            
            executor.execute(task);
        });
 
        /** 
         * Wait for tasks to execute, then shut down the executor. If the current thread is also interrupted, re-cancel 
         * and preserve the interrupt status 
         */
        try {
            
            if(!executor.awaitTermination(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }

        } catch(InterruptedException interrupted) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        return this;
    }
    
    
    /**
     * Handles the given input file. If the file is a directory, then it recursively processes each file in
     * that directory.
     * 
     * @param file         the input file to handle
     */
    private void handleFile(final File file) {
        System.out.printf("Compiling file '%s'\n", file.getAbsolutePath());
        StringBuilder input = new StringBuilder();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while((line = reader.readLine()) != null) {
                input.append(line).append("\n");
            }

            reader.close();

            switch(outputType) {
                
                case XML_TOKENS:
                    writeXMLTokens(input.toString(), file);
                    break;
                
                case XML_TREE:
                    writeXMLTree(input.toString(), file);
                    break;
                    
                default:
                    writeGeneratedCode(input.toString(), file);
                    break;
                
            }

        } catch(IOException cannotRead) {
        }

    }
    
    
    /**
     * Writes the given string (XML token output) to the specified output file
     * 
     * @param input         the XML contents to write
     * @param file          the file to which to write the XML content
     */
    private void writeXMLTokens(String input, File file) {
        Tokenizer tokenizer = new JackTokenizer(input);
        String XMLOutput = tokenizer.toXML("tokens");

        String[] parts = StringUtils.split(file.getName(), ".");
        String outputFileName = String.format("%sT.xml", parts[0]);
        String outputFilePath = file.getAbsolutePath().replace(file.getName(), outputFileName);

        CodeWriter writer = new XMLTreeWriter();
        writer.writeToFile(XMLOutput, outputFilePath);
    }
    
    
    /**
     * Writes the given string (XML syntax tree) to the specified output file
     * 
     * @param input         the XML contents to write
     * @param file          the file to which to write the XML content
     */
    private void writeXMLTree(String input, File file) {
        Tokenizer tokenizer = new JackTokenizer(input);
        XMLCompilationEngine compiler = new XMLCompilationEngine(tokenizer);
        compiler.compileClass();

        String[] parts = StringUtils.split(file.getName(), ".");
        String outputFileName = String.format("%s.xml", parts[0]);
        String outputFilePath = file.getAbsolutePath().replace(file.getName(), outputFileName);

        CodeWriter writer = new XMLTreeWriter();
        writer.writeToFile(compiler.toXML(), outputFilePath);
    }
    
    
    /**
     * Writes the generated code to the specified output file
     * 
     * @param code          the code output to write
     * @param file          the file to which to write the XML content
     */
    private void writeGeneratedCode(String code, File file) {
        Tokenizer tokenizer = new JackTokenizer(code);
        
        String[] parts = StringUtils.split(file.getName(), ".");
        String outputFileName = String.format("%s.vm", parts[0]);
        String outputFilePath = file.getAbsolutePath().replace(file.getName(), outputFileName);
        
        VMCodeWriter codeWriter = new VMCodeWriter(outputFilePath);
        CodeCompilationEngine compiler = new CodeCompilationEngine(tokenizer, codeWriter);
        compiler.compileClass();

    }

}
