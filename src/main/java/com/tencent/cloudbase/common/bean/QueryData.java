package com.tencent.cloudbase.common.bean;

import com.alibaba.fastjson.JSONArray;
import com.tencent.cloudbase.common.database.FormatUtil;
import com.tencent.cloudbase.common.exception.TcbException;

public class QueryData {

    private JSONArray list;
    private long total;
    private long limit;
    private long offset;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public JSONArray getList() {
        return list;
    }

    public void setList(JSONArray list) throws TcbException {
        this.list = FormatUtil.formatResDocumentData(list);
    }
}
