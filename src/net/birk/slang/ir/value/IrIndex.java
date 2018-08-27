package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;

public class IrIndex extends IrValue {

	private IrValue expr;
	private IrValue index;

	public IrIndex(IrValue expr, IrValue index, SourceLoc location) {
		super(IrValue.Type.INDEX, location);
		this.expr = expr;
		this.index = index;
	}

	@Override
	public IrValue eval(IrScope scope) {
		IrValue expr = this.expr.eval(scope);
		if(expr.getType() == IrValue.Type.ARRAY) {
			IrArray array = (IrArray) expr;
			IrValue index = this.index.eval(scope);
			if(index.getType() != IrValue.Type.NUMBER) {
				throw new IrException(index.getLocation(), "Can only index arrays using numbers!");
			}
			IrNumber indexN = (IrNumber) index;

			//TODO: Warn on non-integer index?

			return array.getItems().get((int)indexN.getValue()).eval(scope);
		} else if(expr.getType() == IrValue.Type.TABLE) {
			IrTable table = (IrTable) expr;
			IrValue index = this.index.eval(scope);
			if(index == null) throw new RuntimeException("Internal compiler error! Java null cannot be used as index!");
			IrValue result = table.getMap().get(index);
			if(result == null) {
				result = new IrNull(getLocation());
			}
			return result.eval(scope);
		} else {
			throw new IrException(getLocation(), "Trying to index non-indexable value!");
		}
	}

	public IrValue getExpr() {
		return expr;
	}

	public IrValue getIndex() {
		return index;
	}

	@Override
	public boolean isEqual(IrValue other) {
		throw new RuntimeException("Internal compiler error! Cannot compare IrIndex!");
	}

	@Override
	public int hash() {
		throw new RuntimeException("Internal compiler error! Cannnot hash IrIndex!");
	}
}
