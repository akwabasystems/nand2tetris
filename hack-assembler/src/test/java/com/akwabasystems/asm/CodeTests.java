
package com.akwabasystems.asm;


import junit.framework.TestCase;


public class CodeTests extends TestCase {
    
    
    public void testDestinationFields() {
        Code code = new Code("M", "A", "");
        String dest = code.dest();
        assertNotNull(dest);
        assertEquals(dest, "001");
        
        code = new Code("D", "A", "");
        assertEquals(code.dest(), "010");
        
        code = new Code("MD", "A", "");
        assertEquals(code.dest(), "011");
        
        code = new Code("A", "A", "");
        assertEquals(code.dest(), "100");
        
        code = new Code("AM", "A", "");
        assertEquals(code.dest(), "101");
        
        code = new Code("AD", "A", "");
        assertEquals(code.dest(), "110");
        
        code = new Code("AMD", "A", "");
        assertEquals(code.dest(), "111");
    }
    
    
    public void testJumpFields() {
        Code code = new Code("D", "0", "");
        String jump = code.jump();
        assertNotNull(jump);
        assertEquals(jump, "000");
        
        code = new Code("D", "0", "JGT");
        assertEquals(code.jump(), "001");
        
        code = new Code("D", "0", "JEQ");
        assertEquals(code.jump(), "010");
        
        code = new Code("D", "0", "JGE");
        assertEquals(code.jump(), "011");
        
        code = new Code("D", "0", "JLT");
        assertEquals(code.jump(), "100");
        
        code = new Code("D", "0", "JNE");
        assertEquals(code.jump(), "101");
        
        code = new Code("D", "0", "JLE");
        assertEquals(code.jump(), "110");
        
        code = new Code("D", "0", "JMP");
        assertEquals(code.jump(), "111");
    }
    
    
    public void testComputeAFields() {
        Code code = new Code("D", "0", "");
        String comp = code.comp();
        assertNotNull(comp);
        assertEquals(comp, "101010");
        
        code = new Code("D", "1", "");
        assertEquals(code.comp(), "111111");
        
        code = new Code("D", "-1", "");
        assertEquals(code.comp(), "111010");
        
        code = new Code("D", "D", "");
        assertEquals(code.comp(), "001100");
        
        code = new Code("D", "A", "");
        assertEquals(code.comp(), "110000");
        
        code = new Code("D", "!D", "");
        assertEquals(code.comp(), "001101");
        
        code = new Code("D", "!A", "");
        assertEquals(code.comp(), "110001");
        
        code = new Code("D", "-D", "");
        assertEquals(code.comp(), "001111");
        
        code = new Code("D", "-A", "");
        assertEquals(code.comp(), "110011");
        
        code = new Code("D", "D+1", "");
        assertEquals(code.comp(), "011111");
        
        code = new Code("D", "A+1", "");
        assertEquals(code.comp(), "110111");
        
        code = new Code("D", "D-1", "");
        assertEquals(code.comp(), "001110");
        
        code = new Code("D", "A-1", "");
        assertEquals(code.comp(), "110010");
        
        code = new Code("D", "D+A", "");
        assertEquals(code.comp(), "000010");
        
        code = new Code("D", "D-A", "");
        assertEquals(code.comp(), "010011");
        
        code = new Code("D", "A-D", "");
        assertEquals(code.comp(), "000111");
        
        code = new Code("D", "D&A", "");
        assertEquals(code.comp(), "000000");
        
        code = new Code("D", "D|A", "");
        assertEquals(code.comp(), "010101");
    }
    
    
    public void testComputeMFields() {
        Code code = new Code("D", "M", "");
        String comp = code.comp();
        assertNotNull(comp);
        assertEquals(comp, "110000");
        
        code = new Code("D", "!M", "");
        assertEquals(code.comp(), "110001");
        
        code = new Code("D", "-M", "");
        assertEquals(code.comp(), "110011");
        
        code = new Code("D", "M+1", "");
        assertEquals(code.comp(), "110111");
        
        code = new Code("D", "M-1", "");
        assertEquals(code.comp(), "110010");
        
        code = new Code("D", "D+M", "");
        assertEquals(code.comp(), "000010");
        
        code = new Code("D", "D-M", "");
        assertEquals(code.comp(), "010011");
        
        code = new Code("D", "M-D", "");
        assertEquals(code.comp(), "000111");
        
        code = new Code("D", "D&M", "");
        assertEquals(code.comp(), "000000");
        
        code = new Code("D", "D|M", "");
        assertEquals(code.comp(), "010101");
    }
    
}
