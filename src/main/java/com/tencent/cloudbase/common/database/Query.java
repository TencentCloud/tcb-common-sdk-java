package com.tencent.cloudbase.common.database;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencent.cloudbase.common.Result;
import com.tencent.cloudbase.common.bean.CountData;
import com.tencent.cloudbase.common.bean.DocUpdataData;
import com.tencent.cloudbase.common.bean.QueryData;
import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.database.commands.LogicCommand;
import com.tencent.cloudbase.common.utils.Format;
import com.tencent.cloudbase.common.utils.Request;
import com.tencent.cloudbase.common.utils.TcbListener;
import com.tencent.cloudbase.common.utils.DataUtil;
import com.tencent.cloudbase.common.utils.Validate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Query {
    protected Db db;
    protected String collName;
    private JSONObject fieldFilters;
    private ArrayList<HashMap<String, String>> fieldOrders;
    private JSONObject queryOptions;

    private Request request;

    /**
     * 初始化
     *
     * @param db       - 数据库的引用
     * @param collName - 集合名称
     */
    public Query(Db db,String collName) {
        this(db, collName, new JSONObject(), new ArrayList<HashMap<String, String>>(),
                new JSONObject());
    }

    /**
     * 初始化
     *
     * @param db           - 数据库的引用
     * @param collName     - 集合名称
     * @param fieldFilters - 过滤条件
     * @param fieldOrders  - 排序条件
     * @param queryOptions - 查询条件
     */
    public Query(
            Db db,
            String collName,
            JSONObject fieldFilters,
            ArrayList<HashMap<String, String>> fieldOrders,
            JSONObject queryOptions
    ) {
        this.db = db;
        this.collName = collName;
        this.fieldFilters = fieldFilters;
        this.fieldOrders = fieldOrders;
        this.queryOptions = queryOptions;
        this.request = db.getRequest();
    }

    public Result<QueryData> get() throws TcbException {
        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);

        // 处理排序条件
        ArrayList<HashMap<String, String>> cloneFieldOrders = new ArrayList<>();
        for (HashMap<String, String> order : this.fieldOrders) {
            cloneFieldOrders.add(order);
        }
        if (cloneFieldOrders.size() > 0) {
            params.put("order", cloneFieldOrders);
        }

        // 处理过滤条件
        params.put("query", this.fieldFilters);

        // 处理查询条件
        if (this.queryOptions.containsKey("offset")) {
            int offset = (int) this.queryOptions.get("offset");
            if (offset > 0) {
                params.put("offset", offset);
            }
        }
        if (this.queryOptions.containsKey("limit")) {
            int limit = (int) this.queryOptions.get("limit");
            params.put("limit", Math.min(limit, 100));
        } else {
            params.put("limit", 100);
        }
        if (this.queryOptions.containsKey("projection")) {
            params.put("projection", this.queryOptions.get("projection"));
        }

        String res = this.request.sendMidDataString("database.queryDocument", params);

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

    public Result<CountData> count() throws TcbException {
        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);
        params.put("query", this.fieldFilters);

        String res = this.request.sendMidDataString("database.queryDocument", params);

        Result<CountData> result = DataUtil.buildResult(res,new TypeReference<Result<CountData>>(){});
        return result;
    }

    public void countAsync(final TcbListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Result res = count();
                    listener.onSuccess(res);
                } catch (TcbException e) {
                    listener.onFailed(e);
                }
            }
        }).start();
    }

    /**
     * 查询条件
     *
     * @param query
     * @return
     * @throws TcbException
     */
    public Query where(JSONObject query) throws TcbException {
        // 格式化
        try {
            query = Format.dataFormat(query);
        } catch (Exception e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        return new Query(this.db, this.collName, query, this.fieldOrders, this.queryOptions);
    }

    public Query where(LogicCommand query) throws TcbException {
        JSONObject queryJSON;

        // 格式化
        try {
            queryJSON = query.toJSON();
            queryJSON = Format.dataFormat(queryJSON);
        } catch (Exception e) {
            throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
        }

        return new Query(this.db, this.collName, queryJSON, this.fieldOrders, this.queryOptions);
    }


    /**
     * 设置排序方式
     *
     * @param fieldPath    字段路径
     * @param direction 排序方式
     * @return
     * @throws TcbException
     */
    public Query orderBy(String fieldPath, OrderDirection direction) throws TcbException {
        Validate.isFieldPath(fieldPath);
        Validate.isFieldOrder(direction.getDirection());

        HashMap<String, String> newOrder = new HashMap<>();
        newOrder.put("direction", direction.getDirection());
        newOrder.put("field", fieldPath);

        ArrayList<HashMap<String, String>> combinedOrders = new ArrayList<>();
        combinedOrders.addAll(this.fieldOrders);
        combinedOrders.add(newOrder);

        return new Query(this.db, this.collName, this.fieldFilters, combinedOrders,
                this.queryOptions);
    }

    /**
     * @param limit
     * @return
     */
    public Query limit(int limit) {
        JSONObject combinedOptions = new JSONObject();
        combinedOptions.putAll(this.queryOptions);
        combinedOptions.put("limit", limit);

        return new Query(this.db, this.collName, this.fieldFilters, this.fieldOrders,
                combinedOptions);
    }

    public Query skip(int offset) {
        JSONObject combinedOptions = new JSONObject();
        combinedOptions.putAll(this.queryOptions);
        combinedOptions.put("offset", offset);

        return new Query(
                this.db, this.collName, this.fieldFilters, this.fieldOrders, combinedOptions
        );
    }

    /**
     * 发起请求批量更新文档
     *
     * @param data
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

        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);
        params.put("query", this.fieldFilters);
        params.put("multi", true);
        params.put("merge", true);
        params.put("upsert", false);
        params.put("data", data);
        params.put("interfaceCallSource", "BATCH_UPDATE_DOC");

        String res = request.sendMidDataString("database.updateDocument", params);


        Result<DocUpdataData> result = DataUtil.buildResult(res,new TypeReference<Result<DocUpdataData>>(){});
        return result;
    }

    /**
     * 发起请求批量更新文档（异步）
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
     * 指定要返回的字段
     *
     * @param projection
     * @return
     */
    public Query field(Map<String, Boolean> projection) {
        // 把true和false转义为1和0
        JSONObject newProjection = new JSONObject();
        for (Map.Entry<String, Boolean> entry : projection.entrySet()) {
            if (entry.getValue()) {
                newProjection.put(entry.getKey(), 1);
            } else {
                newProjection.put(entry.getKey(), 0);
            }
        }

        JSONObject option = new JSONObject();
        option.putAll(this.queryOptions);
        option.put("projection", newProjection);

        return new Query(this.db, this.collName, this.fieldFilters, this.fieldOrders, option);
    }

    /**
     * 条件删除文档
     *
     * @return
     * @throws TcbException
     */
    public JSONObject remove() throws TcbException {
        if (this.queryOptions.size() > 0) {
            throw new TcbException(
                    "Database.Query",
                    "`offset`, `limit` and `projection` are not supported in remove() operation"
            );
        }

        if (this.fieldOrders.size() > 0) {
            throw new TcbException(
                    "Database.Query", "`orderBy` is not supported in remove() operation"
            );
        }

        JSONObject params = new JSONObject();
        params.put("collectionName", this.collName);
        params.put("query", this.fieldFilters);
        params.put("multi", true);

        JSONObject res = this.request.sendMidData("database.deleteDocument", params);
        if (res.containsKey("code")) {
            throw new TcbException(res.getString("code"), res.getString("message"));
        } else {
            JSONObject result = new JSONObject();
            try {
                result.put("requestId", res.getString("requestId"));
                result.put("deleted", res.getJSONObject("data").getIntValue("deleted"));
            } catch (Exception e) {
                throw new TcbException(ErrorCode.JSON_ERR, e.getMessage());
            }

            return result;
        }

    }

    /**
     * 条件删除文档（异步）
     *
     * @param listener
     */
    public void removeAsync(final TcbListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject res = remove();
                    listener.onSuccess(res);
                } catch (TcbException e) {
                    listener.onFailed(e);
                }
            }
        }).start();
    }
}
