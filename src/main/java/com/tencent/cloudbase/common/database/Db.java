package com.tencent.cloudbase.common.database;



import com.tencent.cloudbase.common.exception.TcbException;
import com.tencent.cloudbase.common.utils.ErrorCode;
import com.tencent.cloudbase.common.utils.Config;
import com.tencent.cloudbase.common.utils.Request;

public class Db {
    public Config config;
//    public Context context;
    public Command command;
    public Geo geo;

    private Request request;

    public Db(String envName,Request request) {
        this.config = new Config(envName);
//        this.context = context;
        this.command = new Command();
        this.geo = new Geo();
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public ServerDate serverDate() {
        return serverData(0);
    }

    public ServerDate serverData(int offset) {
        return new ServerDate(offset);
    }

    public RegExp regExp(String regexp) throws TcbException {
        return regExp(regexp, "");
    }

    public RegExp regExp(String regexp, String options) throws TcbException {
        return new RegExp(regexp, options);
    }

    public Collection collection(String collName) throws TcbException {
        if (collName==null || collName.isEmpty()) {
            throw new TcbException(ErrorCode.EMPTY_PARAM, "Collection name is required");
        }

        return  new Collection(this, collName);
    }
}
