package compiler;

public class Parser {
	public static final int _EOF = 0;
	public static final int _var = 1;
	public static final int _number = 2;
	public static final int _char = 3;
	public static final int _string = 4;
	public static final int _op = 5;
	public static final int _end = 6;
	public static final int _cond = 7;
	public static final int maxT = 26;

	static final boolean T = true;
	static final boolean x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;

	VarTable vars = new VarTable();
	String var_name = null;
	Var var1 = null, var2 = null;
	
	private int operation(int a, char op, int b){
		switch(op){
			case '+': return a+b;
			case '-': return a-b;
			case '*': return a*b;
			case '/': if (b != 0) return a/b;
		}
		return 0;
	}



	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void Derby() {
		Variables();
		Main();
	}

	void Variables() {
		Expect(8);
		Expect(9);
		while (StartOf(1)) {
			Variable();
		}
		Expect(10);
	}

	void Main() {
		Expect(11);
		Expect(9);
		while (StartOf(2)) {
			Command();
		}
		Expect(10);
	}

	void Variable() {
		if (la.kind == 12) {
			Get();
			Expect(1);
			System.out.println("integer " + t.val + " declared.");
			vars.set(new Var(t.val, Var.Type.INT, 0)); 
			
			Expect(6);
		} else if (la.kind == 13) {
			Get();
			Expect(1);
			System.out.println("boolean " + t.val + " declared.");
			vars.set(new Var(t.val, Var.Type.BOOLEAN, false)); 
			
			Expect(6);
		} else if (la.kind == 14) {
			Get();
			Expect(1);
			System.out.println("char " + t.val + " declared.");
			vars.set(new Var(t.val, Var.Type.CHAR, null)); 
			
			Expect(6);
		} else if (la.kind == 15) {
			Get();
			Expect(1);
			System.out.println("string " + t.val + " declared.");
			vars.set(new Var(t.val, Var.Type.STRING, null)); 
			
			Expect(6);
		} else SynErr(27);
	}

	void Command() {
		if (la.kind == 1) {
			Get();
			if (!vars.exists(t.val))
			SemErr(t.val + " not declared");
			
			var_name = t.val;
			
			Attribution();
			Expect(6);
		} else if (la.kind == 22) {
			Function();
			Expect(6);
		} else if (la.kind == 23) {
			Conditional();
		} else if (la.kind == 25) {
			Repeat();
		} else if (la.kind == 16) {
			Read();
			Expect(6);
		} else SynErr(28);
	}

	void Attribution() {
		Expect(19);
		if (!vars.exists(var_name))
		 		SemErr(var_name + " not declared");
		 
		if (la.kind == 1 || la.kind == 2 || la.kind == 17) {
			Expr();
		} else if (la.kind == 20 || la.kind == 21) {
			if (la.kind == 20) {
				Get();
			} else {
				Get();
			}
			if (vars.exists(var_name)){
			var1 = vars.get(var_name);
			if (var1.type == Var.Type.BOOLEAN){
				if (t.val.equals("true"))
					var1.value = true;
				else 
					var1.value = false;
				vars.set(var1);
			}
			else {
				SemErr(var1.name + " is not a boolean");
			}
			}
			else {
			SemErr(var_name + " not declared");
			}
			
		} else if (la.kind == 3) {
			Get();
			if (vars.exists(var_name)){
			   var1 = vars.get(var_name);
			   if (var1.type == Var.Type.CHAR){
			       var1.value = (char) t.val.charAt(0);
			       vars.set(var1);
			   }
			   else {
			       SemErr(var1.name + " is not a char");
			   }
			}
			else {
			   SemErr(var_name + " not declared");
			}
			
		} else if (la.kind == 4) {
			Get();
			if (vars.exists(var_name)){
			   var1 = vars.get(var_name);
			   if (var1.type == Var.Type.STRING){
			       var1.value = t.val;
			       vars.set(var1);
			   }
			   else {
			       SemErr(var1.name + " is not a string");
			   }
			}
			else {
			   SemErr(var_name + " not declared");
			}
			
		} else SynErr(29);
	}

	void Function() {
		Expect(22);
		Expect(17);
		Expect(1);
		if (vars.exists(t.val))
		System.out.println(vars.get(t.val));
		else 
		SemErr(t.val + " not declared");
		
		Expect(18);
	}

	void Conditional() {
		Expect(23);
		Expect(17);
		Comparation();
		Expect(18);
		Expect(9);
		while (StartOf(2)) {
			Command();
		}
		Expect(10);
		if (la.kind == 24) {
			Get();
			Expect(9);
			while (StartOf(2)) {
				Command();
			}
			Expect(10);
		}
	}

	void Repeat() {
		Expect(25);
		Expect(17);
		Comparation();
		Expect(18);
		Expect(9);
		while (StartOf(2)) {
			Command();
		}
		Expect(10);
	}

	void Read() {
		Expect(16);
		Expect(17);
		Expect(1);
		if (!vars.exists(t.val))
		 		SemErr(t.val + " not declared");
			
		Expect(18);
	}

	void Expr() {
		if (la.kind == 17) {
			Get();
			Expr2();
			Expect(18);
		} else if (la.kind == 1 || la.kind == 2) {
			Expr2();
		} else SynErr(30);
	}

	void Comparation() {
		Expect(1);
		if (vars.exists(t.val))
		var1 = vars.get(t.val);
		else
			SemErr(t.val + " not declared");
		  
		Expect(7);
		if (la.kind == 20 || la.kind == 21) {
			if (la.kind == 20) {
				Get();
			} else {
				Get();
			}
			if (var1.type != Var.Type.BOOLEAN)
			SemErr("Invalid boolean comparation");
			 
		} else if (la.kind == 1) {
			Get();
			if (vars.exists(t.val)){
			var2 = vars.get(t.val);
			if (var1 != null && var1.type != var2.type)
				SemErr("Invalid comparation (different types)");
			}
			else {
			SemErr(t.val + " not declared");
			}
			
		} else if (la.kind == 2) {
			Get();
			if (var1 != null && var1.type != Var.Type.INT)
			SemErr("Invalid integer comparation");
			
		} else if (la.kind == 3) {
			Get();
			if (var1 != null && var1.type != Var.Type.CHAR)
			SemErr("Invalid char comparation");
			 
		} else if (la.kind == 4) {
			Get();
			if (var1 != null && var1.type != Var.Type.STRING)
			SemErr("Invalid string comparation");
			 
		} else SynErr(31);
	}

	void Expr2() {
		int num1 = 0, num2 = 0;
		Object result = null; 
		char op;
		
		if (la.kind == 2) {
			Get();
			if (vars.exists(var_name)){
			    				var1 = vars.get(var_name);
			
			if (var1.type == Var.Type.INT){
				num1 = Integer.parseInt(t.val);
				result = num1;
			}
			else {
				SemErr(var_name + " is not an integer var");
			}
			}
			else {
			SemErr("Invalid var");
			} 
			
			if (la.kind == 5) {
				Get();
				op = t.val.charAt(0); 
				
				if (la.kind == 2) {
					Get();
					num2 = Integer.parseInt(t.val);
					result = operation(num1, op, num2);
					
				} else if (la.kind == 1) {
					Get();
					if (vars.exists(t.val)){
					var1 = vars.get(t.val);
									num2 = (int) var1.value;
									if (op == '/' && num2 == 0)
						SemErr("Invalid division by zero");
					else
						result = operation(num1, op, num2);
					}
					else {
					SemErr("Invalid var " + t.val);
					}
					
				} else SynErr(32);
			}
			if (vars.set(new Var(var_name, Var.Type.INT, result))) 
			System.out.println(String.format("%s = %s", var_name, result));
			else
			SemErr("Invalid integer operation on " + t.val);	  	
			
		} else if (la.kind == 1) {
			Get();
			if (vars.exists(t.val)){
				var1 = vars.get(t.val);
			result = var1.value;
			}
			else {
			SemErr(var_name + " not declared");
			} 
			
			if (la.kind == 5) {
				Get();
				op = t.val.charAt(0); 
				
				if (la.kind == 1) {
					Get();
					if (vars.exists(t.val)){
					var2 = vars.get(t.val);
								      		if (var1.type == var2.type){
						num1 = (int) var1.value;
					 	num2 = (int) var2.value;
					 	if (op == '/' && num2 == 0)
					SemErr("Invalid division by zero");
					else
					result = operation(num1, op, num2);
						}
						else {
							SemErr("Invalid integer operation on " + var_name);
						}
					}
					else {
					SemErr(t.val + " not declared");
					}
					
				} else if (la.kind == 2) {
					Get();
					if (var1 != null && var1.type == Var.Type.INT){
								num1 = (int) var1.value;
								num2 = Integer.parseInt(t.val);
					  				if (op == '/' && num2 == 0)
									SemErr("Invalid division by zero");
								else
									result = operation(num1, op, num2);
							}
							else {
								SemErr(var1.name + " is not an integer var");
							}
					  		 
				} else SynErr(33);
			}
			if (vars.set(new Var(var_name, Var.Type.INT, result))) 
			System.out.println(String.format("%s = %s", var_name, result));
			else
			SemErr("Invalid integer operation on " + t.val);	  	
			
		} else SynErr(34);
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Derby();
		Expect(0);

	}

	private static final boolean[][] set = {
		{T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,x,x,x, x,x,x,x, x,x,x,x, T,T,T,T, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,T,x,x, x,x,x,x, x,x,x,x, x,x,x,x, T,x,x,x, x,x,T,T, x,T,x,x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "var expected"; break;
			case 2: s = "number expected"; break;
			case 3: s = "char expected"; break;
			case 4: s = "string expected"; break;
			case 5: s = "op expected"; break;
			case 6: s = "end expected"; break;
			case 7: s = "cond expected"; break;
			case 8: s = "\"variables\" expected"; break;
			case 9: s = "\"{\" expected"; break;
			case 10: s = "\"}\" expected"; break;
			case 11: s = "\"main\" expected"; break;
			case 12: s = "\"int\" expected"; break;
			case 13: s = "\"boolean\" expected"; break;
			case 14: s = "\"char\" expected"; break;
			case 15: s = "\"string\" expected"; break;
			case 16: s = "\"read\" expected"; break;
			case 17: s = "\"(\" expected"; break;
			case 18: s = "\")\" expected"; break;
			case 19: s = "\":=\" expected"; break;
			case 20: s = "\"true\" expected"; break;
			case 21: s = "\"false\" expected"; break;
			case 22: s = "\"print\" expected"; break;
			case 23: s = "\"if\" expected"; break;
			case 24: s = "\"else\" expected"; break;
			case 25: s = "\"while\" expected"; break;
			case 26: s = "??? expected"; break;
			case 27: s = "invalid Variable"; break;
			case 28: s = "invalid Command"; break;
			case 29: s = "invalid Attribution"; break;
			case 30: s = "invalid Expr"; break;
			case 31: s = "invalid Comparation"; break;
			case 32: s = "invalid Expr2"; break;
			case 33: s = "invalid Expr2"; break;
			case 34: s = "invalid Expr2"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
