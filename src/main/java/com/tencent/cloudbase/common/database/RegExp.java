package com.tencent.cloudbase.common.database;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.utils.ErrorCode;


public class RegExp {
    private String regexp;
    private String options;

    public RegExp(String regexp, String options) throws TcbException {
        if (regexp==null || regexp.isEmpty()) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "regexp must be a string");
        }

        this.regexp = regexp;
        this.options = options;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("$regex", regexp);
        data.put("$options", options);
        return data;
    }
}
