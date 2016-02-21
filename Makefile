LANG = Main
VERJFLEX = 1.6.0
VERCUP = 11b

all: bin/$(LANG).class

bin/$(LANG).class: src/Lexer.lex src/Parser.cup src/$(LANG).java
	mkdir -p bin
	java -jar lib/jflex-$(VERJFLEX).jar -d src/ src/Lexer.lex
	java -jar lib/java-cup-$(VERCUP).jar -destdir src/ -parser Parser src/Parser.cup
	javac -cp lib/java-cup-$(VERCUP)-runtime.jar -sourcepath src/ -d bin/ src/$(LANG).java

clean:
	rm -rf src/Lexer.java src/Lexer.java~ src/sym.java src/Parser.java bin/*.class
