
package com.akwabasystems.asm;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;


public class InstructionTests {
    
    @Test
    public void testAInstruction() {
        Instruction instruction = new AInstruction("@7");
        String binaryCode = instruction.binaryCode();
        assertNotNull(binaryCode);
        assertEquals(binaryCode, "0000000000000111");
        
        instruction = new AInstruction("@100");
        assertEquals(instruction.binaryCode(), "0000000001100100");
    }
    
    
     @Test
    public void testCInstruction() {
        Instruction instruction = new CInstruction("D=A");
        String binaryCode = instruction.binaryCode();
        assertNotNull(binaryCode);
        assertEquals(binaryCode, "1110110000010000");
        
        instruction = new CInstruction("D=D+A");
        assertEquals(instruction.binaryCode(), "1110000010010000");
        
        instruction = new CInstruction("M=D");
        assertEquals(instruction.binaryCode(), "1110001100001000");
        
        instruction = new CInstruction("M=1");
        assertEquals(instruction.binaryCode(), "1110111111001000");
        
        instruction = new CInstruction("M=0");
        assertEquals(instruction.binaryCode(), "1110101010001000");
        
        instruction = new CInstruction("D=M");
        assertEquals(instruction.binaryCode(), "1111110000010000");
        
        instruction = new CInstruction("D=D-A");
        assertEquals(instruction.binaryCode(), "1110010011010000");
        
        instruction = new CInstruction("D;JGT");
        assertEquals(instruction.binaryCode(), "1110001100000001");
        
        instruction = new CInstruction("M=D+M");
        assertEquals(instruction.binaryCode(), "1111000010001000");
        
        instruction = new CInstruction("M=M+1");
        assertEquals(instruction.binaryCode(), "1111110111001000");
        
        instruction = new CInstruction("0;JMP");
        assertEquals(instruction.binaryCode(), "1110101010000111");
    }
    
    
    @Test
    public void testCInstructionWithComments() {
        Instruction instruction = new CInstruction("D=A");
        String binaryCode = instruction.binaryCode();
        assertNotNull(binaryCode);
        assertEquals(binaryCode, "1110110000010000");
        
        instruction = new CInstruction("D=D+A");
        assertEquals(instruction.binaryCode(), "1110000010010000");
        
        instruction = new CInstruction("M=D");
        assertEquals(instruction.binaryCode(), "1110001100001000");
        
        instruction = new CInstruction("M=1");
        assertEquals(instruction.binaryCode(), "1110111111001000");
        
        instruction = new CInstruction("M=0");
        assertEquals(instruction.binaryCode(), "1110101010001000");
        
        instruction = new CInstruction("D=M");
        assertEquals(instruction.binaryCode(), "1111110000010000");
        
        instruction = new CInstruction("D=D-A");
        assertEquals(instruction.binaryCode(), "1110010011010000");
        
        instruction = new CInstruction("D;JGT");
        assertEquals(instruction.binaryCode(), "1110001100000001");
        
        instruction = new CInstruction("M=D+M");
        assertEquals(instruction.binaryCode(), "1111000010001000");
        
        instruction = new CInstruction("M=M+1");
        assertEquals(instruction.binaryCode(), "1111110111001000");
        
        instruction = new CInstruction("0;JMP");
        assertEquals(instruction.binaryCode(), "1110101010000111");
    }
    
    
}
