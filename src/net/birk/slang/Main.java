package net.birk.slang;

import net.birk.slang.ir.IrCore;
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
	 *  - Add for
	 *  - IrJavaFunc:
	 *   - Arguments that take the arg count
	 *   - Flag to set varargs
	 *  - IrSlangFunc:
	 *   - Varargs flags
	 *    - only varargs if last param looks like this "*ident", turns into array
	 *    - Pass all args as array
	 *  - Add userdata type (remember to implement the userdata tag)
	 *  - 'import' keyword
	 *   - 'use' keyword for builtins? or just a simple switch on the import name?
	 *  - Better "Expected operand" error.
	 *
	 * TODONE:
	 *  + Add array type []
	 *  + Add len() function
	 *  + Add table type {}
	 *  + Add while
	 *  + Add anon func
	 *
	 * MAYBEDO:
	 *   - A seperate Table like structure for importing other files(aka. a Module) biggest difference would be
	 *     readonly variables
	 *      - Or we could extand an IrTable and just provide some extra info like module name etc, or is this
	 *        covered by SourceLoc?
	 *   - Python like **kwargs for tables?
	 *   - Argument list type hint
	 *    - Return type as well?
	 *    - ex.
	 *      fn add(a: Number, b: Number): Number {
	 *      	return a + b;
	 *      }
	 *   - Tail calls? Is it even possible for us?
	 *    - We only really need to keep the same Scope, otherwise we nest deep
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
			IrScope fileScope = new IrScope(global);
			for(Node n : nodes) {
				switch (n.getType()) {
					case VAR: {
						NodeVar var = (NodeVar) n;
						if(!fileScope.add(var.getName(), IrValue.generateExpr(var.getExpr()))) {
							throw new IrException(var.getLocation(), "Symbol '" + var.getName() + "' already exists!");
						}
					} break;
					case FUNC: {
						NodeFunc f = (NodeFunc) n;
						if(!fileScope.add(f.getIdent().getName(), IrValue.generateExpr(n))) {
							throw new IrException(f.getLocation(), "Symbol '" + f.getIdent().getName() + "' already exists!");
						}
					} break;
					default: {
						//TODO: Better error: Cant have 'n' at the file scope
						System.err.println("compiler error!");
						assert false;
						throw new RuntimeException("compiler error!");
					}
				}
			}

			IrCore.addFunctions(global);

			Timer.startSection("Ir Run");
			// Eval all top level expression once we have them all added
			for(Map.Entry<String, IrValue> kv : fileScope.getSymbols().entrySet()) {
				IrValue v = kv.getValue();
				fileScope.set(kv.getKey(), v.eval(fileScope));
			}

			// Call main if it exists

			ArrayList<IrValue> mainArgs = new ArrayList<IrValue>();
			IrValue returnValue = fileScope.callFunction("main", mainArgs);
			int returnCode = 0;
			if(returnValue.getType() == IrValue.Type.NUMBER) {
				IrNumber n = (IrNumber) returnValue;
				returnCode = (int) n.getValue();
			}
			/*
			IrValue main = fileScope.get("main");
			if(main == null) {
				throw new IrException(null, "Could not find the main function!");
			}
			IrValue returnValue = ((IrFunc)main).call(fileScope, mainArgs);
			int returnCode = 0;
			if(returnValue.getType() == IrValue.NUMBER) {
				IrNumber n = (IrNumber) returnValue;
				returnCode = (int) n.getValue();
			}*/

			System.out.println("\n============================");
			Timer.printTimings();

			System.exit(returnCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
