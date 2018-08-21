package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;
import net.birk.slang.Token;

public class NodeFalse extends Node {

	public NodeFalse(SourceLoc location) {
		super(Node.FALSE, location);
	}

}
