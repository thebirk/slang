package net.birk.slang.ir;

import java.util.HashMap;

public class IrScope {

	private IrScope parent;
	private HashMap<String, IrValue> symbols;

	public IrScope(IrScope parent) {
		this.parent = parent;
		symbols = new HashMap<String, IrValue>();
	}

	public void set(String name, IrValue value) {
		symbols.put(name, value);
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
