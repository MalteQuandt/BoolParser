import java.util.HashMap;

// Simple enum for the operators, literals and parenthesis.
enum Type {
	TRUE, FALSE, AND, NOT, LPAREN, RPAREN, OR, XOR, BINARY, UNARY;

	@SuppressWarnings("serial")
	static private HashMap<Type, Integer> precedence = new HashMap<Type, Integer>() {
		{
			put(NOT, 0);
			put(AND, 1);
			put(XOR, 2);
			put(OR, 3);
		}
	};

	@SuppressWarnings("serial")
	static private HashMap<Type, Boolean> binary = new HashMap<Type, Boolean>() {
		{
			put(AND, true);
			put(XOR, true);
			put(OR, true);
			put(NOT, false);
		}
	};

	// static private HashMap<Type, Boolean> leftAssociative = new HashMap<Type,
	// Boolean>() {{}};
	Boolean isLiteral() {
		return this == TRUE || this == FALSE;
	}

	Boolean isLParen() {
		return this == LPAREN;
	}

	Boolean isRParen() {
		return this == RPAREN;
	}

	Boolean isParen() {
		return isLParen() || isRParen();
	}

	Boolean isBinary() {
		if (binary.containsKey(this)) {
			return binary.get(this);
		} else {
			return false;
		}
	}

	Boolean isUnary() {
		if (binary.containsKey(this)) {
			return !binary.get(this);
		} else {
			return false;
		}
	}

	Boolean isOperation() {
		return isBinary() || isUnary();
	}

	Integer getPrecedence() {
		return Type.precedence.get(this);
	}

	Boolean higherequalPrecedence(Type type) {
		if (type == null) {
			return null;
		}
		return this.getPrecedence() >= type.getPrecedence();
	}
}