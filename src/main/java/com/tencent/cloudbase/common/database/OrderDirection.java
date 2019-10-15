package com.tencent.cloudbase.common.database;

public enum  OrderDirection {

    DESC("desc"),
    ASC("asc");

    private String direction;

    OrderDirection(String direction){
        this.direction = direction;
    }


    public String getDirection() {
        return direction;
    }

}
