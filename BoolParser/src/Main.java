public class Main {
	// PRECEDENCE: NOT 0, AND 1, XOR 2, OR 3
	// AND != and != AnD
	//
	public static void main(String[] args) {
		Expression expr2 = Expression.generateR(new String("true AND NOT true"));
		Expression expr1 = Expression
				.generateR(new String("true AND NOT true"));
		// expr1 = Expression.read();

		System.out.println("Length: " + expr1.treeLength());

		System.out.println("Value:");
		System.out.println(expr1.evaluate());
		System.out.println("Print the Tree:");
		expr1.print();
		System.out.println("Print the source:");
		expr1.printSource();
		System.out.println("Compare the structure:");
		System.out.println(expr1.compareStructure(expr2));
		System.out.println("Compare the values:");
		System.out.println(expr1.compareValue(expr1));

		expr1.print();
		expr1.getAST().prettyPrint();
	}
}
