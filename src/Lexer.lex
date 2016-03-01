import java_cup.runtime.*;

%%
%class Lexer
%unicode
%cup
%line
%column

%eofval{
  return symbol(sym.EOF);
%eofval}

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

%}

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
Punctuation = [\!#$%&\(\)\*\+,-\.\/:;<=>\?@\[\\\]\^_`\{\|\}\~ \" \']
Escaped = \\b|\\n|\\t|\\f|\\r

Identifier = {Letter}("_"|{Letter}|{Digit})*
Character = \'({Letter}|{Digit}|{Punctuation}|{Escaped})\'
Boolean = ("T"|"F")
// We don't want negatives here, parser takes care of that
Integer = (0|[1-9][0-9]*)
// Rational can be an integer + What is the underscore?
Rational = ({Integer}"_")?{Integer}"/"{Integer}
Float = (0|[1-9][0-9]*)"."[0-9]+

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
  "void"                { return symbol(sym.VOID); }

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
  "<="                  { return symbol(sym.LTHANEQ); }
  ">="                  { return symbol(sym.GTHANEQ); }
  "=="                  { return symbol(sym.EQUALS); }
  "!="                  { return symbol(sym.NEQUALS); }

  // Aggregate keywords
  "dict"                { return symbol(sym.DICT); }
  "seq"                 { return symbol(sym.SEQ); }
  "top"                 { return symbol(sym.TOP); }
  "string"              { return symbol(sym.STR); }

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
  "return"              { return symbol(sym.RETURN); }

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
  "."                   { return symbol(sym.DOT); }

  // Start string
  // The reason we do this is because we want to escape
  // escaped quotations
  "\""                    { string.setLength(0); yybegin(STRING); }

  // Primitive data types
  {Boolean}             { return symbol(sym.BOOLLIT); }
  {Character}           { return symbol(sym.CHARLIT); }
  {Integer}             { return symbol(sym.INTLIT); }
  {Rational}            { return symbol(sym.RATLIT); }
  {Float}               { return symbol(sym.FLOATLIT); }

  // Identifier
  {Identifier}          { return symbol(sym.ID); }

}

<STRING> {
  \\\"                    { string.append('\"'); }
  [^"\"""\\\""]+          { string.append(yytext()); }
  \\t                     { string.append('\t'); }
  \\n                     { string.append('\n'); }
  \\r                     { string.append('\r'); }
  \\f                     { string.append('\f'); }
  \\b                     { string.append('\b'); }
  \\'                     { string.append('\''); }
  \"                      { yybegin(YYINITIAL); return symbol(sym.STRLIT, string.toString()); }
}

[^]     { throw new Error("Lexing error at line " + yyline+1 + ", column " + yycolumn + " "); }
