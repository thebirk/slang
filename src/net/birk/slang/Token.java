package net.birk.slang;

public class Token {

	public static final int IDENT = 128;
	public static final int STRING = 129;
	public static final int NUMBER = 130;
	public static final int VAR = 131;
	public static final int FUNC = 132;
	public static final int UNKNOWN = 133;
	public static final int EOF = 134;
	public static final int TRUE = 135;
	public static final int FALSE = 136;
	public static final int EQUALS = 137;
	public static final int NE = 138;
	public static final int GTE = 139;
	public static final int LTE = 140;
	public static final int DO = 141;
	public static final int PLUS_EQUALS = 142;
	public static final int MINUS_EQUALS = 143;
	public static final int IF = 144;
	public static final int NULL = 145;
	public static final int RETURN = 146;
	public static final int ELSE = 147;
	public static final int WHILE = 148;
	public static final int FOR = 149;
	public static final int IN = 150;
	public static final int LAND = 151;
	public static final int LOR = 152;

	private int type;
	private String lexeme;
	private SourceLoc location;

	public Token(int type, String lexeme, SourceLoc location) {
		this.type = type;
		this.lexeme = lexeme;
		this.location = location;
	}

	public int getType() {
		return type;
	}
	public String getTypeString() {
		return typeToString(type);
	}

	public String getLexeme() {
		return lexeme;
	}
	public SourceLoc getSourceLoc() {
		return location;
	}

	@Override
	public String toString() {
		return "Token { type: '" + getTypeString() + "', lexeme: '" + lexeme + "', location: " + location + "  }";
	}

	public static String typeToString(int type) {
		if(type >= 32 && type < 127) return Character.toString((char)type);

		switch(type) {
			case IDENT: return "identifier";
			case STRING: return "string";
			case NUMBER: return "number";
			case VAR: return "var";
			case FUNC: return "fn";
			case UNKNOWN: return "Unknown!";
			case EOF: return "EOF";
			case TRUE: return "true";
			case FALSE: return "false";
			case EQUALS: return "==";
			case NE: return "!=";
			case GTE: return ">=";
			case LTE: return "<=";
			case DO: return "do";
			case PLUS_EQUALS: return "+=";
			case MINUS_EQUALS: return "-=";
			case IF: return "if";
			case NULL: return "null";
			case RETURN: return "return";
			case ELSE: return "else";
			case WHILE: return "while";
			case FOR: return "for";
			case IN: return "in";
			case LAND: return "&&";
			case LOR: return "||";
		}

		throw new RuntimeException("Invalid Token type: " + type);
	}

}
