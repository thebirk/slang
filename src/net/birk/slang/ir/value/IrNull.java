package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public class IrNull extends IrValue {

	public IrNull(SourceLoc location) {
		super(IrValue.NULL, location);
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}
}
