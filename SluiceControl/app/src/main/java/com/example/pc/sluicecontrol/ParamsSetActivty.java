package com.example.pc.sluicecontrol;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.sluicecontrol.common.BaseActivity;
import com.example.pc.sluicecontrol.common.SluiceParamBean;
import com.example.pc.sluicecontrol.common.adapter.ParamSetAdapter;
import com.example.pc.sluicecontrol.common.utils.Tools;
import com.example.pc.sluicecontrol.serial.CMD;
import com.example.pc.sluicecontrol.serial.MessageTan;
import com.example.pc.sluicecontrol.serial.SerialDataUtils;
import com.example.pc.sluicecontrol.serial.SerialPortUtil;
import com.example.pc.sluicecontrol.serial.Util;

import java.util.ArrayList;
import java.util.List;

public class ParamsSetActivty extends BaseActivity{


    ListView listView;

    SerialPortUtil serialPort;
    MessageTan messageTan;
    List<SluiceParamBean> sluiceParamBeans;
    ParamSetAdapter paramSetAdapter;
    String[] arrayData;   //所有的参数值


    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 0x001:

                    Log.i("123","参数集合::"+sluiceParamBeans);
                    paramSetAdapter = new ParamSetAdapter(ParamsSetActivty.this,sluiceParamBeans);
                    listView.setAdapter(paramSetAdapter);   //展示


                    break;

                case 0x002:

                    //修改后再读取参数
                    Log.i("123","再度读取");
                    messageTan = new MessageTan(CMD.REQUESTCODE78);
                    serialPort.sendBuffer(messageTan.getBtAryTranData());

                    break;

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置横屏
        setMainContentLayout(R.layout.param);

        initView();
        initData();
    }



    @Override
    protected void initView() {
        listView = findViewById(R.id.lv);
        serialPort = SerialPortUtil.getInstance();   //初始化串口

    }

    @Override
    protected void initData() {
        setMiddleTitle("闸机参数设置");
        sluiceParamBeans = new ArrayList<>();


        messageTan = new MessageTan(CMD.REQUESTCODE78);   //读取参数
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        //该方法是读取数据的回调监听，一旦读取到数据，就立马回调
        serialPort.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                final String receiveString = SerialDataUtils.ByteArrToHex(buffer).trim();
                // final String receiveString="41 78 80 80 80 8A 80 8A 81 84 86 84 8B 84 84 86 80 80 81 84 80 80 8F 8F 80 81 80 81 80 8F 81 84 80 82 85 88 80 82 80 81 80 80 80 85 80 85 80 82 80 82 80 81 80 84 80 81 80 89 8F 8F 8F 8F 81 8E 83 80 83 80 82 80 80 80 80 80 81 80 AD BC ED";
                Log.i("123","再次---"+receiveString);
                //对值进行解析
                if(!Tools.isNull(receiveString)) {   //不为空
                    String[] array = receiveString.split(" ");

                    if (array.length>11&&(array[1].equals("78"))){
                        arrayData =new String[74];
                        System.arraycopy(array,2,arrayData,0,74 );    //得到copy后的数组

                        sluiceParamBeans.clear();   //清空集合
                        for(int i=0;i<arrayData.length;i+=2){
                            String s = Util.hexStr2BinStr(arrayData[i]).substring(4,8)+ Util.hexStr2BinStr(arrayData[i+1]).substring(4,8);    //得到二进制数
                            int num = Integer.parseInt(s, 2);
                            // Log.i("123","数据:"+s+"---"+num);
                            SluiceParamBean sluiceParamBean = new SluiceParamBean();

                            if (i==0){
                                addList("扇门关闭超时","100ms",num,sluiceParamBean);
                            }else if(i==2){
                                addList("通行授权超时","1s",num,sluiceParamBean);
                            }else if(i==4){
                                addList("蜂鸣器打开超时","100ms",num,sluiceParamBean);
                            }else if(i==6){
                                addList("蜂鸣器延时超时","100ms",num,sluiceParamBean);
                            }else if(i==8){
                                addList("离开通道超时","100ms",num,sluiceParamBean);
                            }else if(i==10){
                                addList("传感器自检超时","1s",num,sluiceParamBean);
                            }else if(i==12){
                                addList("轮询检测超时","100ms",num,sluiceParamBean);
                            }else if(i==14){
                                //addList("本地运行模式","1s",num);
                            }else if(i==16){
                                addList("马达停止暂停超时","100ms",num,sluiceParamBean);
                            }else if(i==18){
                                addList("马达打开暂停超时","100ms",num,sluiceParamBean);
                            }else if(i==20){
                                addList("安全区传感器超时","1s",num,sluiceParamBean);
                            }else if(i==22){
                                addList("探测区激活开关","-",num,sluiceParamBean);
                            }else if(i==24){
                                // addList("","1s",num);
                            }else if(i==26){
                                // addList("","1s",num);
                            }else if(i==28){
                                addList("传感器组A打开超时","1s",num,sluiceParamBean);
                            }else if(i==30){
                                addList("传感器组B打开超时(高位)","1s",num,sluiceParamBean);
                            }else if(i==32){
                                addList("传感器组B打开超时(低位)","1s",num,sluiceParamBean);
                            }else if(i==34){
                                addList("欺骗通行警报超时","1s",num,sluiceParamBean);
                            }/*else if(i==36){
                    addList("蜂鸣器延时超时","1s",num);
                }else if(i==38){
                    addList("蜂鸣器延时超时","1s",num);
                }else if(i==40){
                    addList("蜂鸣器延时超时","1s",num);
                }else if(i==42){
                    addList("蜂鸣器延时超时","1s",num);
                }else if(i==46){
                    addList("蜂鸣器延时超时","1s",num);
                }*/

                        }

                        mhandler.sendEmptyMessage(0x001);
                    }
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //点击事件
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击进行参数修改
                SluiceParamBean sluiceParamBean = sluiceParamBeans.get(i);   //得到点击的值
                showDiaglog(sluiceParamBean);

            }
        });

    }

    public void addList(String name ,String unit,int number, SluiceParamBean sluiceParamBean){   //添加集合
        sluiceParamBean.setName(name);
        sluiceParamBean.setUnit(unit);
        sluiceParamBean.setValue(number);   //赋值

        sluiceParamBeans.add(sluiceParamBean);
    }


    /**
     *
     * @param
     * @param
     */
    private void showDiaglog(final SluiceParamBean sluiceParamBean){
        // 弹出自定义dialog
        LayoutInflater inflater = LayoutInflater.from(ParamsSetActivty.this);
        View view = inflater.inflate(R.layout.dialog_je, null);

        // 对话框
        final Dialog dialog = new Dialog(ParamsSetActivty.this);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        // 设置宽度为屏幕的宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);

        final EditText et_obj = (EditText) view.findViewById(R.id.et_obj);// 标的

        et_obj.setText(sluiceParamBean.getValue()+"");   //显示默认值

        TextView tv_go = (TextView) view.findViewById(R.id.tv_go);// 确认
        TextView tv_finish = (TextView) view.findViewById(R.id.tv_finish);// 取消

        TextView tv_passWord = (TextView) view.findViewById(R.id.tv_passWord);
        tv_passWord.setText("请输入值：");
        tv_passWord.setVisibility(View.VISIBLE);
        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isNull(et_obj)){
                    toastMsg("输入不能为空");
                }else {

                    if (arrayData.length>0){
                        //把输入的数据转换成二进制
                        int scan = Integer.parseInt(et_obj.getText().toString().trim());
                        String s = Util.decimalToBinary(scan);  //10进制数转二进制数
                        byte bnew = Util.binStrToByte(s);//二进制数转为byte(16进制)


                        //新建一个存放所有参数的byte数组(37个参数)
                        byte [] bytes = new byte[37];
                        for(int i=0;i<arrayData.length;i+=2){    //循环原始的数据

                            //得到二进制数
                            String ss = Util.hexStr2BinStr(arrayData[i]).substring(4,8)+ Util.hexStr2BinStr(arrayData[i+1]).substring(4,8);
                            //把二进制数转换成byte
                            byte bbb = Util.binStrToByte(ss);
                            int j=i/2;
                            bytes[j]=bbb;

                        }


                        if(sluiceParamBean.getName().equals("扇门关闭超时")){

                        }else if(sluiceParamBean.getName().equals("通行授权超时")){

                            bytes[1]=bnew;   //把要修改的值赋给参数集合

                        }

                        //设置参数
                        messageTan = new MessageTan(CMD.REQUESTCODE68,Util.byteStrToBytes(bytes));
                        serialPort.sendBuffer(messageTan.getBtAryTranData());


                       // mhandler.sendEmptyMessage(0x002);
                        mhandler.sendEmptyMessageDelayed(0x002, 1000);   //延迟3秒

                    }

                    dialog.cancel();
                }
            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void initClickListener(View v) {
        switch (v.getId()){


        }
    }

    @Override
    public void onKeyDownBack() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPort.closeSerialPort();

    }

}
