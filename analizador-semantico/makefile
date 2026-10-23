# === Configuración de directorios ===
SRC_DIR = src
BIN_DIR = $(SRC_DIR)/bin
INPUTS = inputs
GRAMMAR = SimpleClassModelWithConstraints.g4
ANTLR_JAR = lib/antlr-4.13.2-complete.jar
GENERATED = SimpleClassModelWithConstraints*.java
MODEL = ModeloGenerado.java

# === Clase principal ===
MAIN_CLASS = Main

# === Archivos fuente propios y generados ===
JAVA_SOURCES = $(wildcard $(SRC_DIR)/*.java)

# === Reglas ===
.PHONY: all clean dirs antlr compile compile-model

# Regla principal
all: clean dirs antlr compile 

# Limpiar todo lo generado
clean:
	rm -rf $(BIN_DIR)
	rm -rf $(SRC_DIR)/$(GENERATED)
	rm -rf $(SRC_DIR)/$(MODEL)
	rm -f $(SRC_DIR)/*.tokens $(SRC_DIR)/*.interp

# Crear directorios necesarios
dirs:
	mkdir -p $(BIN_DIR)

# Generar código ANTLR directamente en src/
antlr:
	java -jar "$(ANTLR_JAR)" -visitor -listener -Dlanguage=Java -o . $(SRC_DIR)/$(GRAMMAR)

# Compilar todos los .java en src/
compile:
	javac -cp "$(ANTLR_JAR)" -d $(BIN_DIR) $(JAVA_SOURCES)

compile-model:
	javac -cp "$(ANTLR_JAR)" -d $(BIN_DIR) $(SRC_DIR)/$(MODEL)
