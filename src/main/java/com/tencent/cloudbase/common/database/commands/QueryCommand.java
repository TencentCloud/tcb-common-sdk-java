package com.tencent.cloudbase.common.database.commands;



import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.utils.Format;
import com.tencent.cloudbase.common.exception.TcbException;


import java.util.ArrayList;

public class QueryCommand extends LogicCommand{
    public QueryCommand(ArrayList<ArrayList<Object>> actions,ArrayList<Object> step) {
        super(actions, step);
    }

    public LogicCommand eq(Object val) throws TcbException {
        return this.queryOp("$eq", val);
    }

    public LogicCommand neq(Object val) throws TcbException {
        return this.queryOp("$neq", val);
    }

    public LogicCommand gt(Number val) throws TcbException {
        return this.queryOp("$gt", val);
    }

    public LogicCommand gte(Number val) throws TcbException {
        return this.queryOp("$gte", val);
    }

    public LogicCommand lt(Number val) throws TcbException {
        return this.queryOp("$lt", val);
    }

    public LogicCommand lte(Number val) throws TcbException {
        return this.queryOp("$lte", val);
    }

    public LogicCommand in(ArrayList<String> val) throws TcbException {
        return this.queryOp("$in", val);
    }

    public LogicCommand nin(ArrayList<Number> val) throws TcbException {
        return this.queryOp("$nin", val);
    }

    private LogicCommand queryOp(String operation, Object val) throws TcbException {
        // 格式化
        try {
            if (val != null) {
                val = Format.dataFormat(val);
            }
        } catch (Exception e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        ArrayList<Object> step = new ArrayList<>();
        step.add(operation);
        step.add(val);
        return this.and(new QueryCommand(new ArrayList<ArrayList<Object>>(), step));
    }


}
