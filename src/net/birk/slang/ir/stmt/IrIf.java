package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrValue;

public class IrIf extends IrStmt {

	private IrValue cond;
	private IrBlock block;
	private IrStmt _else;

	public IrIf(IrValue cond, IrBlock block, IrStmt _else, SourceLoc location) {
		super(IrStmt.Type.IF, location);
		this.cond = cond;
		this.block = block;
		this._else = _else;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		if(IrValue.isTrue(cond.eval(scope))) {
			return block.eval(scope);
		} else {
			if(_else != null) {
				return _else.eval(scope);
			} else {
				return new IrStmtResult(IrStmtResult.NORMAL, null);
			}
		}
	}
}
