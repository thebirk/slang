package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeReturn extends Node {

	private Node expr;

	public NodeReturn(Node expr, SourceLoc location) {
		super(Node.RETURN, location);
		this.expr = expr;
	}

	public Node getExpr() {
		return expr;
	}
}
