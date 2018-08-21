package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrValue;

public class IrIf extends IrStmt {

	private IrValue cond;
	private IrBlock block;
	private IrBlock elseBlock;

	public IrIf(IrValue cond, IrBlock block, IrBlock elseBlock, SourceLoc location) {
		super(IrStmt.IF, location);
		this.cond = cond;
		this.block = block;
		this.elseBlock = elseBlock;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		if(IrValue.isTrue(cond.eval(scope))) {
			return block.eval(scope);
		} else {
			if(elseBlock != null) {
				return block.eval(scope);
			} else {
				return new IrStmtResult(IrStmtResult.NORMAL, null);
			}
		}
	}
}
