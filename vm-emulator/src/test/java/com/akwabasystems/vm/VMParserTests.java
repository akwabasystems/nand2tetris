
package com.akwabasystems.vm;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class VMParserTests {
    
    
    @Test
    public void addCommandParsing() {
        Parser parser = new VMParser();
            
        parser.parse("// This file contains comments");
        parser.parse("");
        parser.parse("// The parser should remove them before processing begins");
        parser.parse(" ");
        parser.parse("push constant 7");
        parser.parse("push constant 8");
        parser.parse("add");

        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("@7\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1\n")
                    .append("@8\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1\n")
                    .append("@SP\n")
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
    public void subCommandParsing() {
        Parser parser = new VMParser();

        parser.parse("push constant 30");
        parser.parse("push constant 10");
        parser.parse("sub");

        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("@30\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1\n")
                    .append("@10\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1\n")
                    .append("@SP\n")
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
    public void andCommandParsing() {
        Parser parser = new VMParser();

        parser.parse("push constant 1");
        parser.parse("push constant 0");
        parser.parse("and");

        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("@1\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1\n")
                    .append("@0\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1\n")
                    .append("@SP\n")
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
    public void ifEqualCommandParsing() {
        Parser parser = new VMParser();

        parser.parse("push constant 17");
        parser.parse("push constant 17");
        parser.parse("eq");
        parser.parse("push constant 17");
        parser.parse("push constant 16");
        parser.parse("eq");
        parser.parse("push constant 16");
        parser.parse("push constant 17");
        parser.parse("eq");
        
        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);

        String[] parts = assemblyCode.split("\n");
        assertTrue(parts[20].contains("@IF_EQUAL_TO"));
        assertTrue(parts[21].contains("D;JEQ"));
    }
    
    
    @Test
    public void ifGreaterThanCommandParsing() {
        Parser parser = new VMParser();

        parser.parse("push constant 32767");
        parser.parse("push constant 32766");
        parser.parse("gt");

        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);

        String[] parts = assemblyCode.split("\n");
        assertTrue(parts[20].contains("@IF_GREATER_THAN"));
        assertTrue(parts[21].contains("D;JGT"));
    }
    
    
    @Test
    public void ifLessThanCommandParsing() {
        Parser parser = new VMParser();

        parser.parse("push constant 32766");
        parser.parse("push constant 32767");
        parser.parse("lt");

        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);

        String[] parts = assemblyCode.split("\n");
        assertTrue(parts[20].contains("@IF_LESS_THAN"));
        assertTrue(parts[21].contains("D;JLT"));
    }
    
    
    @Test
    public void popCommandParsing() {
        Parser parser = new VMParser();

        parser.parse("push constant 21");
        parser.parse("pop local 0");

        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("@21\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1\n")
                    .append("@LCL\n")
                    .append("D=M\n")
                    .append("@0\n")
                    .append("D=D+A\n")
                    .append("@R15\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("AM=M-1\n")
                    .append("D=M\n")
                    .append("@R15\n")
                    .append("A=M\n")
                    .append("M=D");

        assertEquals(assemblyCode, expectedCode.toString());
    }

    
    @Test
    public void bootstrapCode() {
        String bootstrapCode = VMParser.bootstrapCode();
        assertNotNull(bootstrapCode);
        
        StringBuilder codeFragment = new StringBuilder();
        codeFragment.append("@261\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("M=D\n");
        assertTrue(bootstrapCode.contains(codeFragment.toString()));
        
        codeFragment = new StringBuilder();
        codeFragment.append("@Sys.init\n")
                    .append("0;JMP");
        assertTrue(bootstrapCode.contains(codeFragment.toString()));
    }

}
