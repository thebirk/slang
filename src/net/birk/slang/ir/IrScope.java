package net.birk.slang.ir;

import net.birk.slang.ir.value.IrValue;

import java.util.HashMap;

public class IrScope {

	private IrScope parent;
	private HashMap<String, IrValue> symbols;

	public IrScope(IrScope parent) {
		this.parent = parent;
		symbols = new HashMap<String, IrValue>();
	}

	public boolean add(String name, IrValue value) {
		if(symbols.containsKey(name)) return false;
		symbols.put(name, value);
		return true;
	}

	public boolean set(String name, IrValue value) {
		IrValue result = symbols.getOrDefault(name, null);
		if(result == null) {
			if(parent != null) {
				parent.set(name, value);
			} else {
				return false;
			}
		} else {
			symbols.put(name, value);
		}

		return true;
	}

	public IrValue get(String name) {
		IrValue result = symbols.getOrDefault(name, null);
		if(result == null) {
			if(parent != null) {
				return parent.get(name);
			} else {
				return null;
			}
		} else {
			return result;
		}
	}

}
