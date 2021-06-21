import java.util.ArrayList;
import java.util.Stack;

public class Parser {
	private static Parser parser;

	public static Parser INSTANCE() {
		if (parser == null) {
			parser = new Parser();
		}
		return parser;
	}

	/**
	 * Generate a new AST from a valid boolean expression
	 * 
	 * @param tox the token list
	 * @return the expression tree (AST)
	 */
	public Expr generateAST(ArrayList<Token> tox) {
		// TODO: Mash those two steps together:
		ArrayList<Token> out = new ArrayList<>();
		Stack<Token> stack = new Stack<>();
		// Throw error type on wrong parameter
		if (tox == null) {
			return new TreeError();
		}
		if (tox.size() == 0) {
			return new TreeError();
		}
		for (Token tok : tox) {
			if (tok.getType().isBinary()) {
				// If we hit a binary operation, we need to pop the top of the stack onto the
				// output queue as long as the stack is non-empty, the top is not a parenthesis
				// and the precedence of the current token has a higher precedence than the item
				// on the top.
				while (!stack.isEmpty() && (!stack.peek().getType().isParen()
						&& tok.getType().higherequalPrecedence(stack.peek().getType()))) {
					out.add(stack.pop());
				}
				// Now we push the binary operation towards the stack.
				stack.push(tok);
			} else if (tok.getType().isLParen() || tok.getType().isUnary()) {
				// Unary Operators and left parenthesis get pushed to the stack
				stack.push(tok);
			} else if (tok.getType().isRParen()) {
				// If we hit a right parenthesis, we push all the items from the stack onto the
				// output queue, until we hit a left
				// Parenthesis
				while (!stack.peek().getType().equals(Type.LPAREN)) {
					out.add(stack.pop());
				}
				stack.pop();
			} else {
				// Constant/Variable will just be pushed to the output queue.
				out.add(tok);
			}
		}
		while (!stack.isEmpty()) {
			out.add(stack.pop());
		}
		// NOw that we have sortet the tokens in a better format, we can convert it into
		// an ast:
		Stack<Expr> expre = new Stack<Expr>();
		Expr exp = null;
		// Convert the data into an ast:
		for (Token tok : out) {
			if (tok.getType().isLiteral()) {
				// Convert to literal leaf
				exp = new Literal(tok);
				// Push literal leaf to the stack:
				expre.push(exp);
			} else if (tok.getType().isUnary()) {
				// Take the first item on the stack and attach it to the newly
				// generated unary leaf:
				exp = new Not(expre.pop());
				// Push the new unary expression back onto the stack:
				expre.push(exp);
			} else if (tok.getType().isBinary()) {
				// Take the first two items on the stack and attach them to the newly generated
				// token:
				switch (tok.getType()) {
				case AND:
					exp = new And(expre.pop(), expre.pop());
					break;
				case OR:
					exp = new Or(expre.pop(), expre.pop());
					break;
				case XOR:
					exp = new Xor(expre.pop(), expre.pop());
					break;
				default:
					throw new IllegalArgumentException("I don't know how this happened... Did you mess with the code?");
				}
				expre.push(exp);
			}
		}
		return exp;
	}

	/**
	 * This is a traditional recursive descent parser which does nothing yet. Later
	 * it can be used to more efficiently parse the expression and skip the checking
	 * method completely!
	 * 
	 * @return the tree.
	 */
	public Expr rdp(ArrayList<Token> toks) {

		return null;
	}
}
