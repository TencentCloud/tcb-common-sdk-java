package com.tencent.cloudbase.common.database.geos;

import com.tencent.cloudbase.common.exception.TcbException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GeoStringTest {

    @Test
    public void testValidate() throws TcbException {
        Point p = new Point(1,2);
        Point p2 = new Point(3,4);
        List<Point> list = new ArrayList<>();
        list.add(p);
        list.add(p2);


        LineString lineString = new LineString(list);


        Assert.assertTrue(LineString.validate(lineString.toJSON()));
        Assert.assertTrue(Point.validate(p.toJSON()));

    }

}
