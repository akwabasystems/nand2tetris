
package com.akwabasystems.runtime;


import com.akwabasystems.vm.CodeWriter;
import com.akwabasystems.vm.VMParser;
import com.akwabasystems.vm.Parser;
import com.akwabasystems.vm.VMCodeWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;


/**
 * The entry file for the VMEmulator program. It reads a ".vm" file, or a directory containing ".vm" file, parses the
 * commands inside the file(s), and outputs the corresponding assembly files (".asm" extension).
 * 
 * Usage:
 *          java -jar VMEmulator-jar-with-dependencies [--no-bootstrap] <fileOrDirectory>
 * 
 */
public final class VMMain {

    private static boolean shouldBootstrap = true;
    
    private static final Function<File,Boolean> IsSysInitFile = (file) -> {
        return (file.getName().equalsIgnoreCase("Sys.vm") || 
                file.getName().equalsIgnoreCase("SysInit.vm"));
    };
    
    private static final Function<File,Boolean> HasVMExtension = (file) -> {
        String[] parts = StringUtils.split(file.getName(), ".");
        return (parts[parts.length - 1].equals("vm"));
    };
    
    
    /**
     * The entry point into the application
     * 
     * @param args          the argument list to this program
     * @throws Exception if the program cannot be executed
     */
    public static void main(String[] args) throws Exception {
        String inputFileArgument;
        
        if(args.length < 1) {
            StringBuilder buffer = new StringBuilder("\nUsage:\n");
            buffer.append("\tjava -jar VMEmulator-jar-with-dependencies [--no-bootstrap] <fileOrDirectory>\n")
                  .append("\n")
                  .append("Options:\n")
                  .append("\t--no-bootstrap\t\tPrevents the insertion of the bootstrap code\n")
                  .append("\t\t\t\t(SP=256; call Sys.init).\n")
                  .append("\t<fileOrDirectory>\tThe file or directory to parse. The output file name will be generated\n")
                  .append("\t\t\t\tby appending '.asm' to the file or directory name.\n");
            System.out.println(buffer.toString());
            return;
        }

        if(args.length == 2) {
            boolean hasNoBootstrapFlag = (args[0].equals("--no-bootstrap"));
            shouldBootstrap = !hasNoBootstrapFlag;
            inputFileArgument = args[1];
        } else {
            inputFileArgument = args[0];
        }
        
        /** Check whether there are VM files in the directory or the files are VM files */
        boolean isVMFile = false;
        File inputFile = new File(inputFileArgument);
        
        if(inputFile.exists()) {
            if(inputFile.isDirectory()) {
                  isVMFile = Stream.of(inputFile.listFiles())
                                   .anyMatch((file) -> HasVMExtension.apply(file));
            } else {
                isVMFile = HasVMExtension.apply(inputFile);
            }
        }
        
        if(!isVMFile) {
            String message = (inputFile.isDirectory())? "No VM files found in the specified directory" : 
                    "This file doesn't appear to be a valid VM file";
            System.out.println(String.format("Input error: %s\n", message));
            return;
        }

        handleInputFile(inputFile);
    }
    
    
    /**
     * Handles the given input file. If the file is a directory, then it recursively processes each file in
     * that directory. If the directory contains an initialization file (SysInit.vm or Sys.vm), then it reorders
     * all the files to make sure that the initialization file gets processed first,
     * 
     * @param inputFile         the input file to handle
     */
    private static void handleInputFile(final File inputFile) {
        String outputFileName;
        boolean isDirectory = inputFile.isDirectory();

        if(isDirectory) {
            outputFileName = String.format("%s.asm", inputFile.getName());
        } else {
            String[] parts = StringUtils.split(inputFile.getName(), ".");
            outputFileName = String.format("%s.asm", parts[0]);
        }
        
        /** Pre-process the file to make sure that any "Sys.vm" file, if present, gets processed first */ 
        List<File> files = new ArrayList<>();

        if(isDirectory) {
            Stream.of(inputFile.listFiles())
                    .filter(file -> IsSysInitFile.apply(file))
                    .forEach(file -> files.add(file));                             
        } else {
            files.add(inputFile);
        }
        
        /** 
         * Now add all other files to the set if this is a directory. In case the bootstrap file was added, 
         * make sure to skip it in the iteration
         */
        if(isDirectory) {
            for(File file : inputFile.listFiles()) {
                boolean endsWithVM = HasVMExtension.apply(file);
                boolean isBootstrapFile = IsSysInitFile.apply(file);
                
                if(!isBootstrapFile && endsWithVM) {
                    files.add(file);
                }
            }
        }
        
        String outputFilePath = (isDirectory)? String.format("%s/%s", inputFile.getAbsolutePath(), outputFileName) : 
                inputFile.getAbsolutePath().replace(inputFile.getName(), outputFileName);
        StringBuilder builder = new StringBuilder();

        files.stream().forEach((file) -> {
            builder.append(parseFile(file))
                   .append("\n");
        });

        writeAssemblyCode(builder.toString(), outputFilePath);
    }


    /**
     * Writes the assembly code for the VM file(s) that have been processed. If the "--no-bootstrap" option is not
     * provided (which is the default behavior), then it also outputs the bootstrap code (SP=256; call Sys.init)
     * 
     * @param assemblyCode          the assembly code generated by parsing the VM commands
     * @param outputFile            the name of the destination file for the code
     */
    private static void writeAssemblyCode(String assemblyCode, String outputFile) {
        CodeWriter writer = new VMCodeWriter();
        StringBuilder builder = new StringBuilder();
        
        if(shouldBootstrap) {
            builder.append(VMParser.bootstrapCode());
        }

        builder.append(assemblyCode);

        try {

            writer.setFileName(outputFile).writeAssemblyCode(builder.toString());

        } catch(IOException cannotRead) {
            System.out.printf("Couldn't access input or output files - Cause: %s\n", cannotRead.getMessage());
        } finally {
           writer.close();
        }
    }


    /**
     * Processes the given file, and returns the assembly code from its VM commands.
     * 
     * @param inputFile         the file to process
     * @return the assembly code from the VM commands in the given file
     */
    private static String parseFile(final File file) {
        BufferedReader reader;
        StringBuilder assemblyCode = new StringBuilder();
        
        try {

            Parser parser = new VMParser();
            parser.setFileName(file.getName());
            reader = new BufferedReader(new FileReader(file));
            String line;

            while((line = reader.readLine()) != null) {
                parser.parse(line);
            }

            reader.close();

            assemblyCode.append(parser.assemblyCode());

        } catch(IOException cannotRead) {
            System.out.printf("Couldn't access input or output files - Cause: %s\n", cannotRead.getMessage());
        }
        
        return assemblyCode.toString();
    }
}

