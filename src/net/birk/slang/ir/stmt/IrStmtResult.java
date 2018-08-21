package net.birk.slang.ir.stmt;

import net.birk.slang.ir.value.IrValue;

public class IrStmtResult {

	public static final int NORMAL = 0;
	public static final int RETURN = 1;

	private int status;
	private IrValue value;

	public IrStmtResult(int status, IrValue value) {
		this.status = status;
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public IrValue getValue() {
		return value;
	}

}
