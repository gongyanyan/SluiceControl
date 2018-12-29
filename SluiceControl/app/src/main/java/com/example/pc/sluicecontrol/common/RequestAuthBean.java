package com.example.pc.sluicecontrol.common;

import java.io.Serializable;

public class RequestAuthBean implements Serializable {

    private String trxType;
    private String orderNo;
    private String handleStationCode;

    public RequestAuthBean() {
    }

    public RequestAuthBean(String trxType, String orderNo, String handleStationCode) {
        this.trxType = trxType;
        this.orderNo = orderNo;
        this.handleStationCode = handleStationCode;
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getHandleStationCode() {
        return handleStationCode;
    }

    public void setHandleStationCode(String handleStationCode) {
        this.handleStationCode = handleStationCode;
    }

    @Override
    public String toString() {
        return "RequestAuthBean{" +
                "trxType='" + trxType + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", handleStationCode='" + handleStationCode + '\'' +
                '}';
    }
}
