package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;

public abstract class IrStmt {

	public enum Type {
		ASSIGNMENT,
		VAR,
		RETURN,
		CALL,
		BLOCK,
		IF,
		WHILE
	}

	private Type type;
	private SourceLoc location;

	public IrStmt(Type type, SourceLoc location) {
		this.type = type;
		this.location = location;
	}

	public abstract IrStmtResult eval(IrScope scope);

	public Type getType() {
		return type;
	}

	public SourceLoc getLocation() {
		return location;
	}

}
