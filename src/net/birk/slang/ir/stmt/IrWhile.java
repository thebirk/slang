package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrValue;

public class IrWhile extends IrStmt {

	private IrValue cond;
	private IrBlock block;

	public IrWhile(IrValue cond, IrBlock block, SourceLoc location) {
		super(IrStmt.Type.WHILE, location);
		this.cond = cond;
		this.block = block;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		IrStmtResult result = new IrStmtResult(IrStmtResult.NORMAL, null);
		while(IrValue.isTrue(cond.eval(scope))) {
			result = block.eval(scope);
		}
		return result;
	}

}
