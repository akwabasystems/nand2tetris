
package com.akwabasystems.asm;


import junit.framework.TestCase;


public class SymbolParserTests extends TestCase {
    
    public void testInstructionsWithComments() {
        Parser parser = new AssemblyParser();

        parser.parse("// This file contains comments");
        parser.parse("");
        parser.parse("// The parser should remove them before processing begins");
        parser.parse("// File name: projects/06/max/Max.asm");
        parser.parse("// Computes R2 = max(R0, R1)  (R0,R1,R2 refer to RAM[0],RAM[1],RAM[2])");
        parser.parse(" ");
        parser.parse("    @R0");
        parser.parse("    D=M");
        parser.parse("    @R1");
        parser.parse("    D=D-M");
        parser.parse("    @OUTPUT_FIRST");
        parser.parse("    D;JGT");
        parser.parse("    @R1");
        parser.parse("    D=M");
        parser.parse("    @OUTPUT_D");
        parser.parse("    0;JMP");
        parser.parse("(OUTPUT_FIRST)");
        parser.parse("    @R0");
        parser.parse("    D=M");
        parser.parse("(OUTPUT_D)");
        parser.parse("    @R2");
        parser.parse("    M=D");
        parser.parse("(INFINITE_LOOP)");
        parser.parse("    @INFINITE_LOOP");
        parser.parse("    0;JMP");
        
        String binaryCode = parser.binaryCode();
        assertNotNull(binaryCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("0000000000000000\n")
                    .append("1111110000010000\n")
                    .append("0000000000000001\n")
                    .append("1111010011010000\n")
                    .append("0000000000001010\n")
                    .append("1110001100000001\n")
                    .append("0000000000000001\n")
                    .append("1111110000010000\n")
                    .append("0000000000001100\n")
                    .append("1110101010000111\n")
                    .append("0000000000000000\n")
                    .append("1111110000010000\n")
                    .append("0000000000000010\n")
                    .append("1110001100001000\n")
                    .append("0000000000001110\n")
                    .append("1110101010000111");

        assertEquals(binaryCode, expectedCode.toString());
        
    }
    
    
    public void testMax() {
        Parser parser = new AssemblyParser();
        
        parser.parse("@R0");
        parser.parse("D=M");
        parser.parse("@R1");
        parser.parse("D=D-M            // D = first number - second number");
        parser.parse("@OUTPUT_FIRST");
        parser.parse("D;JGT            // if D>0 (first is greater) goto output_first");
        parser.parse("@R1");
        parser.parse("D=M              // D = second number");
        parser.parse("@OUTPUT_D");
        parser.parse("0;JMP            // goto output_d");
        parser.parse("(OUTPUT_FIRST)");
        parser.parse("@R0");             
        parser.parse("D=M              // D = first number");
        parser.parse("(OUTPUT_D)");
        parser.parse("@R2");
        parser.parse("M=D              // M[2] = D (greatest number)");
        parser.parse("(INFINITE_LOOP)");
        parser.parse("@INFINITE_LOOP");
        parser.parse("0;JMP");
        
        String binaryCode = parser.binaryCode();
        assertNotNull(binaryCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("0000000000000000\n")
                    .append("1111110000010000\n")
                    .append("0000000000000001\n")
                    .append("1111010011010000\n")
                    .append("0000000000001010\n")
                    .append("1110001100000001\n")
                    .append("0000000000000001\n")
                    .append("1111110000010000\n")
                    .append("0000000000001100\n")
                    .append("1110101010000111\n")
                    .append("0000000000000000\n")
                    .append("1111110000010000\n")
                    .append("0000000000000010\n")
                    .append("1110001100001000\n")
                    .append("0000000000001110\n")
                    .append("1110101010000111");

        assertEquals(binaryCode, expectedCode.toString());
        
    }
    
    
    public void testRect() {
        Parser parser = new AssemblyParser();
        
        parser.parse("@0");
        parser.parse("D=M");
        parser.parse("@INFINITE_LOOP");
        parser.parse("D;JLE"); 
        parser.parse("@counter");
        parser.parse("M=D");
        parser.parse("@SCREEN");
        parser.parse("D=A");
        parser.parse("@address");
        parser.parse("M=D");
        parser.parse("(LOOP)");
        parser.parse("@address");
        parser.parse("A=M");
        parser.parse("M=-1");
        parser.parse("@address");
        parser.parse("D=M");
        parser.parse("@32");
        parser.parse("D=D+A");
        parser.parse("@address");
        parser.parse("M=D");
        parser.parse("@counter");
        parser.parse("MD=M-1");
        parser.parse("@LOOP");
        parser.parse("D;JGT");
        parser.parse("(INFINITE_LOOP)");
        parser.parse("@INFINITE_LOOP");
        parser.parse("0;JMP");
        
        String binaryCode = parser.binaryCode();
        assertNotNull(binaryCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("0000000000000000\n")
                    .append("1111110000010000\n")
                    .append("0000000000010111\n")
                    .append("1110001100000110\n")
                    .append("0000000000010000\n")
                    .append("1110001100001000\n")
                    .append("0100000000000000\n")
                    .append("1110110000010000\n")
                    .append("0000000000010001\n")
                    .append("1110001100001000\n")
                    .append("0000000000010001\n")
                    .append("1111110000100000\n")
                    .append("1110111010001000\n")
                    .append("0000000000010001\n")
                    .append("1111110000010000\n")
                    .append("0000000000100000\n")
                    .append("1110000010010000\n")
                    .append("0000000000010001\n")
                    .append("1110001100001000\n")
                    .append("0000000000010000\n")
                    .append("1111110010011000\n")
                    .append("0000000000001010\n")
                    .append("1110001100000001\n")
                    .append("0000000000010111\n")
                    .append("1110101010000111");

        assertEquals(binaryCode, expectedCode.toString());

    }

}
