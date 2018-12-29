package com.example.pc.sluicecontrol;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.pc.sluicecontrol.common.BaseActivity;
import com.example.pc.sluicecontrol.common.ModeXMLBean;
import com.example.pc.sluicecontrol.common.config.Contances;
import com.example.pc.sluicecontrol.common.utils.XmlUtil;
import com.example.pc.sluicecontrol.serial.CMD;
import com.example.pc.sluicecontrol.serial.MessageTan;
import com.example.pc.sluicecontrol.serial.SerialPortUtil;
import com.example.pc.sluicecontrol.serial.Util;

import java.util.ArrayList;
import java.util.List;

public class DeviceSetActivty extends BaseActivity{

    SerialPortUtil serialPort;
    Spinner sp_wl,sp_lx,sp_set;
    Button btn;
    boolean isExists;  //判断文件是否存在
    XmlUtil xmlUtil;
    ModeXMLBean modeXMLBean;

    int  id;    //模式id
    String name;   //模式
    String wllx;   //闸机物理类型
    String type;   //闸机类型


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置横屏
        setMainContentLayout(R.layout.deviceset);

        initView();
        initData();
    }



    @Override
    protected void initView() {

       // serialPort = SerialPortUtil.getInstance();   //初始化串口
        sp_wl = findViewById(R.id.sp_wl);
        sp_lx = findViewById(R.id.sp_lx);
        sp_set = findViewById(R.id.sp_set);
        btn = findViewById(R.id.btn_change);
        btn.setOnClickListener(this);

        xmlUtil = new XmlUtil(this);
        modeXMLBean = new ModeXMLBean();

    }

    @Override
    protected void initData() {
        setMiddleTitle("设备设置");



        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){  //判断sd卡是否存在

            String path = Environment.getExternalStorageDirectory()+"/txms.xml";
            isExists = xmlUtil.fileIsExists(path);//判断xml文件是否存在

            if(isExists){   //xml存在
                modeXMLBean = xmlUtil.readxml();

            }else {  //如果xml不存在，则写入默认值

                 modeXMLBean = new ModeXMLBean("1","受控进站","进站","进站主机");
            }
        }

        this.loadDataForsp_wl();
        this.loadDataForsp_lx();
        this.loadDataForsp_set();


        Log.i("123","设置比：："+modeXMLBean);

    }

    @Override
    protected void initClickListener(View v) {
        switch (v.getId()){

            case R.id.btn_change:

                //得到修改后的值
                modeXMLBean.setId(id+"");
                modeXMLBean.setWllx(wllx);
                modeXMLBean.setType(type);
                modeXMLBean.setName(name);

                Log.i("123","修改：："+modeXMLBean);
                if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){  //判断sd卡是否存在
                    xmlUtil.savexml(modeXMLBean);
                    finish();
                }else {
                    toastMsg("SD卡不存在,不能写入");
                }

                break;

        }
    }


    @Override
    public void onKeyDownBack() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //serialPort.closeSerialPort();

    }

    private void loadDataForsp_wl() {

        List<String> spinerList = new ArrayList<>();
        spinerList = Contances.LIST_BILL_LX;

        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,R.layout.spinner_display_style,R.id.txtvwSpinner,
                spinerList);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        this.sp_wl.setAdapter(myAdapter);

        for(int i =0;i< spinerList.size();i++){    //显示选择的物理类型
            if(spinerList.get(i).equals(modeXMLBean.getWllx())){
                sp_wl.setSelection(i);
                break;
            }
        }

        sp_wl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                 wllx = myAdapter.getItem(i);//得到选择的值
                Log.i("123","物理类型---"+wllx);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void loadDataForsp_lx() {

        List<String> spinerList = new ArrayList<>();
        spinerList = Contances.LIST_BILL_TYPE;

        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,R.layout.spinner_display_style,R.id.txtvwSpinner,
                spinerList);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        this.sp_lx.setAdapter(myAdapter);

        for(int i =0;i< spinerList.size();i++){    //显示选择的闸机类型
            if(spinerList.get(i).equals(modeXMLBean.getType())){
                sp_lx.setSelection(i);
                break;
            }
        }

        sp_lx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                 type = myAdapter.getItem(i);//得到选择的值
                Log.i("123","闸机类型---"+type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadDataForsp_set() {

        List<String> spinerList = new ArrayList<>();
        spinerList = Contances.LIST_BILL_MS;

        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,R.layout.spinner_display_style,R.id.txtvwSpinner,
                spinerList);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        this.sp_set.setAdapter(myAdapter);

        for(int i =0;i< spinerList.size();i++){    //显示选择的模式
            if(spinerList.get(i).equals(modeXMLBean.getName())){
                sp_set.setSelection(i);
                break;
            }
        }

        sp_set.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                name = myAdapter.getItem(i);//得到选择的模式
                id = i+1;
                Log.i("123",i+"---模式---"+name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
