package com.tencent.cloudbase.common.database.geos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.exception.TcbException;

import java.util.ArrayList;
import java.util.List;

public class MultiPoint extends GeoListString<Point>{

    public MultiPoint(List<Point> points) throws TcbException{
        if (points.size() == 0) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "points must contain 1 point at least");
        }

        this.list = points;
    }

    public static MultiPoint fromJson(JSONArray object) throws TcbException{
        List<Point> points = new ArrayList<>();
        for(int i =0;i < object.size(); i ++) {
            points.add(Point.fromJson(object.getJSONArray(i)));
        }
        return new MultiPoint(points);
    }

    public static boolean validate(JSONObject json) throws TcbException{
        return GeoString.validate(json, MultiPoint.class);
    }
}
