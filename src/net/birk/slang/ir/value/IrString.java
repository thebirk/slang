package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public class IrString extends IrValue {

	private String value;

	public IrString(String value, SourceLoc location) {
		super(IrValue.STRING, location);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}
}
