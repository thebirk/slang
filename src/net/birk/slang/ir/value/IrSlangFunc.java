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
		//TODO: When we have varargs check that we meet the minium?
		//      Is the varargs argument required? The coolest would be no, and just fill the arg with an empty arrray or null
		// ex:
		//   fn test(fmt, *args) ->
		//     fmt - required
		//     *args - optional (null or empty array) if not given
		if(argNames.size() != args.size()) {
			throw new IrException(getLocation(), "Argument count mismatch! Wanted " + argNames.size() + " got " + args.size());
		}

		IrScope func = scope;
		//TODO: Varargs? Does this need to be args then?
		if(argNames.size() > 0) {
			//NOTE: If we dont have any arguments we dont need the Scope
			//TODO: If we are a vararegs function we might need it based on args
			func = new IrScope(scope);
			for (int i = 0; i < args.size(); i++) {
				func.add(argNames.get(i), args.get(i));
			}
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
