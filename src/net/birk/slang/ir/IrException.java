package net.birk.slang.ir;

import net.birk.slang.SourceLoc;

public class IrException extends RuntimeException {

	public IrException(SourceLoc location, String msg) {
		super(location + ": " + msg);
	}

}
