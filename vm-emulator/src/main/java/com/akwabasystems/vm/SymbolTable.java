
package com.akwabasystems.vm;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * A class that maintains a mapping between symbols defined in an assembly code and their corresponding memory address.
 * 
 * When an instance of this class is created, it automatically adds entries for pre-defined symbols ("SCREEN", "KBD", 
 * "RO" through "R15", etc). 
 */
public final class SymbolTable {
    
    private final ConcurrentMap<String,Integer> symbols = new ConcurrentHashMap<String,Integer>();
    
    
    /**
     * Default constructor. Populates the symbol table with pre-defined symbols
     */
    public SymbolTable() {
        super();
        initialize();
    }
    
    
    /**
     * Adds entries for pre-defined symbols ("SCREEN", "KBD", "R0" though "R15", etc)
     */
    public void initialize() {
        addEntry("SP", 0);
        addEntry("LCL", 1);
        addEntry("ARG", 2);
        addEntry("THIS", 3);
        addEntry("THAT", 4);
        
        for(int i = 0; i < 16; i++) {
            addEntry(String.format("R%s", i), i);
        }
        
        addEntry("SCREEN", 16384);
        addEntry("KBD", 24576);
    }


    /**
     * Adds an entry for the specified symbol and the given address
     * 
     * @param symbol        the symbol for which to add the entry
     * @param address       the memory address to associate to the symbol
     */
    public void addEntry(String symbol, int address) {
        symbols.put(symbol, address);
    }
    

    /**
     * Returns the memory address for the given symbol, or -1 if no entry if found for the symbol
     * 
     * @param symbol        the symbol for which to return the address
     * @return the memory address for the given symbol, or -1 if no entry if found for the symbol
     */
    public int getAddress(String symbol) {
        return symbols.containsKey(symbol)? symbols.get(symbol) : -1;
    }


    /**
     * Returns true if the symbol table contains the specified symbol; otherwise, returns false
     * 
     * @param symbol        the symbol to check
     * @return true if the symbol table contains the specified symbol; otherwise, returns false
     */
    public boolean contains(String symbol) {
        return symbols.containsKey(symbol);
    }
    
    
    /**
     * Returns a string representation of this class
     * 
     * @return a string representation of this class
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        Set<Map.Entry<String,Integer>> entrySet = symbols.entrySet();
        int index = 0;
        
        for (Map.Entry<String,Integer> entry : entrySet) {
            String info = String.format("{ symbol: %s, address: %s }", entry.getKey(), entry.getValue());
            buffer.append(info);
            
            if(index++ < entrySet.size()) {
                buffer.append("\n");
            }
        }
        
        return buffer.toString();
    }

}
