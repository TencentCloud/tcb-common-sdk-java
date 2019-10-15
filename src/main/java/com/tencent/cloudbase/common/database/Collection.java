package com.tencent.cloudbase.common.database;


import com.alibaba.fastjson.JSONObject;
import com.tencent.cloudbase.common.Result;
import com.tencent.cloudbase.common.bean.DocAddData;
import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.utils.TcbListener;


public class Collection extends Query {

    /**
     * 初始化
     *
     * @param db
     * @param collName
     */
    public Collection(Db db, String collName) {
        super(db, collName);
    }

    public Document doc() {
        return new Document(this.db, this.collName, null);
    }

    public Document doc(String docID) {
        return new Document(this.db, this.collName, docID);
    }

    public Result<DocAddData> add(JSONObject data) throws TcbException {
        Document document = this.doc();
        return document.create(data);
    }

    public void addAsync(final JSONObject data, final TcbListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Result res = doc().create(data);
                    listener.onSuccess(res);
                } catch (TcbException e) {
                    listener.onFailed(e);
                }
            }
        }).start();
    }
}
