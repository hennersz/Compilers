# Compilers

Build the source:
 `make`
Run the program to show lexer output:

  `java -cp bin:lib/java-cup-11b-runtime.jar Main show-lexing input_file`

If you have Graphviz installed, you can generate a picture of the parser
output:

  `java -cp bin:lib/java-cup-11b-runtime.jar Main show-parsing input_file | dot -Tpng > parser.png`
