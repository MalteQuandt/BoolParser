import java.util.ArrayList;

public class Analyzer {
	private static Analyzer anal;

	private Analyzer() {

	}

	public static Analyzer INSTANCE() {
		if (anal == null) {
			anal = new Analyzer();
		}
		return anal;
	}

	public Boolean analyze(ArrayList<Token> toks) {
		if (toks == null) {
			return false;
		}
		return syntaxCheck(toks);
	}

	/**
	 * Check if the syntax is conform and if the parenthesis are well formed. This
	 * and the conversion method should later be changed to the recursive descent
	 * parser.
	 * 
	 * @param toks
	 * @return
	 */
	private Boolean syntaxCheck(ArrayList<Token> toks) {
		// Store the previous token
		Token prev = null;
		// Count the number of parenthesis:
		Integer lparencount = 0;
		Boolean oneLit = false;
		// First test:
		if (toks.get(0).getType().isBinary()) {
			System.out.println(
					"Unexpected Binary operation " + toks.get(toks.size() - 1).getType() + " at the first Position.");
			return false;
		}
		if (toks.get(toks.size() - 1).getType().isBinary()) {
			System.out.println(
					"Unexpected Binary operation " + toks.get(toks.size() - 1).getType() + " at the last Position.");
			return false;
		}
		if (toks.get(toks.size() - 1).getType().isUnary()) {
			System.out.println(
					"Unexpected Unary operation " + toks.get(toks.size() - 1).getType() + " at the last Position.");
			return false;
		}
		// Iterate over the token stream and check for the validity of it
		for (Token tok : toks) {
			if (tok.getType().isLParen()) {
				lparencount++;
			} else if (tok.getType().isRParen()) {
				if (lparencount <= 0) {
					System.out.println(
							"Parenthesis are not well formed!" + lparencount + " at " + tok.getType().toString());
					return false;
				}
				if (prev != null) {
					if (prev.getType().isBinary()) {
						System.out.println(
								"Unexpected Binary operation " + tok.getType().toString() + " at " + tok.getPosition());
						return false;
					}
					if (prev.getType().isUnary()) {
						System.out.println(
								"Unexpected Binary operation " + tok.getType().toString() + " at " + tok.getPosition());
						return false;
					}
				}
				lparencount--;
			} else if (tok.getType().isBinary()) {
				if (prev != null) {
					if (prev.getType().isBinary()) {
						System.out.println("Unexpected Binary Opeation" + tok.getType().toString() + " at "
								+ tok.getPosition() + "!");
						return false;
					}
					if (prev.getType().isUnary()) {
						System.out.println("Unexpected Unary Opeation" + tok.getType().toString() + " at "
								+ tok.getPosition() + "!");
						return false;
					}
					if (prev.getType().isLParen()) {
						System.out.println("Unexpected Left Parenthesis" + tok.getType().toString() + " at "
								+ tok.getPosition() + "!");
						return false;
					}
				}
			} else if (tok.getType().isUnary()) {
				if (prev != null) {
					if (prev.getType().isLiteral()) {
						System.out.println(
								"Unexpected Literal " + tok.getType().toString() + " at " + tok.getPosition() + "!");
						return false;
					}
					if (prev.getType().isRParen()) {
						System.out.println("Unexpected Right Parenthesis" + tok.getType().toString() + " at "
								+ tok.getPosition() + "!");
						return false;
					}
				}

				// The token is a unary expression, thus we expect next:
				// Another unary, a left (Opening) paren or a literal:
			} else if (tok.getType().isLiteral()) {
				oneLit = true;
			}
			// store the previous token
			prev = tok;
		}
		// Some errors handlings that are still left off:
		// 1. The parenthesis are not evenly matched,
		// 2. The last token is a binary operation
		// 3. The last token is a left parenthesis
		// 4. There is not at the very least one literal in the expression
		if (lparencount != 0) {
			System.out.println("The parenthesis are not well formed! (" + lparencount + ")");
			return false;
		}
		if (prev.getType().isLParen()) {
			System.out.println("The last token can not be a left parenthesis!");
			return false;
		}
		if (prev.getType().isBinary()) {
			System.out.println("The last token can not be a binary operation!");
			return false;
		}
		if (!oneLit) {
			System.out.println("There does not seem to be a single literal in the expression!");
			return false;
		}

		return true;
	}
}
