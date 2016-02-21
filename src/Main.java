import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import java_cup.runtime.*;

class Main {
  public static void main(String[] argv) {

    if(argv.length < 1
    || argv.length > 2){
      System.err.printf("USAGE:\n\n  %s\n    or\n  %s\n    or\n  %s\n",
"java -cp bin:lib/java-cup-11b-runtime.jar Main input_file",
"java -cp bin:lib/java-cup-11b-runtime.jar Main show-lexing input_file",
"java -cp bin:lib/java-cup-11b-runtime.jar Main show-parsing input_file"
      );
      System.exit(1);
    }

    boolean showLexing = false;
    boolean showParsing = false;
    String inputFile = null;

    for(String arg : argv){
      if     (arg.equals("show-lexing"))  showLexing = true;
      else if(arg.equals("show-parsing")) showParsing = true;
      else                                inputFile = arg;
    }

    if(showLexing && showParsing){
      System.err.println(
        "Specify at most one of show-lexing or show-parsing");
      System.exit(1);
    }

    if(inputFile == null){
      System.err.println("Specify input file as the last argument");
      System.exit(1);
    }

    try {
      Lexer lexer   = new Lexer(new FileReader(inputFile));
      Parser parser = new Parser(lexer);

      if(showLexing){
        lexer.debug(true);
      }

      java_cup.runtime.Symbol symbol = null;

      if(showParsing){
        parser.debug(true);
        System.out.println("digraph G {");
        Symbol result = parser.parse();
        System.out.println("}");
      } else {
        Symbol result = parser.parse();
      }

      //System.out.println();
    } catch (FileNotFoundException e) {
      System.err.printf("Could not find file <%s>\n", argv[0]);
      System.exit(1);

    } catch (/* Yuk, but CUP gives us no choice */ Exception e) {
      e.printStackTrace();
    }
  }
}
