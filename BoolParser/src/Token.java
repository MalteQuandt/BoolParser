// Token to store the types
class Token {
	private final Type type;
	private final Integer position;
	
	// Factory for the token generation
	static Token generate(Type type, Integer position) {
		if(type == null || position < 0) {
			return null;
		} else {
			return new Token(type, position);
		}
	}
	static Token generate(String tok, Integer position) {
		if(tok == null || position < 0) {
			return null;
		}
		Type tempType = null;

		switch(tok) {
			case "true": tempType = Type.TRUE;break;
			case "false": tempType = Type.FALSE;break;
			case "AND": tempType = Type.AND;break;
			case "OR": tempType = Type.OR;break;
			case "NOT": tempType = Type.NOT;break;
			case "(": tempType = Type.LPAREN;break;
			case ")": tempType = Type.RPAREN;break;
			case "XOR": tempType = Type.XOR;break;
		}
		return new Token(tempType, position);
	}

	private Token(Type type, Integer position) {
		this.type = type;
		this.position = position;
	}
	
	public Type getType() {
		return type;
	}
	
	public Integer getPosition() {
		return position;
	}
}