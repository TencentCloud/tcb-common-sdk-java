package com.tencent.cloudbase.common.exception;

public class TcbRemoteException extends TcbException{

    private String code;
    private String stack;

    public TcbRemoteException(String code,String message,String stack){
        super(code,message);
        this.code = code;
        this.stack = stack;
    }

    public String getStack() {
        return stack;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "code: " + this.code + " ; message " + getMessage()+"  stack : "+this.stack;
    }
}
