package com.example.pc.sluicecontrol.nanohttp;

import java.io.Serializable;

public class ParamsBean2 implements Serializable {   //参过来的参数Bean

    private String msgType;
    private String orderID;  //只有订单用户才有这个字段
    private String tenantID;
    private int userType;
    private String userID;
    private float score;
    private String stationCode;

    private String SalveIP;   //从机IP地址


    public ParamsBean2() {

    }

    public ParamsBean2(String msgType, String orderID, String tenantID, int userType, String userID, float score, String stationCode) {
        this.msgType = msgType;
        this.orderID = orderID;
        this.tenantID = tenantID;
        this.userType = userType;
        this.userID = userID;
        this.score = score;
        this.stationCode = stationCode;
    }


    public ParamsBean2(String msgType, String orderID, String tenantID, int userType, String userID, float score, String stationCode, String salveIP) {
        this.msgType = msgType;
        this.orderID = orderID;
        this.tenantID = tenantID;
        this.userType = userType;
        this.userID = userID;
        this.score = score;
        this.stationCode = stationCode;
        SalveIP = salveIP;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }


    public String getSalveIP() {
        return SalveIP;
    }

    public void setSalveIP(String salveIP) {
        SalveIP = salveIP;
    }


    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    @Override
    public String toString() {
        return "ParamsBean2{" +
                "msgType='" + msgType + '\'' +
                ", orderID='" + orderID + '\'' +
                ", tenantID='" + tenantID + '\'' +
                ", userType=" + userType +
                ", userID='" + userID + '\'' +
                ", score=" + score +
                ", stationCode='" + stationCode + '\'' +
                ", SalveIP='" + SalveIP + '\'' +
                '}';
    }
}
