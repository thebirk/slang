package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeIf extends Node {

	private Node expr;
	private NodeBlock block;

	public NodeIf(Node expr, NodeBlock block, SourceLoc location) {
		super(Node.IF, location);
		this.expr = expr;
		this.block = block;
	}

	public Node getExpr() {
		return expr;
	}

	public NodeBlock getBlock() {
		return block;
	}

}
