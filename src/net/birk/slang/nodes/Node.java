package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

public class Node {

	public enum Type {
		NUMBER,
		STRING,
		BINARY,
		TRUE,
		FALSE,
		UNARY,
		INDEX,
		CALL,
		IDENT,
		FIELD,
		FUNC,
		BLOCK,
		VAR,
		ASSIGNMENT,
		IF,
		NULL,
		RETURN,
		ARRAY_LITERAL,
		TABLE_LITERAL,
		WHILE,
		ANON_FUNC,
	}

	private Type type;
	private SourceLoc location;

	public Node(Type type, SourceLoc location) {
		this.type = type;
		this.location = location;
	}

	public Type getType() {
		return type;
	}

	public SourceLoc getLocation() {
		return location;
	}
}
