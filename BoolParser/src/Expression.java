import java.util.ArrayList;
import java.util.Scanner;

public class Expression {

	private ArrayList<Token> expr;
	private AST tree;

	private Expression(String str) {
		if (str == null) {
			throw new IllegalArgumentException("The String expression can not be null!");
		}
		this.setExpr(str);
	}

	private Expression() {
	}

	public static Expression generate(String str) {
		if (str == null) {
			return null;
		} else {
			Expression expr = new Expression();
			Boolean temp = expr.setExpr(str);
			if (temp != null && temp) {
				return expr;
			} else {
				return null;
			}
		}
	}
	public static Expression generateR(String str) {
		if(str==null) {
			return null;
		} else {
			Expression expr = new Expression();
			Boolean temp = expr.setExpress(str);
			if(temp != null && temp) {
				return expr;
			} else {
				return null;
			}
		}
	}

	public Integer treeLength() {
		return getAST().getRoot().accept(new Length());
	}

	public static Expression read() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input a valid expression!");
		String temp = sc.nextLine();
		sc.close();
		return Expression.generate(temp);
	}

	public AST getAST() {
		return tree;
	}

	/**
	 * Get the value of the binary tree.
	 * 
	 * @return the boolean value of the tree
	 */
	public Boolean evaluate() {
		if (tree == null) {
			return null;
		}
		return tree.getRoot().accept(new Calculate());
	}

	/**
	 * Print the ast to the console in a formatted manner.
	 */
	public void print() {
		if (tree == null) {
			return;
		}
		tree.print(expr.size() + 1);
	}

	/**
	 * Print the source code that was used to generate the ast to the console.
	 */
	public void printSource() {
		if (tree == null) {
			return;
		}
		System.out.println(tree.getRoot().accept(new PrintSource()));
	}

	public Boolean compareValue(Expression expr) {
		if (expr == null || getAST() == null) {
			return false;
		} else {
			return this.tree.getRoot().accept(new Calculate()) == expr.getAST().getRoot().accept(new Calculate());
		}
	}

	public Boolean compareStructure(Expression expr) {
		if (expr == null || getAST() == null) {
			return false;
		} else {
			return getAST().getRoot().accept(new Compare(expr.tree.getRoot()));
		}
	}

	private Boolean setExpress(String str) { // Convert the string to a arraylist of tokens
		if (str == null) {
			return false;
		} // Convert the expression to a token list
		ArrayList<Token> toks = Tokenizer.INSTANCE().tokenize(str);
		if (toks == null) {
			return false;
		} // Check if the expression is syntacticalcorrect
		this.expr = toks; // Generate the abstract syntax:
		this.tree = new AST(setAST(this.expr));
		return true;
	}

	private Expr setAST(ArrayList<Token> list) {
		if (expr == null || list == null) {
			return new TreeError();
		} else {
			return RDP.generate(list).parse();
		}
	}

	private Boolean setExpr(String str) { // Convert the string to a arraylist of tokens
		if (str == null) {
			return false;
		} // Convert the expression to a token list
		ArrayList<Token> toks = Tokenizer.INSTANCE().tokenize(str);
		if (toks == null) {
			return false;
		}
		if (!Analyzer.INSTANCE().analyze(toks)) {
			System.out.println("There was a syntactical error!");
			return null;
		}
		// Everything is good, set the token
		expr = toks; // Generate the abstract syntax tree:
		this.tree = new AST(setAST());
		return true;
	}

	private Expr setAST() {
		if (expr == null) {
			return new TreeError();
		} else {
			return Parser.INSTANCE().generateAST(expr);
		}
	}
}
