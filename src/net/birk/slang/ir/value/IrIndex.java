package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;

public class IrIndex extends IrValue {

	private IrValue expr;
	private IrValue index;

	public IrIndex(IrValue expr, IrValue index, SourceLoc location) {
		super(IrValue.INDEX, location);
		this.expr = expr;
		this.index = index;
	}

	@Override
	public IrValue eval(IrScope scope) {
		expr = expr.eval(scope);
		if(expr.getType() == IrValue.ARRAY) {
			IrArray array = (IrArray) expr;
			index = index.eval(scope);
			if(index.getType() != IrValue.NUMBER) {
				throw new IrException(index.getLocation(), "Can only index arrays using numbers!");
			}
			IrNumber indexN = (IrNumber) index;

			//TODO: Warn on non-integer index?

			return array.getItems().get((int)indexN.getValue()).eval(scope);
		}/* else if(value.getType() == IrValue.TABLE) {

		} */else {
			throw new IrException(getLocation(), "Trying to index non-indexable value!");
		}
	}

	public IrValue getExpr() {
		return expr;
	}

	public IrValue getIndex() {
		return index;
	}

}
