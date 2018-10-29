
package com.akwabasystems.vm;


import junit.framework.TestCase;


/**
 *
 * @author Rendezvous7
 */
public class FunctionCallTests extends TestCase {
    
    public void testFunctionDeclaration() {
        Parser parser = new VMParser();
        
        parser.parse("function Increment 1");
        assertEquals(parser.currentFunctionContext(), "Main");
        
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

    }

    
    public void testFunctionCall() {
        Parser parser = new VMParser();
        
        parser.parse("call Sys.init 0");
        parser.parse("function Sys.init 0");
        assertEquals(parser.currentFunctionContext(), "Sys.init");
        
        String assemblyCode = parser.assemblyCode();
        assertNotNull(assemblyCode);
        
        parser.parse("return\n");
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("call Fibonacci 1\n");
        parser.parse("call Fibonacci 1\n");
        assertEquals(parser.currentFunctionContext(), "Fibonacci");
        
        parser.parse("return\n");
        assertEquals(parser.currentFunctionContext(), "Fibonacci");
        
        parser.parse("return\n");
        assertEquals(parser.currentFunctionContext(), "Main");

    }
    
}
