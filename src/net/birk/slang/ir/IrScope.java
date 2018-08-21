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
		if(symbols.containsKey(name)) {
			symbols.put(name, value);
			return true;
		} else {
			if(parent != null) {
				return parent.set(name, value);
			} else {
				return false;
			}
		}
	}

	public IrValue get(String name) {
		if(symbols.containsKey(name)) {
			return  symbols.get(name);
		} else {
			if(parent != null) {
				return parent.get(name);
			} else {
				return null;
			}
		}
/*
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
		*/
	}

	public HashMap<String, IrValue> getSymbols() {
		return symbols;
	}
}
