package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeIndex extends Node {

	private Node expr;
	private Node index;

	public NodeIndex(Node expr, Node index, SourceLoc location) {
		super(Node.INDEX, location);
		this.expr = expr;
		this.index = index;
	}

	public Node getExpr() {
		return expr;
	}

	public Node getIndex() {
		return index;
	}

}
