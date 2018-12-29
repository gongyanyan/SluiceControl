package com.example.pc.sluicecontrol.common;

import java.io.Serializable;

public class SluiceParamBean implements Serializable{
    //闸机参数Bean类

    private  String name;  //参数名称
    private  int value;  //参数值
    private  String unit;  //单位

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "SluiceParamBean{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}
