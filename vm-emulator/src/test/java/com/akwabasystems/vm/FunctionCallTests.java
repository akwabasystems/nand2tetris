
package com.akwabasystems.vm;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;



public class FunctionCallTests {
    
    @Test
    public void functionDeclaration() {
        Parser parser = new VMParser();
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("function Increment 1");
        assertEquals(parser.currentFunctionContext(), "Increment");
        
        StringBuilder expectedCode = new StringBuilder();
        expectedCode.append("(Increment)\n")
               .append("@0\n")
               .append("D=A\n")
               .append("@SP\n")
               .append("A=M\n")
               .append("M=D\n")
               .append("@SP\n")
               .append("M=M+1");
        assertTrue(parser.assemblyCode().contains(expectedCode.toString()));

        parser.parse("return\n");
        assertEquals(parser.currentFunctionContext(), "Main");
        
    }

    
    @Test
    public void functionCall() {
        Parser parser = new VMParser();
        
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("call Sys.init 0");
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("function Sys.init 0");
        assertEquals(parser.currentFunctionContext(), "Sys.init");
        
        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);
        
        parser.parse("return\n");
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("call Fibonacci-1 1\n");
        parser.parse("call Fibonacci-2 1\n");
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("return\n");
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("return\n");
        assertEquals(parser.currentFunctionContext(), "Main");

    }
    
}
