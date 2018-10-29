
package com.akwabasystems.asm;


import junit.framework.TestCase;


public class ParserTests extends TestCase {
    
    
    public void testBasicParser() {
        Parser parser = new AssemblyParser();
        
        parser.parse("// Adds 1 + ... + 100");
        parser.parse("");
        parser.parse("// The parser should remove this comment");
        parser.parse("  ");
        parser.parse("    M=1");
        parser.parse("    M=0");
        parser.parse("(LOOP)");
        parser.parse("    D=M");
        parser.parse("    @100");
        parser.parse("    D=D-A");
        parser.parse("    D;JGT");
        parser.parse("    D=M");
        parser.parse("    M=D+M");
        parser.parse("    M=M+1");
        parser.parse("    0;JMP");
        parser.parse("(END)");
        parser.parse("    0;JMP");
        
        assertNotNull(parser);
        assertFalse(parser.getInstructions().isEmpty());
        
        String binaryCode = parser.binaryCode();
        assertNotNull(binaryCode);
    }
    
    
    public void testAddition() {
        Parser parser = new AssemblyParser();
        
        parser.parse("@2");
        parser.parse("D=A");
        parser.parse("@3");
        parser.parse("D=D+A");
        parser.parse("@0");
        parser.parse("M=D");
        
        String binaryCode = parser.binaryCode();
        assertNotNull(binaryCode);

        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("0000000000000010\n")
                    .append("1110110000010000\n")
                    .append("0000000000000011\n")
                    .append("1110000010010000\n")
                    .append("0000000000000000\n")
                    .append("1110001100001000");
        assertEquals(binaryCode, expectedCode.toString());

    }

    
    public void testMax() {
        Parser parser = new AssemblyParser();
        
        parser.parse("@0");
        parser.parse("D=M");
        parser.parse("@1");
        parser.parse("D=D-M");
        parser.parse("@10");
        parser.parse("D;JGT");
        parser.parse("@1");
        parser.parse("D=M");
        parser.parse("@12");
        parser.parse("0;JMP");
        parser.parse("@0");
        parser.parse("D=M");
        parser.parse("@2");
        parser.parse("M=D");
        parser.parse("@14");
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
        parser.parse("@23");
        parser.parse("D;JLE");
        parser.parse("@16");
        parser.parse("M=D");
        parser.parse("@16384");
        parser.parse("D=A");
        parser.parse("@17");
        parser.parse("M=D");
        parser.parse("@17");
        parser.parse("A=M");
        parser.parse("M=-1");
        parser.parse("@17");
        parser.parse("D=M");
        parser.parse("@32");
        parser.parse("D=D+A");
        parser.parse("@17");
        parser.parse("M=D");
        parser.parse("@16");
        parser.parse("MD=M-1");
        parser.parse("@10");
        parser.parse("D;JGT");
        parser.parse("@23");
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
