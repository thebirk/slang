package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeNull extends Node {

	public NodeNull(SourceLoc location) {
		super(Node.NULL, location);
	}

}
