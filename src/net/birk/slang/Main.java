package net.birk.slang;

import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.stmt.IrStmtResult;
import net.birk.slang.ir.value.*;
import net.birk.slang.nodes.Node;
import net.birk.slang.nodes.NodeFunc;
import net.birk.slang.nodes.NodeVar;

import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
						if(!file.add(var.getName(), IrValue.generateExpr(var.getExpr()))) {
							throw new IrException(var.getLocation(), "Symbol '" + var.getName() + "' already exists!");
						}
					} break;
					case Node.FUNC: {
						NodeFunc f = (NodeFunc) n;
						if(!file.add(f.getIdent().getName(), IrValue.generateExpr(n))) {
							throw new IrException(f.getLocation(), "Symbol '" + f.getIdent().getName() + "' already exists!");
						}
					} break;
					default: {
						System.err.println("compiler error!");
						assert false;
						throw new RuntimeException("compiler error!");
					}
				}
			}

			global.add("println", new IrJavaFunc(null) {
				@Override
				public IrValue call(IrScope scope, ArrayList<IrValue> args) {
					for(IrValue v : args) {
						switch (v.getType()) {
							case IrValue.STRING: {
								IrString str = (IrString) v;
								System.out.print(str.getValue());
							} break;
							case IrValue.NUMBER: {
								IrNumber n = (IrNumber) v;
								System.out.print(n.getValue());
							} break;
							case IrValue.NULL: {
								System.out.print("null");
							} break;
							case IrValue.BOOLEAN: {
								IrBoolean b = (IrBoolean)v;
								System.out.print(b.getValue());
							} break;
							case IrValue.FUNC: {
								throw new RuntimeException("Unimplemented!");
							} //break;
						}
					}
					System.out.println();

					return new IrNull(null);
				}
			});

			Timer.startSection("Ir Run");
			//TODO: Eval all top level variables after adding them all
			// ... Do we really need to do that? Yes!
			for(Map.Entry<String, IrValue> kv : file.getSymbols().entrySet()) {
				IrValue v = kv.getValue();
				file.set(kv.getKey(), v.eval(file));
			}

			//TODO: Call main function
			ArrayList<IrValue> mainArgs = new ArrayList<IrValue>();
			((IrFunc)file.get("main")).call(file, mainArgs);

			System.out.println("\n============================");
			Timer.printTimings();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
