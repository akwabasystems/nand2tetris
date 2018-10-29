
package com.akwabasystems.model;

import com.akwabasystems.utils.VMUtils;


/**
 * A class that represents a return command (return). It resets the state of the different memory segments to their
 * saved values prior to jumping to its target destination.
 */
public final class ReturnCommand extends AbstractVMCommand {
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public ReturnCommand(String command) {
        super(CommandType.C_RETURN, command);
    }

    
    /**
     * Returns the assembly code for this command
     * 
     * @return the assembly code for this command
     */
    public String toAssemblyCode() {
        StringBuilder builder = new StringBuilder();

        /** 
         * Generate the code for saving the current state of the LCL segment
         * FRAME = LCL
         */
        builder.append("@LCL\n")
               .append("D=M\n")
               .append("@R13\n")
               .append("M=D\n");
        
        /** 
         * Generate the code for saving the return value in a temporary variable
         * RET = *(FRAME - 5)
         */
        builder.append("@5\n")
               .append("AD=D-A\n")
               .append("D=M\n")
               .append("@R14\n")
               .append("M=D\n");
        
        /**
         * Generate the code for repositioning the return value for the caller
         * *ARG = pop()
         */
        builder.append(VMUtils.popFromStackAssemblyCode())
               .append("@ARG\n")
               .append("A=M\n")
               .append("M=D\n");

        /**
         * Generate the code for restoring the stack pointer of the caller
         * SP = ARG + 1
         */
        builder.append("@ARG\n")
               .append("D=M+1\n")
               .append("@SP\n")
               .append("M=D\n");
        
        /**
         * Generate the code for restoring the appropriate segments of the caller (LCL, ARG, THIS, THAT)
         * THAT = *(FRAME - 1)
         * THIS = *(FRAME - 2)
         * ARG = *(FRAME - 3)
         * LCL = *(FRAME - 4)
         */
        int i = 1;
        String[] segments = { "THAT", "THIS", "ARG", "LCL" };
        
        for(String segment : segments) {
            builder.append(String.format("@%s\n", i++))
                   .append("D=A\n")
                   .append("@R13\n")
                   .append("A=M-D\n")
                   .append("D=M\n")
                   .append(String.format("@%s\n", segment))
                   .append("M=D\n");
        }

        /**
         * Generate the code for jumping to the return address
         * goto RET
         */
        builder.append("@R14\n")
               .append("A=M\n")
               .append("0;JMP");


        /**
         * Generate the code for the return address
         * (return-address)
         */
        return builder.toString();
    }

}
