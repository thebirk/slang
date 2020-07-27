package net.birk.slang.ir.stmt;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.IrException;
import net.birk.slang.ir.IrScope;
import net.birk.slang.ir.value.IrFunc;
import net.birk.slang.ir.value.IrIdent;
import net.birk.slang.ir.value.IrTable;
import net.birk.slang.ir.value.IrValue;

import java.util.ArrayList;

public class IrSelfCallStmt extends IrStmt {

    private IrValue expr;
    private IrValue ident;
    private ArrayList<IrValue> args;

    public IrSelfCallStmt(IrValue expr, IrValue ident, ArrayList<IrValue> args, SourceLoc location) {
        super(IrStmt.Type.CALL, location);
        this.expr = expr;
        this.ident = ident;
        this.args = args;
    }

    @Override
    public IrStmtResult eval(IrScope scope) {
        IrValue expr = this.expr.eval(scope);
        if(expr.getType() != IrValue.Type.TABLE) {
            throw new IrException(getLocation(), "Cannot use ':' operator with non-table values!");
        }

        IrTable t = (IrTable) expr;
        IrValue func = t.getMap().get(ident);

        if(func.getType() != IrValue.Type.FUNC) {
            throw new IrException(getLocation(), "Right hand side of ':' is not a function!!");
        }

        ArrayList<IrValue> finalArgs = new ArrayList<IrValue>();
        finalArgs.add(expr);
        for(IrValue v : args) {
            finalArgs.add(v.eval(scope));
        }
        func = func.eval(scope);

        ((IrFunc)func).call(scope, finalArgs);

        return new IrStmtResult(IrStmtResult.NORMAL, null);
    }
}
