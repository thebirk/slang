package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeVar extends Node {

	private String name;
	private Node expr;

	public NodeVar(String name, Node expr, SourceLoc location) {
		super(Node.VAR, location);
		this.name = name;
		this.expr = expr;
	}

	public String getName() {
		return name;
	}

	public Node getExpr() {
		return expr;
	}

}
