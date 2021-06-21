import java.util.ArrayList;

public class AST {
	private final Expr root;

	AST(Expr root) {
		this.root = root;
	}

	public Expr getRoot() {
		return root;
	}

	public AST toAST(ArrayList<Token> tokens) {
		if (tokens == null) {
			return null;
		}
		return null;
	}

	public void printSource() {
		root.accept(new PrintSource());
	}

	public Boolean compareValue(AST ast) {
		if (ast == null) {
			return false;
		}
		return root.accept(new Calculate()) == ast.root.accept(new Calculate());
	}
	
	public void prettyPrint() {
		Integer height = getRoot().accept(new Length());
		// Generate a field big enough to print the tree to:
		Character[][] field = new Character[(int)Math.pow(2, height) + height][(int)Math.pow(2, (height+1))-1];
		// Calculate the field:
		getRoot().accept(new PrintHappyLittleTree(getRoot(), field));
		// Print the field:
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[0].length; j++) {
				System.out.print(field[i][j] == null ? " " : field[i][j]);
			}
			System.out.println();
		}
	}

	public void print() {
		this.print(-1);
	}
	/**
	 * Print the binary tree:
	 */
	public void print(Integer width) {
		Integer len = root.accept(new Length()) + 1;
		// Len*len is the maximum with that tree can have:
		Integer inneritr = (int) (width < 0 ? Math.pow(len, 2) : width);
		// Generate a new field which will be used to print the tree:
		Character[][] field = new Character[len][inneritr];
		// Load the tree to the array.
		root.accept(new PrintPreorder(field));
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < inneritr; j++) {
				System.out.print(field[i][j] == null ? ' ' : field[i][j]);
			}
			System.out.println();
		}
	}
}