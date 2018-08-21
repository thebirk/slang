package net.birk.slang.ir;

import net.birk.slang.SourceLoc;

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
		if(value != null) {
			System.err.println("Symbol '" + name + "' does not exist!");
			System.exit(1);
			return null;
		} else {
			return value;
		}
	}

}
