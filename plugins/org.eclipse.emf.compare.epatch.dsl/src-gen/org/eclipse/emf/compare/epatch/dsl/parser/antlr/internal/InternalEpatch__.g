lexer grammar InternalEpatch;
@header {
package org.eclipse.emf.compare.epatch.dsl.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

T12 : 'epatch' ;
T13 : '{' ;
T14 : '}' ;
T15 : 'import' ;
T16 : 'uri' ;
T17 : 'ns' ;
T18 : 'resource' ;
T19 : 'left' ;
T20 : ';' ;
T21 : 'right' ;
T22 : 'object' ;
T23 : '=' ;
T24 : '|' ;
T25 : '[' ;
T26 : ',' ;
T27 : ']' ;
T28 : '.' ;
T29 : ':' ;
T30 : 'null' ;
T31 : 'new' ;
T32 : 'copy' ;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2048
RULE_FRAGMENT : '#' ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'/'|'['|']'|'{'|'}'|'.'|'@'|'%'|':')+;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2050
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2052
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2054
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2056
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2058
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2060
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2062
RULE_ANY_OTHER : .;


