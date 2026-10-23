%{
    #include <stdio.h>
    #include <stdlib.h>
    #include <string.h>

    #define MAX_SYNTAX_ERROR 5
    #define MAX_OPERACIONES 100

    extern int nlin;            // contador de línea heredado del archivo de análisis léxico
    int num_sytax_error = 0;

    extern char *yytext;
    int yylex(void);
    int yyerror(const char *s);

    typedef struct {
        char nombre[100];
        int op_col;  // operaciones sobre colecciones usadas en la especificación
    } Operacion; 

    typedef struct {
        char nombre[100];
        int num_invariantes;
        int op_col_invs;         // operaciones sobre colecciones en invariantes
        Operacion *operaciones;  // operaciones asignadas dinámicamente
        int num_operaciones;
    } Clase;

    Clase **clases;  // arreglo dinámico de punteros a Clase
    int clase_actual = 0;
    int currentClaseIdx = -1;  // guarda el índice de la clase en contexto


    /* Consulta si la clase ya existe, si ya ha sido detectada: return 1,
       si no había sido detectada: return 0*/
    int existeClase(char *nombre) {
        for (int i = 0; i < clase_actual; i++) {
            if (strcmp(clases[i]->nombre, nombre) == 0) {
                return 1;  // La clase ya existe
            }
        }
        return 0;  // La clase no existe
    }


    /* Obtiene el índice de una clase, devuelve -1 si no se está */
    int getClaseIndex(char *nombre) {
        for (int i = 0; i < clase_actual; i++) {
            if (strcmp(clases[i]->nombre, nombre) == 0) {
                return i;
            }
        }
        return -1;
    }

    /* Consulta si una operación ha sido ya añadida a la clase: return 1,
       si no había sido añadida: return 0 */
    int existeOperacion(int clase_idx, char *nombre) {
        for (int i = 0; i < clases[clase_idx]->num_operaciones; i++) {
            if (strcmp(clases[clase_idx]->operaciones[i].nombre, nombre) == 0) {
                return 1; 
            }
        }
        return 0; 
    }
    

    /* Genera el array de clases al analizar un archivo OCL*/
    void inicializarClases() {
        clases = malloc(MAX_SYNTAX_ERROR * sizeof(Clase *));
        if (!clases) {
            fprintf(stderr, "Error al asignar memoria para las clases.\n");
            exit(-1);
        }
    }

    
    /* Libera la memoria pedida dinámicamente en todo el programa */
    void liberarMemoria() {
        for (int i = 0; i < clase_actual; i++) {
            free(clases[i]->operaciones);
            free(clases[i]);
        }
        free(clases);
    }
%}

/* TOKENS y atributos */
%union {
    char cadena[100];
}

/* Declaración de tokens (coincidentes con lo que retorna Flex) */
%token CONTEXT IMPLIES INV SELF ARROBAPRE NOT NULO ENUMVAL
%token OPCOL OPTCLASS PREPOST COMP OPERAT OPLOG VALLOG
%token ID NUM TIPOS CADENA
%token IF THEN ELSE

%type <cadena> ID

/* Precedencia y asociatividad */
%right IMPLIES
%left OPLOG
%left '='
%left COMP
%left OPERAT
%right NOT
%nonassoc UMINUS

/* Símbolo inicial */
%start ini

%%

/* Regla inicial: puede ser vacía o consistir en una secuencia de declaraciones */
ini:
    | declarations
;

/* Regla para una serie de declaraciones.
   Se permite una declaración o una secuencia de ellas (recursividad izquierda). */
declarations:
      declaration
    | declarations declaration
;

/* Una declaración es la declaración del contexto.
   Debe iniciar con el token CONTEXT seguido de un "contextHeader". */
declaration:
      CONTEXT contextHeader
;

/* El encabezado de contexto puede ser para declarar una clase con sus invariantes,
   o para declarar una operación (con sus pre/post). */
contextHeader:
      className constraintClauseClass
    | operationDecl constraintClauseOp
;

/* Regla que identifica el nombre de la clase.
   Se verifica si la clase ya existe y, si no, se crea e inicializa.
   Se actualiza currentClaseIdx para usar en las cláusulas siguientes.
*/
className:
      ID {
          int idx = getClaseIndex($1);
          if (idx == -1) {
              // La clase no existe: se reserva memoria y se la inicializa.
              clases[clase_actual] = malloc(sizeof(Clase));
              if (!clases[clase_actual]) {
                  fprintf(stderr, "Error al asignar memoria para la clase.\n");
                  exit(-1);
              }
              strcpy(clases[clase_actual]->nombre, $1);
              clases[clase_actual]->num_invariantes = 0;
              clases[clase_actual]->num_operaciones = 0;
              clases[clase_actual]->op_col_invs = 0;
              // Se reserva el array dinámico para las operaciones de la clase.
              clases[clase_actual]->operaciones = malloc(MAX_OPERACIONES * sizeof(Operacion));
              if (!clases[clase_actual]->operaciones) {
                  fprintf(stderr, "Error al asignar memoria para las operaciones de la clase.\n");
                  exit(-1);
              }
              idx = clase_actual;
              clase_actual++;
          }
          currentClaseIdx = idx;  // Se actualiza el contexto con el índice de la clase actual.
      }
;

/* Regla para declarar una operación perteneciente a una clase.
   La estructura es: [nombre de la clase] OPTCLASS [nombre de la operación] (...) ':' [tipo] */
operationDecl:
      ID OPTCLASS ID '(' paramListOpt ')' ':' typeSpec {
          int clase_idx = getClaseIndex($1);
          if (clase_idx == -1) {
              // Si la clase no existe, se muestra un mensaje de error.
              fprintf(stderr, "Error: La clase '%s' no está definida.\n", $1);
              currentClaseIdx = -1;
          } else {
              currentClaseIdx = clase_idx;  // Se actualiza el contexto a la clase encontrada.
              // Si la operación no existe aun en la clase, se añade
              if (!existeOperacion(clase_idx, $3)) {
                  int operacion_idx = clases[clase_idx]->num_operaciones;
                  strcpy(clases[clase_idx]->operaciones[operacion_idx].nombre, $3);
                  clases[clase_idx]->operaciones[operacion_idx].op_col = 0;
                  clases[clase_idx]->num_operaciones++;
              }
          }
      }
;

/* Lista opcional de parámetros: puede estar vacía o contener una lista */
paramListOpt:
    | paramList
;

/* Regla de lista de parámetros: un parámetro, o separados por comas */
paramList:
      param
    | param ',' paramList
;

/* Un parámetro tiene la forma: ID ':' typeSpec */
param:
      ID ':' typeSpec
;

/* La especificación del tipo puede ser el token TIPOS o un identificador */
typeSpec:
      TIPOS
    | ID
;

/* Regla para la cláusula de invariantes en una clase.
   Cada invariant se define con "INV ':' expr" y puede repetirse.
   Se incrementa el contador de invariantes de la clase actual.
*/
constraintClauseClass:
    | INV ':' expr constraintClauseClass {
          if (currentClaseIdx >= 0)
              clases[currentClaseIdx]->num_invariantes++;
      }
;

/* Regla para la cláusula de pre/post condiciones en una operación.
   No se realizan acciones adicionales aquí, pero se permite la recursividad. */
constraintClauseOp:
    | PREPOST ':' expr constraintClauseOp
;

/* Regla que define una expresión (a nivel de implicación) */
expr:
      impliesExpr
;

/* Una expresión de implicación es:
   - una expresión OR, o
   - una expresión OR seguida del operador IMPLIES y otra expresión de implicación */
impliesExpr:
      orExpr
    | orExpr IMPLIES impliesExpr
;

/* Una expresión OR es:
   - una expresión de igualdad, o
   - una expresión OR seguida del operador (lógico) OPLOG y una expresión de igualdad */
orExpr:
      equalityExpr
    | orExpr OPLOG equalityExpr
;

/* Una expresión de igualdad es:
   - una expresión relacional, o
   - dos expresiones relacionales separadas por '=' */
equalityExpr:
      relationalExpr
    | relationalExpr '=' relationalExpr
;

/* Una expresión relacional es:
   - una expresión aditiva, o
   - dos expresiones aditivas separadas por un operador comparativo (COMP) */
relationalExpr:
      additiveExpr
    | additiveExpr COMP additiveExpr
;

/* Una expresión aditiva es:
   - una expresión unaria, o
   - una expresión aditiva seguida de un operador (OPERAT) y una expresión unaria */
additiveExpr:
      unaryExpr
    | additiveExpr OPERAT unaryExpr
;

/* Una expresión unaria puede ser:
   - el operador NOT aplicado a otra expresión unaria,
   - un operador unario '-' (definido con precedencia UMINUS),
   - o una expresión postfija */
unaryExpr:
      NOT unaryExpr
    | '-' unaryExpr %prec UMINUS
    | postfixExpr
;

/* Una expresión postfija consiste en una expresión primaria seguida de "postfixTail" */
postfixExpr:
      primary postfixTail
;

/* La "postfixTail" permite encadenar sufijos o llamadas implícitas.
   Se puede dejar vacía o seguir con un sufijo o una llamada implícita, recursivamente. */
postfixTail:
    | suffix postfixTail
    | implicitMethodCall postfixTail
;

/* Regla para el sufijo de una expresión, que puede ser:
   - una llamada de método con punto, o
   - una invocación de operación sobre colecciones (usando OPCOL) */
suffix:
      '.' ID methodCallOpt
    | OPCOL ID '(' argumentListOpt ')' {
          if (currentClaseIdx >= 0) {
              if (clases[currentClaseIdx]->num_operaciones > 0)
                  clases[currentClaseIdx]->operaciones[clases[currentClaseIdx]->num_operaciones - 1].op_col++;
              else
                  clases[currentClaseIdx]->op_col_invs++;
          }
      }
;

/* Regla opcional para llamadas de método.
   Puede estar vacía o consistir en paréntesis que encapsulan una lista de argumentos. */
methodCallOpt:
    | '(' argumentListOpt ')' 
;

/* Regla para la llamada implícita; se utiliza cuando se invoca un método sin el operador punto */
implicitMethodCall:
      ID '(' argumentListOpt ')' {
          if (currentClaseIdx >= 0) {
              if (clases[currentClaseIdx]->num_operaciones > 0)
                  clases[currentClaseIdx]->operaciones[clases[currentClaseIdx]->num_operaciones - 1].op_col++;
              else
                  clases[currentClaseIdx]->op_col_invs++;
          }
      }
;

/* Regla para una expresión primaria, que abarca:
   - literales numéricos, booleanos, nulos, cadenas,
   - identificadores, valores de enumeración,
   - o agrupaciones de expresiones entre paréntesis.
   También se permiten dos formas donde se emplea OPTCLASS, 
   que pueden usarse para desambiguar contextos.
*/
primary:
      NUM
    | VALLOG
    | NULO
    | CADENA
    | SELF
    | ID
    | ENUMVAL
    | ID OPTCLASS ENUMVAL
    | ID OPTCLASS ID
    | '(' expr ')'
;

/* Regla para una lista opcional de argumentos:
   Puede estar vacía o contener una lista de argumentos. */
argumentListOpt:
    | argumentList
;

/* Regla para una lista de argumentos, que son expresiones separadas por comas */
argumentList:
      expr
    | expr ',' argumentList
;

%%
int yyerror(const char *s) {
    fprintf(stderr, "Syntax error: %s at symbol '%s' in line %d.\n", s, yytext, nlin);
    num_sytax_error++;
    if (num_sytax_error >= MAX_SYNTAX_ERROR)
        exit(-1);
    return 0;
}


int main(void) {
    inicializarClases();
    yyparse();

    if (num_sytax_error == 0) {
        for (int i = 0; i < clase_actual; i++) {
            printf("%s:\n", clases[i]->nombre);

            // Imprime cantidad de invariantes
            if (clases[i]->num_invariantes == 0)
                printf("cantidad de invariantes de la clase: ninguna\n");
            else
                printf("cantidad de invariantes de la clase: %d\n", clases[i]->num_invariantes);

            // Imprime cantidad de operaciones sobre colecciones utilizadas en los invariantes
            if (clases[i]->op_col_invs == 0)
                printf("cantidad de operaciones sobre colecciones utilizadas en los invariantes: ninguna\n");
            else
                printf("cantidad de operaciones sobre colecciones utilizadas en los invariantes: %d\n", clases[i]->op_col_invs);
            
            // Imprime operaciones de la clase especificadas
            printf("Operaciones de la clase especificadas: ");
            if (clases[i]->num_operaciones == 0) {
                printf("ninguna\n");
            } else {
                for (int j = 0; j < clases[i]->num_operaciones; j++) {
                    printf("nombre de la operación: %s\n", clases[i]->operaciones[j].nombre);
                    if (clases[i]->operaciones[j].op_col == 0)
                        printf("cantidad de operaciones sobre colecciones utilizadas en la especificación: ninguna\n");
                    else
                        printf("cantidad de operaciones sobre colecciones utilizadas en la especificación: %d\n",
                               clases[i]->operaciones[j].op_col);
                }
            }

            // Separador entre clases, para mejor lectura
            printf("------------------------------------------------\n");
        }
    }
    liberarMemoria();
    return 0;
}
