package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

import java.util.ArrayList;

public class IrArray extends IrValue {

	//TODO: Helper functions that do array extending and bounds checking on read

	private ArrayList<IrValue> items;

	public IrArray(ArrayList<IrValue> items, SourceLoc location) {
		super(IrValue.Type.ARRAY, location);
		this.items = items;
	}

	@Override
	public IrValue eval(IrScope scope) {
		//TODO: Do we need to evaluate the items here, because we might have TABLE_LITERAL.
		// I will do this for now, but evaulating every time seems a little expensive
		for(int i = 0; i < items.size(); i++) {
			items.set(i, items.get(i).eval(scope));
		}
		return this;
	}

	@Override
	public boolean isEqual(IrValue other) {
		throw new RuntimeException("Incomplete!");
	}

	@Override
	public int hash() {
		throw new RuntimeException("Internal compiler error! Cannot hash IrArray!");
	}

	public ArrayList<IrValue> getItems() {
		return items;
	}
}
