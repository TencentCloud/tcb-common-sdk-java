package com.tencent.cloudbase.common.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.database.commands.LogicCommand;
import com.tencent.cloudbase.common.database.commands.UpdateCommand;
import com.tencent.cloudbase.common.database.geos.LineString;
import com.tencent.cloudbase.common.database.geos.MultiLineString;
import com.tencent.cloudbase.common.database.geos.MultiPoint;
import com.tencent.cloudbase.common.database.geos.MultiPolygon;
import com.tencent.cloudbase.common.database.geos.Point;
import com.tencent.cloudbase.common.database.geos.Polygon;
import com.tencent.cloudbase.common.database.RegExp;
import com.tencent.cloudbase.common.database.ServerDate;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Format {

    public static Object dataFormat( Object data) throws TcbException {
        if (data instanceof LogicCommand) {
            return ((LogicCommand) data).toJSON();
        }

        if (data instanceof UpdateCommand) {
            return ((UpdateCommand) data).toJSON();
        }

        if (data instanceof ServerDate) {
            return ((ServerDate) data).parse();
        }

        if (data instanceof Date) {
            JSONObject result = new JSONObject();
            result.put("$date", ((Date) data).getTime());
            return result;
        }

        if (data instanceof RegExp) {
            return ((RegExp) data).toJSON();
        }

        if (data instanceof Point) {
            return ((Point) data).toJSON();
        }

        if (data instanceof LineString) {
            return ((LineString) data).toJSON();
        }

        if (data instanceof Polygon) {
            return ((Polygon) data).toJSON();
        }

        if (data instanceof MultiPoint) {
            return ((MultiPoint) data).toJSON();
        }

        if (data instanceof MultiLineString) {
            return ((MultiLineString) data).toJSON();
        }

        if (data instanceof MultiPolygon) {
            return ((MultiPolygon) data).toJSON();
        }

        if (data instanceof JSONObject) {
            return Format.dataFormat((JSONObject) data);
        }

        if(data instanceof JSONArray){
            return dataFormat((JSONArray) data);
        }

//        if(data instanceof List){
//            return Format.dataFormat((List) data);
//        }

        return data;
    }

    public static JSONObject dataFormat(JSONObject data) throws TcbException{
        JSONObject cloneData = new JSONObject();
        Iterator iterator = data.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = data.get(key);
            cloneData.put(key, Format.dataFormat(value));
        }
        return cloneData;
    }

    public static List<JSONObject> dataFormat(List<Object> data) throws  TcbException {
        ArrayList<JSONObject> cloneData = new ArrayList<>();
        for (Object cmd : data) {
            if (cmd instanceof JSONObject) {
                cloneData.add((JSONObject) dataFormat(cmd));
            } else if (cmd instanceof LogicCommand) {
                cloneData.add((JSONObject) dataFormat(cmd));
            } else {
                throw new TcbException("TYPE_ERROR", "操作符类型只能为 JSONObject 或 LogicCommand");
            }
        }
        return cloneData;
    }

    public static JSONArray dataFormat(JSONArray data) throws  TcbException {
        JSONArray arr = new JSONArray();
        for(int i=0;i<((JSONArray) data).size();i++){
            arr.add(dataFormat(data.get(i)));
        }
        return arr;
    }

    // 兼容 double float .0
    public static JSONObject numberFormat(JSONObject data){
        JSONObject cloneData = new JSONObject();
        Iterator iterator = data.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = data.get(key);
            cloneData.put(key, Format.numberFormatObj(value));
        }
        return cloneData;
    }

    public static Object numberFormatObj(Object obj){
        if(obj instanceof BigDecimal){
            obj = ((BigDecimal) obj).doubleValue();
        }
        if(obj instanceof  Double && (Double)obj%1==0){
            return  ((Number) obj).intValue();
        }
        if(obj instanceof  Float && (Float)obj%1==0){
            return ((Number) obj).intValue();
        }

        if(obj instanceof JSONArray){
            JSONArray arr = new JSONArray();
            for(int i=0;i<((JSONArray) obj).size();i++){
                arr.add(numberFormatObj(((JSONArray) obj).get(i)));
            }
            return arr;
        }

        if(obj instanceof List){
            JSONArray arr = new JSONArray();
            for(int i=0;i<((List) obj).size();i++){
                arr.add(numberFormatObj(((List) obj).get(i)));
            }
            return arr;
        }

        if(obj instanceof JSONObject){
            return numberFormat((JSONObject)obj);
        }

        return obj;
    }

}
