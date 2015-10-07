/*
    Dumitrescu Evelina 341C3
    Tema 2 CPL
    
 */
import java.io.*;
import ro.pub.cs.lcpl.*;
import java.util.*;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.constructor.Constructor;

public class LCPLSemant {

	/*
	 * Clasa pentru un obiect LocalDefinition
	 */
	public static class LCPLLocalDefinition {
		LinkedList<Expression> llexpr = new LinkedList<Expression>();
		LocalDefinition localDef;

		public void insertExpression(Expression expr) {
			llexpr.add(expr);
		}

		public void setLocalDefinition(LocalDefinition localDef) {
			this.localDef = localDef;
		}

		public LocalDefinition getLocalDefinition() {
			return this.localDef;
		}

		public LinkedList<Expression> getExpressions() {
			return this.llexpr;
		}
	}
	/*
	 * Clasa pentru un obiect Method contine obiectele LocalDefinition declarate
	 * in interriorul metodei
	 */
	public static class LCPLMethod {
		private Method method;
		private LinkedHashMap knownLocalDefs = new LinkedHashMap();

		public void insertLocalDef(LCPLLocalDefinition localDef) {
			knownLocalDefs.put(localDef.getLocalDefinition().getName(),
					localDef);
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public Method getMethod() {
			return this.method;
		}

		public String getMethodName() {
			return this.method.getName();
		}

		public LinkedHashMap getLocalDefs() {
			return this.knownLocalDefs;
		}
	}
	/*
	 * clasa pentru un obiect LCPLClass contine metodele, atributele,
	 * declaratiile locale de variabile din interiorul if, while (si cele de
	 * acest gen din interiorul metodelor)
	 */
	public static class LCPLProgramClass {

		LinkedHashMap knownAttributes = new LinkedHashMap();

		LinkedHashMap knownMethods = new LinkedHashMap();
		LCPLClass lcplClass;
		Stack tempLocalDefs = new Stack();

		public Stack getTempLocalDefs() {
			return tempLocalDefs;
		}

		public void insertTempLocalDef(LCPLLocalDefinition localDef) {
			LinkedList llScope = (LinkedList) tempLocalDefs.peek();
			llScope.add(localDef);
		}

		public void insertNewScope() {
			LinkedList newScope = new LinkedList();
			tempLocalDefs.push(newScope);
		}

		public void removeScope() {
			tempLocalDefs.pop();
		}

		public void setLCPLClass(LCPLClass lcplClass) {
			this.lcplClass = lcplClass;
		}

		public LCPLClass getLCPLClass() {
			return this.lcplClass;
		}

		public LinkedHashMap getKnownAttributes() {
			return this.knownAttributes;
		}

		public boolean existsMethod(LCPLMethod method) {
			return (this.knownMethods.get(method.getMethod().getName()) != null);
		}

		public boolean existsAttribute(Attribute attr) {
			return (this.knownAttributes.get(attr.getName()) != null);
		}

		public Attribute getAttribute(String attrName) {
			return (Attribute) this.knownAttributes.get(attrName);
		}

		public void insertMethod(LCPLMethod method) {
			knownMethods.put(method.getMethodName(), method);
		}

		public LinkedHashMap getKnownMethods() {
			return this.knownMethods;
		}

		public void insertAttribute(Attribute attr) {
			knownAttributes.put(attr.getName(), attr);
		}

	}
	/*
	 * lista cu toate tipurile de date tipurile well known: Int, String, NoType,
	 * None dar si declaratiile de clase
	 */
	static LinkedHashMap knownTypes = new LinkedHashMap();
	/*
	 * lista cu toatel clasele din program
	 */
	static LinkedHashMap programClasses = new LinkedHashMap();
	static Attribute currentAttr;

	/*
	 * expresie de tip Addition
	 */
	static void doAddition(LCPLMethod method, LCPLProgramClass lcplClass,
			Addition addition) throws LCPLException {
		/*
		 * determin tipurile celor doi termeni
		 */
		Expression e1 = addition.getE1();
		Expression e2 = addition.getE2();
		doType(method, lcplClass, e1);
		doType(method, lcplClass, e2);
		/*
		 * cei doi termeni pot fi doar de tip Int sau String
		 */
		if ((e2.getType().equals("String"))
				&& ((!(e1 instanceof VoidConstant)) && (!(e1.getType()
						.equals("String"))
						&& (!(e1 instanceof StaticDispatch))
						&& (!(e1 instanceof Dispatch)) && (!(e1.getType()
						.equals("Int")))))) {
			throw new LCPLException("Cannot convert a value of type "
					+ e1.getType() + " into " + e2.getType(), addition);
		}

		if ((e1.getType().equals("String"))
				&& ((!(e2 instanceof VoidConstant)) && (!(e2.getType()
						.equals("String"))
						&& (!(e2 instanceof StaticDispatch))
						&& (!(e2 instanceof Dispatch)) && (!(e2.getType()
						.equals("Int")))))) {
			throw new LCPLException("Cannot convert a value of type "
					+ e2.getType() + " into " + e1.getType(), addition);
		}
		if ((e2.getType().equals("Int"))
				&& ((!(e1 instanceof VoidConstant)) && (!(e1.getType()
						.equals("String"))
						&& (!(e1 instanceof StaticDispatch))
						&& (!(e1 instanceof Dispatch)) && (!(e1.getType()
						.equals("Int")))))) {
			throw new LCPLException(
					"Cannot convert '+' expression to Int or String", addition);
		}

		if ((e1.getType().equals("Int"))
				&& ((!(e2 instanceof VoidConstant)) && (!(e2.getType()
						.equals("String"))
						&& (!(e2 instanceof StaticDispatch))
						&& (!(e2 instanceof Dispatch)) && (!(e2.getType()
						.equals("Int")))))) {
			throw new LCPLException(
					"Cannot convert '+' expression to Int or String", addition);
		}
		/*
		 * daca cei doi termeni au tipul Int atunci conversia se face catre Int
		 */
		if (e1.getType().equals(e2.getType()) && e1.getType().equals("Int")) {
			addition.setType("Int");
			addition.setTypeData((Type) knownTypes.get("Int"));
		} else {
			if (e1.getType().equals("String") == false) {
				Cast newCast = new Cast(e1.getLineNumber(), "String", e1);
				newCast.setTypeData((Type) knownTypes.get("String"));
				addition.setE1(newCast);
			} else if (e2.getType().equals("String") == false) {
				Cast newCast = new Cast(e2.getLineNumber(), "String", e2);
				newCast.setTypeData((Type) knownTypes.get("String"));
				addition.setE2(newCast);
			}

			/*
			 * daca cei doi termeni au tipul String sau String si Int atunci
			 * conversia se face catre String
			 */
			addition.setType("String");
			addition.setTypeData((Type) knownTypes.get("String"));
		}

	}

	/*
	 * expresie de tip Subtraction
	 */
	static void doSubtraction(LCPLMethod method, LCPLProgramClass lcplClass,
			Subtraction subtraction) {
		Expression e1 = subtraction.getE1();
		Expression e2 = subtraction.getE2();
		doType(method, lcplClass, e1);
		doType(method, lcplClass, e2);
		/*
		 * tipul expresiei este Int
		 */
		subtraction.setType("Int");
		subtraction.setTypeData((Type) knownTypes.get("Int"));

	}
	/*
	 * expresie de tip Multiplication
	 */
	static void doMultiplication(LCPLMethod method, LCPLProgramClass lcplClass,
			Multiplication multiplication) throws LCPLException {
		Expression e1 = multiplication.getE1();
		Expression e2 = multiplication.getE2();
		doType(method, lcplClass, e1);
		doType(method, lcplClass, e2);
		if (e1.getType().equals("Int") == false) {
			throw new LCPLException("Cannot convert a value of type "
					+ e1.getType() + " into Int", multiplication);
		}

		if (e2.getType().equals("Int") == false) {
			throw new LCPLException("Cannot convert a value of type "
					+ e2.getType() + " into Int", multiplication);
		}
		/*
		 * tipul expresiei este Int
		 */
		multiplication.setType("Int");
		multiplication.setTypeData((Type) knownTypes.get("Int"));

	}

	/*
	 * expresie de tip Division
	 */
	static void doDivision(LCPLMethod method, LCPLProgramClass lcplClass,
			Division division) {
		Expression e1 = division.getE1();
		Expression e2 = division.getE2();
		doType(method, lcplClass, e1);
		doType(method, lcplClass, e2);
		/*
		 * tipul expresiei este Int
		 */
		division.setType("Int");
		division.setTypeData((Type) knownTypes.get("Int"));

	}

	/*
	 * expresie de tip UnaryMinus
	 */
	static void doUnaryMinus(LCPLMethod method, LCPLProgramClass lcplClass,
			UnaryMinus unaryMinus) {
		Expression e1 = unaryMinus.getE1();
		doType(method, lcplClass, e1);
		/*
		 * tipul expresiei este Int
		 */
		unaryMinus.setType("Int");
		unaryMinus.setTypeData((Type) knownTypes.get("Int"));

	}

	/*
	 * expresie de tip LessThan
	 */
	static void doLessThan(LCPLMethod method, LCPLProgramClass lcplClass,
			LessThan lessThan) throws LCPLException {
		Expression e1 = lessThan.getE1();
		Expression e2 = lessThan.getE2();
		doType(method, lcplClass, e1);
		doType(method, lcplClass, e2);
		if (e1.getType().equals("Int") == false) {
			throw new LCPLException("Cannot convert a value of type "
					+ e1.getType() + " into Int", lessThan);
		}

		if (e2.getType().equals("Int") == false) {
			throw new LCPLException("Cannot convert a value of type "
					+ e2.getType() + " into Int", lessThan);
		}
		lessThan.setType("Int");
		lessThan.setTypeData((Type) knownTypes.get("Int"));

	}
	/*
	 * expresie de tip LessThanEqual
	 */
	static void doLessThanEqual(LCPLMethod method, LCPLProgramClass lcplClass,
			LessThanEqual lessThanEqual) {
		Expression e1 = lessThanEqual.getE1();
		Expression e2 = lessThanEqual.getE2();
		doType(method, lcplClass, e1);
		doType(method, lcplClass, e2);
		/*
		 * tipul expresiei este Int
		 */
		lessThanEqual.setType("Int");
		lessThanEqual.setTypeData((Type) knownTypes.get("Int"));

	}
	/*
	 * expresie de tip LessThanEqual
	 */
	static void doEqualComparison(LCPLMethod method,
			LCPLProgramClass lcplClass, EqualComparison equalComparison)
			throws LCPLException {
		Expression e1 = equalComparison.getE1();
		Expression e2 = equalComparison.getE2();
		doType(method, lcplClass, e1);
		doType(method, lcplClass, e2);
		if ((e2.getType().equals("Int"))
				&& ((!(e1 instanceof VoidConstant)) && (!(e1.getType()
						.equals("String")) && (!(e1.getType().equals("Int")))))) {
			throw new LCPLException(
					"Invalid type of parameters for == expression",
					equalComparison);
		}

		if ((e1.getType().equals("Int"))
				&& ((!(e2 instanceof VoidConstant)) && (!(e2.getType()
						.equals("String")) && (!(e2.getType().equals("Int")))))) {
			throw new LCPLException(
					"Invalid type of parameters for == expression",
					equalComparison);
		}
		if (e2 instanceof VoidConstant && (!(e1 instanceof VoidConstant))) {
			Cast newCast = new Cast(e1.getLineNumber(), "Object", e1);
			newCast.setTypeData((Type) knownTypes.get("Object"));
			equalComparison.setE1(newCast);

		}

		if (e1 instanceof VoidConstant && (!(e2 instanceof VoidConstant))) {
			Cast newCast = new Cast(e2.getLineNumber(), "Object", e2);
			newCast.setTypeData((Type) knownTypes.get("Object"));
			equalComparison.setE2(newCast);

		}
		if (e2.getType().equals("Int") && e1.getType().equals("String")) {
			Cast newCast = new Cast(e2.getLineNumber(), "String", e2);
			newCast.setTypeData((Type) knownTypes.get("String"));
			equalComparison.setE2(newCast);
		}
		if (e2.getType().equals("String") && e1.getType().equals("Int")) {
			Cast newCast = new Cast(e1.getLineNumber(), "String", e1);
			newCast.setTypeData((Type) knownTypes.get("String"));
			equalComparison.setE1(newCast);
		}
		/*
		 * tipul expresiei este Int
		 */
		equalComparison.setType("Int");
		equalComparison.setTypeData((Type) knownTypes.get("Int"));

	}
	/*
	 * expresie de tip LogicalNegation
	 */

	static void doLogicalNegation(LCPLMethod method,
			LCPLProgramClass lcplClass, LogicalNegation logicalNegation) {
		Expression e1 = logicalNegation.getE1();
		doType(method, lcplClass, e1);
		/*
		 * tipul expresiei este Int
		 */
		logicalNegation.setType("Int");
		logicalNegation.setTypeData((Type) knownTypes.get("Int"));

	}
	/*
	 * expresie de tip Substring
	 */

	static void doSubString(LCPLMethod method, LCPLProgramClass lcplClass,
			Expression e) {
		Expression stringExpr = ((SubString) e).getStringExpr();
		Expression startPosition = ((SubString) e).getStartPosition();
		Expression endPosition = ((SubString) e).getEndPosition();
		doType(method, lcplClass, stringExpr);
		doType(method, lcplClass, startPosition);
		doType(method, lcplClass, endPosition);
		/*
		 * tipul expresiei este String
		 */
		e.setType("String");
		e.setTypeData((Type) knownTypes.get("String"));
	}

	/*
	 * stabilirea tipului unei expresii
	 */
	static void doType(LCPLMethod method, LCPLProgramClass lcplClass,
			Expression e) {
		try {
			if (e instanceof Symbol) {
				if (((Symbol) e).getName().equals("self"))
					doSelf(method, lcplClass, e);
				else
					doSymbol(method, lcplClass, (Symbol) e);
			} else if (e instanceof StringConstant)
				doStringConstant((StringConstant) e);
			else if (e instanceof IntConstant)
				doIntConstant((IntConstant) e);
			else if (e instanceof VoidConstant)
				doVoidConstant((VoidConstant) e);
			else if (e instanceof NewObject)
				doNewObject((NewObject) e);
			else if (e instanceof Addition)
				doAddition(method, lcplClass, (Addition) e);
			else if (e instanceof Division)
				doDivision(method, lcplClass, (Division) e);
			else if (e instanceof Multiplication)
				doMultiplication(method, lcplClass, (Multiplication) e);
			else if (e instanceof Dispatch)
				fillDispatch(method, lcplClass, (Dispatch) e);
			else if (e instanceof EqualComparison)
				doEqualComparison(method, lcplClass, (EqualComparison) e);
			else if (e instanceof LessThan)
				doLessThan(method, lcplClass, (LessThan) e);
			else if (e instanceof IfStatement)
				doIfStatement(method, lcplClass, (IfStatement) e);
			else if (e instanceof Assignment)
				doAssignment(method, lcplClass, (Assignment) e);
			else if (e instanceof Subtraction)
				doSubtraction(method, lcplClass, (Subtraction) e);
			else if (e instanceof LocalDefinition)
				doLocalDefinition(method, lcplClass, (LocalDefinition) e, false);

			else if (e instanceof SubString)
				doSubString(method, lcplClass, (SubString) e);

			else if (e instanceof WhileStatement)
				doWhileStatement(method, lcplClass, (WhileStatement) e);
			else if (e instanceof UnaryMinus)
				doUnaryMinus(method, lcplClass, (UnaryMinus) e);

			else if (e instanceof LessThanEqual)
				doLessThanEqual(method, lcplClass, (LessThanEqual) e);

			else if (e instanceof LogicalNegation)
				doLogicalNegation(method, lcplClass, (LogicalNegation) e);
			else

			if (e instanceof StaticDispatch)
				fillStaticDispatch(method, lcplClass, (StaticDispatch) e);
			else if (e instanceof Cast)
				doCast(method, lcplClass, (Cast) e);
			if (method != null) {
				if (!(e instanceof LocalDefinition)) {

					method.getMethod().getBody().setType(e.getType());
					method.getMethod().getBody().setTypeData(e.getTypeData());
				}

			}
		} catch (Exception exc) {

			if (exc instanceof LCPLException) {
				System.err.println("Error in line "
						+ ((LCPLException) exc).node.getLineNumber() + " : "
						+ ((LCPLException) exc).message);
			} else {
				exc.printStackTrace();
			}
			System.exit(1);

		}

	}

	/*
	 * expresie de tip Self
	 */
	static void doSelf(LCPLMethod method, LCPLProgramClass lcplClass,
			Expression expr) {

		((Symbol) expr).setVariable(method.getMethod().getSelf());
		((Symbol) expr).setTypeData((Type) knownTypes.get(lcplClass
				.getLCPLClass().getName()));
		Cast newCast = new Cast(expr.getLineNumber(), "Object", expr);
		newCast.setTypeData((Type) knownTypes.get("Object"));

		expr = null;
	}
	/*
	 * expresie de tip Symbol
	 */
	static void doSymbol(LCPLMethod method, LCPLProgramClass lcplClass,
			Symbol sb) throws LCPLException {
		Set set;
		LinkedHashMap knownVars = lcplClass.getKnownAttributes();
		LinkedHashMap knownLocalDefs = null;
		boolean isFP = false;
		boolean isTLD = false;
		Stack tempLocalDefs = lcplClass.getTempLocalDefs();
		/*
		 * indentific variabila la care se refera simbolul poate fi variabila
		 * locala temporara, parametru formal sau atribut
		 */
		if (method == null) {
			for (int i = 0; i < tempLocalDefs.size(); i++) {
				LinkedList currentScope = (LinkedList) tempLocalDefs
						.elementAt(i);
				for (Object lcplTempLd : currentScope) {
					LocalDefinition tempLd = ((LCPLLocalDefinition) lcplTempLd)
							.getLocalDefinition();

					if ((tempLd.getName().equals(sb.getName()))) {

						sb.setVariable(tempLd);
						sb.setType(tempLd.getType());
						sb.setTypeData((Type) knownTypes.get(tempLd.getType()));
						isTLD = true;
						break;
					}
				}
			}

			if (!isTLD)
				if (knownVars.containsKey(sb.getName())) {
					Attribute enattr = (Attribute) knownVars.get(sb.getName());
					sb.setVariable(enattr);
					sb.setType(enattr.getType());

					sb.setTypeData((Type) knownTypes.get(enattr.getType()));
				}
		} else {

			knownLocalDefs = method.getLocalDefs();

			ArrayList<FormalParam> fpList = (ArrayList<FormalParam>) method
					.getMethod().getParameters();

			for (int i = 0; i < tempLocalDefs.size(); i++) {
				LinkedList currentScope = (LinkedList) tempLocalDefs
						.elementAt(i);
				for (Object lcplTempLd : currentScope) {
					LocalDefinition tempLd = ((LCPLLocalDefinition) lcplTempLd)
							.getLocalDefinition();
					if ((tempLd.getName().equals(sb.getName()))
							&& (tempLd.getLineNumber() <= sb.getLineNumber())) {
						sb.setVariable(tempLd);
						sb.setType(tempLd.getType());
						sb.setTypeData((Type) knownTypes.get(tempLd.getType()));
						isTLD = true;
						break;
					}
				}
			}
			if (!isTLD) {
				if (knownLocalDefs.containsKey(sb.getName())) {
					LocalDefinition enld = (LocalDefinition) ((LCPLLocalDefinition) knownLocalDefs
							.get(sb.getName())).getLocalDefinition();
					if (enld.getLineNumber() <= sb.getLineNumber()) {
						sb.setVariable(enld);
						sb.setType(enld.getType());
						sb.setTypeData((Type) knownTypes.get(enld.getType()));
					}
				} else {
					for (FormalParam fp : fpList) {
						if (fp.getName().equals(sb.getName())) {

							isFP = true;
							sb.setVariable(fp);
							sb.setType(fp.getType());
							sb.setTypeData((Type) knownTypes.get(fp.getType()));
						}
					}
					if (knownVars.containsKey(sb.getName()) && isFP == false) {
						Attribute enattr = (Attribute) knownVars.get(sb
								.getName());
						sb.setVariable(enattr);
						sb.setType(enattr.getType());

						sb.setTypeData((Type) knownTypes.get(enattr.getType()));
					}
				}
			}
		}
		if (sb.getTypeData() == null) {

			throw new LCPLException("Attribute " + sb.getName()
					+ " not found in class "
					+ lcplClass.getLCPLClass().getName(), sb);
		}

	}
	/*
	 * expresie de tip While
	 */
	static void doWhileStatement(LCPLMethod method, LCPLProgramClass lcplClass,
			WhileStatement whileStatement) throws LCPLException {
		Expression condition = whileStatement.getCondition();
		Expression loopBody = whileStatement.getLoopBody();
		doType(method, lcplClass, condition);
		if (condition.getType().equals("Int") == false)
			throw new LCPLException("If condition must be Int", whileStatement);
		for (Expression e : ((Block) loopBody).getExpressions()) {
			if (e instanceof LocalDefinition) {
				LCPLLocalDefinition lcplLocalDef = new LCPLLocalDefinition();
				lcplLocalDef.setLocalDefinition((LocalDefinition) e);
				lcplClass.insertNewScope();
				lcplClass.insertTempLocalDef(lcplLocalDef);
				doLocalDefinition(method, lcplClass, (LocalDefinition) e, true);
				lcplClass.removeScope();
			} else {
				doType(method, lcplClass, e);
				((Block) loopBody).setType(e.getType());
				((Block) loopBody).setTypeData((Type) knownTypes.get(e
						.getType()));
			}
		}
		whileStatement.setType("void");
		whileStatement.setTypeData((Type) knownTypes.get("void"));
	}
	/*
	 * expresied e tip StringConstant
	 */
	static void doStringConstant(StringConstant sc) {
		sc.setTypeData((Type) knownTypes.get("String"));
		sc.setType("String");
	}
	/*
	 * Expresie de tip Void Constant
	 */
	static void doVoidConstant(VoidConstant vc) {
		vc.setTypeData((Type) knownTypes.get("null"));
		vc.setType("void");
	}

	/*
	 * Expresie de tip Int Constant
	 */
	static void doIntConstant(IntConstant ic) {

		ic.setTypeData((Type) knownTypes.get("Int"));
		ic.setType("Int");
	}

	/*
	 * Expresie de tip NewObject
	 */
	static void doNewObject(NewObject no) throws LCPLException {
		// System.out.println("type"+no.getType()+"type data");
		String typeNo = no.getType();
		if (typeNo.equals("Int") || typeNo.equals("String")
				|| typeNo.equals("Object"))
			throw new LCPLException("Illegal instruction : new " + typeNo, no);
		else if (knownTypes.get(typeNo) == null)
			throw new LCPLException("Class " + typeNo + " not found.", no);
		no.setTypeData((Type) knownTypes.get(no.getType()));
	}

	/*
	 * expresied e tip IfStatement
	 */
	static void doIfStatement(LCPLMethod method, LCPLProgramClass lcplClass,
			IfStatement ifstatement) throws LCPLException {
		Expression condition, ifExpr, thenExpr;
		condition = ifstatement.getCondition();
		ifExpr = ifstatement.getIfExpr();
		thenExpr = ifstatement.getThenExpr();

		doType(method, lcplClass, condition);
		/*
		 * stabilire tip expresie if
		 */
		if (((Block) ifExpr).getExpressions().size() == 0) {
			ifExpr.setTypeData((Type) knownTypes.get("(none)"));
			ifExpr.setType("(none)");

		}
		for (Expression e : ((Block) ifExpr).getExpressions()) {
			if (e instanceof LocalDefinition) {
				LCPLLocalDefinition lcplLocalDef = new LCPLLocalDefinition();
				lcplLocalDef.setLocalDefinition((LocalDefinition) e);

				lcplClass.insertNewScope();
				lcplClass.insertTempLocalDef(lcplLocalDef);

				doLocalDefinition(method, lcplClass, (LocalDefinition) e, true);
				lcplClass.removeScope();
				String ifExprType = (((LocalDefinition) e).getScope())
						.getType();
				Type ifExprTypeData = (Type) (((LocalDefinition) e).getScope())
						.getTypeData();
				ifExpr.setTypeData(ifExprTypeData);

				ifExpr.setType(ifExprType);
			} else {
				doType(method, lcplClass, e);
				String ifExprType = e.getType();
				Type ifExprTypeData = (Type) e.getTypeData();
				ifExpr.setTypeData(ifExprTypeData);

				ifExpr.setType(ifExprType);
			}

		}
		if (thenExpr != null)
			for (Expression e : ((Block) thenExpr).getExpressions()) {
				doType(method, lcplClass, e);
				((Block) thenExpr).setType(e.getType());
				((Block) thenExpr).setTypeData(e.getTypeData());

			}
		if (thenExpr == null) {
			ifstatement.setType("void");
			ifstatement.setTypeData((Type) knownTypes.get("void"));
		} else {

			if (thenExpr.getType().equals("void")) {
				ifstatement.setType(((Block) ifExpr).getType());
				ifstatement.setTypeData(((Block) ifExpr).getTypeData());
			} else {
				if (thenExpr.getType().equals(ifExpr.getType()) == false
						&& (ifExpr.getType().equals("void") == false)) {
					ifstatement.setType("(none)");
					ifstatement.setTypeData((Type) knownTypes.get("(none)"));
				} else {
					ifstatement.setType(((Block) thenExpr).getType());
					ifstatement.setTypeData(((Block) thenExpr).getTypeData());
				}
			}
		}

	}
	/*
	 * expresie de tip static dispatch
	 */
	static void fillStaticDispatch(LCPLMethod method,
			LCPLProgramClass lcplClass, StaticDispatch dispatch)
			throws LCPLException {

		Expression dispObj = dispatch.getObject();
		doType(method, lcplClass, dispObj);
		/*
		 * verific selfType, tipul obiectului si al metodei dispatchului
		 */
		String dispType = dispatch.getType();
		LCPLClass selfType = (LCPLClass) knownTypes.get(dispType);
		String objType = dispObj.getType();
		if (selfType == null)
			throw new LCPLException("Class " + dispType + " not found.",
					dispatch);
		else if ((((LCPLClass) knownTypes.get(objType)).getParent().equals(
				dispType) == false)
				&& (objType.equals(dispType) == false))
			throw new LCPLException("Cannot convert from " + objType + " to "
					+ dispType + " in StaticDispatch", dispatch);
		LCPLProgramClass lcplCls = (LCPLProgramClass) programClasses
				.get(dispType);
		LCPLMethod lcplMethod = (LCPLMethod) lcplCls.getKnownMethods().get(
				dispatch.getName());
		dispatch.setMethod((Method) lcplMethod.getMethod());
		String dispatchType = dispatch.getMethod().getReturnType();
		if (dispatchType == "void")
			dispatchType = "(none)";

		dispatch.setSelfType((Type) selfType);
		dispatch.setTypeData((Type) knownTypes.get(dispatch.getMethod()
				.getReturnType()));
		List<Expression> arg = dispatch.getArguments();
		List<FormalParam> argMethod = lcplMethod.getMethod().getParameters();
		for (int i = 0; i < arg.size(); i++) {
			Expression expr = arg.get(i);
			FormalParam fp = argMethod.get(i);
			doType(method, lcplClass, expr);
			String exprType = expr.getType();
			String fpType = fp.getType();

			if (exprType.equals(fpType) == false) {
				Cast newCast = new Cast(expr.getLineNumber(), fpType, expr);
				newCast.setTypeData((Type) knownTypes.get(fpType));
				arg.set(i, newCast);
			}

		}

	}

	/*
	 * exprsie de tip Dispatch
	 */
	static void fillDispatch(LCPLMethod method, LCPLProgramClass lcplClass,
			Dispatch dispatch) throws LCPLException {

		Expression dispObj = dispatch.getObject();

		if (dispObj == null) {
			Symbol obj = new Symbol();
			obj.setName("self");
			if (method != null) {
				obj.setVariable(method.getMethod().getSelf());
				obj.setLineNumber(method.getMethod().getLineNumber());
			} else {
				obj.setVariable(currentAttr.getAttrInitSelf());
				obj.setLineNumber(currentAttr.getLineNumber());
			}
			obj.setTypeData((Type) knownTypes.get(lcplClass.getLCPLClass()
					.getName()));
			dispatch.setObject(obj);

		} else
			doType(method, lcplClass, dispatch.getObject());

		String type = ((Expression) dispatch.getObject()).getType();

		LCPLProgramClass lcplCls = (LCPLProgramClass) programClasses.get(type);
		LCPLMethod lcplMethod = (LCPLMethod) lcplCls.getKnownMethods().get(
				dispatch.getName());
		if (lcplMethod == null) {
			throw new LCPLException(
					"Method " + dispatch.getName() + " not found in class "
							+ lcplCls.getLCPLClass().getName(), dispatch);
		}
		dispatch.setMethod((Method) lcplMethod.getMethod());
		String dispatchType = dispatch.getMethod().getReturnType();
		if (dispatchType == "void")
			dispatchType = "(none)";
		dispatch.setType(dispatchType);
		dispatch.setTypeData((Type) knownTypes.get(dispatch.getMethod()
				.getReturnType()));
		List<Expression> arg = dispatch.getArguments();
		List<FormalParam> argMethod = lcplMethod.getMethod().getParameters();
		if (argMethod.size() > arg.size()) {

			throw new LCPLException("Not enough arguments in method call "
					+ dispatch.getMethod().getName(), method.getMethod()
					.getBody());
		}
		if (argMethod.size() < arg.size()) {
			throw new LCPLException("Too many arguments in method call "
					+ dispatch.getMethod().getName(), method.getMethod()
					.getBody());
		}
		for (int i = 0; i < arg.size(); i++) {
			Expression expr = arg.get(i);
			FormalParam fp = argMethod.get(i);
			doType(method, lcplClass, expr);
			String exprType = expr.getType();
			String fpType = fp.getType();
			Type exprTypeData = (Type) knownTypes.get(exprType);
			Type fpTypeData = (Type) knownTypes.get(fpType);
			if (exprType.equals("(none)")) {
				throw new LCPLException("Cannot convert a value of type "
						+ exprType + " into " + fpType, dispatch);

			}
			if ((exprType.equals("String") == false)
					&& (exprType.equals("Int") == false)
					&& (fpType.equals("String") == false)
					&& (fpType.equals("Int") == false)
					&& (fpType.equals(exprType) == false)) {

				if (exprTypeData instanceof LCPLClass)
					if (((LCPLClass) exprTypeData).getParent().equals(fpType) == false) {

						throw new LCPLException(
								"Cannot convert a value of type " + exprType
										+ " into " + fpType, dispatch);
					}

			}
			if (exprType.equals("String") && fpType.equals("Int")) {
				throw new LCPLException("Cannot convert a value of type "
						+ exprType + " into " + fpType, dispatch);
			}
			if ((exprType.equals("Int") || exprType.equals("String"))
					&& (fpTypeData instanceof LCPLClass)
					&& (fpType.equals("String") == false)
					&& (fpType.equals("Object") == false)) {
				throw new LCPLException("Cannot convert a value of type "
						+ exprType + " into " + fpType, dispatch);
			}
			if (exprType.equals(fpType) == false
					&& (exprType.equals("void") == false)) {
				Cast newCast = new Cast(expr.getLineNumber(), fpType, expr);
				newCast.setTypeData((Type) knownTypes.get(fpType));
				arg.set(i, newCast);
			}

		}

	}
	/*
	 * expresie de tip Assignment
	 */
	static void doAssignment(LCPLMethod method, LCPLProgramClass lcplClass,
			Assignment assignment) throws LCPLException {

		ArrayList<FormalParam> fpList = null;
		if (method != null)
			fpList = (ArrayList<FormalParam>) method.getMethod()
					.getParameters();
		LinkedHashMap attrList = (LinkedHashMap) lcplClass.getKnownAttributes();
		LinkedHashMap ldList = null;
		if (method != null)
			ldList = (LinkedHashMap) method.getLocalDefs();
		String sbName = assignment.getSymbol();
		String actualName = null;
		boolean isFP = false;
		boolean isLD = false;
		boolean isTLD = false;
		boolean startsWithSelf = sbName.startsWith("self.");
		if (ldList != null)
			isLD = ldList.containsKey(sbName);
		/*
		 * identific variabila la care se refera simbolul assignmentului
		 */
		if (startsWithSelf) {

			actualName = sbName.replaceAll("self.", "");
			if (attrList.containsKey(actualName)) {

				assignment.setSymbolData((Variable) attrList.get(actualName));

				assignment.setSymbol(actualName);
			}
		} else {
			if (method == null) {
				Stack tempLocalDefs = lcplClass.getTempLocalDefs();
				for (int i = 0; i < tempLocalDefs.size(); i++) {
					LinkedList currentScope = (LinkedList) tempLocalDefs
							.elementAt(i);
					for (Object lcplTempLd : currentScope) {
						LocalDefinition tempLd = ((LCPLLocalDefinition) lcplTempLd)
								.getLocalDefinition();

						if ((tempLd.getName().equals(sbName))) {

							assignment.setSymbolData((Variable) tempLd);
							break;
						}
					}
				}
			} else {
				Stack tempLocalDefs = lcplClass.getTempLocalDefs();
				for (int i = 0; i < tempLocalDefs.size(); i++) {
					LinkedList currentScope = (LinkedList) tempLocalDefs
							.elementAt(i);
					for (Object lcplTempLd : currentScope) {
						LocalDefinition tempLd = ((LCPLLocalDefinition) lcplTempLd)
								.getLocalDefinition();

						if ((tempLd.getName().equals(sbName))) {

							assignment.setSymbolData((Variable) tempLd);
							isTLD = true;
							break;
						}
					}
				}
				if (!isTLD) {
					if (isLD)
						assignment
								.setSymbolData((Variable) ((LCPLLocalDefinition) ldList
										.get(sbName)).getLocalDefinition());
					else {
						if (fpList != null) {
							for (FormalParam fp : fpList)
								if (fp.getName().equals(sbName)) {
									isFP = true;

									assignment.setSymbolData(fp);
								}
						}
						if (!isFP) {

							if (attrList.containsKey(sbName)) {
								assignment.setSymbolData((Variable) attrList
										.get(sbName));
								assignment.setSymbol(sbName);
							}
						}
					}
				}
			}
		}
		/*
		 * evaluez expresia assignmentului
		 */
		Expression e1 = assignment.getE1();

		Variable asgnSbData = assignment.getSymbolData();
		String asgnType = null;
		if (asgnSbData instanceof LocalDefinition)
			asgnType = ((LocalDefinition) asgnSbData).getType();
		else if (asgnSbData instanceof Attribute)
			asgnType = ((Attribute) asgnSbData).getType();
		else if (asgnSbData instanceof FormalParam)
			asgnType = ((FormalParam) asgnSbData).getType();

		if (asgnSbData == null) {

			throw new LCPLException("Attribute "
					+ sbName.replaceAll("self.", "") + " not found in class "
					+ lcplClass.getLCPLClass().getName(), assignment);
		}
		if (e1 instanceof WhileStatement && (asgnType.equals("none") == false))
			throw new LCPLException(
					"Cannot convert a value of type (none) into " + asgnType,
					assignment);
		doType(method, lcplClass, e1);
		if (asgnType.equals(e1.getType()) == false) {

			Type classAsgn = (Type) knownTypes.get(asgnType);
			Type classE1 = (Type) knownTypes.get(e1.getType());
			if (classE1 instanceof LCPLClass && classAsgn instanceof LCPLClass) {
				if (((LCPLClass) classAsgn).getName().equals(
						((LCPLClass) classE1).getParent()) == false)
					throw new LCPLException("Cannot convert a value of type "
							+ e1.getType() + " into " + asgnType, assignment);
			} else {
				throw new LCPLException("Cannot convert a value of type "
						+ e1.getType() + " into " + asgnType, assignment);
			}
		}
		assignment.setType(e1.getType());
		assignment.setTypeData(e1.getTypeData());

	}
	/*
	 * expresied e tip cast
	 */
	static void doCast(LCPLMethod method, LCPLProgramClass lcplClass, Cast cast)
			throws LCPLException {
		Type castType = (Type) knownTypes.get(cast.getType());
		if (castType == null)
			throw new LCPLException("Class " + cast.getType() + " not found.",
					cast);
		cast.setTypeData(castType);
		Expression e1 = cast.getE1();
		doType(method, lcplClass, e1);
	}

	/*
	 * evaluare expresie initializare local definition si stabilire tip Scope
	 */
	static void initLocalDef(LCPLMethod method, LCPLProgramClass lcplClass,
			LocalDefinition localDef) {
		String ldType = localDef.getType();

		Expression init = localDef.getInit();
		if (init != null) {
			doType(method, lcplClass, init);
			Expression ldScope = localDef.getScope();
			if (ldScope instanceof LocalDefinition) {
				ldScope.setTypeData((Type) knownTypes.get(ldScope.getType()));
			} else {
				ldScope.setTypeData((Type) knownTypes.get("null"));
			}
			if (ldType.equals(init.getType()) == false) {
				Cast initCast = new Cast(init.getLineNumber(), ldType, init);
				initCast.setTypeData((Type) knownTypes.get(ldType));
				localDef.setInit(initCast);
			}
		}
	}
	/*
	 * stabilire tip expression block pentru un LocalDefinition
	 */
	static void fillLocalDefinitionBlock(LCPLMethod method,
			LCPLProgramClass lcplClass, Block exprBlock, boolean temp) {
		if (exprBlock.getExpressions().isEmpty()) {
			exprBlock.setType("(none)");
			exprBlock.setTypeData((Type) knownTypes.get("(none)"));
			method.getMethod().getBody().setType("(none)");
			method.getMethod().getBody()
					.setTypeData((Type) knownTypes.get("(none)"));

		}

		else
			for (Expression expres : exprBlock.getExpressions()) {

				doType(method, lcplClass, expres);
				if (expres instanceof LocalDefinition) {

					exprBlock.setType(((LocalDefinition) expres).getScope()
							.getType());
					exprBlock.setTypeData((Type) ((LocalDefinition) expres)
							.getScope().getTypeData());
				} else {
					if (expres instanceof Dispatch
							|| expres instanceof StaticDispatch) {
						exprBlock.setType(((BaseDispatch) expres).getMethod()
								.getReturnType());
						exprBlock.setTypeData((Type) knownTypes
								.get(((BaseDispatch) expres).getMethod()
										.getReturnType()));

					}

					else {

						exprBlock.setType(expres.getType());
						exprBlock.setTypeData((Type) knownTypes.get(expres
								.getType()));

					}
				}
			}

	}
	/*
	 * expresied e tip LocalDefinition
	 */
	static void doLocalDefinition(LCPLMethod method,
			LCPLProgramClass lcplClass, LocalDefinition localDef, boolean temp)
			throws LCPLException {

		String ldType = localDef.getType();
		Type ldVarType = (Type) knownTypes.get(ldType);
		if (ldVarType == null)
			throw new LCPLException("Class " + ldType + " not found.", localDef);
		localDef.setVariableType(ldVarType);
		Expression ldScope = localDef.getScope();
		initLocalDef(method, lcplClass, localDef);
		LCPLLocalDefinition lcplLocalDef = new LCPLLocalDefinition();
		lcplLocalDef.setLocalDefinition(localDef);

		/*
		 * verific daca declararea se face in interiorul unei metode sau in
		 * interiorul unei clase, intr-un bloc if/while
		 */
		if (!temp)
			method.insertLocalDef(lcplLocalDef);
		else
			lcplClass.insertTempLocalDef(lcplLocalDef);
		if (ldScope instanceof LocalDefinition)
			doLocalDefinition(method, lcplClass, (LocalDefinition) ldScope,
					temp);
		else
			fillLocalDefinitionBlock(method, lcplClass, (Block) ldScope, temp);

		if (ldScope instanceof Block) {

			if (((Block) ldScope).getExpressions().isEmpty() == true) {

				localDef.setTypeData((Type) knownTypes.get("(none)"));
			}

			else {

				localDef.setTypeData(ldScope.getTypeData());
			}
		} else {

			localDef.setTypeData(ldScope.getTypeData());
		}
	}
	/*
	 * expresied e tip metoda
	 */
	static void fillMethod(LCPLMethod method, LCPLProgramClass lcplClass)
			throws LCPLException {
		Block exprblock;
		exprblock = (Block) method.getMethod().getBody();
		ArrayList<FormalParam> fpList = (ArrayList<FormalParam>) method
				.getMethod().getParameters();
		for (FormalParam fp : fpList) {
			String fpType = fp.getType();
			Type fpVarType = (Type) knownTypes.get(fpType);
			if (fpVarType == null)
				throw new LCPLException("Class " + fpType + " not found.", fp);
			fp.setVariableType(fpVarType);
		}
		Expression expres = null;
		if (exprblock.getExpressions() != null)
			for (int i = 0; i < exprblock.getExpressions().size(); i++) {
				expres = exprblock.getExpressions().get(i);
				doType(method, lcplClass, expres);

			}

		if (exprblock.getExpressions().size() != 0) {

			String exprType;
			String retType;
			Type exprTypeData;

			{
				exprType = exprblock.getType();
				exprTypeData = exprblock.getTypeData();
			}
			retType = method.getMethod().getReturnType();
			Type retTypeData = (Type) knownTypes.get(retType);
			if (exprTypeData instanceof LCPLClass
					&& retTypeData instanceof LCPLClass
					&& (retTypeData != exprTypeData)) {

				if ((((LCPLClass) exprTypeData).getParent().equals(
						((LCPLClass) retTypeData).getName()) == false)
						&& (((LCPLClass) retTypeData).getParent().equals(
								((LCPLClass) exprTypeData).getName()) == false)) {
					throw new LCPLException("Cannot convert a value of type "
							+ exprType + " into " + retType, method.getMethod()
							.getBody());
				}
			}
			if ((!(exprTypeData instanceof LCPLClass) || !(retTypeData instanceof LCPLClass))
					&& (retTypeData != exprTypeData)
					&& ((retType.equals("void") == false) && (exprType
							.equals("void") == false))) {
				throw new LCPLException("Cannot convert a value of type "
						+ exprType + " into " + retType, method.getMethod()
						.getBody());
			}
			if ((exprType.equals(retType) == false)
					&& ((retType.equals("void") == false) && (exprType
							.equals("void") == false)))

			{
				Cast newCast = new Cast(method.getMethod().getBody()
						.getLineNumber(), retType, method.getMethod().getBody());
				newCast.setTypeData((Type) knownTypes.get(retType));

				method.getMethod().setBody(newCast);
			}

		} else {
			method.getMethod().getBody().setType("(none)");
			method.getMethod().getBody()
					.setTypeData((Type) knownTypes.get("void"));
			String bodyType = method.getMethod().getBody().getType();
			String retType = method.getMethod().getReturnType();

			if (bodyType.equals(retType) == false
					&& (!(bodyType.equals("(none)") && retType.equals("void"))))
				throw new LCPLException("Cannot convert a value of type "
						+ method.getMethod().getBody().getType() + " into "
						+ method.getMethod().getReturnType(), method
						.getMethod().getSelf());
		}

	}
	/*
	 * preiau atributele si metodele din clasa parinte
	 */
	static void getMethodsAttributesFromParent(LCPLProgramClass parentClass,
			LCPLProgramClass childClass) {

		LinkedHashMap llParentMethods = (LinkedHashMap) parentClass
				.getKnownMethods();
		LinkedHashMap llChildMethods = (LinkedHashMap) childClass
				.getKnownMethods();
		LinkedHashMap llParentAttributes = (LinkedHashMap) parentClass
				.getKnownAttributes();
		LinkedHashMap llChildAttributes = (LinkedHashMap) childClass
				.getKnownAttributes();
		for (Object key : llParentMethods.keySet()) {

			LCPLMethod method = (LCPLMethod) llParentMethods.get(key);
			llChildMethods.put(method.getMethod().getName(), method);

		}

		for (Object key : llParentAttributes.keySet()) {

			Attribute attr = (Attribute) llParentAttributes.get(key);
			llChildAttributes.put(attr.getName(), attr);

		}
	}

	/*
	 * expresied e tip atribut
	 */
	static void doAttribute(LCPLProgramClass lcplClass, Attribute attr)
			throws LCPLException {
		Expression initExpr = attr.getInit();
		LCPLClass cls = lcplClass.getLCPLClass();
		String attrType = attr.getType();
		Type attrVarType = (Type) knownTypes.get(attrType);
		if (attrVarType == null)
			throw new LCPLException("Class " + attrType + " not found.", attr);
		attr.setTypeData(attrVarType);
		if (initExpr != null) {
			FormalParam fp2 = new FormalParam("self", attr.getType());
			fp2.setVariableType(cls);
			attr.setAttrInitSelf(fp2);
			doType(null, lcplClass, initExpr);

			if (initExpr.getType().equals(attr.getType()) == false) {
				Cast initCast = new Cast(initExpr.getLineNumber(),
						attr.getType(), initExpr);
				initCast.setTypeData((Type) knownTypes.get(attr.getType()));
				attr.setInit(initCast);
			}
		}

		lcplClass.insertAttribute(attr);
	}

	/*
	 * verific daca o clasa exista deja
	 */
	static boolean classExists(LCPLClass cls, List<LCPLClass> orderedClasses) {
		for (LCPLClass clsIter : orderedClasses) {
			if (clsIter.getName().equals(cls.getName()))
				return true;
		}
		return false;
	}

	/*
	 * verific mostenirea cliclica
	 */
	static boolean checkCyclicInheritance(LCPLClass cls, List<LCPLClass> classes) {
		try {
			LCPLClass current = null;
			String parrent = cls.getParent();
			LinkedList inheritancePath = new LinkedList();
			inheritancePath.add(cls);
			while (inheritancePath.indexOf(current) == -1) {
				if (current != null)
					inheritancePath.add(current);

				current = null;
				if (parrent == null)
					return false;
				for (LCPLClass clsIter : classes) {

					if (clsIter.getName().equals(parrent) == true) {//

						current = clsIter;
						break;
					}
				}
				if (current == null)
					return false;
				parrent = current.getParent();

			}
		} catch (Exception exc) {
			// exc.printStackTrace();
		}
		return true;

	}
	/*
	 * evaluez clasele incepand cu cele care mostenesc tipul Object sau un tip
	 * deja declarat
	 */
	static void doInheritance(List<LCPLClass> classes,
			List<LCPLClass> orderedClasses) throws LCPLException {
		Stack traversalStack = new Stack();
		for (LCPLClass cls : classes) {
			String parentClass = cls.getParent();

			if (parentClass != null) {
				if (checkCyclicInheritance(cls, classes))
					throw new LCPLException("Class " + cls.getName()
							+ " recursively inherits itself.", cls);
				if (parentClass.equals("Int"))
					throw new LCPLException("Class " + parentClass
							+ " not found.", cls);
				if (parentClass.equals("String"))
					throw new LCPLException("A class cannot inherit a "
							+ parentClass, cls);
				if (parentClass.equals(cls.getName()))
					throw new LCPLException("Class " + parentClass
							+ " recursively inherits itself.", cls);

			}
			if (parentClass == null || knownTypes.containsKey(parentClass)) {
				orderedClasses.add(cls);
				traversalStack.push(cls);
				knownTypes.put(cls.getName(), cls);

			}

		}

		while (!traversalStack.empty()) {
			LCPLClass parentClass = (LCPLClass) traversalStack.pop();

			for (LCPLClass cls : classes) {

				if (classExists(cls, orderedClasses) == false)
					if ((cls.getParent() != null))
						if (cls.getParent().equals(parentClass.getName()) == true) {
							orderedClasses.add(cls);
							traversalStack.push(cls);
							knownTypes.put(cls.getName(), cls);
						}

			}
		}
		for (LCPLClass cls : classes) {
			if (orderedClasses.indexOf(cls) == -1)
				throw new LCPLException("Class " + cls.getParent()
						+ " not found.", cls);
		}

	}
	/*
	 * parcurg arborele AST
	 */
	static void fillAST(Program p) throws LCPLException {
		LinkedList<LCPLClass> orderedClasses = new LinkedList<LCPLClass>();
		/*
		 * ordonez clasele inainte de a incepe prelucrarea lor
		 */
		doInheritance(p.getClasses(), orderedClasses);
		for (LCPLClass cls : orderedClasses) {
			String parent = cls.getParent();

			if (parent == null) {
				parent = "Object";
				cls.setParent(parent);
			}
			LCPLProgramClass lcplParentClass = (LCPLProgramClass) programClasses
					.get(parent);
			cls.setParentData((LCPLClass) knownTypes.get(parent));
			LCPLProgramClass lcplClass = new LCPLProgramClass();
			lcplClass.setLCPLClass(cls);
			if (programClasses.get(cls.getName()) != null)
				throw new LCPLException(
						"A class with the same name already exists : "
								+ cls.getName(), cls);
			programClasses.put(cls.getName(), lcplClass);
			getMethodsAttributesFromParent(
					(LCPLProgramClass) programClasses.get(cls.getParent()),
					lcplClass);

			/*
			 * pentru fiecare clasa identific toate atributele si metodele
			 * existente si abia apoi incep evaluarea expressilor in din
			 * interiorul lor
			 */
			for (Feature feature : cls.getFeatures()) {

				if (feature instanceof Attribute) {
					Attribute attr = (Attribute) feature;

					if (lcplParentClass.existsAttribute(attr))
						throw new LCPLException("Attribute " + attr.getName()
								+ " is redefined.",
								lcplParentClass.getLCPLClass());
					else if (lcplClass.existsAttribute(attr))
						throw new LCPLException(
								"An attribute with the same name already exists in class "
										+ cls.getName() + " : "
										+ attr.getName(), attr);
					else
						lcplClass.insertAttribute(attr);

				} else if (feature instanceof Method) {

					Method method = (Method) feature;
					method.setParent(cls);
					method.setReturnTypeData((Type) knownTypes.get(method
							.getReturnType()));
					FormalParam clsSelf2 = new FormalParam("self",
							cls.getName());
					clsSelf2.setVariableType(cls);
					method.setSelf(clsSelf2);
					LCPLMethod lcplMethod = new LCPLMethod();
					lcplMethod.setMethod(method);

					String methodName = method.getName();

					if (lcplParentClass.getKnownMethods().get(methodName) != null) {
						Method method2 = ((LCPLMethod) lcplParentClass
								.getKnownMethods().get(methodName)).getMethod();
						String retType1 = method.getReturnType();
						String retType2 = method2.getReturnType();
						ArrayList<FormalParam> llFP1 = (ArrayList<FormalParam>) method
								.getParameters();
						ArrayList<FormalParam> llFP2 = (ArrayList<FormalParam>) method2
								.getParameters();
						if (llFP1.size() != llFP2.size()) {
							throw new LCPLException(
									"Overloaded method has a different number of parameters",
									method);
						} else {
							for (int i = 0; i < llFP1.size(); i++) {
								if (llFP1.get(i).getType()
										.equals(llFP2.get(i).getType()) == false)
									throw new LCPLException(
											"Parameter "
													+ llFP2.get(i).getName()
													+ " has a different type in overloaded method.",
											method);

							}
						}

						if (retType1.equals(retType2) == false) {
							throw new LCPLException(
									"Return type changed in overloaded method.",
									method);
						}
					} else if (lcplClass.existsMethod(lcplMethod))
						throw new LCPLException(
								"A method with the same name already exists in class "
										+ cls.getName() + " : "
										+ method.getName(), method);
					lcplClass.insertMethod(lcplMethod);

				}

			}
			if (cls.getName().equals("Main")
					&& lcplClass.getKnownMethods().get("main") == null)
				throw new LCPLException("Method main not found in class Main",
						cls);

		}
		if (programClasses.get("Main") == null)
			throw new LCPLException("Class Main not found.",
					orderedClasses.get(0));
		for (LCPLClass cls : orderedClasses) {
			LCPLProgramClass lcplClass = (LCPLProgramClass) programClasses
					.get(cls.getName());

			for (Feature feature : cls.getFeatures()) {

				if (feature instanceof Attribute) {
					String key = ((Attribute) feature).getName();
					currentAttr = (Attribute) feature;
					doAttribute(lcplClass, (Attribute) lcplClass
							.getKnownAttributes().get(key));
				}
				if (feature instanceof Method) {
					String key = ((Method) feature).getName();
					fillMethod(
							(LCPLMethod) (lcplClass.getKnownMethods().get(key)),
							lcplClass);
				}
			}

		}

	}
	/*
	 * Main
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err
					.println("Usage: LCPLSemant <filein.yaml> <fileout.yaml>\n");
			System.exit(1);
		}
		try {
			Yaml yaml = new Yaml(new Constructor(Program.class));
			FileInputStream fis = new FileInputStream(args[0]);
			Program p = (Program) yaml.load(fis);
			NoType nt = new NoType();
			NullType nlt = new NullType();
			IntType it = new IntType();
			/*
			 * definesc clasele pentru tipurile Object, Int String, NoType,
			 * void, IO + metodele lor
			 */
			LCPLClass st = new LCPLClass();
			st.setParent("Object");
			st.setName("String");
			List<Feature> stringFeatures = new LinkedList<Feature>();
			Method strLength = new Method(0, "length",
					new LinkedList<FormalParam>(), "Int", null);
			FormalParam fp = new FormalParam("self", "String");
			fp.setVariableType(st);
			strLength.setParent(st);
			strLength.setReturnTypeData(it);
			strLength.setSelf(fp);
			Method strToInt = new Method(0, "toInt",
					new LinkedList<FormalParam>(), "Int", null);
			FormalParam fp3 = new FormalParam("self", "String");
			fp3.setVariableType(st);
			strToInt.setParent(st);
			strToInt.setReturnTypeData(it);
			strToInt.setSelf(fp3);
			stringFeatures.add(strLength);
			stringFeatures.add(strToInt);
			st.setFeatures(stringFeatures);
			st.setParent("Object");
			st.setName("String");

			LCPLProgramClass lcplClsStr = new LCPLProgramClass();
			LCPLMethod lcplLengthMethod = new LCPLMethod();
			lcplLengthMethod.setMethod(strLength);
			lcplClsStr.insertMethod(lcplLengthMethod);

			LCPLMethod lcplToIntMethod = new LCPLMethod();
			lcplToIntMethod.setMethod(strToInt);
			lcplClsStr.insertMethod(lcplToIntMethod);

			lcplClsStr.setLCPLClass(st);
			programClasses.put(st.getName(), lcplClsStr);
			LCPLClass ot = new LCPLClass();
			ot.setName("Object");
			ot.setParent(null);
			ot.setParentData(null);
			List<Feature> objectFeatures = new LinkedList<Feature>();

			Method objAbort = new Method(0, "abort",
					new LinkedList<FormalParam>(), "void", null);
			objAbort.setParent(ot);
			FormalParam abortSelf = new FormalParam("self", "Object");
			abortSelf.setVariableType(ot);
			objAbort.setParent(ot);
			objAbort.setReturnTypeData(nt);
			objAbort.setSelf(abortSelf);

			Method objTypeName = new Method(0, "typeName",
					new LinkedList<FormalParam>(), "String", null);
			objTypeName.setParent(ot);
			FormalParam typeNameSelf = new FormalParam("self", "Object");
			typeNameSelf.setVariableType(ot);
			objTypeName.setParent(ot);
			objTypeName.setReturnTypeData(st);
			objTypeName.setSelf(typeNameSelf);

			Method objCopy = new Method(0, "copy",
					new LinkedList<FormalParam>(), "Object", null);
			objCopy.setParent(ot);

			FormalParam fpObjCopy = new FormalParam("self", "Object");
			fpObjCopy.setVariableType(ot);
			objCopy.setReturnTypeData(ot);
			objCopy.setSelf(fpObjCopy);

			objectFeatures.add(objAbort);
			objectFeatures.add(objTypeName);
			objectFeatures.add(objCopy);
			ot.setFeatures(objectFeatures);
			st.setParentData(ot);

			LCPLProgramClass lcplClsObj = new LCPLProgramClass();
			LCPLMethod lcplAbortMethod = new LCPLMethod();
			lcplAbortMethod.setMethod(objAbort);
			lcplClsObj.insertMethod(lcplAbortMethod);

			LCPLMethod lcplTypeNameMethod = new LCPLMethod();
			lcplTypeNameMethod.setMethod(objTypeName);
			lcplClsObj.insertMethod(lcplTypeNameMethod);

			LCPLMethod lcplCopyMethod = new LCPLMethod();
			lcplCopyMethod.setMethod(objCopy);
			lcplClsObj.insertMethod(lcplCopyMethod);

			lcplClsObj.setLCPLClass(ot);
			programClasses.put(ot.getName(), lcplClsObj);

			LCPLClass iot = new LCPLClass();
			iot.setName("IO");
			iot.setParent("Object");
			iot.setParentData(ot);
			List<Feature> ioFeatures = new LinkedList<Feature>();
			LinkedList<FormalParam> outFormalParam = new LinkedList<FormalParam>();
			FormalParam fpOutMethod = new FormalParam("msg", "String");
			fpOutMethod.setVariableType(st);
			outFormalParam.add(fpOutMethod);

			Method outMethod = new Method(0, "out", outFormalParam, "IO", null);
			outMethod.setParent(iot);
			outMethod.setReturnType("IO");
			outMethod.setReturnTypeData(iot);
			FormalParam outSelf = new FormalParam("self", "IO");
			outSelf.setVariableType(iot);
			outMethod.setSelf(outSelf);

			Method inMethod = new Method(0, "in",
					new LinkedList<FormalParam>(), "String", null);
			inMethod.setParent(iot);
			inMethod.setReturnType("String");
			inMethod.setReturnTypeData(st);
			FormalParam inSelf = new FormalParam("self", "IO");
			inSelf.setVariableType(iot);
			inMethod.setSelf(inSelf);
			ioFeatures.add(outMethod);
			ioFeatures.add(inMethod);
			iot.setFeatures(ioFeatures);

			LCPLProgramClass lcplClsIO = new LCPLProgramClass();
			LCPLMethod lcplOutMethod = new LCPLMethod();
			lcplOutMethod.setMethod(outMethod);
			lcplClsIO.insertMethod(lcplOutMethod);

			LCPLMethod lcplInMethod = new LCPLMethod();
			lcplInMethod.setMethod(inMethod);
			lcplClsIO.insertMethod(lcplInMethod);

			lcplClsIO.setLCPLClass(iot);

			programClasses.put(iot.getName(), lcplClsIO);
			getMethodsAttributesFromParent(lcplClsObj, lcplClsIO);
			getMethodsAttributesFromParent(lcplClsObj, lcplClsStr);
			knownTypes.put("void", nt);
			knownTypes.put("(none)", nt);
			knownTypes.put("String", st);
			knownTypes.put("Object", ot);
			knownTypes.put("Int", it);
			knownTypes.put("IO", iot);
			knownTypes.put("null", nlt);

			fillAST(p);
			p.setIntType(it);
			p.setIoType(iot);
			p.setStringType(st);
			p.setNoType(nt);
			p.setObjectType(ot);
			p.setNullType(nlt);
			p.getClasses().add(ot);
			p.getClasses().add(iot);
			p.getClasses().add(st);
			fis.close();
			Yaml yamlOut = new Yaml();
			PrintStream fos = new PrintStream(new FileOutputStream(args[1]));
			fos.println(yamlOut.dump(p));
			fos.close();
		} catch (Exception exc) {

			if (exc instanceof IOException) {
				System.err.println("File error: " + exc.getMessage());
				System.err
						.println("===================================================");
			} else if (exc instanceof LCPLException) {
				System.err.println("Error in line "
						+ ((LCPLException) exc).node.getLineNumber() + " : "
						+ ((LCPLException) exc).message);
				System.exit(1);
			} else {
				exc.printStackTrace();
			}

		}

	}

}
