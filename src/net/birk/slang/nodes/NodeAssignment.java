package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeAssignment extends Node {

	private int op;
	private Node lhs;
	private Node rhs;

	public NodeAssignment(int op, Node lhs, Node rhs, SourceLoc location) {
		super(Node.ASSIGNMENT, location);
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public int getOp() {
		return op;
	}

	public Node getLhs() {
		return lhs;
	}

	public Node getRhs() {
		return rhs;
	}

}
