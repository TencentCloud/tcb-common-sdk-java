package com.tencent.cloudbase.common;


import com.alibaba.fastjson.JSONObject;

public class Test {

    public static void main(String args[]) throws Exception {

        JSONObject object = new JSONObject();
        object.put("total",10);




        System.err.println(object);
        System.err.println("databaseMidTran".compareTo("dataVersion"));
        System.err.println("databaseMidTran".compareTo("dataversion"));
    }
}
