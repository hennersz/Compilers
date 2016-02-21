import java_cup.runtime.*;

%%
%class Lexer
%unicode
%cup
%line
%column

%eofval {
    return sym(sym.EOF);
%eofval }

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    print_lexeme(type, null);
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    print_lexeme(type, value);
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
Punctuation = [ \!#$%&()\*\+,-\.\/:;<=>\?@[\\\]\^_`{¦}\~\'\"] // 'Have we escaped everything?

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
  "bool"                {  }
  "int"                 {  }
  "rat"                 {  }
  "float"               {  }
  "char"                {  }

  // Bool operators
  "!"                   {  }
  "&&"                  {  }
  "||"                  {  }

  // Arithmetic operators
  "+"                   {  }
  "-"                   {  }
  "*"                   {  }
  "/"                   {  }
  "^"                   {  }

  // Comparisons (Does it include greater than?)
  "<"                   {  }
  "<="                  {  }
  "=="                  {  }
  "!="                  {  }

  // Aggregate keywords
  "dict"                {  }
  "seq"                 {  }
  "top"                 {  }

  // Operations on Aggregate
  "::"                  {  }
  "len"                 {  }
  "in"                  {  }

  // Declarations
  "tdef"                {  }
  "fdef"                {  }
  "alias"               {  }

  // IO Functions
  "read"                {  }
  "print"               {  }

  // Control flow
  "if"                  {  }
  "elif"                {  }
  "else"                {  }
  "then"                {  }
  "fi"                  {  }
  "while"               {  }
  "do"                  {  }
  "od"                  {  }
  "forall"              {  }

  // Return statement
  "return"              {  }

  // Main Function
  "main"                {  }

  // Other single character regexes
  "<"                   {  }
  ">"                   {  }
  "("                   {  }
  ")"                   {  }
  "="                   {  }
  "["                   {  }
  "]"                   {  }
  "{"                   {  }
  "}"                   {  }
  ","                   {  }
  ";"                   {  }
  ":"                   {  }
  "::"                  {  }

  // Start string
  // The reason we do this is because we want to escape
  // escaped quotations
  \"                    { string.setLength(0); yybegin(STRING); }


  // Primitive data types
  {Boolean}             {  }
  {Character}           {  }
  {Integer}             {  }
  {Rational}            {  }
  {Float}               {  }

  // Identifier
  {Identifier}          {  }

}

<STRING> {
  \\\"                  { string.append('\"'); }
  [^\"]+                { string.append(yytext()); }
  \"                    { yybegin(YYINITIAL); return symbol(sym.STRING_LITERAL, string.toString()); }
}

[^]     { throw new Error("Lexing error at line " + yyline+1 + ", column " + yycolumn); }
