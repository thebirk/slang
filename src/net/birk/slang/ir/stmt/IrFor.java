package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrArray;
import net.birk.slang.ir.value.IrNull;
import net.birk.slang.ir.value.IrNumber;
import net.birk.slang.ir.value.IrValue;

public class IrFor extends IrStmt {

	private String firstName;
	private String secondName;
	private IrValue expr;
	private IrBlock block;

	public IrFor(String firstName, String secondName, IrValue expr, IrBlock block, SourceLoc location) {
		super(Type.FOR, location);
		this.firstName = firstName;
		this.secondName = secondName;
		this.expr = expr;
		this.block = block;
	}

	@Override
	public IrStmtResult eval(IrScope scope) {
		expr = expr.eval(scope);

		if(expr.getType() == IrValue.Type.ARRAY) {
			IrArray a = (IrArray) expr;

			for(int i = 0; i < a.getItems().size(); i++) {
				IrValue v = a.getItems().get(i);
				IrScope forScope = new IrScope(scope);
				forScope.add(firstName, v); //NOTE: We pass the same value, aka by reference
				if(secondName != null) {
					forScope.add(secondName, new IrNumber(i, getLocation()));
				}
				IrStmtResult result = block.eval(forScope);
				//TODO: if(result.getStatus() == IrStmtResult.BREAK)
				if(result.getStatus() == IrStmtResult.RETURN) {
					return result;
				}
			}

			return new IrStmtResult(IrStmtResult.NORMAL, null);
		}
		else if(expr.getType() == IrValue.Type.TABLE) {
			throw new RuntimeException("Incomplete");
		}
		else if(expr.getType() == IrValue.Type.STRING) {
			throw new RuntimeException("Incomplete");
		}
		else {
			throw new IrException(expr.getLocation(), "Cannot iterate over expression of type " + expr.getType() + "!");
		}
	}

}
