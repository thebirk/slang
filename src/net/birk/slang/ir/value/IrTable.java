package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

import java.util.HashMap;

public class IrTable extends IrValue {

	private HashMap<IrValue, IrValue> map;

	public IrTable(HashMap<IrValue, IrValue> map, SourceLoc location) {
		super(IrValue.TABLE, location);
		this.map = map;
	}

	@Override
	public IrValue eval(IrScope scope) {
		return this;
	}

	public HashMap<IrValue, IrValue> getMap() {
		return map;
	}

}
