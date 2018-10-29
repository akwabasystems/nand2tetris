
package com.akwabasystems.vm;


import com.akwabasystems.model.CommandType;
import com.akwabasystems.model.VMCommand;
import junit.framework.TestCase;


public class CommandTypeTests extends TestCase {
    
    
    public void testCommandsFromSyntax() {
        
        VMCommand command = CommandType.fromCommand("add");

        assertNotNull(command);
        assertEquals(command.getType(), CommandType.C_ARITHMETIC);
        assertEquals(command.getArgument1(), "");

        String[] commands = { "sub", "neg", "eq", "gt", "lt", "and", "or", "not" };
        
        for(String commandName : commands) {
            command = CommandType.fromCommand(commandName);
            assertNotNull(command);
            assertEquals(command.getType(), CommandType.C_ARITHMETIC);
        }
        
        command = CommandType.fromCommand("label loop");
        assertEquals(command.getType(), CommandType.C_LABEL);
        assertEquals(command.getArgument1(), "loop");
        assertEquals(command.getArgument2(), 0);
        
        command = CommandType.fromCommand("goto end");
        assertEquals(command.getType(), CommandType.C_GOTO);
        assertEquals(command.getArgument1(), "end");
        assertEquals(command.getArgument2(), 0);
        
        command = CommandType.fromCommand("push argument 1");
        assertEquals(command.getType(), CommandType.C_PUSH);
        assertEquals(command.getArgument1(), "argument");
        assertEquals(command.getArgument2(), 1);
        
        command = CommandType.fromCommand("pop local 3");
        assertEquals(command.getType(), CommandType.C_POP);
        assertEquals(command.getArgument1(), "local");
        assertEquals(command.getArgument2(), 3);
        
        command = CommandType.fromCommand("if-goto end");
        assertEquals(command.getType(), CommandType.C_IF);
        assertEquals(command.getArgument1(), "end");
        assertEquals(command.getArgument2(), 0);
        
        command = CommandType.fromCommand("function mult 2");
        assertEquals(command.getType(), CommandType.C_FUNCTION);
        assertEquals(command.getArgument1(), "mult");
        assertEquals(command.getArgument2(), 2);
        
        command = CommandType.fromCommand("call mult 2");
        assertEquals(command.getType(), CommandType.C_CALL);
        assertEquals(command.getArgument1(), "mult");
        assertEquals(command.getArgument2(), 2);
        
        command = CommandType.fromCommand("return");
        assertEquals(command.getType(), CommandType.C_RETURN);
        assertEquals(command.getArgument1(), "");
        assertEquals(command.getArgument2(), 0);
        
    }

}
