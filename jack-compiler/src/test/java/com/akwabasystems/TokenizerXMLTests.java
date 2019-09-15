
package com.akwabasystems;

import com.akwabasystems.parsing.JackTokenizer;
import com.akwabasystems.parsing.Tokenizer;
import com.akwabasystems.utils.VMUtils;
import junit.framework.TestCase;

/**
 *
 * @author vn0gxkl
 */
public class TokenizerXMLTests extends TestCase {


    public void testCommentStripping() {
        StringBuilder input = new StringBuilder();
        input.append("// This file is part of www.nand2tetris.org\n")
             .append("// and the book \"The Elements of Computing Systems\"\n")
             .append("// by Nisan and Schocken, MIT Press.\n")
             .append("// File name: projects/10/ArrayTest/Main.jack\n")
             .append("\n")
             .append("// (identical to projects/09/Average/Main.jack)\n")
             .append("\n")
             .append("/** Computes the average of a sequence of integers. */\n")
             .append("class Main { }    // The main class");
        
        String normalizedInput = VMUtils.stripComments(input.toString());
        assertEquals(normalizedInput, "class Main { }");
        
        input = new StringBuilder();
        input.append("/**\n")
             .append(" * Implements the Square Dance game.\n")
             .append(" * This simple game allows the user to move a black square around\n")
             .append(" * the screen, and change the square's size during the movement.\n")
             .append(" * When the game starts, a square of 30 by 30 pixels is shown at the\n")
             .append(" * top-left corner of the screen. The user controls the square as follows.\n")
             .append(" * The 4 arrow keys are used to move the square up, down, left, and right.\n")
             .append(" * The 'z' and 'x' keys are used, respectively, to decrement and increment\n")
             .append(" * the square's size. The 'q' key is used to quit the game.\n")
             .append(" */\n")
             .append("\n")
             .append("class SquareGame { }");
        
        normalizedInput = VMUtils.stripComments(input.toString());
        assertEquals(normalizedInput, "class SquareGame { }");
    }
    
    
    public void testBasicXMLOutput() {
        String input = "let city = \"Paris\";"; 
        Tokenizer tokenizer = new JackTokenizer(input);
        
        String XMLString = tokenizer.toXML("tokens");
        assertTrue(XMLString.startsWith("<tokens>"));
        
        StringBuilder XMLOutput = new StringBuilder("<tokens>\n");
        XMLOutput.append("<keyword> let </keyword>\n")
                 .append("<identifier> city </identifier>\n")
                 .append("<symbol> = </symbol>\n")
                 .append("<stringConstant> Paris </stringConstant>\n")
                 .append("<symbol> ; </symbol>\n")
                 .append("</tokens>");
        assertEquals(XMLString, XMLOutput.toString());
    }


    public void testClassXMLOutput() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void main() {\n")
             .append("    var SquareGame game;\n")
             .append("\n")
             .append("    let game = SquareGame.new();\n")
             .append("    do game.run();\n")
             .append("    do game.dispose();\n")
             .append("\n")
             .append("    return;\n")
             .append("  }\n")
             .append("}");

        Tokenizer tokenizer = new JackTokenizer(input.toString());
        String XMLString = tokenizer.toXML("tokens");
        
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("<tokens>\n")
                      .append("<keyword> class </keyword>\n")
                      .append("<identifier> Main </identifier>\n")
                      .append("<symbol> { </symbol>\n")
                      .append("<keyword> function </keyword>\n")
                      .append("<keyword> void </keyword>\n")
                      .append("<identifier> main </identifier>\n")
                      .append("<symbol> ( </symbol>\n")
                      .append("<symbol> ) </symbol>\n")
                      .append("<symbol> { </symbol>\n")
                      .append("<keyword> var </keyword>\n")
                      .append("<identifier> SquareGame </identifier>\n")
                      .append("<identifier> game </identifier>\n")
                      .append("<symbol> ; </symbol>\n")
                      .append("<keyword> let </keyword>\n")
                      .append("<identifier> game </identifier>\n")
                      .append("<symbol> = </symbol>\n")
                      .append("<identifier> SquareGame </identifier>\n")
                      .append("<symbol> . </symbol>\n")
                      .append("<identifier> new </identifier>\n")
                      .append("<symbol> ( </symbol>\n")
                      .append("<symbol> ) </symbol>\n")
                      .append("<symbol> ; </symbol>\n")
                      .append("<keyword> do </keyword>\n")
                      .append("<identifier> game </identifier>\n")
                      .append("<symbol> . </symbol>\n")
                      .append("<identifier> run </identifier>\n")
                      .append("<symbol> ( </symbol>\n")
                      .append("<symbol> ) </symbol>\n")
                      .append("<symbol> ; </symbol>\n")
                      .append("<keyword> do </keyword>\n")
                      .append("<identifier> game </identifier>\n")
                      .append("<symbol> . </symbol>\n")
                      .append("<identifier> dispose </identifier>\n")
                      .append("<symbol> ( </symbol>\n")
                      .append("<symbol> ) </symbol>\n")
                      .append("<symbol> ; </symbol>\n")
                      .append("<keyword> return </keyword>\n")
                      .append("<symbol> ; </symbol>\n")
                      .append("<symbol> } </symbol>\n")
                      .append("<symbol> } </symbol>\n")
                      .append("</tokens>");
        assertEquals(XMLString, expectedOutput.toString());
    }
    
}
