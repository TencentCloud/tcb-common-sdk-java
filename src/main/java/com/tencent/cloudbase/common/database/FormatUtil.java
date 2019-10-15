package com.tencent.cloudbase.common.database;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.database.geos.*;
import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.utils.ErrorCode;

import java.util.Date;
import java.util.Iterator;


public class FormatUtil {
    public static JSONArray formatResDocumentData(JSONArray documents) throws TcbException {
        JSONArray formatDocuments = new JSONArray();
        try {
            for (int i = 0; i < documents.size(); i++) {
                JSONObject formatDocument = formatField((JSONObject) documents.get(i));
                formatDocuments.add(formatDocument);
            }
        } catch (Exception e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }
        return formatDocuments;
    }

    private static JSONArray formatField(JSONArray document) throws TcbException {
        JSONArray protoField = new JSONArray();

        for (int i = 0; i < document.size(); i++) {
            Object value = document.get(i);
            Object realValue = null;

            String type = whichType(value);

            switch (type) {
                case "GeoPoint": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = Point.fromJson(data);
                    break;
                }
                case "GeoLineString": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = LineString.fromJson(data);
                    break;
                }
                case "GeoPolygon": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = Polygon.fromJson(data);
                    break;
                }
                case "GeoMultiPoint": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = MultiPoint.fromJson(data);
                    break;
                }
                case "GeoMultiLineString": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = MultiLineString.fromJson(data);
                    break;
                }
                case "GeoMultiPolygon": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = MultiPolygon.fromJson(data);
                    break;
                }
                case "Timestamp": {
                    JSONObject jData = (JSONObject) value;
                    realValue = new Date(jData.getLong("$timestamp") * 1000);
                    break;
                }
                case "ServerDate": {
                    JSONObject jData = (JSONObject) value;
                    realValue = new Date(jData.getLong("$date"));
                    break;
                }
                case "JSONArray":
                    realValue = formatField((JSONArray) value);
                    break;
                case "JSONObject":
                    realValue = formatField((JSONObject) value);
                    break;
                default:
                    realValue = value;
            }

            protoField.add(i, realValue);
        }


        return protoField;
    }

    private static JSONObject formatField(JSONObject document) throws TcbException {
        JSONObject protoField = new JSONObject();

        Iterator iterator = document.keySet().iterator();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = document.get(key);
            Object realValue = null;

            String type = whichType(value);

            if(type==null){
                protoField.put(key, realValue);
                continue;
            }

            switch (type) {
                case "GeoPoint": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = Point.fromJson(data);
                    break;
                }
                case "GeoLineString": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = LineString.fromJson(data);
                    break;
                }
                case "GeoPolygon": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = Polygon.fromJson(data);
                    break;
                }
                case "GeoMultiPoint": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = MultiPoint.fromJson(data);
                    break;
                }
                case "GeoMultiLineString": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = MultiLineString.fromJson(data);
                    break;
                }
                case "GeoMultiPolygon": {
                    JSONArray data = ((JSONObject) value).getJSONArray("coordinates");
                    realValue = MultiPolygon.fromJson(data);
                    break;
                }
                case "Timestamp": {
                    JSONObject jData = (JSONObject) value;
                    realValue = new Date(jData.getLong("$timestamp") * 1000);
                    break;
                }
                case "ServerDate": {
                    JSONObject jData = (JSONObject) value;
                    realValue = new Date(jData.getLong("$date"));
                    break;
                }
                case "JSONArray":
                    realValue = formatField((JSONArray) value);
                    break;
                case "JSONObject":
                    realValue = formatField((JSONObject) value);
                    break;
                default:
                    realValue = value;
            }

            protoField.put(key, realValue);
        }


        return protoField;
    }

    public static String whichType(Object data) throws TcbException {

        if(data==null){
            return null;
        }

        if (data instanceof String) {
            return "String";
        }

        if (data instanceof Number) {
            return "Number";
        }

        if (data instanceof JSONArray) {
            return "JSONArray";
        }

        if (data instanceof JSONObject) {
            JSONObject jData = (JSONObject) data;

            if (jData.containsKey("$timestamp")) {
                return "Date";
            }

            if (jData.containsKey("$date")) {
                return "ServerDate";
            }

            if (Point.validate(jData)) {
                return "GeoPoint";
            }

            if (LineString.validate(jData)) {
                return "GeoLineString";
            }

            if (Polygon.validate(jData)) {
                return "GeoPolygon";
            }


            if (MultiPoint.validate(jData)) {
                return "GeoMultiPoint";
            }

            if (MultiLineString.validate(jData)) {
                return "GeoMultiLineString";
            }

            if (MultiPolygon.validate(jData)) {
                return "GeoMultiPolygon";
            }

            return "JSONObject";
        }

        return data.getClass().getName();
    }
}
