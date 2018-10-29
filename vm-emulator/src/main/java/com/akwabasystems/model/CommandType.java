
package com.akwabasystems.model;

import com.akwabasystems.utils.VMUtils;
import java.util.Arrays;
import java.util.List;


/**
 * An enumeration of the different command types. Each constant has a method for returning its command list, as well
 * as the instance of the VMCommand associated with it.
 */
public enum CommandType {
    
    C_ARITHMETIC {
        
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new ArithmeticCommand(command);
        }
    },
    
    C_PUSH {
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"push"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new PushCommand(command);
        }
    },
    
    C_POP {
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"pop"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new PopCommand(command);
        }
    },
    
    C_LABEL {
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"label"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new LabelCommand(command);
        }
    },
    
    
    C_GOTO {
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"goto"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new GotoCommand(command);
        }
    },
    
    
    C_IF {
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"if-goto"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new IfCommand(command);
        }
    },
    
    C_FUNCTION {
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"function"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new FunctionCommand(command);
        }
    },
    
    
    C_RETURN {
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"return"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new ReturnCommand(command);
        }
    },
    
    
    C_CALL {
        @Override
        public List<String> commandList() {
            return Arrays.asList(new String[] {"call"});
        }
        
        @Override
        public VMCommand command(String command) {
            return new CallCommand(command);
        }
    };
    
    
    /**
     * Returns the list of commands for this enum constant
     * 
     * @return the list of commands for this enum constant
     */
    public abstract List<String> commandList();

    
    /**
     * Returns the VMCommand associated to this enum constant
     * 
     * @param command           the command string
     * @return the VMCommand associated to this enum constant
     */
    public abstract VMCommand command(String command);
    

    /**
     * Returns the enum constant whose command matches the given command
     * 
     * @param command          the command for which to find the enum constant
     * @return the enum constant whose command matches the given command
     */
    public static VMCommand fromCommand(String command) {
        
        if(command == null) {
            return null;
        }

        String syntax = VMUtils.stripComments(command);
        String commandName = syntax.split(" ")[0];

        for(CommandType type : values()) {
            if(type.commandList().contains(commandName)) {
                return (VMCommand) type.command(syntax);
            }
        }

        return null;
    }

}
