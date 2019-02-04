
package com.akwabasystems.vm;


import com.akwabasystems.model.CommandType;
import com.akwabasystems.model.VMCommand;
import com.akwabasystems.utils.VMUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


/**
 * A parser for the Hack Virtual Machine. 
 * 
 * It handles the parsing of the VM commands in a file, or in files in a given directory, and generates the 
 * corresponding assembly code representation of those commands. The resulting assembly code is then written to a file
 * whose name is derived from the target file or directory.
 * 
 * This implements uses the following logic: first, it generates a VMCommand object for each parsed command; second, it
 * adds the VMCommand object to a list; and finally, it iterates over the list and translate its command to its
 * corresponding assembly code representation. The final assembly code is then output to a file.
 * 
 * This class removes all in-line comments contained in the files prior to processing them.
 */
public final class VMParser implements Parser {
    
    private final List<VMCommand> commands = Collections.synchronizedList(new ArrayList<>());
    private String fileName;
    private final Stack<String> contexts = new Stack<>();
    private static boolean debugEnabled = false;
    private static boolean bootstrapEnabled = true;

    
    /**
     * Default constructor. Initializes the function context to "Main".
     */
    public VMParser() {
        super();
        contexts.add("Main");
    }


    /**
     * Parses the given command and adds its corresponding VMCommand object to the command list. It sets the context
     * of each command to the appropriate one when entering and leaving a function.
     *
     * @param command       the command to parse
     */
    @Override
    public void parse(String command) {
        String syntax = VMUtils.stripComments(command);
        boolean isComment = syntax.startsWith("//") || (syntax.startsWith("/*") && syntax.endsWith("*/"));
        boolean isValidCommand = (!syntax.isEmpty() && !isComment);
        VMCommand currentCommand = null;

        if(isValidCommand) {
            currentCommand = CommandType.fromCommand(command);

            /** Set the current context to the parsed function */
            boolean isFunctionCall = (currentCommand.getType() == CommandType.C_CALL);
            boolean isFunctionReturn = (currentCommand.getType() == CommandType.C_RETURN);
            
            if(isFunctionCall) {
                onFunctionEnter(currentCommand.getArgument1());
            } else if(isFunctionReturn) {
                onFunctionExit();
            }

            currentCommand.setContext(contexts.peek());
            commands.add(currentCommand);
        }
    }
    
    
    /**
     * Sets the file name for this parser
     * 
     * @param fileName          the name to set for the parser
     * @return a reference to the Parser instance
     */
    @Override
    public Parser setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    
    
    /**
     * A method invoked when the parser enters a function context. It sets the current context to that of the function.
     * 
     * @param functionName      the name of the function that was entered
     */
    private void onFunctionEnter(String functionName) {
        contexts.add(functionName);
    }
    
    
    /**
     * A method invoked when the parser exits a function context. It sets the current context to the one prior to 
     * entering the function
     */
    private void onFunctionExit() {
        boolean shouldRemoveContext = (contexts.size() > 1);
        
        if(shouldRemoveContext) {
            contexts.pop();
        }
    }

    
    /**
     * Returns the current function context for this parser
     * 
     * @return the current function context for this parser
     */
    @Override
    public String currentFunctionContext() {
        return contexts.peek();
    }
    
    
    /**
     * Specifies whether debugging should be enabled for this parser
     * 
     * @param isDebugEnabled        a flag that specifies whether to enable debugging for this parser
     * @return a reference to the Parser instance
     */
    @Override
    public VMParser setDebugEnabled(boolean isDebugEnabled) {
        debugEnabled = isDebugEnabled;
        return this;
    }


    /**
     * Returns true if debugging is enabled for this parser. Otherwise, returns false
     *
     * @return true if debugging is enabled for this parser. Otherwise, returns false
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }


    /**
     * Specifies whether this parser should output the bootstrapping code
     *
     * @param isBootstrapEnabled      a flag that specifies whether to output the bootstrapping code
     * @return a reference to the Parser instance
     */
    @Override
    public VMParser shouldBootstrap(boolean isBootstrapEnabled) {
        bootstrapEnabled = isBootstrapEnabled;
        return this;
    }


    /**
     * Returns true if this parser outputs the bootstrapping code; otherwise, returns false
     *
     * @return true if this parser outputs the bootstrapping code; otherwise, returns false
     */
    public boolean isBootstrapEnabled() {
        return bootstrapEnabled;
    }


    /**
     * Returns the assembly code for bootstrapping the application. The logic initializes the main segments
     * (SP, LCL, ARG, THIS, THAT) to sensible defaults; it then invokes the system init function (call Sys.init).
     * 
     * @return the assembly code for bootstrapping the application
     */
    public static String bootstrapCode() {
        StringBuilder builder = new StringBuilder();
        
        if(debugEnabled) {
            builder.append("// Bootstrap\n");
        }
        
        builder.append(initializeSegment("SP", 261))
               .append(initializeSegment("LCL", 261))
               .append(initializeSegment("ARG", 256))
               .append(initializeSegment("THIS", 0))
               .append(initializeSegment("THAT", 0));

        builder.append("@Sys.init\n")
               .append("0;JMP\n");

        return builder.toString();
    }

    
    /**
     * Returns the assembly code for initializing the given segment to the specified value
     * 
     * @param segment       the segment to initialize
     * @param value         the value to set for the segment    
     * @return the assembly code for initializing the given segment to the specified value
     */
    private static String initializeSegment(String segment, int value) {
        StringBuilder builder = new StringBuilder();
        
        builder.append(String.format("@%s\n", value))
               .append("D=A\n")
               .append(String.format("@%s\n", segment))
               .append("M=D\n");
        
        return builder.toString();
    }
    
    
    /**
     * Returns the assembly code from the parsed VM commands
     * 
     * @return the assembly code from the parsed VM commands
     */
    @Override
    public String assemblyCode() {
        StringBuilder buffer = new StringBuilder();

        synchronized(commands) {
            commands.stream().forEach((command) -> {
                command.setFileName(fileName);
                String assemblyCode = command.toAssemblyCode();

                if(!assemblyCode.isEmpty()) {
                    if(this.isDebugEnabled()) {
                        buffer.append(String.format("// %s\n", command.getCommand()));
                    }

                    buffer.append(assemblyCode);
                    boolean isLast = (commands.indexOf(command) == commands.size() - 1);

                    if(!isLast) {
                        buffer.append("\n");
                    }
                }
            });
        }

        return buffer.toString();

    }

}
