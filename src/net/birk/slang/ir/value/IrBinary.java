package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public class IrBinary extends IrValue {

	private int op;
	private IrValue lhs;
	private IrValue rhs;

	public IrBinary(int op, IrValue lhs, IrValue rhs, SourceLoc location) {
		super(IrValue.Type.BINARY, location);
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return IrValue.doBinary(op, lhs.eval(scope), rhs.eval(scope));
	}

	@Override
	public boolean isEqual(IrValue other) {
		throw new RuntimeException("Cannot compare IrBinary");
	}

	@Override
	public int hash() {
		throw new RuntimeException("Internal compiler error: Cannot hash an IrBinary!");
	}


}
