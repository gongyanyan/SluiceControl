package com.example.pc.sluicecontrol;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pc.sluicecontrol.common.BaseActivity;
import com.example.pc.sluicecontrol.common.utils.Tools;
import com.example.pc.sluicecontrol.serial.CMD;
import com.example.pc.sluicecontrol.serial.MessageTan;
import com.example.pc.sluicecontrol.serial.SerialDataUtils;
import com.example.pc.sluicecontrol.serial.SerialPortUtil;
import com.example.pc.sluicecontrol.serial.Util;

import org.nanohttpd.util.IFactory;

public class SensorTetsActivty extends BaseActivity{

    AppCompatImageView view1,view2,view3,view4,view5,view6,view7,view8,view9,view10,view11,view12,view13,view14,view15,view16,view17,view18,view19,view20,view21,view22,view23,view24;
    SerialPortUtil serialPort;
    MessageTan messageTan;
    private boolean isStop = false;
    CGQThread cgqThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置横屏
        setMainContentLayout(R.layout.sensor);

        initView();
        initData();
    }



    @Override
    protected void initView() {
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view7 = findViewById(R.id.view7);
        view8 = findViewById(R.id.view8);
        view9 = findViewById(R.id.view9);
        view10 = findViewById(R.id.view10);
        view11 = findViewById(R.id.view11);
        view12 = findViewById(R.id.view12);
        view13 = findViewById(R.id.view13);
        view14 = findViewById(R.id.view14);
        view15 = findViewById(R.id.view15);
        view16 = findViewById(R.id.view16);
        view17 = findViewById(R.id.view17);
        view18 = findViewById(R.id.view18);
        view19 = findViewById(R.id.view19);
        view20 = findViewById(R.id.view20);
        view21 = findViewById(R.id.view21);
        view22 = findViewById(R.id.view22);
        view23 = findViewById(R.id.view23);
        view24 = findViewById(R.id.view24);

        //view1.setImageResource(R.drawable.green);   改变颜色


    }

 /*   @Override
    public boolean onTouchEvent(MotionEvent event){
        if (MotionEvent.ACTION_DOWN==event.getAction()){
            float x=event.getX();
            float y=event.getY();
            Log.i("123","您单击的位置是:\nx:"+x+"\n y:"+y);
        }
        return super.onTouchEvent(event);
    }*/

    @Override
    protected void initData() {
        setMiddleTitle("传感器测试");
        serialPort = SerialPortUtil.getInstance();   //初始化串口
        cgqThread = new CGQThread();
        cgqThread.start();

        //该方法是读取数据的回调监听，一旦读取到数据，就立马回调
        serialPort.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                final String receiveString = SerialDataUtils.ByteArrToHex(buffer).trim();   //得到读取表示传感器的值
               Log.i("123","75的参数::::"+receiveString);
               // final String receiveString = "41 75 85 87 80 80 80 80 80 80 80 80 80 80 AD B7 E1";   //得到读取表示传感器的值

                final String str[] = new String[6];
                //对值进行解析
                if(!Tools.isNull(receiveString)){   //不为空
                    String [] array = receiveString.split(" ");
                    if(array.length>6&&array[1].equals("75")){   //75读取传感器的指令
                        str[0] = array[2];
                        str[1] = array[3];
                        str[2] = array[4];
                        str[3] = array[5];
                        str[4] = array[6];
                        str[5] = array[7];
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(str!=null){
                            for (int i = 0; i < str.length; i++) {
                                Log.i("123", i + "===" + str[i]);
                                //int res = str[i].charAt(1);    // str[0] =85；  则res=5
                                if(Tools.isNull(str[i])){
                                    return;
                                }
                                String s = Util.hexStr2BinStr(str[i]);    //得到二进制数
                                String[] strings = Util.splitMothd(s);   //得到分割后的数组
                                //截取后四位数字，// 85 16进制  则取 5   0101
                                if (i == 0) {   //说明是传感器S21-24的状态值

                                    if (strings[4].equals("1")) {    //24
                                        view24.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view24.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[5].equals("1")) {    //23
                                        view23.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view23.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[6].equals("1")) {    //22
                                        view22.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view22.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[7].equals("1")) {    //21
                                        view21.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view21.setImageResource(R.drawable.green);   //绿灯
                                    }

                                } else if (i == 1) {   //说明是传感器S17-20的状态值
                                    if (strings[4].equals("1")) {    //20
                                        view20.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view20.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[5].equals("1")) {    //19
                                        view19.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view19.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[6].equals("1")) {    //18
                                        view18.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view18.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[7].equals("1")) {    //17
                                        view17.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view17.setImageResource(R.drawable.green);   //绿灯
                                    }
                                } else if (i == 2) {  //说明是传感器S13-16的状态值
                                    if (strings[4].equals("1")) {    //16
                                        view16.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view16.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[5].equals("1")) {    //15
                                        view15.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view15.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[6].equals("1")) {    //14
                                        view14.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view14.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[7].equals("1")) {    //13
                                        view13.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view13.setImageResource(R.drawable.green);   //绿灯
                                    }
                                } else if (i == 3) {  //说明是传感器S9-12的状态值
                                    if (strings[4].equals("1")) {    //12
                                        view12.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view12.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[5].equals("1")) {    //11
                                        view11.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view11.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[6].equals("1")) {    //10
                                        view10.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view10.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[7].equals("1")) {    //9
                                        view9.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view9.setImageResource(R.drawable.green);   //绿灯
                                    }
                                } else if (i == 4) {  //说明是传感器S5-8的状态值
                                    if (strings[4].equals("1")) {    //8
                                        view8.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view8.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[5].equals("1")) {    //7
                                        view7.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view7.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[6].equals("1")) {    //6
                                        view6.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view6.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[7].equals("1")) {    //5
                                        view5.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view5.setImageResource(R.drawable.green);   //绿灯
                                    }
                                } else if (i == 5) {  //说明是传感器S1-4的状态值
                                    if (strings[4].equals("1")) {    //4
                                        view4.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view4.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[5].equals("1")) {    //3
                                        view3.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view3.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[6].equals("1")) {    //2
                                        view2.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view2.setImageResource(R.drawable.green);   //绿灯
                                    }

                                    if (strings[7].equals("1")) {    //1
                                        view1.setImageResource(R.drawable.red);   //红灯
                                    } else {
                                        view1.setImageResource(R.drawable.green);   //绿灯
                                    }
                                }
                            }
                        }

                    }
                });
            }
        });
    }

    @Override
    protected void initClickListener(View v) {

    }

    private class CGQThread extends Thread {    //循环读取传感器

        @Override
        public void run() {
            super.run();

            while (!isStop && !isInterrupted()) {    //循环设置(stop)，不然线程只会执行一次
                try {

                    Thread.sleep(10);//延时 50 毫秒
                    messageTan = new MessageTan(CMD.REQUESTCODE75);   //读取传感器
                    serialPort.sendBuffer(messageTan.getBtAryTranData());

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
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
        isStop = true;
        if (cgqThread != null) {
            cgqThread.interrupt();
        }
    }
}
