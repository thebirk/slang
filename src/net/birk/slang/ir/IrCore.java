package net.birk.slang.ir;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.value.*;

import java.util.ArrayList;
import java.util.Map;

public class IrCore {

	private static String stringifyValue(IrValue v) {
		if(v == null) throw new IrException(null, "Internal compiler error! We should never get null here!");
		switch (v.getType()) {
			case IrValue.STRING: {
				IrString str = (IrString) v;
				return str.getValue();
			}
			case IrValue.NUMBER: {
				IrNumber n = (IrNumber) v;
				return "" + n.getValue();
			}
			case IrValue.NULL: {
				return "null";
			}
			case IrValue.BOOLEAN: {
				IrBoolean b = (IrBoolean) v;
				return "" + b.getValue();
			}
			case IrValue.FUNC: {
				throw new RuntimeException("Unimplemented!");
			} //break;
			case IrValue.TABLE: {
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
			case IrValue.ARRAY: {
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
			default: {
				return "<Type not supported by println. pls fix.>";
			}
		}
	}

	public static void addFunctions(IrScope global) {
		//NOTE: Is this correct?
		global.add("eps", new IrNumber(Math.ulp(1.0), new SourceLoc("builtin-eps", 1, 1)));
		global.add("pi", new IrNumber(Math.PI, new SourceLoc("builtin-eps", 1, 1)));

		//TODO: contains, check if table has key

		//TODO: Array utilities
		// - Check if contains value
		// - First index of a value
		// - All indices of a value

		global.add("println", new IrJavaFunc(new SourceLoc("builtin-println", 1, 1)) {
			@Override
			public IrValue call(IrScope scope, ArrayList<IrValue> args) {
				for(IrValue v : args) {
					System.out.print(stringifyValue(v));
				}
				System.out.println();
				return new IrNull(getLocation());
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

		global.add("len", new IrJavaFunc(new SourceLoc("builtin-len", 1, 1)) {
			@Override
			public IrValue call(IrScope scope, ArrayList<IrValue> args) {
				//NOTE: Do we crash or just return null if we dont get what we want?
				IrValue v = args.get(0);
				if(v.getType() == IrValue.ARRAY) {
					IrArray array = (IrArray) v;
					return new IrNumber(array.getItems().size(), v.getLocation());
				} else if(v.getType() == IrValue.STRING) {
					IrString str = (IrString) v;
					return new IrNumber(str.getValue().length(), v.getLocation());
				}
				else if (v.getType() == IrValue.TABLE) {
					IrTable table = (IrTable) v;
					return new IrNumber(table.getMap().size(), v.getLocation());
				}
				else {
					return new IrNull(null);
				}
			}
		});

		//TODO: Add assert
	}

}
