%{
    #include <stdio.h>
    #include <stdlib.h>
    #include <string.h>
    #define TAMSTRING 40

    /*Declaracion implicita de las clases para que no salte warnin*/
    int encontrarClase(char* clase);
    int agrandarArray();

    typedef struct {
        char id[TAMSTRING];
        int numInv;
        int numOp;
    } ClaseStruct;

    int nlin = 1;
    int ncol = 1;
    int opcolecciones = 0; /*Num de operaciones sobre colecciones encontradas*/

    /*Variables usadas para manejo del array.*/
    ClaseStruct *arrayClass;            /*Puntero que siempre apunta al inicio del array.*/
    int tamArray = 10;                  /*Tamaño total reservado en memoria.*/
    int finArray = 0;                   /*Cantidad de clases guardadas en el array.*/
    int i;                              /*Indice con el que se va a manipular el array*/
    char classActual[TAMSTRING];        /*String q sirve para identificar en que contexto de clase se encuentran
                                         *los invariantes u operaciones.*/
%}
%s Coleccion Clase Operacion
res     (inv|self|pre|post|@pre)
contx   context
inv     inv:
opcol   ->
comp    (<(=|>)?|>=?|=)
seplog  \|
letra   [a-zA-Z]
dig     [0-9]
digs    {dig}+
int     {digs}
real    {digs}(\.{digs})?
id      {letra}({letra}|{digs}|_)*
operat  [\+\*\-/]
colec   (Set|Bag|Sequence)
tipos   (Boolean|Real|Integer|String)
oplog   (not|and|or|xor)
vallog  (true|false)
cond    (implies|if|else|then|endif)
paren   [\(\)]
llave   [\{\}]
dospuntos \:
punto     \.
coma    \,
optclass \:\:
cadena  ('{id}'|'')
comt    --.*
%%
{opcol}    {ncol+= yyleng; printf ("Palabra reservada: %s\n", yytext); BEGIN Coleccion;}
<Coleccion>{id} {
            ncol+= yyleng;
            opcolecciones ++;
            BEGIN INITIAL;
}
{contx}     {ncol+= yyleng; printf("Palabra reservada: %s\n", yytext); BEGIN Clase;}
<Clase>{id} {
            printf("Clase: %s\n", yytext);
            ncol+= yyleng;
            strcpy(classActual, yytext); /*Se guarda en que contexto de clase estamos para inv y optclass*/

            /*Se mira a ver si la clase ya esta guardada, si no esta guardada se mete en el array.*/
            if(encontrarClase(yytext) == -1){ 

                 /*Guarda la clase al final de array*/
                strcpy(arrayClass[finArray].id, yytext);
                arrayClass[finArray].numInv = 0;
                arrayClass[finArray].numOp = 0;

                /*Si el array esta lleno se duplica su tamano.*/
                finArray ++;
                if(finArray == tamArray){
                    agrandarArray();
                }
            }

            BEGIN INITIAL;
}
{optclass}  { ncol += yyleng; BEGIN Operacion;}
<Operacion>{id} {
            ncol+= yyleng;

            /*Busca la clase en el array y suma 1 a las operaciones especificadas.*/
            i = encontrarClase(classActual);
            arrayClass[i].numOp++;

            BEGIN INITIAL;
}
{inv}       {
            /*Busca la clase en el array y suma 1 a los invariantes establecidos,
             *despues hace reject y el inv se ejecuta como palabra reservada.*/
            i = encontrarClase(classActual);
            arrayClass[i].numInv++;
            REJECT;
            }
{comp}      {printf("Comparador: %s\n", yytext);            ncol+= yyleng;}
{seplog}    {printf("Separador lógico: %s\n", yytext);      ncol+= yyleng;}
{int}       {printf("Entero: %s\n", yytext);                ncol+= yyleng;}
{real}      {printf("Real: %s\n", yytext);                  ncol+= yyleng;}
{vallog}    {printf("Valor lógico: %s\n", yytext);          ncol+= yyleng;}
{operat}    {printf("Operador aritmético: %s\n", yytext);   ncol+= yyleng;}
{res}       {printf("Palabra reservada: %s\n", yytext);     ncol+= yyleng;}
{tipos}     {printf("Tipos: %s\n", yytext);                 ncol+= yyleng;}
{colec}     {printf("Colección: %s\n", yytext);             ncol+= yyleng;}
{oplog}     {printf("Operador lógico: %s\n", yytext);       ncol+= yyleng;}
{paren}     {printf("Paréntesis: %s\n", yytext);            ncol+= yyleng;}
{llave}     {printf("Llave: %s\n", yytext);                 ncol+= yyleng;}
{dospuntos} {printf("Dos puntos: %s\n", yytext);            ncol+= yyleng;}
{punto}     {printf("Punto: %s\n", yytext);                 ncol+= yyleng;}
{cond}      {printf("Condicional: %s\n", yytext);           ncol+= yyleng;}
{id}        {printf("Identificador: %s\n", yytext);         ncol+= yyleng;}
{cadena}    {printf("Cadena de texto: %s\n", yytext);       ncol+= yyleng;}
{coma}      {printf("Coma: %s\n", yytext);                  ncol+= yyleng;}
{comt}      ncol+= yyleng;
\n          { nlin ++; ncol = 1;}
[ \t]+      ncol+= yyleng;
.           {printf("Error en línea %d y columna %d\n", nlin, ncol);  ncol+= yyleng;}
%%

/*Busca en el array una clase mandada como parametro.
 *Devuelve la posicion en la que esta la clase. Si devuelve -1 es porque no esta la clase.*/
int encontrarClase(char* clase) {
    for (i = 0; i < finArray; i++) {
        if (strcmp(clase, arrayClass[i].id) == 0) {
            /*Encontro la clase*/
            return i;
        }
    }
    /*No encontro la clase*/
    return -1;
}


/*Duplica el tamano del array*/
int agrandarArray() {
    tamArray *= 2;
    
    if ((arrayClass = (ClaseStruct *) realloc(arrayClass, (tamArray) * sizeof(ClaseStruct))) == NULL) {
        printf("Error al reservar memoria duplicando tamano del array\n");
        return 1;
    }
    return 0;
}

int main() {

    /*Reserva inicial de memora*/
    if((arrayClass = (ClaseStruct *) malloc(tamArray* sizeof(ClaseStruct))) == NULL){
        printf("Error al reservar memoria creando array de clases\n");
        return 1;
    }

    yylex();

    /*Se muestan el resumen del analisis*/
    printf("\n===== RESUMEN =====\n");
    printf("Numero de clases analizadas: %d\n", finArray);
    for(i = 0; i < finArray; i++){
        printf("Para la clase %s se han establecido %d invariantes y se han especificado %d operaciones\n", arrayClass[i].id, arrayClass[i].numInv, arrayClass[i].numOp);
    }
    printf("Numero de operaciones sobre colecciones: %d\n", opcolecciones);

    free(arrayClass);
    return 0;
}