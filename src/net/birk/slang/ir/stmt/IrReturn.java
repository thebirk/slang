package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrValue;

public class IrReturn extends IrStmt {

	private IrValue value;

	public IrReturn(IrValue value, SourceLoc location) {
		super(IrStmt.Type.RETURN, location);
		this.value = value;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		if(value == null) {
			return new IrStmtResult(IrStmtResult.RETURN, null);
		} else {
			return new IrStmtResult(IrStmtResult.RETURN, value.eval(scope));
		}

	}

}
