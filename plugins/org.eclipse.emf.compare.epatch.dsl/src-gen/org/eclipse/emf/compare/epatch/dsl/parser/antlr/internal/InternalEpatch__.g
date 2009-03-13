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
T18 : 'java' ;
T19 : '.' ;
T20 : 'extension' ;
T21 : '::' ;
T22 : 'resource' ;
T23 : 'left' ;
T24 : ';' ;
T25 : 'right' ;
T26 : 'object' ;
T27 : '=' ;
T28 : '|' ;
T29 : '[' ;
T30 : ',' ;
T31 : ']' ;
T32 : ':' ;
T33 : 'null' ;
T34 : 'new' ;
T35 : 'copy' ;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2270
RULE_FRAGMENT : '#' ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'/'|'['|']'|'{'|'}'|'.'|'@'|'%'|':')+;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2272
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2274
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2276
RULE_STRING : ('\"' ('\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')|~(('\\'|'\"')))* '\"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2278
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2280
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2282
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g" 2284
RULE_ANY_OTHER : .;


