package net.birk.slang.nodes;

import net.birk.slang.SourceLoc;

import java.util.ArrayList;

public class NodeTableLiteral extends Node {

	public static final int IDENT_ENTRY = 0;
	public static final int INDEX_ENTRY = 1;

	public static class Entry {

		private Node expr;
		private Node identOrIndex;
		private int type;

		public Entry(int type, Node expr, Node identOrIndex) {
			this.type = type;
			this.expr = expr;
			this.identOrIndex = identOrIndex;
		}

		public Node getExpr() {
			return expr;
		}

		public Node getIdentOrIndex() {
			return identOrIndex;
		}

		public int getType() {
			return type;
		}

	}

	private ArrayList<Entry> entries;

	public NodeTableLiteral(ArrayList<Entry> entries, SourceLoc location) {
		super(Node.Type.TABLE_LITERAL, location);
		this.entries = entries;
	}

	public ArrayList<Entry> getEntries() {
		return entries;
	}

}
