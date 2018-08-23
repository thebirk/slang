package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public class IrBoolean extends IrValue {

	private boolean value;

	public IrBoolean(boolean value, SourceLoc location) {
		super(IrValue.BOOLEAN, location);
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}

	@Override
	public boolean isEqual(IrValue other) {
		if(other.getType() == IrValue.BOOLEAN) {
			IrBoolean b = (IrBoolean) other;
			return b.getValue() == value;
		} else {
			return false;
		}
	}

	@Override
	public int hash() {
		return Boolean.hashCode(value);
	}

}
