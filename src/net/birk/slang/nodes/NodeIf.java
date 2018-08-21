package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeIf extends Node {

	private Node expr;
	private NodeBlock block;
	private NodeBlock elseBlock;

	public NodeIf(Node expr, NodeBlock block, NodeBlock elseBlock, SourceLoc location) {
		super(Node.IF, location);
		this.expr = expr;
		this.block = block;
		this.elseBlock = elseBlock;
	}

	public Node getExpr() {
		return expr;
	}

	public NodeBlock getBlock() {
		return block;
	}

	public NodeBlock getElseBlock() {
		return elseBlock;
	}

}
