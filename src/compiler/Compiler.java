package compiler;

import java.io.UnsupportedEncodingException;

public class Compiler {

	public static void main(String[] args) throws UnsupportedEncodingException {

		if (args.length > 0) {
			
			try {
				System.out.println("Reading source file " + args[0]);
				Scanner scanner = new Scanner(args[0]);
			
				System.out.println("Parsing source file " + args[0]);
				Parser parser = new Parser(scanner);
				
				System.out.println();
				parser.Parse();
				
				System.out.println("\n=> " + parser.errors.count + " error(s)");
			}
			catch (NullPointerException e){
				System.out.println("Erro: " + e.getMessage());
			}

		} else {
			System.out.println("Syntax: java Compiler <source file>");
			Scanner scanner = new Scanner("input/input1.txt");
			
			System.out.println("Parsing source file input/input1.txt");
			Parser parser = new Parser(scanner);
			
			System.out.println();
			parser.Parse();
			
			System.out.println("\n=> " + parser.errors.count + " error(s)");
			
		}
		
	}

}
