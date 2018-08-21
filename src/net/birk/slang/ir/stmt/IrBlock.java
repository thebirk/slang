package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

import java.util.ArrayList;

public class IrBlock extends IrStmt {

	private ArrayList<IrStmt> stmts;

	public IrBlock(ArrayList<IrStmt> stmts, SourceLoc location) {
		super(IrStmt.BLOCK, location);
		this.stmts = stmts;
	}

	@Override
	public IrStmtResult eval(IrScope parent) {
		IrScope scope = new IrScope(parent);
		for(IrStmt stmt : stmts) {
			IrStmtResult result = stmt.eval(scope);
			if(result.getStatus() != IrStmtResult.NORMAL) {
				//TODO: This will catch both returns and future thing like break and continue, fix that!
				return result;
			}
		}

		return new IrStmtResult(IrStmtResult.NORMAL, null);
	}
}
