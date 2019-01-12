
package com.akwabasystems.vm;

import com.akwabasystems.model.CommandType;
import com.akwabasystems.model.VMCommand;
import com.akwabasystems.model.ArithmeticCommand;
import com.akwabasystems.model.CallCommand;
import com.akwabasystems.model.FunctionCommand;
import com.akwabasystems.model.GotoCommand;
import com.akwabasystems.model.IfCommand;
import com.akwabasystems.model.LabelCommand;
import com.akwabasystems.model.PopCommand;
import com.akwabasystems.model.PushCommand;
import com.akwabasystems.model.ReturnCommand;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;


public class VMCommandTests {
    
    
    @Test
    public void arithmeticCommand() {
        VMCommand command = new ArithmeticCommand("add");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_ARITHMETIC);
        assertEquals(command.getArgument1(), "");
        assertEquals(command.getArgument2(), 0);
    }
    
    
    @Test
    public void gotoCommand() {
        VMCommand command = new GotoCommand("goto loop");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_GOTO);
        assertEquals(command.getArgument1(), "loop");
        assertEquals(command.getArgument2(), 0);
    }
    
    
    @Test
    public void pushCommand() {
        VMCommand command = new PushCommand("push argument 1");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_PUSH);
        assertEquals(command.getArgument1(), "argument");
        assertEquals(command.getArgument2(), 1);
    }
    
    
    @Test
    public void popCommand() {
        VMCommand command = new PopCommand("pop local 3");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_POP);
        assertEquals(command.getArgument1(), "local");
        assertEquals(command.getArgument2(), 3);
    }
    
    
    @Test
    public void ifCommand() {
        VMCommand command = new IfCommand("if-goto end");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_IF);
        assertEquals(command.getArgument1(), "end");
        assertEquals(command.getArgument2(), 0);
    }
    
    
    @Test
    public void labelCommand() {
        VMCommand command = new LabelCommand("label loop");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_LABEL);
        assertEquals(command.getArgument1(), "loop");
        assertEquals(command.getArgument2(), 0);
    }
    
    
    @Test
    public void functionCommand() {
        VMCommand command = new FunctionCommand("function mult 2");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_FUNCTION);
        assertEquals(command.getArgument1(), "mult");
        assertEquals(command.getArgument2(), 2);
    }
    
    
    @Test
    public void callCommand() {
        VMCommand command = new CallCommand("call mult 2");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_CALL);
        assertEquals(command.getArgument1(), "mult");
        assertEquals(command.getArgument2(), 2);
    }
    
    
    @Test
    public void returnCommand() {
        VMCommand command = new ReturnCommand("return");
        
        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_RETURN);
        assertEquals(command.getArgument1(), "");
        assertEquals(command.getArgument2(), 0);
    }
}
