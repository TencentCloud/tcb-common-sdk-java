package com.tencent.cloudbase.common.utils;

public interface TcbListener {

    public void onSuccess(Object res);

    public void onFailed(Exception res);
}
