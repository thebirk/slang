package net.birk.slang;

public class SourceLoc {

	private String file;
	private int line, offset;

	public SourceLoc(String file, int line, int offset) {
		this.file = file;
		this.line = line;
		this.offset = offset;
	}

	public String getFileName() {
		return file;
	}

	public int getLine() {
		return line;
	}

	public int getOffset() {
		return offset;
	}

	@Override
	public String toString() {
		return "" + file + "(" + line + ":" + offset + ")";
	}

}
