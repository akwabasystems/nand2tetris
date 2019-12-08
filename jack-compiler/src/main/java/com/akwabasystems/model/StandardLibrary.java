
package com.akwabasystems.model;

import java.util.HashMap;
import java.util.Map;


/*
 * A class that stores the number of arguments for each method in the Jack standard library API. It allows a 
 * lookup for a given method during a subroutine call, making it easier to output the number of arguments
 * expected by the method in case it is a standard API.
 */
public class StandardLibrary {
    private static StandardLibrary instance;
    private final Map<String,Map<String,Integer>> library = new HashMap<>();
    
    
    /**
     * Returns a unique instance of this StandardLibrary class
     * 
     * @return a unique instance of this StandardLibrary class
     */
    public static StandardLibrary getInstance() {
        if (instance == null) {
            instance = new StandardLibrary();
        }
        
        return instance;
    }
    
    
    /**
     * Initializes this standard library by storing the expected argument count for each method in its modules
     */
    public void initialize() {
        Map<String, Integer> mathAPI = new HashMap<>();
        mathAPI.put("init", 0);
        mathAPI.put("abs", 1);
        mathAPI.put("multiply", 2);
        mathAPI.put("divide", 2);
        mathAPI.put("min", 2);
        mathAPI.put("max", 2);
        mathAPI.put("sqrt", 1);
        library.put("Math", mathAPI);
        
        Map<String,Integer> stringAPI = new HashMap<>();
        stringAPI.put("new", 1);
        stringAPI.put("dispose", 0);
        stringAPI.put("length", 0);
        stringAPI.put("charAt", 1);
        stringAPI.put("setCharAt", 1);
        stringAPI.put("appendChar", 1);
        stringAPI.put("eraseLastChar", 0);
        stringAPI.put("intValue", 0);
        stringAPI.put("setInt", 1);
        stringAPI.put("backSpace", 0);
        stringAPI.put("doubleQuote", 0);
        stringAPI.put("newLine", 0);
        library.put("String", stringAPI);
        
        Map<String,Integer> arrayAPI = new HashMap<>();
        arrayAPI.put("new", 1);
        arrayAPI.put("dispose", 0);
        library.put("Array", arrayAPI);
        
        Map<String,Integer> outputAPI = new HashMap<>();
        outputAPI.put("init", 0);
        outputAPI.put("initMap", 0);
        outputAPI.put("create", 12);
        outputAPI.put("createShiftedMap", 0);
        outputAPI.put("getMap", 1);
        outputAPI.put("drawChar", 1);
        outputAPI.put("moveCursor", 2);
        outputAPI.put("printChar", 1);
        outputAPI.put("printString", 1);
        outputAPI.put("printInt", 1);
        outputAPI.put("println", 0);
        outputAPI.put("backSpace", 0);
        library.put("Output", outputAPI);
        
        Map<String,Integer> screenAPI = new HashMap<>();
        screenAPI.put("init", 0);
        screenAPI.put("clearScreen", 0);
        screenAPI.put("setColor", 1);
        screenAPI.put("drawPixel", 2);
        screenAPI.put("drawLine", 4);
        screenAPI.put("drawRectangle", 4);
        screenAPI.put("drawCircle", 3);
        screenAPI.put("updateLocation", 2);
        screenAPI.put("drawConditional", 3);
        screenAPI.put("drawHorizontal", 3);
        screenAPI.put("drawSymetric", 4);
        library.put("Screen", screenAPI);
        
        Map<String,Integer> keyboardAPI = new HashMap<>();
        keyboardAPI.put("init", 0);
        keyboardAPI.put("keyPressed", 0);
        keyboardAPI.put("readChar", 0);
        keyboardAPI.put("readLine", 1);
        keyboardAPI.put("readInt", 1);
        library.put("Keyboard", keyboardAPI);
        
        Map<String,Integer> memoryAPI = new HashMap<>();
        memoryAPI.put("init", 0);
        memoryAPI.put("peek", 1);
        memoryAPI.put("poke", 2);
        memoryAPI.put("alloc", 1);
        memoryAPI.put("deAlloc", 1);
        library.put("Memory", memoryAPI);
        
        Map<String,Integer> sysAPI = new HashMap<>();
        sysAPI.put("init", 0);
        sysAPI.put("halt", 0);
        sysAPI.put("error", 1);
        sysAPI.put("wait", 1);
        library.put("Sys", sysAPI);
        
    }
    
    
    /**
     * Returns true if the given name is that of a standard library object; otherwise, returns false
     * 
     * @param name      the name to check
     * @return true if the given name is that of a standard library object; otherwise, returns false
     */
    public boolean isStandardLibraryObject(String name) {
        return library.containsKey(name);
    }
    
    
    /**
     * Returns the expected number of arguments for the given method of the specified standard library object,
     * or null if no such method is found.
     * 
     * @param object        the object on which the method is being invoked
     * @param method        the method for which to return the argument count
     * @return the expected number of arguments for the given method of the specified standard library object, or null
     *  if no such method is found.
     */
    public Integer getExpectedArguments(String object, String method) {
        if (!isStandardLibraryObject(object)) {
            return null;
        }
        
        Map<String,Integer> api = library.get(object);
        return api.containsKey(method)? api.get(method) : null;
    }

}
