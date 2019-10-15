package com.tencent.cloudbase.common.database.geos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.exception.TcbException;


import java.util.ArrayList;

public class MultiLineString extends GeoListString<LineString>{

    public MultiLineString(ArrayList<LineString> lines) throws TcbException{

        if (lines.size() == 0) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "Polygon must contain 1 linestring at least");
        }

        this.list = lines;
    }

    public static MultiLineString fromJson(JSONArray object) throws TcbException {
        ArrayList<LineString> lines = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            lines.add(LineString.fromJson(object.getJSONArray(i)));
        }
        return new MultiLineString(lines);
    }

    public static boolean validate(JSONObject json) throws TcbException{
        return GeoString.validate(json, MultiLineString.class);
    }

}
