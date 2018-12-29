package com.example.pc.sluicecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pc.sluicecontrol.common.BaseActivity;
import com.example.pc.sluicecontrol.nanohttp.HttpServerImpl;
import com.example.pc.sluicecontrol.serial.CMD;
import com.example.pc.sluicecontrol.serial.MessageTan;
import com.example.pc.sluicecontrol.serial.SerialPortUtil;
import com.example.pc.sluicecontrol.serial.Util;

public class GateTetsActivty extends BaseActivity{

    Button btn1,btn2,btn3;

    SerialPortUtil serialPort;
    MessageTan messageTan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置横屏
        setMainContentLayout(R.layout.gatetest);

        initView();
        initData();
    }



    @Override
    protected void initView() {
        serialPort = SerialPortUtil.getInstance();   //初始化串口
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setMiddleTitle("门单元测试");
    }

    @Override
    protected void initClickListener(View v) {
        switch (v.getId()){
            case R.id.btn1:

                messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRAMAddress, Util.byteStrToBytes((byte)0x10));
                serialPort.sendBuffer(messageTan.getBtAryTranData());
                toastMsg("指令发送成功");

                break;

            case R.id.btn2:

                messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRBMAddress, Util.byteStrToBytes((byte)0x10));
                serialPort.sendBuffer(messageTan.getBtAryTranData());
                toastMsg("指令发送成功");

                break;

            case R.id.btn3:

                Intent intent = new Intent(GateTetsActivty.this,SensorTetsActivty.class);
                startActivity(intent);

                break;
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
