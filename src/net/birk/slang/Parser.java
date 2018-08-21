package net.birk.slang;

import net.birk.slang.nodes.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	private List<Token> tokens;
	private int tokenOffset;
	private Token currentToken;

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
		currentToken = null;

		tokenOffset = -1;
		nextToken();
	}

	private Node parseOperand() {
		Token t = currentToken;

		if(matchToken(Token.NUMBER)) {
			return new NodeNumber(Double.valueOf(t.getLexeme()), t.getSourceLoc());
		}
		else if(matchToken(Token.IDENT)) {
			return new NodeIdent(t.getLexeme(), t.getSourceLoc());
		}
		else if(matchToken(Token.STRING)) {
			return new NodeString(t.getLexeme(), t.getSourceLoc());
		}
		else if(matchToken(Token.TRUE)) {
			return new NodeTrue(t.getSourceLoc());
		}
		else if(matchToken(Token.FALSE)) {
			return new NodeFalse(t.getSourceLoc());
		}
		else if(matchToken(Token.NULL)) {
			return new NodeNull(t.getSourceLoc());
		}
		else if(matchToken('(')) {
			Node expr = parseExpr();
			if(!matchToken(')')) {
				System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected ')' after expression, got '" + currentToken.getTypeString() + "'.");
				System.exit(1);
				return null;
			}

			return expr;
		}
		else {
			System.err.println(t.getSourceLoc() + ": Syntax error! Expected operand.");
			System.exit(1);
			return null;
		}
	}

	private Node parseBase() {
		Node expr = parseOperand();

		while(isToken('(') || isToken('[') || isToken('.')) {
			Token op = currentToken;

			if(matchToken('[')) {
				Node index = parseExpr();

				if(!matchToken(']')) {
					System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected ']', got '" + currentToken.getTypeString() + "'.");
					System.exit(1);
					return null;
				}
				expr = new NodeIndex(expr, index, op.getSourceLoc());
			}
			else if(matchToken('(')) {
				ArrayList<Node> args = new ArrayList<Node>();
				if(matchToken(')')) {
					expr = new NodeCall(expr, args, op.getSourceLoc());
				} else {
					do {
						Node arg = parseExpr();
						args.add(arg);
					} while(matchToken(','));

					if(!matchToken(')')) {
						System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected ')', got '" + currentToken.getTypeString() + "'.");
						System.exit(1);
						return null;
					}

					expr = new NodeCall(expr, args, op.getSourceLoc());
				}
			}
			else if(matchToken('.')) {
				Token t = currentToken;
				if(!matchToken(Token.IDENT)) {
					System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected 'identifier' after '.', got '" + currentToken.getTypeString() + "'.");
					System.exit(1);
					return null;
				}

				expr = new NodeField(expr, new NodeIdent(t.getLexeme(), t.getSourceLoc()), t.getSourceLoc());
			}
			else {
				System.err.println("Unsynced while and if!");
				assert false;
				System.exit(1);
			}
		}

		return expr;
	}

	private Node parseUnary() {
		boolean isUnary = false;
		Token op = currentToken;

		if(isToken('+') || isToken('-') || isToken('!')) {
			isUnary = true;
			nextToken();
		}

		Node rhs = parseBase();

		if(isUnary) {
			rhs = new NodeUnary(op.getType(), rhs, op.getSourceLoc());
		}
		return rhs;
	}

	private Node parseMul() {
		Node lhs = parseUnary();

		while(isToken('*') || isToken('/') || isToken('%')) {
			Token op = currentToken;
			nextToken();
			Node rhs = parseUnary();
			lhs = new NodeBinary(op.getType(), lhs, rhs, op.getSourceLoc());
		}

		return lhs;
	}

	private Node parsePlus() {
		Node lhs = parseMul();

		while(isToken('+') || isToken('-')) {
			Token op = currentToken;
			nextToken();
			Node rhs = parseMul();
			lhs = new NodeBinary(op.getType(), lhs, rhs, op.getSourceLoc());
		}

		return lhs;
	}

	private Node parseComps() {
		Node lhs = parsePlus();

		while(isToken(Token.EQUALS) || isToken(Token.NE) || isToken(Token.GTE) || isToken(Token.LTE) || isToken('<') || isToken('>')) {
			Token op = currentToken;
			nextToken();
			Node rhs = parsePlus();
			lhs = new NodeBinary(op.getType(), lhs, rhs, op.getSourceLoc());
		}

		return lhs;
	}

	public Node parseExpr() {
		return parseComps();
	}

	private Node parseVar() {
		assert currentToken.getType() == Token.VAR;

		Token var = currentToken;
		nextToken();

		Token ident = currentToken;
		if(!matchToken(Token.IDENT)) {
			System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected 'identifier' after 'var', got '" + currentToken.getTypeString() + "'.");
			System.exit(1);
			return null;
		}

		if(!matchToken('=')) {
			System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected '=' while parsing 'var' declaration, got '" + currentToken.getTypeString() + "'.");
			System.exit(1);
			return null;
		}

		Node expr = parseExpr();

		return new NodeVar(ident.getLexeme(), expr, var.getSourceLoc());
	}

	private Node parseIf() {
		assert currentToken.getType() == Token.IF;

		Token iff = currentToken;
		nextToken();

		Node expr = parseExpr();
		NodeBlock block = parseBlock();
		Node _else = null;

		if(matchToken(Token.ELSE)) {
			if(isToken(Token.IF)) {
				_else = parseIf();
			} else {
				_else = parseBlock();
			}
		}

		return new NodeIf(expr, block, _else, iff.getSourceLoc());
	}

	private Node parseReturn() {
		assert currentToken.getType() == Token.RETURN;

		Token ret = currentToken;
		nextToken();

		if(isToken(';')) {
			return new NodeReturn(null, ret.getSourceLoc());
		} else {
			Node expr = parseExpr();
			return new NodeReturn(expr, ret.getSourceLoc());
		}
	}

	private Node parseStmt() {
		if(isToken(Token.FUNC)) {
			return parseFunc();
		}
		else if(isToken(Token.VAR)) {
			return parseVar();
		}
		else if(isToken(Token.IF)) {
			return parseIf();
		}
		else if(isToken(Token.RETURN)) {
			return parseReturn();
		}
		else if(isToken('{') || isToken(Token.DO)) {
			return parseBlock();
		}
		else if(matchToken(';')) {
			return null;
		}
		else {
			Node expr = parseExpr();
			Token op = currentToken;
			if(isToken('=') || isToken(Token.MINUS_EQUALS) || isToken(Token.PLUS_EQUALS)) {
				nextToken();
				Node rhs = parseExpr();
				return new NodeAssignment(op.getType(), expr, rhs, op.getSourceLoc());
			}
			else if(expr.getType() == Node.CALL) {
				return expr;
			}
			else {
				System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected statement, got '" + currentToken.getTypeString() + "'.");
				System.exit(1);
				return null;
			}
		}
	}

	private NodeBlock parseBlock() {
		ArrayList<Node> stmts = new ArrayList<Node>();
		Token start = currentToken;
		if(matchToken(Token.DO)) {
			Node stmt = parseStmt();
			if(stmt != null) {
				stmts.add(stmt);

				//NOTE: Hacky!
				if(stmt.getType() != Node.FUNC && stmt.getType() != Node.IF && stmt.getType() != Node.BLOCK) {
					if (!matchToken(';')) {
						System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected ';' after statement, got '" + currentToken.getTypeString() + "'.");
						System.exit(1);
						return null;
					}
				}
			}

			return new NodeBlock(stmts, currentToken.getSourceLoc());
		}
		else if(matchToken('{')) {
			if(matchToken('}')) {
				return new NodeBlock(stmts, currentToken.getSourceLoc());
			}

			do {
				Node stmt = parseStmt();
				if(stmt != null) {
					stmts.add(stmt);

					//NOTE: Hacky!
					if(stmt.getType() != Node.FUNC && stmt.getType() != Node.IF && stmt.getType() != Node.BLOCK) {
						if (!matchToken(';')) {
							System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected ';' after statement, got '" + currentToken.getTypeString() + "'.");
							System.exit(1);
							return null;
						}
					}
				}
			} while (!matchToken('}'));

			return new NodeBlock(stmts, currentToken.getSourceLoc());
		}
		else {
			System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected 'do' or '{' while parsing block, got '" + currentToken.getTypeString() + "'.");
			System.exit(1);
			return null;
		}
	}

	public NodeFunc parseFunc() {
		assert currentToken.getType() == Token.FUNC;

		Token fn = currentToken;
		nextToken();

		Token name = currentToken;
		if(!matchToken(Token.IDENT)) {
			System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected 'identifier' after 'fn', got '" + currentToken.getTypeString() + "'.");
			System.exit(1);
			return null;
		}
		NodeIdent ident = new NodeIdent(name.getLexeme(), name.getSourceLoc());

		if(!matchToken('(')) {
			System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected '(' after function name, got '" + currentToken.getTypeString() + "'.");
			System.exit(1);
			return null;
		}

		ArrayList<String> args = new ArrayList<String>();
		if(!matchToken(')')) {
			do {
				Token id = currentToken;
				if(!matchToken(Token.IDENT)) {
					System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected 'identifier' while parsing argument list, got '" + currentToken.getTypeString() + "'.");
					System.exit(1);
					return null;
				}
				args.add(id.getLexeme());
			} while (matchToken(','));

			if(!matchToken(')')) {
				System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected ')' while parsing argument list, got '" + currentToken.getTypeString() + "'.");
				System.exit(1);
				return null;
			}
		}

		NodeBlock block = parseBlock();

		NodeFunc func = new NodeFunc(ident, args, block, fn.getSourceLoc());
		return func;
	}

	public ArrayList<Node> parse() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		while(!isToken(Token.EOF)) {
			Node stmt = parseStmt();
			nodes.add(stmt);

			if(stmt.getType() == Node.VAR) {
				if(!matchToken(';')) {
					System.err.println(currentToken.getSourceLoc() + ": Syntax error! Expected ';' after statement, got '" + currentToken.getTypeString() + "'.");
					System.exit(1);
					return null;
				}
			}
		}
		return nodes;
	}

	private boolean isToken(int type) {
		return currentToken.getType() == type;
	}

	private boolean matchToken(int type) {
		if(isToken(type)) {
			nextToken();
			return true;
		}
		return false;
	}

	private Token nextToken() {
		tokenOffset++;
		currentToken = tokens.get(tokenOffset);
		return tokens.get(tokenOffset);
	}

}
