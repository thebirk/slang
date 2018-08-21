package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

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
		return IrValue.add(op, lhs.eval(scope), rhs.eval(scope));
	}
}
