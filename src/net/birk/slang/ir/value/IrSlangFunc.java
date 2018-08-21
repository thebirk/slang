package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.stmt.IrBlock;
import net.birk.slang.ir.stmt.IrStmtResult;

import java.util.ArrayList;

public class IrSlangFunc extends IrFunc {

	private IrBlock block;
	private ArrayList<String> argNames;

	public IrSlangFunc(IrBlock block, ArrayList<String> argNames, SourceLoc location) {
		super(IrFunc.SLANG_FUNCTION, location);
		this.block = block;
		this.argNames = argNames;
	}

	@Override
	public IrValue call(IrScope scope, ArrayList<IrValue> args) {
		if(argNames.size() != args.size()) {
			throw new IrException(getLocation(), "Argument count mismatch! Wanted " + argNames.size() + " got " + args.size());
		}
		IrScope func = new IrScope(scope);
		for(int i = 0; i < args.size(); i++) {
			func.add(argNames.get(i), args.get(i));
		}

		IrStmtResult result = block.eval(func);

		//TODO: This location may be very confusing, but do we really use IrValue locations?
		IrValue resultValue = result.getValue();
		if(resultValue == null) {
			resultValue = new IrNull(getLocation());
		}
		return resultValue;
	}
}
