package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrIdent;
import net.birk.slang.ir.value.IrValue;

public class IrAssignment extends IrStmt {

	private IrValue lhs;
	private IrValue rhs;

	public IrAssignment(IrValue lhs, IrValue rhs, SourceLoc location) {
		super(IrStmt.ASSIGNMENT, location);
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		lhs = lhs.eval(scope);
		if(lhs.getType() != IrValue.IDENT) {
			throw new IrException(lhs.getLocation(), "Left hand side of assignment cannot be assigned to!");
		}

		IrIdent ident = (IrIdent) lhs;
		if(!scope.set(ident.getName(), rhs.eval(scope))) {
			throw new IrException(getLocation(), "Symbol '" + ident.getName() + "' does not exist!");
		}

		return new IrStmtResult(IrStmtResult.NORMAL, null);
	}
}
