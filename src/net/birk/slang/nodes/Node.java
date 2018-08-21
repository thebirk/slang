package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class Node {

	public static final int NUMBER = 1;
	public static final int STRING = 2;
	public static final int BINARY = 3;
	public static final int TRUE = 4;
	public static final int FALSE = 5;
	public static final int UNARY = 6;
	public static final int INDEX = 7;
	public static final int CALL = 8;
	public static final int IDENT = 9;
	public static final int FIELD = 10;
	public static final int FUNC = 11;
	public static final int BLOCK = 12;
	public static final int VAR = 13;
	public static final int ASSIGNMENT = 14;
	public static final int IF = 15;
	public static final int NULL = 16;
	public static final int RETURN = 17;
	public static final int ARRAY_LITERAL = 18;

	private int type;
	private SourceLoc location;

	public Node(int type, SourceLoc location) {
		this.type = type;
		this.location = location;
	}

	public int getType() {
		return type;
	}

	public SourceLoc getLocation() {
		return location;
	}
}
