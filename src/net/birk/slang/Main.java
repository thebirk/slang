package net.birk.slang;

import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.IrValue;
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
			for(Token t : lexer.tokens) {
				System.out.println(Token.typeToString(t.getType()) + ": '" + t.getLexeme() + "' :" + t.getSourceLoc());
			}
			Timer.endSection();

			Timer.startSection("Parser");
			Parser parser = new Parser(lexer.tokens);
			ArrayList<Node> nodes = parser.parse();

			IrScope global = new IrScope(null);
			IrScope root = new IrScope(global);
			for(Node n : nodes) {
				switch (n.getType()) {
					case Node.VAR: {
						NodeVar var = (NodeVar) n;
						root.set(var.getName(), IrValue.generateExpr(var.getExpr()));
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

			Timer.endSection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Timer.printTimings();
    }
}
