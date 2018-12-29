package com.example.pc.sluicecontrol.common;

import java.io.Serializable;

public class ModeXMLBean implements Serializable {

    String id ;      //模式id
    String name ;   //闸机模式
    String wllx ;   //闸机物理类型  (进站)
    String type ;   //闸机类型    (进站主机)


    public ModeXMLBean() {
    }

    public ModeXMLBean(String id, String name, String wllx, String type) {
        this.id = id;
        this.name = name;
        this.wllx = wllx;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWllx() {
        return wllx;
    }

    public void setWllx(String wllx) {
        this.wllx = wllx;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ModeXMLBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", wllx='" + wllx + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
