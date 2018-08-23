package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrValue;

public class IrVar extends IrStmt {

	private String name;
	private IrValue value;

	public IrVar(String name, IrValue value, SourceLoc location) {
		super(IrStmt.Type.VAR, location);
		this.name = name;
		this.value = value;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		if(!scope.add(name, value.eval(scope))) {
			throw new IrException(getLocation(), "Symbol '" + name + "' already exists!");
		}
		return new IrStmtResult(IrStmtResult.NORMAL, null);
	}
}
