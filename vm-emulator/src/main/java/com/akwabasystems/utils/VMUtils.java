
package com.akwabasystems.utils;


/**
 * A class that contains a collection of utility methods used for various use cases.
 */
public class VMUtils {
    
    /**
     * Generates the assembly code for pushing a value onto the stack. The value is the one stored at the memory
     * location addressed by the current value of the stack pointer.
     * 
     * @return the assembly code for pushing a value onto the stack
     */
    public static String pushToStackAssemblyCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("@SP\n")
               .append("A=M\n")
               .append("M=D\n")
               .append(incrementStackPointer());
        
        return builder.toString();
    }
    
    
    /**
     * Generates the assembly code for pushing the base value of a given segment onto the stack.
     * 
     * @param segment           the segment whose value to push onto the stack
     * @return the assembly code for pushing the base value of a given segment onto the stack.
     */
    public static String pushSegmentToStackAssemblyCode(String segment) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("@%s\n", segment))
               .append("D=M\n")
               .append(pushToStackAssemblyCode());
        
        return builder.toString();
    }
    

    /**
     * Generates the assembly code for popping a value from the stack. The value is then stored in the D register for
     * further use.
     * 
     * @return the assembly code for popping the top-most value of the stack.
     */
    public static String popFromStackAssemblyCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("@SP\n")
               .append("AM=M-1\n")
               .append("D=M\n");
        
        return builder.toString();
    }
    
    
    /**
     * Generates the assembly code for incrementing the stack pointer
     * 
     * @return the assembly code for incrementing the stack pointer 
     */
    public static String incrementStackPointer() {
        StringBuilder builder = new StringBuilder();
        builder.append("@SP\n")
               .append("M=M+1");
        
        return builder.toString();
    }
    
    
    /**
     * Removes all comments from the given command
     * 
     * @param command           the command from which to remove the comments
     * @return a version of the given command stripped of all comments
     */
    public static String stripComments(String command) {
       return command.replaceAll("\\/\\/[^\n\r]*", "").trim();
    }
    
}
