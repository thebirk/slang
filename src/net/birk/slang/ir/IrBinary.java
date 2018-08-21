package net.birk.slang.ir;

import net.birk.slang.SourceLoc;

public class IrBinary extends IrValue {

	private int op;
	private IrValue lhs;
	private IrValue rhs;

	public IrBinary(int op, IrValue lhs, IrValue rhs, SourceLoc location) {
		super(IrValue.BINARY, location);
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return IrValue.add(lhs.eval(scope), rhs.eval(scope));
	}
}
