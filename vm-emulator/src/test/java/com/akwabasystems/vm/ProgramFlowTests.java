
package com.akwabasystems.vm;


import static org.junit.Assert.assertEquals;
import org.junit.Test;



public class ProgramFlowTests {
    
    @Test
    public void parsingContext() {
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


    @Test
    public void label() {
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
    
    
    @Test
    public void gotoLabel() {
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
