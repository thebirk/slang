package net.birk.slang.ir;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.value.*;

import java.util.ArrayList;

public class IrCore {

	public static void addFunctions(IrScope global) {
		//NOTE: Is this correct?
		global.add("eps", new IrNumber(Math.ulp(1.0), new SourceLoc("builtin-eps", 1, 1)));
		global.add("pi", new IrNumber(Math.PI, new SourceLoc("builtin-eps", 1, 1)));

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
				// else if (v.getType() == IrValue.TABLE) {}
				else {
					return new IrNull(null);
				}
			}
		});

		//TODO: Add assert
	}

}
