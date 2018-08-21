package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public class IrUnary extends IrValue {

	private int op;
	private IrValue expr;

	public IrUnary(int op, IrValue expr, SourceLoc location) {
		super(IrValue.UNARY, location);
		this.op = op;
		this.expr = expr;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return IrValue.unary(op, expr.eval(scope));
	}
}