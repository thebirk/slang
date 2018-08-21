package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class NodeNumber extends Node {

	private double value;

	public NodeNumber(double value, SourceLoc location) {
		super(Node.NUMBER, location);
		this.value = value;
	}

	public double getValue() {
		return value;
	}

}
