package net.birk.slang.gfx;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class IrGfx {

	public static Window window = null;

	public static void addFunctions(IrScope scope) {
		HashMap<IrValue, IrValue> gfx = new HashMap<IrValue, IrValue>();

		gfx.put(new IrString("init", null), new IrJavaFunc(2, new SourceLoc("builtin-gfx-init", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				IrValue v1 = args.get(0);
				IrValue v2 = args.get(1);
				if(v1.getType() != Type.NUMBER && v2.getType() != Type.NUMBER) {
					throw new IrException(v1.getLocation(), "Wrong argument types passed, 'init' expect two numbers!");
				}

				IrNumber width = (IrNumber)v1;
				IrNumber height = (IrNumber)v2;

				window = new Window((int)width.getValue(), (int)height.getValue());

				return new IrBoolean(true, getLocation());
			}
		});

		gfx.put(new IrString("clear", null), new IrJavaFunc(3, new SourceLoc("builtin-gfx-clear", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				IrValue v1 = args.get(0);
				IrValue v2 = args.get(1);
				IrValue v3 = args.get(2);
				if(v1.getType() != Type.NUMBER && v2.getType() != Type.NUMBER && v3.getType() != Type.NUMBER) {
					throw new IrException(v1.getLocation(), "Wrong argument types passed, 'clear' expect three numbers!");
				}

				IrNumber r = (IrNumber)v1;
				IrNumber g = (IrNumber)v2;
				IrNumber b = (IrNumber)v3;
				Color c = new Color((int)r.getValue(), (int)g.getValue(), (int)b.getValue());
				window.clear(c.getRGB());

				return new IrBoolean(true, getLocation());
			}
		});

		gfx.put(new IrString("present", null), new IrJavaFunc(0, new SourceLoc("builtin-gfx-present", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				window.present();
				return new IrNull(getLocation());
			}
		});

		gfx.put(new IrString("shouldClose", null), new IrJavaFunc(0, new SourceLoc("builtin-gfx-present", 1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				return new IrBoolean(window.shouldClose(), getLocation());
			}
		});

		gfx.put(new IrString("fillRect", null), new IrJavaFunc(7, new SourceLoc("builtin-gfx-fillrect",1, 1)) {
			@Override
			public IrValue javaCall(IrScope scope, ArrayList<IrValue> args) {
				IrValue v1 = args.get(0);
				IrValue v2 = args.get(1);
				IrValue v3 = args.get(2);
				IrValue v4 = args.get(3);
				IrValue v5 = args.get(4);
				IrValue v6 = args.get(5);
				IrValue v7 = args.get(6);
				if(v1.getType() != Type.NUMBER && v2.getType() != Type.NUMBER && v3.getType() != Type.NUMBER &&
						v4.getType() != Type.NUMBER && v5.getType() != Type.NUMBER && v6.getType() != Type.NUMBER && v7.getType() != Type.NUMBER) {
					throw new IrException(v1.getLocation(), "Wrong argument types passed, 'fillRect' expect seven numbers!");
				}

				IrNumber x = (IrNumber)v1;
				IrNumber y = (IrNumber)v2;
				IrNumber w = (IrNumber)v3;
				IrNumber h = (IrNumber)v4;

				IrNumber r = (IrNumber)v5;
				IrNumber g = (IrNumber)v6;
				IrNumber b = (IrNumber)v7;

				window.fillRect((int)x.getValue(), (int)y.getValue(), (int)w.getValue(), (int)h.getValue(), (int)r.getValue(), (int)g.getValue(), (int)b.getValue());

				return new IrNull(getLocation());
			}
		});

		IrTable gfxTable = new IrTable(gfx, new SourceLoc("builtin-gfx", 1, 1));
		scope.add("gfx", gfxTable);
	}

}
