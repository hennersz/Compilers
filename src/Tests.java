import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import java_cup.runtime.*;

class Tests {
    public static void main(String[] args) {
        //test();
        testParser();
    }

    private static void test() {
        try {
            Lexer lexer = new Lexer(new FileReader("tests/test1.q"));
            try {
                Symbol nextSymbol = lexer.next_token();
                while(nextSymbol.sym != sym.EOF) {
                    System.out.println(nextSymbol);
                    nextSymbol = lexer.next_token();
                }
            } catch (IOException e) {
            	System.out.println(e);
            }
            
        } catch (FileNotFoundException e) {
        	System.out.println(e);
        }
        
        //System.out.println(readFile("tests/test1.q"));
    }
    
    private static void testParser() {
    	Lexer lexer;
		try {
			lexer = new Lexer(new FileInputStream("tests/test1.q"));
			try {
				Parser parser = new Parser(lexer);
				Symbol result = parser.parse();
				// System.out.println(result.value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    private static String readFile(String path) {
        String result = "";
        try {
            FileInputStream fileStream = new FileInputStream(path);
            int content;
            try {
                while ((content = fileStream.read()) != -1) {
                    result = result + (char)content;
                }
            } catch (IOException e) {
                System.out.println("exception:");
                System.out.println(e);
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("exception:");
            System.out.println(e);
        }

        return result;
    }
}
