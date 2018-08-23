package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.Token;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.*;

public class IrAssignment extends IrStmt {

	private int op;
	private IrValue lhs;
	private IrValue rhs;

	public IrAssignment(int op, IrValue lhs, IrValue rhs, SourceLoc location) {
		super(IrStmt.Type.ASSIGNMENT, location);
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		if(lhs.getType() != IrValue.IDENT && lhs.getType() != IrValue.INDEX) {
			lhs = lhs.eval(scope);
		}
		if(lhs.getType() != IrValue.IDENT && lhs.getType() != IrValue.INDEX) {
			throw new IrException(lhs.getLocation(), "Cannot assign to left hand of assignment!");
			//throw new IrException(lhs.getLocation(), "Left hand side of assignment cannot be assigned to!");
		}



		if(lhs.getType() == IrValue.IDENT) {
			IrValue result = rhs.eval(scope);
			switch (op) {
				case '=': break;
				case Token.PLUS_EQUALS: {
					result = IrValue.doBinary('+', lhs.eval(scope), result);
				} break;
				case Token.MINUS_EQUALS: {
					result = IrValue.doBinary('-', lhs.eval(scope), result);
				} break;

				default: {
					throw new IrException(getLocation(), "Internal compiler error!");
				}
			}

			IrIdent ident = (IrIdent) lhs;
			if (!scope.set(ident.getName(), result)) {
				throw new IrException(lhs.getLocation(), "Symbol '" + ident.getName() + "' does not exist!");
			}
		} else if(lhs.getType() == IrValue.INDEX) {
			IrIndex irIndex = (IrIndex) lhs;
			IrValue expr = irIndex.getExpr().eval(scope);

			if(expr.getType() == IrValue.ARRAY) {
				IrArray array = (IrArray) expr;
				IrValue index = irIndex.getIndex().eval(scope);
				if(index.getType() != IrValue.NUMBER) {
					throw new IrException(index.getLocation(), "Can only index arrays using numbers!");
				}
				IrNumber indexN = (IrNumber) index;
				int nIndex = (int)indexN.getValue();

				IrValue result = rhs.eval(scope);
				IrValue lhsValue = array.getItems().get(nIndex).eval(scope);
				switch (op) {
					case '=': break;
					case Token.PLUS_EQUALS: {
						result = IrValue.doBinary('+', lhsValue, result);
					} break;
					case Token.MINUS_EQUALS: {
						result = IrValue.doBinary('-', lhsValue, result);
					} break;

					default: {
						throw new IrException(getLocation(), "Internal compiler error!");
					}
				}

				//TODO: Warn on non-integer index?
				array.getItems().set((int)((IrNumber) index).getValue(), result);
			}
			else if(expr.getType() == IrValue.TABLE) {
				// Good luck!
				IrTable table = (IrTable) expr;
				IrValue indexValue = irIndex.getIndex().eval(scope);

				IrValue result = rhs.eval(scope);
				IrValue lhsValue = table.getMap().get(indexValue);

				if(lhsValue == null) {
					//TODO: If we have += or -= no key is not valid but if we only have = then add the key
					throw new RuntimeException("This is not good!");
				}

				lhsValue =  lhsValue.eval(scope);

				switch (op) {
					case '=': break;
					case Token.PLUS_EQUALS: {
						result = IrValue.doBinary('+', lhsValue, result);
					} break;
					case Token.MINUS_EQUALS: {
						result = IrValue.doBinary('-', lhsValue, result);
					} break;

					default: {
						throw new IrException(getLocation(), "Internal compiler error!");
					}
				}

				table.getMap().put(indexValue, result);
			}
			else {
				throw new IrException(getLocation(), "Trying to index non-indexable value!");
			}


		} else {
			throw new IrException(getLocation(), "Internal compiler error!");
		}

		return new IrStmtResult(IrStmtResult.NORMAL, null);
	}
}
