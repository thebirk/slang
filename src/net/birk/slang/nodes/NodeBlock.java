package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

import java.util.ArrayList;

public class NodeBlock extends  Node {

	private ArrayList<Node> stmts;

	public NodeBlock(ArrayList<Node> stmts, SourceLoc location) {
		super(Node.Type.BLOCK, location);
		this.stmts = stmts;
	}

	public ArrayList<Node> getStmts() {
		return stmts;
	}
}
