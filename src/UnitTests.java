import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import java_cup.runtime.Symbol;

import org.junit.Test;

public class UnitTests {

	private void assertParsing(String string, List<Integer> expectedSym) {
        Lexer lexer = new Lexer(new StringReader(string));
        Iterator<Integer> expectedSymIterator = expectedSym.iterator();
        
        try {
        	Symbol nextSymbol = null;
            while(expectedSymIterator.hasNext()) {
                nextSymbol = lexer.next_token();
                Integer nextExpectedSymbol = expectedSymIterator.next();
                assertTrue(nextSymbol.sym == (int)nextExpectedSymbol);
            }
            assertTrue(lexer.next_token().sym == sym.EOF);
        } catch (IOException e) {
        	fail(e.toString());
        }
		
	}
	private void assertParsing2(String string) {
    	Lexer lexer;

		lexer = new Lexer(new StringReader(string));
		try {
			Parser parser = new Parser(lexer);
			Object result = parser.parse().value;
			System.out.println(result.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean matchParsing(String string, List<Integer> expectedSym) {
		 Lexer lexer = new Lexer(new StringReader(string));
	        Iterator<Integer> expectedSymIterator = expectedSym.iterator();
	        
	        try {
	        	Symbol nextSymbol = null;
	            while(expectedSymIterator.hasNext()) {
	                nextSymbol = lexer.next_token();
	                Integer nextExpectedSymbol = expectedSymIterator.next();
	                if (nextSymbol.sym != (int)nextExpectedSymbol) return false;
	            }
	            if (lexer.next_token().sym != sym.EOF) return false;
	        } catch (IOException e) {
	        	fail(e.toString());
	        }

			return true;
	}
	
	private void assertEmtpyParse(String string) {
		Lexer lexer = new Lexer(new StringReader(string));
		
		try {
			if (lexer.next_token().sym != sym.EOF) {
				fail("Not an empty parse");
			}
		} catch (IOException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testInteger() {
		assertParsing("1", Arrays.asList(sym.INTLITERAL));
		assertParsing("0", Arrays.asList(sym.INTLITERAL));
		assertFalse(matchParsing("01", Arrays.asList(sym.INTLITERAL)));
	}
	@Test
	public void testIntegerAndDots() {
		assertParsing("1.", Arrays.asList(sym.INTLITERAL, sym.DOT));
		assertParsing(".1", Arrays.asList(sym.DOT, sym.INTLITERAL));
	}
	
	@Test
	public void testIntegers() {
		assertParsing("1\n1", Arrays.asList(sym.INTLITERAL, sym.INTLITERAL));
		assertParsing("1 1", Arrays.asList(sym.INTLITERAL, sym.INTLITERAL));
		assertParsing("1 1 1", Arrays.asList(sym.INTLITERAL, sym.INTLITERAL, sym.INTLITERAL));
	}
	
	@Test
	public void testFloat() {
		assertParsing("1.0", Arrays.asList(sym.FLOATLITERAL));
		assertParsing("1.01", Arrays.asList(sym.FLOATLITERAL));
		assertParsing("1.1", Arrays.asList(sym.FLOATLITERAL));
		assertParsing("1.10", Arrays.asList(sym.FLOATLITERAL));
		assertParsing("1.00", Arrays.asList(sym.FLOATLITERAL));
		assertParsing("0.1", Arrays.asList(sym.FLOATLITERAL));
		assertParsing("0.10", Arrays.asList(sym.FLOATLITERAL));
		assertParsing("0.0", Arrays.asList(sym.FLOATLITERAL));
	}
	
	@Test
	public void testFloatWithDot() {
		assertParsing("1.1.", Arrays.asList(sym.FLOATLITERAL, sym.DOT));
		assertParsing("1.1.1", Arrays.asList(sym.FLOATLITERAL, sym.DOT, sym.INTLITERAL));
		assertParsing("1.1.1.1", Arrays.asList(sym.FLOATLITERAL, sym.DOT, sym.FLOATLITERAL));
	}
	
	@Test
	public void testFloats() {
		assertParsing("1.1 1.1", Arrays.asList(sym.FLOATLITERAL, sym.FLOATLITERAL));
		assertParsing("1.1\n1.1", Arrays.asList(sym.FLOATLITERAL, sym.FLOATLITERAL));
	}
	
	@Test
	public void testString() {
		assertParsing("\"hello\"", Arrays.asList(sym.STRINGLITERAL));
		assertParsing("\"int\"", Arrays.asList(sym.STRINGLITERAL));
		assertParsing("\"string\"", Arrays.asList(sym.STRINGLITERAL));
		assertParsing("\"\"", Arrays.asList(sym.STRINGLITERAL));
		assertParsing("\"1\"", Arrays.asList(sym.STRINGLITERAL));
		assertParsing("\"first second\"", Arrays.asList(sym.STRINGLITERAL));
		assertParsing("\"first \\\" second\"", Arrays.asList(sym.STRINGLITERAL));
		assertFalse(matchParsing("\"stringwith\"inthemiddle\"", Arrays.asList(sym.STRINGLITERAL)));
	}
	
	@Test
	public void testChar() {
		assertFalse(matchParsing("''", Arrays.asList(sym.CHARLITERAL)));
		assertParsing("''", Arrays.asList(sym.error, sym.error));
		
		assertFalse(matchParsing("'ab'", Arrays.asList(sym.CHARLITERAL)));
		assertParsing("'ab'", Arrays.asList(sym.error, sym.IDENTIFIER, sym.error));
		
		assertParsing("'0'", Arrays.asList(sym.CHARLITERAL));
		assertParsing("'a'", Arrays.asList(sym.CHARLITERAL));
		assertParsing("'/'", Arrays.asList(sym.CHARLITERAL));
		assertParsing("'\\'", Arrays.asList(sym.error, sym.error, sym.error));
		assertParsing("'\\\\'", Arrays.asList(sym.CHARLITERAL));
		assertParsing("'\0'", Arrays.asList(sym.CHARLITERAL));
	}
	@Test	
	public void testChars() {
		assertParsing("'a' 'b'", Arrays.asList(sym.CHARLITERAL, sym.CHARLITERAL));
		assertParsing("'a'.'b'", Arrays.asList(sym.CHARLITERAL, sym.DOT, sym.CHARLITERAL));
		assertParsing("'a''b'", Arrays.asList(sym.CHARLITERAL, sym.CHARLITERAL));
	}
	@Test
	public void testBool(){
		assertParsing("true", Arrays.asList(sym.TRUE));
		assertParsing("false", Arrays.asList(sym.FALSE));
		assertParsing("true1", Arrays.asList(sym.IDENTIFIER));
	}
	@Test
	public void testKeyword() {
		assertParsing("char", Arrays.asList(sym.CHAR));
		assertParsing("bool", Arrays.asList(sym.BOOL));
		assertParsing("float", Arrays.asList(sym.FLOAT));
		assertParsing("dict", Arrays.asList(sym.DICT));
		assertParsing("string", Arrays.asList(sym.STRING));
		assertParsing("list", Arrays.asList(sym.LIST));
		assertParsing("in", Arrays.asList(sym.IN));
		assertParsing("tdef", Arrays.asList(sym.TDEF));
		assertParsing("true", Arrays.asList(sym.TRUE));
		assertParsing("false", Arrays.asList(sym.FALSE));
		assertParsing("def", Arrays.asList(sym.DEF));
		assertParsing("while", Arrays.asList(sym.WHILE));
		assertParsing("do", Arrays.asList(sym.DO));
		assertParsing("return", Arrays.asList(sym.RETURN));
		assertParsing("if", Arrays.asList(sym.IF));
		assertParsing("else", Arrays.asList(sym.ELSE));
		assertParsing("foreach", Arrays.asList(sym.FOREACH));
		assertParsing("read", Arrays.asList(sym.READ));
		assertParsing("print", Arrays.asList(sym.PRINT));
		assertParsing("void", Arrays.asList(sym.VOID));
	}
	
	@Test
	public void testOperators() {
		assertParsing("[|", Arrays.asList(sym.LDICT));
		assertParsing("|]", Arrays.asList(sym.RDICT));
		assertParsing("[", Arrays.asList(sym.LBRACKET));
		assertParsing("]", Arrays.asList(sym.RBRACKET));
		assertParsing("(", Arrays.asList(sym.LPAREN));
		assertParsing(")", Arrays.asList(sym.RPAREN));
		assertParsing("{", Arrays.asList(sym.LBRACE));
		assertParsing("}", Arrays.asList(sym.RBRACE));
		assertParsing(".", Arrays.asList(sym.DOT));
		assertParsing("::", Arrays.asList(sym.CONCAT));
		assertParsing(":", Arrays.asList(sym.COLON));
		assertParsing(";", Arrays.asList(sym.SEMICOLON));
		assertParsing("==", Arrays.asList(sym.EQEQ));
		assertParsing("=", Arrays.asList(sym.EQ));
		assertParsing("+", Arrays.asList(sym.PLUS));
		assertParsing("-", Arrays.asList(sym.MINUS));
		assertParsing("*", Arrays.asList(sym.TIMES));
		assertParsing("^", Arrays.asList(sym.POWER));
		assertParsing("/", Arrays.asList(sym.DIVIDE));
		assertParsing("&&", Arrays.asList(sym.AND));
		assertParsing("!=", Arrays.asList(sym.NEQ));
		assertParsing("!", Arrays.asList(sym.NOT));
		assertParsing("<=", Arrays.asList(sym.LTE));
		assertParsing("<", Arrays.asList(sym.LT));
		assertParsing(">=", Arrays.asList(sym.GTE));
		assertParsing(">", Arrays.asList(sym.GT));
	}
	
	@Test
	public void testComments() {
		assertEmtpyParse("// Hello 123");
		assertParsing("// Hello \n abc", Arrays.asList(sym.IDENTIFIER));
		assertParsing("abc // Hello", Arrays.asList(sym.IDENTIFIER));
		assertParsing("abc // Hello \n cde", Arrays.asList(sym.IDENTIFIER, sym.IDENTIFIER));
		assertParsing("abc /** Hello \n\n\n world */ \n cde", Arrays.asList(sym.IDENTIFIER, sym.IDENTIFIER));
		assertParsing("abc /** Hello", Arrays.asList(sym.IDENTIFIER, sym.DIVIDE, sym.TIMES, sym.TIMES, sym.IDENTIFIER));
	}
	
	@Test
	public void testIdentifiers() {
		assertParsing("bla", Arrays.asList(sym.IDENTIFIER));
		assertParsing("bla1", Arrays.asList(sym.IDENTIFIER));
		assertParsing("1bla", Arrays.asList(sym.INTLITERAL, sym.IDENTIFIER));
		assertParsing("bla_bla", Arrays.asList(sym.IDENTIFIER, sym.error, sym.IDENTIFIER));
	}
	
	@Test
	public void testDictionary(){
		assertParsing("numbers = dict()", Arrays.asList(sym.IDENTIFIER, sym.EQ, sym.DICT, sym.LPAREN, sym.RPAREN)); 
		
	}
	@Test
	public void testTypedef(){
		assertParsing("tdef person : name:string, surname:string, age:int;", Arrays.asList(sym.TDEF, sym.IDENTIFIER, sym.COLON, sym.IDENTIFIER, sym.COLON, sym.STRING,sym.COMMA, sym.IDENTIFIER, sym.COLON, sym.STRING, sym.COMMA, sym.IDENTIFIER, sym.COLON, sym.INT, sym.SEMICOLON));
	}
	
	
	
	
}
