package net.birk.slang.ir;

import net.birk.slang.SourceLoc;
import net.birk.slang.Token;
import net.birk.slang.ir.value.*;

import java.util.ArrayList;
import java.util.Map;

public class IrCore {

	public static String stringifyValue(IrValue v) {
		if(v == null) throw new IrException(null, "Internal compiler error! We should never get null here!");
		switch (v.getType()) {
			case STRING: {
				IrString str = (IrString) v;
				return str.getValue();
			}
			case IDENT: {
				IrIdent ident = (IrIdent) v;
				return ident.getName();
			}
			case NUMBER: {
				IrNumber n = (IrNumber) v;
				//return "" + n.getValue();
				double d = n.getValue();
				if(d == (long)d) {
					return String.format("%d", (long)d);
				} else {
					return String.format("%s", n.getValue());
				}
			}
			case NULL: {
				return "null";
			}
			case BOOLEAN: {
				IrBoolean b = (IrBoolean) v;
				return "" + b.getValue();
			}
			case FUNC: {
				throw new RuntimeException("Unimplemented!");
			} //break;
			case TABLE: {
				IrTable table = (IrTable) v;
				//TODO: Print it all
				StringBuilder builder = new StringBuilder();
				builder.append("{ ");

				boolean prefix = false;
				for(Map.Entry<IrValue, IrValue> e : table.getMap().entrySet()) {
					if(prefix) {
						builder.append(", [" + stringifyValue(e.getKey()) + "] = " + stringifyValue(e.getValue()));
					} else {
						prefix = true;
						builder.append("[" + stringifyValue(e.getKey()) + "] = " + stringifyValue(e.getValue()));
					}
				}
				builder.append(" }");

				return builder.toString();
			}
			case ARRAY: {
				StringBuilder builder = new StringBuilder();
				builder.append("[ ");
				IrArray irArray = (IrArray) v;
				boolean prefix = false;
				for(IrValue value : irArray.getItems()) {
					if(prefix) {
						builder.append(", " + stringifyValue(value));
					} else {
						prefix = true;
						builder.append(stringifyValue(value));
					}
				}

				builder.append(" ]");
				return builder.toString();
			}
			case USERDATA: {
				IrUserdata d = (IrUserdata) v;
				return "userdata#" + d.getUserType();
			}
			default: {
				return "<Type not supported by println. pls fix.> Type: " + v.getType();
			}
		}
	}

	public static void addFunctions(IrScope global) {
		//NOTE: Is this correct?
		global.add("eps", new IrNumber(Math.ulp(1.0), new SourceLoc("builtin-eps", 1, 1)));
		global.add("pi", new IrNumber(Math.PI, new SourceLoc("builtin-eps", 1, 1)));

		//TODO: contains, check if table has key

		//TODO: Bitwise utils
		// - bor, band, bxor, bnot, shl, shr
		// - hex literals    '0x'
		// - binary literals '0b'


		//TODO: Array utilities
		// - Check if contains value
		// - First index of a value
		// - All indices of a value

		global.add("int", new IrJavaFunc(1, new SourceLoc("builtin-int", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				IrValue v = args.get(0);
				if(v.getType() == Type.NUMBER) {
					IrNumber n = (IrNumber) v;
					return new IrNumber((long)n.getValue(), null);
				} else {
					return new IrNull(null);
				}
			}
		});

		global.add("println", new IrJavaFunc(-1, new SourceLoc("builtin-println", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				for(IrValue v : args) {
					System.out.print(stringifyValue(v));
				}
				System.out.println();
				return new IrNull(getLocation());
			}
		});

		global.add("exit", new IrJavaFunc(1, new SourceLoc("builtin-exit", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				if(args.get(0).getType() == IrValue.Type.NUMBER) {
					IrNumber n = (IrNumber) args.get(0);
					System.exit((int)n.getValue());
				}
				return new IrNull(null);
			}
		});

		global.add("len", new IrJavaFunc(1, new SourceLoc("builtin-len", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				//NOTE: Do we crash or just return null if we dont get what we want?
				IrValue v = args.get(0);
				if(v.getType() == IrValue.Type.ARRAY) {
					IrArray array = (IrArray) v;
					return new IrNumber(array.getItems().size(), v.getLocation());
				} else if(v.getType() == IrValue.Type.STRING) {
					IrString str = (IrString) v;
					return new IrNumber(str.getValue().length(), v.getLocation());
				}
				else if (v.getType() == IrValue.Type.TABLE) {
					IrTable table = (IrTable) v;
					return new IrNumber(table.getMap().size(), v.getLocation());
				}
				else {
					return new IrNumber(0, null);
				}
			}
		});

		global.add("assert", new IrJavaFunc(1, new SourceLoc("builtin-assert", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				if(!IrValue.isTrue(args.get(0))) {
					throw new IrException(getLocation(), "Assertion failed!");
				}
				return new IrBoolean(true, null);
			}
		});

		global.add("type", new IrJavaFunc(1, new SourceLoc("builtin-type", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				// We could have this return an array if we get more than one argument

				IrValue v = args.get(0);
				String result = "";
				switch (v.getType()) {
					case NUMBER: result = "number"; break;
					case STRING: result = "string"; break;
					case NULL: result = "null"; break;
					case BOOLEAN: result = "boolean"; break;
					case USERDATA: {
						IrUserdata d = (IrUserdata) v;
						result = "userdata#" + d.getUserType();
						break;
					}
					case FUNC: result = "func"; break;
					case ARRAY: result = "array"; break;
					case TABLE: result = "table"; break;
					default: {
						throw new RuntimeException("Internal compiler error! This value type should never ever be passed as a value! Type: " + v.getType());
					}
				}

				return new IrString(result, null);
			}
		});

		global.add("range", new IrJavaFunc(1, new SourceLoc("builtin-range", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				IrValue arg = args.get(0);
				if(arg.getType() != Type.NUMBER) {
					throw new IrException(getLocation(), "range expects a number argument!");
				}
				IrNumber n = (IrNumber) arg;
				ArrayList<IrValue> items = new ArrayList<IrValue>((int)n.getValue());
				for(int i = 0; i < (int)n.getValue(); i++) {
					items.add(new IrNumber(i, getLocation()));
				}
				return new IrArray(items, getLocation());
			}
		});
	}

}
