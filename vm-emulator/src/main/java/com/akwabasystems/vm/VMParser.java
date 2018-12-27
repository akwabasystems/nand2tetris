
package com.akwabasystems.vm;


import com.akwabasystems.model.CallCommand;
import com.akwabasystems.model.CommandType;
import com.akwabasystems.model.VMCommand;
import com.akwabasystems.utils.VMUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
    
    private final List<VMCommand> commands = Collections.synchronizedList(new ArrayList<VMCommand>());
    private String fileName;
    private final Stack<String> contexts = new Stack<>();
    
    
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
            boolean isFunction = (currentCommand.getType() == CommandType.C_FUNCTION);
            boolean isReturn = (currentCommand.getType() == CommandType.C_RETURN);

            if(isFunction) {
                onFunctionEnter(currentCommand.getArgument1());
            } else if(isReturn) {
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
     * Initializes the different memory segments prior to executing the program instructions. For instance, the 
     * stack pointer is set to memory location 256, with the LCL, ARG, THIS, and THAT segments retaining their 
     * default values.
     * 
     * @return the assembly code for initializing the different memory segments.
     */
    public static String initializeStackPointer() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("@256\n")
               .append("D=A\n")
               .append("@SP\n")
               .append("M=D\n");
        
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
     * Returns the assembly code for bootstrapping the application. The bootstrap code consists of initializing the
     * value of the stack pointer (SP=256) and invoking the system init function (call Sys.init).
     * 
     * @return the assembly code for bootstrapping the application
     */
    public static String bootstrapCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(initializeSegment("SP", 256));

        VMCommand initCommand = new CallCommand("call Sys.init 0");
        builder.append(initCommand.toAssemblyCode())
               .append("\n");

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
