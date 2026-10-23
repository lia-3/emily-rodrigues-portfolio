grammar SimpleClassModelWithConstraints;

model: 
	classModelSpecification multipleContextSpecifications
	;

classModelSpecification
    : (classifier | association)*
    ;

classifier
    : classDefinition
    | datatypeDefinition
    | enumeration
    ;

classDefinition
    : 'class' identifier ('extends' identifier)? ('implements' idList)? '{' classBody? '}'
    ;

classBody
    : classBodyElement+
    ;

classBodyElement
    : attributeDefinition
    | operationDefinition
    ;

attributeDefinition
    : 'attribute' identifier ('identity' | 'derived')? ':' type ';'
    | 'static' 'attribute' identifier ':' type ';'
    ;

operationDefinition
    : ('static')? 'operation' identifier '(' parameterDeclarations? ')' ':' type ';'
    ;

parameterDeclarations
    : (parameterDeclaration ',')* parameterDeclaration
    ;

parameterDeclaration
    : identifier ':' type
    ;

idList
    : (identifier ',')* identifier
    ;

datatypeDefinition
    : 'datatype' identifier '=' '{' datatypeBodyElement* '}' 
    ; 

datatypeBodyElement
    : 'field' identifier ':' type ';'
    | operationDefinition
    ;

enumeration
    : 'enumeration' identifier '{' enumerationLiteral+ '}' 
    ;

enumerationLiteral
    : 'literal' identifier ';'
    ;

association
    : 'association' '{' associationName?  associationEndA=associationEnd   associationEndB=associationEnd  associationClass? '}'
    ;

associationClass
    : 'with association class' '{' classBody '}'
    ;

associationName
    : 'name' '=' identifier
    ;

associationEnd
    : 'target' '=' identifier multiplicity? ('<>'|'<<>>')? (('+'|'-')identifier)? constraints?
    ;

multiplicity
    : '*'
    | '0..*'
    | '1..*'
    | INT
    | INT '..' INT
    | INT '..' '*'
    ;

constraints
    : '{' ('unique' | 'ordered')+ '}'
    ;
 
identifier
    : ID
    ;

type
    : primitiveType
    | identifier
    ;

primitiveType
    : 'Boolean'
    | 'Integer'
    | 'Real'
    | 'String'
;

multipleContextSpecifications
    : (invariantContext | prepostContext)+
    ;

invariantContext
    : 'context' ID (invariant)+
    ;

invariant
    : 'inv' ID? ':' expression
    ;

prepostContext
    : 'context' ID '::' operationDeclaration prepostSpecification
    ;

operationDeclaration
    :  identifier '(' parameterDeclarations? ')' ':' ocltype 
    ;

prepostSpecification
    : precondition* postcondition* 
    ;

precondition
    : 'pre:' expression 
    ;

postcondition
    : 'post:' expression 
    ;

ocltype
    : 'Sequence' '(' ocltype ')'
    | 'Set' '(' ocltype ')'
    | 'Bag' '(' ocltype ')'
    | 'OrderedSet' '(' ocltype ')'
    | 'Ref' '(' ocltype ')'  
    | 'Map' '(' ocltype ',' ocltype ')'
    | 'Function' '(' ocltype ',' ocltype ')'
    | type
    ;

expressionList
    : (expression ',')* expression
    ;

expression
    : logicalExpression
    | conditionalExpression
    | lambdaExpression
    | letExpression
    ;

// Basic expressions can appear on the LHS of . or ->

basicExpression
    : 'null'
    | basicExpression '.' ID ( '(' expressionList? ')' )?
    | basicExpression '(' expressionList? ')'
    | basicExpression '[' expression ']'
    | ID '@pre'
    | INT
    | FLOAT_LITERAL
    | STRING1_LITERAL
    | STRING2_LITERAL
    | ENUMERATION_LITERAL
    | ID
    | '(' expression ')'
    ;

conditionalExpression
    : 'if' expression 'then' expression 'else' expression 'endif'
    ;

lambdaExpression
    : 'lambda' identifier ':' ocltype 'in' expression
    ;

// A let is just an application of a lambda:

letExpression
    : 'let' ID ':' ocltype '=' expression 'in' expression
    ;

logicalExpression
    : 'not' logicalExpression
    | logicalExpression 'and' logicalExpression
    | logicalExpression '&' logicalExpression
    | logicalExpression 'or' logicalExpression
    | logicalExpression 'xor' logicalExpression
    | logicalExpression '=>' logicalExpression
    | logicalExpression 'implies' logicalExpression
    | equalityExpression
    ;

equalityExpression
    : additiveExpression ('=' | '<' | '>' | '>=' | '<=' | '/=' | '<>' | ':' | '/:' | '<:') additiveExpression
    | additiveExpression
    ;

additiveExpression
    : additiveExpression ('+' | '-') additiveExpression
    | additiveExpression ('..' | '|->' | '.') additiveExpression
    | factorExpression
    ;

factorExpression
    : factor2Expression ('*' | '/' | 'mod' | 'div') factorExpression
    | factor2Expression
    ;

// factor2Expressions can appear on LHS of ->
// ->subrange is used for ->substring and ->subSequence

factor2Expression
    : ('-' | '+' | '?' | '!') factor2Expression
    | factor2Expression '->size()'
    | factor2Expression '->copy()'
    | factor2Expression (
        '->isEmpty()'
        | '->notEmpty()'
        | '->asSet()'
        | '->asBag()'
        | '->asOrderedSet()'
        | '->asSequence()'
        | '->sort()'
    )
    | factor2Expression '->any()'
    | factor2Expression '->log()'
    | factor2Expression '->exp()'
    | factor2Expression '->sin()'
    | factor2Expression '->cos()'
    | factor2Expression '->tan()'
    | factor2Expression '->asin()'
    | factor2Expression '->acos()'
    | factor2Expression '->atan()'
    | factor2Expression '->log10()'
    | factor2Expression '->first()'
    | factor2Expression '->last()'
    | factor2Expression '->front()'
    | factor2Expression '->tail()'
    | factor2Expression '->reverse()'
    | factor2Expression '->tanh()'
    | factor2Expression '->sinh()'
    | factor2Expression '->cosh()'
    | factor2Expression '->floor()'
    | factor2Expression '->ceil()'
    | factor2Expression '->round()'
    | factor2Expression '->abs()'
    | factor2Expression '->oclType()'
    | factor2Expression '->allInstances()'
    | factor2Expression '->oclIsUndefined()'
    | factor2Expression '->oclIsInvalid()'
    | factor2Expression '->oclIsNew()'
    | factor2Expression '->sum()'
    | factor2Expression '->prd()'
    | factor2Expression '->max()'
    | factor2Expression '->min()'
    | factor2Expression '->sqrt()'
    | factor2Expression '->cbrt()'
    | factor2Expression '->sqr()'
    | factor2Expression '->characters()'
    | factor2Expression '->toInteger()'
    | factor2Expression '->toReal()'
    | factor2Expression '->toBoolean()'
    | factor2Expression '->display()' 
    | factor2Expression '->toUpperCase()'
    | factor2Expression '->toLowerCase()'
    | factor2Expression ('->unionAll()' | '->intersectAll()' | '->concatenateAll()')
    | factor2Expression ('->pow' | '->gcd') '(' expression ')'
    | factor2Expression (
        '->at'
        | '->union'
        | '->intersection'
        | '->includes'
        | '->excludes'
        | '->including'
        | '->excluding'
        | '->includesAll'
        | '->symmetricDifference'
        | '->excludesAll'
        | '->prepend'
        | '->append'
        | '->count'
        | '->apply'
    ) '(' expression ')'
    | factor2Expression (
        '->hasMatch'
        | '->isMatch'
        | '->firstMatch'
        | '->indexOf'
        | '->lastIndexOf'
        | '->split'
        | '->hasPrefix'
        | '->hasSuffix'
        | '->equalsIgnoreCase'
    ) '(' expression ')'
    | factor2Expression ('->oclAsType' | '->oclIsTypeOf' | '->oclIsKindOf' | '->oclAsSet') '(' expression ')'
    | factor2Expression '->collect' '(' identOptType '|' expression ')'
    | factor2Expression '->select' '(' identOptType '|' expression ')'
    | factor2Expression '->reject' '(' identOptType '|' expression ')'
    | factor2Expression '->forAll' '(' identOptType '|' expression ')'
    | factor2Expression '->exists' '(' identOptType '|' expression ')'
    | factor2Expression '->exists1' '(' identOptType '|' expression ')'
    | factor2Expression '->one' '(' identOptType '|' expression ')'
    | factor2Expression '->any' '(' identOptType '|' expression ')'
    | factor2Expression '->closure' '(' identOptType '|' expression ')'
    | factor2Expression '->sortedBy' '(' identOptType '|' expression ')'    
    | factor2Expression '->sortedBy' '(' identifier ')'
    | factor2Expression '->isUnique' '(' identOptType '|' expression ')'
    | factor2Expression '->subrange' '(' expression ',' expression ')'
    | factor2Expression '->replace' '(' expression ',' expression ')'
    | factor2Expression '->replaceAll' '(' expression ',' expression ')'
    | factor2Expression '->replaceAllMatches' '(' expression ',' expression ')'
    | factor2Expression '->replaceFirstMatch' '(' expression ',' expression ')'
    | factor2Expression '->insertAt' '(' expression ',' expression ')'
    | factor2Expression '->insertInto' '(' expression ',' expression ')'
    | factor2Expression '->setAt' '(' expression ',' expression ')'
    | factor2Expression '->iterate' '(' identifier ';' identifier '=' expression '|' expression ')'
    | setExpression
    | basicExpression
    ;

identOptType
    : ID (':' ocltype)?
    ;    

setExpression
    : 'OrderedSet{' expressionList? '}'
    | 'Bag{' expressionList? '}'
    | 'Set{' expressionList? '}'
    | 'Sequence{' expressionList? '}'
    | 'Map{' expressionList? '}'
    ;

qualified_name
    : ENUMERATION_LITERAL
    ;

INT
    : [0-9]+
    ;

FLOAT_LITERAL
    : [0-9]+ '.' [0-9]+
    ;

STRING1_LITERAL
    : '"' (~["\\\r\n])* '"'
    ;

STRING2_LITERAL
    : '\'' (~['\\\r\n])* '\'';

ENUMERATION_LITERAL
    : ID '::' ID
    ;

NULL_LITERAL
    : 'null'
    ;

ID
    : [a-zA-Z_$]+ [a-zA-Z0-9_$]*
    ; // match identifiers


MULTILINE_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

NEWLINE
    : [\r\n]+ -> skip
    ;

WS
    : [ \t\n\r]+ -> skip
    ;


