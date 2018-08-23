package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeFalse extends Node {

	public NodeFalse(SourceLoc location) {
		super(Node.Type.FALSE, location);
	}

}
