package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public class IrString extends IrValue {

	private String value;

	public IrString(String value, SourceLoc location) {
		super(IrValue.Type.STRING, location);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}

	@Override
	public boolean isEqual(IrValue other) {
		if(other.getType() == IrValue.Type.IDENT) {
			IrIdent i = (IrIdent) other;
			return i.getName().equals(value);
		} else if(other.getType() == IrValue.Type.STRING) {
			IrString s = (IrString ) other;
			return s.getValue().equals(value);
		} else {
			return false;
		}
	}

	@Override
	public int hash() {
		return value.hashCode();
	}

}
