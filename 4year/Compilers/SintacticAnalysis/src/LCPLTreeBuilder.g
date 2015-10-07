//Dumitrescu Evelina 331CA 
//CPL Tema1

grammar LCPLTreeBuilder;



options {

    language = Java;

    output = AST;

    memoize=true;

    ASTLabelType = CommonTree;

    

}

/*allowed tokens*/

tokens {
    PROGRAM;
    NEW='new';
    ARROW='->';
    CLASS = 'class';
    INHERITS = 'inherits';
    END = 'end';
    VOID = 'void';
    SELF='self';
    VAR='var';
    LOCAL='local';
    LOOP='loop';
    COLON=':';
    ADDITION='+';
    MINUS='-';    
    MULTIPLY='*';
    DIVIDE='/';
    LESSTHAN='<';
    LESSTHANEQUAL='<=';
    EQUAL='==';
    NOT='!';
    CLASSDEFS;
    FEATURES;
    DECLARATIONS;
    DECLARATION;       
    METHOD;
    METHODEXPRESSION;
    METHODNAME;
    FORMALPARAM;
    LOCALDEFS;
    LOCALDEF;
    DISPATCH;
    SYMBOL;    
    ATOM;
    STATEMENT;
    ID;
    INT;
    ASSIGNMENT='=';
    NAME;
    STRING_CONST;
    IDENTIFIER;
    PARENT;
    INTEGER;
    STRINGCONST;
    VOIDKWRD;
    SELFKWRD;
    SUBSTR='substring';
    INDEX;
    STATICDISPATCH;
    PARAMCALLS;
    CAST;
    WHILEBODY;
    IF = 'if';
    THEN='then';
    ELSE='else';
    WHILE='while';
    WHILEST;
    CONDITION;
    IFBODY;
    ELSEBODY;
    IFST;

}


@members {

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


}

/*rule for a program*/
program :   (classdef)* -> ^(PROGRAM ^(CLASSDEFS classdef*))
        ;


/*rule for classes*/
classdef : 

       CLASS name=ID (INHERITS parent=ID)?   (feature)* fin1=END ';'->
        ^(CLASS ^(NAME $name) ^(PARENT $parent?)   ^(FEATURES feature*)) 
        ;


/*class components*/
feature:
    attributeblock
    |method
    ;
 
/*global class variables(class attributes)*/ 
attributeblock:
    start=VAR (declaration)* END ';' 
    -> ^(DECLARATIONS  declaration*)   
    ;

/*local variable declaration block*/
localdefblock:
    start=LOCAL (localdef)* END 
    -> ^(LOCALDEFS[$start] localdef*)
    ;
                

/*rule for class attributes*/        
declaration: 
         type=ID name=ID ('=')? (value=expression)? ';'

        -> ^(DECLARATION $type  $name $value?)
        ;

    
/*method rule*/
method  :   
         name=ID   ( formalparam)*  (ARROW)? (rettype=ID)? start=COLON (methodexpression ';' )* fin=END ';'   
        -> ^(METHOD $start $name ^(FORMALPARAM  formalparam*)  $rettype? ^(METHODEXPRESSION methodexpression*) )  
    ;  
 
/*method parameter rule*/
formalparam:     
       ','? type = ID  {isTypeID($type.text)}?  var = ID {isVarNameID($var.text)}?   
       -> ^(FORMALPARAM $type $var)      
    ;   


/*method statements*/
methodexpression:
    generalexpression 
    |localdefblock
    ;


/*method call(dispatch)*/
dispatch:
    '[' (object=objectdispatch '.')? name=ID  (paramcall1=paramcall)? (',' paramcall2+=paramcall)*']'
     -> ^(DISPATCH $object? $name  ^(PARAMCALLS $paramcall1? $paramcall2*))
    ;

objectdispatch:
    arg=ID
    -> ^(ATOM ^(IDENTIFIER $arg))
    |'(' NEW  arg2=ID ')'
    ->^(NEW $arg2)
    |arg3=SELF
    -> ^(ATOM ^(SELFKWRD $arg3))
    |(arg=STRING )
    -> ^(ATOM ^(STRINGCONST $arg))            
    |dispatch
    |staticdispatch
    |cast
    ;
    
/*static method call(static dispatch)*/ 
staticdispatch:
    '[' object=ID '.' type=ID '.' name=ID  (paramcall1=paramcall)? (',' paramcall2+=paramcall)*']'
     -> ^(STATICDISPATCH ^(ATOM ^(IDENTIFIER $object)) $type $name  ^(PARAMCALLS $paramcall1? $paramcall2*))
     |'[' object2=SELF '.' type=ID '.' name=ID  (paramcall1=paramcall)? (',' paramcall2+=paramcall)*']'
     -> ^(STATICDISPATCH ^(ATOM ^(SELFKWRD $object2))  $type $name  ^(PARAMCALLS $paramcall1? $paramcall2*))
    ;   

/*if statement*/
ifstatement:
    IF cond=generalexpression THEN  (me1+=methodexpression ';')* (ELSE (me2+=methodexpression ';')*)? END
    ->^(IF ^(CONDITION $cond) ^(IFBODY ^(METHODEXPRESSION $me1*)) ^(ELSEBODY ^(METHODEXPRESSION $me2*)))
    ;
    
/*while statement*/    
whilestatement:
    WHILE  cond=generalexpression LOOP (me1+=methodexpression ';')*   END  
    ->^(WHILE ^(CONDITION $cond) ^(WHILEBODY ^(METHODEXPRESSION $me1*)) )
    ;
     
/*parameters for dispatch call*/
paramcall:
     generalexpression
    ;
    
/*substring*/    
substr:
    se=stringexpr  ('[' start+=paramcall ',' fin+=paramcall']')+ 
    ->^(SUBSTR $se  ^(PARAMCALLS $start+) ^(PARAMCALLS $fin+))
    ;
    
/*object for substring*/    
stringexpr:
    arg=ID
    -> ^(ATOM ^(IDENTIFIER $arg)) 
    |cast
    ;
    
/*cast statement*/    
cast:
    '{' type=ID  exp=castexpression'}'
    -> ^(CAST $type $exp)
    ;
    
    
castexpression:
    generalexpression
    ;
    
/*variable value assignment*/        
assignment: 
    ID ASSIGNMENT expr=cast 
    ->^(ASSIGNMENT $expr)
    ;    
        

/*variable declaration inside a local block*/        
localdef: 
         type=ID name=ID ('=')? (value=generalexpression)? ';'

        -> ^(LOCALDEF $type  $name $value?)

        ;

/*general expression that cand be statemenets or operands*/       
generalexpression:
     expression
     |(NOT ^ expression) => NOT ^ expression
     |ID (ASSIGNMENT^ generalexpression)=> ASSIGNMENT^ generalexpression
     |(SELF '.' ID) (ASSIGNMENT^ expression)=> ASSIGNMENT^ expression
     ;
     
     
expression:
    expr_aritm
    ;

 
/*atom inside an expression*/
atom:
    arg=ID 
    -> ^(ATOM ^(IDENTIFIER $arg))
    | '(' generalexpression ')' -> generalexpression
    |arg=INT 

    -> ^(ATOM ^(INTEGER $arg))

    |arg=VOID 

    -> ^(ATOM ^(VOIDKWRD $arg))

    |arg=SELF 

    -> ^(ATOM ^(SELFKWRD $arg))    

    |(arg=STRING )

    -> ^(ATOM ^(STRINGCONST $arg))

    |((MINUS^) arg=INT)
    | ((NEW ^) arg=ID)

     | dispatch
     |staticdispatch
     |cast
     |(ifstatement) =>ifstatement
     |whilestatement
     

    ;    

/*arithmetic expressions*/
expr_aritm:
    asexpr 
     (
     (EQUAL^ asexpr) => EQUAL^ asexpr
     |(LESSTHAN^ asexpr) => LESSTHAN^ asexpr
     |(LESSTHANEQUAL^ asexpr) => LESSTHANEQUAL^ asexpr
     )*

    ;


/*addition, subtraction expressions*/   
asexpr:
    arg1=mdexpr 
    (
    (ADDITION^ arg2=mdexpr) => ADDITION^ arg2=mdexpr
    |(MINUS^ arg2=mdexpr) => MINUS^ arg2=mdexpr
    )*
    ;

   
/*multiply division expressions*/
mdexpr:
    arg1=atom2 
    (
    (MULTIPLY^ arg2=atom2) => MULTIPLY^ arg2=atom2
    |(DIVIDE^ arg2=atom2) => DIVIDE^ arg2=atom2
    )* ;    


atom2:
    (substr) =>substr
    |atom
    ;


    
STRING_CONST
    :  

            '"Hello world!"' 
    ;

LINE_COMMENT
  :

  '#'

  ~(

    '\r'

    | '\n'

   )*

  {

   $channel = HIDDEN;

  }

  ;




/*allowed characters in a string*/

STRING

  :

  '"' 
  (~('"' 
  | '\\' 
  | '\r' 
  | '\n') 
  | '\\' ( . | '\r\n'))* 
  '"';


/*integer*/

INT

  :

  ('0'..'9')+

  ;

  

/*identifier*/         

ID  

    :   

        ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*

    ;       

    

/*white space that should be skipped*/ 

WS  : 

  (

    ' ' | '\r' | '\t' | '\n'

  )+

  

  {

   $channel = HIDDEN;

  }

  ;


