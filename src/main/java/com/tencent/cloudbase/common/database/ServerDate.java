package com.tencent.cloudbase.common.database;

import com.alibaba.fastjson.JSONObject;

public class ServerDate {
    public int offset;

    public ServerDate(int offset) {
        this.offset = offset;
    }

    public JSONObject parse() {
        JSONObject date = new JSONObject();
        date.put("offset", this.offset);

        JSONObject result = new JSONObject();
        result.put("$date", date);

        return result;
    }
}
