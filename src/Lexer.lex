import java_cup.runtime.*;

%%
%class Lexer
%unicode
%cup
%line
%column

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

%}

// Declarations
EndOfLine = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {EndOfLine} | [ \t\f]

MultiLineComment = "/#"[^#]~"#/"|"/#""#"+"/"
// Single line comment might be at the end of the file
// So EndOfLine is not necessary
SingleLineComment = "#"{InputCharacter}*{EndOfLine}?
Comment = ({MultiLineComment}|{SingleLineComment})

Letter = [a-zA-Z]
Digit = [0-9]
// Have we escaped everything?
Punctuation = [ \!#$%&()\*\+,-\.\/:;<=>\?@\[\\\]\^_`{¦}\~ \" \']

Identifier = {Letter}("_"|{Letter}|{Digit})*
Character = "'"({Letter}|{Digit}|{Punctuation})"'"
Boolean = ("T"|"F")
Integer = (0|[1-9][0-9]*)
// Rational can be an integer + What is the underscore?
Rational = ({Integer}|(({Integer}"_")?{Integer}"/"{Integer}))
Float = "-"?(0|[1-9][0-9]*)"."[0-9]+

%state STRING

%%
<YYINITIAL> {

  // Comments and whitespace
  {Comment}             { /* Do nothing */ }
  {WhiteSpace}          { /* Do nothing */ }

  // Primitives keywords
  "bool"                { return symbol(sym.BOOL); }
  "int"                 { return symbol(sym.INT); }
  "rat"                 { return symbol(sym.RAT); }
  "float"               { return symbol(sym.FLOAT); }
  "char"                { return symbol(sym.CHAR); }

  // Bool operators
  "!"                   { return symbol(sym.NOT); }
  "&&"                  { return symbol(sym.AND); }
  "||"                  { return symbol(sym.OR); }

  // Arithmetic operators
  "+"                   { return symbol(sym.PLUS); }
  "-"                   { return symbol(sym.MINUS); }
  "*"                   { return symbol(sym.TIMES); }
  "/"                   { return symbol(sym.DIV); }
  "^"                   { return symbol(sym.POW); }

  // Comparisons (Does it include greater than?)
  "<"                   { return symbol(sym.LTHAN); }
  "<="                  { return symbol(sym.THANEQ); }
  "=="                  { return symbol(sym.EQUALS); }
  "!="                  { return symbol(sym.NEQUALS); }

  // Aggregate keywords
  "dict"                { return symbol(sym.DICT); }
  "seq"                 { return symbol(sym.SEQ); }
  "top"                 { return symbol(sym.TOP); }

  // Operations on Aggregate
  "::"                  { return symbol(sym.APPEND); }
  "len"                 { return symbol(sym.LEN); }
  "in"                  { return symbol(sym.IN); }

  // Declarations
  "tdef"                { return symbol(sym.TDEF); }
  "fdef"                { return symbol(sym.FDEF); }
  "alias"               { return symbol(sym.ALIAS); }

  // IO Functions
  "read"                { return symbol(sym.READ); }
  "print"               { return symbol(sym.PRINT); }

  // Control flow
  "if"                  { return symbol(sym.IF); }
  "elif"                { return symbol(sym.ELIF); }
  "else"                { return symbol(sym.ELSE); }
  "then"                { return symbol(sym.THEN); }
  "fi"                  { return symbol(sym.ENDIF); }
  "while"               { return symbol(sym.WHILE); }
  "do"                  { return symbol(sym.DO); }
  "od"                  { return symbol(sym.ENDDO); }
  "forall"              { return symbol(sym.FORALL); }

  // Return statement
  "return"              { return symbol(sym.RET); }

  // Main Function
  "main"                { return symbol(sym.MAIN); }

  // Other single character regexes
  ">"                   { return symbol(sym.RANGBR); }
  "("                   { return symbol(sym.LPAREN); }
  ")"                   { return symbol(sym.RPAREN); }
  "="                   { return symbol(sym.ASSIGN); }
  "["                   { return symbol(sym.LBRACK); }
  "]"                   { return symbol(sym.RBRACK); }
  "{"                   { return symbol(sym.LBRACE); }
  "}"                   { return symbol(sym.RBRACE); }
  ","                   { return symbol(sym.COMMA); }
  ";"                   { return symbol(sym.SEMICOL); }
  ":"                   { return symbol(sym.COL); }

  // Start string
  // The reason we do this is because we want to escape
  // escaped quotations
  \"                    { string.setLength(0); yybegin(STRING); }


  // Primitive data types
  {Boolean}             { return symbol(sym.BOOL_LIT); }
  {Character}           { return symbol(sym.CHAR_LIT); }
  {Integer}             { return symbol(sym.INT_LIT); }
  {Rational}            { return symbol(sym.RAT_LIT); }
  {Float}               { return symbol(sym.FLOAT_LIT); }

  // Identifier
  {Identifier}          { return symbol(sym.ID); }

}

<STRING> {
  \\\"                  { string.append('\"'); }
  [^\"]+                { string.append(yytext()); }
  \"                    { yybegin(YYINITIAL); return symbol(sym.STRING_LIT, string.toString()); }
}

[^]     { throw new Error("Lexing error at line " + yyline+1 + ", column " + yycolumn); }
