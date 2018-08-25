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
		while(IrValue.isTrue(cond.eval(scope))) {
			IrStmtResult result = block.eval(scope);
			if(result.getStatus() == IrStmtResult.RETURN) {
				return result;
			}
			// else if(result.getStatus() == IrStmtResult.BREAK)
		}
		return new IrStmtResult(IrStmtResult.NORMAL, null);
	}

}
