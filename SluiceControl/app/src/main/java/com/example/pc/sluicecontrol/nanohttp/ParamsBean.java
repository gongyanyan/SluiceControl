package com.example.pc.sluicecontrol.nanohttp;

import java.io.Serializable;

public class ParamsBean implements Serializable {   //参过来的参数Bean

    private String msgType;
    private String groupID;
    private String userID;
    private String score;


    public ParamsBean() {
    }

    public ParamsBean(String msgType, String groupID, String userID, String score) {
        this.msgType = msgType;
        this.groupID = groupID;
        this.userID = userID;
        this.score = score;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "SetBean{" +
                "msgType='" + msgType + '\'' +
                ", groupID='" + groupID + '\'' +
                ", userID='" + userID + '\'' +
                ", score='" + score + '\'' +
                '}';
    }

}
