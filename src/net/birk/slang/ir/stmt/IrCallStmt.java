package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrFunc;
import net.birk.slang.ir.value.IrValue;

import java.util.ArrayList;

public class IrCallStmt extends IrStmt {

	private IrValue func;
	private ArrayList<IrValue> args;

	public IrCallStmt(IrValue func, ArrayList<IrValue> args, SourceLoc location) {
		super(IrStmt.CALL, location);
		this.func = func;
		this.args = args;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		ArrayList<IrValue> finalArgs = new ArrayList<IrValue>();
		for(IrValue v : args) {
			finalArgs.add(v.eval(scope));
		}
		func = func.eval(scope);
		if(func.getType() != IrValue.FUNC) {
			throw new IrException(getLocation(), "Trying to call a non function value!");
		}
		((IrFunc)func).call(scope, finalArgs);
		return new IrStmtResult(IrStmtResult.NORMAL, null);
	}
}
