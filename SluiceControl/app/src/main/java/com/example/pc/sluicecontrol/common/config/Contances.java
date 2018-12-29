package com.example.pc.sluicecontrol.common.config;




import com.example.pc.sluicecontrol.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public final class Contances {

    /**
     * 选择门单元物理类型
     */
    public final static ArrayList<String> LIST_BILL_LX = new ArrayList<String>(){
        {

            add("进站");
            add("出站");

        }
    };


    /**
     * 闸机类型
     */
    public final static ArrayList<String> LIST_BILL_TYPE = new ArrayList<String>(){
        {

            add("进站主机");
            add("进站从机");
            add("出站主机");
            add("出站从机");

        }
    };


    /**
     * 选择门单元通行模式
     */
    public final static ArrayList<String> LIST_BILL_MS = new ArrayList<String>(){
        {

            add("受控进站");
            add("受控出站");
            add("双向受控");
            add("受控进站/免费出站");
            add("受控出站/免费进站");
            add("免费进站/禁止出站");
            add("免费出站/禁止进站");
            add("双向免费");
            add("维修模式");
        }
    };



}
