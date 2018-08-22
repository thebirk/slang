package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

import java.util.ArrayList;

public class NodeAnonFunc extends Node {

	private ArrayList<String> args;
	private NodeBlock block;

	public NodeAnonFunc(ArrayList<String> args, NodeBlock block, SourceLoc location) {
		super(Node.ANON_FUNC, location);
		this.args = args;
		this.block = block;
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	public NodeBlock getBlock() {
		return block;
	}

}
