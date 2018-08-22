package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;

public class IrIdent extends IrValue {

	private String name;

	public IrIdent(String name, SourceLoc location) {
		super(IrValue.IDENT, location);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public IrValue eval(IrScope scope) {
		IrValue value = scope.get(name);
		if(value == null) {
			throw new IrException(getLocation(), "Symbol '" + name + "' does not exist!");
		} else {
			return value;
		}
	}

	@Override
	public boolean isEqual(IrValue other) {
		if(other.getType() == IrValue.IDENT) {
			IrIdent i = (IrIdent) other;
			return i.getName().equals(name);
		} else if(other.getType() == IrValue.STRING) {
			IrString s = (IrString ) other;
			return s.getValue().equals(name);
		} else {
			return false;
		}
	}

	@Override
	public int hash() {
		return name.hashCode();
	}
}
