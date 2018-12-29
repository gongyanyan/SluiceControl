package com.example.pc.sluicecontrol;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.sluicecontrol.common.BaseActivity;
import com.example.pc.sluicecontrol.common.CommonProgressDialog;
import com.example.pc.sluicecontrol.common.config.Config;
import com.example.pc.sluicecontrol.common.utils.Tools;
import com.example.pc.sluicecontrol.common.utils.UpdateApkUtils;
import com.example.pc.sluicecontrol.common.utils.XmlUtil;
import com.example.pc.sluicecontrol.serial.CMD;
import com.example.pc.sluicecontrol.serial.MessageTan;
import com.example.pc.sluicecontrol.serial.SerialPortUtil;
import com.example.pc.sluicecontrol.serial.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AppManageActivty extends BaseActivity implements CommonProgressDialog.OnOkAndCancelListener {

    Button btn1, btn2;
    TextView tv_Version, tv_num, tv_evt, tv_address;
    String url;
    MyAsyncTask myAsyncTask;
    ProgressDialog mProgressDialog;
    int fileLength;  //所下载文件的总大小
    //方式三
    CommonProgressDialog mDialog;  //自定义的Dialog
    DownloadAsyncTask downloadAsyncTask;

    XmlUtil xmlUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置横屏
        setMainContentLayout(R.layout.appmanage);

        initView();
        initData();
    }


    @Override
    protected void initView() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        tv_Version = findViewById(R.id.tv_Version);
        tv_num = findViewById(R.id.tv_num);
        tv_evt = findViewById(R.id.tv_evt);
        tv_address = findViewById(R.id.tv_address);
        xmlUtil = new XmlUtil(this);
    }

    @Override
    protected void initData() {
        setMiddleTitle("APP管理");

        initdialog();   //方式二原生的Dialog

        mDialog = new CommonProgressDialog(this);

        mDialog.setMessage("正在下载...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setIndeterminate(true);
        //mDialog.setMax(100 * 1024 * 1024);
        mDialog.setCancelable(true);
        mDialog.setOnOkAndCancelListener(this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去掉白色背景

    }

    private void initdialog() {
        mProgressDialog = new ProgressDialog(AppManageActivty.this);
        mProgressDialog.setMessage("正在下载...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setButton("取消", new SureButtonListener());

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                myAsyncTask.cancel(true);
            }
        });
    }

    @Override
    public void onCancel(View v) {    //重写只定义Dialog的方法
        mDialog.dismiss();
        mDialog.setButtonUnclick();

    }

    @Override
    public void onInstall(View v) {  //重写只定义Dialog的方法   安装APK文件

        String path ="/sdcard/sluice.apk";

        if (xmlUtil.fileIsExists(path)) {   //判断xml文件是否存在
            //安装 apk
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            startActivity(install);
        }else {
            toastMsg("apk文件不存在，无法安装");
        }
    }

    //Dialog中确定按钮的监听器
    private class SureButtonListener implements android.content.DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int which) {
            //点击“取消按钮”取消对话框
            dialog.cancel();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        tv_Version.setText(Config.Config(this).getAutoUptade() + "");
        tv_evt.setText(Config.Config(this).getEVTNAME());
        url = Config.Config(this).getURL() + ":" + Config.Config(this).getPORT();
        tv_address.setText(url);  //显示地址
    }

    @Override
    protected void initClickListener(View v) {
        switch (v.getId()) {
            case R.id.btn1:   //切换服务器地址

                Intent intent1 = new Intent(AppManageActivty.this, ChangeServerActivty.class);
                startActivity(intent1);

                break;

            case R.id.btn2:   //检查apk版本
                //showLoadingView();

                if (!Tools.isNull(url)) {
                    String urlAddress = url + "/sluice.apk";

                    //方式一
                    //UpdateApkUtils updateApkUtils = new UpdateApkUtils(AppManageActivty.this,urlAddress);

                    //方式二(原生的)
                 /*   myAsyncTask = new MyAsyncTask(AppManageActivty.this);
                    myAsyncTask.execute(urlAddress);  */

                    //方式二(原生的)
                    downloadAsyncTask = new DownloadAsyncTask(AppManageActivty.this);
                    downloadAsyncTask.execute(urlAddress);
                }

                break;
        }
    }


    private class DownloadAsyncTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/sluice.apk");
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        //publishProgress((int) (total * 100 / fileLength));
                        publishProgress((int) total);   //当前下载了多少
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mDialog.setIndeterminate(false);
            mDialog.setMax(fileLength);
            mDialog.setProgress(progress[0]);  //当前显示下载了多少

        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            //mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                mDialog.setButton();    //点亮安装按钮
        }
    }



    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public MyAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/sluice.apk");
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        //publishProgress((int) (total * 100 / fileLength));
                        publishProgress((int) total);   //当前下载了多少
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            //mProgressDialog.setMax(100);
            mProgressDialog.setMax(fileLength);
            mProgressDialog.setProgress(progress[0]);  //当前显示下载了多少
            mProgressDialog.setProgressNumberFormat(getPrintSize(progress[0]) + " /" + getPrintSize(fileLength));  //转化成MB显示
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            //mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }
    }


    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if(size< 1024){
            return String.valueOf(size) + "KB";
        } else{
            size = size / 1024;
        }
        if(size< 1024){
            //因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else{
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }


    @Override
    public void onKeyDownBack() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
