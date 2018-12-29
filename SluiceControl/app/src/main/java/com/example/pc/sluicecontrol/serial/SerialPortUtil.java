package com.example.pc.sluicecontrol.serial;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

/**
        * 串口操作类
        */
public class SerialPortUtil {
    private String TAG = SerialPortUtil.class.getSimpleName();
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    //private String path = "/dev/ttyS0"; //这个是我们要读取的串口路径，这个硬件开发人员会告诉我们的
    private String path = "/dev/ttyMT2";

    private int baudrate = 9600;//这个参数，硬件开发人员也会告诉我们`IU   的AA344ESE
    private static SerialPortUtil portUtil;
    private OnDataReceiveListener onDataReceiveListener = null;
    private boolean isStop = false;
    private LunXunThread lunXunThread;
    private SerialPortFinder serialPortFinder;   //查找连接串口的设备


    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }

    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {   //回调方法
        onDataReceiveListener = dataReceiveListener;
    }

    public static SerialPortUtil getInstance() {     //单例模式
       // if (null == portUtil) {
            portUtil = new SerialPortUtil();
            portUtil.onCreate();
       // }
        return portUtil;
    }

    /**
     * 初始化串口信息
     */
    public void onCreate() {
        try {

            serialPortFinder = new SerialPortFinder();
            String[] allDevicesPath = serialPortFinder.getAllDevicesPath();   //得到所有的设备
            //Log.i("123",allDevicesPath[0]+"所有设备:::"+allDevicesPath.toString()+"--"+allDevicesPath.length);
            /*for(int i=0;i<allDevicesPath.length;i++){
                path = allDevicesPath[i];   //循环去设备
                if(mSerialPort.isOpened){   //false
                    mSerialPort = new SerialPort(new File(path), baudrate, 0);  //打开串口
                }
            }*/

            mSerialPort = new SerialPort(new File(path), baudrate, 0);  //打开串口
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            mReadThread = new ReadThread();
            lunXunThread = new LunXunThread();
            isStop = false;
            //lunXunThread.start();   //开启轮询
            mReadThread.start();   //线程读取数据

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        String str = cmd;
        str = str.replace(" ", "");
        byte[] mBuffer = SerialDataUtils.HexToByteArr(str);       //ByteArrToHex byte转换成String类型
        if (!isStop) {
            try {
                if (mOutputStream != null) {
                    mOutputStream.write(mBuffer);
                } else {
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        } else {
            System.out.println("sendCmds serialPort isClose");
            result = false;
        }

        return result;
    }


    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds2(String cmd) {
        boolean result = true;
        byte[] mBuffer = (cmd+"\r\n").getBytes();
//注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer

        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);

            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        String tail = "\r\n";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length+tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
//注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer

        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBufferTemp);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    private class LunXunThread extends Thread{   //轮询
        @Override
        public void run() {
            super.run();
            while (!isStop && !isInterrupted()) {

                byte b [] = {CMD.LoopCode};
                sendBuffer(b);
            }
        }
    }


    private class ReadThread extends Thread {    //循环读取串口返回的数据

        @Override
        public void run() {
            super.run();
            System.out.println("ReadThread.run isInterrupted()=" + isInterrupted());
            while (!isStop && !isInterrupted()) {    //循环设置(stop)，不然线程只会执行一次
                System.out.println("ReadThread.run mInputStream=" + mInputStream);
                int size;
                try {
                    if (mInputStream == null){
                        System.out.println("ReadThread.run return");
                        return;
                    }

                    byte[] buffer = new byte[512];
                    System.out.println("ReadThread.run buffer");
                    size = mInputStream.read(buffer);//该方法读不到数据时，会阻塞在这里， (串口返回的数据)
                    System.out.println("ReadThread.run size=" + size);
                    if (size > 0) {  //说明读到了数据
                       /* if(MyLog.isDyeLevel()){
                            MyLog.log(TAG, MyLog.DYE_LOG_LEVEL, "length is:"+size+",data is:"+new String(buffer, 0, size));
                        }*/
                        byte[] buffer2 = new byte[size];
                        for (int i = 0; i < size; i++) {
                            buffer2[i] = buffer[i];
                        }
                        if (onDataReceiveListener != null) {
                            onDataReceiveListener.onDataReceive(buffer2, size);
                        }
                    }
                    Thread.sleep(10);//延时 50 毫秒
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ReadThread.run  e.printStackTrace() " + e);
                    return;
                }
            }
        }
    }



    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
        }
    }

}