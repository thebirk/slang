package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeIdent extends Node {

	private String name;

	public NodeIdent(String name, SourceLoc location) {
		super(Node.IDENT, location);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
