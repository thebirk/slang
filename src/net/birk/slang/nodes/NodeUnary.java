package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeUnary extends Node {

	private int op;
	private Node expr;

	public NodeUnary(int op, Node expr, SourceLoc location) {
		super(Node.UNARY, location);
		this.op = op;
		this.expr = expr;
	}

	public int getOp() {
		return op;
	}

	public Node getExpr() {
		return expr;
	}

}
