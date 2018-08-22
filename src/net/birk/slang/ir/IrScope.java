package net.birk.slang.ir;

import net.birk.slang.ir.value.IrFunc;
import net.birk.slang.ir.value.IrNull;
import net.birk.slang.ir.value.IrValue;

import java.util.ArrayList;
import java.util.HashMap;

public class IrScope {

	private IrScope parent;
	private HashMap<String, IrValue> symbols;

	public IrScope(IrScope parent) {
		this.parent = parent;
		symbols = new HashMap<String, IrValue>();
	}

	public IrValue callFunction(String name, ArrayList<IrValue> args) {
		IrValue v = get(name);
		if(v == null) {
			throw new IrException(null, "Trying to call non-existing function '" + name + "'!");
		}
		IrFunc f = (IrFunc) v;
		return f.call(this, args);
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
	}

	public HashMap<String, IrValue> getSymbols() {
		return symbols;
	}

}
