package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

import java.util.ArrayList;

public class NodeArrayLiteral extends Node {

	private ArrayList<Node> items;

	public NodeArrayLiteral(ArrayList<Node> items, SourceLoc location) {
		super(Node.Type.ARRAY_LITERAL, location);
		this.items = items;
	}

	public ArrayList<Node> getItems() {
		return items;
	}

}
