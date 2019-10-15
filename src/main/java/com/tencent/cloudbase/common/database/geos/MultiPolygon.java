package com.tencent.cloudbase.common.database.geos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.exception.TcbException;

import java.util.ArrayList;

public class MultiPolygon extends GeoListString<Polygon>{

    public MultiPolygon(ArrayList<Polygon> polygons) throws TcbException{
        if (polygons.size() == 0) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "MultiPolygon must contain 1 polygon at least");
        }

        this.list = polygons;
    }

    public static MultiPolygon fromJson(JSONArray object) throws TcbException{
        ArrayList<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            polygons.add(Polygon.fromJson(object.getJSONArray(i)));
        }
        return new MultiPolygon(polygons);
    }

    public static boolean validate(JSONObject json) throws TcbException{
        return GeoString.validate(json, MultiPolygon.class);
    }
}
