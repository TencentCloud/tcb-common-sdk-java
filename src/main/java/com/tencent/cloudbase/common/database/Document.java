package com.tencent.cloudbase.common.database;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencent.cloudbase.common.Result;
import com.tencent.cloudbase.common.bean.DocAddData;
import com.tencent.cloudbase.common.bean.DocRemoveData;
import com.tencent.cloudbase.common.bean.DocUpdataData;
import com.tencent.cloudbase.common.bean.QueryData;
import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.utils.*;

import java.util.HashMap;
import java.util.Map;

public class Document {

    private Db db;
    private String collName;
    private String id;
    private static Request request;
    private HashMap<String, Number> projection;

    public Document(Db db, String collName,String docID) {
        this(db, collName, docID, new HashMap<String, Number>());
    }

    private Document(Db db, String collName, String docID, HashMap<String, Number> projection) {
        this.db = db;
        this.collName = collName;
        this.id = docID;
        this.projection = projection;
        this.request = db.getRequest();
    }

    /**
     * 创建一篇文档
     *
     * @param data 文档数据
     * @return
     * @throws TcbException
     */
    public Result<DocAddData> create(JSONObject data) throws TcbException {
        // 格式化
        try {
            data = Format.dataFormat(data);
        } catch (Exception e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);
        params.put("data", data);
        if (this.id != null && !this.id.isEmpty()) {
            params.put("_id", this.id);
        }

        String res = this.request.sendMidDataString("database.addDocument", params);


        Result<DocAddData> result = DataUtil.buildResult(res,new TypeReference<Result<DocAddData>>(){});
        return result;
    }

    /**
     * 创建一篇文档（异步）
     *
     * @param listener
     */
    public void createAsync(final JSONObject data, final TcbListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Result res = create(data);
                    listener.onSuccess(res);
                } catch (TcbException e) {
                    listener.onFailed(e);
                }
            }
        }).start();
    }

    /**
     * 创建或添加数据
     *
     * 如果文档ID不存在，则创建该文档并插入数据，根据返回数据的 upserted_id 判断
     * 添加数据的话，根据返回数据的 set 判断影响的行数
     *
     * @param data 文档数据
     * @return
     * @throws TcbException
     */
    public Result<DocUpdataData> set(JSONObject data) throws TcbException {
        if (data.containsKey("_id")) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "不能更新_id的值");
        }

        if (this.id == null || this.id.isEmpty()) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "docId不能为空");
        }

        boolean hasOperator = this.checkOperatorMixed(data);
        // 不能包含操作符
        if (hasOperator) {
            throw new TcbException(ErrorCode.DATABASE_REQUEST_FAILED, "update operator complicit");
        }

        // 格式化
        try {
            data = Format.dataFormat(data);
        } catch (Exception e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);
        params.put("multi", false);
        params.put("merge", false);
        params.put("upsert", true);
        params.put("data", data);
        params.put("interfaceCallSource", "SINGLE_SET_DOC");
        if (this.id != null && !this.id.isEmpty()) {
            params.put("_id", this.id);

            JSONObject query = new JSONObject();
            query.put("_id",this.id);
            params.put("query",query);
        }

        String res = this.request.sendMidDataString("database.updateDocument", params);

        Result<DocUpdataData> result = DataUtil.buildResult(res,new TypeReference<Result<DocUpdataData>>(){});
        return result;
    }

    /**
     * 创建或添加数据（异步）
     *
     * 如果文档ID不存在，则创建该文档并插入数据，根据返回数据的 upserted_id 判断
     * 添加数据的话，根据返回数据的 set 判断影响的行数
     *
     * @param data
     * @param listener
     */
    public void setAsync(final JSONObject data, final TcbListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Result res = set(data);
                    listener.onSuccess(res);
                } catch (TcbException e) {
                    listener.onFailed(e);
                }
            }
        }).start();
    }

    /**
     * 更新数据
     *
     * @param data 文档数据
     * @return
     * @throws TcbException
     */
    public Result<DocUpdataData> update(JSONObject data) throws TcbException {
        if (data.containsKey("_id")) {
            throw new TcbException(ErrorCode.INVALID_PARAM, "不能更新_id的值");
        }

        // 格式化
        try {
            data = Format.dataFormat(data);
        } catch (Exception e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        HashMap<String, String> query = new HashMap<>();
        query.put("_id", this.id);

        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);
        params.put("data", data);
        params.put("query", query);
        params.put("multi", false);
        params.put("merge", true);
        params.put("upsert", false);
        params.put("interfaceCallSource", "SINGLE_UPDATE_DOC");

        String res = this.request.sendMidDataString("database.updateDocument", params);

        Result<DocUpdataData> result = DataUtil.buildResult(res,new TypeReference<Result<DocUpdataData>>(){});
        return result;
    }

    /**
     * 更新数据（异步）
     *
     * @param data
     * @param listener
     */
    public void updateAsync(final JSONObject data, final TcbListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Result res = update(data);
                    listener.onSuccess(res);
                } catch (TcbException e) {
                    listener.onFailed(e);
                }
            }
        }).start();
    }

    /**
     * 删除文档
     *
     * @return
     * @throws TcbException
     */
    public Result<DocRemoveData> remove() throws TcbException {
        HashMap<String, String> query = new HashMap<>();
        query.put("_id", this.id);

        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);
        params.put("query", query);
        params.put("multi", false);

        String res = this.request.sendMidDataString("database.deleteDocument", params);

        Result<DocRemoveData> result = DataUtil.buildResult(res,new TypeReference<Result<DocRemoveData>>(){});
        return result;
    }

    /**
     * 删除文档（异步）
     *
     * @param listener
     */
    public void removeAsync(final TcbListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Result res = remove();
                    listener.onSuccess(res);
                } catch (TcbException e) {
                    listener.onFailed(e);
                }
            }
        }).start();
    }

    public Result<QueryData> get() throws TcbException {
        HashMap<String, String> query = new HashMap<>();
        query.put("_id", this.id);

        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);
        params.put("query", query);
        params.put("multi", false);
        params.put("projection", this.projection);

        String res = this.request.sendString("database.queryDocument", params);

        Result<QueryData> result = DataUtil.buildResult(res,new TypeReference<Result<QueryData>>(){});
        return result;
    }

    public void getAsync(final TcbListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Result res = get();
                    listener.onSuccess(res);
                } catch (TcbException e) {
                    listener.onFailed(e);
                }
            }
        }).start();
    }

    /**
     * 指定要返回的字段
     *
     * @param projection
     * @return
     */
    public Document field(HashMap<String, Boolean> projection) {
        // 把true和false转义为1和0
        HashMap<String, Number> newProjection = new HashMap<>();
        for (Map.Entry<String, Boolean> entry : projection.entrySet()) {
            if (entry.getValue()) {
                newProjection.put(entry.getKey(), 1);
            } else {
                newProjection.put(entry.getKey(), 0);
            }
        }
        return new Document(this.db, this.collName, this.id, newProjection);
    }

    private boolean checkOperatorMixed(JSONObject data) {
        //todo:
        return false;
    }

}
