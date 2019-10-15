package com.tencent.cloudbase.common.database.commands;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.utils.Format;
import com.tencent.cloudbase.common.exception.TcbException;


import java.util.ArrayList;
import java.util.List;

public class LogicCommand {
    public ArrayList<ArrayList<Object>> actions;

    public LogicCommand(ArrayList<ArrayList<Object>> actions,ArrayList<Object> step) {
        this.actions = actions;
        if (step.size() > 0) {
            this.actions.add(step);
        }
    }

    public JSONObject toJSON()  {
        JSONArray actionArr = new JSONArray();
        JSONObject actionMap = new JSONObject();
        actionMap.put("_actions", actions);

        return actionMap;
    }

    public LogicCommand or(Object... args) throws TcbException {
        List<Object> list = new ArrayList<>();
        for(Object obj : args){
            list.add(obj);
        }
        return this.logicOp("$or", list);
    }

    public LogicCommand or (List<Object> args) throws TcbException {
        return this.logicOp("$or", args);
    }

    public LogicCommand and(Object... args) throws TcbException {
        List<Object> list = new ArrayList<>();
        for(Object obj : args){
            list.add(obj);
        }
        return this.logicOp("$and", list);
    }

    public LogicCommand and (ArrayList<Object> args) throws TcbException {
        return this.logicOp("$and", args);
    }

    private LogicCommand logicOp(String operation,List<Object> commands) throws TcbException{
        // 格式化
        List<JSONObject> formatCommands = new ArrayList<>();
        try {
            formatCommands = Format.dataFormat(commands);
        } catch (Exception e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        ArrayList<Object> step = new ArrayList<>();
        step.add(operation);
        step.addAll(formatCommands);
        return new LogicCommand(this.actions, step);
    }
}
