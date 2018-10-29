
package com.akwabasystems;

import com.akwabasystems.model.Token;
import com.akwabasystems.model.TokenType;
import com.akwabasystems.parsing.JackTokenizer;
import com.akwabasystems.parsing.Tokenizer;
import junit.framework.TestCase;

/**
 *
 * @author vn0gxkl
 */
public class TokenizerTests extends TestCase {
    
    
    public void testKeywordAndSymbolTokens() {
        StringBuilder input = new StringBuilder();
        input.append("class,   constructor; function, method, field, \n")
             .append("static, var, int, char, boolean, void, true, \n")
             .append("false, null, this, let, do, if, else, while, return\n");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        assertNotNull(tokenizer);
        
        Token token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "class");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ",");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "constructor");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "function");
        
        tokenizer.nextToken();
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ",");
        
        tokenizer.nextToken();
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "static");
    }
    
    
    public void testClassConstruct1() {
        StringBuilder input = new StringBuilder();
        input.append("class Bar {\n")
             .append("    method Fraction foo() {\n")
             .append("        var int temp;\n")
             .append("        let counter;\n")
             .append("    }\n")
             .append("}\n");

        Tokenizer tokenizer = new JackTokenizer(input.toString());
        
        Token token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "class");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "Bar");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "{");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "method");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "Fraction");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "foo");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "(");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ")");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "{");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "var");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "int");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "temp");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "let");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "counter");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "}");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "}");
    }
    
    
    public void testStringConstant() {
        String input = "let city = \"Paris\";"; 
        Tokenizer tokenizer = new JackTokenizer(input);
        
        Token token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "let");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "city");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "=");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.STRING_CONSTANT);
        assertEquals(token.getText(), "Paris");

        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
    }
    
    
    public void testIntegerConstant() {
        String input = "let counter = 123;"; 
        Tokenizer tokenizer = new JackTokenizer(input);
        
        Token token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "let");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "counter");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "=");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.INT_CONSTANT);
        assertEquals(token.getText(), "123");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
    }
    
    
    public void testMainClass() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("  static boolean test;    // Added for testing -- there is no static keyword\n")
             .append("  // in the Square files.\n")
             .append("   \n")
             .append("  function void main() {\n")
             .append("     var SquareGame game;\n")
             .append("     let game = SquareGame.new();\n")
             .append("     do game.run();\n")
             .append("     do game.dispose();\n")
             .append("     return;\n")
             .append("  }\n")
             .append("}");
        Tokenizer tokenizer = new JackTokenizer(input.toString());
         
        Token token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "class");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "Main");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "{");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "static");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "boolean");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "test");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "function");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "void");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "main");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "(");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ")");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "{");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "var");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "SquareGame");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "game");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "let");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "game");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "=");

        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "SquareGame");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ".");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "new");

        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "(");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ")");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "do");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "game");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ".");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "run");

        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "(");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ")");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "do");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "game");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ".");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "dispose");

        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "(");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ")");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "return");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), ";");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "}");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "}");
    }
    
    
    public void testIfElseStatement() {
        StringBuilder input = new StringBuilder();
        input.append("function void test() {  // Added to test Jack syntax that is not use in\n")
             .append("  var Array a;\n")
             .append("  if (false) {\n")
             .append("    let s = \"string constant\";\n")
             .append("    let a[1] = -32767;\n")
             .append("  } else {              // There is no else keyword in the Square files.\n")
             .append("    let i = i * (-j);\n")
             .append("    let j = j / (-2);   // note: unary negate constant 2\n")
             .append("    let i = i | j;\n")
             .append("  }\n")
             .append("  return;\n")
             .append("}");
        Tokenizer tokenizer = new JackTokenizer(input.toString());
         
        Token token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "function");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "void");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "test");
        
        tokenizer.nextToken();
        tokenizer.nextToken();
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "var");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "Array");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "a");
        
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "if");
        
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "false");
        
        tokenizer.nextToken();
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.KEYWORD);
        assertEquals(token.getText(), "let");
        
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "=");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.STRING_CONSTANT);
        assertEquals(token.getText(), "string constant");
        
        tokenizer.nextToken();
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.IDENTIFIER);
        assertEquals(token.getText(), "a");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "[");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.INT_CONSTANT);
        assertEquals(token.getText(), "1");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "]");
        
        tokenizer.nextToken();
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.SYMBOL);
        assertEquals(token.getText(), "-");
        
        token = tokenizer.nextToken();
        assertEquals(token.getType(), TokenType.INT_CONSTANT);
        assertEquals(token.getText(), "32767");
    }
}
