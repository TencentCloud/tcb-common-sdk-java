package com.tencent.cloudbase.common.database.geos;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.exception.TcbException;

import java.util.ArrayList;
import java.util.List;

public class LineString extends GeoListString<Point>{


    public LineString(List<Point> points) throws TcbException{

        if (points.size() < 2) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "points must contain 2 points at least");
        }

        this.list = points;
    }


    public static boolean validate(JSONObject json) throws TcbException{
        return GeoString.validate(json, LineString.class);
    }

    public static LineString fromJson(JSONArray object) throws TcbException{
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            points.add(Point.fromJson(object.getJSONArray(i)));
        }
        return new LineString(points);
    }

    public static boolean validateCoordinates(JSONArray object){

        if(object==null){
            return false;
        }

        for(int i=0;i<object.size();i++){
            try{
                boolean single = Point.validateCoordinates(object.getJSONArray(i));
                if(!single){
                    return false;
                }
            }catch (Exception e){
                return false;
            }
        }

        return true;
    }

    public boolean isClosed() {
        Point firstPoint = (Point) list.get(0);
        Point lastPoint = (Point) list.get(list.size() - 1);

        return  firstPoint.getLatitude() == lastPoint.getLatitude() && firstPoint.getLongitude() == lastPoint.getLongitude();
    }
}
