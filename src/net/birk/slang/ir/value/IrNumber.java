package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public class IrNumber extends IrValue {

	private double value;

	public IrNumber(double value, SourceLoc location) {
		super(IrValue.Type.NUMBER, location);
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}

	@Override
	public boolean isEqual(IrValue other) {
		if(other.getType() == IrValue.Type.NUMBER) {
			IrNumber n = (IrNumber) other;
			return n.getValue() == value;
		} else {
			return false;
		}
	}

	@Override
	public int hash() {
		return Double.hashCode(value);
	}

}
