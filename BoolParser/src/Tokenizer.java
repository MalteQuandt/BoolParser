import java.util.ArrayList;

public class Tokenizer {
	private static Tokenizer tok;

	public static Tokenizer INSTANCE() {
		if (tok == null) {
			tok = new Tokenizer();
		}
		return tok;
	}

	public ArrayList<Token> tokenize(String expr) {
		return generateTokens(expr);
	}

	private Boolean check(String str) {
		// Check if the string matches with the alphabet and return a predicate value
		// whether it does or not.
		switch (str) {
		case "true":
		case "false":
		case "AND":
		case "OR":
		case "NOT":
		case "(":
		case ")":
		case "XOR":
			return true;
		default:
			return false;
		}
	}

	@Deprecated
	private ArrayList<Token> toToken(String[] toks) {
		Integer len = 0; // Do some checks if the parameters are valid
		if (toks == null || ((len = toks.length) == 0)) {
			return null;
		}
		ArrayList<Token> list = new ArrayList<Token>(len); // Iterate over the string array and convert it into an
															// arraylist of tokens
		for (Integer pos = 0; pos < len; pos++) { // Check, if the token is a valid token in the alphabet.
			if (!check(toks[pos])) {
				System.out.println("Unexpected Token at " + pos + " ( " + toks[pos] + " )!");
				return null;
			} // Generate the token with a factory and push it onto the list
			list.add(Token.generate(toks[pos], pos));
		}
		return list;
	}

	// Does not depend on the split function, which i have found to be not reliable
	private ArrayList<Token> generateTokens(String expr) {
		ArrayList<Token> toks = new ArrayList<Token>();
		Integer len = expr.length();
		Token temp = null;
		for (Integer i = 0; i < len; i++) {
			if (Character.isLetter(expr.charAt(i))) {
				// Look if it matches one of the words of our alphabet, and if it does create
				// the
				// token to push to the list.
				// If it does not, send error and end the procedure.
				Integer start = i;
				for (; (expr.length() != i) && Character.isLetter(expr.charAt(i)); i++)
					;
				// Generate a substring
				String str = expr.substring(start, i);
				// Check if the string belongs to the alphabet
				if (check(str)) {
					temp = Token.generate(str, start);
					i--;
				} else {
					System.out.println("Unexpected Token at " + start + " ( " + str + " )!");
					return null;
				}
			} else if (Character.isWhitespace(expr.charAt(i))) {
				// Ignore those characters:
				continue;
			} else if (expr.charAt(i) == '(') {
				// Generate a new Token for the parenthesis:
				temp = Token.generate(Type.LPAREN, i);
			} else if (expr.charAt(i) == ')') {
				temp = Token.generate(Type.RPAREN, i);
			} else {
				// Unexpected character that just should not be here:
				System.out.println("Unexpected Token at " + i + " ( " + expr.charAt(i) + " )!");
				return null;
			}
			// Push the token to the list
			toks.add(temp);
		}

		return toks;
	}
}
