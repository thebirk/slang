package net.birk.slang.ir.value;

import java.util.ArrayList;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;

public class IrSelfCall extends IrValue {

    private IrValue expr;
    private IrValue ident;
    private ArrayList<IrValue> args;

	public IrSelfCall(IrValue expr, IrValue ident, ArrayList<IrValue> args, SourceLoc location) {
		super(Type.SELFCALL, location);
		this.expr = expr;
        this.ident = ident;
        this.args = args;
	}

	@Override
	public IrValue eval(IrScope scope) {
		IrValue expr = this.expr.eval(scope);
		if(expr.getType() != Type.TABLE) {
			throw new IrException(getLocation(), "Cannot use ':' operator with non-table values!");
        }

        IrTable t = (IrTable) expr;
        IrValue func = t.getMap().get(ident);

        if(func.getType() != Type.FUNC) {
            throw new IrException(getLocation(), "Right hand sid of ':' is not a function!!");
        }
        
        ArrayList<IrValue> finalArgs = new ArrayList<IrValue>();
        finalArgs.add(expr);
		for(IrValue v : args) {
			finalArgs.add(v.eval(scope));
		}
        func = func.eval(scope);
        
		return ((IrFunc)func).call(scope, finalArgs);
	}

	@Override
	public int hash() {
		throw new IrException(getLocation(), "Internal compiler error! Cannot hash IrField!");
	}

	@Override
	public boolean isEqual(IrValue other) {
		throw new IrException(getLocation(), "Internal compiler error! Cannot compare IrField!");
	}
}
