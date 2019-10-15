package com.tencent.cloudbase.common.database;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.database.commands.LogicCommand;
import com.tencent.cloudbase.common.database.commands.QueryCommand;
import com.tencent.cloudbase.common.database.commands.UpdateCommand;
import com.tencent.cloudbase.common.database.geos.Point;
import com.tencent.cloudbase.common.utils.Format;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command {
    public QueryCommand eq(Object val) throws TcbException {
        return this.queryOp("$eq", val);
    }

    public QueryCommand neq(Object val) throws TcbException {
        return this.queryOp("$neq", val);
    }

    public QueryCommand gt(Number val) throws TcbException {
        return this.queryOp("$gt", val);
    }

    public QueryCommand gte(Number val) throws TcbException {
        return this.queryOp("$gte", val);
    }

    public QueryCommand lt(Number val) throws TcbException {
        return this.queryOp("$lt", val);
    }

    public QueryCommand lte(Number val) throws TcbException {
        return this.queryOp("$lte", val);
    }

    public QueryCommand in(ArrayList<Object> val) throws TcbException {
        return this.queryOp("$in", val);
    }

    public QueryCommand nin(ArrayList<Object> val) throws TcbException {
        return this.queryOp("$nin", val);
    }

    public QueryCommand geoNear(
            Point point,
            Number maxDistance,
            Number minDistance
    ) throws TcbException {
        JSONObject resultGeometry = new JSONObject();
        try {
            resultGeometry.put("geometry", point);
            if (maxDistance != null) {
                resultGeometry.put("maxDistance", maxDistance);
            }
            if (minDistance != null) {
                resultGeometry.put("minDistance", minDistance);
            }
        } catch (JSONException e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        return this.queryOp("$geoNear", resultGeometry);
    }

    public QueryCommand geoWithin(Object geometry) throws TcbException {
        JSONObject resultGeometry = new JSONObject();
        try {
            resultGeometry.put("geometry", geometry);
        } catch (JSONException e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        return this.queryOp("$geoWithin", resultGeometry);
    }

    public QueryCommand geoIntersects(Object geometry) throws TcbException {
        JSONObject resultGeometry = new JSONObject();
        try {
            resultGeometry.put("geometry", geometry);
        } catch (JSONException e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        return this.queryOp("$geoIntersects", resultGeometry);
    }

    public LogicCommand or(Object... args) throws TcbException {
        Object firstArg = args[0];
        if (firstArg instanceof ArrayList) {
            ArrayList<Object> newArgs = new ArrayList<>();
            for (Object obj : (ArrayList) firstArg) {
                newArgs.add(obj);
            }
            return this.logicOp("$or", newArgs);
        }
        return this.logicOp("$or", new ArrayList<>(Arrays.asList(args)));
    }

    public LogicCommand and(Object... args) throws TcbException {
        Object firstArg = args[0];
        if (firstArg instanceof ArrayList) {
            ArrayList<Object> newArgs = new ArrayList<>();
            for (Object obj : (ArrayList) firstArg) {
                newArgs.add(obj);
            }
            return this.logicOp("$and", newArgs);
        }
        return this.logicOp("$and", new ArrayList<>(Arrays.asList(args)));
    }

    public UpdateCommand set(Object val) throws TcbException {
        return this.updateOp("$set", val);
    }

    public UpdateCommand remove() throws TcbException {
        return this.updateOp("$remove", null);
    }

    public UpdateCommand inc(Number val) throws TcbException {
        return this.updateOp("$inc", val);
    }

    public UpdateCommand mul(Number val) throws TcbException {
        return this.updateOp("$mul", val);
    }

    public UpdateCommand push(List<Object> val) throws TcbException {
        return this.updateOp("$push", val);
    }

    public UpdateCommand pop() throws TcbException {
        return this.updateOp("$pop", null);
    }

    public UpdateCommand shift() throws TcbException {
        return this.updateOp("$shift", null);
    }

    public UpdateCommand unshift(ArrayList<Object> val) throws TcbException {
        return this.updateOp("$unshift", val);
    }

    private QueryCommand queryOp(String operation, Object val) throws TcbException {
        // 格式化
        try {
            val = Format.dataFormat(val);
        } catch (JSONException e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        ArrayList<Object> step = new ArrayList<>();
        step.add(operation);
        step.add(val);
        return new QueryCommand(new ArrayList<ArrayList<Object>>(), step);
    }

    // commands 为 JSONObject 或 LogicCommand
    private LogicCommand logicOp(String operation, List<Object> commands) throws TcbException {
        // 格式化
        List<JSONObject> formatCommands = new ArrayList<>();
        try {
            formatCommands = Format.dataFormat(commands);
        } catch (JSONException e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        ArrayList<Object> step = new ArrayList<>();
        step.add(operation);
        step.addAll(formatCommands);
        return new LogicCommand(new ArrayList<ArrayList<Object>>(), step);
    }

    private UpdateCommand updateOp(String operation, Object val) throws TcbException {
        // 格式化
        try {
            if (val != null) {
                val = Format.dataFormat(val);
            }
        } catch (JSONException e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        List<Object> step = new ArrayList<>();
        step.add(operation);
        if (val != null) {
            step.add(val);
        }
        return new UpdateCommand(new ArrayList<>(), step);
    }

}
