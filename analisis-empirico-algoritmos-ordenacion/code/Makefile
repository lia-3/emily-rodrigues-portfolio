# Compilador y opciones
JAVAC = javac
JAVA = java
JAVAC_FLAGS = -d bin -sourcepath src
JAVA_FLAGS = -Xint -Xss16m   

# Directorios
SRC_DIR = src
BIN_DIR = bin
MAIN_CLASS = Main

# Encontrar todos los archivos .java
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Regla principal
all: compile run

# Compilar
compile:
	@echo "Compilando..."
	@mkdir -p $(BIN_DIR)
	$(JAVAC) $(JAVAC_FLAGS) $(SOURCES)
	@echo "Compilación completada en $(BIN_DIR)/"

# Ejecutar con JIT desactivado
run:
	@echo "Ejecutando con JIT desactivado (-Xint, -Xss16m)..."
	cd $(BIN_DIR) && $(JAVA) $(JAVA_FLAGS) $(MAIN_CLASS)

# Limpiar archivos compilados
clean:
	@echo "Limpiando..."
	rm -rf $(BIN_DIR)/*
	@echo "Limpieza completada"

# Ayuda
help:
	@echo "Makefile para proyecto Java"
	@echo "Comandos disponibles:"
	@echo "  make compile    - Compilar el proyecto"
	@echo "  make run        - Ejecutar con JIT desactivado y stack aumentado"
	@echo "  make all        - Compilar y ejecutar"
	@echo "  make clean      - Limpiar archivos compilados"
	@echo "  make help       - Mostrar esta ayuda"

.PHONY: all compile run clean help
