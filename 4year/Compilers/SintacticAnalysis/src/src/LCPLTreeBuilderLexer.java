// $ANTLR 3.2 Sep 23, 2009 12:02:23 src/LCPLTreeBuilder.g 2013-11-19 14:14:40

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LCPLTreeBuilderLexer extends Lexer {
    public static final int IFBODY=61;
    public static final int CAST=53;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__67=67;
    public static final int CLASS=7;
    public static final int ADDITION=16;
    public static final int WHILE=58;
    public static final int FORMALPARAM=31;
    public static final int DISPATCH=34;
    public static final int NEW=5;
    public static final int CONDITION=60;
    public static final int METHODEXPRESSION=29;
    public static final int NOT=23;
    public static final int ATOM=36;
    public static final int ID=38;
    public static final int EOF=-1;
    public static final int DECLARATION=27;
    public static final int STATEMENT=37;
    public static final int IF=55;
    public static final int INDEX=50;
    public static final int ELSEBODY=62;
    public static final int PARENT=44;
    public static final int STATICDISPATCH=51;
    public static final int NAME=41;
    public static final int THEN=56;
    public static final int INHERITS=8;
    public static final int MULTIPLY=18;
    public static final int SUBSTR=49;
    public static final int IDENTIFIER=43;
    public static final int STRING_CONST=42;
    public static final int LOOP=14;
    public static final int EQUAL=22;
    public static final int STRINGCONST=46;
    public static final int VAR=12;
    public static final int VOID=10;
    public static final int LESSTHANEQUAL=21;
    public static final int PARAMCALLS=52;
    public static final int IFST=63;
    public static final int DIVIDE=19;
    public static final int INTEGER=45;
    public static final int LESSTHAN=20;
    public static final int WHILEBODY=54;
    public static final int SYMBOL=35;
    public static final int FEATURES=25;
    public static final int LINE_COMMENT=65;
    public static final int ELSE=57;
    public static final int VOIDKWRD=47;
    public static final int DECLARATIONS=26;
    public static final int CLASSDEFS=24;
    public static final int INT=39;
    public static final int MINUS=17;
    public static final int LOCAL=13;
    public static final int SELFKWRD=48;
    public static final int COLON=15;
    public static final int LOCALDEF=33;
    public static final int T__71=71;
    public static final int WS=66;
    public static final int T__72=72;
    public static final int T__70=70;
    public static final int WHILEST=59;
    public static final int LOCALDEFS=32;
    public static final int ARROW=6;
    public static final int PROGRAM=4;
    public static final int ASSIGNMENT=40;
    public static final int METHODNAME=30;
    public static final int END=9;
    public static final int T__75=75;
    public static final int SELF=11;
    public static final int T__74=74;
    public static final int METHOD=28;
    public static final int T__73=73;
    public static final int STRING=64;

    // delegates
    // delegators

    public LCPLTreeBuilderLexer() {;} 
    public LCPLTreeBuilderLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public LCPLTreeBuilderLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "src/LCPLTreeBuilder.g"; }

    // $ANTLR start "NEW"
    public final void mNEW() throws RecognitionException {
        try {
            int _type = NEW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:7:5: ( 'new' )
            // src/LCPLTreeBuilder.g:7:7: 'new'
            {
            match("new"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEW"

    // $ANTLR start "ARROW"
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:8:7: ( '->' )
            // src/LCPLTreeBuilder.g:8:9: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ARROW"

    // $ANTLR start "CLASS"
    public final void mCLASS() throws RecognitionException {
        try {
            int _type = CLASS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:9:7: ( 'class' )
            // src/LCPLTreeBuilder.g:9:9: 'class'
            {
            match("class"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CLASS"

    // $ANTLR start "INHERITS"
    public final void mINHERITS() throws RecognitionException {
        try {
            int _type = INHERITS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:10:10: ( 'inherits' )
            // src/LCPLTreeBuilder.g:10:12: 'inherits'
            {
            match("inherits"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INHERITS"

    // $ANTLR start "END"
    public final void mEND() throws RecognitionException {
        try {
            int _type = END;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:11:5: ( 'end' )
            // src/LCPLTreeBuilder.g:11:7: 'end'
            {
            match("end"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "END"

    // $ANTLR start "VOID"
    public final void mVOID() throws RecognitionException {
        try {
            int _type = VOID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:12:6: ( 'void' )
            // src/LCPLTreeBuilder.g:12:8: 'void'
            {
            match("void"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VOID"

    // $ANTLR start "SELF"
    public final void mSELF() throws RecognitionException {
        try {
            int _type = SELF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:13:6: ( 'self' )
            // src/LCPLTreeBuilder.g:13:8: 'self'
            {
            match("self"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SELF"

    // $ANTLR start "VAR"
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:14:5: ( 'var' )
            // src/LCPLTreeBuilder.g:14:7: 'var'
            {
            match("var"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VAR"

    // $ANTLR start "LOCAL"
    public final void mLOCAL() throws RecognitionException {
        try {
            int _type = LOCAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:15:7: ( 'local' )
            // src/LCPLTreeBuilder.g:15:9: 'local'
            {
            match("local"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LOCAL"

    // $ANTLR start "LOOP"
    public final void mLOOP() throws RecognitionException {
        try {
            int _type = LOOP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:16:6: ( 'loop' )
            // src/LCPLTreeBuilder.g:16:8: 'loop'
            {
            match("loop"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LOOP"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:17:7: ( ':' )
            // src/LCPLTreeBuilder.g:17:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COLON"

    // $ANTLR start "ADDITION"
    public final void mADDITION() throws RecognitionException {
        try {
            int _type = ADDITION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:18:10: ( '+' )
            // src/LCPLTreeBuilder.g:18:12: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ADDITION"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:19:7: ( '-' )
            // src/LCPLTreeBuilder.g:19:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "MULTIPLY"
    public final void mMULTIPLY() throws RecognitionException {
        try {
            int _type = MULTIPLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:20:10: ( '*' )
            // src/LCPLTreeBuilder.g:20:12: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULTIPLY"

    // $ANTLR start "DIVIDE"
    public final void mDIVIDE() throws RecognitionException {
        try {
            int _type = DIVIDE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:21:8: ( '/' )
            // src/LCPLTreeBuilder.g:21:10: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIVIDE"

    // $ANTLR start "LESSTHAN"
    public final void mLESSTHAN() throws RecognitionException {
        try {
            int _type = LESSTHAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:22:10: ( '<' )
            // src/LCPLTreeBuilder.g:22:12: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LESSTHAN"

    // $ANTLR start "LESSTHANEQUAL"
    public final void mLESSTHANEQUAL() throws RecognitionException {
        try {
            int _type = LESSTHANEQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:23:15: ( '<=' )
            // src/LCPLTreeBuilder.g:23:17: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LESSTHANEQUAL"

    // $ANTLR start "EQUAL"
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:24:7: ( '==' )
            // src/LCPLTreeBuilder.g:24:9: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUAL"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:25:5: ( '!' )
            // src/LCPLTreeBuilder.g:25:7: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "ASSIGNMENT"
    public final void mASSIGNMENT() throws RecognitionException {
        try {
            int _type = ASSIGNMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:26:12: ( '=' )
            // src/LCPLTreeBuilder.g:26:14: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ASSIGNMENT"

    // $ANTLR start "SUBSTR"
    public final void mSUBSTR() throws RecognitionException {
        try {
            int _type = SUBSTR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:27:8: ( 'substring' )
            // src/LCPLTreeBuilder.g:27:10: 'substring'
            {
            match("substring"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SUBSTR"

    // $ANTLR start "IF"
    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:28:4: ( 'if' )
            // src/LCPLTreeBuilder.g:28:6: 'if'
            {
            match("if"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IF"

    // $ANTLR start "THEN"
    public final void mTHEN() throws RecognitionException {
        try {
            int _type = THEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:29:6: ( 'then' )
            // src/LCPLTreeBuilder.g:29:8: 'then'
            {
            match("then"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "THEN"

    // $ANTLR start "ELSE"
    public final void mELSE() throws RecognitionException {
        try {
            int _type = ELSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:30:6: ( 'else' )
            // src/LCPLTreeBuilder.g:30:8: 'else'
            {
            match("else"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ELSE"

    // $ANTLR start "WHILE"
    public final void mWHILE() throws RecognitionException {
        try {
            int _type = WHILE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:31:7: ( 'while' )
            // src/LCPLTreeBuilder.g:31:9: 'while'
            {
            match("while"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHILE"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:32:7: ( ';' )
            // src/LCPLTreeBuilder.g:32:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:33:7: ( ',' )
            // src/LCPLTreeBuilder.g:33:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:34:7: ( '[' )
            // src/LCPLTreeBuilder.g:34:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:35:7: ( '.' )
            // src/LCPLTreeBuilder.g:35:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:36:7: ( ']' )
            // src/LCPLTreeBuilder.g:36:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:37:7: ( '(' )
            // src/LCPLTreeBuilder.g:37:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:38:7: ( ')' )
            // src/LCPLTreeBuilder.g:38:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__73"

    // $ANTLR start "T__74"
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:39:7: ( '{' )
            // src/LCPLTreeBuilder.g:39:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__74"

    // $ANTLR start "T__75"
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:40:7: ( '}' )
            // src/LCPLTreeBuilder.g:40:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__75"

    // $ANTLR start "STRING_CONST"
    public final void mSTRING_CONST() throws RecognitionException {
        try {
            int _type = STRING_CONST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:341:5: ( '\"Hello world!\"' )
            // src/LCPLTreeBuilder.g:343:13: '\"Hello world!\"'
            {
            match("\"Hello world!\""); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING_CONST"

    // $ANTLR start "LINE_COMMENT"
    public final void mLINE_COMMENT() throws RecognitionException {
        try {
            int _type = LINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:347:3: ( '#' (~ ( '\\r' | '\\n' ) )* )
            // src/LCPLTreeBuilder.g:349:3: '#' (~ ( '\\r' | '\\n' ) )*
            {
            match('#'); 
            // src/LCPLTreeBuilder.g:351:3: (~ ( '\\r' | '\\n' ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\u0000' && LA1_0<='\t')||(LA1_0>='\u000B' && LA1_0<='\f')||(LA1_0>='\u000E' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:351:3: ~ ( '\\r' | '\\n' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);



               _channel = HIDDEN;

              

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LINE_COMMENT"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:374:3: ( '\"' (~ ( '\"' | '\\\\' | '\\r' | '\\n' ) | '\\\\' ( . | '\\r\\n' ) )* '\"' )
            // src/LCPLTreeBuilder.g:376:3: '\"' (~ ( '\"' | '\\\\' | '\\r' | '\\n' ) | '\\\\' ( . | '\\r\\n' ) )* '\"'
            {
            match('\"'); 
            // src/LCPLTreeBuilder.g:377:3: (~ ( '\"' | '\\\\' | '\\r' | '\\n' ) | '\\\\' ( . | '\\r\\n' ) )*
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='!')||(LA3_0>='#' && LA3_0<='[')||(LA3_0>=']' && LA3_0<='\uFFFF')) ) {
                    alt3=1;
                }
                else if ( (LA3_0=='\\') ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:377:4: ~ ( '\"' | '\\\\' | '\\r' | '\\n' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;
            	case 2 :
            	    // src/LCPLTreeBuilder.g:381:5: '\\\\' ( . | '\\r\\n' )
            	    {
            	    match('\\'); 
            	    // src/LCPLTreeBuilder.g:381:10: ( . | '\\r\\n' )
            	    int alt2=2;
            	    int LA2_0 = input.LA(1);

            	    if ( (LA2_0=='\r') ) {
            	        int LA2_1 = input.LA(2);

            	        if ( (LA2_1=='\n') ) {
            	            alt2=2;
            	        }
            	        else if ( ((LA2_1>='\u0000' && LA2_1<='\t')||(LA2_1>='\u000B' && LA2_1<='\f')||(LA2_1>='\u000E' && LA2_1<='\uFFFF')) ) {
            	            alt2=1;
            	        }
            	        else {
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 2, 1, input);

            	            throw nvae;
            	        }
            	    }
            	    else if ( ((LA2_0>='\u0000' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='\uFFFF')) ) {
            	        alt2=1;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 2, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt2) {
            	        case 1 :
            	            // src/LCPLTreeBuilder.g:381:12: .
            	            {
            	            matchAny(); 

            	            }
            	            break;
            	        case 2 :
            	            // src/LCPLTreeBuilder.g:381:16: '\\r\\n'
            	            {
            	            match("\r\n"); 


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:389:3: ( ( '0' .. '9' )+ )
            // src/LCPLTreeBuilder.g:391:3: ( '0' .. '9' )+
            {
            // src/LCPLTreeBuilder.g:391:3: ( '0' .. '9' )+
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
            	    // src/LCPLTreeBuilder.g:391:4: '0' .. '9'
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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:401:5: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src/LCPLTreeBuilder.g:403:9: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // src/LCPLTreeBuilder.g:403:29: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')||(LA5_0>='A' && LA5_0<='Z')||LA5_0=='_'||(LA5_0>='a' && LA5_0<='z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/LCPLTreeBuilder.g:411:5: ( ( ' ' | '\\r' | '\\t' | '\\n' )+ )
            // src/LCPLTreeBuilder.g:413:3: ( ' ' | '\\r' | '\\t' | '\\n' )+
            {
            // src/LCPLTreeBuilder.g:413:3: ( ' ' | '\\r' | '\\t' | '\\n' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\t' && LA6_0<='\n')||LA6_0=='\r'||LA6_0==' ') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);



               _channel = HIDDEN;

              

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // src/LCPLTreeBuilder.g:1:8: ( NEW | ARROW | CLASS | INHERITS | END | VOID | SELF | VAR | LOCAL | LOOP | COLON | ADDITION | MINUS | MULTIPLY | DIVIDE | LESSTHAN | LESSTHANEQUAL | EQUAL | NOT | ASSIGNMENT | SUBSTR | IF | THEN | ELSE | WHILE | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | STRING_CONST | LINE_COMMENT | STRING | INT | ID | WS )
        int alt7=40;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // src/LCPLTreeBuilder.g:1:10: NEW
                {
                mNEW(); 

                }
                break;
            case 2 :
                // src/LCPLTreeBuilder.g:1:14: ARROW
                {
                mARROW(); 

                }
                break;
            case 3 :
                // src/LCPLTreeBuilder.g:1:20: CLASS
                {
                mCLASS(); 

                }
                break;
            case 4 :
                // src/LCPLTreeBuilder.g:1:26: INHERITS
                {
                mINHERITS(); 

                }
                break;
            case 5 :
                // src/LCPLTreeBuilder.g:1:35: END
                {
                mEND(); 

                }
                break;
            case 6 :
                // src/LCPLTreeBuilder.g:1:39: VOID
                {
                mVOID(); 

                }
                break;
            case 7 :
                // src/LCPLTreeBuilder.g:1:44: SELF
                {
                mSELF(); 

                }
                break;
            case 8 :
                // src/LCPLTreeBuilder.g:1:49: VAR
                {
                mVAR(); 

                }
                break;
            case 9 :
                // src/LCPLTreeBuilder.g:1:53: LOCAL
                {
                mLOCAL(); 

                }
                break;
            case 10 :
                // src/LCPLTreeBuilder.g:1:59: LOOP
                {
                mLOOP(); 

                }
                break;
            case 11 :
                // src/LCPLTreeBuilder.g:1:64: COLON
                {
                mCOLON(); 

                }
                break;
            case 12 :
                // src/LCPLTreeBuilder.g:1:70: ADDITION
                {
                mADDITION(); 

                }
                break;
            case 13 :
                // src/LCPLTreeBuilder.g:1:79: MINUS
                {
                mMINUS(); 

                }
                break;
            case 14 :
                // src/LCPLTreeBuilder.g:1:85: MULTIPLY
                {
                mMULTIPLY(); 

                }
                break;
            case 15 :
                // src/LCPLTreeBuilder.g:1:94: DIVIDE
                {
                mDIVIDE(); 

                }
                break;
            case 16 :
                // src/LCPLTreeBuilder.g:1:101: LESSTHAN
                {
                mLESSTHAN(); 

                }
                break;
            case 17 :
                // src/LCPLTreeBuilder.g:1:110: LESSTHANEQUAL
                {
                mLESSTHANEQUAL(); 

                }
                break;
            case 18 :
                // src/LCPLTreeBuilder.g:1:124: EQUAL
                {
                mEQUAL(); 

                }
                break;
            case 19 :
                // src/LCPLTreeBuilder.g:1:130: NOT
                {
                mNOT(); 

                }
                break;
            case 20 :
                // src/LCPLTreeBuilder.g:1:134: ASSIGNMENT
                {
                mASSIGNMENT(); 

                }
                break;
            case 21 :
                // src/LCPLTreeBuilder.g:1:145: SUBSTR
                {
                mSUBSTR(); 

                }
                break;
            case 22 :
                // src/LCPLTreeBuilder.g:1:152: IF
                {
                mIF(); 

                }
                break;
            case 23 :
                // src/LCPLTreeBuilder.g:1:155: THEN
                {
                mTHEN(); 

                }
                break;
            case 24 :
                // src/LCPLTreeBuilder.g:1:160: ELSE
                {
                mELSE(); 

                }
                break;
            case 25 :
                // src/LCPLTreeBuilder.g:1:165: WHILE
                {
                mWHILE(); 

                }
                break;
            case 26 :
                // src/LCPLTreeBuilder.g:1:171: T__67
                {
                mT__67(); 

                }
                break;
            case 27 :
                // src/LCPLTreeBuilder.g:1:177: T__68
                {
                mT__68(); 

                }
                break;
            case 28 :
                // src/LCPLTreeBuilder.g:1:183: T__69
                {
                mT__69(); 

                }
                break;
            case 29 :
                // src/LCPLTreeBuilder.g:1:189: T__70
                {
                mT__70(); 

                }
                break;
            case 30 :
                // src/LCPLTreeBuilder.g:1:195: T__71
                {
                mT__71(); 

                }
                break;
            case 31 :
                // src/LCPLTreeBuilder.g:1:201: T__72
                {
                mT__72(); 

                }
                break;
            case 32 :
                // src/LCPLTreeBuilder.g:1:207: T__73
                {
                mT__73(); 

                }
                break;
            case 33 :
                // src/LCPLTreeBuilder.g:1:213: T__74
                {
                mT__74(); 

                }
                break;
            case 34 :
                // src/LCPLTreeBuilder.g:1:219: T__75
                {
                mT__75(); 

                }
                break;
            case 35 :
                // src/LCPLTreeBuilder.g:1:225: STRING_CONST
                {
                mSTRING_CONST(); 

                }
                break;
            case 36 :
                // src/LCPLTreeBuilder.g:1:238: LINE_COMMENT
                {
                mLINE_COMMENT(); 

                }
                break;
            case 37 :
                // src/LCPLTreeBuilder.g:1:251: STRING
                {
                mSTRING(); 

                }
                break;
            case 38 :
                // src/LCPLTreeBuilder.g:1:258: INT
                {
                mINT(); 

                }
                break;
            case 39 :
                // src/LCPLTreeBuilder.g:1:262: ID
                {
                mID(); 

                }
                break;
            case 40 :
                // src/LCPLTreeBuilder.g:1:265: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\1\uffff\1\36\1\42\6\36\4\uffff\1\56\1\60\1\uffff\2\36\16\uffff"+
        "\1\36\2\uffff\2\36\1\70\7\36\4\uffff\2\36\2\uffff\1\104\2\36\1\uffff"+
        "\1\107\2\36\1\112\6\36\2\uffff\2\36\1\uffff\1\124\1\125\1\uffff"+
        "\1\126\2\36\1\131\1\132\1\36\1\uffff\1\135\1\36\3\uffff\1\36\1\140"+
        "\2\uffff\1\141\2\uffff\2\36\3\uffff\2\36\1\uffff\1\151\1\36\2\uffff"+
        "\1\154\10\uffff";
    static final String DFA7_eofS =
        "\163\uffff";
    static final String DFA7_minS =
        "\1\11\1\145\1\76\1\154\1\146\1\154\1\141\1\145\1\157\4\uffff\2\75"+
        "\1\uffff\2\150\11\uffff\1\0\4\uffff\1\167\2\uffff\1\141\1\150\1"+
        "\60\1\144\1\163\1\151\1\162\1\154\1\142\1\143\4\uffff\1\145\1\151"+
        "\1\0\1\uffff\1\60\1\163\1\145\1\uffff\1\60\1\145\1\144\1\60\1\146"+
        "\1\163\1\141\1\160\1\156\1\154\1\0\1\uffff\1\163\1\162\1\uffff\2"+
        "\60\1\uffff\1\60\1\164\1\154\2\60\1\145\1\0\1\60\1\151\3\uffff\1"+
        "\162\1\60\2\uffff\1\60\1\0\1\uffff\1\164\1\151\2\uffff\1\0\1\163"+
        "\1\156\1\0\1\60\1\147\1\0\1\uffff\1\60\1\0\1\uffff\4\0\2\uffff";
    static final String DFA7_maxS =
        "\1\175\1\145\1\76\1\154\2\156\1\157\1\165\1\157\4\uffff\2\75\1\uffff"+
        "\2\150\11\uffff\1\uffff\4\uffff\1\167\2\uffff\1\141\1\150\1\172"+
        "\1\144\1\163\1\151\1\162\1\154\1\142\1\157\4\uffff\1\145\1\151\1"+
        "\uffff\1\uffff\1\172\1\163\1\145\1\uffff\1\172\1\145\1\144\1\172"+
        "\1\146\1\163\1\141\1\160\1\156\1\154\1\uffff\1\uffff\1\163\1\162"+
        "\1\uffff\2\172\1\uffff\1\172\1\164\1\154\2\172\1\145\1\uffff\1\172"+
        "\1\151\3\uffff\1\162\1\172\2\uffff\1\172\1\uffff\1\uffff\1\164\1"+
        "\151\2\uffff\1\uffff\1\163\1\156\1\uffff\1\172\1\147\1\uffff\1\uffff"+
        "\1\172\1\uffff\1\uffff\4\uffff\2\uffff";
    static final String DFA7_acceptS =
        "\11\uffff\1\13\1\14\1\16\1\17\2\uffff\1\23\2\uffff\1\32\1\33\1\34"+
        "\1\35\1\36\1\37\1\40\1\41\1\42\1\uffff\1\44\1\46\1\47\1\50\1\uffff"+
        "\1\2\1\15\12\uffff\1\21\1\20\1\22\1\24\3\uffff\1\45\3\uffff\1\26"+
        "\13\uffff\1\1\2\uffff\1\5\2\uffff\1\10\11\uffff\1\30\1\6\1\7\2\uffff"+
        "\1\12\1\27\2\uffff\1\3\2\uffff\1\11\1\31\7\uffff\1\4\2\uffff\1\25"+
        "\4\uffff\2\43";
    static final String DFA7_specialS =
        "\33\uffff\1\14\27\uffff\1\1\17\uffff\1\0\15\uffff\1\3\12\uffff\1"+
        "\2\5\uffff\1\5\2\uffff\1\4\2\uffff\1\6\2\uffff\1\7\1\uffff\1\10"+
        "\1\11\1\12\1\13\2\uffff}>";
    static final String[] DFA7_transitionS = {
            "\2\37\2\uffff\1\37\22\uffff\1\37\1\17\1\33\1\34\4\uffff\1\27"+
            "\1\30\1\13\1\12\1\23\1\2\1\25\1\14\12\35\1\11\1\22\1\15\1\16"+
            "\3\uffff\32\36\1\24\1\uffff\1\26\3\uffff\2\36\1\3\1\36\1\5\3"+
            "\36\1\4\2\36\1\10\1\36\1\1\4\36\1\7\1\20\1\36\1\6\1\21\3\36"+
            "\1\31\1\uffff\1\32",
            "\1\40",
            "\1\41",
            "\1\43",
            "\1\45\7\uffff\1\44",
            "\1\47\1\uffff\1\46",
            "\1\51\15\uffff\1\50",
            "\1\52\17\uffff\1\53",
            "\1\54",
            "",
            "",
            "",
            "",
            "\1\55",
            "\1\57",
            "",
            "\1\61",
            "\1\62",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\64\1\uffff\2\64\1\uffff\72\64\1\63\uffb7\64",
            "",
            "",
            "",
            "",
            "\1\65",
            "",
            "",
            "\1\66",
            "\1\67",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77\13\uffff\1\100",
            "",
            "",
            "",
            "",
            "\1\101",
            "\1\102",
            "\12\64\1\uffff\2\64\1\uffff\127\64\1\103\uff9a\64",
            "",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\105",
            "\1\106",
            "",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\110",
            "\1\111",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\12\64\1\uffff\2\64\1\uffff\136\64\1\121\uff93\64",
            "",
            "\1\122",
            "\1\123",
            "",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\127",
            "\1\130",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\133",
            "\12\64\1\uffff\2\64\1\uffff\136\64\1\134\uff93\64",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\136",
            "",
            "",
            "",
            "\1\137",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "",
            "",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\12\64\1\uffff\2\64\1\uffff\141\64\1\142\uff90\64",
            "",
            "\1\143",
            "\1\144",
            "",
            "",
            "\12\64\1\uffff\2\64\1\uffff\22\64\1\145\uffdf\64",
            "\1\146",
            "\1\147",
            "\12\64\1\uffff\2\64\1\uffff\151\64\1\150\uff88\64",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\152",
            "\12\64\1\uffff\2\64\1\uffff\141\64\1\153\uff90\64",
            "",
            "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\12\64\1\uffff\2\64\1\uffff\144\64\1\155\uff8d\64",
            "",
            "\12\64\1\uffff\2\64\1\uffff\136\64\1\156\uff93\64",
            "\12\64\1\uffff\2\64\1\uffff\126\64\1\157\uff9b\64",
            "\12\64\1\uffff\2\64\1\uffff\23\64\1\160\uffde\64",
            "\12\64\1\uffff\2\64\1\uffff\24\64\1\161\uffdd\64",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( NEW | ARROW | CLASS | INHERITS | END | VOID | SELF | VAR | LOCAL | LOOP | COLON | ADDITION | MINUS | MULTIPLY | DIVIDE | LESSTHAN | LESSTHANEQUAL | EQUAL | NOT | ASSIGNMENT | SUBSTR | IF | THEN | ELSE | WHILE | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | STRING_CONST | LINE_COMMENT | STRING | INT | ID | WS );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA7_67 = input.LA(1);

                        s = -1;
                        if ( (LA7_67=='l') ) {s = 81;}

                        else if ( ((LA7_67>='\u0000' && LA7_67<='\t')||(LA7_67>='\u000B' && LA7_67<='\f')||(LA7_67>='\u000E' && LA7_67<='k')||(LA7_67>='m' && LA7_67<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA7_51 = input.LA(1);

                        s = -1;
                        if ( (LA7_51=='e') ) {s = 67;}

                        else if ( ((LA7_51>='\u0000' && LA7_51<='\t')||(LA7_51>='\u000B' && LA7_51<='\f')||(LA7_51>='\u000E' && LA7_51<='d')||(LA7_51>='f' && LA7_51<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA7_92 = input.LA(1);

                        s = -1;
                        if ( (LA7_92=='o') ) {s = 98;}

                        else if ( ((LA7_92>='\u0000' && LA7_92<='\t')||(LA7_92>='\u000B' && LA7_92<='\f')||(LA7_92>='\u000E' && LA7_92<='n')||(LA7_92>='p' && LA7_92<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA7_81 = input.LA(1);

                        s = -1;
                        if ( (LA7_81=='l') ) {s = 92;}

                        else if ( ((LA7_81>='\u0000' && LA7_81<='\t')||(LA7_81>='\u000B' && LA7_81<='\f')||(LA7_81>='\u000E' && LA7_81<='k')||(LA7_81>='m' && LA7_81<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA7_101 = input.LA(1);

                        s = -1;
                        if ( (LA7_101=='w') ) {s = 104;}

                        else if ( ((LA7_101>='\u0000' && LA7_101<='\t')||(LA7_101>='\u000B' && LA7_101<='\f')||(LA7_101>='\u000E' && LA7_101<='v')||(LA7_101>='x' && LA7_101<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA7_98 = input.LA(1);

                        s = -1;
                        if ( (LA7_98==' ') ) {s = 101;}

                        else if ( ((LA7_98>='\u0000' && LA7_98<='\t')||(LA7_98>='\u000B' && LA7_98<='\f')||(LA7_98>='\u000E' && LA7_98<='\u001F')||(LA7_98>='!' && LA7_98<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA7_104 = input.LA(1);

                        s = -1;
                        if ( (LA7_104=='o') ) {s = 107;}

                        else if ( ((LA7_104>='\u0000' && LA7_104<='\t')||(LA7_104>='\u000B' && LA7_104<='\f')||(LA7_104>='\u000E' && LA7_104<='n')||(LA7_104>='p' && LA7_104<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA7_107 = input.LA(1);

                        s = -1;
                        if ( (LA7_107=='r') ) {s = 109;}

                        else if ( ((LA7_107>='\u0000' && LA7_107<='\t')||(LA7_107>='\u000B' && LA7_107<='\f')||(LA7_107>='\u000E' && LA7_107<='q')||(LA7_107>='s' && LA7_107<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA7_109 = input.LA(1);

                        s = -1;
                        if ( (LA7_109=='l') ) {s = 110;}

                        else if ( ((LA7_109>='\u0000' && LA7_109<='\t')||(LA7_109>='\u000B' && LA7_109<='\f')||(LA7_109>='\u000E' && LA7_109<='k')||(LA7_109>='m' && LA7_109<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA7_110 = input.LA(1);

                        s = -1;
                        if ( (LA7_110=='d') ) {s = 111;}

                        else if ( ((LA7_110>='\u0000' && LA7_110<='\t')||(LA7_110>='\u000B' && LA7_110<='\f')||(LA7_110>='\u000E' && LA7_110<='c')||(LA7_110>='e' && LA7_110<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA7_111 = input.LA(1);

                        s = -1;
                        if ( (LA7_111=='!') ) {s = 112;}

                        else if ( ((LA7_111>='\u0000' && LA7_111<='\t')||(LA7_111>='\u000B' && LA7_111<='\f')||(LA7_111>='\u000E' && LA7_111<=' ')||(LA7_111>='\"' && LA7_111<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA7_112 = input.LA(1);

                        s = -1;
                        if ( (LA7_112=='\"') ) {s = 113;}

                        else if ( ((LA7_112>='\u0000' && LA7_112<='\t')||(LA7_112>='\u000B' && LA7_112<='\f')||(LA7_112>='\u000E' && LA7_112<='!')||(LA7_112>='#' && LA7_112<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA7_27 = input.LA(1);

                        s = -1;
                        if ( (LA7_27=='H') ) {s = 51;}

                        else if ( ((LA7_27>='\u0000' && LA7_27<='\t')||(LA7_27>='\u000B' && LA7_27<='\f')||(LA7_27>='\u000E' && LA7_27<='G')||(LA7_27>='I' && LA7_27<='\uFFFF')) ) {s = 52;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 7, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}