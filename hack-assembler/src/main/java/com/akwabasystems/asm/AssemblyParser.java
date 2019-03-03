
package com.akwabasystems.asm;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A parser for the Hack assembly language. 
 * 
 * It uses the following parsing logic: first, it reads the input file and generates a list of instruction objects 
 * for each line. This process is similar to that of generating an abstract syntax tree (or AST).
 * It then processes that collection using the two-pass approach defined in the book: the first pass adds entries in 
 * the symbol table for all labels; and the second pass processes all variables. Finally, it revisits all
 * A-instructions and replaces their symbols with their respective memory addresses.
 * 
 * This class pre-processes all commands by removing all in-line comments.
 */
public final class AssemblyParser implements Parser {
    
    private final List<Instruction> instructions = Collections.synchronizedList(new ArrayList<Instruction>());
    private SymbolTable symbolTable;
    private int addressCounter = 16;
    private int instructionCounter = 0;
    
    
    /**
     * Returns the instruction list for this parser
     * 
     * @return the instruction list for this parser
     */
    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }
    
    
    /**
     * Parses the given command and adds its corresponding instruction object to the instruction list
     * 
     * @param command       the command to parse
     */
    @Override
    public void parse(String command) {
        String syntax = command.replaceAll("\\/\\/[^\n\r]*", "").trim();
        
        boolean isComment = syntax.startsWith("//") || (syntax.startsWith("/*") && syntax.endsWith("*/"));
        boolean isValidInstruction = (!syntax.isEmpty() && !isComment);

        if(isValidInstruction) {
            boolean isAInstruction = (syntax.charAt(0) == '@');
            boolean isLabel = (syntax.startsWith("(") && syntax.endsWith(")"));
        
            Instruction instruction = (isAInstruction)? new AInstruction(syntax) : 
                                      (isLabel)? new LInstruction(syntax) : new CInstruction(syntax);
            instructions.add(instruction);
        }
    }
    
    
    /**
     * Returns the binary code for the program that was parsed
     * 
     * @return the binary code for the program that was parsed
     */
    @Override
    public String binaryCode() {
        symbolTable = new SymbolTable();
        symbolTable.initialize();
        
        processSymbols();
        return generateBinaryCode();
    }
    
    
    /**
     * Processes all symbols (labels and variables) contained in the instructions of this assembly parser, and replaces
     * all symbols in A-instructions with their respective memory addresses
    */
    private void processSymbols() {
        synchronized(instructions) {

            instructions.stream().forEach((instruction) -> {
                if(instruction.isAInstruction() || instruction.isCInstruction()) {
                    instructionCounter++;
                } else if(instruction.isLCommand()) {
                    String label = instruction.getCommand().replace("(", "").replace(")", "");
                    symbolTable.addEntry(label, instructionCounter);
                }
            });

            instructions.stream().forEach((instruction) -> {
                if(instruction.isAInstruction()) {
                    boolean isVariable = false;
                    String bits = instruction.getCommand().substring(1);

                    /** In order to determine whether the value is a constant, try to decode an integer from
                     * it. If the operation fails, then it is a variable rather than a constant.
                     */
                    try {

                        int constant = Integer.decode(String.valueOf(bits));

                    } catch (NumberFormatException notANumber) {
                        isVariable = true;
                    }

                    if(isVariable && !symbolTable.contains(bits)) {
                        symbolTable.addEntry(bits, addressCounter++);
                    }
                }
            });
            
            /** Revisits all A-instructions and resolve their symbol values */
            instructions.stream().forEach((instruction) -> {
                if(instruction.isAInstruction()) {
                    String bits = instruction.getCommand().substring(1);
                    
                    if(symbolTable.contains(bits)) {
                        int address = symbolTable.getAddress(bits);
                        instruction.setCommand(String.format("@%s", address));
                    }
                }
            });
        }
    }
    
    
    /**
     * Returns the binary code for the program parsed by this assembly parser
     * 
     * @return the binary code for the program parsed by this assembly parser
     */
    private String generateBinaryCode() {
        StringBuilder buffer = new StringBuilder();

        synchronized(instructions) {
            
            instructions.stream().forEach((instruction) -> {
                String binaryCode = instruction.binaryCode();
                
                if(!binaryCode.isEmpty()) {
                    buffer.append(binaryCode);
                    
                    boolean isLast = (instructions.indexOf(instruction) == instructions.size() - 1);
                
                    if(!isLast) {
                        buffer.append("\n");
                    }
                }
            });
        }
        
        return buffer.toString();
    }
    
}
