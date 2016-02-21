import java_cup.runtime.*;

%%
%class Lexer
%unicode
%cup
%line
%column

%eofval{
    return sym(sym.EOF);
%eofval}

%{

  private Symbol symbol(int type) {
    print_lexeme(type, null);
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    print_lexeme(type, value);
    return new Symbol(type, yyline, yycolumn, value);
  }

%}

Whitespace = \r|\n|\r\n|" "|"\t"

Letter = [a-zA-Z]
Digit = [0-9]
IdChar = {Letter} | {Digit} | "_"
Identifier = {Letter}{IdChar}*
Integer = (0|[1-9]{Digit}*)

%%
<YYINITIAL> {

}

[^]     { throw new Error("Line " + yyline+1 + ", Column " + yycolumn); }
