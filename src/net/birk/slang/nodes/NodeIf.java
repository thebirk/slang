package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeIf extends Node {

	private Node expr;
	private NodeBlock block;
	private Node _else;

	public NodeIf(Node expr, NodeBlock block, Node _else, SourceLoc location) {
		super(Node.Type.IF, location);
		this.expr = expr;
		this.block = block;
		this._else = _else;
	}

	public Node getExpr() {
		return expr;
	}

	public NodeBlock getBlock() {
		return block;
	}

	public Node getElse() {
		return _else;
	}

}
