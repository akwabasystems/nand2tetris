
package com.akwabasystems.model;


/**
 * A class that represents a function declaration command (functionName n). Its logic defines a unique label for the
 * function to define, and initializes its local arguments to 0.
 */
public final class FunctionCommand extends AbstractVMCommand {
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public FunctionCommand(String command) {
        super(CommandType.C_FUNCTION, command);
    }

    
    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    @Override
    public String toAssemblyCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("(%s)", getArgument1()));
        int totalArguments = getArgument2();

        if(totalArguments > 0) {
            builder.append("\n");
            
            for(int i = 0; i < totalArguments; i++) {
                builder.append(Segment.CONSTANT.pushAssemblyCode(0));

                if(i != totalArguments - 1) {
                    builder.append("\n");
                }
            }
        }
        
        return builder.toString();
    }
}
