package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;

import java.util.ArrayList;

public abstract class IrJavaFunc extends IrFunc {

	// argCount < 0 -> varargs
	private int argcCount;

	public IrJavaFunc(int argCount, SourceLoc location) {
		super(IrFunc.JAVA_FUNCTION, location);
		this.argcCount = argCount;
	}

	public abstract IrValue javaCall(IrScope scope, ArrayList<IrValue> args);

	@Override
	public IrValue call(IrScope scope, ArrayList<IrValue> args) {
		if(argcCount >= 0 && args.size() != argcCount) {
			throw new IrException(getLocation(), "Argument count mismatch! Wanted " + argcCount + " got " + args.size());
		}
		return javaCall(scope, args);
	}
}
