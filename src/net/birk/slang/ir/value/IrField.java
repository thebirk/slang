package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;

public class IrField extends IrValue {

	private IrValue ident;
	private IrValue expr;

	public IrField(IrValue ident, IrValue expr, SourceLoc location) {
		super(Type.FIELD, location);
		this.ident = ident;
		this.expr = expr;
	}

	@Override
	public IrValue eval(IrScope scope) {
		IrValue expr = this.expr.eval(scope);
		if(expr.getType() != Type.TABLE) {
			throw new IrException(getLocation(), "Cannot use '.' operator with non-table values!");
		}
		IrTable t = (IrTable) expr;
		return t.getMap().get(ident);
	}

	@Override
	public int hash() {
		throw new IrException(getLocation(), "Internal compiler error! Cannot hash IrField!");
	}

	@Override
	public boolean isEqual(IrValue other) {
		throw new IrException(getLocation(), "Internal compiler error! Cannot compare IrField!");
	}

	public IrValue getIdent() {
		return ident;
	}

	public IrValue getExpr() {
		return expr;
	}

}
