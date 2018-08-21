package net.birk.slang;

import net.birk.slang.nodes.Node;

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
			Timer.endSection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Timer.printTimings();
    }
}
