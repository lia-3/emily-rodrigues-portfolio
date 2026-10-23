# Makefile para analizador léxico y sintáctico

# Nombre de los archivos
LEX = lexico.l
YACC = sintactico.y
OUT = parser

# Reglas
all: $(OUT)

$(OUT): y.tab.c lex.yy.c
	cc y.tab.c lex.yy.c -ll -o $(OUT)

y.tab.c y.tab.h: $(YACC)
	yacc -d $(YACC)

lex.yy.c: $(LEX)
	lex $(LEX)

clean:
	rm -f y.tab.c y.tab.h lex.yy.c $(OUT)

.PHONY: all clean
