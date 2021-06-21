interface Visitor<R> {
	public R visitAnd(And expr);

	public R visitXor(Xor expr);

	public R visitOr(Or expr);

	public R visitNot(Not expr);

	public R visitLiteral(Literal expr);
}