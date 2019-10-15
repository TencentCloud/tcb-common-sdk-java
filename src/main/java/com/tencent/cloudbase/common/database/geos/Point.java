package com.tencent.cloudbase.common.database.geos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.utils.Validate;
import com.tencent.cloudbase.common.exception.TcbException;

/**
 * 地理位置
 */
public class Point extends GeoString{
    /**
     * 经度
     * [-90, 90]
     */
    private double longitude;

    /**
     * 纬度
     * [-180, 180]
     */
    private double latitude;

    public Point(double longitude, double latitude) throws TcbException {
        Validate.isGeopoint("longitude", longitude);
        Validate.isGeopoint("latitude", latitude);

        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    protected void buildCoordinates(){
        coordinates.add(longitude);
        coordinates.add(latitude);
    }

    public static boolean validate(JSONObject json) throws TcbException{
        return GeoString.validate(json, Point.class);
    }

    public static Point fromJson(JSONArray object) throws TcbException{
        return new Point(object.getDouble(0), object.getDouble(1));
    }

    public static boolean validateCoordinates(JSONArray object){
        try {
            if (!Validate.isGeopoint("longitude", object.getDouble(0))
                    || !Validate.isGeopoint("latitude", object.getDouble(1))) {
                return false;
            }
        }catch (Exception e){
            return false;
        }

        return true;
    }

    public String toReadableString() {
        return "[ " + this.longitude + " " + this.latitude + " ]";
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

}
