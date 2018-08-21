package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

import java.util.ArrayList;

public class NodeFunc extends Node {

	private NodeIdent ident;
	private ArrayList<String>  args;
	private NodeBlock block;

	public NodeFunc(NodeIdent ident, ArrayList<String> args, NodeBlock block, SourceLoc location) {
		super(Node.FUNC, location);
		this.ident = ident;
		this.args = args;
		this.block = block;
	}

	public Node getIdent() {
		return ident;
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	public NodeBlock getBlock() {
		return block;
	}
}
