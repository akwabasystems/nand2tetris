
package com.akwabasystems.asm;


/**
 * A class that represents a C-instruction, which specifies what to compute, where to store the computed value,
 * and what to do next (proceed to the next instruction or jump to a specified instruction).
 */
public final class CInstruction extends Instruction {
    
    
    /**
     * Creates an instance of this class with the given command
     * 
     * @param command       the command to set for this class instance
     */
    public CInstruction(String command) {
        super(CommandType.C_COMMAND, command);
    }
    
    
    /**
     * Returns the binary code for this C-instruction. First, the logic  computes the different fields of the 
     * instruction accordingly to the following pattern: "dest=comp;jump". Second, it creates an instance of a Code
     * object using the fields in the command. Finally, it outputs the binary code depending on whether the command
     * uses the A register or the memory.
     * 
     * @return the binary code for this C-instruction
     */
    @Override
    public String binaryCode() {
        boolean hasExpression = command.contains("=");
        boolean hasJump = command.contains(";");

        String dest = "";
        String comp = "";
        String jump = "";
        boolean usesMemory = false;

        if(hasExpression || hasJump) {

            /** Handle the use case where there's a jump (for instance, D;JEQ) */
            if(hasJump) {
                String[] parts = command.split(";");
                jump = parts[1];
                String[] fields = parts[0].split("=");
                
                if(fields.length > 1) {
                    dest = fields[0];
                    comp = fields[1];
                } else {
                    comp = fields[0];
                }
                
            } else {
            
                /** Handle the use for normal expressions (for instance, M=1 or D=M) */
                String[] fields = command.split("=");
                dest = fields[0];
                comp = fields[1];
                
                /** 
                 * Check whether the right hand of the expression contains "M" (uses the memory rather than 
                 * the A register)
                 */
                usesMemory = comp.contains("M");
            }
            
        } else {
            /** 
             * At this point, it is safe to assume that the command consists of a "comp" part only
             * (for instance, "M=1")
             */
            comp = command;
        }
        
        Code code = new Code(dest, comp, jump);
        
        StringBuilder buffer = new StringBuilder("111");
        buffer.append((usesMemory)? "1" : "0");
        buffer.append(code.comp()).append(code.dest()).append(code.jump());

        return buffer.toString();

    }

}
