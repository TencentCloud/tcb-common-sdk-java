package com.tencent.cloudbase.common.database;

import com.tencent.cloudbase.common.database.geos.LineString;
import com.tencent.cloudbase.common.database.geos.MultiLineString;
import com.tencent.cloudbase.common.database.geos.MultiPoint;
import com.tencent.cloudbase.common.database.geos.MultiPolygon;
import com.tencent.cloudbase.common.database.geos.Point;
import com.tencent.cloudbase.common.database.geos.Polygon;
import com.tencent.cloudbase.common.exception.TcbException;

import java.util.ArrayList;

public class Geo {

    public Point point(double longitude, double latitude) throws TcbException {
        return new Point(longitude, latitude);
    }

    public MultiPoint multiPoint(ArrayList<Point> points) throws TcbException {
        return new MultiPoint(points);
    }

    public LineString lineString(ArrayList<Point> points) throws TcbException {
        return new LineString(points);
    }

    public MultiLineString multiLineString(ArrayList<LineString> lines) throws TcbException {
        return new MultiLineString(lines);
    }

    public Polygon polygon (ArrayList<LineString> lines) throws TcbException {
        return new Polygon(lines);
    }

    public MultiPolygon multiPolygon (ArrayList<Polygon> polygons) throws TcbException {
        return new MultiPolygon(polygons);
    }
}
