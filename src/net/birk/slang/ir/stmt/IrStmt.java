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

	/*public static final int ASSIGNMENT = 0;
	public static final int VAR = 1;
	public static final int RETURN = 2;
	public static final int CALL = 3;
	public static final int BLOCK = 4;
	public static final int IF = 5;
	public static final int WHILE = 6;*/

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
