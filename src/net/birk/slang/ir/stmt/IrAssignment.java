package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.Token;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrIdent;
import net.birk.slang.ir.value.IrValue;

public class IrAssignment extends IrStmt {

	private int op;
	private IrValue lhs;
	private IrValue rhs;

	public IrAssignment(int op, IrValue lhs, IrValue rhs, SourceLoc location) {
		super(IrStmt.ASSIGNMENT, location);
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		if(lhs.getType() != IrValue.IDENT) {
			lhs = lhs.eval(scope);
		}
		if(lhs.getType() != IrValue.IDENT) {
			throw new IrException(lhs.getLocation(), "Left hand side of assignment cannot be assigned to!");
		}

		IrValue result = rhs.eval(scope);
		switch (op) {
			case '=': break;
			case Token.PLUS_EQUALS: {
				result = IrValue.doBinary('+', lhs.eval(scope), result);
			} break;
			case Token.MINUS_EQUALS: {
				result = IrValue.doBinary('-', lhs.eval(scope), result);
			} break;

			default: {
				throw new IrException(getLocation(), "Internal compiler error!");
			}
		}

		IrIdent ident = (IrIdent) lhs;
		if(!scope.set(ident.getName(), result)) {
			throw new IrException(lhs.getLocation(), "Symbol '" + ident.getName() + "' does not exist!");
		}

		return new IrStmtResult(IrStmtResult.NORMAL, null);
	}
}
