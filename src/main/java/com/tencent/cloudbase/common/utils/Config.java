package com.tencent.cloudbase.common.utils;

public class Config {

    public String envName = "";
    public String appId = "";

    public Config(String envName, String appId) {
        this.envName = envName;
        this.appId = appId;
    }

    public Config(String envName) {
        this.envName = envName;
    }
}
