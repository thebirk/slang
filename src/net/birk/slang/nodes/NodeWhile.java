package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeWhile extends Node {

	private Node cond;
	private NodeBlock block;

	public NodeWhile(Node cond, NodeBlock block, SourceLoc location) {
		super(Node.WHILE, location);
		this.cond = cond;
		this.block = block;
	}

	public Node getCond() {
		return cond;
	}

	public NodeBlock getBlock() {
		return block;
	}

}
