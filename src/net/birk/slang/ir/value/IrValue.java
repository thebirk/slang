package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrScope;
import net.birk.slang.nodes.*;

public abstract class IrValue {

	public static final int NULL = 0;
	public static final int STRING = 1;
	public static final int NUMBER = 2;
	public static final int FUNC = 3;
	public static final int BINARY = 4;
	public static final int IDENT = 5;
	public static final int UNARY = 6;
	public static final int BOOLEAN = 7;

	private int type;
	private SourceLoc location;

	public IrValue(int type, SourceLoc location) {
		this.type = type;
		this.location = location;
	}

	public int getType() {
		return type;
	}

	public abstract IrValue eval(IrScope scope);

	//NOTE: We do not resole lhs and rhs here. We expect you to cover that.
	public static IrValue add(int op, IrValue lhs, IrValue rhs) {
		//TODO: Complete
		//      - If both are numbers we do number ops
		//      - If the first arg is a string we do string concatenation
		throw new RuntimeException("Incomplete!");
	}

	public static IrValue unary(int op, IrValue value) {
		throw new RuntimeException("Incomplete");
	}

	public static IrValue generateExpr(Node expr) {
		switch (expr.getType()) {
			case Node.NULL: {
				return new IrNull(expr.getLocation());
			}
			case Node.STRING: {
				NodeString str = (NodeString) expr;
				return new IrString(str.getValue(), str.getLocation());
			}
			case Node.NUMBER: {
				NodeNumber num = (NodeNumber) expr;
				return new IrNumber(num.getValue(), num.getLocation());
			}
			case Node.IDENT: {
				NodeIdent ident = (NodeIdent) expr;
				return new IrIdent(ident.getName(), ident.getLocation());
			}
			case Node.BINARY: {
				NodeBinary b = (NodeBinary) expr;
				return new IrBinary(b.getOp(), generateExpr(b.getLhs()), generateExpr(b.getRhs()), b.getLocation());
			}
			case Node.TRUE: {
				return new IrBoolean(true, expr.getLocation());
			}
			case Node.FALSE: {
				return new IrBoolean(false, expr.getLocation());
			}


			default: {
				throw new RuntimeException("Invalid type value of missing case!");
			}
		}
	}

	public SourceLoc getLocation() {
		return location;
	}

}
