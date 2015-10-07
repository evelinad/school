//DUMITRESCU EVELINA 331CA 
//CPL Tema1

tree grammar LCPLTreeChecker;



options {

    tokenVocab=LCPLTreeBuilder;

    ASTLabelType=CommonTree;

}



@members {



}



@header {

    import java.util.LinkedList;

    import ro.pub.cs.lcpl.*;
        import java.util.Set;
    import java.util.LinkedHashMap;

        import java.util.Iterator;
                import java.util.Map;        

}







/*rule for a program that cand be made of mutiple classes*/

program returns [Program result] 

    : ^(PROGRAM classdefs) 
    {  
        LinkedList<LCPLClass> classes = new LinkedList<LCPLClass>(); 
        for(LCPLClass c : $classdefs.result)
            classes.add(c); 
        $result=new Program($PROGRAM.line, classes);  
    } 
    ;



classdefs returns[LinkedList<LCPLClass> result] 
    : 
        ^(CLASSDEFS  
        {    
            $result = new LinkedList<LCPLClass>(); 
        } 
        ( 
            classdef 
        { 
            $result.add($classdef.result);    
        } 
        )* 
        );     

    

       

/*rules for classes that caninherit a parent class , have attributes and methods*/

classdef returns [LCPLClass result] 

    :^(CLASS classname inherit?  features) 
    { 
        List<Feature> featureslist = new LinkedList<Feature>(); 
        for(Feature f:$features.result)
            featureslist.add(f); 
        $result= new LCPLClass($CLASS.line, $classname.result, $inherit.result, featureslist); 
    }     
    ;    



    

/*feafures from a class,attributes and methods*/    

features returns[LinkedList<Feature> result]

        :^(FEATURES
        {
            $result = new LinkedList<Feature>();
        }
        (
            feature
        {
            for(Feature f:$feature.result)
            $result.add(f);   
        }
        )*
        )
        ;

/*a feature can be a method or an attribute*/
feature  returns [LinkedList<Feature> result]
    :attributeblock
    {
        
        $result = $attributeblock.result;
    }
    |method
    {
        $result =new LinkedList<Feature>();
        $result.add( $method.result);
    }
    ;     


/*class attribute block*/
attributeblock returns[LinkedList<Feature> result]
    :
    ^(DECLARATIONS 
    {
        $result = new LinkedList<Feature>();
    
    }
    (
        globaldeclaration
    {
        $result.add($globaldeclaration.result);  
    }
    )*)
    ;
    
globaldeclaration returns[Attribute result]
    :^(DECLARATION  type=ID name=ID (value=expression)?)
    {
        $result = new Attribute($type.line, $name.text,$type.text,$value.result);
        
    }
    ;
    
/*method rule that can have a name, parameters and any expressions*/     
method returns [Method result]    
    : ^(METHOD start=COLON name=ID formalparams (rettype=ID)? methodexpressions )  
    { 
        List<FormalParam> parameters = new LinkedList<FormalParam>(); 
        for(FormalParam fp: $formalparams.result)    
             parameters.add(fp);  
        List<Expression> body = new LinkedList<Expression>();  
        String returntype;
        returntype= ($rettype.text == null)?"void":$rettype.text;  
        $result = new Method($start.line, $name.text, parameters, returntype, $methodexpressions.result);          
      
    }  
    ; 
    
    
/*a block of method expressions*/    
methodexpressions returns[Block result] 
    : 
        ^(METHODEXPRESSION  
        {    
           List<Expression> blockbody = new LinkedList<Expression>();
           LinkedHashMap lhm = new LinkedHashMap();
           $result=new Block(0, blockbody); 
        } 
        ( 
            methodexpression 
        { 
            lhm.put($methodexpression.result,new Integer($methodexpression.startline));    
        } 
        )* 
        {
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
            $result =methodblock ;            
       }
       ) 
       ;
       
/*a method expression that can be a statement or a local variable declaration block*/           
methodexpression returns[Expression result, int startline]
    :
    expression
    {
        $result=$expression.result;
        $startline=$expression.result.getLineNumber();
    }
    |localdefblock
    {
       $result=$localdefblock.result.get(0);
       $startline=$localdefblock.startline;
    }
    ;
    
    
localdefblock returns[LinkedList<LocalDefinition> result, int startline]
    :^(LOCALDEFS 
    {

        int len;
        $result=new LinkedList<LocalDefinition>();
        $startline=$LOCALDEFS.line;
    }
    (localdef
    {
        $result.add($localdef.result);
    }          
    )*
    {
    
    len=$result.size();
    if(len>=2)
    {
        for(int i = 0;i<len-1;i++)
          {
              $result.get(i).setScope((Expression)$result.get(i+1));
           }
                    
     }
     $result.get(len-1).setScope(null);
    
    })       
    ;
    
    
/*inside local variable declaration from a local block*/    
localdef returns[LocalDefinition result]
    :^(LOCALDEF  type=ID  name=ID (value=expression)?)
    {


        $result=new LocalDefinition($LOCALDEF.line,$name.text,$type.text,$value.result,null ); 

    }
    ;
        
formalparams returns[LinkedList<FormalParam> result] 
        : ^(FORMALPARAM  
        { 
            $result = new LinkedList<FormalParam>(); 
        } 
        ( 
            formalparam 
        { 
            $result.add($formalparam.result);     
        } 
        )* 
         
        ) 
        ; 
        
/*methdo formal parameters*/         
formalparam returns[FormalParam result] 
    : ^(FORMALPARAM type=ID name=ID) 
    { 
        $result = new FormalParam($name.text, $type.text ); 
    } 
    ;               


/* all kind of expressions*/       
expression returns[Expression result]

    :^(ADDITION arg1=expression arg2=expression)

    {
        $result=new Addition($ADDITION.line,$arg1.result,$arg2.result);

    }
    |^(MINUS arg1=expression arg2=expression)
    {
        $result=new Subtraction($MINUS.line,$arg1.result,$arg2.result);

    }
    |^(MULTIPLY arg1=expression arg2=expression)
    {
        $result=new Multiplication($MULTIPLY.line,$arg1.result,$arg2.result);

    }
    |^(DIVIDE arg1=expression arg2=expression)
    {
        $result=new Division($DIVIDE.line,$arg1.result,$arg2.result);

    }
    |^(LESSTHAN arg1=expression arg2=expression) 
    { 
        $result=new LessThan($LESSTHAN.line,$arg1.result,$arg2.result); 
    } 
    /*less than equal*/ 
    |^(LESSTHANEQUAL arg1=expression arg2=expression) 
    { 
        $result=new LessThanEqual($LESSTHANEQUAL.line,$arg1.result,$arg2.result); 
    } 
    |^(MINUS INT) 
    { 

        $result=new UnaryMinus($MINUS.line,new IntConstant($INT.line,Integer.parseInt( $INT.text))); 
    } 
     |^(NOT arg1=expression) 
    { 
        $result=new LogicalNegation($NOT.line,$arg1.result); 
    }
    |^(ASSIGNMENT arg4=ID arg2=expression) 
    { 
        $result=new Assignment($ASSIGNMENT.line,$arg4.text,$arg2.result); 
    }       
    |^(ASSIGNMENT arg7=(SELF'.'ID) arg2=expression) 
    { 
        $result=new Assignment($ASSIGNMENT.line,"self."+$ID.text,$arg2.result); 
    } 
    /*equal*/ 
    |^(EQUAL arg1=expression arg2=expression) 
    { 
        $result=new EqualComparison($EQUAL.line,$arg1.result,$arg2.result); 
    }
     /*ID type*/
    |^(ATOM identifier)    
    {
        $result=$identifier.result;
    }
    /*INT type*/
    | ^(ATOM integer)    
    {
        $result=$integer.result;

    }
    | ^(NEW arg4=ID)    
    {

        $result=new NewObject($NEW.line,$arg4.text);

    }
    /*VOID type*/ 
    |^(ATOM voidkwrd)    
    {
        $result=$voidkwrd.result;
    }
    /*SELF keyword*/
    |^(ATOM selfkwrd)    
    {
    
        $result=$selfkwrd.result;

    }

    /*STRING type*/ 

    |^(ATOM stringconst )   

    {

     $result = $stringconst.result;

    }
   
    /*dispatch*/
   |^(DISPATCH (object=expression)? name=ID arg5=paramcalls)   

    {

        $result = new Dispatch($DISPATCH.line,$object.result,$name.text,$arg5.result);

    }
       
    /*static dispatch*/
    |^(STATICDISPATCH (object=expression)? type=ID name=ID arg5=paramcalls)   

    {

         $result = new StaticDispatch($STATICDISPATCH.line,$object.result,$type.text,$name.text,$arg5.result);

    }
    /*cast*/
    |^(CAST  type=ID arg6=expression)   

    {

         $result = new Cast($CAST.line,$type.text,$arg6.result);

    }
    /*if statement*/
    |^(IF ^(CONDITION cond=expression) ^(IFBODY me1=methodexpressions) ^(ELSEBODY me2=methodexpressions))
    {
         if($me2.result.getExpressions().isEmpty())
            $result = new IfStatement($IF.line,$cond.result, $me1.result, null);
        else
            $result = new IfStatement($IF.line,$cond.result, $me1.result, $me2.result);
        
    }
    /*while statement*/
    |^(WHILE ^(CONDITION cond=expression) ^(WHILEBODY me=methodexpressions))
    {
        $result = new WhileStatement($WHILE.line,$cond.result, $me.result);
    }
    /*substring statement*/
    |^(SUBSTR stringexpr=expression  start=paramcalls  fin=paramcalls)
    {
        int len = $start.result.size();
        if(len == 1)
           $result=new SubString($SUBSTR.line,$stringexpr.result,$start.result.get(0),$fin.result.get(0));
        else
            $result=new SubString($SUBSTR.line,new SubString($SUBSTR.line,$stringexpr.result,$start.result.get(0),$fin.result.get(0)),$start.result.get(1),$fin.result.get(1));
    }
    

    ;
    
     
/*parameters for method call*/     
paramcalls returns [LinkedList<Expression> result] 
    : 
      ^(PARAMCALLS 
    { 
        $result = new LinkedList<Expression>(); 
    } 
     (   expression 
    { 
            $result.add($expression.result);    
    } 
    )* 
    );      

        

classname returns[String result]
    :
    ^(NAME
    (
        ID
    {
        $result = $ID.text;
    }
    ))
    ;

inherit returns[String result]
    :
    ^(PARENT
    (
        ID
    {
        $result = $ID.text;
    }
    )*)
    ;
    
    
identifier returns[Symbol result]
    :
    ^(IDENTIFIER

    (
        ID
    {
        $result = new Symbol($ID.line,$ID.text);
    }
    ))
    ;


/*atoms*/
integer returns[IntConstant result]
    :
    ^(INTEGER
    (
        INT
    {
        $result = new IntConstant($INT.line,Integer.parseInt( $INT.text));
    }
    ))
    ;   

voidkwrd returns[VoidConstant result]
    :
    ^(VOIDKWRD
    (
        VOID
    {
        $result = new VoidConstant($VOID.line);
    }
    ))
    ;    

    

selfkwrd returns[Symbol result]
    :
    ^(SELFKWRD
    (
        SELF
    {
        $result = new Symbol($SELF.line, $SELF.text);
    }
    ))
    ; 

newobject returns[NewObject result]
    :
    ^(NEW
    (
         ID
    {
        $result = new NewObject($ID.line, $ID.text);
    }
    ))
    ; 

stringconst returns[StringConstant result]
    :
    ^(STRINGCONST
    (
        STRING
    {
         String str = $STRING.text; 
         str = str.substring(1, str.length() - 1);
         str = str.replace("\\n", "\n");
         str = str.replace("\\r", "\r");
         str = str.replace("\\\"", "\"");  
         str = str.replace("\\t", "\t");           
         str = str.replaceAll("\\\\(?![nrt_\"])", ""); 
         $result = new StringConstant($STRING.line, str);

    }

    ))

    ;       




