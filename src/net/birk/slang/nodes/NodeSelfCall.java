package net.birk.slang.nodes;

import java.util.ArrayList;

import net.birk.slang.SourceLoc;

public class NodeSelfCall extends Node {

    public Node expr;
    public NodeIdent ident;
	public ArrayList<Node> args;

	public NodeSelfCall(Node expr, NodeIdent ident, ArrayList<Node> args, SourceLoc location) {
		super(Node.Type.SELFCALL, location);
		this.expr = expr;
		this.ident = ident;
		this.args = args;
	}

	public Node getExpr() {
		return expr;
    }
    
    public NodeIdent getIdent() {
        return ident;
    }

	public ArrayList<Node> getArgs() {
		return args;
	}

}
