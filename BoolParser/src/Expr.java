
abstract class Expr {
	abstract <R> R accept(Visitor<R> visit);

	public Boolean equals(Expr expr) {
		return getType().equals(expr.getType());
	}

	public abstract Type getType();
}

class Binary extends Expr {
	private final Expr right;
	private final Expr left;

	Binary(Expr left, Expr right) {
		this.right = right;
		this.left = left;
	}

	public Expr getLeft() {
		return left;
	}

	public Expr getRight() {
		return right;
	}

	@Override
	<R> R accept(Visitor<R> visit) {
		return null;
	}

	@Override
	public Type getType() {
		return Type.BINARY;
	}
}

class Unary extends Expr {
	private final Expr right;

	Unary(Expr expr) {
		this.right = expr;
	}

	public Expr getRight() {
		return right;
	}

	@Override
	<R> R accept(Visitor<R> visit) {
		return null;
	}

	@Override
	public Type getType() {
		return Type.UNARY;
	}
}

class And extends Binary {
	And(Expr left, Expr right) {
		super(left, right);
	}

	@Override
	<R> R accept(Visitor<R> visit) {
		return visit.visitAnd(this);
	}

	@Override
	public String toString() {
		return "AND";
	}

	@Override
	public Type getType() {
		return Type.AND;
	}
}

class Xor extends Binary {
	Xor(Expr left, Expr right) {
		super(left, right);
	}

	@Override
	<R> R accept(Visitor<R> visit) {
		return visit.visitXor(this);
	}

	@Override
	public String toString() {
		return "XOR";
	}

	@Override
	public Type getType() {
		return Type.XOR;
	}
}

class Or extends Binary {
	Or(Expr left, Expr right) {
		super(left, right);
	}

	@Override
	<R> R accept(Visitor<R> visit) {
		return visit.visitOr(this);
	}

	@Override
	public String toString() {
		return "OR";
	}

	@Override
	public Type getType() {
		return Type.OR;
	}
}

class Not extends Unary {
	Not(Expr expr) {
		super(expr);
	}

	@Override
	<R> R accept(Visitor<R> visit) {
		return visit.visitNot(this);
	}

	@Override
	public String toString() {
		return "NOT";
	}

	@Override
	public Type getType() {
		return Type.NOT;
	}
}

// Return this class in case of a user generated error
class TreeError extends Expr {

	@Override
	<R> R accept(Visitor<R> visit) {
		return null;
	}

	@Override
	public String toString() {
		return "ERROR";
	}

	@Override
	public Type getType() {
		return null;
	}

}

class Literal extends Expr {
	private final Token tok;

	Literal(Token tok) {
		this.tok = tok;
	}

	public Token getToken() {
		return tok;
	}

	@Override
	<R> R accept(Visitor<R> visit) {
		return visit.visitLiteral(this);
	}

	@Override
	public String toString() {
		return tok.getType().name();
	}

	@Override
	public Type getType() {
		return this.tok.getType();
	}
}

class Compare implements Visitor<Boolean> {

	private final Expr expr;

	Compare(Expr expr) {
		this.expr = expr;
	}

	@Override
	public Boolean visitAnd(And expr) {
		if (!expr.equals(this.expr))
			return false;
		if (!((And) this.expr).getLeft().accept(new Compare(expr.getLeft())))
			return false;
		if (!((And) this.expr).getRight().accept(new Compare(expr.getRight())))
			return false;
		return true;
	}

	@Override
	public Boolean visitXor(Xor expr) {
		if (!expr.equals(this.expr))
			return false;
		if (!((Xor) this.expr).getLeft().accept(new Compare(expr.getLeft())))
			return false;
		if (!((Xor) this.expr).getRight().accept(new Compare(expr.getRight())))
			return false;
		return true;
	}

	@Override
	public Boolean visitOr(Or expr) {
		if (!expr.equals(this.expr))
			return false;
		if (!((Or) this.expr).getLeft().accept(new Compare(expr.getLeft())))
			return false;
		if (!((Or) this.expr).getRight().accept(new Compare(expr.getRight())))
			return false;
		return true;
	}

	@Override
	public Boolean visitNot(Not expr) {
		if (!expr.equals(this.expr))
			return false;
		if (!((Not) this.expr).getRight().accept(new Compare(expr.getRight())))
			return false;
		return true;
	}

	@Override
	public Boolean visitLiteral(Literal expr) {
		return this.expr.equals(expr);
	}

}

class PrintSource implements Visitor<String> {

	@Override
	public String visitAnd(And epxr) {
		return "new And(" + epxr.getLeft().accept(new PrintSource()) + ", " + epxr.getRight().accept(new PrintSource())
				+ ")";
	}

	@Override
	public String visitXor(Xor expr) {
		return "new Xor(" + expr.getLeft().accept(new PrintSource()) + ", " + expr.getRight().accept(new PrintSource())
				+ ")";
	}

	@Override
	public String visitOr(Or expr) {
		return "new Or(" + expr.getLeft().accept(new PrintSource()) + ", " + expr.getRight().accept(new PrintSource())
				+ ")";
	}

	@Override
	public String visitNot(Not expr) {
		return "new Not(" + expr.getRight().accept(new PrintSource()) + ")";
	}

	@Override
	public String visitLiteral(Literal expr) {
		return "new Literal(Token.generate(Type." + expr.getToken().getType() + "," + expr.getToken().getPosition()
				+ "))";
	}

}

class PrintHappyLittleTree implements Visitor<Integer> {

	private final Integer length;
	private final Integer row;
	private final Integer col;
	private final Character[][] field;
	private final Integer sketches;

	public PrintHappyLittleTree(Character[][] field, Integer row, Integer col, Integer length) {
		this.length = length;
		this.row = row;
		this.col = col;
		this.field = field;
		this.sketches = (int) Math.pow(2, this.length - 1);
	}

	public PrintHappyLittleTree(Expr expr, Character[][] field) {
		// Initilize values:
		this.length = length(expr);
		this.row = 0;
		this.field = field;
		this.col = field[0].length / 2;
		// Print the width and height of the field:
		System.out.println("Width: " + field.length);
		System.out.println("Height: " + field[0].length);
		// The amount of sketches to draw below:
		this.sketches = (int) Math.pow(2, this.length - 1);

	}

	// Utility Methods:
	private Integer length(Expr expr) {
		if (expr == null) {
			return null;
		}
		return expr.accept(new Length());
	}

	private void drawDownBinary(Integer newLen, char bin) {
		// Draw down the right and left side of the tree:
		// 1. Draw the top value:
		field[row][col] = bin;
		// 2. Draw left and right up to the number of sketches below:
		for (Integer a = 1; a <= newLen; a++) {
			// 2.1 Draw the left side:
			field[row + a][col - a] = '/';
			// 2.2 Draw the right side:
			field[row + a][col + a] = '\\';
		}

	}

	private void drawDownUnary(Integer newLen, char bin) {
		// Draw down the right and left side of the tree:
		// 1. Draw the top value:
		field[row][col] = bin;
		// 2. Draw left and right up to the number of sketches below:
		for (Integer a = 1; a <= newLen; a++) {
			// Draw the right side:
			field[row + a][col + a] = '\\';
		}
	}

	@Override
	public Integer visitAnd(And expr) {
		Integer a = (int) (Math.pow(2, expr.accept(new Length()) - 1));
		drawDownBinary((int) (Math.pow(2, expr.accept(new Length()) - 1)), '&');
		expr.getLeft().accept(new PrintHappyLittleTree(this.field, row + a + 1, col + a, length - 1));
		expr.getRight().accept(new PrintHappyLittleTree(this.field, row + a + 1, col - a, length - 1));
		return 0x0;
	}

	@Override
	public Integer visitXor(Xor expr) {
		Integer a = (int) (Math.pow(2, expr.accept(new Length()) - 1));
		drawDownBinary(a, '^');
		expr.getLeft().accept(new PrintHappyLittleTree(this.field, row + a + 1, col + a, length - 1));
		expr.getRight().accept(new PrintHappyLittleTree(this.field, row + a + 1, col - a, length - 1));
		return 0x0;
	}

	@Override
	public Integer visitOr(Or expr) {
		Integer a = (int) (Math.pow(2, expr.accept(new Length()) - 1));
		drawDownBinary(a, '|');
		expr.getLeft().accept(new PrintHappyLittleTree(this.field, row + a + 1, col + a, length - 1));
		expr.getRight().accept(new PrintHappyLittleTree(this.field, row + a + 1, col - a, length - 1));
		return 0x0;
	}

	@Override
	public Integer visitNot(Not expr) {
		Integer a = (int) (Math.pow(2, expr.accept(new Length()) - 1));
		drawDownUnary(a, '!');
		expr.getRight().accept(new PrintHappyLittleTree(this.field, row + a + 1, col + a, length - 1));
		return 0x0;
	}

	@Override
	public Integer visitLiteral(Literal expr) {
		field[row][col] = expr.getToken().getType() == Type.TRUE ? '1' : '0';
		return 0x0;
	}

}

class PrintPreorder implements Visitor<Integer> {

	private Character[][] builder;
	private Integer row;
	private Integer col;

	PrintPreorder(Character[][] field) {
		this.builder = field;
		this.row = 0;
		this.col = 0;
	}

	PrintPreorder(Character[][] field, Integer row, Integer col) {
		this.builder = field;
		this.row = row;
		this.col = col;
	}

	@Override
	public Integer visitAnd(And expr) {
		col = expr.getRight().accept(new PrintPreorder(this.builder, this.row + 1, this.col));
		col++;
		builder[row][col] = '&';
		col = expr.getLeft().accept(new PrintPreorder(this.builder, this.row + 1, this.col));
		return col;
	}

	@Override
	public Integer visitXor(Xor expr) {
		col = expr.getRight().accept(new PrintPreorder(this.builder, this.row + 1, this.col));
		col++;
		builder[row][col] = '^';
		col = expr.getLeft().accept(new PrintPreorder(this.builder, this.row + 1, this.col));
		return col;
	}

	@Override
	public Integer visitOr(Or expr) {
		col = expr.getRight().accept(new PrintPreorder(this.builder, this.row + 1, this.col));
		col++;
		builder[row][col] = '|';
		col = expr.getLeft().accept(new PrintPreorder(this.builder, this.row + 1, this.col));
		return col;
	}

	@Override
	public Integer visitNot(Not expr) {
		col++;
		builder[row][col] = '!';
		col = expr.getRight().accept(new PrintPreorder(this.builder, this.row + 1, this.col));
		return col;
	}

	@Override
	public Integer visitLiteral(Literal expr) {
		col++;
		builder[row][col] = expr.getToken().getType() == Type.TRUE ? '1' : '0';
		return col;
	}

}

class UglyPrinter implements Visitor<Integer> {

	private Integer offset;
	private static final Integer oset = 4;

	UglyPrinter() {
		this(0);
		System.out.println("AST: ");
	}

	UglyPrinter(Integer offset) {
		this.offset = offset;
	}

	@Override
	public Integer visitAnd(And expr) {
		offset += oset;
		// Visit the right tree
		expr.getRight().accept(new UglyPrinter(offset));
		// Inorder printing of the tree:
		print(offset, "AND");
		// Visit the left tree:
		expr.getLeft().accept(new UglyPrinter(offset));
		return 0;
	}

	@Override
	public Integer visitXor(Xor expr) {
		offset += oset;
		// Visit the right tree
		expr.getRight().accept(new UglyPrinter(offset));
		// Inorder printing of the tree:
		print(offset, "XOR");
		// Visit the left tree:
		expr.getLeft().accept(new UglyPrinter(offset));
		return 0;
	}

	@Override
	public Integer visitOr(Or expr) {
		offset += oset;
		// Visit the right tree
		expr.getRight().accept(new UglyPrinter(offset));
		// Inorder printing of the tree:
		print(offset, "OR");
		// Visit the left tree:
		expr.getLeft().accept(new UglyPrinter(offset));
		return 0;
	}

	@Override
	public Integer visitNot(Not expr) {
		offset += oset;
		// Inorder printing of the tree:
		print(offset, "NOT");
		// Visit the right tree
		expr.getRight().accept(new UglyPrinter(offset));
		return 0;
	}

	@Override
	public Integer visitLiteral(Literal expr) {
		offset += oset;
		print(offset, expr.getToken().getType().name());
		return 0x0;
	}

	private void print(Integer offset, String name) {
		// System.out.println();
		for (Integer i = oset; i < offset; i++) {
			System.out.print(" ");
		}
		System.out.println(name);
	}

}

/**
 * Calculate the maximal length of the kompositorium binary tree.
 * 
 * @author Malte Quandt
 */
class Length implements Visitor<Integer> {
	private Integer len;

	Length(Integer len) {
		this.len = len;
	}

	Length() {
		this.len = 0;
	}

	@Override
	public Integer visitAnd(And expr) {
		Integer right = expr.getRight().accept(new Length(this.len + 1));
		Integer left = expr.getLeft().accept(new Length(this.len + 1));
		return right > left ? right : left;
	}
	
	@Override
	public Integer visitXor(Xor expr) {
		Integer right = expr.getRight().accept(new Length(this.len + 1));
		Integer left = expr.getLeft().accept(new Length(this.len + 1));
		return right > left ? right : left;
	}

	@Override
	public Integer visitOr(Or expr) {
		Integer right = expr.getRight().accept(new Length(this.len + 1));
		Integer left = expr.getLeft().accept(new Length(this.len + 1));
		return right > left ? right : left;
	}

	@Override
	public Integer visitNot(Not expr) {
		return expr.getRight().accept(new Length(this.len + 1));
	}

	@Override
	public Integer visitLiteral(Literal expr) {
		return this.len;
	}
}

class Calculate implements Visitor<Boolean> {
	@Override
	public Boolean visitAnd(And expr) {
		return expr.getRight().accept(new Calculate()) && expr.getLeft().accept(new Calculate());
	}

	@Override
	public Boolean visitOr(Or expr) {
		return expr.getRight().accept(new Calculate()) || expr.getLeft().accept(new Calculate());
	}

	@Override
	public Boolean visitNot(Not expr) {
		return !expr.getRight().accept(new Calculate());
	}

	@Override
	public Boolean visitLiteral(Literal expr) {
		return expr.getToken().getType() == Type.TRUE;
	}

	@Override
	public Boolean visitXor(Xor expr) {
		return expr.getRight().accept(new Calculate()) ^ expr.getLeft().accept(new Calculate());
	}
}