package com.example.pc.sluicecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pc.sluicecontrol.common.ModeXMLBean;
import com.example.pc.sluicecontrol.common.config.Config;
import com.example.pc.sluicecontrol.common.utils.Tools;
import com.example.pc.sluicecontrol.common.utils.XmlUtil;
import com.example.pc.sluicecontrol.nanohttp.HttpServerImpl;
import com.example.pc.sluicecontrol.nanohttp.ParamsBean;
import com.example.pc.sluicecontrol.serial.CMD;
import com.example.pc.sluicecontrol.serial.MessageTan;
import com.example.pc.sluicecontrol.serial.SerialDataUtils;
import com.example.pc.sluicecontrol.serial.SerialPortUtil;
import com.example.pc.sluicecontrol.serial.Util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivityStand extends Activity implements View.OnClickListener{

    //可以使用的版本


    SerialPortUtil serialPort;
    HttpServerImpl mHttpServer;
    MessageTan messageTan;
    LinearLayout imageView;

    private TextView txtView;

    int id =1;   //受控进站模式
    XmlUtil xmlUtil;
    boolean isExists;  //判断文件是否存在
    String status="81";   //定义扇门状态 (默认打开)
    MyThread myThread;  //读取扇门状态
    boolean isrun=true;   //判断线程是不是在运行
    boolean isOpenDoor;   //是不是发送的开门请求
    String IpAndress;   //本机(PAD)的IP地址
    String SlaveIpAndress;   //发送请求的从机的IP地址
    int zhujiConut;   //主机计数(大于3秒后才开始去读扇门的状态)
    boolean isFirst = true;   //标志位， 用来区分是否是第一次(开门人通过前)读取寄存器CTNA,CTNB的值
    String firstNum;    //  寄存器CTNA,CTNB初始值
    String sencendNum;    //  闸机关闭后寄存器CTNA,CTNB的值(包括逆向闯入的关闭)
    boolean isThough = false;   //人是否已经过了闸机
    String isA="初始值";   //是开的A门，还是B门
    boolean isOpened = false;  //门是不是打开了

    int recLen=0;
    TimerTask mTimerTask;
    Timer mTimer;
    ModeXMLBean modeXMLBean;
    ParamsBean paramsBean;   //得到http请求传入的参数

    private long version = Long.valueOf("20181116");


    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message

            switch (msg.what) {

                case 1:   //扇门

                    recLen--;
                    txtView.setText("" + recLen);

                    //Log.i("123",recLen+"状态循环::"+status);
                    if(recLen <= 0||isThough){     //isThough为true 人已经通过扇门，扇门关闭

                        destroyTimer();
                         stopThread();

                        txtView.setVisibility(View.GONE);
                        //背景图片变成pass
                        Resources resources = getBaseContext().getResources();
                        Drawable drawable = resources.getDrawable(R.drawable.ren);
                        imageView.setBackgroundDrawable(drawable);

                        Log.i("123","从机");
                        setColor(CMD.b);   //恢复指示灯的颜色  (此处只有连接主机的pad才能复位)
                        zhujiConut =0;
                        recLen=0;
                        isThough =false;
                        status="81";
                        isA="初始值";

                        if(modeXMLBean.getType().equals("进站从机")||modeXMLBean.getType().equals("出站从机")){  //如果是从机，则需要告诉主机，线程已经结束
                            ParamsBean setBean = new ParamsBean("从机循环结束","white","chen",IpAndress);
                            String json = JSONObject.toJSONString(setBean);;
                            postString("http://172.16.0.13:8082/",json);   //主机的地址
                            isOpenDoor = false;
                        }

                    }
                    break;

                case 2:

                    if(zhujiConut>1){   //开门大于3秒后才开始去读取扇门状态
                        //Log.i("123","读取扇门状态");
                        messageTan = new MessageTan(CMD.REQUESTCODE70,CMD.DIRAUXOBddress);    //读取扇门打开状态  (每一秒读取一次)
                        serialPort.sendBuffer(messageTan.getBtAryTranData());
                    }

                    break;
            }

            super.handleMessage(msg);

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);    //隐藏标题栏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   //设置竖屏
        this.getWindow().setFlags(          //设置全屏
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image);
        txtView = findViewById(R.id.tv);
        imageView.setOnClickListener(this);
        xmlUtil = new XmlUtil(this);
        modeXMLBean = new ModeXMLBean();
        paramsBean = new ParamsBean();

        mHttpServer = HttpServerImpl.getInstance();

        try {
            mHttpServer.start();
        } catch (IOException e) {
            Log.d("123", "onCreate http start error :" ,e);
        }

//        Intent intent = new Intent(this,HttpService.class);
//        startService(intent);     //启动后台服务
        IpAndress = getLocalIpStr(this);   //得到本机的IP地址
        Log.i("123","IP地址:"+getLocalIpStr(this));
        Config.Config(this).setAutoUpdate(version);   //版本保存到本地

        mHttpServer.setOnOpenDoorListener(new HttpServerImpl.OnOpenDoorListener() {
            @Override
            public void onOpenDoor(String param) {
                paramsBean = JSON.parseObject(param,ParamsBean.class);   //请求的值

                if(paramsBean.getMsgType().equals("FaceRecognitionResult")){   //执行开门命令

                    //1、先区分是不是主机， 不是主机的话，需要发送http请求给主机，让主机开门
                    //2、再区分进站、出站等模式，根据不同的模式，发送不同的指令

                    if(modeXMLBean.getType().equals("进站主机")||modeXMLBean.getType().equals("出站主机")){
                        //说明是主机，连接的是门单元(注意::只有一台pad连接门单元,另外三台都是发送开门指令给连接门单元的pad让它来开门)

                        Log.i("123","状态>>>>>>"+status);
                       // if(!status.equals("80")){ //如果门开了，则不让再发开门指令
                            //直接发送开门指令
                            if(modeXMLBean.getWllx().equals("出站")){   //开B门
                                if(isA.equals("初始值")||isA.equals("CNTB")){
                                    isA="CNTB";
                                    messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRBMAddress, Util.byteStrToBytes((byte)0x10));
                                    serialPort.sendBuffer(messageTan.getBtAryTranData());
                                    isFirst = true;
                                    isOpened=false;
                                    ReadThrough(isA);  //读取寄存器的值
                                    // setColor(CMD.ared);   //A红B绿
                                }

                            }else if(modeXMLBean.getWllx().equals("进站")){  //开A门
                                if(isA.equals("初始值")||isA.equals("CNTA")){
                                    isA="CNTA";
                                    messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRAMAddress, Util.byteStrToBytes((byte)0x10));
                                    serialPort.sendBuffer(messageTan.getBtAryTranData());
                                    isFirst = true;
                                    isOpened=false;
                                    ReadThrough(isA);  //读取寄存器的值
                                    //setColor(CMD.agreen);   //A绿B红
                                }
                            }
                            //timeMothod();   //改变界面与倒计时
                            readSatus();
                      //  }

                    }else {

                        //发送POST请求给连门单元的PAD(主机)进行开门
                        ParamsBean setBean = new ParamsBean(modeXMLBean.getType(),"white","chen",IpAndress);
                        String json = JSONObject.toJSONString(setBean);
                        postString("http://172.16.0.13:8082/",json);   //主机的地址
                        isOpenDoor = true;
                    }

                } else{

                    SlaveIpAndress = paramsBean.getScore();  //得到从机的IP地址
                    isThough = false;

                    if(paramsBean.getMsgType().equals("出站从机")){   //开B门

                       // if(!status.equals("80")){   //门没开就发送开门指令

                        if(isA.equals("初始值")||isA.equals("CNTB")){
                            isA="CNTB";
                            messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRBMAddress, Util.byteStrToBytes((byte)0x10));
                            serialPort.sendBuffer(messageTan.getBtAryTranData());
                            isFirst = true;
                            isOpened=false;
                            ReadThrough(isA);  //读取寄存器的值
                            // setColor(CMD.ared);   //A红B绿
                            readSatus();
                        }

                     //   }

                    }else if(paramsBean.getMsgType().equals("进站从机")){  //开A门

                       // if(!status.equals("80")) {   //门没开就发送开门指令
                        if(isA.equals("初始值")||isA.equals("CNTA")) {
                            isA = "CNTA";
                            Log.i("123","开A门");
                            messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRAMAddress, Util.byteStrToBytes((byte)0x10));
                            serialPort.sendBuffer(messageTan.getBtAryTranData());
                            isFirst = true;
                            isOpened=false;
                            ReadThrough(isA);  //读取寄存器的值
                            // setColor(CMD.agreen);   //A绿B红
                            readSatus();
                        }

                      //  }

                    }else if(paramsBean.getMsgType().equals("false")){
                        isThough = false;
                        recLen=10;    //从机重新计时
                    }else if(paramsBean.getMsgType().equals("true")){   // 说明扇门已经关闭，人已经果果闸机, 从机PAD可以结束跑秒了
                        isThough = true;
                    }else if(paramsBean.getMsgType().equals("从机循环结束")){
                        //结束主机读取扇门的线程
                        //  (这边有个问题：假设跑秒快结束了，又来了一个开门信号，则会重新开门，但读取扇门状态的线程关闭了，
                        // 则不会再跑程序不会再倒计时与切换画面)
                      //  if(isThough){   //说明通过了，就关闭读取扇门的线程
                        stopThread();

                        status="81";
                        //改变灯的颜色
                        setColor(CMD.b);   //指示灯复位
                        isA="初始值";
                    }else  if (paramsBean.getMsgType().equals("80")){   //说明门开了
                        timeMothod();   //开始倒计时
                    }
                }
            }
        });
    }


    public void readSatus(){   //读取扇门状态
        isrun =true;
        zhujiConut =0;
        if(myThread==null){
            myThread = new MyThread();
            myThread.start();
        }

    }

    public  void  timeMothod(){    //改变界面与倒计时

        runOnUiThread(new Runnable() {    //更改界面在线程里面执行
            @Override
            public void run() {
                txtView.setVisibility(View.VISIBLE);
                //背景图片变成pass
                Resources resources = getBaseContext().getResources();
                Drawable drawable = resources.getDrawable(R.drawable.pass);
                imageView.setBackgroundDrawable(drawable);

            }
        });

        //已经结束或者还没有开始时。 (关门超时时间倒计时)
        recLen=10;
        status="80";  //默认为打开
        isThough = false;
        destroyTimer();   //销毁计时
        initTimer();    //初始化计时器

        // 参数：0，延时0秒后执行;1000，每隔1秒执行1次task。
        mTimer.schedule(mTimerTask, 0, 1000);   //开始计时

    }


    public class MyThread extends Thread{    //读取扇门状态

        @Override
        public void run() {
            super.run();

            while (isrun) {

                try {
                    zhujiConut++;
                    Thread.sleep(1000);
                    // handler.sendEmptyMessageDelayed(2, 3000);   //延迟3秒
                    handler.sendEmptyMessage(2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //初始化timer
    public void initTimer() {
        mTimerTask = new TimerTask() {

            @Override
            public void run() {

                Message message = new Message();
                message.what = 1;
                message.obj = recLen;
                handler.sendMessage(message);
            }
        };
        mTimer = new Timer();
    }


    public void destroyTimer() {    //销毁计数器

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

    }

    public void stopThread(){
        if (isrun) {
            isrun = false;
            myThread = null;
        }
    }

    @Override
    protected void onResume() {     //设置受控模式
        super.onResume();


        serialPort = SerialPortUtil.getInstance();   //初始化串口  (放在resume里面，不然切换到维修界面就会停止工作)

        //该方法是读取数据的回调监听，一旦读取到数据，就立马回调
        serialPort.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                final String receiveString = SerialDataUtils.ByteArrToHex(buffer).trim();   //得到读取表示扇门状态的寄存器0x0B的值
                Log.i("123","Main接收到的参数::::"+receiveString);
//                41 70 90 9B 80 80 81 80 80 80 8C 80 AD BE E9      关闭 81 80
//                41 70 90 9B 80 80 80 88 80 80 8C 80 AD B7 E9      打开 80 88
                //对值进行解析
                if(!Tools.isNull(receiveString)){   //不为空
                    String [] array = receiveString.split(" ");

                    if(array.length>11&&(array[3].equals("97")||array[3].equals("96"))){   //CNTB或CNTA
                        //41 70 90 97 80 80 80 80 80 80 80 87 AD BC ED 41 ED
                        jageIsThough( array[11]);
                    } else if(array.length>6&&array[3].equals("9B")){   //说明读到的是9B寄存器
                        status = array[6];   //保存读取到的状态
                        if(status.equals("80")){  //说明门打开了

                           // Log.i("123",isOpened+"计时--"+paramsBean);
                            if (!isOpened) {
                                isOpened=true;
                                if (paramsBean.getMsgType() != null) {

                                    if (paramsBean.getMsgType().equals("进站从机")) {

                                        setColor(CMD.agreen);   //A绿B红
                                        ParamsBean setBean = new ParamsBean(status, "80", "chen", IpAndress);
                                        String json = JSONObject.toJSONString(setBean);
                                        ;
                                        postString("http://" + SlaveIpAndress + ":8082/", json);   //主机发送给从机
                                        isOpenDoor = false;


                                    } else if (paramsBean.getMsgType().equals("出站从机")) {

                                        setColor(CMD.ared);   //A红B绿
                                        ParamsBean setBean = new ParamsBean(status, "80", "chen", IpAndress);
                                        String json = JSONObject.toJSONString(setBean);

                                        postString("http://" + SlaveIpAndress + ":8082/", json);   //主机发送给从机
                                        isOpenDoor = false;

                                    } else {   //主机
                                        if (modeXMLBean.getWllx().equals("出站")) {   //开B门
                                            setColor(CMD.ared);   //A红B绿
                                        } else if (modeXMLBean.getWllx().equals("进站")) {  //开A门
                                            setColor(CMD.agreen);   //A绿B红
                                        }

                                        timeMothod();   //改变界面与倒计时
                                    }

                                }
                            }
                        } else  if(!status.equals("80")) {   //闸门关闭状态值为81 (可以将判断改成!80，因为有的时候会出现82 83的情况)

                            if (!Tools.isNull(isA)) {  //不为空
                                ReadThrough(isA);
                            }
                        }
                    }
                }


                //(可以将判断改成!80，因为有的时候会出现82 83的情况)
                if(!status.equals("80")) {  //则发送请求给从机，结束跑秒进程，与切换图片
                    if (paramsBean.getMsgType() != null) {
                        if (paramsBean.getMsgType().equals("进站从机") || paramsBean.getMsgType().equals("出站从机")) {

                            //只要扇门关闭了， 不管是不是已经通过了闸机，主机都要发送信息给从机

                            ParamsBean setBean = new ParamsBean(isThough + "", "111", "chen", IpAndress);
                            String json = JSONObject.toJSONString(setBean);
                            ;
                            postString("http://" + SlaveIpAndress + ":8082/", json);   //主机发送给从机

                           // Log.i("123", "----------------" + isThough + "----------" + SlaveIpAndress);

                            isOpenDoor = false;
                            //paramsBean =null;    //把得到的参数至空

                        } /*else {    //进站主机的
                            if (!isThough) {
                                recLen = 11;
                            }
                        }*/
                    }
                }
            }
        });

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //判断sd卡是否存在

            String path = Environment.getExternalStorageDirectory()+"/txms.xml";
            isExists = xmlUtil.fileIsExists(path);//判断xml文件是否存在

            if(isExists){   //xml存在
                modeXMLBean = xmlUtil.readxml();
                try {
                    id = Integer.parseInt(modeXMLBean.getId());  //得到写入xml的模式值
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }else {  //如果xml不存在，则写入默认值

                modeXMLBean = new ModeXMLBean("1","受控进站","进站","进站主机");
                xmlUtil.savexml(modeXMLBean);   //写入默认的设备信息

            }
        }

        Log.i("123","模式::"+id);
        CMD.mode(id);  //得到通行类型

        //设置受控模式(60指令设置寄存器)
        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRAMAddress,Util.byteStrToBytes(CMD.DIRAMCode));
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRBMAddress,Util.byteStrToBytes(CMD.DIRBMCode));
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRGENAddress,Util.byteStrToBytes(CMD.DIRGENCode));
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRSET1Address,Util.byteStrToBytes(CMD.DIRSET1Code));
        serialPort.sendBuffer(messageTan.getBtAryTranData());

        //设置指示灯
        //byte  btte [] = {(byte)0x01,(byte)0x00,(byte)0x20,(byte)0x00};
//        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRAUXOAddress,Util.byteStrToBytes(CMD.b));
//        serialPort.sendBuffer(messageTan.getBtAryTranData());
        setColor(CMD.b);

    }

    public void jageIsThough(String str){   //判断是否通过闸机
        //41 70 90 97 80 80 80 80 80 80 80 80 AD BB ED 41 ED      //CTNB
        if(isFirst){  //第一次读到寄存器的值

            firstNum = str;    //得到初始值
            isFirst=false;
        }else {
            sencendNum =str;    //闸机关闭后的值

            if(!sencendNum.equals(firstNum)){   //说明人已经过闸(人过闸计数器+1)
                isThough = true;
            }else {
                isThough = false;
                if(recLen>0){   //说明倒计时还没有结束， 结束了就不要再重新计时了，属于正常超时
                    recLen =10;   //重新计时(主机才生效)------人逆向想过闸，关门后要重新计时
                }
                isOpened=false;   //倒计时标志位
            }
        }

    }

    public void ReadThrough (String str){   //读取寄存器 CNTA 、 CNTB 的值，判断乘客是否通过

        if(str.equals("CNTA")){
            //isA = "CNTA";
            messageTan = new MessageTan(CMD.REQUESTCODE70,CMD.DIRCNTAAddress);
            serialPort.sendBuffer(messageTan.getBtAryTranData());
        }else if(str.equals("CNTB")){
            //isA = "CNTB";
            messageTan = new MessageTan(CMD.REQUESTCODE70,CMD.DIRCNTBAddress);
            serialPort.sendBuffer(messageTan.getBtAryTranData());
        }
    }

    public void ReadAlarm (){   //读取寄存器Alrarm,判断是不是发生闯入

        messageTan = new MessageTan(CMD.REQUESTCODE70,CMD.DIRALARMAddress);
        serialPort.sendBuffer(messageTan.getBtAryTranData());
    }

    public void setColor(byte[] bytes){
        messageTan = new MessageTan(CMD.REQUESTCODE60,CMD.DIRAUXOAddress,Util.byteStrToBytes(bytes));
        serialPort.sendBuffer(messageTan.getBtAryTranData());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPort.closeSerialPort();
        mHttpServer.stop();
        destroyTimer();
        stopThread();
    }


    /**
     * 退出界面停止掉，以免影响后面的串口读取
     */
    @Override
    protected void onStop() {
        super.onStop();
        serialPort.closeSerialPort();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case  R.id.image:

                //跳转维修测试界面

                Intent intent = new Intent(MainActivityStand.this,RepairTetsActivty.class);
                startActivity(intent);

                break;
        }

    }

    public static String getLocalIpStr(Context context) {   //得到IP地址
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intToIpAddr(wifiInfo.getIpAddress());
    }

    private static String intToIpAddr(int ip) {
        return (ip & 0xff) + "." + ((ip>>8)&0xff) + "." + ((ip>>16)&0xff) + "." + ((ip>>24)&0xff);
    }

    /**
     * 使用post方式提交json字符串
     *
     * @param url     提交的路径
     * @param content 提交的内容
     */
    public void postString(String url, String content) {
        //构建一个RequestBody对象,,因为提交的是json字符串需要添加一个MediaType为"application/json",
        // 普通的字符串直接是null就可以了

     //   OkHttpClient okHttpClient = new OkHttpClient();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                .addInterceptor(new RetryIntercepter(3))//重试
                .build();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), content);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("123", "onFailure: " + e.getMessage());   //失败
                if (e instanceof SocketTimeoutException) {
                    //判断超时异常
                    Log.i("123","超时");
                }

                if (e instanceof ConnectException) {
                    //判断连接异常，
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("123", "onResponse: " + response.body().string());    //成功

               /* if(isOpenDoor){
                    timeMothod();   //改变界面与倒计时
                }*/
            }
        });
    }


    /**
     * 重试拦截器
     */
    public class RetryIntercepter implements Interceptor {

        public int maxRetry;//最大重试次数
        private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

        public RetryIntercepter(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            System.out.println("retryNum=" + retryNum);
            Response response = chain.proceed(request);
            while (!response.isSuccessful() && retryNum < maxRetry) {
                retryNum++;
                System.out.println("retryNum=" + retryNum);
                response = chain.proceed(request);
            }
            return response;
        }
    }

}
