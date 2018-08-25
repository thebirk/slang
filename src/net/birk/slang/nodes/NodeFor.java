package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;
import net.birk.slang.Token;

public class NodeFor extends Node {

	private String firstName;
	private String secondName;
	private Node expr;
	private NodeBlock block;

	public NodeFor(Token ident, Token secondName, Node expr, NodeBlock block, SourceLoc location) {
		super(Node.Type.FOR, location);
		this.firstName = ident.getLexeme();
		if(secondName != null) {
			this.secondName = secondName.getLexeme();
		}
		this.expr = expr;
		this.block = block;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public Node getExpr() {
		return expr;
	}

	public NodeBlock getBlock() {
		return block;
	}

}
