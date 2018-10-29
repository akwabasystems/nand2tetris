
package com.akwabasystems.asm;


import junit.framework.TestCase;


public class SymbolTests extends TestCase {
    
    
    public void testPredefinedSymbols() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.initialize();
        
        int address = symbolTable.getAddress("SP");
        assertEquals(address, 0);
        
        address = symbolTable.getAddress("LCL");
        assertEquals(address, 1);
        
        address = symbolTable.getAddress("ARG");
        assertEquals(address, 2);
        
        address = symbolTable.getAddress("THIS");
        assertEquals(address, 3);
        
        address = symbolTable.getAddress("THAT");
        assertEquals(address, 4);
        
        for(int i = 0; i < 16; i++) {
            address = symbolTable.getAddress(String.format("R%s", i));
            assertEquals(address, i);
        }
        
        address = symbolTable.getAddress("SCREEN");
        assertEquals(address, 16384);
        
        address = symbolTable.getAddress("KBD");
        assertEquals(address, 24576);
    }
    

    public void testSymbolTable() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.initialize();
        
        symbolTable.addEntry("i", 1024);
        symbolTable.addEntry("sum", 1025);
        
        assertTrue(symbolTable.contains("i"));
        assertTrue(symbolTable.contains("sum"));
        
        int address = symbolTable.getAddress("i");
        assertEquals(address, 1024);
        
        address = symbolTable.getAddress("sum");
        assertEquals(address, 1025);
        
        address = symbolTable.getAddress("x");
        assertEquals(address, -1);

    }

}
