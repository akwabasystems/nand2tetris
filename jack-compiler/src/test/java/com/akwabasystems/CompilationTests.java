
package com.akwabasystems;

import com.akwabasystems.parsing.CompilationEngine;
import com.akwabasystems.parsing.JackTokenizer;
import com.akwabasystems.parsing.Tokenizer;
import junit.framework.TestCase;


public class CompilationTests extends TestCase {
    
    
    public void testBasicClass() {
        StringBuilder input = new StringBuilder();
        input.append("class Main { }");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();
        
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }


    public void testClassVarDeclarations() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("  static boolean test;")
             .append("  field int x, y;")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();

        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<classVarDec>\n")
              .append("<keyword> static </keyword>\n")
              .append("<keyword> boolean </keyword>\n")
              .append("<identifier> test </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</classVarDec>\n")
              .append("<classVarDec>\n")
              .append("<keyword> field </keyword>\n")
              .append("<keyword> int </keyword>\n")
              .append("<identifier> x </identifier>\n")
              .append("<symbol> , </symbol>\n")
              .append("<identifier> y </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</classVarDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testBasicSubroutineDeclaration() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("  function void main() {")
             .append("  }")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();

        assertNotNull(compiler.toXML());
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }

    
    public void testBasicSubroutineDeclarations() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("  function void main() {")
             .append("  }")
             .append("\n")
             .append("  function int increment() {")
             .append("  }")
             .append("}");

        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();

        assertNotNull(compiler.toXML());
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> int </keyword>\n")
              .append("<identifier> increment </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testClassVarAndSubroutineDeclaration() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("  static boolean test;")
             .append("\n")
             .append("  function void main() {")
             .append("  }")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();

        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<classVarDec>\n")
              .append("<keyword> static </keyword>\n")
              .append("<keyword> boolean </keyword>\n")
              .append("<identifier> test </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</classVarDec>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    

    public void testSubroutineParameterList() {
        StringBuilder input = new StringBuilder();
        input.append("class Square {\n")
             .append("\n")
             .append("  constructor Square new(int Ax, int Ay, int Asize) {\n")
             .append("  }\n")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();

        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Square </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> constructor </keyword>\n")
              .append("<identifier> Square </identifier>\n")
              .append("<identifier> new </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("<keyword> int </keyword>\n")
              .append("<identifier> Ax </identifier>\n")
              .append("<symbol> , </symbol>\n")
              .append("<keyword> int </keyword>\n")
              .append("<identifier> Ay </identifier>\n")
              .append("<symbol> , </symbol>\n")
              .append("<keyword> int </keyword>\n")
              .append("<identifier> Asize </identifier>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testSubroutineVarDeclarations() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void main() {\n")
             .append("     var SquareGame game;\n")
             .append("     var int i, j, k;\n")
             .append("     var String s;\n")
             .append("     var Array a;\n")
             .append("  }\n")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();
        
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<varDec>\n")
              .append("<keyword> var </keyword>\n")
              .append("<identifier> SquareGame </identifier>\n")
              .append("<identifier> game </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</varDec>\n")
              .append("<varDec>\n")
              .append("<keyword> var </keyword>\n")
              .append("<keyword> int </keyword>\n")
              .append("<identifier> i </identifier>\n")
              .append("<symbol> , </symbol>\n")
              .append("<identifier> j </identifier>\n")
              .append("<symbol> , </symbol>\n")
              .append("<identifier> k </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</varDec>\n")
              .append("<varDec>\n")
              .append("<keyword> var </keyword>\n")
              .append("<identifier> String </identifier>\n")
              .append("<identifier> s </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</varDec>\n")
              .append("<varDec>\n")
              .append("<keyword> var </keyword>\n")
              .append("<identifier> Array </identifier>\n")
              .append("<identifier> a </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</varDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testReturnStatement() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void main() {\n")
             .append("     var SquareGame game;\n")
             .append("     return;\n")
             .append("  }\n")
             .append("\n")
             .append("  function int increment() {\n")
             .append("     var int counter;\n")
             .append("     return counter;\n")
             .append("  }\n")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();

        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<varDec>\n")
              .append("<keyword> var </keyword>\n")
              .append("<identifier> SquareGame </identifier>\n")
              .append("<identifier> game </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</varDec>\n")
              .append("<statements>\n")
              .append("<returnStatement>\n")
              .append("<keyword> return </keyword>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</returnStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> int </keyword>\n")
              .append("<identifier> increment </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<varDec>\n")
              .append("<keyword> var </keyword>\n")
              .append("<keyword> int </keyword>\n")
              .append("<identifier> counter </identifier>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</varDec>\n")
              .append("<statements>\n")
              .append("<returnStatement>\n")
              .append("<keyword> return </keyword>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> counter </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</returnStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")  
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testLetStatement() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void main() {\n")
             .append("    let game = game;\n")
             .append("    let a[i] = j;\n")
             .append("  }\n")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();
        
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<letStatement>\n")
              .append("<keyword> let </keyword>\n")
              .append("<identifier> game </identifier>\n")
              .append("<symbol> = </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> game </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</letStatement>\n")
              .append("<letStatement>\n")
              .append("<keyword> let </keyword>\n")
              .append("<identifier> a </identifier>\n")
              .append("<symbol> [ </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> i </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ] </symbol>\n")
              .append("<symbol> = </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> j </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</letStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testLetStatementWithOperator() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void main() {\n")
             .append("    let i = i | j;\n")
             .append("  }\n")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();
        
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<letStatement>\n")
              .append("<keyword> let </keyword>\n")
              .append("<identifier> i </identifier>\n")
              .append("<symbol> = </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> i </identifier>\n")
              .append("</term>\n")
              .append("<symbol> | </symbol>\n")
              .append("<term>\n")
              .append("<identifier> j </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</letStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    

    public void testDoStatement() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void main() {\n")
             .append("    do draw();\n")
             .append("    do game.run();\n")
             .append("    do Screen.setColor(x);\n")
             .append("    do Memory.deAlloc(this);\n")
             .append("    do Screen.drawRectangle(x, y, x, y);\n")
             .append("  }\n")
             .append("}");

        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();

        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<doStatement>\n")
              .append("<keyword> do </keyword>\n")
              .append("<identifier> draw </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<expressionList>\n")
              .append("</expressionList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</doStatement>\n")
              .append("<doStatement>\n")
              .append("<keyword> do </keyword>\n")
              .append("<identifier> game </identifier>\n")
              .append("<symbol> . </symbol>\n")
              .append("<identifier> run </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<expressionList>\n")
              .append("</expressionList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</doStatement>\n")
              .append("<doStatement>\n")
              .append("<keyword> do </keyword>\n")
              .append("<identifier> Screen </identifier>\n")
              .append("<symbol> . </symbol>\n")
              .append("<identifier> setColor </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<expressionList>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> x </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("</expressionList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</doStatement>\n")
              .append("<doStatement>\n")
              .append("<keyword> do </keyword>\n")
              .append("<identifier> Memory </identifier>\n")
              .append("<symbol> . </symbol>\n")
              .append("<identifier> deAlloc </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<expressionList>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<keyword> this </keyword>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("</expressionList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</doStatement>\n")
              .append("<doStatement>\n")
              .append("<keyword> do </keyword>\n")
              .append("<identifier> Screen </identifier>\n")
              .append("<symbol> . </symbol>\n")
              .append("<identifier> drawRectangle </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<expressionList>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> x </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> , </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> y </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> , </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> x </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> , </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> y </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("</expressionList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</doStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testIfStatement() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void main() {\n")
             .append("    if (i) {\n")
             .append("      let s = i;\n")
             .append("      let a[i] = j;\n")
             .append("    }\n")
             .append("  }\n")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();
        
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<ifStatement>\n")
              .append("<keyword> if </keyword>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> i </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<letStatement>\n")
              .append("<keyword> let </keyword>\n")
              .append("<identifier> s </identifier>\n")
              .append("<symbol> = </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> i </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</letStatement>\n")
              .append("<letStatement>\n")
              .append("<keyword> let </keyword>\n")
              .append("<identifier> a </identifier>\n")
              .append("<symbol> [ </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> i </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ] </symbol>\n")
              .append("<symbol> = </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> j </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</letStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</ifStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testIfElseStatement() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void main() {\n")
             .append("    if (i) {\n")
             .append("      let s = i;\n")
             .append("    } else {\n")
             .append("      let s = j;\n")
             .append("    }\n")
             .append("  }\n")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();
        
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> main </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<ifStatement>\n")
              .append("<keyword> if </keyword>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> i </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<letStatement>\n")
              .append("<keyword> let </keyword>\n")
              .append("<identifier> s </identifier>\n")
              .append("<symbol> = </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> i </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</letStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("<keyword> else </keyword>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<letStatement>\n")
              .append("<keyword> let </keyword>\n")
              .append("<identifier> s </identifier>\n")
              .append("<symbol> = </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> j </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</letStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</ifStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
    
    public void testWhileStatement() {
        StringBuilder input = new StringBuilder();
        input.append("class Main {\n")
             .append("\n")
             .append("  function void run() {\n")
             .append("    while (key) {\n")
             .append("      let k = key;\n")
             .append("    }\n")
             .append("  }\n")
             .append("}");
        
        Tokenizer tokenizer = new JackTokenizer(input.toString());
        CompilationEngine compiler = new CompilationEngine(tokenizer);
        compiler.compileClass();
        
        StringBuilder output = new StringBuilder();
        output.append("<class>\n")
              .append("<keyword> class </keyword>\n")
              .append("<identifier> Main </identifier>\n")
              .append("<symbol> { </symbol>\n")
              .append("<subroutineDec>\n")
              .append("<keyword> function </keyword>\n")
              .append("<keyword> void </keyword>\n")
              .append("<identifier> run </identifier>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<parameterList>\n")
              .append("</parameterList>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<subroutineBody>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<whileStatement>\n")
              .append("<keyword> while </keyword>\n")
              .append("<symbol> ( </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> key </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ) </symbol>\n")
              .append("<symbol> { </symbol>\n")
              .append("<statements>\n")
              .append("<letStatement>\n")
              .append("<keyword> let </keyword>\n")
              .append("<identifier> k </identifier>\n")
              .append("<symbol> = </symbol>\n")
              .append("<expression>\n")
              .append("<term>\n")
              .append("<identifier> key </identifier>\n")
              .append("</term>\n")
              .append("</expression>\n")
              .append("<symbol> ; </symbol>\n")
              .append("</letStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</whileStatement>\n")
              .append("</statements>\n")
              .append("<symbol> } </symbol>\n")
              .append("</subroutineBody>\n")
              .append("</subroutineDec>\n")
              .append("<symbol> } </symbol>\n")
              .append("</class>");
        assertEquals(compiler.toXML(), output.toString());
    }
    
}
