
package com.akwabasystems.asm;


import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;


public class CodeTests {
    
    
    @Test
    public void destinationFields() {
        Code code = new Code("M", "A", "");
        String dest = code.dest();
        assertNotNull(dest);
        assertEquals(dest, "001");
        
        code = new Code("D", "A", "");
        Assert.assertEquals(code.dest(), "010");
        
        code = new Code("MD", "A", "");
        Assert.assertEquals(code.dest(), "011");
        
        code = new Code("A", "A", "");
        Assert.assertEquals(code.dest(), "100");
        
        code = new Code("AM", "A", "");
        Assert.assertEquals(code.dest(), "101");
        
        code = new Code("AD", "A", "");
        Assert.assertEquals(code.dest(), "110");
        
        code = new Code("AMD", "A", "");
        Assert.assertEquals(code.dest(), "111");
    }
    
    
    @Test
    public void jumpFields() {
        Code code = new Code("D", "0", "");
        String jump = code.jump();
        Assert.assertNotNull(jump);
        Assert.assertEquals(jump, "000");
        
        code = new Code("D", "0", "JGT");
        Assert.assertEquals(code.jump(), "001");
        
        code = new Code("D", "0", "JEQ");
        Assert.assertEquals(code.jump(), "010");
        
        code = new Code("D", "0", "JGE");
        Assert.assertEquals(code.jump(), "011");
        
        code = new Code("D", "0", "JLT");
        Assert.assertEquals(code.jump(), "100");
        
        code = new Code("D", "0", "JNE");
        Assert.assertEquals(code.jump(), "101");
        
        code = new Code("D", "0", "JLE");
        Assert.assertEquals(code.jump(), "110");
        
        code = new Code("D", "0", "JMP");
        Assert.assertEquals(code.jump(), "111");
    }
    
    
    @Test
    public void computeAFields() {
        Code code = new Code("D", "0", "");
        String comp = code.comp();
        Assert.assertNotNull(comp);
        Assert.assertEquals(comp, "101010");
        
        code = new Code("D", "1", "");
        Assert.assertEquals(code.comp(), "111111");
        
        code = new Code("D", "-1", "");
        Assert.assertEquals(code.comp(), "111010");
        
        code = new Code("D", "D", "");
        Assert.assertEquals(code.comp(), "001100");
        
        code = new Code("D", "A", "");
        Assert.assertEquals(code.comp(), "110000");
        
        code = new Code("D", "!D", "");
        Assert.assertEquals(code.comp(), "001101");
        
        code = new Code("D", "!A", "");
        Assert.assertEquals(code.comp(), "110001");
        
        code = new Code("D", "-D", "");
        Assert.assertEquals(code.comp(), "001111");
        
        code = new Code("D", "-A", "");
        Assert.assertEquals(code.comp(), "110011");
        
        code = new Code("D", "D+1", "");
        Assert.assertEquals(code.comp(), "011111");
        
        code = new Code("D", "A+1", "");
        Assert.assertEquals(code.comp(), "110111");
        
        code = new Code("D", "D-1", "");
        Assert.assertEquals(code.comp(), "001110");
        
        code = new Code("D", "A-1", "");
        Assert.assertEquals(code.comp(), "110010");
        
        code = new Code("D", "D+A", "");
        Assert.assertEquals(code.comp(), "000010");
        
        code = new Code("D", "D-A", "");
        Assert.assertEquals(code.comp(), "010011");
        
        code = new Code("D", "A-D", "");
        Assert.assertEquals(code.comp(), "000111");
        
        code = new Code("D", "D&A", "");
        Assert.assertEquals(code.comp(), "000000");
        
        code = new Code("D", "D|A", "");
        Assert.assertEquals(code.comp(), "010101");
    }
    
    
    @Test
    public void computeMFields() {
        Code code = new Code("D", "M", "");
        String comp = code.comp();
        Assert.assertNotNull(comp);
        Assert.assertEquals(comp, "110000");
        
        code = new Code("D", "!M", "");
        Assert.assertEquals(code.comp(), "110001");
        
        code = new Code("D", "-M", "");
        Assert.assertEquals(code.comp(), "110011");
        
        code = new Code("D", "M+1", "");
        Assert.assertEquals(code.comp(), "110111");
        
        code = new Code("D", "M-1", "");
        Assert.assertEquals(code.comp(), "110010");
        
        code = new Code("D", "D+M", "");
        Assert.assertEquals(code.comp(), "000010");
        
        code = new Code("D", "D-M", "");
        Assert.assertEquals(code.comp(), "010011");
        
        code = new Code("D", "M-D", "");
        Assert.assertEquals(code.comp(), "000111");
        
        code = new Code("D", "D&M", "");
        Assert.assertEquals(code.comp(), "000000");
        
        code = new Code("D", "D|M", "");
        Assert.assertEquals(code.comp(), "010101");
    }
    
}
