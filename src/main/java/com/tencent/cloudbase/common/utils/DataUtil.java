package com.tencent.cloudbase.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencent.cloudbase.common.Result;
import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.exception.TcbRemoteException;

public class DataUtil {

    public static <T> Result<T> buildResult(String res, TypeReference<Result<T>> type) throws TcbException {
        JSONObject json = JSONObject.parseObject(res);
        if(json.containsKey("code")){
            TcbRemoteException remoteException = new TcbRemoteException(json.getString("code"),json.getString("message"),json.getString("stack"));
            throw remoteException;
        }

        Result result = JSONObject.parseObject(res,type);

        return result;
    }
}
