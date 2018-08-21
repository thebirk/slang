package net.birk.slang;

import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.*;
import net.birk.slang.nodes.Node;
import net.birk.slang.nodes.NodeFunc;
import net.birk.slang.nodes.NodeVar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {

	/*
	 * TODO:
	 *  - IrJavaFunc:
	 *   - Arguments that take the arg count
	 *   - Flag to set varargs
	 *  - IrSlangFunc:
	 *   - Varargs flags
	 *    - only varargs if last param looks like this "*ident", turns into array
	 *    - Pass all args as array
	 *  - Add table type {}
	 *  - Add userdata type (remember to implement the userdata tag)
	 *
	 * TODONE:
	 *  + Add array type []
	 *
	 * MAYBEDO:
	 *   - Python like **kwargs for tables?
	 *   - Argument list type hint
	 *    - Return type as well?
	 *    - ex.
	 *      fn add(a: Number, b: Number): Number {
	 *      	return a + b;
	 *      }
	 *
	 */

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

			global.add("println", new IrJavaFunc(new SourceLoc("builtin-println", 1, 1)) {
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

			global.add("exit", new IrJavaFunc(new SourceLoc("builtin-exit", 1, 1)) {
				@Override
				public IrValue call(IrScope scope, ArrayList<IrValue> args) {
					if(args.get(0).getType() == IrValue.NUMBER) {
						IrNumber n = (IrNumber) args.get(0);
						System.exit((int)n.getValue());
					}
					return new IrNull(null);
				}
			});

			Timer.startSection("Ir Run");
			// Eval all top level expression once we have them all added
			for(Map.Entry<String, IrValue> kv : file.getSymbols().entrySet()) {
				IrValue v = kv.getValue();
				file.set(kv.getKey(), v.eval(file));
			}

			// Call main if it exists
			ArrayList<IrValue> mainArgs = new ArrayList<IrValue>();
			IrValue main = file.get("main");
			if(main == null) {
				throw new IrException(null, "Could not find the main function!");
			}
			IrValue returnValue = ((IrFunc)main).call(file, mainArgs);
			int returnCode = 0;
			if(returnValue.getType() == IrValue.NUMBER) {
				IrNumber n = (IrNumber) returnValue;
				returnCode = (int) n.getValue();
			}

			System.out.println("\n============================");
			Timer.printTimings();

			System.exit(returnCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
