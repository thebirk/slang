package net.birk.slang;

import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrValue;
import net.birk.slang.nodes.Node;
import net.birk.slang.nodes.NodeVar;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		Lexer lexer = null;
		try {
			Timer.startSection("Lexer");
			lexer = new Lexer("test.slang");
			lexer.lex();

			Timer.startSection("Parser");
			Parser parser = new Parser(lexer.tokens);
			ArrayList<Node> nodes = parser.parse();

			Timer.startSection("Ir Gen");
			IrScope global = new IrScope(null);
			IrScope file = new IrScope(global);
			for(Node n : nodes) {
				switch (n.getType()) {
					case Node.VAR: {
						NodeVar var = (NodeVar) n;
						file.set(var.getName(), IrValue.generateExpr(var.getExpr()));
					} break;
					case Node.FUNC: {

					} break;
					default: {
						System.err.println("compiler error!");
						assert false;
						throw new RuntimeException("compiler error!");
					}
				}
			}

			Timer.startSection("Ir Run");
			//TODO: Eval all top level variables after adding them all
			// ...

			//TODO: Call main function

			Timer.printTimings();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
