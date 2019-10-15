package com.tencent.cloudbase.common.database.geos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.exception.TcbException;


import java.util.ArrayList;
import java.util.List;

/**
 * 地理位置
 */
public class Polygon extends GeoListString<LineString>{

    public Polygon(List<LineString> lines) throws TcbException {
        if (lines.size() == 0) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "Polygon must contain 1 linestring at least");
        }

        for (LineString line : lines) {
            if (!line.isClosed()) {
                StringBuilder sb = new StringBuilder();
                for (Point point : line.list) {
                    sb.append(point.toReadableString()).append(" ");
                }
                throw new TcbException(ErrorCode.INVALID_PARAM, "LineString " + sb.toString() + "is not a closed cycle");
            }
        }

        this.list = lines;
    }

    public static boolean validate(JSONObject json) throws TcbException{
        return GeoString.validate(json, Polygon.class);
    }

    public static Polygon fromJson(JSONArray object) throws TcbException{
        List<LineString> lines = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            lines.add(LineString.fromJson(object.getJSONArray(i)));
        }
        return new Polygon(lines);
    }

    public static boolean validateCoordinates(JSONArray object){

        if(object==null){
            return false;
        }

        for(int i=0;i<object.size();i++){
            try{
                boolean single = LineString.validateCoordinates(object.getJSONArray(i));
                if(!single){
                    return false;
                }
            }catch (Exception e){
                return false;
            }

        }

        return true;
    }
}
