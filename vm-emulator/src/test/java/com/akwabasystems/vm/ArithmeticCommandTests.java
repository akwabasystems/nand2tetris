
package com.akwabasystems.vm;


import com.akwabasystems.model.CommandType;
import com.akwabasystems.model.VMCommand;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;


public class ArithmeticCommandTests {
    
    
    @Test
    public void pushConstant() {
        VMCommand command = CommandType.fromCommand("push constant 7");
        assertNotNull(command);
        
        String assemblyCode = command.toAssemblyCode();
        assertNotNull(assemblyCode);
        
        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("@7\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1");
        assertEquals(assemblyCode, expectedCode.toString());

        StringBuilder buffer = new StringBuilder();
        buffer.append(assemblyCode).append("\n");

        VMCommand command2 = CommandType.fromCommand("push constant 8");
        assemblyCode = command2.toAssemblyCode();
        buffer.append(assemblyCode);
        
        expectedCode = new StringBuilder();
        expectedCode.append("@8\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1");
        assertEquals(assemblyCode, expectedCode.toString());
    }

    
    @Test
    public void addCommand() {
        VMCommand command = CommandType.fromCommand("add");
        assertNotNull(command);
        
        String assemblyCode = command.toAssemblyCode();
        assertNotNull(assemblyCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("@SP\n")
                    .append("AM=M-1\n")
                    .append("D=M\n")
                    .append("@SP\n")
                    .append("AM=M-1\n")
                    .append("MD=D+M\n")
                    .append("@SP\n")
                    .append("M=M+1");

        assertEquals(assemblyCode, expectedCode.toString());
    }

    
    @Test
    public void subCommand() {
        VMCommand command = CommandType.fromCommand("sub");
        assertNotNull(command);
        
        String assemblyCode = command.toAssemblyCode();
        assertNotNull(assemblyCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("@SP\n")
                    .append("AM=M-1\n")
                    .append("D=M\n")
                    .append("@SP\n")
                    .append("AM=M-1\n")
                    .append("MD=M-D\n")
                    .append("@SP\n")
                    .append("M=M+1");

        assertEquals(assemblyCode, expectedCode.toString());
    }

    
    @Test
    public void andCommand() {
        VMCommand command = CommandType.fromCommand("and");
        assertNotNull(command);
        
        String assemblyCode = command.toAssemblyCode();
        assertNotNull(assemblyCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("@SP\n")
                    .append("AM=M-1\n")
                    .append("D=M\n")
                    .append("@SP\n")
                    .append("AM=M-1\n")
                    .append("MD=D&M\n")
                    .append("@SP\n")
                    .append("M=M+1");
        
        assertEquals(assemblyCode, expectedCode.toString());
    }
    
    
    @Test
    public void lessThanCommand() {
        VMCommand command = CommandType.fromCommand("lt");
        assertNotNull(command);
        
        String assemblyCode = command.toAssemblyCode();
        assertNotNull(assemblyCode);
    }

    
    @Test
    public void greaterThanCommand() {
        VMCommand command = CommandType.fromCommand("gt");
        assertNotNull(command);
        
        String assemblyCode = command.toAssemblyCode();
        assertNotNull(assemblyCode);
    }
    
    
    @Test
    public void equalCommand() {
        VMCommand command = CommandType.fromCommand("eq");
        assertNotNull(command);
    }
    
}
