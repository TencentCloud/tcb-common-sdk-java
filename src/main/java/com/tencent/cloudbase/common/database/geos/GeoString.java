package com.tencent.cloudbase.common.database.geos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.exception.TcbException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class GeoString {

    protected String type;

    protected JSONArray coordinates = new JSONArray();

    private boolean builed = false;

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("type", getType());
        result.put("coordinates", getCoordinates());
        return result;
    }

    protected abstract void buildCoordinates();

    public static boolean validate(JSONObject object,Class clazz) throws TcbException{

        if (!object.containsKey("type") || !object.containsKey("coordinates")) {
            return false;
        }

        if (!clazz.getSimpleName().equals(object.get("type"))) {
            return false;
        }

        JSONArray array = object.getJSONArray("coordinates");
        if(array==null){
            return false;
        }

        try{
            Class tClazz = getTClass(clazz);
            Method method = tClazz.getDeclaredMethod("validateCoordinates", JSONArray.class);
            method.setAccessible(true);
            if(!GeoListString.class.isAssignableFrom(clazz)){
                return (boolean) method.invoke(tClazz,array);
            }
            for(int i=0;i<array.size();i++){
                Object obj = array.get(i);
                boolean res = (boolean) method.invoke(tClazz,obj);
                if(!res){
                    return false;
                }
            }
        }catch (Exception e){
            throw new TcbException(ErrorCode.INVOKE_ERR,e.getMessage());
        }

        return true;
    }

    public static Class<? extends GeoString> getTClass(Class clazz) throws Exception {
        Class cla = null;
        try {
            ParameterizedType type = (ParameterizedType) clazz.getAnnotatedSuperclass().getType();
            cla = (Class) type.getActualTypeArguments()[0];
        }catch (Exception e){
            cla = clazz;
        }
        return cla;
    }


    public String getType() {
        return this.getClass().getSimpleName();
    }

    public JSONArray getCoordinates(){
        if(builed){
            return coordinates;
        }
        buildCoordinates();
        builed = true;
        return coordinates;
    }
}
