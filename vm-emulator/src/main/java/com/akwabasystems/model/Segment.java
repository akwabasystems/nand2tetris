
package com.akwabasystems.model;


import com.akwabasystems.utils.VMUtils;


/**
 * An enumeration of the different types of segments used in memory access commands. Each constant has a method for
 * returning its argument based on a given string, as well as the assembly codes associated with its operations.
 */
public enum Segment {
    
    CONSTANT {

        @Override
        public String argument() {
            return "constant";
        }

        @Override
        public String pushAssemblyCode(int index) {
            StringBuilder buffer = new StringBuilder();

            buffer.append(String.format("@%s\n", String.valueOf(index)))
                  .append("D=A\n")
                  .append(VMUtils.pushToStackAssemblyCode());

            return buffer.toString();
        }

        @Override
        public String popAssemblyCode(int index) {
            return "";
        }
    },

    
    LOCAL {
        
        @Override
        public String argument() {
            return "local";
        }
        
        @Override
        public String pushAssemblyCode(int index) {
            return pushFromSegmentIndex("LCL", index);
        }
        
        @Override
        public String popAssemblyCode(int index) {
            return popToSegmentIndex("LCL", index);
        }
    },
    
    
    ARGUMENT {
        
        @Override
        public String argument() {
            return "argument";
        }
        
        @Override
        public String pushAssemblyCode(int index) {
            return pushFromSegmentIndex("ARG", index);
        }
        
        @Override
        public String popAssemblyCode(int index) {
            return popToSegmentIndex("ARG", index);
        }
        
    },
    
    
    THIS {
        
        @Override
        public String argument() {
            return "this";
        }
        
        @Override
        public String pushAssemblyCode(int index) {
            return pushFromSegmentIndex("THIS", index);
        }
        
        @Override
        public String popAssemblyCode(int index) {
            return popToSegmentIndex("THIS", index);
        }
       
    },
    
    
    THAT {
        
        @Override
        public String argument() {
            return "that";
        }
        
        @Override
        public String pushAssemblyCode(int index) {
            return pushFromSegmentIndex("THAT", index);
        }
        
        @Override
        public String popAssemblyCode(int index) {
            return popToSegmentIndex("THAT", index);
        }
        
    },
    
    
    POINTER {
        
        @Override
        public String argument() {
            return "pointer";
        }
        
        @Override
        public String pushAssemblyCode(int index) {
            return pushFromDirectSegmentIndex("3", index);
        }
        
        @Override
        public String popAssemblyCode(int index) {
            return popToDirectSegmentIndex("3", index);
        }
        
    },
    
    
    TEMP {
        
        @Override
        public String argument() {
            return "temp";
        }
        
        @Override
        public String pushAssemblyCode(int index) {
            return pushFromDirectSegmentIndex("5", index);
        }
        
        @Override
        public String popAssemblyCode(int index) {
            return popToDirectSegmentIndex("5", index);
        }
    },
    
    
    STATIC {
        
        @Override
        public String argument() {
            return "static";
        }
        
        @Override
        public String pushAssemblyCode(int index) {
            return "";
        }
        
        @Override
        public String popAssemblyCode(int index) {
            return "";
        }
    };


    /**
     * Returns the argument for this enum constant
     * 
     * @return the argument for this enum constant
     */
    public abstract String argument();
    
    
    /**
     * Returns the assembly code of the push command for this enum constant
     * 
     * @param index         the index of the memory segment at which to push the item 
     * @return the assembly code of the push command for this enum constant
     */
    public abstract String pushAssemblyCode(int index);
    
    
    /**
     * Returns the assembly code of the pop command for this enum constant
     * 
     * @param index         the index of the memory segment from which to pop the item 
     * @return the assembly code of the pop command for this enum constant
     */
    public abstract String popAssemblyCode(int index);
    
    
    /**
     * Returns the assembly code of the push command from the given segment and onto the stack
     * 
     * @param segment       the segment from which to get the value
     * @param index         the index of the memory segment at which to get the value 
     * @return the assembly code of the push command from the given segment and onto the stack
     */
    public static String pushFromSegmentIndex(String segment, int index) {
        return pushFromSegmentMapping(segment, index, false);
    }
    
    
    /**
     * Returns the assembly code of a direct push command from the given segment onto the stack. A direct push uses
     * the value of the A register rather than that of M, which is the memory addressed by A.
     * 
     * @param segment       the segment from which to get the value
     * @param index         the index of the memory segment at which to get the value 
     * @return the assembly code of a direct push command from the given segment onto the stack
     */
    public static String pushFromDirectSegmentIndex(String segment, int index) {
        return pushFromSegmentMapping(segment, index, true);
    }
    
    
    /**
     * Returns the assembly code for pushing an item from a given segment onto the stack
     * 
     * @param segment           the segment from which to get the value
     * @param index             the index of the memory segment at which to get the value 
     * @param isDirectMapping   a flag that specifies whether to use a direct mapping (A register) rather than an
     *                          indirect one (the M memory)
     * @return the assembly code for pushing an item from a given segment onto the stack
     */
    public static String pushFromSegmentMapping(String segment, int index, boolean isDirectMapping) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(String.format("@%s\n", segment))
              .append((isDirectMapping)? "D=A\n" : "D=M\n")
              .append(String.format("@%s\n", index))
              .append("A=D+A\n")
              .append("D=M\n")
              .append(VMUtils.pushToStackAssemblyCode());

        return buffer.toString();
    }
    
    
    /**
     * Returns the assembly code of a pop command from the stack into the given segment
     * 
     * @param segment       the segment into which to push the value
     * @param index         the index of the memory segment at which to push the item 
     * @return the assembly code of the pop command into the given segment
     */
    public static String popToSegmentIndex(String segment, int index) {
        return popToSegmentMapping(segment, index, false);
    }


    /**
     * Returns the assembly code of a direct pop command from the stack into the given segment. A direct pop uses the
     * value of the A register rather than that of M, which is the memory addressed by A.
     * 
     * @param segment       the segment into which to push the value
     * @param index         the index of the memory segment at which to push the item 
     * @return the assembly code of the direct pop command from the stack into the given segment
     */
    public static String popToDirectSegmentIndex(String segment, int index) {
        return popToSegmentMapping(segment, index, true);
    }
    
    
    /**
     * Returns the assembly code for popping an item from the stack into a given segment.
     * 
     * @param segment           the segment into which to push the value
     * @param index             the index of the memory segment at which to put the value 
     * @param isDirectMapping   a flag that specifies whether to use a direct mapping (A register) rather than an
     *                          indirect one (the M memory)
     * @return the assembly code for popping an item from the stack into a given segment
     */
    public static String popToSegmentMapping(String segment, int index, boolean isDirectMapping) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(String.format("@%s\n", segment))
              .append((isDirectMapping)? "D=A\n" : "D=M\n")
              .append(String.format("@%s\n", index))
              .append("D=D+A\n")
              .append("@R15\n")
              .append("M=D\n");

        buffer.append(VMUtils.popFromStackAssemblyCode());

        buffer.append("@R15\n")
              .append("A=M\n")
              .append("M=D");

        return buffer.toString();
    }
    
    
    /**
     * Returns the assembly code of the push command from the given static segment and onto the stack. By convention,
     * a static variable j in a file f can be translated into the following symbol: @f.j.
     * 
     * @param fileName      the name of the file for the current command; needed to compute the symbol name.
     * @param index         the index of the memory segment from which to get the value 
     * @return the assembly code of the push command from the given static segment and onto the stack
     */
    public static String pushToStaticSegmentIndex(String fileName, int index) {
        String symbol = String.format("@%s.%s", fileName, index);
        
        StringBuilder buffer = new StringBuilder();
        buffer.append(symbol)
                .append("\n")
                .append("D=M\n")
                .append(VMUtils.pushToStackAssemblyCode());

          return buffer.toString();
    }


    /**
     * Returns the assembly code of the pop command from the stack into the given static segment. By convention,
     * a static variable j in a file f can be translated into the following symbol: @f.j.
     * 
     * @param fileName      the name of the file for the current command; needed to compute the symbol name.
     * @param index         the index of the memory segment at which to store the value 
     * @return the assembly code of the pop command from the stack into the given static segment
     */
    public static String popFromStaticSegmentIndex(String fileName, int index) {
        String symbol = String.format("@%s.%s", fileName, index);
        
        StringBuilder builder = new StringBuilder();
        builder.append("@SP\n")
               .append("AM=M-1\n")
               .append("D=M\n")
               .append(symbol)
               .append("\n")
               .append("M=D");
        
        return builder.toString();
    }
    

    /**
     * Returns the enum constant whose argument matches the given argument
     * 
     * @param argument              the argument for which to find the enum constant
     * @return the enum constant whose argument matches the given argument
     */
    public static Segment fromArgument(String argument) {
        
        if(argument == null) {
            return null;
        }
        
        for(Segment type : values()) {
            if(type.argument().equals(argument)) {
                return type;
            }
        }

        return null;
    }

}
