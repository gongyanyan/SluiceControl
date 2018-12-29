package com.example.pc.sluicecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pc.sluicecontrol.common.BaseActivity;
import com.example.pc.sluicecontrol.serial.CMD;
import com.example.pc.sluicecontrol.serial.MessageTan;
import com.example.pc.sluicecontrol.serial.SerialPortUtil;
import com.example.pc.sluicecontrol.serial.Util;

public class TrafficTetsActivty extends BaseActivity{

    Button btn1,btn2;

    SerialPortUtil serialPort;
    MessageTan messageTan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置横屏
        setMainContentLayout(R.layout.traffictest);

        initView();
        initData();
    }



    @Override
    protected void initView() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        serialPort = SerialPortUtil.getInstance();   //初始化串口
    }

    @Override
    protected void initData() {
        setMiddleTitle("通行指示器测试");
    }

    @Override
    protected void initClickListener(View v) {
        switch (v.getId()){
            case R.id.btn1:

                setModel(CMD.green);

                break;

            case R.id.btn2:   //禁止通行

                setModel(CMD.red);

                break;
        }
    }

    private void setModel( byte  btte []) {
        //设置受控模式(60指令设置寄存器)

        CMD.mode(9);
        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRAMAddress, Util.byteStrToBytes(CMD.DIRAMCode));
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRBMAddress,Util.byteStrToBytes(CMD.DIRBMCode));
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRGENAddress,Util.byteStrToBytes(CMD.DIRGENCode));
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRSET1Address,Util.byteStrToBytes(CMD.DIRSET1Code));
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        //设置指示灯
       // byte  btte [] = {(byte)0x01,(byte)0x20,(byte)0x24,(byte)0x00};

        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRAUXOAddress,Util.byteStrToBytes(btte));
        serialPort.sendBuffer(messageTan.getBtAryTranData());
        toastMsg("指令发送成功");
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
