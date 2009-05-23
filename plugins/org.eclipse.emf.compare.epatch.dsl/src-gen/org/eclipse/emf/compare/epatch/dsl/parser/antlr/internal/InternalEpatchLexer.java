package org.eclipse.emf.compare.epatch.dsl.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class InternalEpatchLexer extends Lexer {
    public static final int T21=21;
    public static final int RULE_ML_COMMENT=8;
    public static final int T14=14;
    public static final int T29=29;
    public static final int RULE_ID=4;
    public static final int T22=22;
    public static final int RULE_STRING=5;
    public static final int T12=12;
    public static final int T28=28;
    public static final int T23=23;
    public static final int T13=13;
    public static final int T20=20;
    public static final int T25=25;
    public static final int T18=18;
    public static final int RULE_WS=10;
    public static final int T26=26;
    public static final int T15=15;
    public static final int RULE_INT=7;
    public static final int EOF=-1;
    public static final int T32=32;
    public static final int T17=17;
    public static final int Tokens=33;
    public static final int T31=31;
    public static final int RULE_ANY_OTHER=11;
    public static final int T16=16;
    public static final int T27=27;
    public static final int RULE_SL_COMMENT=9;
    public static final int RULE_FRAGMENT=6;
    public static final int T30=30;
    public static final int T24=24;
    public static final int T19=19;
    public InternalEpatchLexer() {;} 
    public InternalEpatchLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g"; }

    // $ANTLR start T12
    public final void mT12() throws RecognitionException {
        try {
            int _type = T12;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:10:5: ( 'epatch' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:10:7: 'epatch'
            {
            match("epatch"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T12

    // $ANTLR start T13
    public final void mT13() throws RecognitionException {
        try {
            int _type = T13;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:11:5: ( '{' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:11:7: '{'
            {
            match('{'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T13

    // $ANTLR start T14
    public final void mT14() throws RecognitionException {
        try {
            int _type = T14;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:12:5: ( '}' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:12:7: '}'
            {
            match('}'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T14

    // $ANTLR start T15
    public final void mT15() throws RecognitionException {
        try {
            int _type = T15;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:13:5: ( 'import' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:13:7: 'import'
            {
            match("import"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T15

    // $ANTLR start T16
    public final void mT16() throws RecognitionException {
        try {
            int _type = T16;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:14:5: ( 'uri' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:14:7: 'uri'
            {
            match("uri"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T16

    // $ANTLR start T17
    public final void mT17() throws RecognitionException {
        try {
            int _type = T17;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:15:5: ( 'ns' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:15:7: 'ns'
            {
            match("ns"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T17

    // $ANTLR start T18
    public final void mT18() throws RecognitionException {
        try {
            int _type = T18;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:16:5: ( 'resource' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:16:7: 'resource'
            {
            match("resource"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T18

    // $ANTLR start T19
    public final void mT19() throws RecognitionException {
        try {
            int _type = T19;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:17:5: ( 'left' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:17:7: 'left'
            {
            match("left"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T19

    // $ANTLR start T20
    public final void mT20() throws RecognitionException {
        try {
            int _type = T20;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:18:5: ( ';' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:18:7: ';'
            {
            match(';'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T20

    // $ANTLR start T21
    public final void mT21() throws RecognitionException {
        try {
            int _type = T21;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:19:5: ( 'right' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:19:7: 'right'
            {
            match("right"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T21

    // $ANTLR start T22
    public final void mT22() throws RecognitionException {
        try {
            int _type = T22;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:20:5: ( 'object' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:20:7: 'object'
            {
            match("object"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T22

    // $ANTLR start T23
    public final void mT23() throws RecognitionException {
        try {
            int _type = T23;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:21:5: ( '=' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:21:7: '='
            {
            match('='); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T23

    // $ANTLR start T24
    public final void mT24() throws RecognitionException {
        try {
            int _type = T24;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:22:5: ( '|' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:22:7: '|'
            {
            match('|'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T24

    // $ANTLR start T25
    public final void mT25() throws RecognitionException {
        try {
            int _type = T25;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:23:5: ( '[' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:23:7: '['
            {
            match('['); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T25

    // $ANTLR start T26
    public final void mT26() throws RecognitionException {
        try {
            int _type = T26;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:24:5: ( ',' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:24:7: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T26

    // $ANTLR start T27
    public final void mT27() throws RecognitionException {
        try {
            int _type = T27;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:25:5: ( ']' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:25:7: ']'
            {
            match(']'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T27

    // $ANTLR start T28
    public final void mT28() throws RecognitionException {
        try {
            int _type = T28;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:26:5: ( '.' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:26:7: '.'
            {
            match('.'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T28

    // $ANTLR start T29
    public final void mT29() throws RecognitionException {
        try {
            int _type = T29;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:27:5: ( ':' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:27:7: ':'
            {
            match(':'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T29

    // $ANTLR start T30
    public final void mT30() throws RecognitionException {
        try {
            int _type = T30;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:28:5: ( 'null' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:28:7: 'null'
            {
            match("null"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T30

    // $ANTLR start T31
    public final void mT31() throws RecognitionException {
        try {
            int _type = T31;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:29:5: ( 'new' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:29:7: 'new'
            {
            match("new"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T31

    // $ANTLR start T32
    public final void mT32() throws RecognitionException {
        try {
            int _type = T32;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:30:5: ( 'copy' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:30:7: 'copy'
            {
            match("copy"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T32

    // $ANTLR start RULE_FRAGMENT
    public final void mRULE_FRAGMENT() throws RecognitionException {
        try {
            int _type = RULE_FRAGMENT;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2048:15: ( '#' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '/' | '[' | ']' | '{' | '}' | '.' | '@' | '%' | ':' )+ )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2048:17: '#' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '/' | '[' | ']' | '{' | '}' | '.' | '@' | '%' | ':' )+
            {
            match('#'); 
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2048:21: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '/' | '[' | ']' | '{' | '}' | '.' | '@' | '%' | ':' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='%'||(LA1_0>='.' && LA1_0<=':')||(LA1_0>='@' && LA1_0<='[')||LA1_0==']'||LA1_0=='_'||(LA1_0>='a' && LA1_0<='{')||LA1_0=='}') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:
            	    {
            	    if ( input.LA(1)=='%'||(input.LA(1)>='.' && input.LA(1)<=':')||(input.LA(1)>='@' && input.LA(1)<='[')||input.LA(1)==']'||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='{')||input.LA(1)=='}' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_FRAGMENT

    // $ANTLR start RULE_ID
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2050:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2050:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2050:11: ( '^' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='^') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2050:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2050:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_ID

    // $ANTLR start RULE_INT
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2052:10: ( ( '0' .. '9' )+ )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2052:12: ( '0' .. '9' )+
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2052:12: ( '0' .. '9' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2052:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_INT

    // $ANTLR start RULE_STRING
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:13: ( ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='\"') ) {
                alt7=1;
            }
            else if ( (LA7_0=='\'') ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("2054:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:16: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:20: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop5:
                    do {
                        int alt5=3;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0=='\\') ) {
                            alt5=1;
                        }
                        else if ( ((LA5_0>='\u0000' && LA5_0<='!')||(LA5_0>='#' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFE')) ) {
                            alt5=2;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:21: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:62: ~ ( ( '\\\\' | '\"' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:82: '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:87: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop6:
                    do {
                        int alt6=3;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0=='\\') ) {
                            alt6=1;
                        }
                        else if ( ((LA6_0>='\u0000' && LA6_0<='&')||(LA6_0>='(' && LA6_0<='[')||(LA6_0>=']' && LA6_0<='\uFFFE')) ) {
                            alt6=2;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:88: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2054:129: ~ ( ( '\\\\' | '\\'' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_STRING

    // $ANTLR start RULE_ML_COMMENT
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2056:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2056:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2056:24: ( options {greedy=false; } : . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFE')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFE')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2056:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match("*/"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_ML_COMMENT

    // $ANTLR start RULE_SL_COMMENT
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2058:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2058:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2058:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='\u0000' && LA9_0<='\t')||(LA9_0>='\u000B' && LA9_0<='\f')||(LA9_0>='\u000E' && LA9_0<='\uFFFE')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2058:24: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2058:40: ( ( '\\r' )? '\\n' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\n'||LA11_0=='\r') ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2058:41: ( '\\r' )? '\\n'
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2058:41: ( '\\r' )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0=='\r') ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2058:41: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_SL_COMMENT

    // $ANTLR start RULE_WS
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2060:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2060:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2060:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='\t' && LA12_0<='\n')||LA12_0=='\r'||LA12_0==' ') ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_WS

    // $ANTLR start RULE_ANY_OTHER
    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2062:16: ( . )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2062:18: .
            {
            matchAny(); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_ANY_OTHER

    public void mTokens() throws RecognitionException {
        // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:8: ( T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | RULE_FRAGMENT | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt13=29;
        int LA13_0 = input.LA(1);

        if ( (LA13_0=='e') ) {
            int LA13_1 = input.LA(2);

            if ( (LA13_1=='p') ) {
                int LA13_28 = input.LA(3);

                if ( (LA13_28=='a') ) {
                    int LA13_56 = input.LA(4);

                    if ( (LA13_56=='t') ) {
                        int LA13_67 = input.LA(5);

                        if ( (LA13_67=='c') ) {
                            int LA13_77 = input.LA(6);

                            if ( (LA13_77=='h') ) {
                                int LA13_85 = input.LA(7);

                                if ( ((LA13_85>='0' && LA13_85<='9')||(LA13_85>='A' && LA13_85<='Z')||LA13_85=='_'||(LA13_85>='a' && LA13_85<='z')) ) {
                                    alt13=23;
                                }
                                else {
                                    alt13=1;}
                            }
                            else {
                                alt13=23;}
                        }
                        else {
                            alt13=23;}
                    }
                    else {
                        alt13=23;}
                }
                else {
                    alt13=23;}
            }
            else {
                alt13=23;}
        }
        else if ( (LA13_0=='{') ) {
            alt13=2;
        }
        else if ( (LA13_0=='}') ) {
            alt13=3;
        }
        else if ( (LA13_0=='i') ) {
            int LA13_4 = input.LA(2);

            if ( (LA13_4=='m') ) {
                int LA13_32 = input.LA(3);

                if ( (LA13_32=='p') ) {
                    int LA13_57 = input.LA(4);

                    if ( (LA13_57=='o') ) {
                        int LA13_68 = input.LA(5);

                        if ( (LA13_68=='r') ) {
                            int LA13_78 = input.LA(6);

                            if ( (LA13_78=='t') ) {
                                int LA13_86 = input.LA(7);

                                if ( ((LA13_86>='0' && LA13_86<='9')||(LA13_86>='A' && LA13_86<='Z')||LA13_86=='_'||(LA13_86>='a' && LA13_86<='z')) ) {
                                    alt13=23;
                                }
                                else {
                                    alt13=4;}
                            }
                            else {
                                alt13=23;}
                        }
                        else {
                            alt13=23;}
                    }
                    else {
                        alt13=23;}
                }
                else {
                    alt13=23;}
            }
            else {
                alt13=23;}
        }
        else if ( (LA13_0=='u') ) {
            int LA13_5 = input.LA(2);

            if ( (LA13_5=='r') ) {
                int LA13_33 = input.LA(3);

                if ( (LA13_33=='i') ) {
                    int LA13_58 = input.LA(4);

                    if ( ((LA13_58>='0' && LA13_58<='9')||(LA13_58>='A' && LA13_58<='Z')||LA13_58=='_'||(LA13_58>='a' && LA13_58<='z')) ) {
                        alt13=23;
                    }
                    else {
                        alt13=5;}
                }
                else {
                    alt13=23;}
            }
            else {
                alt13=23;}
        }
        else if ( (LA13_0=='n') ) {
            switch ( input.LA(2) ) {
            case 'e':
                {
                int LA13_34 = input.LA(3);

                if ( (LA13_34=='w') ) {
                    int LA13_59 = input.LA(4);

                    if ( ((LA13_59>='0' && LA13_59<='9')||(LA13_59>='A' && LA13_59<='Z')||LA13_59=='_'||(LA13_59>='a' && LA13_59<='z')) ) {
                        alt13=23;
                    }
                    else {
                        alt13=20;}
                }
                else {
                    alt13=23;}
                }
                break;
            case 'u':
                {
                int LA13_35 = input.LA(3);

                if ( (LA13_35=='l') ) {
                    int LA13_60 = input.LA(4);

                    if ( (LA13_60=='l') ) {
                        int LA13_71 = input.LA(5);

                        if ( ((LA13_71>='0' && LA13_71<='9')||(LA13_71>='A' && LA13_71<='Z')||LA13_71=='_'||(LA13_71>='a' && LA13_71<='z')) ) {
                            alt13=23;
                        }
                        else {
                            alt13=19;}
                    }
                    else {
                        alt13=23;}
                }
                else {
                    alt13=23;}
                }
                break;
            case 's':
                {
                int LA13_36 = input.LA(3);

                if ( ((LA13_36>='0' && LA13_36<='9')||(LA13_36>='A' && LA13_36<='Z')||LA13_36=='_'||(LA13_36>='a' && LA13_36<='z')) ) {
                    alt13=23;
                }
                else {
                    alt13=6;}
                }
                break;
            default:
                alt13=23;}

        }
        else if ( (LA13_0=='r') ) {
            switch ( input.LA(2) ) {
            case 'i':
                {
                int LA13_37 = input.LA(3);

                if ( (LA13_37=='g') ) {
                    int LA13_62 = input.LA(4);

                    if ( (LA13_62=='h') ) {
                        int LA13_72 = input.LA(5);

                        if ( (LA13_72=='t') ) {
                            int LA13_80 = input.LA(6);

                            if ( ((LA13_80>='0' && LA13_80<='9')||(LA13_80>='A' && LA13_80<='Z')||LA13_80=='_'||(LA13_80>='a' && LA13_80<='z')) ) {
                                alt13=23;
                            }
                            else {
                                alt13=10;}
                        }
                        else {
                            alt13=23;}
                    }
                    else {
                        alt13=23;}
                }
                else {
                    alt13=23;}
                }
                break;
            case 'e':
                {
                int LA13_38 = input.LA(3);

                if ( (LA13_38=='s') ) {
                    int LA13_63 = input.LA(4);

                    if ( (LA13_63=='o') ) {
                        int LA13_73 = input.LA(5);

                        if ( (LA13_73=='u') ) {
                            int LA13_81 = input.LA(6);

                            if ( (LA13_81=='r') ) {
                                int LA13_88 = input.LA(7);

                                if ( (LA13_88=='c') ) {
                                    int LA13_92 = input.LA(8);

                                    if ( (LA13_92=='e') ) {
                                        int LA13_94 = input.LA(9);

                                        if ( ((LA13_94>='0' && LA13_94<='9')||(LA13_94>='A' && LA13_94<='Z')||LA13_94=='_'||(LA13_94>='a' && LA13_94<='z')) ) {
                                            alt13=23;
                                        }
                                        else {
                                            alt13=7;}
                                    }
                                    else {
                                        alt13=23;}
                                }
                                else {
                                    alt13=23;}
                            }
                            else {
                                alt13=23;}
                        }
                        else {
                            alt13=23;}
                    }
                    else {
                        alt13=23;}
                }
                else {
                    alt13=23;}
                }
                break;
            default:
                alt13=23;}

        }
        else if ( (LA13_0=='l') ) {
            int LA13_8 = input.LA(2);

            if ( (LA13_8=='e') ) {
                int LA13_39 = input.LA(3);

                if ( (LA13_39=='f') ) {
                    int LA13_64 = input.LA(4);

                    if ( (LA13_64=='t') ) {
                        int LA13_74 = input.LA(5);

                        if ( ((LA13_74>='0' && LA13_74<='9')||(LA13_74>='A' && LA13_74<='Z')||LA13_74=='_'||(LA13_74>='a' && LA13_74<='z')) ) {
                            alt13=23;
                        }
                        else {
                            alt13=8;}
                    }
                    else {
                        alt13=23;}
                }
                else {
                    alt13=23;}
            }
            else {
                alt13=23;}
        }
        else if ( (LA13_0==';') ) {
            alt13=9;
        }
        else if ( (LA13_0=='o') ) {
            int LA13_10 = input.LA(2);

            if ( (LA13_10=='b') ) {
                int LA13_41 = input.LA(3);

                if ( (LA13_41=='j') ) {
                    int LA13_65 = input.LA(4);

                    if ( (LA13_65=='e') ) {
                        int LA13_75 = input.LA(5);

                        if ( (LA13_75=='c') ) {
                            int LA13_83 = input.LA(6);

                            if ( (LA13_83=='t') ) {
                                int LA13_89 = input.LA(7);

                                if ( ((LA13_89>='0' && LA13_89<='9')||(LA13_89>='A' && LA13_89<='Z')||LA13_89=='_'||(LA13_89>='a' && LA13_89<='z')) ) {
                                    alt13=23;
                                }
                                else {
                                    alt13=11;}
                            }
                            else {
                                alt13=23;}
                        }
                        else {
                            alt13=23;}
                    }
                    else {
                        alt13=23;}
                }
                else {
                    alt13=23;}
            }
            else {
                alt13=23;}
        }
        else if ( (LA13_0=='=') ) {
            alt13=12;
        }
        else if ( (LA13_0=='|') ) {
            alt13=13;
        }
        else if ( (LA13_0=='[') ) {
            alt13=14;
        }
        else if ( (LA13_0==',') ) {
            alt13=15;
        }
        else if ( (LA13_0==']') ) {
            alt13=16;
        }
        else if ( (LA13_0=='.') ) {
            alt13=17;
        }
        else if ( (LA13_0==':') ) {
            alt13=18;
        }
        else if ( (LA13_0=='c') ) {
            int LA13_18 = input.LA(2);

            if ( (LA13_18=='o') ) {
                int LA13_49 = input.LA(3);

                if ( (LA13_49=='p') ) {
                    int LA13_66 = input.LA(4);

                    if ( (LA13_66=='y') ) {
                        int LA13_76 = input.LA(5);

                        if ( ((LA13_76>='0' && LA13_76<='9')||(LA13_76>='A' && LA13_76<='Z')||LA13_76=='_'||(LA13_76>='a' && LA13_76<='z')) ) {
                            alt13=23;
                        }
                        else {
                            alt13=21;}
                    }
                    else {
                        alt13=23;}
                }
                else {
                    alt13=23;}
            }
            else {
                alt13=23;}
        }
        else if ( (LA13_0=='#') ) {
            int LA13_19 = input.LA(2);

            if ( (LA13_19=='%'||(LA13_19>='.' && LA13_19<=':')||(LA13_19>='@' && LA13_19<='[')||LA13_19==']'||LA13_19=='_'||(LA13_19>='a' && LA13_19<='{')||LA13_19=='}') ) {
                alt13=22;
            }
            else {
                alt13=29;}
        }
        else if ( (LA13_0=='^') ) {
            int LA13_20 = input.LA(2);

            if ( ((LA13_20>='A' && LA13_20<='Z')||LA13_20=='_'||(LA13_20>='a' && LA13_20<='z')) ) {
                alt13=23;
            }
            else {
                alt13=29;}
        }
        else if ( ((LA13_0>='A' && LA13_0<='Z')||LA13_0=='_'||(LA13_0>='a' && LA13_0<='b')||LA13_0=='d'||(LA13_0>='f' && LA13_0<='h')||(LA13_0>='j' && LA13_0<='k')||LA13_0=='m'||(LA13_0>='p' && LA13_0<='q')||(LA13_0>='s' && LA13_0<='t')||(LA13_0>='v' && LA13_0<='z')) ) {
            alt13=23;
        }
        else if ( ((LA13_0>='0' && LA13_0<='9')) ) {
            alt13=24;
        }
        else if ( (LA13_0=='\"') ) {
            int LA13_23 = input.LA(2);

            if ( ((LA13_23>='\u0000' && LA13_23<='\uFFFE')) ) {
                alt13=25;
            }
            else {
                alt13=29;}
        }
        else if ( (LA13_0=='\'') ) {
            int LA13_24 = input.LA(2);

            if ( ((LA13_24>='\u0000' && LA13_24<='\uFFFE')) ) {
                alt13=25;
            }
            else {
                alt13=29;}
        }
        else if ( (LA13_0=='/') ) {
            switch ( input.LA(2) ) {
            case '/':
                {
                alt13=27;
                }
                break;
            case '*':
                {
                alt13=26;
                }
                break;
            default:
                alt13=29;}

        }
        else if ( ((LA13_0>='\t' && LA13_0<='\n')||LA13_0=='\r'||LA13_0==' ') ) {
            alt13=28;
        }
        else if ( ((LA13_0>='\u0000' && LA13_0<='\b')||(LA13_0>='\u000B' && LA13_0<='\f')||(LA13_0>='\u000E' && LA13_0<='\u001F')||LA13_0=='!'||(LA13_0>='$' && LA13_0<='&')||(LA13_0>='(' && LA13_0<='+')||LA13_0=='-'||LA13_0=='<'||(LA13_0>='>' && LA13_0<='@')||LA13_0=='\\'||LA13_0=='`'||(LA13_0>='~' && LA13_0<='\uFFFE')) ) {
            alt13=29;
        }
        else {
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | RULE_FRAGMENT | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );", 13, 0, input);

            throw nvae;
        }
        switch (alt13) {
            case 1 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:10: T12
                {
                mT12(); 

                }
                break;
            case 2 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:14: T13
                {
                mT13(); 

                }
                break;
            case 3 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:18: T14
                {
                mT14(); 

                }
                break;
            case 4 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:22: T15
                {
                mT15(); 

                }
                break;
            case 5 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:26: T16
                {
                mT16(); 

                }
                break;
            case 6 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:30: T17
                {
                mT17(); 

                }
                break;
            case 7 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:34: T18
                {
                mT18(); 

                }
                break;
            case 8 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:38: T19
                {
                mT19(); 

                }
                break;
            case 9 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:42: T20
                {
                mT20(); 

                }
                break;
            case 10 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:46: T21
                {
                mT21(); 

                }
                break;
            case 11 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:50: T22
                {
                mT22(); 

                }
                break;
            case 12 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:54: T23
                {
                mT23(); 

                }
                break;
            case 13 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:58: T24
                {
                mT24(); 

                }
                break;
            case 14 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:62: T25
                {
                mT25(); 

                }
                break;
            case 15 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:66: T26
                {
                mT26(); 

                }
                break;
            case 16 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:70: T27
                {
                mT27(); 

                }
                break;
            case 17 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:74: T28
                {
                mT28(); 

                }
                break;
            case 18 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:78: T29
                {
                mT29(); 

                }
                break;
            case 19 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:82: T30
                {
                mT30(); 

                }
                break;
            case 20 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:86: T31
                {
                mT31(); 

                }
                break;
            case 21 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:90: T32
                {
                mT32(); 

                }
                break;
            case 22 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:94: RULE_FRAGMENT
                {
                mRULE_FRAGMENT(); 

                }
                break;
            case 23 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:108: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 24 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:116: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 25 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:125: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 26 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:137: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 27 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:153: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 28 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:169: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 29 :
                // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1:177: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


 

}