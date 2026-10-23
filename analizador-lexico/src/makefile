# Makefile para el proyecto de analizador léxico

# Definir el compilador de Lex y C
LEX = lex
CC = cc

# Archivos fuente y ejecutable
SRC = analizador.l
OBJ = lex.yy.c
EXEC = analizador

# Flags de compilación
CFLAGS = -lfl

# Regla por defecto
all: $(EXEC)

# Regla para generar el código C a partir del archivo Lex
$(OBJ): $(SRC)
	$(LEX) $(SRC)

# Regla para compilar el código C generado por Lex y crear el ejecutable
$(EXEC): $(OBJ)
	$(CC) $(OBJ) -o $(EXEC) $(CFLAGS)

# Regla para limpiar archivos generados
clean:
	rm -f $(OBJ) $(EXEC)

.PHONY: all clean run
