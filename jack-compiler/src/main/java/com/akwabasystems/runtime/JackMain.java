
package com.akwabasystems.runtime;

import com.akwabasystems.model.OutputType;
import com.akwabasystems.parsing.JackAnalyzer;
import java.io.File;
import org.apache.commons.lang.StringUtils;
import com.akwabasystems.parsing.Analyzer;
import com.akwabasystems.utils.VMUtils;


/**
 * The entry file for the JackCompiler program. It reads a ".jack" file, or a directory containing one or more such 
 * files, parses the code within the file(s), and outputs either the XML structure or the abstract syntax tree 
 * from the code based on the command-line arguments.
 * 
 * Usage:
 *          java -jar JackCompiler-jar-with-dependencies [--xml-tokens] [--xml-tree] <fileOrDirectory>
 * 
 * Options:
 *      --xml-tokens        Outputs an XML file with the tokens contained in the input code
 *      --xml-tree          Outputs an XML file with the syntax tree of the input code
 *                          Both options are mutually exclusive
 * 
 *      fileOrDirectory     The file or directory to parse. Each ".jack" file will be parsed into its equivalent
 *                          ".xml" or ".vm" output
 * 
 */
public final class JackMain {

    private static OutputType outputType = OutputType.CODE_GENERATION;


    /**
     * The entry point into the application. Processes the command-line arguments and writes the proper output to
     * the destination file.
     * 
     * @param args          the argument list to this program
     * @throws Exception if the program cannot be executed
     */
    public static void main(String[] args) throws Exception {
        String inputFileArgument;
        
        if(args.length < 1) {
            StringBuilder buffer = new StringBuilder("\nUsage:\n");
            buffer.append("\tjava -jar JackCompiler-jar-with-dependencies [--xml-tokens] [--xml-tree] <fileOrDirectory>\n")
                  .append("\n")
                  .append("Options:\n")
                  .append("\t--xml-tokens\t\tOutputs an XML file with the tokens contained in the input code\n")
                  .append("\t--xml-tree\t\tOutputs an XML file with the syntax tree of the input code\n")
                  .append("\t\t\t\tBoth options are mutually exclusive\n")
                  .append("\t<fileOrDirectory>\tThe file or directory to parse. Each \".jack\" file will be parsed \n")
                  .append("\t\t\t\tinto its equivalent \".xml\" or \".vm\" output.\n");
            System.out.println(buffer.toString());
            return;
        }

        if(args.length == 2) {
            boolean shouldOutputXMLTokens = (args[0].equals("--xml-tokens"));
            boolean shouldOutputXMLTree = (args[0].equals("--xml-tree"));
            
            if(shouldOutputXMLTokens) {
                outputType = OutputType.XML_TOKENS;
            } else if(shouldOutputXMLTree) {
                outputType = OutputType.XML_TREE;
            }
            
            inputFileArgument = args[1];
        } else {
            inputFileArgument = args[0];
        }
        
        boolean isValidInput = false;
        File inputFile = new File(inputFileArgument);
        
        if(inputFile.exists()) {
            if(inputFile.isDirectory()) {
                for(File file : inputFile.listFiles()) {
                    if(VMUtils.hasExtension(file, "jack")) {
                        isValidInput = true;
                        break;
                    }
                }
            } else {
                String[] parts = StringUtils.split(inputFile.getName(), ".");
                isValidInput = (parts[parts.length - 1].equals("jack"));
            }
        }
        
        if(!isValidInput) {
            String message = (inputFile.isDirectory())? "No Jack files found in the specified directory" : 
                    "This file doesn't appear to be a valid Jack file";
            System.out.println(String.format("\nError: %s\n", message));
            return;
        }

        Analyzer analyzer = new JackAnalyzer();
        analyzer.setOutputType(outputType).parse(inputFile);
    }

}
