package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;

public abstract class IrUserdata extends IrValue {

	private int userType;

	public IrUserdata(int userType, SourceLoc location) {
		super(Type.USERDATA, location);
		this.userType = userType;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}

	@Override
	public int hash() {
		throw new IrException(getLocation(), "Cannot hash a value of type USERDATA");
	}

	@Override
	public boolean isEqual(IrValue other) {
		throw new IrException(getLocation(), "Cannot compare a value of type USERDATA");
	}

}
