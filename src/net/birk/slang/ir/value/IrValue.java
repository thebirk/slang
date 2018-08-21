package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.Token;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.stmt.*;
import net.birk.slang.nodes.*;

import java.util.ArrayList;

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
	public static IrValue doBinary(int op, IrValue lhs, IrValue rhs) {
		switch(op) {
			case Token.EQUALS:
			case Token.NE:
			case Token.GTE:
			case Token.LTE:
			case '<':
			case '>': {

			}

			case '+': {
				if(lhs.getType() == IrValue.STRING) {

					break;
				}
				// fallthrough if we dont have a string
			}
			case '-':
			case '*':
			case '/':
			case '%': {
				if(lhs.getType() != IrValue.NUMBER || rhs.getType() != IrValue.NUMBER) {
					
				}
			}
		}

		if(lhs.getType() == IrValue.NUMBER && rhs.getType() == IrValue.NUMBER) {
			IrNumber lhsn = (IrNumber)lhs;
			IrNumber rhsn = (IrNumber)rhs;

			switch (op) {
				case '+': return new IrNumber(lhsn.getValue() + rhsn.getValue(), lhs.getLocation());
				case '-': return new IrNumber(lhsn.getValue() - rhsn.getValue(), lhs.getLocation());
				case '*': return new IrNumber(lhsn.getValue() * rhsn.getValue(), lhs.getLocation());
				case '/': return new IrNumber(lhsn.getValue() / rhsn.getValue(), lhs.getLocation());
				case '%': return new IrNumber(lhsn.getValue() % rhsn.getValue(), lhs.getLocation());

				default: {
					throw new RuntimeException("Invalid op!");
				}
			}
		} else {
			//TODO: Complete
			//      - If both are numbers we do number ops
			//      - If the first arg is a string we do string concatenation
			throw new RuntimeException("Incomplete!");
		}
	}

	public static IrValue unary(int op, IrValue value) {
		throw new RuntimeException("Incomplete");
	}

	public static boolean isTrue(IrValue eval) {
		switch (eval.getType()) {
			case IrValue.BOOLEAN: {
				return ((IrBoolean)eval).getValue();
			}
			case IrValue.NUMBER: {
				throw new IrException(eval.getLocation(), "Cannot use a number as a boolean expression");
			}
			case IrValue.STRING: {
				throw new IrException(eval.getLocation(), "Cannot use a string as a boolean expression");
			}
			case IrValue.NULL: {
				return false;
			}

			default: {
				throw new RuntimeException("Incomplete");
			}
		}
	}

	public static IrStmt generateStmt(Node n) {
		switch (n.getType()) {
			case Node.RETURN: {
				NodeReturn nreturn = (NodeReturn) n;

				if(nreturn.getExpr() != null) {
					return new IrReturn(generateExpr(nreturn.getExpr()), n.getLocation());
				} else {
					return new IrReturn(null, n.getLocation());
				}
			}
			case Node.BLOCK: {
				NodeBlock block = (NodeBlock) n;
				return generateBlock(block);
			}
			case Node.VAR: {
				NodeVar nvar = (NodeVar) n;
				IrVar irVar = new IrVar(nvar.getName(), generateExpr(nvar.getExpr()), nvar.getLocation());
				return irVar;
			}
			case Node.CALL: {
				NodeCall ncall = (NodeCall) n;
				ArrayList<IrValue> args = new ArrayList<IrValue>();
				for(Node argn : ncall.getArgs()) {
					args.add(generateExpr(argn));
				}
				IrCall irCall = new IrCall(generateExpr(ncall.getExpr()), args, ncall.getLocation());
				return irCall;
			}
			case Node.ASSIGNMENT: {
				NodeAssignment na = (NodeAssignment) n;
				IrAssignment irAssignment = new IrAssignment(generateExpr(na.getLhs()), generateExpr(na.getRhs()), na.getLocation());
				return irAssignment;
			}
			case Node.IF: {
				NodeIf nif = (NodeIf) n;
				if(nif.getElseBlock() == null) {
					return new IrIf(generateExpr(nif.getExpr()), generateBlock(nif.getBlock()), null, nif.getLocation());
				} else {
					return new IrIf(generateExpr(nif.getExpr()), generateBlock(nif.getBlock()), generateBlock(nif.getElseBlock()), nif.getLocation());
				}
			}
			default: {
				throw new RuntimeException("Incomplete switch!");
			}
		}
	}

	public static IrBlock generateBlock(NodeBlock n) {
		ArrayList<IrStmt> stmts = new ArrayList<IrStmt>();
		for(Node stmt : n.getStmts()) {
			stmts.add(generateStmt(stmt));
		}

		return new IrBlock(stmts, n.getLocation());
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
			case Node.FUNC: {
				NodeFunc func = (NodeFunc) expr;
				IrBlock block = generateBlock(func.getBlock());
				IrFunc irFunc = new IrSlangFunc(block, func.getArgs(), func.getLocation());
				return irFunc;
			}
			case Node.UNARY: {
				NodeUnary un = (NodeUnary) expr;
				return new IrUnary(un.getOp(), generateExpr(un.getExpr()), un.getLocation());
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