
package com.akwabasystems.model;

import com.akwabasystems.utils.VMUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * A class that represents a function or method call (call functionName n). Its logic sets the values of the
 * appropriate segments (LCL, ARG, THIS, THAT) prior to invoking the target function. It also contains a registry
 * that keeps track of the invocation count for each function. This allows it to set a unique return address in case
 * a function is called recursively.
 */
public final class CallCommand extends AbstractVMCommand {
    
    private static final ConcurrentMap<String,Integer> registry = new ConcurrentHashMap<>();
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public CallCommand(String command) {
        super(CommandType.C_CALL, command);
    }

    
    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    @Override
    public String toAssemblyCode() {
        String functionName = getArgument1();
        String returnAddress = String.format("%s$ret", functionName);
        
        /** 
         * Map the function name to the number of times it has been invoked. This is useful for recursive calls,
         * as the return address for each call must be different.
         */
        int invocations = 0;
        
        if(registry.containsKey(functionName)) {
            invocations = registry.get(functionName);
        }
        
        registry.put(functionName, ++invocations);
        returnAddress = (invocations == 1)? returnAddress : String.format("%s%s", returnAddress, invocations);
        
        StringBuilder builder = new StringBuilder();
        
        /** 
         * Generate the code for the return address 
         * push return-address
         */
        builder.append(String.format("@%s\n", returnAddress))
               .append("D=A\n")
               .append(VMUtils.pushToStackAssemblyCode())
               .append("\n");

        /** 
         * Generate the code for saving the stack frame for the current context: 
         * push LCL
         * push ARG
         * push THIS
         * push THAT
         */
        String[] segments = { "LCL", "ARG", "THIS", "THAT" };
        
        for(String segment : segments) {
            builder.append(VMUtils.pushSegmentToStackAssemblyCode(segment));
            builder.append("\n");
        }

        /**
         * Generate the code for repositioning the ARG segment
         * ARG = SP - n - 5 (n = number of arguments) 
         */
        builder.append(String.format("@%s\n", getArgument2()))
               .append("D=A\n")
               .append("@5\n")
               .append("D=D+A\n")
               .append("@SP\n")
               .append("D=M-D\n")
               .append("@ARG\n")
               .append("M=D\n");
        
        /**
         * Generate the code for repositioning the LCL segment
         * LCL = SP
         */
        builder.append("@SP\n")
               .append("D=M\n")
               .append("@LCL\n")
               .append("M=D\n");
        
        /**
         * Generate the code for jumping to the called function
         * goto fn
         */
        builder.append(String.format("@%s\n", getArgument1()))
               .append("0;JMP\n");
        
        /**
         * Generate the code for the return address label
         * (return-address)
         */
        builder.append(String.format("(%s)", returnAddress));

        return builder.toString();
    }
    
}
