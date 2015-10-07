// $ANTLR 3.2 Sep 23, 2009 12:02:23 src/LCPLTreeBuilder.g 2013-11-19 14:14:39

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class LCPLTreeBuilderParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PROGRAM", "NEW", "ARROW", "CLASS", "INHERITS", "END", "VOID", "SELF", "VAR", "LOCAL", "LOOP", "COLON", "ADDITION", "MINUS", "MULTIPLY", "DIVIDE", "LESSTHAN", "LESSTHANEQUAL", "EQUAL", "NOT", "CLASSDEFS", "FEATURES", "DECLARATIONS", "DECLARATION", "METHOD", "METHODEXPRESSION", "METHODNAME", "FORMALPARAM", "LOCALDEFS", "LOCALDEF", "DISPATCH", "SYMBOL", "ATOM", "STATEMENT", "ID", "INT", "ASSIGNMENT", "NAME", "STRING_CONST", "IDENTIFIER", "PARENT", "INTEGER", "STRINGCONST", "VOIDKWRD", "SELFKWRD", "SUBSTR", "INDEX", "STATICDISPATCH", "PARAMCALLS", "CAST", "WHILEBODY", "IF", "THEN", "ELSE", "WHILE", "WHILEST", "CONDITION", "IFBODY", "ELSEBODY", "IFST", "STRING", "LINE_COMMENT", "WS", "';'", "','", "'['", "'.'", "']'", "'('", "')'", "'{'", "'}'"
    };
    public static final int IFBODY=61;
    public static final int CAST=53;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int CLASS=7;
    public static final int T__67=67;
    public static final int ADDITION=16;
    public static final int WHILE=58;
    public static final int FORMALPARAM=31;
    public static final int DISPATCH=34;
    public static final int NEW=5;
    public static final int CONDITION=60;
    public static final int NOT=23;
    public static final int METHODEXPRESSION=29;
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
    public static final int LINE_COMMENT=65;
    public static final int FEATURES=25;
    public static final int ELSE=57;
    public static final int VOIDKWRD=47;
    public static final int DECLARATIONS=26;
    public static final int INT=39;
    public static final int CLASSDEFS=24;
    public static final int SELFKWRD=48;
    public static final int LOCAL=13;
    public static final int MINUS=17;
    public static final int COLON=15;
    public static final int LOCALDEF=33;
    public static final int WS=66;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int WHILEST=59;
    public static final int T__70=70;
    public static final int LOCALDEFS=32;
    public static final int ARROW=6;
    public static final int PROGRAM=4;
    public static final int ASSIGNMENT=40;
    public static final int METHODNAME=30;
    public static final int END=9;
    public static final int T__75=75;
    public static final int T__74=74;
    public static final int SELF=11;
    public static final int T__73=73;
    public static final int METHOD=28;
    public static final int STRING=64;

    // delegates
    // delegators


        public LCPLTreeBuilderParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public LCPLTreeBuilderParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
            this.state.ruleMemo = new HashMap[40+1];
             
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return LCPLTreeBuilderParser.tokenNames; }
    public String getGrammarFileName() { return "src/LCPLTreeBuilder.g"; }



        //functions that validate if a token is a 
        //data type or a variable name
        boolean isTypeID(String s)
        {

            return Character.isUpperCase(s.charAt(0));

        }

        boolean isVarNameID(String s)

        {

             return !(Character.isUpperCase(s.charAt(0)));

        } 




    public static class program_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // src/LCPLTreeBuilder.g:112:1: program : ( classdef )* -> ^( PROGRAM ^( CLASSDEFS ( classdef )* ) ) ;
    public final LCPLTreeBuilderParser.program_return program() throws RecognitionException {
        LCPLTreeBuilderParser.program_return retval = new LCPLTreeBuilderParser.program_return();
        retval.start = input.LT(1);
        int program_StartIndex = input.index();
        CommonTree root_0 = null;

        LCPLTreeBuilderParser.classdef_return classdef1 = null;


        RewriteRuleSubtreeStream stream_classdef=new RewriteRuleSubtreeStream(adaptor,"rule classdef");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return retval; }
            // src/LCPLTreeBuilder.g:112:9: ( ( classdef )* -> ^( PROGRAM ^( CLASSDEFS ( classdef )* ) ) )
            // src/LCPLTreeBuilder.g:112:13: ( classdef )*
            {
            // src/LCPLTreeBuilder.g:112:13: ( classdef )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==CLASS) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:112:14: classdef
            	    {
            	    pushFollow(FOLLOW_classdef_in_program589);
            	    classdef1=classdef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_classdef.add(classdef1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);



            // AST REWRITE
            // elements: classdef
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 112:25: -> ^( PROGRAM ^( CLASSDEFS ( classdef )* ) )
            {
                // src/LCPLTreeBuilder.g:112:28: ^( PROGRAM ^( CLASSDEFS ( classdef )* ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PROGRAM, "PROGRAM"), root_1);

                // src/LCPLTreeBuilder.g:112:38: ^( CLASSDEFS ( classdef )* )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CLASSDEFS, "CLASSDEFS"), root_2);

                // src/LCPLTreeBuilder.g:112:50: ( classdef )*
                while ( stream_classdef.hasNext() ) {
                    adaptor.addChild(root_2, stream_classdef.nextTree());

                }
                stream_classdef.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 1, program_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "program"

    public static class classdef_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "classdef"
    // src/LCPLTreeBuilder.g:117:1: classdef : CLASS name= ID ( INHERITS parent= ID )? ( feature )* fin1= END ';' -> ^( CLASS ^( NAME $name) ^( PARENT ( $parent)? ) ^( FEATURES ( feature )* ) ) ;
    public final LCPLTreeBuilderParser.classdef_return classdef() throws RecognitionException {
        LCPLTreeBuilderParser.classdef_return retval = new LCPLTreeBuilderParser.classdef_return();
        retval.start = input.LT(1);
        int classdef_StartIndex = input.index();
        CommonTree root_0 = null;

        Token name=null;
        Token parent=null;
        Token fin1=null;
        Token CLASS2=null;
        Token INHERITS3=null;
        Token char_literal5=null;
        LCPLTreeBuilderParser.feature_return feature4 = null;


        CommonTree name_tree=null;
        CommonTree parent_tree=null;
        CommonTree fin1_tree=null;
        CommonTree CLASS2_tree=null;
        CommonTree INHERITS3_tree=null;
        CommonTree char_literal5_tree=null;
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_CLASS=new RewriteRuleTokenStream(adaptor,"token CLASS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_INHERITS=new RewriteRuleTokenStream(adaptor,"token INHERITS");
        RewriteRuleTokenStream stream_END=new RewriteRuleTokenStream(adaptor,"token END");
        RewriteRuleSubtreeStream stream_feature=new RewriteRuleSubtreeStream(adaptor,"rule feature");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return retval; }
            // src/LCPLTreeBuilder.g:117:10: ( CLASS name= ID ( INHERITS parent= ID )? ( feature )* fin1= END ';' -> ^( CLASS ^( NAME $name) ^( PARENT ( $parent)? ) ^( FEATURES ( feature )* ) ) )
            // src/LCPLTreeBuilder.g:119:8: CLASS name= ID ( INHERITS parent= ID )? ( feature )* fin1= END ';'
            {
            CLASS2=(Token)match(input,CLASS,FOLLOW_CLASS_in_classdef633); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CLASS.add(CLASS2);

            name=(Token)match(input,ID,FOLLOW_ID_in_classdef637); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            // src/LCPLTreeBuilder.g:119:22: ( INHERITS parent= ID )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==INHERITS) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // src/LCPLTreeBuilder.g:119:23: INHERITS parent= ID
                    {
                    INHERITS3=(Token)match(input,INHERITS,FOLLOW_INHERITS_in_classdef640); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INHERITS.add(INHERITS3);

                    parent=(Token)match(input,ID,FOLLOW_ID_in_classdef644); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(parent);


                    }
                    break;

            }

            // src/LCPLTreeBuilder.g:119:46: ( feature )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==VAR||LA3_0==ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:119:47: feature
            	    {
            	    pushFollow(FOLLOW_feature_in_classdef651);
            	    feature4=feature();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_feature.add(feature4.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            fin1=(Token)match(input,END,FOLLOW_END_in_classdef657); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_END.add(fin1);

            char_literal5=(Token)match(input,67,FOLLOW_67_in_classdef659); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_67.add(char_literal5);



            // AST REWRITE
            // elements: parent, feature, name, CLASS
            // token labels: name, parent
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
            RewriteRuleTokenStream stream_parent=new RewriteRuleTokenStream(adaptor,"token parent",parent);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 119:69: -> ^( CLASS ^( NAME $name) ^( PARENT ( $parent)? ) ^( FEATURES ( feature )* ) )
            {
                // src/LCPLTreeBuilder.g:120:9: ^( CLASS ^( NAME $name) ^( PARENT ( $parent)? ) ^( FEATURES ( feature )* ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_CLASS.nextNode(), root_1);

                // src/LCPLTreeBuilder.g:120:17: ^( NAME $name)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NAME, "NAME"), root_2);

                adaptor.addChild(root_2, stream_name.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // src/LCPLTreeBuilder.g:120:31: ^( PARENT ( $parent)? )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARENT, "PARENT"), root_2);

                // src/LCPLTreeBuilder.g:120:40: ( $parent)?
                if ( stream_parent.hasNext() ) {
                    adaptor.addChild(root_2, stream_parent.nextNode());

                }
                stream_parent.reset();

                adaptor.addChild(root_1, root_2);
                }
                // src/LCPLTreeBuilder.g:120:52: ^( FEATURES ( feature )* )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FEATURES, "FEATURES"), root_2);

                // src/LCPLTreeBuilder.g:120:63: ( feature )*
                while ( stream_feature.hasNext() ) {
                    adaptor.addChild(root_2, stream_feature.nextTree());

                }
                stream_feature.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 2, classdef_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "classdef"

    public static class feature_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "feature"
    // src/LCPLTreeBuilder.g:125:1: feature : ( attributeblock | method );
    public final LCPLTreeBuilderParser.feature_return feature() throws RecognitionException {
        LCPLTreeBuilderParser.feature_return retval = new LCPLTreeBuilderParser.feature_return();
        retval.start = input.LT(1);
        int feature_StartIndex = input.index();
        CommonTree root_0 = null;

        LCPLTreeBuilderParser.attributeblock_return attributeblock6 = null;

        LCPLTreeBuilderParser.method_return method7 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return retval; }
            // src/LCPLTreeBuilder.g:125:8: ( attributeblock | method )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==VAR) ) {
                alt4=1;
            }
            else if ( (LA4_0==ID) ) {
                alt4=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // src/LCPLTreeBuilder.g:126:5: attributeblock
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_attributeblock_in_feature720);
                    attributeblock6=attributeblock();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attributeblock6.getTree());

                    }
                    break;
                case 2 :
                    // src/LCPLTreeBuilder.g:127:6: method
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_method_in_feature727);
                    method7=method();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, method7.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 3, feature_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "feature"

    public static class attributeblock_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "attributeblock"
    // src/LCPLTreeBuilder.g:131:1: attributeblock : start= VAR ( declaration )* END ';' -> ^( DECLARATIONS ( declaration )* ) ;
    public final LCPLTreeBuilderParser.attributeblock_return attributeblock() throws RecognitionException {
        LCPLTreeBuilderParser.attributeblock_return retval = new LCPLTreeBuilderParser.attributeblock_return();
        retval.start = input.LT(1);
        int attributeblock_StartIndex = input.index();
        CommonTree root_0 = null;

        Token start=null;
        Token END9=null;
        Token char_literal10=null;
        LCPLTreeBuilderParser.declaration_return declaration8 = null;


        CommonTree start_tree=null;
        CommonTree END9_tree=null;
        CommonTree char_literal10_tree=null;
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_VAR=new RewriteRuleTokenStream(adaptor,"token VAR");
        RewriteRuleTokenStream stream_END=new RewriteRuleTokenStream(adaptor,"token END");
        RewriteRuleSubtreeStream stream_declaration=new RewriteRuleSubtreeStream(adaptor,"rule declaration");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return retval; }
            // src/LCPLTreeBuilder.g:131:15: (start= VAR ( declaration )* END ';' -> ^( DECLARATIONS ( declaration )* ) )
            // src/LCPLTreeBuilder.g:132:5: start= VAR ( declaration )* END ';'
            {
            start=(Token)match(input,VAR,FOLLOW_VAR_in_attributeblock749); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_VAR.add(start);

            // src/LCPLTreeBuilder.g:132:15: ( declaration )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==ID) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:132:16: declaration
            	    {
            	    pushFollow(FOLLOW_declaration_in_attributeblock752);
            	    declaration8=declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_declaration.add(declaration8.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            END9=(Token)match(input,END,FOLLOW_END_in_attributeblock756); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_END.add(END9);

            char_literal10=(Token)match(input,67,FOLLOW_67_in_attributeblock758); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_67.add(char_literal10);



            // AST REWRITE
            // elements: declaration
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 133:5: -> ^( DECLARATIONS ( declaration )* )
            {
                // src/LCPLTreeBuilder.g:133:8: ^( DECLARATIONS ( declaration )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(DECLARATIONS, "DECLARATIONS"), root_1);

                // src/LCPLTreeBuilder.g:133:24: ( declaration )*
                while ( stream_declaration.hasNext() ) {
                    adaptor.addChild(root_1, stream_declaration.nextTree());

                }
                stream_declaration.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 4, attributeblock_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "attributeblock"

    public static class localdefblock_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "localdefblock"
    // src/LCPLTreeBuilder.g:137:1: localdefblock : start= LOCAL ( localdef )* END -> ^( LOCALDEFS[$start] ( localdef )* ) ;
    public final LCPLTreeBuilderParser.localdefblock_return localdefblock() throws RecognitionException {
        LCPLTreeBuilderParser.localdefblock_return retval = new LCPLTreeBuilderParser.localdefblock_return();
        retval.start = input.LT(1);
        int localdefblock_StartIndex = input.index();
        CommonTree root_0 = null;

        Token start=null;
        Token END12=null;
        LCPLTreeBuilderParser.localdef_return localdef11 = null;


        CommonTree start_tree=null;
        CommonTree END12_tree=null;
        RewriteRuleTokenStream stream_LOCAL=new RewriteRuleTokenStream(adaptor,"token LOCAL");
        RewriteRuleTokenStream stream_END=new RewriteRuleTokenStream(adaptor,"token END");
        RewriteRuleSubtreeStream stream_localdef=new RewriteRuleSubtreeStream(adaptor,"rule localdef");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return retval; }
            // src/LCPLTreeBuilder.g:137:14: (start= LOCAL ( localdef )* END -> ^( LOCALDEFS[$start] ( localdef )* ) )
            // src/LCPLTreeBuilder.g:138:5: start= LOCAL ( localdef )* END
            {
            start=(Token)match(input,LOCAL,FOLLOW_LOCAL_in_localdefblock796); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LOCAL.add(start);

            // src/LCPLTreeBuilder.g:138:17: ( localdef )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==ID) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:138:18: localdef
            	    {
            	    pushFollow(FOLLOW_localdef_in_localdefblock799);
            	    localdef11=localdef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_localdef.add(localdef11.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            END12=(Token)match(input,END,FOLLOW_END_in_localdefblock803); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_END.add(END12);



            // AST REWRITE
            // elements: localdef
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 139:5: -> ^( LOCALDEFS[$start] ( localdef )* )
            {
                // src/LCPLTreeBuilder.g:139:8: ^( LOCALDEFS[$start] ( localdef )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LOCALDEFS, start), root_1);

                // src/LCPLTreeBuilder.g:139:28: ( localdef )*
                while ( stream_localdef.hasNext() ) {
                    adaptor.addChild(root_1, stream_localdef.nextTree());

                }
                stream_localdef.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 5, localdefblock_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "localdefblock"

    public static class declaration_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "declaration"
    // src/LCPLTreeBuilder.g:144:1: declaration : type= ID name= ID ( '=' )? (value= expression )? ';' -> ^( DECLARATION $type $name ( $value)? ) ;
    public final LCPLTreeBuilderParser.declaration_return declaration() throws RecognitionException {
        LCPLTreeBuilderParser.declaration_return retval = new LCPLTreeBuilderParser.declaration_return();
        retval.start = input.LT(1);
        int declaration_StartIndex = input.index();
        CommonTree root_0 = null;

        Token type=null;
        Token name=null;
        Token char_literal13=null;
        Token char_literal14=null;
        LCPLTreeBuilderParser.expression_return value = null;


        CommonTree type_tree=null;
        CommonTree name_tree=null;
        CommonTree char_literal13_tree=null;
        CommonTree char_literal14_tree=null;
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_ASSIGNMENT=new RewriteRuleTokenStream(adaptor,"token ASSIGNMENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return retval; }
            // src/LCPLTreeBuilder.g:144:12: (type= ID name= ID ( '=' )? (value= expression )? ';' -> ^( DECLARATION $type $name ( $value)? ) )
            // src/LCPLTreeBuilder.g:145:10: type= ID name= ID ( '=' )? (value= expression )? ';'
            {
            type=(Token)match(input,ID,FOLLOW_ID_in_declaration869); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(type);

            name=(Token)match(input,ID,FOLLOW_ID_in_declaration873); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            // src/LCPLTreeBuilder.g:145:26: ( '=' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==ASSIGNMENT) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // src/LCPLTreeBuilder.g:145:27: '='
                    {
                    char_literal13=(Token)match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_declaration876); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ASSIGNMENT.add(char_literal13);


                    }
                    break;

            }

            // src/LCPLTreeBuilder.g:145:33: (value= expression )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==NEW||(LA8_0>=VOID && LA8_0<=SELF)||LA8_0==MINUS||(LA8_0>=ID && LA8_0<=INT)||LA8_0==IF||LA8_0==WHILE||LA8_0==STRING||LA8_0==69||LA8_0==72||LA8_0==74) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // src/LCPLTreeBuilder.g:145:34: value= expression
                    {
                    pushFollow(FOLLOW_expression_in_declaration883);
                    value=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(value.getTree());

                    }
                    break;

            }

            char_literal14=(Token)match(input,67,FOLLOW_67_in_declaration887); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_67.add(char_literal14);



            // AST REWRITE
            // elements: type, name, value
            // token labels: name, type
            // rule labels: retval, value
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
            RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 147:9: -> ^( DECLARATION $type $name ( $value)? )
            {
                // src/LCPLTreeBuilder.g:147:12: ^( DECLARATION $type $name ( $value)? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(DECLARATION, "DECLARATION"), root_1);

                adaptor.addChild(root_1, stream_type.nextNode());
                adaptor.addChild(root_1, stream_name.nextNode());
                // src/LCPLTreeBuilder.g:147:39: ( $value)?
                if ( stream_value.hasNext() ) {
                    adaptor.addChild(root_1, stream_value.nextTree());

                }
                stream_value.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 6, declaration_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "declaration"

    public static class method_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "method"
    // src/LCPLTreeBuilder.g:152:1: method : name= ID ( formalparam )* ( ARROW )? (rettype= ID )? start= COLON ( methodexpression ';' )* fin= END ';' -> ^( METHOD $start $name ^( FORMALPARAM ( formalparam )* ) ( $rettype)? ^( METHODEXPRESSION ( methodexpression )* ) ) ;
    public final LCPLTreeBuilderParser.method_return method() throws RecognitionException {
        LCPLTreeBuilderParser.method_return retval = new LCPLTreeBuilderParser.method_return();
        retval.start = input.LT(1);
        int method_StartIndex = input.index();
        CommonTree root_0 = null;

        Token name=null;
        Token rettype=null;
        Token start=null;
        Token fin=null;
        Token ARROW16=null;
        Token char_literal18=null;
        Token char_literal19=null;
        LCPLTreeBuilderParser.formalparam_return formalparam15 = null;

        LCPLTreeBuilderParser.methodexpression_return methodexpression17 = null;


        CommonTree name_tree=null;
        CommonTree rettype_tree=null;
        CommonTree start_tree=null;
        CommonTree fin_tree=null;
        CommonTree ARROW16_tree=null;
        CommonTree char_literal18_tree=null;
        CommonTree char_literal19_tree=null;
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_END=new RewriteRuleTokenStream(adaptor,"token END");
        RewriteRuleSubtreeStream stream_formalparam=new RewriteRuleSubtreeStream(adaptor,"rule formalparam");
        RewriteRuleSubtreeStream stream_methodexpression=new RewriteRuleSubtreeStream(adaptor,"rule methodexpression");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return retval; }
            // src/LCPLTreeBuilder.g:152:9: (name= ID ( formalparam )* ( ARROW )? (rettype= ID )? start= COLON ( methodexpression ';' )* fin= END ';' -> ^( METHOD $start $name ^( FORMALPARAM ( formalparam )* ) ( $rettype)? ^( METHODEXPRESSION ( methodexpression )* ) ) )
            // src/LCPLTreeBuilder.g:153:10: name= ID ( formalparam )* ( ARROW )? (rettype= ID )? start= COLON ( methodexpression ';' )* fin= END ';'
            {
            name=(Token)match(input,ID,FOLLOW_ID_in_method952); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            // src/LCPLTreeBuilder.g:153:20: ( formalparam )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==ID) ) {
                    int LA9_2 = input.LA(2);

                    if ( (LA9_2==ID) ) {
                        alt9=1;
                    }


                }
                else if ( (LA9_0==68) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:153:22: formalparam
            	    {
            	    pushFollow(FOLLOW_formalparam_in_method958);
            	    formalparam15=formalparam();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_formalparam.add(formalparam15.getTree());

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            // src/LCPLTreeBuilder.g:153:37: ( ARROW )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==ARROW) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // src/LCPLTreeBuilder.g:153:38: ARROW
                    {
                    ARROW16=(Token)match(input,ARROW,FOLLOW_ARROW_in_method964); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW16);


                    }
                    break;

            }

            // src/LCPLTreeBuilder.g:153:46: (rettype= ID )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==ID) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // src/LCPLTreeBuilder.g:153:47: rettype= ID
                    {
                    rettype=(Token)match(input,ID,FOLLOW_ID_in_method971); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(rettype);


                    }
                    break;

            }

            start=(Token)match(input,COLON,FOLLOW_COLON_in_method977); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(start);

            // src/LCPLTreeBuilder.g:153:72: ( methodexpression ';' )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==NEW||(LA12_0>=VOID && LA12_0<=SELF)||LA12_0==LOCAL||LA12_0==MINUS||LA12_0==NOT||(LA12_0>=ID && LA12_0<=INT)||LA12_0==IF||LA12_0==WHILE||LA12_0==STRING||LA12_0==69||LA12_0==72||LA12_0==74) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:153:73: methodexpression ';'
            	    {
            	    pushFollow(FOLLOW_methodexpression_in_method980);
            	    methodexpression17=methodexpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_methodexpression.add(methodexpression17.getTree());
            	    char_literal18=(Token)match(input,67,FOLLOW_67_in_method982); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_67.add(char_literal18);


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            fin=(Token)match(input,END,FOLLOW_END_in_method989); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_END.add(fin);

            char_literal19=(Token)match(input,67,FOLLOW_67_in_method991); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_67.add(char_literal19);



            // AST REWRITE
            // elements: formalparam, start, name, rettype, methodexpression
            // token labels: start, name, rettype
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_start=new RewriteRuleTokenStream(adaptor,"token start",start);
            RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
            RewriteRuleTokenStream stream_rettype=new RewriteRuleTokenStream(adaptor,"token rettype",rettype);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 154:9: -> ^( METHOD $start $name ^( FORMALPARAM ( formalparam )* ) ( $rettype)? ^( METHODEXPRESSION ( methodexpression )* ) )
            {
                // src/LCPLTreeBuilder.g:154:12: ^( METHOD $start $name ^( FORMALPARAM ( formalparam )* ) ( $rettype)? ^( METHODEXPRESSION ( methodexpression )* ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD, "METHOD"), root_1);

                adaptor.addChild(root_1, stream_start.nextNode());
                adaptor.addChild(root_1, stream_name.nextNode());
                // src/LCPLTreeBuilder.g:154:34: ^( FORMALPARAM ( formalparam )* )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FORMALPARAM, "FORMALPARAM"), root_2);

                // src/LCPLTreeBuilder.g:154:49: ( formalparam )*
                while ( stream_formalparam.hasNext() ) {
                    adaptor.addChild(root_2, stream_formalparam.nextTree());

                }
                stream_formalparam.reset();

                adaptor.addChild(root_1, root_2);
                }
                // src/LCPLTreeBuilder.g:154:64: ( $rettype)?
                if ( stream_rettype.hasNext() ) {
                    adaptor.addChild(root_1, stream_rettype.nextNode());

                }
                stream_rettype.reset();
                // src/LCPLTreeBuilder.g:154:74: ^( METHODEXPRESSION ( methodexpression )* )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHODEXPRESSION, "METHODEXPRESSION"), root_2);

                // src/LCPLTreeBuilder.g:154:93: ( methodexpression )*
                while ( stream_methodexpression.hasNext() ) {
                    adaptor.addChild(root_2, stream_methodexpression.nextTree());

                }
                stream_methodexpression.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 7, method_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "method"

    public static class formalparam_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "formalparam"
    // src/LCPLTreeBuilder.g:158:1: formalparam : ( ',' )? type= ID {...}?var= ID {...}? -> ^( FORMALPARAM $type $var) ;
    public final LCPLTreeBuilderParser.formalparam_return formalparam() throws RecognitionException {
        LCPLTreeBuilderParser.formalparam_return retval = new LCPLTreeBuilderParser.formalparam_return();
        retval.start = input.LT(1);
        int formalparam_StartIndex = input.index();
        CommonTree root_0 = null;

        Token type=null;
        Token var=null;
        Token char_literal20=null;

        CommonTree type_tree=null;
        CommonTree var_tree=null;
        CommonTree char_literal20_tree=null;
        RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return retval; }
            // src/LCPLTreeBuilder.g:158:12: ( ( ',' )? type= ID {...}?var= ID {...}? -> ^( FORMALPARAM $type $var) )
            // src/LCPLTreeBuilder.g:159:8: ( ',' )? type= ID {...}?var= ID {...}?
            {
            // src/LCPLTreeBuilder.g:159:8: ( ',' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==68) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // src/LCPLTreeBuilder.g:159:8: ','
                    {
                    char_literal20=(Token)match(input,68,FOLLOW_68_in_formalparam1066); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_68.add(char_literal20);


                    }
                    break;

            }

            type=(Token)match(input,ID,FOLLOW_ID_in_formalparam1073); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(type);

            if ( !((isTypeID((type!=null?type.getText():null)))) ) {
                if (state.backtracking>0) {state.failed=true; return retval;}
                throw new FailedPredicateException(input, "formalparam", "isTypeID($type.text)");
            }
            var=(Token)match(input,ID,FOLLOW_ID_in_formalparam1083); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(var);

            if ( !((isVarNameID((var!=null?var.getText():null)))) ) {
                if (state.backtracking>0) {state.failed=true; return retval;}
                throw new FailedPredicateException(input, "formalparam", "isVarNameID($var.text)");
            }


            // AST REWRITE
            // elements: var, type
            // token labels: var, type
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_var=new RewriteRuleTokenStream(adaptor,"token var",var);
            RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 160:8: -> ^( FORMALPARAM $type $var)
            {
                // src/LCPLTreeBuilder.g:160:11: ^( FORMALPARAM $type $var)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FORMALPARAM, "FORMALPARAM"), root_1);

                adaptor.addChild(root_1, stream_type.nextNode());
                adaptor.addChild(root_1, stream_var.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 8, formalparam_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "formalparam"

    public static class methodexpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "methodexpression"
    // src/LCPLTreeBuilder.g:165:1: methodexpression : ( generalexpression | localdefblock );
    public final LCPLTreeBuilderParser.methodexpression_return methodexpression() throws RecognitionException {
        LCPLTreeBuilderParser.methodexpression_return retval = new LCPLTreeBuilderParser.methodexpression_return();
        retval.start = input.LT(1);
        int methodexpression_StartIndex = input.index();
        CommonTree root_0 = null;

        LCPLTreeBuilderParser.generalexpression_return generalexpression21 = null;

        LCPLTreeBuilderParser.localdefblock_return localdefblock22 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return retval; }
            // src/LCPLTreeBuilder.g:165:17: ( generalexpression | localdefblock )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==NEW||(LA14_0>=VOID && LA14_0<=SELF)||LA14_0==MINUS||LA14_0==NOT||(LA14_0>=ID && LA14_0<=INT)||LA14_0==IF||LA14_0==WHILE||LA14_0==STRING||LA14_0==69||LA14_0==72||LA14_0==74) ) {
                alt14=1;
            }
            else if ( (LA14_0==LOCAL) ) {
                alt14=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // src/LCPLTreeBuilder.g:166:5: generalexpression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_generalexpression_in_methodexpression1135);
                    generalexpression21=generalexpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, generalexpression21.getTree());

                    }
                    break;
                case 2 :
                    // src/LCPLTreeBuilder.g:167:6: localdefblock
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_localdefblock_in_methodexpression1143);
                    localdefblock22=localdefblock();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, localdefblock22.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 9, methodexpression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "methodexpression"

    public static class dispatch_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "dispatch"
    // src/LCPLTreeBuilder.g:172:1: dispatch : '[' (object= objectdispatch '.' )? name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']' -> ^( DISPATCH ( $object)? $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) ) ;
    public final LCPLTreeBuilderParser.dispatch_return dispatch() throws RecognitionException {
        LCPLTreeBuilderParser.dispatch_return retval = new LCPLTreeBuilderParser.dispatch_return();
        retval.start = input.LT(1);
        int dispatch_StartIndex = input.index();
        CommonTree root_0 = null;

        Token name=null;
        Token char_literal23=null;
        Token char_literal24=null;
        Token char_literal25=null;
        Token char_literal26=null;
        List list_paramcall2=null;
        LCPLTreeBuilderParser.objectdispatch_return object = null;

        LCPLTreeBuilderParser.paramcall_return paramcall1 = null;

        RuleReturnScope paramcall2 = null;
        CommonTree name_tree=null;
        CommonTree char_literal23_tree=null;
        CommonTree char_literal24_tree=null;
        CommonTree char_literal25_tree=null;
        CommonTree char_literal26_tree=null;
        RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
        RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_70=new RewriteRuleTokenStream(adaptor,"token 70");
        RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
        RewriteRuleSubtreeStream stream_paramcall=new RewriteRuleSubtreeStream(adaptor,"rule paramcall");
        RewriteRuleSubtreeStream stream_objectdispatch=new RewriteRuleSubtreeStream(adaptor,"rule objectdispatch");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return retval; }
            // src/LCPLTreeBuilder.g:172:9: ( '[' (object= objectdispatch '.' )? name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']' -> ^( DISPATCH ( $object)? $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) ) )
            // src/LCPLTreeBuilder.g:173:5: '[' (object= objectdispatch '.' )? name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']'
            {
            char_literal23=(Token)match(input,69,FOLLOW_69_in_dispatch1162); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_69.add(char_literal23);

            // src/LCPLTreeBuilder.g:173:9: (object= objectdispatch '.' )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==ID) ) {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==70) ) {
                    alt15=1;
                }
            }
            else if ( (LA15_0==SELF||LA15_0==STRING||LA15_0==69||LA15_0==72||LA15_0==74) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // src/LCPLTreeBuilder.g:173:10: object= objectdispatch '.'
                    {
                    pushFollow(FOLLOW_objectdispatch_in_dispatch1167);
                    object=objectdispatch();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_objectdispatch.add(object.getTree());
                    char_literal24=(Token)match(input,70,FOLLOW_70_in_dispatch1169); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_70.add(char_literal24);


                    }
                    break;

            }

            name=(Token)match(input,ID,FOLLOW_ID_in_dispatch1175); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            // src/LCPLTreeBuilder.g:173:47: (paramcall1= paramcall )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==NEW||(LA16_0>=VOID && LA16_0<=SELF)||LA16_0==MINUS||LA16_0==NOT||(LA16_0>=ID && LA16_0<=INT)||LA16_0==IF||LA16_0==WHILE||LA16_0==STRING||LA16_0==69||LA16_0==72||LA16_0==74) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // src/LCPLTreeBuilder.g:173:48: paramcall1= paramcall
                    {
                    pushFollow(FOLLOW_paramcall_in_dispatch1181);
                    paramcall1=paramcall();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_paramcall.add(paramcall1.getTree());

                    }
                    break;

            }

            // src/LCPLTreeBuilder.g:173:71: ( ',' paramcall2+= paramcall )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==68) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:173:72: ',' paramcall2+= paramcall
            	    {
            	    char_literal25=(Token)match(input,68,FOLLOW_68_in_dispatch1186); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_68.add(char_literal25);

            	    pushFollow(FOLLOW_paramcall_in_dispatch1190);
            	    paramcall2=paramcall();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_paramcall.add(paramcall2.getTree());
            	    if (list_paramcall2==null) list_paramcall2=new ArrayList();
            	    list_paramcall2.add(paramcall2.getTree());


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            char_literal26=(Token)match(input,71,FOLLOW_71_in_dispatch1193); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_71.add(char_literal26);



            // AST REWRITE
            // elements: paramcall2, name, paramcall1, object
            // token labels: name
            // rule labels: retval, paramcall1, object
            // token list labels: 
            // rule list labels: paramcall2
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_paramcall1=new RewriteRuleSubtreeStream(adaptor,"rule paramcall1",paramcall1!=null?paramcall1.tree:null);
            RewriteRuleSubtreeStream stream_object=new RewriteRuleSubtreeStream(adaptor,"rule object",object!=null?object.tree:null);
            RewriteRuleSubtreeStream stream_paramcall2=new RewriteRuleSubtreeStream(adaptor,"token paramcall2",list_paramcall2);
            root_0 = (CommonTree)adaptor.nil();
            // 174:6: -> ^( DISPATCH ( $object)? $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) )
            {
                // src/LCPLTreeBuilder.g:174:9: ^( DISPATCH ( $object)? $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(DISPATCH, "DISPATCH"), root_1);

                // src/LCPLTreeBuilder.g:174:20: ( $object)?
                if ( stream_object.hasNext() ) {
                    adaptor.addChild(root_1, stream_object.nextTree());

                }
                stream_object.reset();
                adaptor.addChild(root_1, stream_name.nextNode());
                // src/LCPLTreeBuilder.g:174:36: ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARAMCALLS, "PARAMCALLS"), root_2);

                // src/LCPLTreeBuilder.g:174:49: ( $paramcall1)?
                if ( stream_paramcall1.hasNext() ) {
                    adaptor.addChild(root_2, stream_paramcall1.nextTree());

                }
                stream_paramcall1.reset();
                // src/LCPLTreeBuilder.g:174:62: ( $paramcall2)*
                while ( stream_paramcall2.hasNext() ) {
                    adaptor.addChild(root_2, stream_paramcall2.nextTree());

                }
                stream_paramcall2.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 10, dispatch_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "dispatch"

    public static class objectdispatch_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "objectdispatch"
    // src/LCPLTreeBuilder.g:177:1: objectdispatch : (arg= ID -> ^( ATOM ^( IDENTIFIER $arg) ) | '(' NEW arg2= ID ')' -> ^( NEW $arg2) | arg3= SELF -> ^( ATOM ^( SELFKWRD $arg3) ) | (arg= STRING ) -> ^( ATOM ^( STRINGCONST $arg) ) | dispatch | staticdispatch | cast );
    public final LCPLTreeBuilderParser.objectdispatch_return objectdispatch() throws RecognitionException {
        LCPLTreeBuilderParser.objectdispatch_return retval = new LCPLTreeBuilderParser.objectdispatch_return();
        retval.start = input.LT(1);
        int objectdispatch_StartIndex = input.index();
        CommonTree root_0 = null;

        Token arg=null;
        Token arg2=null;
        Token arg3=null;
        Token char_literal27=null;
        Token NEW28=null;
        Token char_literal29=null;
        LCPLTreeBuilderParser.dispatch_return dispatch30 = null;

        LCPLTreeBuilderParser.staticdispatch_return staticdispatch31 = null;

        LCPLTreeBuilderParser.cast_return cast32 = null;


        CommonTree arg_tree=null;
        CommonTree arg2_tree=null;
        CommonTree arg3_tree=null;
        CommonTree char_literal27_tree=null;
        CommonTree NEW28_tree=null;
        CommonTree char_literal29_tree=null;
        RewriteRuleTokenStream stream_NEW=new RewriteRuleTokenStream(adaptor,"token NEW");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_SELF=new RewriteRuleTokenStream(adaptor,"token SELF");
        RewriteRuleTokenStream stream_72=new RewriteRuleTokenStream(adaptor,"token 72");
        RewriteRuleTokenStream stream_73=new RewriteRuleTokenStream(adaptor,"token 73");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return retval; }
            // src/LCPLTreeBuilder.g:177:15: (arg= ID -> ^( ATOM ^( IDENTIFIER $arg) ) | '(' NEW arg2= ID ')' -> ^( NEW $arg2) | arg3= SELF -> ^( ATOM ^( SELFKWRD $arg3) ) | (arg= STRING ) -> ^( ATOM ^( STRINGCONST $arg) ) | dispatch | staticdispatch | cast )
            int alt18=7;
            alt18 = dfa18.predict(input);
            switch (alt18) {
                case 1 :
                    // src/LCPLTreeBuilder.g:178:5: arg= ID
                    {
                    arg=(Token)match(input,ID,FOLLOW_ID_in_objectdispatch1242); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(arg);



                    // AST REWRITE
                    // elements: arg
                    // token labels: arg
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg=new RewriteRuleTokenStream(adaptor,"token arg",arg);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 179:5: -> ^( ATOM ^( IDENTIFIER $arg) )
                    {
                        // src/LCPLTreeBuilder.g:179:8: ^( ATOM ^( IDENTIFIER $arg) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:179:15: ^( IDENTIFIER $arg)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IDENTIFIER, "IDENTIFIER"), root_2);

                        adaptor.addChild(root_2, stream_arg.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // src/LCPLTreeBuilder.g:180:6: '(' NEW arg2= ID ')'
                    {
                    char_literal27=(Token)match(input,72,FOLLOW_72_in_objectdispatch1266); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_72.add(char_literal27);

                    NEW28=(Token)match(input,NEW,FOLLOW_NEW_in_objectdispatch1268); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEW.add(NEW28);

                    arg2=(Token)match(input,ID,FOLLOW_ID_in_objectdispatch1273); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(arg2);

                    char_literal29=(Token)match(input,73,FOLLOW_73_in_objectdispatch1275); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_73.add(char_literal29);



                    // AST REWRITE
                    // elements: NEW, arg2
                    // token labels: arg2
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg2=new RewriteRuleTokenStream(adaptor,"token arg2",arg2);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 181:5: -> ^( NEW $arg2)
                    {
                        // src/LCPLTreeBuilder.g:181:7: ^( NEW $arg2)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEW.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_arg2.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // src/LCPLTreeBuilder.g:182:6: arg3= SELF
                    {
                    arg3=(Token)match(input,SELF,FOLLOW_SELF_in_objectdispatch1296); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SELF.add(arg3);



                    // AST REWRITE
                    // elements: arg3
                    // token labels: arg3
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg3=new RewriteRuleTokenStream(adaptor,"token arg3",arg3);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 183:5: -> ^( ATOM ^( SELFKWRD $arg3) )
                    {
                        // src/LCPLTreeBuilder.g:183:8: ^( ATOM ^( SELFKWRD $arg3) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:183:15: ^( SELFKWRD $arg3)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SELFKWRD, "SELFKWRD"), root_2);

                        adaptor.addChild(root_2, stream_arg3.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // src/LCPLTreeBuilder.g:184:6: (arg= STRING )
                    {
                    // src/LCPLTreeBuilder.g:184:6: (arg= STRING )
                    // src/LCPLTreeBuilder.g:184:7: arg= STRING
                    {
                    arg=(Token)match(input,STRING,FOLLOW_STRING_in_objectdispatch1323); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STRING.add(arg);


                    }



                    // AST REWRITE
                    // elements: arg
                    // token labels: arg
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg=new RewriteRuleTokenStream(adaptor,"token arg",arg);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 185:5: -> ^( ATOM ^( STRINGCONST $arg) )
                    {
                        // src/LCPLTreeBuilder.g:185:8: ^( ATOM ^( STRINGCONST $arg) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:185:15: ^( STRINGCONST $arg)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRINGCONST, "STRINGCONST"), root_2);

                        adaptor.addChild(root_2, stream_arg.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // src/LCPLTreeBuilder.g:186:6: dispatch
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_dispatch_in_objectdispatch1361);
                    dispatch30=dispatch();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, dispatch30.getTree());

                    }
                    break;
                case 6 :
                    // src/LCPLTreeBuilder.g:187:6: staticdispatch
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_staticdispatch_in_objectdispatch1368);
                    staticdispatch31=staticdispatch();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, staticdispatch31.getTree());

                    }
                    break;
                case 7 :
                    // src/LCPLTreeBuilder.g:188:6: cast
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_cast_in_objectdispatch1375);
                    cast32=cast();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, cast32.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 11, objectdispatch_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "objectdispatch"

    public static class staticdispatch_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "staticdispatch"
    // src/LCPLTreeBuilder.g:192:1: staticdispatch : ( '[' object= ID '.' type= ID '.' name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']' -> ^( STATICDISPATCH ^( ATOM ^( IDENTIFIER $object) ) $type $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) ) | '[' object2= SELF '.' type= ID '.' name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']' -> ^( STATICDISPATCH ^( ATOM ^( SELFKWRD $object2) ) $type $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) ) );
    public final LCPLTreeBuilderParser.staticdispatch_return staticdispatch() throws RecognitionException {
        LCPLTreeBuilderParser.staticdispatch_return retval = new LCPLTreeBuilderParser.staticdispatch_return();
        retval.start = input.LT(1);
        int staticdispatch_StartIndex = input.index();
        CommonTree root_0 = null;

        Token object=null;
        Token type=null;
        Token name=null;
        Token object2=null;
        Token char_literal33=null;
        Token char_literal34=null;
        Token char_literal35=null;
        Token char_literal36=null;
        Token char_literal37=null;
        Token char_literal38=null;
        Token char_literal39=null;
        Token char_literal40=null;
        Token char_literal41=null;
        Token char_literal42=null;
        List list_paramcall2=null;
        LCPLTreeBuilderParser.paramcall_return paramcall1 = null;

        RuleReturnScope paramcall2 = null;
        CommonTree object_tree=null;
        CommonTree type_tree=null;
        CommonTree name_tree=null;
        CommonTree object2_tree=null;
        CommonTree char_literal33_tree=null;
        CommonTree char_literal34_tree=null;
        CommonTree char_literal35_tree=null;
        CommonTree char_literal36_tree=null;
        CommonTree char_literal37_tree=null;
        CommonTree char_literal38_tree=null;
        CommonTree char_literal39_tree=null;
        CommonTree char_literal40_tree=null;
        CommonTree char_literal41_tree=null;
        CommonTree char_literal42_tree=null;
        RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
        RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_70=new RewriteRuleTokenStream(adaptor,"token 70");
        RewriteRuleTokenStream stream_SELF=new RewriteRuleTokenStream(adaptor,"token SELF");
        RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
        RewriteRuleSubtreeStream stream_paramcall=new RewriteRuleSubtreeStream(adaptor,"rule paramcall");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return retval; }
            // src/LCPLTreeBuilder.g:192:15: ( '[' object= ID '.' type= ID '.' name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']' -> ^( STATICDISPATCH ^( ATOM ^( IDENTIFIER $object) ) $type $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) ) | '[' object2= SELF '.' type= ID '.' name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']' -> ^( STATICDISPATCH ^( ATOM ^( SELFKWRD $object2) ) $type $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) ) )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==69) ) {
                int LA23_1 = input.LA(2);

                if ( (LA23_1==ID) ) {
                    alt23=1;
                }
                else if ( (LA23_1==SELF) ) {
                    alt23=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 23, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // src/LCPLTreeBuilder.g:193:5: '[' object= ID '.' type= ID '.' name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']'
                    {
                    char_literal33=(Token)match(input,69,FOLLOW_69_in_staticdispatch1398); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_69.add(char_literal33);

                    object=(Token)match(input,ID,FOLLOW_ID_in_staticdispatch1402); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(object);

                    char_literal34=(Token)match(input,70,FOLLOW_70_in_staticdispatch1404); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_70.add(char_literal34);

                    type=(Token)match(input,ID,FOLLOW_ID_in_staticdispatch1408); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(type);

                    char_literal35=(Token)match(input,70,FOLLOW_70_in_staticdispatch1410); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_70.add(char_literal35);

                    name=(Token)match(input,ID,FOLLOW_ID_in_staticdispatch1414); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    // src/LCPLTreeBuilder.g:193:44: (paramcall1= paramcall )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==NEW||(LA19_0>=VOID && LA19_0<=SELF)||LA19_0==MINUS||LA19_0==NOT||(LA19_0>=ID && LA19_0<=INT)||LA19_0==IF||LA19_0==WHILE||LA19_0==STRING||LA19_0==69||LA19_0==72||LA19_0==74) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // src/LCPLTreeBuilder.g:193:45: paramcall1= paramcall
                            {
                            pushFollow(FOLLOW_paramcall_in_staticdispatch1420);
                            paramcall1=paramcall();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_paramcall.add(paramcall1.getTree());

                            }
                            break;

                    }

                    // src/LCPLTreeBuilder.g:193:68: ( ',' paramcall2+= paramcall )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==68) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // src/LCPLTreeBuilder.g:193:69: ',' paramcall2+= paramcall
                    	    {
                    	    char_literal36=(Token)match(input,68,FOLLOW_68_in_staticdispatch1425); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_68.add(char_literal36);

                    	    pushFollow(FOLLOW_paramcall_in_staticdispatch1429);
                    	    paramcall2=paramcall();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_paramcall.add(paramcall2.getTree());
                    	    if (list_paramcall2==null) list_paramcall2=new ArrayList();
                    	    list_paramcall2.add(paramcall2.getTree());


                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);

                    char_literal37=(Token)match(input,71,FOLLOW_71_in_staticdispatch1432); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_71.add(char_literal37);



                    // AST REWRITE
                    // elements: object, paramcall2, paramcall1, name, type
                    // token labels: name, object, type
                    // rule labels: retval, paramcall1
                    // token list labels: 
                    // rule list labels: paramcall2
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
                    RewriteRuleTokenStream stream_object=new RewriteRuleTokenStream(adaptor,"token object",object);
                    RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_paramcall1=new RewriteRuleSubtreeStream(adaptor,"rule paramcall1",paramcall1!=null?paramcall1.tree:null);
                    RewriteRuleSubtreeStream stream_paramcall2=new RewriteRuleSubtreeStream(adaptor,"token paramcall2",list_paramcall2);
                    root_0 = (CommonTree)adaptor.nil();
                    // 194:6: -> ^( STATICDISPATCH ^( ATOM ^( IDENTIFIER $object) ) $type $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) )
                    {
                        // src/LCPLTreeBuilder.g:194:9: ^( STATICDISPATCH ^( ATOM ^( IDENTIFIER $object) ) $type $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATICDISPATCH, "STATICDISPATCH"), root_1);

                        // src/LCPLTreeBuilder.g:194:26: ^( ATOM ^( IDENTIFIER $object) )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_2);

                        // src/LCPLTreeBuilder.g:194:33: ^( IDENTIFIER $object)
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IDENTIFIER, "IDENTIFIER"), root_3);

                        adaptor.addChild(root_3, stream_object.nextNode());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_type.nextNode());
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // src/LCPLTreeBuilder.g:194:69: ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARAMCALLS, "PARAMCALLS"), root_2);

                        // src/LCPLTreeBuilder.g:194:82: ( $paramcall1)?
                        if ( stream_paramcall1.hasNext() ) {
                            adaptor.addChild(root_2, stream_paramcall1.nextTree());

                        }
                        stream_paramcall1.reset();
                        // src/LCPLTreeBuilder.g:194:95: ( $paramcall2)*
                        while ( stream_paramcall2.hasNext() ) {
                            adaptor.addChild(root_2, stream_paramcall2.nextTree());

                        }
                        stream_paramcall2.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // src/LCPLTreeBuilder.g:195:7: '[' object2= SELF '.' type= ID '.' name= ID (paramcall1= paramcall )? ( ',' paramcall2+= paramcall )* ']'
                    {
                    char_literal38=(Token)match(input,69,FOLLOW_69_in_staticdispatch1481); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_69.add(char_literal38);

                    object2=(Token)match(input,SELF,FOLLOW_SELF_in_staticdispatch1485); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SELF.add(object2);

                    char_literal39=(Token)match(input,70,FOLLOW_70_in_staticdispatch1487); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_70.add(char_literal39);

                    type=(Token)match(input,ID,FOLLOW_ID_in_staticdispatch1491); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(type);

                    char_literal40=(Token)match(input,70,FOLLOW_70_in_staticdispatch1493); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_70.add(char_literal40);

                    name=(Token)match(input,ID,FOLLOW_ID_in_staticdispatch1497); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    // src/LCPLTreeBuilder.g:195:49: (paramcall1= paramcall )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==NEW||(LA21_0>=VOID && LA21_0<=SELF)||LA21_0==MINUS||LA21_0==NOT||(LA21_0>=ID && LA21_0<=INT)||LA21_0==IF||LA21_0==WHILE||LA21_0==STRING||LA21_0==69||LA21_0==72||LA21_0==74) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // src/LCPLTreeBuilder.g:195:50: paramcall1= paramcall
                            {
                            pushFollow(FOLLOW_paramcall_in_staticdispatch1503);
                            paramcall1=paramcall();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_paramcall.add(paramcall1.getTree());

                            }
                            break;

                    }

                    // src/LCPLTreeBuilder.g:195:73: ( ',' paramcall2+= paramcall )*
                    loop22:
                    do {
                        int alt22=2;
                        int LA22_0 = input.LA(1);

                        if ( (LA22_0==68) ) {
                            alt22=1;
                        }


                        switch (alt22) {
                    	case 1 :
                    	    // src/LCPLTreeBuilder.g:195:74: ',' paramcall2+= paramcall
                    	    {
                    	    char_literal41=(Token)match(input,68,FOLLOW_68_in_staticdispatch1508); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_68.add(char_literal41);

                    	    pushFollow(FOLLOW_paramcall_in_staticdispatch1512);
                    	    paramcall2=paramcall();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_paramcall.add(paramcall2.getTree());
                    	    if (list_paramcall2==null) list_paramcall2=new ArrayList();
                    	    list_paramcall2.add(paramcall2.getTree());


                    	    }
                    	    break;

                    	default :
                    	    break loop22;
                        }
                    } while (true);

                    char_literal42=(Token)match(input,71,FOLLOW_71_in_staticdispatch1515); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_71.add(char_literal42);



                    // AST REWRITE
                    // elements: name, type, paramcall2, paramcall1, object2
                    // token labels: object2, name, type
                    // rule labels: retval, paramcall1
                    // token list labels: 
                    // rule list labels: paramcall2
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_object2=new RewriteRuleTokenStream(adaptor,"token object2",object2);
                    RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
                    RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_paramcall1=new RewriteRuleSubtreeStream(adaptor,"rule paramcall1",paramcall1!=null?paramcall1.tree:null);
                    RewriteRuleSubtreeStream stream_paramcall2=new RewriteRuleSubtreeStream(adaptor,"token paramcall2",list_paramcall2);
                    root_0 = (CommonTree)adaptor.nil();
                    // 196:6: -> ^( STATICDISPATCH ^( ATOM ^( SELFKWRD $object2) ) $type $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) )
                    {
                        // src/LCPLTreeBuilder.g:196:9: ^( STATICDISPATCH ^( ATOM ^( SELFKWRD $object2) ) $type $name ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATICDISPATCH, "STATICDISPATCH"), root_1);

                        // src/LCPLTreeBuilder.g:196:26: ^( ATOM ^( SELFKWRD $object2) )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_2);

                        // src/LCPLTreeBuilder.g:196:33: ^( SELFKWRD $object2)
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SELFKWRD, "SELFKWRD"), root_3);

                        adaptor.addChild(root_3, stream_object2.nextNode());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_type.nextNode());
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // src/LCPLTreeBuilder.g:196:69: ^( PARAMCALLS ( $paramcall1)? ( $paramcall2)* )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARAMCALLS, "PARAMCALLS"), root_2);

                        // src/LCPLTreeBuilder.g:196:82: ( $paramcall1)?
                        if ( stream_paramcall1.hasNext() ) {
                            adaptor.addChild(root_2, stream_paramcall1.nextTree());

                        }
                        stream_paramcall1.reset();
                        // src/LCPLTreeBuilder.g:196:95: ( $paramcall2)*
                        while ( stream_paramcall2.hasNext() ) {
                            adaptor.addChild(root_2, stream_paramcall2.nextTree());

                        }
                        stream_paramcall2.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 12, staticdispatch_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "staticdispatch"

    public static class ifstatement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ifstatement"
    // src/LCPLTreeBuilder.g:200:1: ifstatement : IF cond= generalexpression THEN (me1+= methodexpression ';' )* ( ELSE (me2+= methodexpression ';' )* )? END -> ^( IF ^( CONDITION $cond) ^( IFBODY ^( METHODEXPRESSION ( $me1)* ) ) ^( ELSEBODY ^( METHODEXPRESSION ( $me2)* ) ) ) ;
    public final LCPLTreeBuilderParser.ifstatement_return ifstatement() throws RecognitionException {
        LCPLTreeBuilderParser.ifstatement_return retval = new LCPLTreeBuilderParser.ifstatement_return();
        retval.start = input.LT(1);
        int ifstatement_StartIndex = input.index();
        CommonTree root_0 = null;

        Token IF43=null;
        Token THEN44=null;
        Token char_literal45=null;
        Token ELSE46=null;
        Token char_literal47=null;
        Token END48=null;
        List list_me1=null;
        List list_me2=null;
        LCPLTreeBuilderParser.generalexpression_return cond = null;

        RuleReturnScope me1 = null;
        RuleReturnScope me2 = null;
        CommonTree IF43_tree=null;
        CommonTree THEN44_tree=null;
        CommonTree char_literal45_tree=null;
        CommonTree ELSE46_tree=null;
        CommonTree char_literal47_tree=null;
        CommonTree END48_tree=null;
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_THEN=new RewriteRuleTokenStream(adaptor,"token THEN");
        RewriteRuleTokenStream stream_END=new RewriteRuleTokenStream(adaptor,"token END");
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleTokenStream stream_ELSE=new RewriteRuleTokenStream(adaptor,"token ELSE");
        RewriteRuleSubtreeStream stream_methodexpression=new RewriteRuleSubtreeStream(adaptor,"rule methodexpression");
        RewriteRuleSubtreeStream stream_generalexpression=new RewriteRuleSubtreeStream(adaptor,"rule generalexpression");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return retval; }
            // src/LCPLTreeBuilder.g:200:12: ( IF cond= generalexpression THEN (me1+= methodexpression ';' )* ( ELSE (me2+= methodexpression ';' )* )? END -> ^( IF ^( CONDITION $cond) ^( IFBODY ^( METHODEXPRESSION ( $me1)* ) ) ^( ELSEBODY ^( METHODEXPRESSION ( $me2)* ) ) ) )
            // src/LCPLTreeBuilder.g:201:5: IF cond= generalexpression THEN (me1+= methodexpression ';' )* ( ELSE (me2+= methodexpression ';' )* )? END
            {
            IF43=(Token)match(input,IF,FOLLOW_IF_in_ifstatement1578); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IF.add(IF43);

            pushFollow(FOLLOW_generalexpression_in_ifstatement1582);
            cond=generalexpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_generalexpression.add(cond.getTree());
            THEN44=(Token)match(input,THEN,FOLLOW_THEN_in_ifstatement1584); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_THEN.add(THEN44);

            // src/LCPLTreeBuilder.g:201:37: (me1+= methodexpression ';' )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==NEW||(LA24_0>=VOID && LA24_0<=SELF)||LA24_0==LOCAL||LA24_0==MINUS||LA24_0==NOT||(LA24_0>=ID && LA24_0<=INT)||LA24_0==IF||LA24_0==WHILE||LA24_0==STRING||LA24_0==69||LA24_0==72||LA24_0==74) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:201:38: me1+= methodexpression ';'
            	    {
            	    pushFollow(FOLLOW_methodexpression_in_ifstatement1590);
            	    me1=methodexpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_methodexpression.add(me1.getTree());
            	    if (list_me1==null) list_me1=new ArrayList();
            	    list_me1.add(me1.getTree());

            	    char_literal45=(Token)match(input,67,FOLLOW_67_in_ifstatement1592); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_67.add(char_literal45);


            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);

            // src/LCPLTreeBuilder.g:201:66: ( ELSE (me2+= methodexpression ';' )* )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==ELSE) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // src/LCPLTreeBuilder.g:201:67: ELSE (me2+= methodexpression ';' )*
                    {
                    ELSE46=(Token)match(input,ELSE,FOLLOW_ELSE_in_ifstatement1597); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ELSE.add(ELSE46);

                    // src/LCPLTreeBuilder.g:201:72: (me2+= methodexpression ';' )*
                    loop25:
                    do {
                        int alt25=2;
                        int LA25_0 = input.LA(1);

                        if ( (LA25_0==NEW||(LA25_0>=VOID && LA25_0<=SELF)||LA25_0==LOCAL||LA25_0==MINUS||LA25_0==NOT||(LA25_0>=ID && LA25_0<=INT)||LA25_0==IF||LA25_0==WHILE||LA25_0==STRING||LA25_0==69||LA25_0==72||LA25_0==74) ) {
                            alt25=1;
                        }


                        switch (alt25) {
                    	case 1 :
                    	    // src/LCPLTreeBuilder.g:201:73: me2+= methodexpression ';'
                    	    {
                    	    pushFollow(FOLLOW_methodexpression_in_ifstatement1602);
                    	    me2=methodexpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_methodexpression.add(me2.getTree());
                    	    if (list_me2==null) list_me2=new ArrayList();
                    	    list_me2.add(me2.getTree());

                    	    char_literal47=(Token)match(input,67,FOLLOW_67_in_ifstatement1604); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_67.add(char_literal47);


                    	    }
                    	    break;

                    	default :
                    	    break loop25;
                        }
                    } while (true);


                    }
                    break;

            }

            END48=(Token)match(input,END,FOLLOW_END_in_ifstatement1610); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_END.add(END48);



            // AST REWRITE
            // elements: cond, IF, me2, me1
            // token labels: 
            // rule labels: retval, cond
            // token list labels: 
            // rule list labels: me1, me2
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_me1=new RewriteRuleSubtreeStream(adaptor,"token me1",list_me1);
            RewriteRuleSubtreeStream stream_me2=new RewriteRuleSubtreeStream(adaptor,"token me2",list_me2);
            root_0 = (CommonTree)adaptor.nil();
            // 202:5: -> ^( IF ^( CONDITION $cond) ^( IFBODY ^( METHODEXPRESSION ( $me1)* ) ) ^( ELSEBODY ^( METHODEXPRESSION ( $me2)* ) ) )
            {
                // src/LCPLTreeBuilder.g:202:7: ^( IF ^( CONDITION $cond) ^( IFBODY ^( METHODEXPRESSION ( $me1)* ) ) ^( ELSEBODY ^( METHODEXPRESSION ( $me2)* ) ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_IF.nextNode(), root_1);

                // src/LCPLTreeBuilder.g:202:12: ^( CONDITION $cond)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CONDITION, "CONDITION"), root_2);

                adaptor.addChild(root_2, stream_cond.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // src/LCPLTreeBuilder.g:202:31: ^( IFBODY ^( METHODEXPRESSION ( $me1)* ) )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IFBODY, "IFBODY"), root_2);

                // src/LCPLTreeBuilder.g:202:40: ^( METHODEXPRESSION ( $me1)* )
                {
                CommonTree root_3 = (CommonTree)adaptor.nil();
                root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHODEXPRESSION, "METHODEXPRESSION"), root_3);

                // src/LCPLTreeBuilder.g:202:59: ( $me1)*
                while ( stream_me1.hasNext() ) {
                    adaptor.addChild(root_3, stream_me1.nextTree());

                }
                stream_me1.reset();

                adaptor.addChild(root_2, root_3);
                }

                adaptor.addChild(root_1, root_2);
                }
                // src/LCPLTreeBuilder.g:202:67: ^( ELSEBODY ^( METHODEXPRESSION ( $me2)* ) )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ELSEBODY, "ELSEBODY"), root_2);

                // src/LCPLTreeBuilder.g:202:78: ^( METHODEXPRESSION ( $me2)* )
                {
                CommonTree root_3 = (CommonTree)adaptor.nil();
                root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHODEXPRESSION, "METHODEXPRESSION"), root_3);

                // src/LCPLTreeBuilder.g:202:97: ( $me2)*
                while ( stream_me2.hasNext() ) {
                    adaptor.addChild(root_3, stream_me2.nextTree());

                }
                stream_me2.reset();

                adaptor.addChild(root_2, root_3);
                }

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 13, ifstatement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "ifstatement"

    public static class whilestatement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whilestatement"
    // src/LCPLTreeBuilder.g:206:1: whilestatement : WHILE cond= generalexpression LOOP (me1+= methodexpression ';' )* END -> ^( WHILE ^( CONDITION $cond) ^( WHILEBODY ^( METHODEXPRESSION ( $me1)* ) ) ) ;
    public final LCPLTreeBuilderParser.whilestatement_return whilestatement() throws RecognitionException {
        LCPLTreeBuilderParser.whilestatement_return retval = new LCPLTreeBuilderParser.whilestatement_return();
        retval.start = input.LT(1);
        int whilestatement_StartIndex = input.index();
        CommonTree root_0 = null;

        Token WHILE49=null;
        Token LOOP50=null;
        Token char_literal51=null;
        Token END52=null;
        List list_me1=null;
        LCPLTreeBuilderParser.generalexpression_return cond = null;

        RuleReturnScope me1 = null;
        CommonTree WHILE49_tree=null;
        CommonTree LOOP50_tree=null;
        CommonTree char_literal51_tree=null;
        CommonTree END52_tree=null;
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_WHILE=new RewriteRuleTokenStream(adaptor,"token WHILE");
        RewriteRuleTokenStream stream_END=new RewriteRuleTokenStream(adaptor,"token END");
        RewriteRuleTokenStream stream_LOOP=new RewriteRuleTokenStream(adaptor,"token LOOP");
        RewriteRuleSubtreeStream stream_methodexpression=new RewriteRuleSubtreeStream(adaptor,"rule methodexpression");
        RewriteRuleSubtreeStream stream_generalexpression=new RewriteRuleSubtreeStream(adaptor,"rule generalexpression");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return retval; }
            // src/LCPLTreeBuilder.g:206:15: ( WHILE cond= generalexpression LOOP (me1+= methodexpression ';' )* END -> ^( WHILE ^( CONDITION $cond) ^( WHILEBODY ^( METHODEXPRESSION ( $me1)* ) ) ) )
            // src/LCPLTreeBuilder.g:207:5: WHILE cond= generalexpression LOOP (me1+= methodexpression ';' )* END
            {
            WHILE49=(Token)match(input,WHILE,FOLLOW_WHILE_in_whilestatement1676); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WHILE.add(WHILE49);

            pushFollow(FOLLOW_generalexpression_in_whilestatement1681);
            cond=generalexpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_generalexpression.add(cond.getTree());
            LOOP50=(Token)match(input,LOOP,FOLLOW_LOOP_in_whilestatement1683); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LOOP.add(LOOP50);

            // src/LCPLTreeBuilder.g:207:40: (me1+= methodexpression ';' )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==NEW||(LA27_0>=VOID && LA27_0<=SELF)||LA27_0==LOCAL||LA27_0==MINUS||LA27_0==NOT||(LA27_0>=ID && LA27_0<=INT)||LA27_0==IF||LA27_0==WHILE||LA27_0==STRING||LA27_0==69||LA27_0==72||LA27_0==74) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:207:41: me1+= methodexpression ';'
            	    {
            	    pushFollow(FOLLOW_methodexpression_in_whilestatement1688);
            	    me1=methodexpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_methodexpression.add(me1.getTree());
            	    if (list_me1==null) list_me1=new ArrayList();
            	    list_me1.add(me1.getTree());

            	    char_literal51=(Token)match(input,67,FOLLOW_67_in_whilestatement1690); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_67.add(char_literal51);


            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);

            END52=(Token)match(input,END,FOLLOW_END_in_whilestatement1696); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_END.add(END52);



            // AST REWRITE
            // elements: WHILE, cond, me1
            // token labels: 
            // rule labels: retval, cond
            // token list labels: 
            // rule list labels: me1
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_me1=new RewriteRuleSubtreeStream(adaptor,"token me1",list_me1);
            root_0 = (CommonTree)adaptor.nil();
            // 208:5: -> ^( WHILE ^( CONDITION $cond) ^( WHILEBODY ^( METHODEXPRESSION ( $me1)* ) ) )
            {
                // src/LCPLTreeBuilder.g:208:7: ^( WHILE ^( CONDITION $cond) ^( WHILEBODY ^( METHODEXPRESSION ( $me1)* ) ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_WHILE.nextNode(), root_1);

                // src/LCPLTreeBuilder.g:208:15: ^( CONDITION $cond)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CONDITION, "CONDITION"), root_2);

                adaptor.addChild(root_2, stream_cond.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                // src/LCPLTreeBuilder.g:208:34: ^( WHILEBODY ^( METHODEXPRESSION ( $me1)* ) )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(WHILEBODY, "WHILEBODY"), root_2);

                // src/LCPLTreeBuilder.g:208:46: ^( METHODEXPRESSION ( $me1)* )
                {
                CommonTree root_3 = (CommonTree)adaptor.nil();
                root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHODEXPRESSION, "METHODEXPRESSION"), root_3);

                // src/LCPLTreeBuilder.g:208:65: ( $me1)*
                while ( stream_me1.hasNext() ) {
                    adaptor.addChild(root_3, stream_me1.nextTree());

                }
                stream_me1.reset();

                adaptor.addChild(root_2, root_3);
                }

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 14, whilestatement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "whilestatement"

    public static class paramcall_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "paramcall"
    // src/LCPLTreeBuilder.g:212:1: paramcall : generalexpression ;
    public final LCPLTreeBuilderParser.paramcall_return paramcall() throws RecognitionException {
        LCPLTreeBuilderParser.paramcall_return retval = new LCPLTreeBuilderParser.paramcall_return();
        retval.start = input.LT(1);
        int paramcall_StartIndex = input.index();
        CommonTree root_0 = null;

        LCPLTreeBuilderParser.generalexpression_return generalexpression53 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return retval; }
            // src/LCPLTreeBuilder.g:212:10: ( generalexpression )
            // src/LCPLTreeBuilder.g:213:6: generalexpression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_generalexpression_in_paramcall1751);
            generalexpression53=generalexpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, generalexpression53.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 15, paramcall_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "paramcall"

    public static class substr_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "substr"
    // src/LCPLTreeBuilder.g:217:1: substr : se= stringexpr ( '[' start+= paramcall ',' fin+= paramcall ']' )+ -> ^( SUBSTR $se ^( PARAMCALLS ( $start)+ ) ^( PARAMCALLS ( $fin)+ ) ) ;
    public final LCPLTreeBuilderParser.substr_return substr() throws RecognitionException {
        LCPLTreeBuilderParser.substr_return retval = new LCPLTreeBuilderParser.substr_return();
        retval.start = input.LT(1);
        int substr_StartIndex = input.index();
        CommonTree root_0 = null;

        Token char_literal54=null;
        Token char_literal55=null;
        Token char_literal56=null;
        List list_start=null;
        List list_fin=null;
        LCPLTreeBuilderParser.stringexpr_return se = null;

        RuleReturnScope start = null;
        RuleReturnScope fin = null;
        CommonTree char_literal54_tree=null;
        CommonTree char_literal55_tree=null;
        CommonTree char_literal56_tree=null;
        RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
        RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
        RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
        RewriteRuleSubtreeStream stream_paramcall=new RewriteRuleSubtreeStream(adaptor,"rule paramcall");
        RewriteRuleSubtreeStream stream_stringexpr=new RewriteRuleSubtreeStream(adaptor,"rule stringexpr");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return retval; }
            // src/LCPLTreeBuilder.g:217:7: (se= stringexpr ( '[' start+= paramcall ',' fin+= paramcall ']' )+ -> ^( SUBSTR $se ^( PARAMCALLS ( $start)+ ) ^( PARAMCALLS ( $fin)+ ) ) )
            // src/LCPLTreeBuilder.g:218:5: se= stringexpr ( '[' start+= paramcall ',' fin+= paramcall ']' )+
            {
            pushFollow(FOLLOW_stringexpr_in_substr1779);
            se=stringexpr();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_stringexpr.add(se.getTree());
            // src/LCPLTreeBuilder.g:218:20: ( '[' start+= paramcall ',' fin+= paramcall ']' )+
            int cnt28=0;
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==69) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:218:21: '[' start+= paramcall ',' fin+= paramcall ']'
            	    {
            	    char_literal54=(Token)match(input,69,FOLLOW_69_in_substr1783); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_69.add(char_literal54);

            	    pushFollow(FOLLOW_paramcall_in_substr1787);
            	    start=paramcall();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_paramcall.add(start.getTree());
            	    if (list_start==null) list_start=new ArrayList();
            	    list_start.add(start.getTree());

            	    char_literal55=(Token)match(input,68,FOLLOW_68_in_substr1789); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_68.add(char_literal55);

            	    pushFollow(FOLLOW_paramcall_in_substr1793);
            	    fin=paramcall();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_paramcall.add(fin.getTree());
            	    if (list_fin==null) list_fin=new ArrayList();
            	    list_fin.add(fin.getTree());

            	    char_literal56=(Token)match(input,71,FOLLOW_71_in_substr1794); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_71.add(char_literal56);


            	    }
            	    break;

            	default :
            	    if ( cnt28 >= 1 ) break loop28;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(28, input);
                        throw eee;
                }
                cnt28++;
            } while (true);



            // AST REWRITE
            // elements: start, se, fin
            // token labels: 
            // rule labels: retval, se
            // token list labels: 
            // rule list labels: start, fin
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_se=new RewriteRuleSubtreeStream(adaptor,"rule se",se!=null?se.tree:null);
            RewriteRuleSubtreeStream stream_start=new RewriteRuleSubtreeStream(adaptor,"token start",list_start);
            RewriteRuleSubtreeStream stream_fin=new RewriteRuleSubtreeStream(adaptor,"token fin",list_fin);
            root_0 = (CommonTree)adaptor.nil();
            // 219:5: -> ^( SUBSTR $se ^( PARAMCALLS ( $start)+ ) ^( PARAMCALLS ( $fin)+ ) )
            {
                // src/LCPLTreeBuilder.g:219:7: ^( SUBSTR $se ^( PARAMCALLS ( $start)+ ) ^( PARAMCALLS ( $fin)+ ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SUBSTR, "SUBSTR"), root_1);

                adaptor.addChild(root_1, stream_se.nextTree());
                // src/LCPLTreeBuilder.g:219:21: ^( PARAMCALLS ( $start)+ )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARAMCALLS, "PARAMCALLS"), root_2);

                if ( !(stream_start.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_start.hasNext() ) {
                    adaptor.addChild(root_2, stream_start.nextTree());

                }
                stream_start.reset();

                adaptor.addChild(root_1, root_2);
                }
                // src/LCPLTreeBuilder.g:219:43: ^( PARAMCALLS ( $fin)+ )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARAMCALLS, "PARAMCALLS"), root_2);

                if ( !(stream_fin.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_fin.hasNext() ) {
                    adaptor.addChild(root_2, stream_fin.nextTree());

                }
                stream_fin.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 16, substr_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "substr"

    public static class stringexpr_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "stringexpr"
    // src/LCPLTreeBuilder.g:223:1: stringexpr : (arg= ID -> ^( ATOM ^( IDENTIFIER $arg) ) | cast );
    public final LCPLTreeBuilderParser.stringexpr_return stringexpr() throws RecognitionException {
        LCPLTreeBuilderParser.stringexpr_return retval = new LCPLTreeBuilderParser.stringexpr_return();
        retval.start = input.LT(1);
        int stringexpr_StartIndex = input.index();
        CommonTree root_0 = null;

        Token arg=null;
        LCPLTreeBuilderParser.cast_return cast57 = null;


        CommonTree arg_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return retval; }
            // src/LCPLTreeBuilder.g:223:11: (arg= ID -> ^( ATOM ^( IDENTIFIER $arg) ) | cast )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==ID) ) {
                alt29=1;
            }
            else if ( (LA29_0==74) ) {
                alt29=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }
            switch (alt29) {
                case 1 :
                    // src/LCPLTreeBuilder.g:224:5: arg= ID
                    {
                    arg=(Token)match(input,ID,FOLLOW_ID_in_stringexpr1854); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(arg);



                    // AST REWRITE
                    // elements: arg
                    // token labels: arg
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg=new RewriteRuleTokenStream(adaptor,"token arg",arg);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 225:5: -> ^( ATOM ^( IDENTIFIER $arg) )
                    {
                        // src/LCPLTreeBuilder.g:225:8: ^( ATOM ^( IDENTIFIER $arg) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:225:15: ^( IDENTIFIER $arg)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IDENTIFIER, "IDENTIFIER"), root_2);

                        adaptor.addChild(root_2, stream_arg.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // src/LCPLTreeBuilder.g:226:6: cast
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_cast_in_stringexpr1879);
                    cast57=cast();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, cast57.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 17, stringexpr_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "stringexpr"

    public static class cast_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "cast"
    // src/LCPLTreeBuilder.g:230:1: cast : '{' type= ID exp= castexpression '}' -> ^( CAST $type $exp) ;
    public final LCPLTreeBuilderParser.cast_return cast() throws RecognitionException {
        LCPLTreeBuilderParser.cast_return retval = new LCPLTreeBuilderParser.cast_return();
        retval.start = input.LT(1);
        int cast_StartIndex = input.index();
        CommonTree root_0 = null;

        Token type=null;
        Token char_literal58=null;
        Token char_literal59=null;
        LCPLTreeBuilderParser.castexpression_return exp = null;


        CommonTree type_tree=null;
        CommonTree char_literal58_tree=null;
        CommonTree char_literal59_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_74=new RewriteRuleTokenStream(adaptor,"token 74");
        RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
        RewriteRuleSubtreeStream stream_castexpression=new RewriteRuleSubtreeStream(adaptor,"rule castexpression");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return retval; }
            // src/LCPLTreeBuilder.g:230:5: ( '{' type= ID exp= castexpression '}' -> ^( CAST $type $exp) )
            // src/LCPLTreeBuilder.g:231:5: '{' type= ID exp= castexpression '}'
            {
            char_literal58=(Token)match(input,74,FOLLOW_74_in_cast1905); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_74.add(char_literal58);

            type=(Token)match(input,ID,FOLLOW_ID_in_cast1909); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(type);

            pushFollow(FOLLOW_castexpression_in_cast1914);
            exp=castexpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_castexpression.add(exp.getTree());
            char_literal59=(Token)match(input,75,FOLLOW_75_in_cast1915); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_75.add(char_literal59);



            // AST REWRITE
            // elements: type, exp
            // token labels: type
            // rule labels: exp, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
            RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp",exp!=null?exp.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 232:5: -> ^( CAST $type $exp)
            {
                // src/LCPLTreeBuilder.g:232:8: ^( CAST $type $exp)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CAST, "CAST"), root_1);

                adaptor.addChild(root_1, stream_type.nextNode());
                adaptor.addChild(root_1, stream_exp.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 18, cast_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "cast"

    public static class castexpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "castexpression"
    // src/LCPLTreeBuilder.g:236:1: castexpression : generalexpression ;
    public final LCPLTreeBuilderParser.castexpression_return castexpression() throws RecognitionException {
        LCPLTreeBuilderParser.castexpression_return retval = new LCPLTreeBuilderParser.castexpression_return();
        retval.start = input.LT(1);
        int castexpression_StartIndex = input.index();
        CommonTree root_0 = null;

        LCPLTreeBuilderParser.generalexpression_return generalexpression60 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return retval; }
            // src/LCPLTreeBuilder.g:236:15: ( generalexpression )
            // src/LCPLTreeBuilder.g:237:5: generalexpression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_generalexpression_in_castexpression1956);
            generalexpression60=generalexpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, generalexpression60.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 19, castexpression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "castexpression"

    public static class assignment_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignment"
    // src/LCPLTreeBuilder.g:241:1: assignment : ID ASSIGNMENT expr= cast -> ^( ASSIGNMENT $expr) ;
    public final LCPLTreeBuilderParser.assignment_return assignment() throws RecognitionException {
        LCPLTreeBuilderParser.assignment_return retval = new LCPLTreeBuilderParser.assignment_return();
        retval.start = input.LT(1);
        int assignment_StartIndex = input.index();
        CommonTree root_0 = null;

        Token ID61=null;
        Token ASSIGNMENT62=null;
        LCPLTreeBuilderParser.cast_return expr = null;


        CommonTree ID61_tree=null;
        CommonTree ASSIGNMENT62_tree=null;
        RewriteRuleTokenStream stream_ASSIGNMENT=new RewriteRuleTokenStream(adaptor,"token ASSIGNMENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_cast=new RewriteRuleSubtreeStream(adaptor,"rule cast");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return retval; }
            // src/LCPLTreeBuilder.g:241:11: ( ID ASSIGNMENT expr= cast -> ^( ASSIGNMENT $expr) )
            // src/LCPLTreeBuilder.g:242:5: ID ASSIGNMENT expr= cast
            {
            ID61=(Token)match(input,ID,FOLLOW_ID_in_assignment1987); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID61);

            ASSIGNMENT62=(Token)match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_assignment1989); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ASSIGNMENT.add(ASSIGNMENT62);

            pushFollow(FOLLOW_cast_in_assignment1993);
            expr=cast();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_cast.add(expr.getTree());


            // AST REWRITE
            // elements: expr, ASSIGNMENT
            // token labels: 
            // rule labels: retval, expr
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr",expr!=null?expr.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 243:5: -> ^( ASSIGNMENT $expr)
            {
                // src/LCPLTreeBuilder.g:243:7: ^( ASSIGNMENT $expr)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_ASSIGNMENT.nextNode(), root_1);

                adaptor.addChild(root_1, stream_expr.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 20, assignment_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "assignment"

    public static class localdef_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "localdef"
    // src/LCPLTreeBuilder.g:248:1: localdef : type= ID name= ID ( '=' )? (value= generalexpression )? ';' -> ^( LOCALDEF $type $name ( $value)? ) ;
    public final LCPLTreeBuilderParser.localdef_return localdef() throws RecognitionException {
        LCPLTreeBuilderParser.localdef_return retval = new LCPLTreeBuilderParser.localdef_return();
        retval.start = input.LT(1);
        int localdef_StartIndex = input.index();
        CommonTree root_0 = null;

        Token type=null;
        Token name=null;
        Token char_literal63=null;
        Token char_literal64=null;
        LCPLTreeBuilderParser.generalexpression_return value = null;


        CommonTree type_tree=null;
        CommonTree name_tree=null;
        CommonTree char_literal63_tree=null;
        CommonTree char_literal64_tree=null;
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_ASSIGNMENT=new RewriteRuleTokenStream(adaptor,"token ASSIGNMENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_generalexpression=new RewriteRuleSubtreeStream(adaptor,"rule generalexpression");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return retval; }
            // src/LCPLTreeBuilder.g:248:9: (type= ID name= ID ( '=' )? (value= generalexpression )? ';' -> ^( LOCALDEF $type $name ( $value)? ) )
            // src/LCPLTreeBuilder.g:249:10: type= ID name= ID ( '=' )? (value= generalexpression )? ';'
            {
            type=(Token)match(input,ID,FOLLOW_ID_in_localdef2053); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(type);

            name=(Token)match(input,ID,FOLLOW_ID_in_localdef2057); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            // src/LCPLTreeBuilder.g:249:26: ( '=' )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==ASSIGNMENT) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // src/LCPLTreeBuilder.g:249:27: '='
                    {
                    char_literal63=(Token)match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_localdef2060); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ASSIGNMENT.add(char_literal63);


                    }
                    break;

            }

            // src/LCPLTreeBuilder.g:249:33: (value= generalexpression )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==NEW||(LA31_0>=VOID && LA31_0<=SELF)||LA31_0==MINUS||LA31_0==NOT||(LA31_0>=ID && LA31_0<=INT)||LA31_0==IF||LA31_0==WHILE||LA31_0==STRING||LA31_0==69||LA31_0==72||LA31_0==74) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // src/LCPLTreeBuilder.g:249:34: value= generalexpression
                    {
                    pushFollow(FOLLOW_generalexpression_in_localdef2067);
                    value=generalexpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_generalexpression.add(value.getTree());

                    }
                    break;

            }

            char_literal64=(Token)match(input,67,FOLLOW_67_in_localdef2071); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_67.add(char_literal64);



            // AST REWRITE
            // elements: value, name, type
            // token labels: name, type
            // rule labels: retval, value
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
            RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 251:9: -> ^( LOCALDEF $type $name ( $value)? )
            {
                // src/LCPLTreeBuilder.g:251:12: ^( LOCALDEF $type $name ( $value)? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LOCALDEF, "LOCALDEF"), root_1);

                adaptor.addChild(root_1, stream_type.nextNode());
                adaptor.addChild(root_1, stream_name.nextNode());
                // src/LCPLTreeBuilder.g:251:36: ( $value)?
                if ( stream_value.hasNext() ) {
                    adaptor.addChild(root_1, stream_value.nextTree());

                }
                stream_value.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 21, localdef_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "localdef"

    public static class generalexpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "generalexpression"
    // src/LCPLTreeBuilder.g:256:1: generalexpression : ( expression | ( NOT expression )=> NOT expression | ID ( ASSIGNMENT generalexpression )=> ASSIGNMENT generalexpression | ( SELF '.' ID ) ( ASSIGNMENT expression )=> ASSIGNMENT expression );
    public final LCPLTreeBuilderParser.generalexpression_return generalexpression() throws RecognitionException {
        LCPLTreeBuilderParser.generalexpression_return retval = new LCPLTreeBuilderParser.generalexpression_return();
        retval.start = input.LT(1);
        int generalexpression_StartIndex = input.index();
        CommonTree root_0 = null;

        Token NOT66=null;
        Token ID68=null;
        Token ASSIGNMENT69=null;
        Token SELF71=null;
        Token char_literal72=null;
        Token ID73=null;
        Token ASSIGNMENT74=null;
        LCPLTreeBuilderParser.expression_return expression65 = null;

        LCPLTreeBuilderParser.expression_return expression67 = null;

        LCPLTreeBuilderParser.generalexpression_return generalexpression70 = null;

        LCPLTreeBuilderParser.expression_return expression75 = null;


        CommonTree NOT66_tree=null;
        CommonTree ID68_tree=null;
        CommonTree ASSIGNMENT69_tree=null;
        CommonTree SELF71_tree=null;
        CommonTree char_literal72_tree=null;
        CommonTree ID73_tree=null;
        CommonTree ASSIGNMENT74_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return retval; }
            // src/LCPLTreeBuilder.g:256:18: ( expression | ( NOT expression )=> NOT expression | ID ( ASSIGNMENT generalexpression )=> ASSIGNMENT generalexpression | ( SELF '.' ID ) ( ASSIGNMENT expression )=> ASSIGNMENT expression )
            int alt32=4;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==ID) ) {
                int LA32_1 = input.LA(2);

                if ( (LA32_1==EOF||LA32_1==LOOP||(LA32_1>=ADDITION && LA32_1<=EQUAL)||LA32_1==THEN||(LA32_1>=67 && LA32_1<=69)||LA32_1==71||LA32_1==73||LA32_1==75) ) {
                    alt32=1;
                }
                else if ( (LA32_1==ASSIGNMENT) ) {
                    alt32=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA32_0==NEW||LA32_0==VOID||LA32_0==MINUS||LA32_0==INT||LA32_0==IF||LA32_0==WHILE||LA32_0==STRING||LA32_0==69||LA32_0==72||LA32_0==74) ) {
                alt32=1;
            }
            else if ( (LA32_0==SELF) ) {
                int LA32_3 = input.LA(2);

                if ( (LA32_3==70) ) {
                    alt32=4;
                }
                else if ( (LA32_3==EOF||LA32_3==LOOP||(LA32_3>=ADDITION && LA32_3<=EQUAL)||LA32_3==THEN||(LA32_3>=67 && LA32_3<=68)||LA32_3==71||LA32_3==73||LA32_3==75) ) {
                    alt32=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 3, input);

                    throw nvae;
                }
            }
            else if ( (LA32_0==NOT) && (synpred1_LCPLTreeBuilder())) {
                alt32=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // src/LCPLTreeBuilder.g:257:6: expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_expression_in_generalexpression2128);
                    expression65=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression65.getTree());

                    }
                    break;
                case 2 :
                    // src/LCPLTreeBuilder.g:258:7: ( NOT expression )=> NOT expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    NOT66=(Token)match(input,NOT,FOLLOW_NOT_in_generalexpression2146); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT66_tree = (CommonTree)adaptor.create(NOT66);
                    root_0 = (CommonTree)adaptor.becomeRoot(NOT66_tree, root_0);
                    }
                    pushFollow(FOLLOW_expression_in_generalexpression2150);
                    expression67=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression67.getTree());

                    }
                    break;
                case 3 :
                    // src/LCPLTreeBuilder.g:259:7: ID ( ASSIGNMENT generalexpression )=> ASSIGNMENT generalexpression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID68=(Token)match(input,ID,FOLLOW_ID_in_generalexpression2158); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID68_tree = (CommonTree)adaptor.create(ID68);
                    adaptor.addChild(root_0, ID68_tree);
                    }
                    ASSIGNMENT69=(Token)match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_generalexpression2168); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ASSIGNMENT69_tree = (CommonTree)adaptor.create(ASSIGNMENT69);
                    root_0 = (CommonTree)adaptor.becomeRoot(ASSIGNMENT69_tree, root_0);
                    }
                    pushFollow(FOLLOW_generalexpression_in_generalexpression2171);
                    generalexpression70=generalexpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, generalexpression70.getTree());

                    }
                    break;
                case 4 :
                    // src/LCPLTreeBuilder.g:260:7: ( SELF '.' ID ) ( ASSIGNMENT expression )=> ASSIGNMENT expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // src/LCPLTreeBuilder.g:260:7: ( SELF '.' ID )
                    // src/LCPLTreeBuilder.g:260:8: SELF '.' ID
                    {
                    SELF71=(Token)match(input,SELF,FOLLOW_SELF_in_generalexpression2180); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SELF71_tree = (CommonTree)adaptor.create(SELF71);
                    adaptor.addChild(root_0, SELF71_tree);
                    }
                    char_literal72=(Token)match(input,70,FOLLOW_70_in_generalexpression2182); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal72_tree = (CommonTree)adaptor.create(char_literal72);
                    adaptor.addChild(root_0, char_literal72_tree);
                    }
                    ID73=(Token)match(input,ID,FOLLOW_ID_in_generalexpression2184); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID73_tree = (CommonTree)adaptor.create(ID73);
                    adaptor.addChild(root_0, ID73_tree);
                    }

                    }

                    ASSIGNMENT74=(Token)match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_generalexpression2195); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ASSIGNMENT74_tree = (CommonTree)adaptor.create(ASSIGNMENT74);
                    root_0 = (CommonTree)adaptor.becomeRoot(ASSIGNMENT74_tree, root_0);
                    }
                    pushFollow(FOLLOW_expression_in_generalexpression2198);
                    expression75=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression75.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 22, generalexpression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "generalexpression"

    public static class expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // src/LCPLTreeBuilder.g:264:1: expression : expr_aritm ;
    public final LCPLTreeBuilderParser.expression_return expression() throws RecognitionException {
        LCPLTreeBuilderParser.expression_return retval = new LCPLTreeBuilderParser.expression_return();
        retval.start = input.LT(1);
        int expression_StartIndex = input.index();
        CommonTree root_0 = null;

        LCPLTreeBuilderParser.expr_aritm_return expr_aritm76 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return retval; }
            // src/LCPLTreeBuilder.g:264:11: ( expr_aritm )
            // src/LCPLTreeBuilder.g:265:5: expr_aritm
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expr_aritm_in_expression2226);
            expr_aritm76=expr_aritm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expr_aritm76.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 23, expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class atom_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atom"
    // src/LCPLTreeBuilder.g:270:1: atom : (arg= ID -> ^( ATOM ^( IDENTIFIER $arg) ) | '(' generalexpression ')' -> generalexpression | arg= INT -> ^( ATOM ^( INTEGER $arg) ) | arg= VOID -> ^( ATOM ^( VOIDKWRD $arg) ) | arg= SELF -> ^( ATOM ^( SELFKWRD $arg) ) | (arg= STRING ) -> ^( ATOM ^( STRINGCONST $arg) ) | ( ( MINUS ) arg= INT ) | ( ( NEW ) arg= ID ) | dispatch | staticdispatch | cast | ( ifstatement )=> ifstatement | whilestatement );
    public final LCPLTreeBuilderParser.atom_return atom() throws RecognitionException {
        LCPLTreeBuilderParser.atom_return retval = new LCPLTreeBuilderParser.atom_return();
        retval.start = input.LT(1);
        int atom_StartIndex = input.index();
        CommonTree root_0 = null;

        Token arg=null;
        Token char_literal77=null;
        Token char_literal79=null;
        Token MINUS80=null;
        Token NEW81=null;
        LCPLTreeBuilderParser.generalexpression_return generalexpression78 = null;

        LCPLTreeBuilderParser.dispatch_return dispatch82 = null;

        LCPLTreeBuilderParser.staticdispatch_return staticdispatch83 = null;

        LCPLTreeBuilderParser.cast_return cast84 = null;

        LCPLTreeBuilderParser.ifstatement_return ifstatement85 = null;

        LCPLTreeBuilderParser.whilestatement_return whilestatement86 = null;


        CommonTree arg_tree=null;
        CommonTree char_literal77_tree=null;
        CommonTree char_literal79_tree=null;
        CommonTree MINUS80_tree=null;
        CommonTree NEW81_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_VOID=new RewriteRuleTokenStream(adaptor,"token VOID");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_SELF=new RewriteRuleTokenStream(adaptor,"token SELF");
        RewriteRuleTokenStream stream_72=new RewriteRuleTokenStream(adaptor,"token 72");
        RewriteRuleTokenStream stream_73=new RewriteRuleTokenStream(adaptor,"token 73");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");
        RewriteRuleSubtreeStream stream_generalexpression=new RewriteRuleSubtreeStream(adaptor,"rule generalexpression");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return retval; }
            // src/LCPLTreeBuilder.g:270:5: (arg= ID -> ^( ATOM ^( IDENTIFIER $arg) ) | '(' generalexpression ')' -> generalexpression | arg= INT -> ^( ATOM ^( INTEGER $arg) ) | arg= VOID -> ^( ATOM ^( VOIDKWRD $arg) ) | arg= SELF -> ^( ATOM ^( SELFKWRD $arg) ) | (arg= STRING ) -> ^( ATOM ^( STRINGCONST $arg) ) | ( ( MINUS ) arg= INT ) | ( ( NEW ) arg= ID ) | dispatch | staticdispatch | cast | ( ifstatement )=> ifstatement | whilestatement )
            int alt33=13;
            alt33 = dfa33.predict(input);
            switch (alt33) {
                case 1 :
                    // src/LCPLTreeBuilder.g:271:5: arg= ID
                    {
                    arg=(Token)match(input,ID,FOLLOW_ID_in_atom2248); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(arg);



                    // AST REWRITE
                    // elements: arg
                    // token labels: arg
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg=new RewriteRuleTokenStream(adaptor,"token arg",arg);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 272:5: -> ^( ATOM ^( IDENTIFIER $arg) )
                    {
                        // src/LCPLTreeBuilder.g:272:8: ^( ATOM ^( IDENTIFIER $arg) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:272:15: ^( IDENTIFIER $arg)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IDENTIFIER, "IDENTIFIER"), root_2);

                        adaptor.addChild(root_2, stream_arg.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // src/LCPLTreeBuilder.g:273:7: '(' generalexpression ')'
                    {
                    char_literal77=(Token)match(input,72,FOLLOW_72_in_atom2274); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_72.add(char_literal77);

                    pushFollow(FOLLOW_generalexpression_in_atom2276);
                    generalexpression78=generalexpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_generalexpression.add(generalexpression78.getTree());
                    char_literal79=(Token)match(input,73,FOLLOW_73_in_atom2278); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_73.add(char_literal79);



                    // AST REWRITE
                    // elements: generalexpression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 273:33: -> generalexpression
                    {
                        adaptor.addChild(root_0, stream_generalexpression.nextTree());

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // src/LCPLTreeBuilder.g:274:6: arg= INT
                    {
                    arg=(Token)match(input,INT,FOLLOW_INT_in_atom2291); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(arg);



                    // AST REWRITE
                    // elements: arg
                    // token labels: arg
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg=new RewriteRuleTokenStream(adaptor,"token arg",arg);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 276:5: -> ^( ATOM ^( INTEGER $arg) )
                    {
                        // src/LCPLTreeBuilder.g:276:8: ^( ATOM ^( INTEGER $arg) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:276:15: ^( INTEGER $arg)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(INTEGER, "INTEGER"), root_2);

                        adaptor.addChild(root_2, stream_arg.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // src/LCPLTreeBuilder.g:278:6: arg= VOID
                    {
                    arg=(Token)match(input,VOID,FOLLOW_VOID_in_atom2320); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VOID.add(arg);



                    // AST REWRITE
                    // elements: arg
                    // token labels: arg
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg=new RewriteRuleTokenStream(adaptor,"token arg",arg);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 280:5: -> ^( ATOM ^( VOIDKWRD $arg) )
                    {
                        // src/LCPLTreeBuilder.g:280:8: ^( ATOM ^( VOIDKWRD $arg) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:280:15: ^( VOIDKWRD $arg)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(VOIDKWRD, "VOIDKWRD"), root_2);

                        adaptor.addChild(root_2, stream_arg.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // src/LCPLTreeBuilder.g:282:6: arg= SELF
                    {
                    arg=(Token)match(input,SELF,FOLLOW_SELF_in_atom2349); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SELF.add(arg);



                    // AST REWRITE
                    // elements: arg
                    // token labels: arg
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg=new RewriteRuleTokenStream(adaptor,"token arg",arg);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 284:5: -> ^( ATOM ^( SELFKWRD $arg) )
                    {
                        // src/LCPLTreeBuilder.g:284:8: ^( ATOM ^( SELFKWRD $arg) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:284:15: ^( SELFKWRD $arg)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SELFKWRD, "SELFKWRD"), root_2);

                        adaptor.addChild(root_2, stream_arg.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // src/LCPLTreeBuilder.g:286:6: (arg= STRING )
                    {
                    // src/LCPLTreeBuilder.g:286:6: (arg= STRING )
                    // src/LCPLTreeBuilder.g:286:7: arg= STRING
                    {
                    arg=(Token)match(input,STRING,FOLLOW_STRING_in_atom2383); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STRING.add(arg);


                    }



                    // AST REWRITE
                    // elements: arg
                    // token labels: arg
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_arg=new RewriteRuleTokenStream(adaptor,"token arg",arg);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 288:5: -> ^( ATOM ^( STRINGCONST $arg) )
                    {
                        // src/LCPLTreeBuilder.g:288:8: ^( ATOM ^( STRINGCONST $arg) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATOM, "ATOM"), root_1);

                        // src/LCPLTreeBuilder.g:288:15: ^( STRINGCONST $arg)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRINGCONST, "STRINGCONST"), root_2);

                        adaptor.addChild(root_2, stream_arg.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 7 :
                    // src/LCPLTreeBuilder.g:290:6: ( ( MINUS ) arg= INT )
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // src/LCPLTreeBuilder.g:290:6: ( ( MINUS ) arg= INT )
                    // src/LCPLTreeBuilder.g:290:7: ( MINUS ) arg= INT
                    {
                    // src/LCPLTreeBuilder.g:290:7: ( MINUS )
                    // src/LCPLTreeBuilder.g:290:8: MINUS
                    {
                    MINUS80=(Token)match(input,MINUS,FOLLOW_MINUS_in_atom2413); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    MINUS80_tree = (CommonTree)adaptor.create(MINUS80);
                    root_0 = (CommonTree)adaptor.becomeRoot(MINUS80_tree, root_0);
                    }

                    }

                    arg=(Token)match(input,INT,FOLLOW_INT_in_atom2419); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    arg_tree = (CommonTree)adaptor.create(arg);
                    adaptor.addChild(root_0, arg_tree);
                    }

                    }


                    }
                    break;
                case 8 :
                    // src/LCPLTreeBuilder.g:291:7: ( ( NEW ) arg= ID )
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // src/LCPLTreeBuilder.g:291:7: ( ( NEW ) arg= ID )
                    // src/LCPLTreeBuilder.g:291:8: ( NEW ) arg= ID
                    {
                    // src/LCPLTreeBuilder.g:291:8: ( NEW )
                    // src/LCPLTreeBuilder.g:291:9: NEW
                    {
                    NEW81=(Token)match(input,NEW,FOLLOW_NEW_in_atom2430); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NEW81_tree = (CommonTree)adaptor.create(NEW81);
                    root_0 = (CommonTree)adaptor.becomeRoot(NEW81_tree, root_0);
                    }

                    }

                    arg=(Token)match(input,ID,FOLLOW_ID_in_atom2437); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    arg_tree = (CommonTree)adaptor.create(arg);
                    adaptor.addChild(root_0, arg_tree);
                    }

                    }


                    }
                    break;
                case 9 :
                    // src/LCPLTreeBuilder.g:293:8: dispatch
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_dispatch_in_atom2448);
                    dispatch82=dispatch();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, dispatch82.getTree());

                    }
                    break;
                case 10 :
                    // src/LCPLTreeBuilder.g:294:7: staticdispatch
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_staticdispatch_in_atom2456);
                    staticdispatch83=staticdispatch();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, staticdispatch83.getTree());

                    }
                    break;
                case 11 :
                    // src/LCPLTreeBuilder.g:295:7: cast
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_cast_in_atom2464);
                    cast84=cast();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, cast84.getTree());

                    }
                    break;
                case 12 :
                    // src/LCPLTreeBuilder.g:296:7: ( ifstatement )=> ifstatement
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_ifstatement_in_atom2477);
                    ifstatement85=ifstatement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ifstatement85.getTree());

                    }
                    break;
                case 13 :
                    // src/LCPLTreeBuilder.g:297:7: whilestatement
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_whilestatement_in_atom2485);
                    whilestatement86=whilestatement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, whilestatement86.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 24, atom_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "atom"

    public static class expr_aritm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expr_aritm"
    // src/LCPLTreeBuilder.g:303:1: expr_aritm : asexpr ( ( EQUAL asexpr )=> EQUAL asexpr | ( LESSTHAN asexpr )=> LESSTHAN asexpr | ( LESSTHANEQUAL asexpr )=> LESSTHANEQUAL asexpr )* ;
    public final LCPLTreeBuilderParser.expr_aritm_return expr_aritm() throws RecognitionException {
        LCPLTreeBuilderParser.expr_aritm_return retval = new LCPLTreeBuilderParser.expr_aritm_return();
        retval.start = input.LT(1);
        int expr_aritm_StartIndex = input.index();
        CommonTree root_0 = null;

        Token EQUAL88=null;
        Token LESSTHAN90=null;
        Token LESSTHANEQUAL92=null;
        LCPLTreeBuilderParser.asexpr_return asexpr87 = null;

        LCPLTreeBuilderParser.asexpr_return asexpr89 = null;

        LCPLTreeBuilderParser.asexpr_return asexpr91 = null;

        LCPLTreeBuilderParser.asexpr_return asexpr93 = null;


        CommonTree EQUAL88_tree=null;
        CommonTree LESSTHAN90_tree=null;
        CommonTree LESSTHANEQUAL92_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return retval; }
            // src/LCPLTreeBuilder.g:303:11: ( asexpr ( ( EQUAL asexpr )=> EQUAL asexpr | ( LESSTHAN asexpr )=> LESSTHAN asexpr | ( LESSTHANEQUAL asexpr )=> LESSTHANEQUAL asexpr )* )
            // src/LCPLTreeBuilder.g:304:5: asexpr ( ( EQUAL asexpr )=> EQUAL asexpr | ( LESSTHAN asexpr )=> LESSTHAN asexpr | ( LESSTHANEQUAL asexpr )=> LESSTHANEQUAL asexpr )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_asexpr_in_expr_aritm2514);
            asexpr87=asexpr();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, asexpr87.getTree());
            // src/LCPLTreeBuilder.g:305:6: ( ( EQUAL asexpr )=> EQUAL asexpr | ( LESSTHAN asexpr )=> LESSTHAN asexpr | ( LESSTHANEQUAL asexpr )=> LESSTHANEQUAL asexpr )*
            loop34:
            do {
                int alt34=4;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==EQUAL) && (synpred5_LCPLTreeBuilder())) {
                    alt34=1;
                }
                else if ( (LA34_0==LESSTHAN) && (synpred6_LCPLTreeBuilder())) {
                    alt34=2;
                }
                else if ( (LA34_0==LESSTHANEQUAL) && (synpred7_LCPLTreeBuilder())) {
                    alt34=3;
                }


                switch (alt34) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:306:6: ( EQUAL asexpr )=> EQUAL asexpr
            	    {
            	    EQUAL88=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_expr_aritm2538); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    EQUAL88_tree = (CommonTree)adaptor.create(EQUAL88);
            	    root_0 = (CommonTree)adaptor.becomeRoot(EQUAL88_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_asexpr_in_expr_aritm2541);
            	    asexpr89=asexpr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, asexpr89.getTree());

            	    }
            	    break;
            	case 2 :
            	    // src/LCPLTreeBuilder.g:307:7: ( LESSTHAN asexpr )=> LESSTHAN asexpr
            	    {
            	    LESSTHAN90=(Token)match(input,LESSTHAN,FOLLOW_LESSTHAN_in_expr_aritm2558); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    LESSTHAN90_tree = (CommonTree)adaptor.create(LESSTHAN90);
            	    root_0 = (CommonTree)adaptor.becomeRoot(LESSTHAN90_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_asexpr_in_expr_aritm2561);
            	    asexpr91=asexpr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, asexpr91.getTree());

            	    }
            	    break;
            	case 3 :
            	    // src/LCPLTreeBuilder.g:308:7: ( LESSTHANEQUAL asexpr )=> LESSTHANEQUAL asexpr
            	    {
            	    LESSTHANEQUAL92=(Token)match(input,LESSTHANEQUAL,FOLLOW_LESSTHANEQUAL_in_expr_aritm2578); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    LESSTHANEQUAL92_tree = (CommonTree)adaptor.create(LESSTHANEQUAL92);
            	    root_0 = (CommonTree)adaptor.becomeRoot(LESSTHANEQUAL92_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_asexpr_in_expr_aritm2581);
            	    asexpr93=asexpr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, asexpr93.getTree());

            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 25, expr_aritm_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "expr_aritm"

    public static class asexpr_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "asexpr"
    // src/LCPLTreeBuilder.g:315:1: asexpr : arg1= mdexpr ( ( ADDITION arg2= mdexpr )=> ADDITION arg2= mdexpr | ( MINUS arg2= mdexpr )=> MINUS arg2= mdexpr )* ;
    public final LCPLTreeBuilderParser.asexpr_return asexpr() throws RecognitionException {
        LCPLTreeBuilderParser.asexpr_return retval = new LCPLTreeBuilderParser.asexpr_return();
        retval.start = input.LT(1);
        int asexpr_StartIndex = input.index();
        CommonTree root_0 = null;

        Token ADDITION94=null;
        Token MINUS95=null;
        LCPLTreeBuilderParser.mdexpr_return arg1 = null;

        LCPLTreeBuilderParser.mdexpr_return arg2 = null;


        CommonTree ADDITION94_tree=null;
        CommonTree MINUS95_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return retval; }
            // src/LCPLTreeBuilder.g:315:7: (arg1= mdexpr ( ( ADDITION arg2= mdexpr )=> ADDITION arg2= mdexpr | ( MINUS arg2= mdexpr )=> MINUS arg2= mdexpr )* )
            // src/LCPLTreeBuilder.g:316:5: arg1= mdexpr ( ( ADDITION arg2= mdexpr )=> ADDITION arg2= mdexpr | ( MINUS arg2= mdexpr )=> MINUS arg2= mdexpr )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_mdexpr_in_asexpr2614);
            arg1=mdexpr();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arg1.getTree());
            // src/LCPLTreeBuilder.g:317:5: ( ( ADDITION arg2= mdexpr )=> ADDITION arg2= mdexpr | ( MINUS arg2= mdexpr )=> MINUS arg2= mdexpr )*
            loop35:
            do {
                int alt35=3;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==ADDITION) && (synpred8_LCPLTreeBuilder())) {
                    alt35=1;
                }
                else if ( (LA35_0==MINUS) && (synpred9_LCPLTreeBuilder())) {
                    alt35=2;
                }


                switch (alt35) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:318:5: ( ADDITION arg2= mdexpr )=> ADDITION arg2= mdexpr
            	    {
            	    ADDITION94=(Token)match(input,ADDITION,FOLLOW_ADDITION_in_asexpr2638); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    ADDITION94_tree = (CommonTree)adaptor.create(ADDITION94);
            	    root_0 = (CommonTree)adaptor.becomeRoot(ADDITION94_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_mdexpr_in_asexpr2643);
            	    arg2=mdexpr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arg2.getTree());

            	    }
            	    break;
            	case 2 :
            	    // src/LCPLTreeBuilder.g:319:6: ( MINUS arg2= mdexpr )=> MINUS arg2= mdexpr
            	    {
            	    MINUS95=(Token)match(input,MINUS,FOLLOW_MINUS_in_asexpr2661); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    MINUS95_tree = (CommonTree)adaptor.create(MINUS95);
            	    root_0 = (CommonTree)adaptor.becomeRoot(MINUS95_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_mdexpr_in_asexpr2666);
            	    arg2=mdexpr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arg2.getTree());

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 26, asexpr_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "asexpr"

    public static class mdexpr_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mdexpr"
    // src/LCPLTreeBuilder.g:325:1: mdexpr : arg1= atom2 ( ( MULTIPLY arg2= atom2 )=> MULTIPLY arg2= atom2 | ( DIVIDE arg2= atom2 )=> DIVIDE arg2= atom2 )* ;
    public final LCPLTreeBuilderParser.mdexpr_return mdexpr() throws RecognitionException {
        LCPLTreeBuilderParser.mdexpr_return retval = new LCPLTreeBuilderParser.mdexpr_return();
        retval.start = input.LT(1);
        int mdexpr_StartIndex = input.index();
        CommonTree root_0 = null;

        Token MULTIPLY96=null;
        Token DIVIDE97=null;
        LCPLTreeBuilderParser.atom2_return arg1 = null;

        LCPLTreeBuilderParser.atom2_return arg2 = null;


        CommonTree MULTIPLY96_tree=null;
        CommonTree DIVIDE97_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return retval; }
            // src/LCPLTreeBuilder.g:325:7: (arg1= atom2 ( ( MULTIPLY arg2= atom2 )=> MULTIPLY arg2= atom2 | ( DIVIDE arg2= atom2 )=> DIVIDE arg2= atom2 )* )
            // src/LCPLTreeBuilder.g:326:5: arg1= atom2 ( ( MULTIPLY arg2= atom2 )=> MULTIPLY arg2= atom2 | ( DIVIDE arg2= atom2 )=> DIVIDE arg2= atom2 )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_atom2_in_mdexpr2697);
            arg1=atom2();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arg1.getTree());
            // src/LCPLTreeBuilder.g:327:5: ( ( MULTIPLY arg2= atom2 )=> MULTIPLY arg2= atom2 | ( DIVIDE arg2= atom2 )=> DIVIDE arg2= atom2 )*
            loop36:
            do {
                int alt36=3;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==MULTIPLY) && (synpred10_LCPLTreeBuilder())) {
                    alt36=1;
                }
                else if ( (LA36_0==DIVIDE) && (synpred11_LCPLTreeBuilder())) {
                    alt36=2;
                }


                switch (alt36) {
            	case 1 :
            	    // src/LCPLTreeBuilder.g:328:5: ( MULTIPLY arg2= atom2 )=> MULTIPLY arg2= atom2
            	    {
            	    MULTIPLY96=(Token)match(input,MULTIPLY,FOLLOW_MULTIPLY_in_mdexpr2721); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    MULTIPLY96_tree = (CommonTree)adaptor.create(MULTIPLY96);
            	    root_0 = (CommonTree)adaptor.becomeRoot(MULTIPLY96_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_atom2_in_mdexpr2726);
            	    arg2=atom2();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arg2.getTree());

            	    }
            	    break;
            	case 2 :
            	    // src/LCPLTreeBuilder.g:329:6: ( DIVIDE arg2= atom2 )=> DIVIDE arg2= atom2
            	    {
            	    DIVIDE97=(Token)match(input,DIVIDE,FOLLOW_DIVIDE_in_mdexpr2744); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    DIVIDE97_tree = (CommonTree)adaptor.create(DIVIDE97);
            	    root_0 = (CommonTree)adaptor.becomeRoot(DIVIDE97_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_atom2_in_mdexpr2749);
            	    arg2=atom2();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, arg2.getTree());

            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 27, mdexpr_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "mdexpr"

    public static class atom2_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atom2"
    // src/LCPLTreeBuilder.g:333:1: atom2 : ( ( substr )=> substr | atom );
    public final LCPLTreeBuilderParser.atom2_return atom2() throws RecognitionException {
        LCPLTreeBuilderParser.atom2_return retval = new LCPLTreeBuilderParser.atom2_return();
        retval.start = input.LT(1);
        int atom2_StartIndex = input.index();
        CommonTree root_0 = null;

        LCPLTreeBuilderParser.substr_return substr98 = null;

        LCPLTreeBuilderParser.atom_return atom99 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return retval; }
            // src/LCPLTreeBuilder.g:333:6: ( ( substr )=> substr | atom )
            int alt37=2;
            alt37 = dfa37.predict(input);
            switch (alt37) {
                case 1 :
                    // src/LCPLTreeBuilder.g:334:5: ( substr )=> substr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_substr_in_atom22778);
                    substr98=substr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, substr98.getTree());

                    }
                    break;
                case 2 :
                    // src/LCPLTreeBuilder.g:335:6: atom
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_atom_in_atom22785);
                    atom99=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom99.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 28, atom2_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "atom2"

    // $ANTLR start synpred1_LCPLTreeBuilder
    public final void synpred1_LCPLTreeBuilder_fragment() throws RecognitionException {   
        // src/LCPLTreeBuilder.g:258:7: ( NOT expression )
        // src/LCPLTreeBuilder.g:258:8: NOT expression
        {
        match(input,NOT,FOLLOW_NOT_in_synpred1_LCPLTreeBuilder2137); if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred1_LCPLTreeBuilder2141);
        expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_LCPLTreeBuilder

    // $ANTLR start synpred4_LCPLTreeBuilder
    public final void synpred4_LCPLTreeBuilder_fragment() throws RecognitionException {   
        // src/LCPLTreeBuilder.g:296:7: ( ifstatement )
        // src/LCPLTreeBuilder.g:296:8: ifstatement
        {
        pushFollow(FOLLOW_ifstatement_in_synpred4_LCPLTreeBuilder2473);
        ifstatement();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred4_LCPLTreeBuilder

    // $ANTLR start synpred5_LCPLTreeBuilder
    public final void synpred5_LCPLTreeBuilder_fragment() throws RecognitionException {   
        // src/LCPLTreeBuilder.g:306:6: ( EQUAL asexpr )
        // src/LCPLTreeBuilder.g:306:7: EQUAL asexpr
        {
        match(input,EQUAL,FOLLOW_EQUAL_in_synpred5_LCPLTreeBuilder2530); if (state.failed) return ;
        pushFollow(FOLLOW_asexpr_in_synpred5_LCPLTreeBuilder2533);
        asexpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred5_LCPLTreeBuilder

    // $ANTLR start synpred6_LCPLTreeBuilder
    public final void synpred6_LCPLTreeBuilder_fragment() throws RecognitionException {   
        // src/LCPLTreeBuilder.g:307:7: ( LESSTHAN asexpr )
        // src/LCPLTreeBuilder.g:307:8: LESSTHAN asexpr
        {
        match(input,LESSTHAN,FOLLOW_LESSTHAN_in_synpred6_LCPLTreeBuilder2550); if (state.failed) return ;
        pushFollow(FOLLOW_asexpr_in_synpred6_LCPLTreeBuilder2553);
        asexpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred6_LCPLTreeBuilder

    // $ANTLR start synpred7_LCPLTreeBuilder
    public final void synpred7_LCPLTreeBuilder_fragment() throws RecognitionException {   
        // src/LCPLTreeBuilder.g:308:7: ( LESSTHANEQUAL asexpr )
        // src/LCPLTreeBuilder.g:308:8: LESSTHANEQUAL asexpr
        {
        match(input,LESSTHANEQUAL,FOLLOW_LESSTHANEQUAL_in_synpred7_LCPLTreeBuilder2570); if (state.failed) return ;
        pushFollow(FOLLOW_asexpr_in_synpred7_LCPLTreeBuilder2573);
        asexpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred7_LCPLTreeBuilder

    // $ANTLR start synpred8_LCPLTreeBuilder
    public final void synpred8_LCPLTreeBuilder_fragment() throws RecognitionException {   
        LCPLTreeBuilderParser.mdexpr_return arg2 = null;


        // src/LCPLTreeBuilder.g:318:5: ( ADDITION arg2= mdexpr )
        // src/LCPLTreeBuilder.g:318:6: ADDITION arg2= mdexpr
        {
        match(input,ADDITION,FOLLOW_ADDITION_in_synpred8_LCPLTreeBuilder2628); if (state.failed) return ;
        pushFollow(FOLLOW_mdexpr_in_synpred8_LCPLTreeBuilder2633);
        arg2=mdexpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred8_LCPLTreeBuilder

    // $ANTLR start synpred9_LCPLTreeBuilder
    public final void synpred9_LCPLTreeBuilder_fragment() throws RecognitionException {   
        LCPLTreeBuilderParser.mdexpr_return arg2 = null;


        // src/LCPLTreeBuilder.g:319:6: ( MINUS arg2= mdexpr )
        // src/LCPLTreeBuilder.g:319:7: MINUS arg2= mdexpr
        {
        match(input,MINUS,FOLLOW_MINUS_in_synpred9_LCPLTreeBuilder2651); if (state.failed) return ;
        pushFollow(FOLLOW_mdexpr_in_synpred9_LCPLTreeBuilder2656);
        arg2=mdexpr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred9_LCPLTreeBuilder

    // $ANTLR start synpred10_LCPLTreeBuilder
    public final void synpred10_LCPLTreeBuilder_fragment() throws RecognitionException {   
        LCPLTreeBuilderParser.atom2_return arg2 = null;


        // src/LCPLTreeBuilder.g:328:5: ( MULTIPLY arg2= atom2 )
        // src/LCPLTreeBuilder.g:328:6: MULTIPLY arg2= atom2
        {
        match(input,MULTIPLY,FOLLOW_MULTIPLY_in_synpred10_LCPLTreeBuilder2711); if (state.failed) return ;
        pushFollow(FOLLOW_atom2_in_synpred10_LCPLTreeBuilder2716);
        arg2=atom2();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred10_LCPLTreeBuilder

    // $ANTLR start synpred11_LCPLTreeBuilder
    public final void synpred11_LCPLTreeBuilder_fragment() throws RecognitionException {   
        LCPLTreeBuilderParser.atom2_return arg2 = null;


        // src/LCPLTreeBuilder.g:329:6: ( DIVIDE arg2= atom2 )
        // src/LCPLTreeBuilder.g:329:7: DIVIDE arg2= atom2
        {
        match(input,DIVIDE,FOLLOW_DIVIDE_in_synpred11_LCPLTreeBuilder2734); if (state.failed) return ;
        pushFollow(FOLLOW_atom2_in_synpred11_LCPLTreeBuilder2739);
        arg2=atom2();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred11_LCPLTreeBuilder

    // $ANTLR start synpred12_LCPLTreeBuilder
    public final void synpred12_LCPLTreeBuilder_fragment() throws RecognitionException {   
        // src/LCPLTreeBuilder.g:334:5: ( substr )
        // src/LCPLTreeBuilder.g:334:6: substr
        {
        pushFollow(FOLLOW_substr_in_synpred12_LCPLTreeBuilder2774);
        substr();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_LCPLTreeBuilder

    // Delegated rules

    public final boolean synpred11_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred11_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred10_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_LCPLTreeBuilder() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_LCPLTreeBuilder_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA18 dfa18 = new DFA18(this);
    protected DFA33 dfa33 = new DFA33(this);
    protected DFA37 dfa37 = new DFA37(this);
    static final String DFA18_eotS =
        "\17\uffff";
    static final String DFA18_eofS =
        "\17\uffff";
    static final String DFA18_minS =
        "\1\13\4\uffff\1\13\1\uffff\1\5\1\106\1\uffff\2\46\2\5\1\uffff";
    static final String DFA18_maxS =
        "\1\112\4\uffff\1\112\1\uffff\1\112\1\106\1\uffff\2\46\2\112\1\uffff";
    static final String DFA18_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\7\2\uffff\1\5\4\uffff\1\6";
    static final String DFA18_specialS =
        "\17\uffff}>";
    static final String[] DFA18_transitionS = {
            "\1\3\32\uffff\1\1\31\uffff\1\4\4\uffff\1\5\2\uffff\1\2\1\uffff"+
            "\1\6",
            "",
            "",
            "",
            "",
            "\1\10\32\uffff\1\7\31\uffff\1\11\4\uffff\1\11\2\uffff\1\11"+
            "\1\uffff\1\11",
            "",
            "\1\11\4\uffff\2\11\5\uffff\1\11\5\uffff\1\11\16\uffff\2\11"+
            "\17\uffff\1\11\2\uffff\1\11\5\uffff\1\11\3\uffff\2\11\1\12\2"+
            "\11\1\uffff\1\11",
            "\1\13",
            "",
            "\1\14",
            "\1\15",
            "\1\11\4\uffff\2\11\5\uffff\1\11\5\uffff\1\11\16\uffff\2\11"+
            "\17\uffff\1\11\2\uffff\1\11\5\uffff\1\11\3\uffff\2\11\1\16\2"+
            "\11\1\uffff\1\11",
            "\1\11\4\uffff\2\11\5\uffff\1\11\5\uffff\1\11\16\uffff\2\11"+
            "\17\uffff\1\11\2\uffff\1\11\5\uffff\1\11\3\uffff\2\11\1\16\2"+
            "\11\1\uffff\1\11",
            ""
    };

    static final short[] DFA18_eot = DFA.unpackEncodedString(DFA18_eotS);
    static final short[] DFA18_eof = DFA.unpackEncodedString(DFA18_eofS);
    static final char[] DFA18_min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
    static final char[] DFA18_max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
    static final short[] DFA18_accept = DFA.unpackEncodedString(DFA18_acceptS);
    static final short[] DFA18_special = DFA.unpackEncodedString(DFA18_specialS);
    static final short[][] DFA18_transition;

    static {
        int numStates = DFA18_transitionS.length;
        DFA18_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA18_transition[i] = DFA.unpackEncodedString(DFA18_transitionS[i]);
        }
    }

    class DFA18 extends DFA {

        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA18_eot;
            this.eof = DFA18_eof;
            this.min = DFA18_min;
            this.max = DFA18_max;
            this.accept = DFA18_accept;
            this.special = DFA18_special;
            this.transition = DFA18_transition;
        }
        public String getDescription() {
            return "177:1: objectdispatch : (arg= ID -> ^( ATOM ^( IDENTIFIER $arg) ) | '(' NEW arg2= ID ')' -> ^( NEW $arg2) | arg3= SELF -> ^( ATOM ^( SELFKWRD $arg3) ) | (arg= STRING ) -> ^( ATOM ^( STRINGCONST $arg) ) | dispatch | staticdispatch | cast );";
        }
    }
    static final String DFA33_eotS =
        "\25\uffff";
    static final String DFA33_eofS =
        "\25\uffff";
    static final String DFA33_minS =
        "\1\5\10\uffff\1\13\3\uffff\1\5\1\106\1\uffff\2\46\2\5\1\uffff";
    static final String DFA33_maxS =
        "\1\112\10\uffff\1\112\3\uffff\1\112\1\106\1\uffff\2\46\2\112\1\uffff";
    static final String DFA33_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\uffff\1\13\1\14\1\15"+
        "\2\uffff\1\11\4\uffff\1\12";
    static final String DFA33_specialS =
        "\1\0\24\uffff}>";
    static final String[] DFA33_transitionS = {
            "\1\10\4\uffff\1\4\1\5\5\uffff\1\7\24\uffff\1\1\1\3\17\uffff"+
            "\1\13\2\uffff\1\14\5\uffff\1\6\4\uffff\1\11\2\uffff\1\2\1\uffff"+
            "\1\12",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\16\32\uffff\1\15\31\uffff\1\17\4\uffff\1\17\2\uffff\1\17"+
            "\1\uffff\1\17",
            "",
            "",
            "",
            "\1\17\4\uffff\2\17\5\uffff\1\17\5\uffff\1\17\16\uffff\2\17"+
            "\17\uffff\1\17\2\uffff\1\17\5\uffff\1\17\3\uffff\2\17\1\20\2"+
            "\17\1\uffff\1\17",
            "\1\21",
            "",
            "\1\22",
            "\1\23",
            "\1\17\4\uffff\2\17\5\uffff\1\17\5\uffff\1\17\16\uffff\2\17"+
            "\17\uffff\1\17\2\uffff\1\17\5\uffff\1\17\3\uffff\2\17\1\24\2"+
            "\17\1\uffff\1\17",
            "\1\17\4\uffff\2\17\5\uffff\1\17\5\uffff\1\17\16\uffff\2\17"+
            "\17\uffff\1\17\2\uffff\1\17\5\uffff\1\17\3\uffff\2\17\1\24\2"+
            "\17\1\uffff\1\17",
            ""
    };

    static final short[] DFA33_eot = DFA.unpackEncodedString(DFA33_eotS);
    static final short[] DFA33_eof = DFA.unpackEncodedString(DFA33_eofS);
    static final char[] DFA33_min = DFA.unpackEncodedStringToUnsignedChars(DFA33_minS);
    static final char[] DFA33_max = DFA.unpackEncodedStringToUnsignedChars(DFA33_maxS);
    static final short[] DFA33_accept = DFA.unpackEncodedString(DFA33_acceptS);
    static final short[] DFA33_special = DFA.unpackEncodedString(DFA33_specialS);
    static final short[][] DFA33_transition;

    static {
        int numStates = DFA33_transitionS.length;
        DFA33_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA33_transition[i] = DFA.unpackEncodedString(DFA33_transitionS[i]);
        }
    }

    class DFA33 extends DFA {

        public DFA33(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 33;
            this.eot = DFA33_eot;
            this.eof = DFA33_eof;
            this.min = DFA33_min;
            this.max = DFA33_max;
            this.accept = DFA33_accept;
            this.special = DFA33_special;
            this.transition = DFA33_transition;
        }
        public String getDescription() {
            return "270:1: atom : (arg= ID -> ^( ATOM ^( IDENTIFIER $arg) ) | '(' generalexpression ')' -> generalexpression | arg= INT -> ^( ATOM ^( INTEGER $arg) ) | arg= VOID -> ^( ATOM ^( VOIDKWRD $arg) ) | arg= SELF -> ^( ATOM ^( SELFKWRD $arg) ) | (arg= STRING ) -> ^( ATOM ^( STRINGCONST $arg) ) | ( ( MINUS ) arg= INT ) | ( ( NEW ) arg= ID ) | dispatch | staticdispatch | cast | ( ifstatement )=> ifstatement | whilestatement );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA33_0 = input.LA(1);

                         
                        int index33_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA33_0==ID) ) {s = 1;}

                        else if ( (LA33_0==72) ) {s = 2;}

                        else if ( (LA33_0==INT) ) {s = 3;}

                        else if ( (LA33_0==VOID) ) {s = 4;}

                        else if ( (LA33_0==SELF) ) {s = 5;}

                        else if ( (LA33_0==STRING) ) {s = 6;}

                        else if ( (LA33_0==MINUS) ) {s = 7;}

                        else if ( (LA33_0==NEW) ) {s = 8;}

                        else if ( (LA33_0==69) ) {s = 9;}

                        else if ( (LA33_0==74) ) {s = 10;}

                        else if ( (LA33_0==IF) && (synpred4_LCPLTreeBuilder())) {s = 11;}

                        else if ( (LA33_0==WHILE) ) {s = 12;}

                         
                        input.seek(index33_0);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 33, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA37_eotS =
        "\16\uffff";
    static final String DFA37_eofS =
        "\16\uffff";
    static final String DFA37_minS =
        "\1\5\2\0\13\uffff";
    static final String DFA37_maxS =
        "\1\112\2\0\13\uffff";
    static final String DFA37_acceptS =
        "\3\uffff\1\2\11\uffff\1\1";
    static final String DFA37_specialS =
        "\1\uffff\1\0\1\1\13\uffff}>";
    static final String[] DFA37_transitionS = {
            "\1\3\4\uffff\2\3\5\uffff\1\3\24\uffff\1\1\1\3\17\uffff\1\3\2"+
            "\uffff\1\3\5\uffff\1\3\4\uffff\1\3\2\uffff\1\3\1\uffff\1\2",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA37_eot = DFA.unpackEncodedString(DFA37_eotS);
    static final short[] DFA37_eof = DFA.unpackEncodedString(DFA37_eofS);
    static final char[] DFA37_min = DFA.unpackEncodedStringToUnsignedChars(DFA37_minS);
    static final char[] DFA37_max = DFA.unpackEncodedStringToUnsignedChars(DFA37_maxS);
    static final short[] DFA37_accept = DFA.unpackEncodedString(DFA37_acceptS);
    static final short[] DFA37_special = DFA.unpackEncodedString(DFA37_specialS);
    static final short[][] DFA37_transition;

    static {
        int numStates = DFA37_transitionS.length;
        DFA37_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA37_transition[i] = DFA.unpackEncodedString(DFA37_transitionS[i]);
        }
    }

    class DFA37 extends DFA {

        public DFA37(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 37;
            this.eot = DFA37_eot;
            this.eof = DFA37_eof;
            this.min = DFA37_min;
            this.max = DFA37_max;
            this.accept = DFA37_accept;
            this.special = DFA37_special;
            this.transition = DFA37_transition;
        }
        public String getDescription() {
            return "333:1: atom2 : ( ( substr )=> substr | atom );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA37_1 = input.LA(1);

                         
                        int index37_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_LCPLTreeBuilder()) ) {s = 13;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index37_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA37_2 = input.LA(1);

                         
                        int index37_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_LCPLTreeBuilder()) ) {s = 13;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index37_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 37, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_classdef_in_program589 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_CLASS_in_classdef633 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_classdef637 = new BitSet(new long[]{0x0000004000001300L});
    public static final BitSet FOLLOW_INHERITS_in_classdef640 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_classdef644 = new BitSet(new long[]{0x0000004000001200L});
    public static final BitSet FOLLOW_feature_in_classdef651 = new BitSet(new long[]{0x0000004000001200L});
    public static final BitSet FOLLOW_END_in_classdef657 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_classdef659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeblock_in_feature720 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_method_in_feature727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_attributeblock749 = new BitSet(new long[]{0x0000004000000200L});
    public static final BitSet FOLLOW_declaration_in_attributeblock752 = new BitSet(new long[]{0x0000004000000200L});
    public static final BitSet FOLLOW_END_in_attributeblock756 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_attributeblock758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LOCAL_in_localdefblock796 = new BitSet(new long[]{0x0000004000000200L});
    public static final BitSet FOLLOW_localdef_in_localdefblock799 = new BitSet(new long[]{0x0000004000000200L});
    public static final BitSet FOLLOW_END_in_localdefblock803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_declaration869 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_declaration873 = new BitSet(new long[]{0x048001C000020C20L,0x0000000000000529L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_declaration876 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000529L});
    public static final BitSet FOLLOW_expression_in_declaration883 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_declaration887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_method952 = new BitSet(new long[]{0x0000004000008040L,0x0000000000000010L});
    public static final BitSet FOLLOW_formalparam_in_method958 = new BitSet(new long[]{0x0000004000008040L,0x0000000000000010L});
    public static final BitSet FOLLOW_ARROW_in_method964 = new BitSet(new long[]{0x0000004000008000L});
    public static final BitSet FOLLOW_ID_in_method971 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_COLON_in_method977 = new BitSet(new long[]{0x048000C000822E20L,0x0000000000000521L});
    public static final BitSet FOLLOW_methodexpression_in_method980 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_method982 = new BitSet(new long[]{0x048000C000822E20L,0x0000000000000521L});
    public static final BitSet FOLLOW_END_in_method989 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_method991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_formalparam1066 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_formalparam1073 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_formalparam1083 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_generalexpression_in_methodexpression1135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localdefblock_in_methodexpression1143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_dispatch1162 = new BitSet(new long[]{0x0000004000000800L,0x0000000000000521L});
    public static final BitSet FOLLOW_objectdispatch_in_dispatch1167 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_dispatch1169 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_dispatch1175 = new BitSet(new long[]{0x048000C000820C20L,0x00000000000005B1L});
    public static final BitSet FOLLOW_paramcall_in_dispatch1181 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_68_in_dispatch1186 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_paramcall_in_dispatch1190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_71_in_dispatch1193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_objectdispatch1242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_objectdispatch1266 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEW_in_objectdispatch1268 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_objectdispatch1273 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_73_in_objectdispatch1275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELF_in_objectdispatch1296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_objectdispatch1323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dispatch_in_objectdispatch1361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_staticdispatch_in_objectdispatch1368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cast_in_objectdispatch1375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_staticdispatch1398 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_staticdispatch1402 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_staticdispatch1404 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_staticdispatch1408 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_staticdispatch1410 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_staticdispatch1414 = new BitSet(new long[]{0x048000C000820C20L,0x00000000000005B1L});
    public static final BitSet FOLLOW_paramcall_in_staticdispatch1420 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_68_in_staticdispatch1425 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_paramcall_in_staticdispatch1429 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_71_in_staticdispatch1432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_staticdispatch1481 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_SELF_in_staticdispatch1485 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_staticdispatch1487 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_staticdispatch1491 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_staticdispatch1493 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_staticdispatch1497 = new BitSet(new long[]{0x048000C000820C20L,0x00000000000005B1L});
    public static final BitSet FOLLOW_paramcall_in_staticdispatch1503 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_68_in_staticdispatch1508 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_paramcall_in_staticdispatch1512 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_71_in_staticdispatch1515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifstatement1578 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_generalexpression_in_ifstatement1582 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_THEN_in_ifstatement1584 = new BitSet(new long[]{0x068000C000822E20L,0x0000000000000521L});
    public static final BitSet FOLLOW_methodexpression_in_ifstatement1590 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_ifstatement1592 = new BitSet(new long[]{0x068000C000822E20L,0x0000000000000521L});
    public static final BitSet FOLLOW_ELSE_in_ifstatement1597 = new BitSet(new long[]{0x048000C000822E20L,0x0000000000000521L});
    public static final BitSet FOLLOW_methodexpression_in_ifstatement1602 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_ifstatement1604 = new BitSet(new long[]{0x048000C000822E20L,0x0000000000000521L});
    public static final BitSet FOLLOW_END_in_ifstatement1610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_whilestatement1676 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_generalexpression_in_whilestatement1681 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_LOOP_in_whilestatement1683 = new BitSet(new long[]{0x048000C000822E20L,0x0000000000000521L});
    public static final BitSet FOLLOW_methodexpression_in_whilestatement1688 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_whilestatement1690 = new BitSet(new long[]{0x048000C000822E20L,0x0000000000000521L});
    public static final BitSet FOLLOW_END_in_whilestatement1696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_generalexpression_in_paramcall1751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_stringexpr_in_substr1779 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_substr1783 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_paramcall_in_substr1787 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_68_in_substr1789 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_paramcall_in_substr1793 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_substr1794 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_ID_in_stringexpr1854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cast_in_stringexpr1879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_cast1905 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_cast1909 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_castexpression_in_cast1914 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_cast1915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_generalexpression_in_castexpression1956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_assignment1987 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_assignment1989 = new BitSet(new long[]{0x0000004000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_cast_in_assignment1993 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_localdef2053 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_localdef2057 = new BitSet(new long[]{0x048001C000820C20L,0x0000000000000529L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_localdef2060 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000529L});
    public static final BitSet FOLLOW_generalexpression_in_localdef2067 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_localdef2071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_generalexpression2128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_generalexpression2146 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_expression_in_generalexpression2150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_generalexpression2158 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_generalexpression2168 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_generalexpression_in_generalexpression2171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELF_in_generalexpression2180 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_generalexpression2182 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_generalexpression2184 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_generalexpression2195 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_expression_in_generalexpression2198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expr_aritm_in_expression2226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom2248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_atom2274 = new BitSet(new long[]{0x048000C000820C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_generalexpression_in_atom2276 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_73_in_atom2278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_atom2291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VOID_in_atom2320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELF_in_atom2349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_atom2383 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_atom2413 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_INT_in_atom2419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_atom2430 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_atom2437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dispatch_in_atom2448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_staticdispatch_in_atom2456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cast_in_atom2464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifstatement_in_atom2477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_whilestatement_in_atom2485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_asexpr_in_expr_aritm2514 = new BitSet(new long[]{0x0000000000700002L});
    public static final BitSet FOLLOW_EQUAL_in_expr_aritm2538 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_asexpr_in_expr_aritm2541 = new BitSet(new long[]{0x0000000000700002L});
    public static final BitSet FOLLOW_LESSTHAN_in_expr_aritm2558 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_asexpr_in_expr_aritm2561 = new BitSet(new long[]{0x0000000000700002L});
    public static final BitSet FOLLOW_LESSTHANEQUAL_in_expr_aritm2578 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_asexpr_in_expr_aritm2581 = new BitSet(new long[]{0x0000000000700002L});
    public static final BitSet FOLLOW_mdexpr_in_asexpr2614 = new BitSet(new long[]{0x0000000000030002L});
    public static final BitSet FOLLOW_ADDITION_in_asexpr2638 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_mdexpr_in_asexpr2643 = new BitSet(new long[]{0x0000000000030002L});
    public static final BitSet FOLLOW_MINUS_in_asexpr2661 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_mdexpr_in_asexpr2666 = new BitSet(new long[]{0x0000000000030002L});
    public static final BitSet FOLLOW_atom2_in_mdexpr2697 = new BitSet(new long[]{0x00000000000C0002L});
    public static final BitSet FOLLOW_MULTIPLY_in_mdexpr2721 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_atom2_in_mdexpr2726 = new BitSet(new long[]{0x00000000000C0002L});
    public static final BitSet FOLLOW_DIVIDE_in_mdexpr2744 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_atom2_in_mdexpr2749 = new BitSet(new long[]{0x00000000000C0002L});
    public static final BitSet FOLLOW_substr_in_atom22778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_atom22785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_synpred1_LCPLTreeBuilder2137 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_expression_in_synpred1_LCPLTreeBuilder2141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifstatement_in_synpred4_LCPLTreeBuilder2473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_synpred5_LCPLTreeBuilder2530 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_asexpr_in_synpred5_LCPLTreeBuilder2533 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LESSTHAN_in_synpred6_LCPLTreeBuilder2550 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_asexpr_in_synpred6_LCPLTreeBuilder2553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LESSTHANEQUAL_in_synpred7_LCPLTreeBuilder2570 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_asexpr_in_synpred7_LCPLTreeBuilder2573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ADDITION_in_synpred8_LCPLTreeBuilder2628 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_mdexpr_in_synpred8_LCPLTreeBuilder2633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_synpred9_LCPLTreeBuilder2651 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_mdexpr_in_synpred9_LCPLTreeBuilder2656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MULTIPLY_in_synpred10_LCPLTreeBuilder2711 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_atom2_in_synpred10_LCPLTreeBuilder2716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIVIDE_in_synpred11_LCPLTreeBuilder2734 = new BitSet(new long[]{0x048000C000020C20L,0x0000000000000521L});
    public static final BitSet FOLLOW_atom2_in_synpred11_LCPLTreeBuilder2739 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_substr_in_synpred12_LCPLTreeBuilder2774 = new BitSet(new long[]{0x0000000000000002L});

}