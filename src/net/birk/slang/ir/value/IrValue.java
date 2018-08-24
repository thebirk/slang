package net.birk.slang.ir.value;

import net.birk.slang.SourceLoc;
import net.birk.slang.Token;
import net.birk.slang.ir.IrCore;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.stmt.*;
import net.birk.slang.nodes.*;

import java.util.ArrayList;

public abstract class IrValue {

	public enum Type {
		NULL,
		STRING,
		NUMBER,
		FUNC,
		BINARY,
		IDENT,
		UNARY,
		BOOLEAN,
		CALL,
		ARRAY,
		INDEX,
		TABLE,
		TABLE_LITERAL,
		USERDATA,
		FIELD,
	}

	private Type type;
	private SourceLoc location;

	public IrValue(Type type, SourceLoc location) {
		this.type = type;
		this.location = location;
	}

	public abstract IrValue eval(IrScope scope);
	public abstract int hash();
	public abstract boolean isEqual(IrValue other);

	@Override
	public final boolean equals(Object obj) {
		if(obj instanceof IrValue) {
			return isEqual((IrValue) obj);
		} else {
			return false;
		}
	}

	@Override
	public final int hashCode() {
		int h = hash();
		//System.out.println("hashCode for type " + type + " at " + getLocation() + ": " + h);
		return h;
	}

	public Type getType() {
		return type;
	}


	public SourceLoc getLocation() {
		return location;
	}

	//NOTE: We do not resolve lhs and rhs here. We expect you to cover that.
	public static IrValue doBinary(int op, IrValue lhs, IrValue rhs) {
		// == !=        -> boolean, number, null
		// <, <=, >, >= -> number

		switch(op) {
			case Token.EQUALS:
			case Token.NE: {
				if(lhs.getType() == IrValue.Type.NULL || rhs.getType() == IrValue.Type.NULL) {
					if(lhs.getType() == IrValue.Type.NULL && rhs.getType() == IrValue.Type.NULL) {
						if(op == Token.EQUALS) return new IrBoolean(true, lhs.getLocation());
						else return new IrBoolean(false, lhs.getLocation());
					} else {
						if(op == Token.NE) {
							return new IrBoolean(true, lhs.getLocation());
						} else {
							return new IrBoolean(false, lhs.getLocation());
						}
					}
				}
				else if(lhs.getType() == IrValue.Type.BOOLEAN && rhs.getType() == IrValue.Type.BOOLEAN) {
					IrBoolean lb = (IrBoolean) lhs;
					IrBoolean rb = (IrBoolean) rhs;
					if(op == Token.EQUALS) {
						return new IrBoolean(lb.getValue() == rb.getValue(), lhs.getLocation());
					} else {
						return new IrBoolean(lb.getValue() != rb.getValue(), lhs.getLocation());
					}
				}
				else if(lhs.getType() == IrValue.Type.NUMBER && rhs.getType() == IrValue.Type.NUMBER) {
					IrNumber ln = (IrNumber) lhs;
					IrNumber rn = (IrNumber) rhs;
					if(op == Token.EQUALS) {
						return new IrBoolean(ln.getValue() == rn.getValue(), lhs.getLocation());
					} else {
						return new IrBoolean(ln.getValue() != rn.getValue(), lhs.getLocation());
					}
				}
				else if(lhs.getType() == IrValue.Type.STRING && rhs.getType() == IrValue.Type.STRING) {
					IrString ln = (IrString) lhs;
					IrString rn = (IrString) rhs;
					if(op == Token.EQUALS) {
						return new IrBoolean(ln.getValue() == rn.getValue(), lhs.getLocation());
					} else {
						return new IrBoolean(ln.getValue() != rn.getValue(), lhs.getLocation());
					}
				}
				else {
					throw new IrException(lhs.getLocation(), "Cannont compare left and right side using operator '" + op + "'!");
				}
			}
			case Token.GTE:
			case Token.LTE:
			case '<':
			case '>': {
				if(lhs.getType() != IrValue.Type.NUMBER && rhs.getType() != IrValue.Type.NUMBER) {
					throw new IrException(lhs.getLocation(), "Can only use operator '" + op + "' on numbers!");
				}

				IrNumber lhsn = (IrNumber)lhs;
				IrNumber rhsn = (IrNumber)rhs;
				switch (op) {
					case Token.GTE: return new IrBoolean(lhsn.getValue() >= rhsn.getValue(), lhs.getLocation());
					case Token.LTE: return new IrBoolean(lhsn.getValue() <= rhsn.getValue(), lhs.getLocation());
					case '>': return new IrBoolean(lhsn.getValue() > rhsn.getValue(), lhs.getLocation());
					case '<': return new IrBoolean(lhsn.getValue() < rhsn.getValue(), lhs.getLocation());

					default: {
						throw new IrException(lhs.getLocation(), "Internal compiler error!");
					}
				}
			}

			case '+': {
				if(lhs.getType() == IrValue.Type.STRING && rhs.getType() == IrValue.Type.NUMBER) {
					IrString lhstr = (IrString) lhs;
					IrNumber rhstr = (IrNumber) rhs;
					return new IrString(lhstr.getValue() + IrCore.stringifyValue(rhstr), lhs.getLocation());
				} else if(lhs.getType() == IrValue.Type.NUMBER && rhs.getType() == IrValue.Type.STRING) {
					IrNumber lhstr = (IrNumber) lhs;
					IrString rhstr = (IrString) rhs;
					return new IrString(IrCore.stringifyValue(lhstr) + rhstr.getValue(), lhs.getLocation());
				} else if(lhs.getType() == IrValue.Type.STRING && rhs.getType() == IrValue.Type.STRING) {
					IrString lhstr = (IrString) lhs;
					IrString rhstr = (IrString) rhs;
					return new IrString(lhstr.getValue() + rhstr.getValue(), lhs.getLocation());
				}
				// fallthrough if we don't have a string
			}
			case '-':
			case '*':
			case '/':
			case '%': {
				if(lhs.getType() != IrValue.Type.NUMBER) {
					throw new IrException(lhs.getLocation(), "Can only use operator '" + ((char)op) + "' on numbers!");
				} else if(rhs.getType() != IrValue.Type.NUMBER) {
					throw new IrException(lhs.getLocation(), "Can only use operator '" + ((char)op) + "' on numbers!");
				}

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
			}

			default: {
				//TODO: Complete
				//      - If both are numbers we do number ops
				//      - If the first arg is a string we do string concatenation
				throw new RuntimeException("Incomplete!");
			}
		}
	}

	public static IrValue unary(int op, IrValue value) {
		// + - !
		// + - -> numbers
		// !   -> boolean, null
		switch (op){
			case '+':
			case '-': {
				if(value.getType() != IrValue.Type.NUMBER) {
					throw new IrException(value.getLocation(), "Unary operator '" + op + "' is only supported on numbers!");
				}

				switch (op) {
					case '+': return value;
					case '-': {
						IrNumber iv = (IrNumber) value;
						IrNumber result = new IrNumber(-((IrNumber) value).getValue(), iv.getLocation());
						return result;
					}

					default: {
						throw new RuntimeException("Compiler error!");
					}
				}
			}
			case '!': {
				if(value.getType() == IrValue.Type.BOOLEAN) {
					IrBoolean irBoolean = (IrBoolean) value;
					return new IrBoolean(!irBoolean.getValue(), value.getLocation());
				} else if (value.getType() == IrValue.Type.NULL) {
					return new IrBoolean(true, value.getLocation());
				} else {
					throw new IrException(value.getLocation(), "Unary operator '!' is only supported on booleans and null!");
				}
			}

			default: {
				throw new RuntimeException("Compiler error!");
			}
		}
	}

	public static boolean isTrue(IrValue eval) {
		switch (eval.getType()) {
			case BOOLEAN: {
				return ((IrBoolean)eval).getValue();
			}
			case NUMBER: {
				throw new IrException(eval.getLocation(), "Cannot use a number as a boolean expression");
			}
			case STRING: {
				throw new IrException(eval.getLocation(), "Cannot use a string as a boolean expression");
			}
			case FUNC: {
				throw new IrException(eval.getLocation(), "Cannot use a function as a boolean expression");
			}
			case NULL: {
				return false;
			}

			default: {
				throw new RuntimeException("Incomplete");
			}
		}
	}

	public static IrStmt generateStmt(Node n) {
		switch (n.getType()) {
			case RETURN: {
				NodeReturn nreturn = (NodeReturn) n;

				if(nreturn.getExpr() != null) {
					return new IrReturn(generateExpr(nreturn.getExpr()), n.getLocation());
				} else {
					return new IrReturn(null, n.getLocation());
				}
			}
			case BLOCK: {
				NodeBlock block = (NodeBlock) n;
				return generateBlock(block);
			}
			case VAR: {
				NodeVar nvar = (NodeVar) n;
				IrVar irVar = new IrVar(nvar.getName(), generateExpr(nvar.getExpr()), nvar.getLocation());
				return irVar;
			}
			case CALL: {
				NodeCall ncall = (NodeCall) n;
				ArrayList<IrValue> args = new ArrayList<IrValue>();
				for(Node argn : ncall.getArgs()) {
					args.add(generateExpr(argn));
				}
				IrCallStmt irCallStmt = new IrCallStmt(generateExpr(ncall.getExpr()), args, ncall.getLocation());
				return irCallStmt;
			}
			case ASSIGNMENT: {
				NodeAssignment na = (NodeAssignment) n;
				IrAssignment irAssignment = new IrAssignment(na.getOp(), generateExpr(na.getLhs()), generateExpr(na.getRhs()), na.getLocation());
				return irAssignment;
			}
			case IF: {
				NodeIf nif = (NodeIf) n;
				if(nif.getElse() == null) {
					return new IrIf(generateExpr(nif.getExpr()), generateBlock(nif.getBlock()), null, nif.getLocation());
				} else {
					return new IrIf(generateExpr(nif.getExpr()), generateBlock(nif.getBlock()), generateStmt(nif.getElse()), nif.getLocation());
				}
			}
			case FUNC: {
				NodeFunc nfunc = (NodeFunc) n;
				IrFunc irFunc = new IrSlangFunc(generateBlock(nfunc.getBlock()), nfunc.getArgs(), nfunc.getLocation());
				IrVar irVar = new IrVar(nfunc.getIdent().getName(), irFunc, nfunc.getLocation());
				return irVar;
			}
			case WHILE: {
				NodeWhile nw = (NodeWhile) n;
				return new IrWhile(generateExpr(nw.getCond()), generateBlock(nw.getBlock()), n.getLocation());
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
			case NULL: {
				return new IrNull(expr.getLocation());
			}
			case STRING: {
				NodeString str = (NodeString) expr;
				return new IrString(str.getValue(), str.getLocation());
			}
			case NUMBER: {
				NodeNumber num = (NodeNumber) expr;
				return new IrNumber(num.getValue(), num.getLocation());
			}
			case IDENT: {
				NodeIdent ident = (NodeIdent) expr;
				return new IrIdent(ident.getName(), ident.getLocation());
			}
			case BINARY: {
				NodeBinary b = (NodeBinary) expr;
				return new IrBinary(b.getOp(), generateExpr(b.getLhs()), generateExpr(b.getRhs()), b.getLocation());
			}
			case TRUE: {
				return new IrBoolean(true, expr.getLocation());
			}
			case FALSE: {
				return new IrBoolean(false, expr.getLocation());
			}
			case FUNC: {
				NodeFunc func = (NodeFunc) expr;
				IrBlock block = generateBlock(func.getBlock());
				IrFunc irFunc = new IrSlangFunc(block, func.getArgs(), func.getLocation());
				return irFunc;
			}
			case UNARY: {
				NodeUnary un = (NodeUnary) expr;
				return new IrUnary(un.getOp(), generateExpr(un.getExpr()), un.getLocation());
			}
			case CALL: {
				NodeCall ncall = (NodeCall) expr;
				ArrayList<IrValue> args = new ArrayList<IrValue>();
				for(Node n : ncall.getArgs()) {
					args.add(generateExpr(n));
				}
				return new IrCallValue(generateExpr(ncall.getExpr()), args, ncall.getLocation());
			}
			case ARRAY_LITERAL: {
				NodeArrayLiteral narr = (NodeArrayLiteral) expr;
				ArrayList<IrValue> items = new ArrayList<IrValue>();
				for(Node n : narr.getItems()) {
					items.add(generateExpr(n));
				}
				return new IrArray(items, narr.getLocation());
			}
			case INDEX: {
				NodeIndex ni = (NodeIndex) expr;
				return new IrIndex(generateExpr(ni.getExpr()), generateExpr(ni.getIndex()), expr.getLocation());
			}
			case TABLE_LITERAL: {
				NodeTableLiteral nt = (NodeTableLiteral) expr;
				return new IrTableLiteral(nt.getEntries(), nt.getLocation());
			}
			case ANON_FUNC: {
				NodeAnonFunc f = (NodeAnonFunc) expr;
				return new IrSlangFunc(generateBlock(f.getBlock()), f.getArgs(), f.getLocation());
			}
			case FIELD: {
				NodeField f = (NodeField) expr;
				return new IrField(generateExpr(f.getIdent()), generateExpr(f.getExpr()), f.getLocation());
			}

			default: {
				throw new IrException(expr.getLocation(), "Invalid type value of missing case!");
			}
		}
	}

}
