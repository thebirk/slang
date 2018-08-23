package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;
import net.birk.slang.Token;

public class NodeBinary extends Node {

	private int op;
	private Node lhs;
	private Node rhs;

	public NodeBinary(int op, Node lhs, Node rhs, SourceLoc location) {
		super(Node.Type.BINARY, location);
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
