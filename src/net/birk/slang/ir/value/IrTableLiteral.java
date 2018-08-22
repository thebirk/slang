package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;
import net.birk.slang.nodes.NodeArrayLiteral;
import net.birk.slang.nodes.NodeTableLiteral;

import java.util.ArrayList;
import java.util.HashMap;

public class IrTableLiteral extends IrValue {

	private ArrayList<NodeTableLiteral.Entry> entries;

	public IrTableLiteral(ArrayList<NodeTableLiteral.Entry> entries, SourceLoc location) {
		super(IrValue.TABLE_LITERAL, location);
		this.entries = entries;
	}

	@Override
	public IrValue eval(IrScope scope) {
		//TODO: Eval this to an IrTable
		HashMap<IrValue, IrValue> map = new HashMap<IrValue, IrValue>();
		for(NodeTableLiteral.Entry e : entries) {
			if (e.getType() == NodeTableLiteral.INDEX_ENTRY) {
				map.put(generateExpr(e.getIdentOrIndex()).eval(scope), generateExpr(e.getExpr()).eval(scope));
			} else if(e.getType() == NodeTableLiteral.IDENT_ENTRY) {
				IrValue ident = generateExpr(e.getIdentOrIndex());
				if(ident.getType() != IrValue.IDENT) throw new RuntimeException("Internal compiler error! This entry is lying about its index type!");
				map.put(ident, generateExpr(e.getExpr()).eval(scope));
			} else {
				throw new RuntimeException("Internal compiler error! Invalid table entry kind!");
			}
		}
		return new IrTable(map, getLocation());
	}

	@Override
	public boolean isEqual(IrValue other) {
		throw new RuntimeException("Internal compiler error! Cannot compare IrTableLiteral!");
	}

	@Override
	public int hash() {
		throw new RuntimeException("Internal compiler error! Cannot hash IrTableLiteral!");
	}
}
