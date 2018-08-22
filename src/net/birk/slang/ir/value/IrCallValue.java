package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;

import java.util.ArrayList;

public class IrCallValue extends IrValue {

	private IrValue func;
	private ArrayList<IrValue> args;

	public IrCallValue(IrValue func, ArrayList<IrValue> args, SourceLoc location) {
		super(IrValue.CALL, location);
		this.func = func;
		this.args = args;
	}

	@Override
	public IrValue eval(IrScope scope) {
		ArrayList<IrValue> finalArgs = new ArrayList<IrValue>();
		for(IrValue v : args) {
			finalArgs.add(v.eval(scope));
		}
		func = func.eval(scope);
		if(func.getType() != IrValue.FUNC) {
			throw new IrException(getLocation(), "Trying to call a non function value!");
		}
		return ((IrFunc)func).call(scope, finalArgs);
	}

	@Override
	public boolean isEqual(IrValue other) {
		throw new RuntimeException("Cannot compare IrCallValue!");
	}

	@Override
	public int hash() {
		throw new RuntimeException("Internal compiler error! Cannont hash IrCallValue");
	}
}
