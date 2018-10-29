
package com.akwabasystems.vm;


import junit.framework.TestCase;


/**
 *
 * @author Rendezvous7
 */
public class ProgramFlowTests extends TestCase {
    
    public void testParsingContext() {
        Parser parser = new VMParser();
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("function Fibonacci 0");
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("return");
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("function Sys.init 0");
        parser.parse("push constant 4");
        parser.parse("call Main.Application 0");
        assertEquals(parser.currentFunctionContext(), "Main.Application");
        
        parser.parse("return");
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("return");
        assertEquals(parser.currentFunctionContext(), "Main");
    }


    public void testLabel() {
        Parser parser = new VMParser();
        assertEquals(parser.currentFunctionContext(), "Main");
        
        parser.parse("label MainLoop");
        String assemblyCode = parser.assemblyCode();
        assertEquals(assemblyCode, "(Main$MainLoop)");
        
        parser.parse("function Sys.init 0");
        parser.parse("label InfiniteLoop");
        
        String[] parts = parser.assemblyCode().split("\n");
        assertEquals(parts[parts.length - 1], "(Main$InfiniteLoop)");
        
        parser.parse("return");
        parser.parse("label Reset");
        
        parts = parser.assemblyCode().split("\n");
        assertEquals(parts[parts.length - 1], "(Main$Reset)");
    }
    
    
    public void testGotoLabel() {
        Parser parser = new VMParser();
        parser.parse("if-goto InfiniteLoop");
        String assemblyCode = parser.assemblyCode();
        
        StringBuilder builder = new StringBuilder();
        builder.append("@SP\n")
               .append("AM=M-1\n")
               .append("D=M\n")
               .append("@Main$InfiniteLoop\n")
               .append("D;JNE");
        assertEquals(assemblyCode, builder.toString());
    }
    
}
