%{
    #include <stdio.h>
    #include <string.h>
    #include "y.tab.h"

    int nlin = 1;
    int ncol = 1;
%}

/* Definición de macros para patrones */
comp       (<(=|>)?|>=?)
prepost    (pre|post)
letra      [a-zA-Z]
dig        [0-9]
digs       {dig}+
int        {digs}
real       {digs}(\.{digs})?
num        {int}|{real}
id         ({letra}|_)({letra}|{dig}|_)*
enumval    {dig}({letra}|{dig}|_)*
operat     [\+\*\-/]
tipos      (Boolean|Real|Integer|String|Set|Bag|Sequence|void)
oplog      (and|or|xor)
vallog     (true|false)
cadena     ('.*')
comt       --.*

%%

"context"         { ncol += yyleng; return CONTEXT; }
"implies"         { ncol += yyleng; return IMPLIES; }
"inv"             { ncol += yyleng; return INV; }
"self"            { ncol += yyleng; return SELF; }
"@pre"            { ncol += yyleng; return ARROBAPRE; }
"not"             { ncol += yyleng; return NOT; }
"null"            { ncol += yyleng; return NULO; }
"->"              { ncol += yyleng; return OPCOL; }
"::"              { ncol += yyleng; return OPTCLASS; }

{prepost}         { ncol += yyleng; return PREPOST; }
{comp}            { ncol += yyleng; return COMP; }
{num}             { ncol += yyleng; return NUM; }
{operat}          { ncol += yyleng; return OPERAT; }
{tipos}           { ncol += yyleng; return TIPOS; }
{oplog}           { ncol += yyleng; return OPLOG; }
{vallog}          { ncol += yyleng; return VALLOG; }
{cadena}          { ncol += yyleng; return CADENA; }

{enumval}         { ncol += yyleng; strcpy(yylval.cadena, yytext); return ENUMVAL; }
{id}              { ncol += yyleng; strcpy(yylval.cadena, yytext); return ID; }

"."               { ncol += yyleng; return '.'; }
","               { ncol += yyleng; return ','; }
";"               { ncol += yyleng; return ';'; }
"("               { ncol += yyleng; return '('; }
")"               { ncol += yyleng; return ')'; }
"|"               { ncol += yyleng; return '|'; }
"="               { ncol += yyleng; return '='; }
":"               { ncol += yyleng; return ':'; }

{comt}            { ncol += yyleng; }
\n                { nlin++; ncol = 1; }
[ \t]+            { ncol += yyleng; }
"TODO:"[^\n]*     { ncol += yyleng; }
.                 { printf("Lexical error in line %d and column %d\n", nlin, ncol); ncol += yyleng; }
%%
