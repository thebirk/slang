package net.birk.slang.ir;

import net.birk.slang.SourceLoc;

public class IrFunc extends IrValue {

	public IrFunc(SourceLoc location) {
		super(IrValue.FUNC, location);
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}
}
