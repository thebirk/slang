package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.stmt.IrStmtResult;

import java.util.ArrayList;

public class IrSlangFunc extends IrFunc {

	public IrSlangFunc(SourceLoc location) {
		super(IrFunc.SLANG_FUNCTION, location);
	}

	@Override
	public IrValue call(ArrayList<IrValue> args) {
		throw new RuntimeException("Unimplemented");
	}
}
