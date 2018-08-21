package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;

public abstract class IrJavaFunc extends IrFunc {

	public IrJavaFunc(SourceLoc location) {
		super(IrFunc.JAVA_FUNCTION, location);
	}

}
