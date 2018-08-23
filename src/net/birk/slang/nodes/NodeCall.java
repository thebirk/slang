package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

import java.util.ArrayList;

public class NodeCall extends Node {

	private Node expr;
	private ArrayList<Node> args;

	public NodeCall(Node expr, ArrayList<Node> args, SourceLoc location) {
		super(Node.Type.CALL, location);
		this.expr = expr;
		this.args = args;
	}

	public Node getExpr() {
		return expr;
	}

	public ArrayList<Node> getArgs() {
		return args;
	}

}
