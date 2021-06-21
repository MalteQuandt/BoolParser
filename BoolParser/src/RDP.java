import java.util.ArrayList;

public class RDP {
	// Position in the token list:
	private Integer pos = 0;
	// Token list:
	private ArrayList<Token> list;
	// Get the singleton instance to the recursive descent parser
	public static RDP generate(ArrayList<Token> list) {
		if(list==null) {
			return null;
		}
		return new RDP(list);
	}
	private RDP(ArrayList<Token> list) {
		this.list = list;
	}

	// Grammar for the boolean expressions:
	// EXPR:= TERM ( "OR" TERM)*
	// TERM:= FACTOR ("XOR" FACTOR)*
	// FACTOR:= UNARY ("AND" UNARY)*
	// UNARY:= "!" UNARY | PRIM
	// PRIM:= "true" | "false" | "(" EXPR ")"
	public Expr parse() {
		try {
			return expr();
		} catch(ParseError e) {
			System.out.println("There was an exception when parsing the tree!");
			return new TreeError();
		}
		
	}

	// parsing methods:
	private Expr expr() throws ParseError {
		Expr expr = term();
		
		while(match(Type.OR)) {
			Expr right = term();
			expr = new Or(expr, right);
		}
		return expr;
	}
	private Expr term() throws ParseError {
		Expr expr = factor();
		
		while(match(Type.XOR)) {
			Expr right = factor();
			expr = new Xor(expr, right);
		}
		return expr;
	}
	private Expr factor() throws ParseError {
		Expr expr = unary();
		
		while(match(Type.AND)) {
			Expr right = unary();
			expr = new And(expr, right);
		}
		return expr;
	}
	private Expr unary() throws ParseError {
		if(match(Type.NOT)) {
			Expr right = unary();
			if(right==null) {
				throw new Error("There was an error");
			}
			return new Not(right);
		}
		return prim();
	}
	private Expr prim() throws ParseError {
		if(match(Type.FALSE)) {
			return new Literal(prev());
		}
		if(match(Type.TRUE)) {
			return new Literal(prev());
		}
		// System.out.println(list.get(pos).getType().toString());
		if(match(Type.LPAREN)) {
			Expr expr = expr();
			consume(Type.RPAREN, "Expect ')' after Expression");
			return expr;
		}
		
		throw new ParseError("The expression is wrongly formed at the token " + (atEnd() ? "after the end" : list.get(pos).toString()));
	}
	
	// Utility methods:
	
	private Token consume(Type type, String mssg) {
		if(check(type)) {return advance();}
		throw new Error(mssg);
		
	}
	private Boolean match(Type type) {
		if(!check(type)) {
			return false;
		} 
		advance();
		return true;
	}
	private Boolean check(Type type) {
		if(!atEnd()) return peek().getType().equals(type);
		else return false;
	}
	private Boolean atEnd() {
		if(list== null ) {
			return null;
		}
		return pos == list.size();
	}
	private Token peek() {
		if(list==null) {
			return null;
		}
		return list.get(pos);
	}
	private Token prev() {
		if(list== null) {
			return null;
		}
		return list.get(pos-1);
	}
	private Token advance() {
		if(list==null) {
			return null;
		}
		if(!atEnd()) {
			pos++;
		}
		return prev();
	}
}
