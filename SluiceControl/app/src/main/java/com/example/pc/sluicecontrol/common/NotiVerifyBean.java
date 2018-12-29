package com.example.pc.sluicecontrol.common;

import java.io.Serializable;

public class NotiVerifyBean implements Serializable{

    private String trxType;
    private String orderNo;
    private String handleStationCode;
    private String handleDateTime;
    private String reserve1;
    private String reserve2;


    public NotiVerifyBean() {
    }

    public NotiVerifyBean(String trxType, String orderNo, String handleStationCode, String handleDateTime, String reserve1, String reserve2) {
        this.trxType = trxType;
        this.orderNo = orderNo;
        this.handleStationCode = handleStationCode;
        this.handleDateTime = handleDateTime;
        this.reserve1 = reserve1;
        this.reserve2 = reserve2;
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

    public String getHandleDateTime() {
        return handleDateTime;
    }

    public void setHandleDateTime(String handleDateTime) {
        this.handleDateTime = handleDateTime;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    @Override
    public String toString() {
        return "NotiVerifyBean{" +
                "trxType='" + trxType + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", handleStationCode='" + handleStationCode + '\'' +
                ", handleDateTime='" + handleDateTime + '\'' +
                ", reserve1='" + reserve1 + '\'' +
                ", reserve2='" + reserve2 + '\'' +
                '}';
    }
}
