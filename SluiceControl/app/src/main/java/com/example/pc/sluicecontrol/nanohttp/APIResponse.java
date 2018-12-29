package com.example.pc.sluicecontrol.nanohttp;

import java.io.Serializable;

public class APIResponse<T> implements Serializable {

    private int retCode;
    private String retMsg;
    private T result;


    public APIResponse(int retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\r\n retCode is " + retCode + "\r\n");
        sb.append("retMsg is " + retMsg + "\r\n");
        return sb.toString();
    }
}
