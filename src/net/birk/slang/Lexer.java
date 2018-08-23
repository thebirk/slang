package net.birk.slang;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Lexer {

	private String path;
	private int line, offset;
	private BufferedReader fr;
	public ArrayList<Token> tokens;

	public Lexer(String path) throws FileNotFoundException {
		this.path = path;
		//fr = new BufferedReader(new FileReader(path));
		fr = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
		tokens = new ArrayList<Token>();
		line = 1;
		offset = 1;
	}

	private Token makeSimpleToken(int c) {
		offset++;
		return new Token(c, Character.toString((char)c), new SourceLoc(path, line, offset-1));
	}

	private Token readToken() throws IOException {
		int c = fr.read();

		if(c == ' ' || c == '\t' || c == '\r') {
			offset++;
			return readToken();
		}

		if(c == '\n') {
			line++;
			offset = 1;
			return readToken();
		}

		if(c == '"') {
			StringBuilder builder = new StringBuilder();
			c = fr.read();

			while((c != '"') && fr.ready()) {
				builder.append((char) c);
				c = fr.read();
			}

			Token t = new Token(Token.STRING, builder.toString(), new SourceLoc(path, line, offset));
			offset += builder.length();
			return t;
		}

		if(Character.isDigit(c)) {
			StringBuilder builder = new StringBuilder();
			boolean foundDot = false;
			while((Character.isDigit(c) || c == '.') && c != -1) {
				if(c == '.') {
					if(foundDot) {
						break;
					} else {
						foundDot = true;
					}
				}
				builder.append((char) c);
				fr.mark(1);
				c = fr.read();
			}
			fr.reset();

			Token t = new Token(Token.NUMBER, builder.toString(), new SourceLoc(path, line, offset));
			offset += builder.length();
			return t;
		}

		if(Character.isAlphabetic(c) || c == '_') {
			StringBuilder builder = new StringBuilder();
			while((Character.isAlphabetic(c) || Character.isDigit(c) || c == '_') && fr.ready()) {
				builder.append((char) c);
				fr.mark(1);
				c = fr.read();
			}
			if(!fr.ready()) {
				builder.append((char) c);
			} else {
				fr.reset();
			}

			Token t = new Token(Token.IDENT, builder.toString(), new SourceLoc(path, line, offset));
			offset += builder.length();
			return t;
		}

		//TODO: Double tokens
		fr.mark(1);
		int c2 = fr.read();

		if(c == '/' && c2 == '/') {
			do {
				c = fr.read();
			} while(c != '\n' && c != -1);
			offset = 1;
			line++;
			return readToken();
		}
		else if(c == '=' && c2 == '=') {
			offset += 2;
			return new Token(Token.EQUALS, "==", new SourceLoc(path, line, offset - 2));
		}
		else if(c == '!' && c2 == '=') {
			offset += 2;
			return new Token(Token.NE, "!=", new SourceLoc(path, line, offset - 2));
		}
		else if(c == '<' && c2 == '=') {
			offset += 2;
			return new Token(Token.LTE, "<=", new SourceLoc(path, line, offset - 2));
		}
		else if(c == '>' && c2 == '=') {
			offset += 2;
			return new Token(Token.GTE, ">=", new SourceLoc(path, line, offset - 2));
		}
		else if(c == '+' && c2 == '=') {
			offset += 2;
			return new Token(Token.PLUS_EQUALS, "+=", new SourceLoc(path, line, offset - 2));
		}
		else if(c == '-' && c2 == '=') {
			offset += 2;
			return new Token(Token.MINUS_EQUALS, "-=", new SourceLoc(path, line, offset - 2));
		}
		else {
			fr.reset();
		}

		switch (c) {
			case '=': return makeSimpleToken(c);
			case '!': return makeSimpleToken(c);

			case '<': return makeSimpleToken(c);
			case '>': return makeSimpleToken(c);

			case '*': return makeSimpleToken(c);
			case '/': return makeSimpleToken(c);
			case '+': return makeSimpleToken(c);
			case '-': return makeSimpleToken(c);
			case '%': return makeSimpleToken(c);

			case '.': return makeSimpleToken(c);
			case ',': return makeSimpleToken(c);
			case ':': return makeSimpleToken(c);
			case ';': return makeSimpleToken(c);

			case '(': return makeSimpleToken(c);
			case ')': return makeSimpleToken(c);
			case '{': return makeSimpleToken(c);
			case '}': return makeSimpleToken(c);
			case '[': return makeSimpleToken(c);
			case ']': return makeSimpleToken(c);
		}

		offset++;
		return new Token(Token.UNKNOWN, "", new SourceLoc(path, line, offset));
	}

	public void lex() throws IOException {
		while(fr.ready()) {
			tokens.add(readToken());
		}
		tokens.add(new Token(Token.EOF, "EOF", new SourceLoc(path, line, offset)));
		fr.close();

		for(int i = 0; i < tokens.size(); i++) {
			Token t = tokens.get(i);
			if(t.getType() == Token.IDENT) {
				switch (t.getLexeme()) {
					case "fn": tokens.set(i, new Token(Token.FUNC, "fn", t.getSourceLoc())); break;
					case "var": tokens.set(i, new Token(Token.VAR, "var", t.getSourceLoc())); break;
					case "true": tokens.set(i, new Token(Token.TRUE, "true", t.getSourceLoc())); break;
					case "false": tokens.set(i, new Token(Token.FALSE, "false", t.getSourceLoc())); break;
					case "do": tokens.set(i, new Token(Token.DO, "do", t.getSourceLoc())); break;
					case "if": tokens.set(i, new Token(Token.IF, "if", t.getSourceLoc())); break;
					case "null": tokens.set(i, new Token(Token.NULL, "null", t.getSourceLoc())); break;
					case "return": tokens.set(i, new Token(Token.RETURN, "return", t.getSourceLoc())); break;
					case "else": tokens.set(i, new Token(Token.ELSE, "else", t.getSourceLoc())); break;
					case "while": tokens.set(i, new Token(Token.WHILE, "while", t.getSourceLoc())); break;
				}
			}
		}
	}

	public void printTokens() {
		for(Token t : tokens) {
			System.out.println(Token.typeToString(t.getType()) + ": '" + t.getLexeme() + "' :" + t.getSourceLoc());
		}
	}

}
