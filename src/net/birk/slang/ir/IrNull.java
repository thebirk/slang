package net.birk.slang.ir;

import net.birk.slang.SourceLoc;

public class IrNull extends IrValue {

	public IrNull(SourceLoc location) {
		super(IrValue.NULL, location);
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}
}
