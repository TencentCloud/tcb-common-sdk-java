package com.tencent.cloudbase.common.database.geos;

import java.util.List;

public abstract class GeoListString<T extends GeoString> extends GeoString{

    protected List<T> list;

    @Override
    protected void buildCoordinates() {
        for (T t : list) {
            coordinates.add(t.getCoordinates());
        }
    }

    public List<T> getList() {
        return list;
    }


}
