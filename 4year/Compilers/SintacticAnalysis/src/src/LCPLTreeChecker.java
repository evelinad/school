// $ANTLR 3.2 Sep 23, 2009 12:02:23 src/LCPLTreeChecker.g 2013-11-19 14:14:41


    import java.util.LinkedList;

    import ro.pub.cs.lcpl.*;
        import java.util.Set;
    import java.util.LinkedHashMap;

        import java.util.Iterator;
                import java.util.Map;        



import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LCPLTreeChecker extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PROGRAM", "NEW", "ARROW", "CLASS", "INHERITS", "END", "VOID", "SELF", "VAR", "LOCAL", "LOOP", "COLON", "ADDITION", "MINUS", "MULTIPLY", "DIVIDE", "LESSTHAN", "LESSTHANEQUAL", "EQUAL", "NOT", "CLASSDEFS", "FEATURES", "DECLARATIONS", "DECLARATION", "METHOD", "METHODEXPRESSION", "METHODNAME", "FORMALPARAM", "LOCALDEFS", "LOCALDEF", "DISPATCH", "SYMBOL", "ATOM", "STATEMENT", "ID", "INT", "ASSIGNMENT", "NAME", "STRING_CONST", "IDENTIFIER", "PARENT", "INTEGER", "STRINGCONST", "VOIDKWRD", "SELFKWRD", "SUBSTR", "INDEX", "STATICDISPATCH", "PARAMCALLS", "CAST", "WHILEBODY", "IF", "THEN", "ELSE", "WHILE", "WHILEST", "CONDITION", "IFBODY", "ELSEBODY", "IFST", "STRING", "LINE_COMMENT", "WS", "';'", "','", "'['", "'.'", "']'", "'('", "')'", "'{'", "'}'"
    };
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


        public LCPLTreeChecker(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public LCPLTreeChecker(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return LCPLTreeChecker.tokenNames; }
    public String getGrammarFileName() { return "src/LCPLTreeChecker.g"; }








    // $ANTLR start "program"
    // src/LCPLTreeChecker.g:47:1: program returns [Program result] : ^( PROGRAM classdefs ) ;
    public final Program program() throws RecognitionException {
        Program result = null;

        CommonTree PROGRAM2=null;
        LinkedList<LCPLClass> classdefs1 = null;


        try {
            // src/LCPLTreeChecker.g:49:5: ( ^( PROGRAM classdefs ) )
            // src/LCPLTreeChecker.g:49:7: ^( PROGRAM classdefs )
            {
            PROGRAM2=(CommonTree)match(input,PROGRAM,FOLLOW_PROGRAM_in_program79); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_classdefs_in_program81);
            classdefs1=classdefs();

            state._fsp--;


            match(input, Token.UP, null); 
              
                    LinkedList<LCPLClass> classes = new LinkedList<LCPLClass>(); 
                    for(LCPLClass c : classdefs1)
                        classes.add(c); 
                    result =new Program((PROGRAM2!=null?PROGRAM2.getLine():0), classes);  
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "program"


    // $ANTLR start "classdefs"
    // src/LCPLTreeChecker.g:60:1: classdefs returns [LinkedList<LCPLClass> result] : ^( CLASSDEFS ( classdef )* ) ;
    public final LinkedList<LCPLClass> classdefs() throws RecognitionException {
        LinkedList<LCPLClass> result = null;

        LCPLClass classdef3 = null;


        try {
            // src/LCPLTreeChecker.g:61:5: ( ^( CLASSDEFS ( classdef )* ) )
            // src/LCPLTreeChecker.g:62:9: ^( CLASSDEFS ( classdef )* )
            {
            match(input,CLASSDEFS,FOLLOW_CLASSDEFS_in_classdefs123); 

                
                        result = new LinkedList<LCPLClass>(); 
                    

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/LCPLTreeChecker.g:66:9: ( classdef )*
                loop1:
                do {
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==CLASS) ) {
                        alt1=1;
                    }


                    switch (alt1) {
                	case 1 :
                	    // src/LCPLTreeChecker.g:67:13: classdef
                	    {
                	    pushFollow(FOLLOW_classdef_in_classdefs161);
                	    classdef3=classdef();

                	    state._fsp--;

                	     
                	                result.add(classdef3);    
                	            

                	    }
                	    break;

                	default :
                	    break loop1;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "classdefs"


    // $ANTLR start "classdef"
    // src/LCPLTreeChecker.g:80:1: classdef returns [LCPLClass result] : ^( CLASS classname ( inherit )? features ) ;
    public final LCPLClass classdef() throws RecognitionException {
        LCPLClass result = null;

        CommonTree CLASS5=null;
        LinkedList<Feature> features4 = null;

        String classname6 = null;

        String inherit7 = null;


        try {
            // src/LCPLTreeChecker.g:82:5: ( ^( CLASS classname ( inherit )? features ) )
            // src/LCPLTreeChecker.g:82:6: ^( CLASS classname ( inherit )? features )
            {
            CLASS5=(CommonTree)match(input,CLASS,FOLLOW_CLASS_in_classdef236); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_classname_in_classdef238);
            classname6=classname();

            state._fsp--;

            // src/LCPLTreeChecker.g:82:24: ( inherit )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==PARENT) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // src/LCPLTreeChecker.g:82:24: inherit
                    {
                    pushFollow(FOLLOW_inherit_in_classdef240);
                    inherit7=inherit();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_features_in_classdef244);
            features4=features();

            state._fsp--;


            match(input, Token.UP, null); 
             
                    List<Feature> featureslist = new LinkedList<Feature>(); 
                    for(Feature f:features4)
                        featureslist.add(f); 
                    result = new LCPLClass((CLASS5!=null?CLASS5.getLine():0), classname6, inherit7, featureslist); 
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "classdef"


    // $ANTLR start "features"
    // src/LCPLTreeChecker.g:97:1: features returns [LinkedList<Feature> result] : ^( FEATURES ( feature )* ) ;
    public final LinkedList<Feature> features() throws RecognitionException {
        LinkedList<Feature> result = null;

        LinkedList<Feature> feature8 = null;


        try {
            // src/LCPLTreeChecker.g:99:9: ( ^( FEATURES ( feature )* ) )
            // src/LCPLTreeChecker.g:99:10: ^( FEATURES ( feature )* )
            {
            match(input,FEATURES,FOLLOW_FEATURES_in_features301); 


                        result = new LinkedList<Feature>();
                    

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/LCPLTreeChecker.g:103:9: ( feature )*
                loop3:
                do {
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==DECLARATIONS||LA3_0==METHOD) ) {
                        alt3=1;
                    }


                    switch (alt3) {
                	case 1 :
                	    // src/LCPLTreeChecker.g:104:13: feature
                	    {
                	    pushFollow(FOLLOW_feature_in_features335);
                	    feature8=feature();

                	    state._fsp--;


                	                for(Feature f:feature8)
                	                result.add(f);   
                	            

                	    }
                	    break;

                	default :
                	    break loop3;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "features"


    // $ANTLR start "feature"
    // src/LCPLTreeChecker.g:114:1: feature returns [LinkedList<Feature> result] : ( attributeblock | method );
    public final LinkedList<Feature> feature() throws RecognitionException {
        LinkedList<Feature> result = null;

        LinkedList<Feature> attributeblock9 = null;

        Method method10 = null;


        try {
            // src/LCPLTreeChecker.g:115:5: ( attributeblock | method )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==DECLARATIONS) ) {
                alt4=1;
            }
            else if ( (LA4_0==METHOD) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // src/LCPLTreeChecker.g:115:6: attributeblock
                    {
                    pushFollow(FOLLOW_attributeblock_in_feature393);
                    attributeblock9=attributeblock();

                    state._fsp--;


                            
                            result = attributeblock9;
                        

                    }
                    break;
                case 2 :
                    // src/LCPLTreeChecker.g:120:6: method
                    {
                    pushFollow(FOLLOW_method_in_feature406);
                    method10=method();

                    state._fsp--;


                            result =new LinkedList<Feature>();
                            result.add( method10);
                        

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "feature"


    // $ANTLR start "attributeblock"
    // src/LCPLTreeChecker.g:129:1: attributeblock returns [LinkedList<Feature> result] : ^( DECLARATIONS ( globaldeclaration )* ) ;
    public final LinkedList<Feature> attributeblock() throws RecognitionException {
        LinkedList<Feature> result = null;

        Attribute globaldeclaration11 = null;


        try {
            // src/LCPLTreeChecker.g:130:5: ( ^( DECLARATIONS ( globaldeclaration )* ) )
            // src/LCPLTreeChecker.g:131:5: ^( DECLARATIONS ( globaldeclaration )* )
            {
            match(input,DECLARATIONS,FOLLOW_DECLARATIONS_in_attributeblock445); 


                    result = new LinkedList<Feature>();
                
                

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/LCPLTreeChecker.g:136:5: ( globaldeclaration )*
                loop5:
                do {
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==DECLARATION) ) {
                        alt5=1;
                    }


                    switch (alt5) {
                	case 1 :
                	    // src/LCPLTreeChecker.g:137:9: globaldeclaration
                	    {
                	    pushFollow(FOLLOW_globaldeclaration_in_attributeblock468);
                	    globaldeclaration11=globaldeclaration();

                	    state._fsp--;


                	            result.add(globaldeclaration11);  
                	        

                	    }
                	    break;

                	default :
                	    break loop5;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "attributeblock"


    // $ANTLR start "globaldeclaration"
    // src/LCPLTreeChecker.g:144:1: globaldeclaration returns [Attribute result] : ^( DECLARATION type= ID name= ID (value= expression )? ) ;
    public final Attribute globaldeclaration() throws RecognitionException {
        Attribute result = null;

        CommonTree type=null;
        CommonTree name=null;
        Expression value = null;


        try {
            // src/LCPLTreeChecker.g:145:5: ( ^( DECLARATION type= ID name= ID (value= expression )? ) )
            // src/LCPLTreeChecker.g:145:6: ^( DECLARATION type= ID name= ID (value= expression )? )
            {
            match(input,DECLARATION,FOLLOW_DECLARATION_in_globaldeclaration506); 

            match(input, Token.DOWN, null); 
            type=(CommonTree)match(input,ID,FOLLOW_ID_in_globaldeclaration511); 
            name=(CommonTree)match(input,ID,FOLLOW_ID_in_globaldeclaration515); 
            // src/LCPLTreeChecker.g:145:37: (value= expression )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==NEW||(LA6_0>=ADDITION && LA6_0<=NOT)||LA6_0==DISPATCH||LA6_0==ATOM||LA6_0==ASSIGNMENT||LA6_0==SUBSTR||LA6_0==STATICDISPATCH||LA6_0==CAST||LA6_0==IF||LA6_0==WHILE) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // src/LCPLTreeChecker.g:145:38: value= expression
                    {
                    pushFollow(FOLLOW_expression_in_globaldeclaration520);
                    value=expression();

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

                    result = new Attribute((type!=null?type.getLine():0), (name!=null?name.getText():null),(type!=null?type.getText():null),value);
                    
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "globaldeclaration"


    // $ANTLR start "method"
    // src/LCPLTreeChecker.g:153:1: method returns [Method result] : ^( METHOD start= COLON name= ID formalparams (rettype= ID )? methodexpressions ) ;
    public final Method method() throws RecognitionException {
        Method result = null;

        CommonTree start=null;
        CommonTree name=null;
        CommonTree rettype=null;
        LinkedList<FormalParam> formalparams12 = null;

        Block methodexpressions13 = null;


        try {
            // src/LCPLTreeChecker.g:154:5: ( ^( METHOD start= COLON name= ID formalparams (rettype= ID )? methodexpressions ) )
            // src/LCPLTreeChecker.g:154:7: ^( METHOD start= COLON name= ID formalparams (rettype= ID )? methodexpressions )
            {
            match(input,METHOD,FOLLOW_METHOD_in_method566); 

            match(input, Token.DOWN, null); 
            start=(CommonTree)match(input,COLON,FOLLOW_COLON_in_method570); 
            name=(CommonTree)match(input,ID,FOLLOW_ID_in_method574); 
            pushFollow(FOLLOW_formalparams_in_method576);
            formalparams12=formalparams();

            state._fsp--;

            // src/LCPLTreeChecker.g:154:49: (rettype= ID )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==ID) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // src/LCPLTreeChecker.g:154:50: rettype= ID
                    {
                    rettype=(CommonTree)match(input,ID,FOLLOW_ID_in_method581); 

                    }
                    break;

            }

            pushFollow(FOLLOW_methodexpressions_in_method585);
            methodexpressions13=methodexpressions();

            state._fsp--;


            match(input, Token.UP, null); 
             
                    List<FormalParam> parameters = new LinkedList<FormalParam>(); 
                    for(FormalParam fp: formalparams12)    
                         parameters.add(fp);  
                    List<Expression> body = new LinkedList<Expression>();  
                    String returntype;
                    returntype= ((rettype!=null?rettype.getText():null) == null)?"void":(rettype!=null?rettype.getText():null);  
                    result = new Method((start!=null?start.getLine():0), (name!=null?name.getText():null), parameters, returntype, methodexpressions13);          
                  
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "method"


    // $ANTLR start "methodexpressions"
    // src/LCPLTreeChecker.g:169:1: methodexpressions returns [Block result] : ^( METHODEXPRESSION ( methodexpression )* ) ;
    public final Block methodexpressions() throws RecognitionException {
        Block result = null;

        LCPLTreeChecker.methodexpression_return methodexpression14 = null;


        try {
            // src/LCPLTreeChecker.g:170:5: ( ^( METHODEXPRESSION ( methodexpression )* ) )
            // src/LCPLTreeChecker.g:171:9: ^( METHODEXPRESSION ( methodexpression )* )
            {
            match(input,METHODEXPRESSION,FOLLOW_METHODEXPRESSION_in_methodexpressions644); 

                
                       List<Expression> blockbody = new LinkedList<Expression>();
                       LinkedHashMap lhm = new LinkedHashMap();
                       result =new Block(0, blockbody); 
                    

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/LCPLTreeChecker.g:177:9: ( methodexpression )*
                loop8:
                do {
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==NEW||(LA8_0>=ADDITION && LA8_0<=NOT)||LA8_0==LOCALDEFS||LA8_0==DISPATCH||LA8_0==ATOM||LA8_0==ASSIGNMENT||LA8_0==SUBSTR||LA8_0==STATICDISPATCH||LA8_0==CAST||LA8_0==IF||LA8_0==WHILE) ) {
                        alt8=1;
                    }


                    switch (alt8) {
                	case 1 :
                	    // src/LCPLTreeChecker.g:178:13: methodexpression
                	    {
                	    pushFollow(FOLLOW_methodexpression_in_methodexpressions682);
                	    methodexpression14=methodexpression();

                	    state._fsp--;

                	     
                	                lhm.put((methodexpression14!=null?methodexpression14.result:null),new Integer((methodexpression14!=null?methodexpression14.startline:0)));    
                	            

                	    }
                	    break;

                	default :
                	    break loop8;
                    }
                } while (true);


                           int firstline=-1;  
                           LocalDefinition ld=null;

                           blockbody = new LinkedList<Expression>();
                           Block methodblock=new Block(0, blockbody);
                           Set set ;
                           set= (lhm).entrySet();
                           Iterator i = set.iterator();
                           Expression e=null; 

                           while(i.hasNext())
                           {

                                Map.Entry me = (Map.Entry)i.next();
                                if(e == null)
                                {
                                     e = (Expression)me.getKey();
                                     methodblock= new Block(((Integer)me.getValue()).intValue(), blockbody);
                                }
                                else
                                {
                                      e = (Expression)me.getKey();
                                }
                           
                                String classtype = e.getClass().getName();
                                blockbody.add(e);
                                if(classtype.contains("LocalDefinition") == true)
                                {
                                    if(ld!=null)
                                    {
                                        ld.setScope(new Block(blockbody.get(0).getLineNumber(),blockbody));

                                    }
                                    blockbody = new LinkedList<Expression>();

                                    ld=(LocalDefinition)e;
                                    Expression scope = ld.getScope();
                              
                                    while(scope!=null)
                                    {
                                        ld = (LocalDefinition)scope;
                                        scope = ld.getScope();
                                    }
                                }

                           }    
                           if(ld!=null)
                           {
                                if(!blockbody.isEmpty())
                                    ld.setScope(new Block(blockbody.get(0).getLineNumber(),blockbody));
                                else
                                {
                                    blockbody = new LinkedList<Expression>();
                                    ld.setScope(new Block(0,blockbody));
                                } 
                            }
                            result =methodblock ;            
                       

                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "methodexpressions"

    public static class methodexpression_return extends TreeRuleReturnScope {
        public Expression result;
        public int startline;
    };

    // $ANTLR start "methodexpression"
    // src/LCPLTreeChecker.g:246:1: methodexpression returns [Expression result, int startline] : ( expression | localdefblock );
    public final LCPLTreeChecker.methodexpression_return methodexpression() throws RecognitionException {
        LCPLTreeChecker.methodexpression_return retval = new LCPLTreeChecker.methodexpression_return();
        retval.start = input.LT(1);

        Expression expression15 = null;

        LCPLTreeChecker.localdefblock_return localdefblock16 = null;


        try {
            // src/LCPLTreeChecker.g:247:5: ( expression | localdefblock )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==NEW||(LA9_0>=ADDITION && LA9_0<=NOT)||LA9_0==DISPATCH||LA9_0==ATOM||LA9_0==ASSIGNMENT||LA9_0==SUBSTR||LA9_0==STATICDISPATCH||LA9_0==CAST||LA9_0==IF||LA9_0==WHILE) ) {
                alt9=1;
            }
            else if ( (LA9_0==LOCALDEFS) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // src/LCPLTreeChecker.g:248:5: expression
                    {
                    pushFollow(FOLLOW_expression_in_methodexpression773);
                    expression15=expression();

                    state._fsp--;


                            retval.result =expression15;
                            retval.startline =expression15.getLineNumber();
                        

                    }
                    break;
                case 2 :
                    // src/LCPLTreeChecker.g:253:6: localdefblock
                    {
                    pushFollow(FOLLOW_localdefblock_in_methodexpression786);
                    localdefblock16=localdefblock();

                    state._fsp--;


                           retval.result =(localdefblock16!=null?localdefblock16.result:null).get(0);
                           retval.startline =(localdefblock16!=null?localdefblock16.startline:0);
                        

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "methodexpression"

    public static class localdefblock_return extends TreeRuleReturnScope {
        public LinkedList<LocalDefinition> result;
        public int startline;
    };

    // $ANTLR start "localdefblock"
    // src/LCPLTreeChecker.g:261:1: localdefblock returns [LinkedList<LocalDefinition> result, int startline] : ^( LOCALDEFS ( localdef )* ) ;
    public final LCPLTreeChecker.localdefblock_return localdefblock() throws RecognitionException {
        LCPLTreeChecker.localdefblock_return retval = new LCPLTreeChecker.localdefblock_return();
        retval.start = input.LT(1);

        CommonTree LOCALDEFS17=null;
        LocalDefinition localdef18 = null;


        try {
            // src/LCPLTreeChecker.g:262:5: ( ^( LOCALDEFS ( localdef )* ) )
            // src/LCPLTreeChecker.g:262:6: ^( LOCALDEFS ( localdef )* )
            {
            LOCALDEFS17=(CommonTree)match(input,LOCALDEFS,FOLLOW_LOCALDEFS_in_localdefblock821); 



                    int len;
                    retval.result =new LinkedList<LocalDefinition>();
                    retval.startline =(LOCALDEFS17!=null?LOCALDEFS17.getLine():0);
                

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/LCPLTreeChecker.g:269:5: ( localdef )*
                loop10:
                do {
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==LOCALDEF) ) {
                        alt10=1;
                    }


                    switch (alt10) {
                	case 1 :
                	    // src/LCPLTreeChecker.g:269:6: localdef
                	    {
                	    pushFollow(FOLLOW_localdef_in_localdefblock835);
                	    localdef18=localdef();

                	    state._fsp--;


                	            retval.result.add(localdef18);
                	        

                	    }
                	    break;

                	default :
                	    break loop10;
                    }
                } while (true);


                    
                    len=retval.result.size();
                    if(len>=2)
                    {
                        for(int i = 0;i<len-1;i++)
                          {
                              retval.result.get(i).setScope((Expression)retval.result.get(i+1));
                           }
                                    
                     }
                     retval.result.get(len-1).setScope(null);
                    
                    

                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "localdefblock"


    // $ANTLR start "localdef"
    // src/LCPLTreeChecker.g:292:1: localdef returns [LocalDefinition result] : ^( LOCALDEF type= ID name= ID (value= expression )? ) ;
    public final LocalDefinition localdef() throws RecognitionException {
        LocalDefinition result = null;

        CommonTree type=null;
        CommonTree name=null;
        CommonTree LOCALDEF19=null;
        Expression value = null;


        try {
            // src/LCPLTreeChecker.g:293:5: ( ^( LOCALDEF type= ID name= ID (value= expression )? ) )
            // src/LCPLTreeChecker.g:293:6: ^( LOCALDEF type= ID name= ID (value= expression )? )
            {
            LOCALDEF19=(CommonTree)match(input,LOCALDEF,FOLLOW_LOCALDEF_in_localdef907); 

            match(input, Token.DOWN, null); 
            type=(CommonTree)match(input,ID,FOLLOW_ID_in_localdef912); 
            name=(CommonTree)match(input,ID,FOLLOW_ID_in_localdef917); 
            // src/LCPLTreeChecker.g:293:35: (value= expression )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==NEW||(LA11_0>=ADDITION && LA11_0<=NOT)||LA11_0==DISPATCH||LA11_0==ATOM||LA11_0==ASSIGNMENT||LA11_0==SUBSTR||LA11_0==STATICDISPATCH||LA11_0==CAST||LA11_0==IF||LA11_0==WHILE) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // src/LCPLTreeChecker.g:293:36: value= expression
                    {
                    pushFollow(FOLLOW_expression_in_localdef922);
                    value=expression();

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 



                    result =new LocalDefinition((LOCALDEF19!=null?LOCALDEF19.getLine():0),(name!=null?name.getText():null),(type!=null?type.getText():null),value,null ); 

                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "localdef"


    // $ANTLR start "formalparams"
    // src/LCPLTreeChecker.g:302:1: formalparams returns [LinkedList<FormalParam> result] : ^( FORMALPARAM ( formalparam )* ) ;
    public final LinkedList<FormalParam> formalparams() throws RecognitionException {
        LinkedList<FormalParam> result = null;

        FormalParam formalparam20 = null;


        try {
            // src/LCPLTreeChecker.g:303:9: ( ^( FORMALPARAM ( formalparam )* ) )
            // src/LCPLTreeChecker.g:303:11: ^( FORMALPARAM ( formalparam )* )
            {
            match(input,FORMALPARAM,FOLLOW_FORMALPARAM_in_formalparams965); 

             
                        result = new LinkedList<FormalParam>(); 
                    

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/LCPLTreeChecker.g:307:9: ( formalparam )*
                loop12:
                do {
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==FORMALPARAM) ) {
                        alt12=1;
                    }


                    switch (alt12) {
                	case 1 :
                	    // src/LCPLTreeChecker.g:308:13: formalparam
                	    {
                	    pushFollow(FOLLOW_formalparam_in_formalparams1003);
                	    formalparam20=formalparam();

                	    state._fsp--;

                	     
                	                result.add(formalparam20);     
                	            

                	    }
                	    break;

                	default :
                	    break loop12;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "formalparams"


    // $ANTLR start "formalparam"
    // src/LCPLTreeChecker.g:318:1: formalparam returns [FormalParam result] : ^( FORMALPARAM type= ID name= ID ) ;
    public final FormalParam formalparam() throws RecognitionException {
        FormalParam result = null;

        CommonTree type=null;
        CommonTree name=null;

        try {
            // src/LCPLTreeChecker.g:319:5: ( ^( FORMALPARAM type= ID name= ID ) )
            // src/LCPLTreeChecker.g:319:7: ^( FORMALPARAM type= ID name= ID )
            {
            match(input,FORMALPARAM,FOLLOW_FORMALPARAM_in_formalparam1094); 

            match(input, Token.DOWN, null); 
            type=(CommonTree)match(input,ID,FOLLOW_ID_in_formalparam1098); 
            name=(CommonTree)match(input,ID,FOLLOW_ID_in_formalparam1102); 

            match(input, Token.UP, null); 
             
                    result = new FormalParam((name!=null?name.getText():null), (type!=null?type.getText():null) ); 
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "formalparam"


    // $ANTLR start "expression"
    // src/LCPLTreeChecker.g:327:1: expression returns [Expression result] : ( ^( ADDITION arg1= expression arg2= expression ) | ^( MINUS arg1= expression arg2= expression ) | ^( MULTIPLY arg1= expression arg2= expression ) | ^( DIVIDE arg1= expression arg2= expression ) | ^( LESSTHAN arg1= expression arg2= expression ) | ^( LESSTHANEQUAL arg1= expression arg2= expression ) | ^( MINUS INT ) | ^( NOT arg1= expression ) | ^( ASSIGNMENT arg4= ID arg2= expression ) | ^( ASSIGNMENT arg7= ( SELF '.' ID ) arg2= expression ) | ^( EQUAL arg1= expression arg2= expression ) | ^( ATOM identifier ) | ^( ATOM integer ) | ^( NEW arg4= ID ) | ^( ATOM voidkwrd ) | ^( ATOM selfkwrd ) | ^( ATOM stringconst ) | ^( DISPATCH (object= expression )? name= ID arg5= paramcalls ) | ^( STATICDISPATCH (object= expression )? type= ID name= ID arg5= paramcalls ) | ^( CAST type= ID arg6= expression ) | ^( IF ^( CONDITION cond= expression ) ^( IFBODY me1= methodexpressions ) ^( ELSEBODY me2= methodexpressions ) ) | ^( WHILE ^( CONDITION cond= expression ) ^( WHILEBODY me= methodexpressions ) ) | ^( SUBSTR stringexpr= expression start= paramcalls fin= paramcalls ) );
    public final Expression expression() throws RecognitionException {
        Expression result = null;

        CommonTree arg4=null;
        CommonTree arg7=null;
        CommonTree name=null;
        CommonTree type=null;
        CommonTree ADDITION21=null;
        CommonTree MINUS22=null;
        CommonTree MULTIPLY23=null;
        CommonTree DIVIDE24=null;
        CommonTree LESSTHAN25=null;
        CommonTree LESSTHANEQUAL26=null;
        CommonTree MINUS27=null;
        CommonTree INT28=null;
        CommonTree NOT29=null;
        CommonTree ASSIGNMENT30=null;
        CommonTree ASSIGNMENT31=null;
        CommonTree ID32=null;
        CommonTree EQUAL33=null;
        CommonTree NEW36=null;
        CommonTree DISPATCH40=null;
        CommonTree STATICDISPATCH41=null;
        CommonTree CAST42=null;
        CommonTree IF43=null;
        CommonTree WHILE44=null;
        CommonTree SUBSTR45=null;
        Expression arg1 = null;

        Expression arg2 = null;

        Expression object = null;

        LinkedList<Expression> arg5 = null;

        Expression arg6 = null;

        Expression cond = null;

        Block me1 = null;

        Block me2 = null;

        Block me = null;

        Expression stringexpr = null;

        LinkedList<Expression> start = null;

        LinkedList<Expression> fin = null;

        Symbol identifier34 = null;

        IntConstant integer35 = null;

        VoidConstant voidkwrd37 = null;

        Symbol selfkwrd38 = null;

        StringConstant stringconst39 = null;


        try {
            // src/LCPLTreeChecker.g:329:5: ( ^( ADDITION arg1= expression arg2= expression ) | ^( MINUS arg1= expression arg2= expression ) | ^( MULTIPLY arg1= expression arg2= expression ) | ^( DIVIDE arg1= expression arg2= expression ) | ^( LESSTHAN arg1= expression arg2= expression ) | ^( LESSTHANEQUAL arg1= expression arg2= expression ) | ^( MINUS INT ) | ^( NOT arg1= expression ) | ^( ASSIGNMENT arg4= ID arg2= expression ) | ^( ASSIGNMENT arg7= ( SELF '.' ID ) arg2= expression ) | ^( EQUAL arg1= expression arg2= expression ) | ^( ATOM identifier ) | ^( ATOM integer ) | ^( NEW arg4= ID ) | ^( ATOM voidkwrd ) | ^( ATOM selfkwrd ) | ^( ATOM stringconst ) | ^( DISPATCH (object= expression )? name= ID arg5= paramcalls ) | ^( STATICDISPATCH (object= expression )? type= ID name= ID arg5= paramcalls ) | ^( CAST type= ID arg6= expression ) | ^( IF ^( CONDITION cond= expression ) ^( IFBODY me1= methodexpressions ) ^( ELSEBODY me2= methodexpressions ) ) | ^( WHILE ^( CONDITION cond= expression ) ^( WHILEBODY me= methodexpressions ) ) | ^( SUBSTR stringexpr= expression start= paramcalls fin= paramcalls ) )
            int alt15=23;
            alt15 = dfa15.predict(input);
            switch (alt15) {
                case 1 :
                    // src/LCPLTreeChecker.g:329:6: ^( ADDITION arg1= expression arg2= expression )
                    {
                    ADDITION21=(CommonTree)match(input,ADDITION,FOLLOW_ADDITION_in_expression1157); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1161);
                    arg1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression1165);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 

                            result =new Addition((ADDITION21!=null?ADDITION21.getLine():0),arg1,arg2);

                        

                    }
                    break;
                case 2 :
                    // src/LCPLTreeChecker.g:335:6: ^( MINUS arg1= expression arg2= expression )
                    {
                    MINUS22=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_expression1181); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1185);
                    arg1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression1189);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 

                            result =new Subtraction((MINUS22!=null?MINUS22.getLine():0),arg1,arg2);

                        

                    }
                    break;
                case 3 :
                    // src/LCPLTreeChecker.g:340:6: ^( MULTIPLY arg1= expression arg2= expression )
                    {
                    MULTIPLY23=(CommonTree)match(input,MULTIPLY,FOLLOW_MULTIPLY_in_expression1204); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1208);
                    arg1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression1212);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 

                            result =new Multiplication((MULTIPLY23!=null?MULTIPLY23.getLine():0),arg1,arg2);

                        

                    }
                    break;
                case 4 :
                    // src/LCPLTreeChecker.g:345:6: ^( DIVIDE arg1= expression arg2= expression )
                    {
                    DIVIDE24=(CommonTree)match(input,DIVIDE,FOLLOW_DIVIDE_in_expression1227); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1231);
                    arg1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression1235);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 

                            result =new Division((DIVIDE24!=null?DIVIDE24.getLine():0),arg1,arg2);

                        

                    }
                    break;
                case 5 :
                    // src/LCPLTreeChecker.g:350:6: ^( LESSTHAN arg1= expression arg2= expression )
                    {
                    LESSTHAN25=(CommonTree)match(input,LESSTHAN,FOLLOW_LESSTHAN_in_expression1250); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1254);
                    arg1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression1258);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     
                            result =new LessThan((LESSTHAN25!=null?LESSTHAN25.getLine():0),arg1,arg2); 
                        

                    }
                    break;
                case 6 :
                    // src/LCPLTreeChecker.g:355:6: ^( LESSTHANEQUAL arg1= expression arg2= expression )
                    {
                    LESSTHANEQUAL26=(CommonTree)match(input,LESSTHANEQUAL,FOLLOW_LESSTHANEQUAL_in_expression1282); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1286);
                    arg1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression1290);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     
                            result =new LessThanEqual((LESSTHANEQUAL26!=null?LESSTHANEQUAL26.getLine():0),arg1,arg2); 
                        

                    }
                    break;
                case 7 :
                    // src/LCPLTreeChecker.g:359:6: ^( MINUS INT )
                    {
                    MINUS27=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_expression1307); 

                    match(input, Token.DOWN, null); 
                    INT28=(CommonTree)match(input,INT,FOLLOW_INT_in_expression1309); 

                    match(input, Token.UP, null); 
                     

                            result =new UnaryMinus((MINUS27!=null?MINUS27.getLine():0),new IntConstant((INT28!=null?INT28.getLine():0),Integer.parseInt( (INT28!=null?INT28.getText():null)))); 
                        

                    }
                    break;
                case 8 :
                    // src/LCPLTreeChecker.g:364:7: ^( NOT arg1= expression )
                    {
                    NOT29=(CommonTree)match(input,NOT,FOLLOW_NOT_in_expression1327); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1331);
                    arg1=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     
                            result =new LogicalNegation((NOT29!=null?NOT29.getLine():0),arg1); 
                        

                    }
                    break;
                case 9 :
                    // src/LCPLTreeChecker.g:368:6: ^( ASSIGNMENT arg4= ID arg2= expression )
                    {
                    ASSIGNMENT30=(CommonTree)match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_expression1347); 

                    match(input, Token.DOWN, null); 
                    arg4=(CommonTree)match(input,ID,FOLLOW_ID_in_expression1351); 
                    pushFollow(FOLLOW_expression_in_expression1355);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     
                            result =new Assignment((ASSIGNMENT30!=null?ASSIGNMENT30.getLine():0),(arg4!=null?arg4.getText():null),arg2); 
                        

                    }
                    break;
                case 10 :
                    // src/LCPLTreeChecker.g:372:6: ^( ASSIGNMENT arg7= ( SELF '.' ID ) arg2= expression )
                    {
                    ASSIGNMENT31=(CommonTree)match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_expression1378); 

                    match(input, Token.DOWN, null); 
                    // src/LCPLTreeChecker.g:372:24: ( SELF '.' ID )
                    // src/LCPLTreeChecker.g:372:25: SELF '.' ID
                    {
                    match(input,SELF,FOLLOW_SELF_in_expression1383); 
                    match(input,70,FOLLOW_70_in_expression1384); 
                    ID32=(CommonTree)match(input,ID,FOLLOW_ID_in_expression1385); 

                    }

                    pushFollow(FOLLOW_expression_in_expression1390);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     
                            result =new Assignment((ASSIGNMENT31!=null?ASSIGNMENT31.getLine():0),"self."+(ID32!=null?ID32.getText():null),arg2); 
                        

                    }
                    break;
                case 11 :
                    // src/LCPLTreeChecker.g:377:6: ^( EQUAL arg1= expression arg2= expression )
                    {
                    EQUAL33=(CommonTree)match(input,EQUAL,FOLLOW_EQUAL_in_expression1414); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1418);
                    arg1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression1422);
                    arg2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     
                            result =new EqualComparison((EQUAL33!=null?EQUAL33.getLine():0),arg1,arg2); 
                        

                    }
                    break;
                case 12 :
                    // src/LCPLTreeChecker.g:382:6: ^( ATOM identifier )
                    {
                    match(input,ATOM,FOLLOW_ATOM_in_expression1445); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_identifier_in_expression1447);
                    identifier34=identifier();

                    state._fsp--;


                    match(input, Token.UP, null); 

                            result =identifier34;
                        

                    }
                    break;
                case 13 :
                    // src/LCPLTreeChecker.g:387:7: ^( ATOM integer )
                    {
                    match(input,ATOM,FOLLOW_ATOM_in_expression1473); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_integer_in_expression1475);
                    integer35=integer();

                    state._fsp--;


                    match(input, Token.UP, null); 

                            result =integer35;

                        

                    }
                    break;
                case 14 :
                    // src/LCPLTreeChecker.g:392:7: ^( NEW arg4= ID )
                    {
                    NEW36=(CommonTree)match(input,NEW,FOLLOW_NEW_in_expression1495); 

                    match(input, Token.DOWN, null); 
                    arg4=(CommonTree)match(input,ID,FOLLOW_ID_in_expression1499); 

                    match(input, Token.UP, null); 


                            result =new NewObject((NEW36!=null?NEW36.getLine():0),(arg4!=null?arg4.getText():null));

                        

                    }
                    break;
                case 15 :
                    // src/LCPLTreeChecker.g:399:6: ^( ATOM voidkwrd )
                    {
                    match(input,ATOM,FOLLOW_ATOM_in_expression1525); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_voidkwrd_in_expression1527);
                    voidkwrd37=voidkwrd();

                    state._fsp--;


                    match(input, Token.UP, null); 

                            result =voidkwrd37;
                        

                    }
                    break;
                case 16 :
                    // src/LCPLTreeChecker.g:404:6: ^( ATOM selfkwrd )
                    {
                    match(input,ATOM,FOLLOW_ATOM_in_expression1552); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_selfkwrd_in_expression1554);
                    selfkwrd38=selfkwrd();

                    state._fsp--;


                    match(input, Token.UP, null); 

                        
                            result =selfkwrd38;

                        

                    }
                    break;
                case 17 :
                    // src/LCPLTreeChecker.g:413:6: ^( ATOM stringconst )
                    {
                    match(input,ATOM,FOLLOW_ATOM_in_expression1582); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_stringconst_in_expression1584);
                    stringconst39=stringconst();

                    state._fsp--;


                    match(input, Token.UP, null); 


                         result = stringconst39;

                        

                    }
                    break;
                case 18 :
                    // src/LCPLTreeChecker.g:422:5: ^( DISPATCH (object= expression )? name= ID arg5= paramcalls )
                    {
                    DISPATCH40=(CommonTree)match(input,DISPATCH,FOLLOW_DISPATCH_in_expression1613); 

                    match(input, Token.DOWN, null); 
                    // src/LCPLTreeChecker.g:422:16: (object= expression )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==NEW||(LA13_0>=ADDITION && LA13_0<=NOT)||LA13_0==DISPATCH||LA13_0==ATOM||LA13_0==ASSIGNMENT||LA13_0==SUBSTR||LA13_0==STATICDISPATCH||LA13_0==CAST||LA13_0==IF||LA13_0==WHILE) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // src/LCPLTreeChecker.g:422:17: object= expression
                            {
                            pushFollow(FOLLOW_expression_in_expression1618);
                            object=expression();

                            state._fsp--;


                            }
                            break;

                    }

                    name=(CommonTree)match(input,ID,FOLLOW_ID_in_expression1624); 
                    pushFollow(FOLLOW_paramcalls_in_expression1628);
                    arg5=paramcalls();

                    state._fsp--;


                    match(input, Token.UP, null); 


                            result = new Dispatch((DISPATCH40!=null?DISPATCH40.getLine():0),object,(name!=null?name.getText():null),arg5);

                        

                    }
                    break;
                case 19 :
                    // src/LCPLTreeChecker.g:431:6: ^( STATICDISPATCH (object= expression )? type= ID name= ID arg5= paramcalls )
                    {
                    STATICDISPATCH41=(CommonTree)match(input,STATICDISPATCH,FOLLOW_STATICDISPATCH_in_expression1661); 

                    match(input, Token.DOWN, null); 
                    // src/LCPLTreeChecker.g:431:23: (object= expression )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==NEW||(LA14_0>=ADDITION && LA14_0<=NOT)||LA14_0==DISPATCH||LA14_0==ATOM||LA14_0==ASSIGNMENT||LA14_0==SUBSTR||LA14_0==STATICDISPATCH||LA14_0==CAST||LA14_0==IF||LA14_0==WHILE) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // src/LCPLTreeChecker.g:431:24: object= expression
                            {
                            pushFollow(FOLLOW_expression_in_expression1666);
                            object=expression();

                            state._fsp--;


                            }
                            break;

                    }

                    type=(CommonTree)match(input,ID,FOLLOW_ID_in_expression1672); 
                    name=(CommonTree)match(input,ID,FOLLOW_ID_in_expression1676); 
                    pushFollow(FOLLOW_paramcalls_in_expression1680);
                    arg5=paramcalls();

                    state._fsp--;


                    match(input, Token.UP, null); 


                             result = new StaticDispatch((STATICDISPATCH41!=null?STATICDISPATCH41.getLine():0),object,(type!=null?type.getText():null),(name!=null?name.getText():null),arg5);

                        

                    }
                    break;
                case 20 :
                    // src/LCPLTreeChecker.g:439:6: ^( CAST type= ID arg6= expression )
                    {
                    CAST42=(CommonTree)match(input,CAST,FOLLOW_CAST_in_expression1705); 

                    match(input, Token.DOWN, null); 
                    type=(CommonTree)match(input,ID,FOLLOW_ID_in_expression1710); 
                    pushFollow(FOLLOW_expression_in_expression1714);
                    arg6=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 


                             result = new Cast((CAST42!=null?CAST42.getLine():0),(type!=null?type.getText():null),arg6);

                        

                    }
                    break;
                case 21 :
                    // src/LCPLTreeChecker.g:447:6: ^( IF ^( CONDITION cond= expression ) ^( IFBODY me1= methodexpressions ) ^( ELSEBODY me2= methodexpressions ) )
                    {
                    IF43=(CommonTree)match(input,IF,FOLLOW_IF_in_expression1739); 

                    match(input, Token.DOWN, null); 
                    match(input,CONDITION,FOLLOW_CONDITION_in_expression1742); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1746);
                    cond=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    match(input,IFBODY,FOLLOW_IFBODY_in_expression1750); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_methodexpressions_in_expression1754);
                    me1=methodexpressions();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    match(input,ELSEBODY,FOLLOW_ELSEBODY_in_expression1758); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_methodexpressions_in_expression1762);
                    me2=methodexpressions();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 

                             if(me2.getExpressions().isEmpty())
                                result = new IfStatement((IF43!=null?IF43.getLine():0),cond, me1, null);
                            else
                                result = new IfStatement((IF43!=null?IF43.getLine():0),cond, me1, me2);
                            
                        

                    }
                    break;
                case 22 :
                    // src/LCPLTreeChecker.g:456:6: ^( WHILE ^( CONDITION cond= expression ) ^( WHILEBODY me= methodexpressions ) )
                    {
                    WHILE44=(CommonTree)match(input,WHILE,FOLLOW_WHILE_in_expression1784); 

                    match(input, Token.DOWN, null); 
                    match(input,CONDITION,FOLLOW_CONDITION_in_expression1787); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1791);
                    cond=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    match(input,WHILEBODY,FOLLOW_WHILEBODY_in_expression1795); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_methodexpressions_in_expression1799);
                    me=methodexpressions();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 

                            result = new WhileStatement((WHILE44!=null?WHILE44.getLine():0),cond, me);
                        

                    }
                    break;
                case 23 :
                    // src/LCPLTreeChecker.g:461:6: ^( SUBSTR stringexpr= expression start= paramcalls fin= paramcalls )
                    {
                    SUBSTR45=(CommonTree)match(input,SUBSTR,FOLLOW_SUBSTR_in_expression1821); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression1825);
                    stringexpr=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_paramcalls_in_expression1830);
                    start=paramcalls();

                    state._fsp--;

                    pushFollow(FOLLOW_paramcalls_in_expression1835);
                    fin=paramcalls();

                    state._fsp--;


                    match(input, Token.UP, null); 

                            int len = start.size();
                            if(len == 1)
                               result =new SubString((SUBSTR45!=null?SUBSTR45.getLine():0),stringexpr,start.get(0),fin.get(0));
                            else
                                result =new SubString((SUBSTR45!=null?SUBSTR45.getLine():0),new SubString((SUBSTR45!=null?SUBSTR45.getLine():0),stringexpr,start.get(0),fin.get(0)),start.get(1),fin.get(1));
                        

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "expression"


    // $ANTLR start "paramcalls"
    // src/LCPLTreeChecker.g:475:1: paramcalls returns [LinkedList<Expression> result] : ^( PARAMCALLS ( expression )* ) ;
    public final LinkedList<Expression> paramcalls() throws RecognitionException {
        LinkedList<Expression> result = null;

        Expression expression46 = null;


        try {
            // src/LCPLTreeChecker.g:476:5: ( ^( PARAMCALLS ( expression )* ) )
            // src/LCPLTreeChecker.g:477:7: ^( PARAMCALLS ( expression )* )
            {
            match(input,PARAMCALLS,FOLLOW_PARAMCALLS_in_paramcalls1895); 

             
                    result = new LinkedList<Expression>(); 
                

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/LCPLTreeChecker.g:481:6: ( expression )*
                loop16:
                do {
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==NEW||(LA16_0>=ADDITION && LA16_0<=NOT)||LA16_0==DISPATCH||LA16_0==ATOM||LA16_0==ASSIGNMENT||LA16_0==SUBSTR||LA16_0==STATICDISPATCH||LA16_0==CAST||LA16_0==IF||LA16_0==WHILE) ) {
                        alt16=1;
                    }


                    switch (alt16) {
                	case 1 :
                	    // src/LCPLTreeChecker.g:481:10: expression
                	    {
                	    pushFollow(FOLLOW_expression_in_paramcalls1914);
                	    expression46=expression();

                	    state._fsp--;

                	     
                	                result.add(expression46);    
                	        

                	    }
                	    break;

                	default :
                	    break loop16;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "paramcalls"


    // $ANTLR start "classname"
    // src/LCPLTreeChecker.g:490:1: classname returns [String result] : ^( NAME ( ID ) ) ;
    public final String classname() throws RecognitionException {
        String result = null;

        CommonTree ID47=null;

        try {
            // src/LCPLTreeChecker.g:491:5: ( ^( NAME ( ID ) ) )
            // src/LCPLTreeChecker.g:492:5: ^( NAME ( ID ) )
            {
            match(input,NAME,FOLLOW_NAME_in_classname1972); 

            match(input, Token.DOWN, null); 
            // src/LCPLTreeChecker.g:493:5: ( ID )
            // src/LCPLTreeChecker.g:494:9: ID
            {
            ID47=(CommonTree)match(input,ID,FOLLOW_ID_in_classname1988); 

                    result = (ID47!=null?ID47.getText():null);
                

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "classname"


    // $ANTLR start "inherit"
    // src/LCPLTreeChecker.g:501:1: inherit returns [String result] : ^( PARENT ( ID )* ) ;
    public final String inherit() throws RecognitionException {
        String result = null;

        CommonTree ID48=null;

        try {
            // src/LCPLTreeChecker.g:502:5: ( ^( PARENT ( ID )* ) )
            // src/LCPLTreeChecker.g:503:5: ^( PARENT ( ID )* )
            {
            match(input,PARENT,FOLLOW_PARENT_in_inherit2026); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/LCPLTreeChecker.g:504:5: ( ID )*
                loop17:
                do {
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==ID) ) {
                        alt17=1;
                    }


                    switch (alt17) {
                	case 1 :
                	    // src/LCPLTreeChecker.g:505:9: ID
                	    {
                	    ID48=(CommonTree)match(input,ID,FOLLOW_ID_in_inherit2042); 

                	            result = (ID48!=null?ID48.getText():null);
                	        

                	    }
                	    break;

                	default :
                	    break loop17;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "inherit"


    // $ANTLR start "identifier"
    // src/LCPLTreeChecker.g:513:1: identifier returns [Symbol result] : ^( IDENTIFIER ( ID ) ) ;
    public final Symbol identifier() throws RecognitionException {
        Symbol result = null;

        CommonTree ID49=null;

        try {
            // src/LCPLTreeChecker.g:514:5: ( ^( IDENTIFIER ( ID ) ) )
            // src/LCPLTreeChecker.g:515:5: ^( IDENTIFIER ( ID ) )
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_identifier2090); 

            match(input, Token.DOWN, null); 
            // src/LCPLTreeChecker.g:517:5: ( ID )
            // src/LCPLTreeChecker.g:518:9: ID
            {
            ID49=(CommonTree)match(input,ID,FOLLOW_ID_in_identifier2107); 

                    result = new Symbol((ID49!=null?ID49.getLine():0),(ID49!=null?ID49.getText():null));
                

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "identifier"


    // $ANTLR start "integer"
    // src/LCPLTreeChecker.g:527:1: integer returns [IntConstant result] : ^( INTEGER ( INT ) ) ;
    public final IntConstant integer() throws RecognitionException {
        IntConstant result = null;

        CommonTree INT50=null;

        try {
            // src/LCPLTreeChecker.g:528:5: ( ^( INTEGER ( INT ) ) )
            // src/LCPLTreeChecker.g:529:5: ^( INTEGER ( INT ) )
            {
            match(input,INTEGER,FOLLOW_INTEGER_in_integer2148); 

            match(input, Token.DOWN, null); 
            // src/LCPLTreeChecker.g:530:5: ( INT )
            // src/LCPLTreeChecker.g:531:9: INT
            {
            INT50=(CommonTree)match(input,INT,FOLLOW_INT_in_integer2164); 

                    result = new IntConstant((INT50!=null?INT50.getLine():0),Integer.parseInt( (INT50!=null?INT50.getText():null)));
                

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "integer"


    // $ANTLR start "voidkwrd"
    // src/LCPLTreeChecker.g:538:1: voidkwrd returns [VoidConstant result] : ^( VOIDKWRD ( VOID ) ) ;
    public final VoidConstant voidkwrd() throws RecognitionException {
        VoidConstant result = null;

        CommonTree VOID51=null;

        try {
            // src/LCPLTreeChecker.g:539:5: ( ^( VOIDKWRD ( VOID ) ) )
            // src/LCPLTreeChecker.g:540:5: ^( VOIDKWRD ( VOID ) )
            {
            match(input,VOIDKWRD,FOLLOW_VOIDKWRD_in_voidkwrd2205); 

            match(input, Token.DOWN, null); 
            // src/LCPLTreeChecker.g:541:5: ( VOID )
            // src/LCPLTreeChecker.g:542:9: VOID
            {
            VOID51=(CommonTree)match(input,VOID,FOLLOW_VOID_in_voidkwrd2221); 

                    result = new VoidConstant((VOID51!=null?VOID51.getLine():0));
                

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "voidkwrd"


    // $ANTLR start "selfkwrd"
    // src/LCPLTreeChecker.g:551:1: selfkwrd returns [Symbol result] : ^( SELFKWRD ( SELF ) ) ;
    public final Symbol selfkwrd() throws RecognitionException {
        Symbol result = null;

        CommonTree SELF52=null;

        try {
            // src/LCPLTreeChecker.g:552:5: ( ^( SELFKWRD ( SELF ) ) )
            // src/LCPLTreeChecker.g:553:5: ^( SELFKWRD ( SELF ) )
            {
            match(input,SELFKWRD,FOLLOW_SELFKWRD_in_selfkwrd2269); 

            match(input, Token.DOWN, null); 
            // src/LCPLTreeChecker.g:554:5: ( SELF )
            // src/LCPLTreeChecker.g:555:9: SELF
            {
            SELF52=(CommonTree)match(input,SELF,FOLLOW_SELF_in_selfkwrd2285); 

                    result = new Symbol((SELF52!=null?SELF52.getLine():0), (SELF52!=null?SELF52.getText():null));
                

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "selfkwrd"


    // $ANTLR start "newobject"
    // src/LCPLTreeChecker.g:562:1: newobject returns [NewObject result] : ^( NEW ( ID ) ) ;
    public final NewObject newobject() throws RecognitionException {
        NewObject result = null;

        CommonTree ID53=null;

        try {
            // src/LCPLTreeChecker.g:563:5: ( ^( NEW ( ID ) ) )
            // src/LCPLTreeChecker.g:564:5: ^( NEW ( ID ) )
            {
            match(input,NEW,FOLLOW_NEW_in_newobject2324); 

            match(input, Token.DOWN, null); 
            // src/LCPLTreeChecker.g:565:5: ( ID )
            // src/LCPLTreeChecker.g:566:10: ID
            {
            ID53=(CommonTree)match(input,ID,FOLLOW_ID_in_newobject2341); 

                    result = new NewObject((ID53!=null?ID53.getLine():0), (ID53!=null?ID53.getText():null));
                

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "newobject"


    // $ANTLR start "stringconst"
    // src/LCPLTreeChecker.g:573:1: stringconst returns [StringConstant result] : ^( STRINGCONST ( STRING ) ) ;
    public final StringConstant stringconst() throws RecognitionException {
        StringConstant result = null;

        CommonTree STRING54=null;

        try {
            // src/LCPLTreeChecker.g:574:5: ( ^( STRINGCONST ( STRING ) ) )
            // src/LCPLTreeChecker.g:575:5: ^( STRINGCONST ( STRING ) )
            {
            match(input,STRINGCONST,FOLLOW_STRINGCONST_in_stringconst2380); 

            match(input, Token.DOWN, null); 
            // src/LCPLTreeChecker.g:576:5: ( STRING )
            // src/LCPLTreeChecker.g:577:9: STRING
            {
            STRING54=(CommonTree)match(input,STRING,FOLLOW_STRING_in_stringconst2396); 

                     String str = (STRING54!=null?STRING54.getText():null); 
                     str = str.substring(1, str.length() - 1);
                     str = str.replace("\\n", "\n");
                     str = str.replace("\\r", "\r");
                     str = str.replace("\\\"", "\"");  
                     str = str.replace("\\t", "\t");           
                     str = str.replaceAll("\\\\(?![nrt_\"])", ""); 
                     result = new StringConstant((STRING54!=null?STRING54.getLine():0), str);

                

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "stringconst"

    // Delegated rules


    protected DFA15 dfa15 = new DFA15(this);
    static final String DFA15_eotS =
        "\36\uffff";
    static final String DFA15_eofS =
        "\36\uffff";
    static final String DFA15_minS =
        "\1\5\1\uffff\1\2\5\uffff\1\2\1\uffff\1\2\7\uffff\1\5\1\13\1\53\11"+
        "\uffff";
    static final String DFA15_maxS =
        "\1\72\1\uffff\1\2\5\uffff\1\2\1\uffff\1\2\7\uffff\1\72\1\46\1\60"+
        "\11\uffff";
    static final String DFA15_acceptS =
        "\1\uffff\1\1\1\uffff\1\3\1\4\1\5\1\6\1\10\1\uffff\1\13\1\uffff\1"+
        "\16\1\22\1\23\1\24\1\25\1\26\1\27\3\uffff\1\7\1\2\1\11\1\12\1\17"+
        "\1\14\1\15\1\20\1\21";
    static final String DFA15_specialS =
        "\36\uffff}>";
    static final String[] DFA15_transitionS = {
            "\1\13\12\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\11\1\7\12\uffff\1\14"+
            "\1\uffff\1\12\3\uffff\1\10\10\uffff\1\21\1\uffff\1\15\1\uffff"+
            "\1\16\1\uffff\1\17\2\uffff\1\20",
            "",
            "\1\22",
            "",
            "",
            "",
            "",
            "",
            "\1\23",
            "",
            "\1\24",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\26\12\uffff\10\26\12\uffff\1\26\1\uffff\1\26\2\uffff\1\25"+
            "\1\26\10\uffff\1\26\1\uffff\1\26\1\uffff\1\26\1\uffff\1\26\2"+
            "\uffff\1\26",
            "\1\30\32\uffff\1\27",
            "\1\32\1\uffff\1\33\1\35\1\31\1\34",
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

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "327:1: expression returns [Expression result] : ( ^( ADDITION arg1= expression arg2= expression ) | ^( MINUS arg1= expression arg2= expression ) | ^( MULTIPLY arg1= expression arg2= expression ) | ^( DIVIDE arg1= expression arg2= expression ) | ^( LESSTHAN arg1= expression arg2= expression ) | ^( LESSTHANEQUAL arg1= expression arg2= expression ) | ^( MINUS INT ) | ^( NOT arg1= expression ) | ^( ASSIGNMENT arg4= ID arg2= expression ) | ^( ASSIGNMENT arg7= ( SELF '.' ID ) arg2= expression ) | ^( EQUAL arg1= expression arg2= expression ) | ^( ATOM identifier ) | ^( ATOM integer ) | ^( NEW arg4= ID ) | ^( ATOM voidkwrd ) | ^( ATOM selfkwrd ) | ^( ATOM stringconst ) | ^( DISPATCH (object= expression )? name= ID arg5= paramcalls ) | ^( STATICDISPATCH (object= expression )? type= ID name= ID arg5= paramcalls ) | ^( CAST type= ID arg6= expression ) | ^( IF ^( CONDITION cond= expression ) ^( IFBODY me1= methodexpressions ) ^( ELSEBODY me2= methodexpressions ) ) | ^( WHILE ^( CONDITION cond= expression ) ^( WHILEBODY me= methodexpressions ) ) | ^( SUBSTR stringexpr= expression start= paramcalls fin= paramcalls ) );";
        }
    }
 

    public static final BitSet FOLLOW_PROGRAM_in_program79 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_classdefs_in_program81 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CLASSDEFS_in_classdefs123 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_classdef_in_classdefs161 = new BitSet(new long[]{0x0000000000000088L});
    public static final BitSet FOLLOW_CLASS_in_classdef236 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_classname_in_classdef238 = new BitSet(new long[]{0x0000100002000000L});
    public static final BitSet FOLLOW_inherit_in_classdef240 = new BitSet(new long[]{0x0000100002000000L});
    public static final BitSet FOLLOW_features_in_classdef244 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FEATURES_in_features301 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_feature_in_features335 = new BitSet(new long[]{0x0000000014000008L});
    public static final BitSet FOLLOW_attributeblock_in_feature393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_method_in_feature406 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DECLARATIONS_in_attributeblock445 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_globaldeclaration_in_attributeblock468 = new BitSet(new long[]{0x0000000008000008L});
    public static final BitSet FOLLOW_DECLARATION_in_globaldeclaration506 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_globaldeclaration511 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_globaldeclaration515 = new BitSet(new long[]{0x04AA011400FF0028L});
    public static final BitSet FOLLOW_expression_in_globaldeclaration520 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_METHOD_in_method566 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_COLON_in_method570 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_method574 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_formalparams_in_method576 = new BitSet(new long[]{0x0000004020000000L});
    public static final BitSet FOLLOW_ID_in_method581 = new BitSet(new long[]{0x0000004020000000L});
    public static final BitSet FOLLOW_methodexpressions_in_method585 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_METHODEXPRESSION_in_methodexpressions644 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_methodexpression_in_methodexpressions682 = new BitSet(new long[]{0x04AA011500FF0028L});
    public static final BitSet FOLLOW_expression_in_methodexpression773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localdefblock_in_methodexpression786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LOCALDEFS_in_localdefblock821 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_localdef_in_localdefblock835 = new BitSet(new long[]{0x0000000200000008L});
    public static final BitSet FOLLOW_LOCALDEF_in_localdef907 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_localdef912 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_localdef917 = new BitSet(new long[]{0x04AA011400FF0028L});
    public static final BitSet FOLLOW_expression_in_localdef922 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FORMALPARAM_in_formalparams965 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_formalparam_in_formalparams1003 = new BitSet(new long[]{0x0000000080000008L});
    public static final BitSet FOLLOW_FORMALPARAM_in_formalparam1094 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_formalparam1098 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_formalparam1102 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ADDITION_in_expression1157 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1161 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1165 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MINUS_in_expression1181 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1185 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1189 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MULTIPLY_in_expression1204 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1208 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1212 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DIVIDE_in_expression1227 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1231 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1235 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LESSTHAN_in_expression1250 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1254 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1258 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LESSTHANEQUAL_in_expression1282 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1286 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1290 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MINUS_in_expression1307 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_expression1309 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_in_expression1327 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1331 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_expression1347 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_expression1351 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1355 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_expression1378 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SELF_in_expression1383 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_expression1384 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_expression1385 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1390 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EQUAL_in_expression1414 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1418 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1422 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATOM_in_expression1445 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_identifier_in_expression1447 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATOM_in_expression1473 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_integer_in_expression1475 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NEW_in_expression1495 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_expression1499 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATOM_in_expression1525 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_voidkwrd_in_expression1527 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATOM_in_expression1552 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_selfkwrd_in_expression1554 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATOM_in_expression1582 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_stringconst_in_expression1584 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DISPATCH_in_expression1613 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1618 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_expression1624 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_paramcalls_in_expression1628 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STATICDISPATCH_in_expression1661 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1666 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_expression1672 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ID_in_expression1676 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_paramcalls_in_expression1680 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CAST_in_expression1705 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_expression1710 = new BitSet(new long[]{0x04AA011400FF0020L});
    public static final BitSet FOLLOW_expression_in_expression1714 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IF_in_expression1739 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CONDITION_in_expression1742 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1746 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IFBODY_in_expression1750 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_methodexpressions_in_expression1754 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ELSEBODY_in_expression1758 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_methodexpressions_in_expression1762 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHILE_in_expression1784 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CONDITION_in_expression1787 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1791 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHILEBODY_in_expression1795 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_methodexpressions_in_expression1799 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUBSTR_in_expression1821 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression1825 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_paramcalls_in_expression1830 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_paramcalls_in_expression1835 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARAMCALLS_in_paramcalls1895 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_paramcalls1914 = new BitSet(new long[]{0x04AA011400FF0028L});
    public static final BitSet FOLLOW_NAME_in_classname1972 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_classname1988 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARENT_in_inherit2026 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_inherit2042 = new BitSet(new long[]{0x0000004000000008L});
    public static final BitSet FOLLOW_IDENTIFIER_in_identifier2090 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_identifier2107 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INTEGER_in_integer2148 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_integer2164 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VOIDKWRD_in_voidkwrd2205 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VOID_in_voidkwrd2221 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SELFKWRD_in_selfkwrd2269 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SELF_in_selfkwrd2285 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NEW_in_newobject2324 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_newobject2341 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STRINGCONST_in_stringconst2380 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_stringconst2396 = new BitSet(new long[]{0x0000000000000008L});

}