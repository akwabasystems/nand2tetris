
package com.akwabasystems.model;

import com.akwabasystems.utils.VMUtils;
import java.util.concurrent.atomic.AtomicLong;


/**
 * An enumeration of the different arithmetic command types. Each constant has a method for the assembly code
 * associated with its function.
 */
public enum ArithmeticCommandType {
    
    ADD {
        
        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();

            buffer.append("@SP\n")
                  .append("AM=M-1\n")
                  .append("D=M\n")
                  .append("@SP\n")
                  .append("AM=M-1\n")
                  .append("MD=D+M\n")
                  .append("@SP\n")
                  .append("M=M+1");

            return buffer.toString();
        }
        
    },
    
    SUB {
        
        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();

            buffer.append("@SP\n")
                  .append("AM=M-1\n")
                  .append("D=M\n")
                  .append("@SP\n")
                  .append("AM=M-1\n")
                  .append("MD=M-D\n")
                  .append("@SP\n")
                  .append("M=M+1");

            return buffer.toString();
        }
        
    },
    
    NEG {
        
        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();

            buffer.append("@SP\n")
                  .append("AM=M-1\n")
                  .append("M=-M\n")
                  .append("@SP\n")
                  .append("M=M+1");

            return buffer.toString();
        }
        
    },
    
    EQ {
        
        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();
            Long increment = counter.incrementAndGet();

            buffer.append("    @SP\n")
                  .append("    AM=M-1\n")
                  .append("    D=M\n")
                  .append("    @SP\n")
                  .append("    AM=M-1\n")
                  .append("    D=M-D\n")
                  .append(String.format("    @IF_EQUAL_TO%s\n", increment))
                  .append("    D;JEQ\n")
                  .append("    @SP\n")
                  .append("    A=M\n")
                  .append("    M=0\n")
                  .append(String.format("    @IF_EQUAL_TO_END%s\n", increment))
                  .append("    0;JMP\n")
                  .append(String.format("(IF_EQUAL_TO%s)\n", increment))
                  .append("    @SP\n")
                  .append("    A=M\n")
                  .append("    M=-1\n")
                  .append(String.format("(IF_EQUAL_TO_END%s)\n", increment))
                  .append("    @SP\n")
                  .append("    M=M+1");

            return buffer.toString();   
        }

    },


    GT {

        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();
            Long increment = counter.incrementAndGet();
            
            buffer.append("    @SP\n")
                  .append("    AM=M-1\n")
                  .append("    D=M\n")
                  .append("    @SP\n")
                  .append("    AM=M-1\n")
                  .append("    D=M-D\n")
                  .append(String.format("    @IF_GREATER_THAN%s\n", increment))
                  .append("    D;JGT\n")
                  .append("    @SP\n")
                  .append("    A=M\n")
                  .append("    M=0\n")
                  .append(String.format("    @IF_GREATER_THAN_END%s\n", increment))
                  .append("    0;JMP\n")
                  .append(String.format("(IF_GREATER_THAN%s)\n", increment))
                  .append("    @SP\n")
                  .append("    A=M\n")
                  .append("    M=-1\n")
                  .append(String.format("(IF_GREATER_THAN_END%s)\n", increment))
                  .append("    @SP\n")
                  .append("    M=M+1");

            return buffer.toString();
        }

    },
    
    
    LT {
        
        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();
            Long increment = counter.incrementAndGet();
            
            buffer.append("    @SP\n")
                  .append("    AM=M-1\n")
                  .append("    D=M\n")
                  .append("    @SP\n")
                  .append("    AM=M-1\n")
                  .append("    D=M-D\n")
                  .append(String.format("    @IF_LESS_THAN%s\n", increment))
                  .append("    D;JLT\n")
                  .append("    @SP\n")
                  .append("    A=M\n")
                  .append("    M=0\n")
                  .append(String.format("    @IF_LESS_THAN_END%s\n", increment))
                  .append("    0;JMP\n")
                  .append(String.format("(IF_LESS_THAN%s)\n", increment))
                  .append("    @SP\n")
                  .append("    A=M\n")
                  .append("    M=-1\n")
                  .append(String.format("(IF_LESS_THAN_END%s)\n", increment))
                  .append("    @SP\n")
                  .append("    M=M+1");

            return buffer.toString();
        }
        
    },


    AND {
        
        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();

            buffer.append("@SP\n")
                  .append("AM=M-1\n")
                  .append("D=M\n")
                  .append("@SP\n")
                  .append("AM=M-1\n")
                  .append("MD=D&M\n")
                  .append("@SP\n")
                  .append("M=M+1");

            return buffer.toString();
        }
        
    },
    
    
    OR {
        
        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();

            buffer.append("@SP\n")
                  .append("AM=M-1\n")
                  .append("D=M\n")
                  .append("@SP\n")
                  .append("AM=M-1\n")
                  .append("MD=D|M\n")
                  .append("@SP\n")
                  .append("M=M+1");

            return buffer.toString();
        }
        
    },
    
    
    NOT {
        
        @Override
        public String toAssemblyCode(String command) {
            StringBuilder buffer = new StringBuilder();

            buffer.append("@SP\n")
                  .append("AM=M-1\n")
                  .append("M=!M\n")
                  .append("@SP\n")
                  .append("M=M+1");

            return buffer.toString();
        }
        
    };
    
    
    private static final AtomicLong counter = new AtomicLong(0);
    
    
    /**
     * Returns the assembly code for the given command
     * 
     * @param command           the command for which to return the assembly code
     * @return the assembly code for the given command
     */
    public abstract String toAssemblyCode(String command);
    

    /**
     * Returns the enum constant whose command matches the given command. It removes all comments from the command
     * prior to processing.
     * 
     * @param command          the command for which to find the enum constant
     * @return the enum constant whose command matches the given command
     */
    public static ArithmeticCommandType fromCommand(String command) {
        
        if(command == null) {
            return null;
        }
        
        String syntax = VMUtils.stripComments(command);
        String[] parts = syntax.split(" ");

        for(ArithmeticCommandType type : values()) {
            if(type.name().toLowerCase().equals(parts[0])) {
                return type;
            }
        }

        return null;
    }

}
