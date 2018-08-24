package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeField extends Node {

	public Node expr;
	public NodeIdent ident;

	public NodeField(Node expr, NodeIdent ident, SourceLoc location) {
		super(Node.Type.FIELD, location);
		this.expr = expr;
		this.ident = ident;
	}

	public Node getExpr() {
		return expr;
	}

	public NodeIdent getIdent() {
		return ident;
	}

}
