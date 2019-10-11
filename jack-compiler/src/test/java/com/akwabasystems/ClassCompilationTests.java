
package com.akwabasystems;


import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;


public class ClassCompilationTests extends TestCase {
    private static final String REGEXP = "(?:function|constructor|method)\\s+(void|\\w+)\\s+([a-z]\\w+)\\s*\\(([^\\)]*)\\)";
    private static Pattern pattern = Pattern.compile(REGEXP);
    

    @Test
    public void testFunctionParsing() {
        String constructorDeclaration = "constructor Main main()";
        String functionNoArgDeclaration = "function void main()";
        String functionArgsDeclaration = "function void main(int a, int b)";
        String methodDeclaration = "method Main initialize()";

        Matcher matcher = pattern.matcher(constructorDeclaration);
        Matcher noArgFunctionMatcher = pattern.matcher(functionNoArgDeclaration);
        Matcher argFunctionMatcher = pattern.matcher(functionArgsDeclaration);
        Matcher methodMatcher = pattern.matcher(methodDeclaration);

        assertNotNull(constructorDeclaration);
        assertTrue("Pattern matches constructor declaration", matcher.matches());
        assertTrue("Pattern matches function with no arguments", noArgFunctionMatcher.matches());
        assertTrue("Pattern matches constructor declaration", argFunctionMatcher.matches());
        assertTrue("Pattern matches method declaration", methodMatcher.matches());
    }


    @Test
    public void testNoArgumentFunction() {
        String functionNoArgDeclaration = "function void main()";

        Matcher noArgFunctionMatcher = pattern.matcher(functionNoArgDeclaration);
        assertEquals("No argument constructor should have 3 captured groups", 3, noArgFunctionMatcher.groupCount());

        boolean foundMatches = noArgFunctionMatcher.find();
        assertTrue("The expression should find matches", foundMatches);
        assertEquals("The first match should be 'void'", "void", noArgFunctionMatcher.group(1));
        assertEquals("The second match should be 'main'", "main", noArgFunctionMatcher.group(2));
        assertEquals("The third match should be empty", "", noArgFunctionMatcher.group(3));
    }


    @Test
    public void testMultipleArgumentFunction() {
        String functionArgsDeclaration = "function Main initialize(int a, int b, int c)";

        Matcher functionMatcher = pattern.matcher(functionArgsDeclaration);
        boolean foundMatches = functionMatcher.find();
        
        assertTrue("The expression should find matches", foundMatches);
        assertEquals("The first match should be 'Main'", "Main", functionMatcher.group(1));
        assertEquals("The second match should be 'initialize'", "initialize", functionMatcher.group(2));
        assertEquals("The third match should be 'int a, int b, int c'", "int a, int b, int c", functionMatcher.group(3));

        String[] arguments = functionMatcher.group(3).split(",");
        assertEquals("The function should have 3 arguments", 3, arguments.length);
    }

}
