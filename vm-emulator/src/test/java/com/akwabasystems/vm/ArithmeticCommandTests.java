
package com.akwabasystems.vm;


import com.akwabasystems.model.CommandType;
import com.akwabasystems.model.VMCommand;
import junit.framework.TestCase;


public class ArithmeticCommandTests extends TestCase {
    
    
    public void testPushConstant() {
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

    
    public void testAddCommand() {
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


    public void testSubCommand() {
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

    
    public void testAndCommand() {
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

}
