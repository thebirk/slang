package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeString extends Node {

	private String value;

	public NodeString(String value, SourceLoc location) {
		super(Node.Type.STRING, location);
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
