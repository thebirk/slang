package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public class IrFunc extends IrValue {

	public static final int SLANG_FUNCTION = 0;
	public static final int JAVA_FUNCTION = 1;

	private int functionType;

	public IrFunc(int functionType, SourceLoc location) {
		super(IrValue.FUNC, location);
		this.functionType = functionType;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}
}
