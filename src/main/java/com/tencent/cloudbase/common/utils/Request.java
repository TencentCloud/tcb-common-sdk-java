package com.tencent.cloudbase.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.exception.TcbException;

public abstract class Request {

    private static final String DATA_VERSION = "2019-06-01";

    public JSONObject sendMidData(String action,JSONObject params) throws TcbException{
//        params.put("dataVersion",DATA_VERSION);
//        params.put("databaseMidTran",true);
        return JSONObject.parseObject(sendMidDataString(action,params));
    }

    public JSONObject send(String action,JSONObject params) throws TcbException{
        return JSONObject.parseObject(sendString(action,params));
    }


    public String sendMidDataString(String action,JSONObject params) throws TcbException{
        params.put("dataVersion",DATA_VERSION);
        params.put("databaseMidTran",true);
        return sendString(action,params);
    }

    public abstract String sendString(String action,JSONObject params) throws TcbException;
}
