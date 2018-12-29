package com.example.pc.sluicecontrol.common.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.pc.sluicecontrol.common.config.Config;

import java.io.File;

/**
 * Created by Administrator on 2016/9/13.
 */
public class UpdateApkUtils {

    private static final String TAG = "MainActivity";
    private Context context ;
    //private static final String HTTPPATH = "http://140.206.74.202:8088/apk/mc3.apk";

    //private String url ;
    private String NAMW = "sluice.apk" ;

    /**
     * 下载 ID
     */
    private long reference;

    public UpdateApkUtils(Context context ,String url){
        this.context = context;

      /*  if(Tools.isNull(Config.Config(context).getApk_URL())){   //如果没有重新设置升级地址，则调用默认的地址
            Config.Config(context).setApk_URL("sc.baison.com.cn:8088");
        }

        url = "http://"+Config.Config(context).getApk_URL()+"/apk/mc3.apk";*/

        downloadFile(url,NAMW);
    }


//    @Override
//    public void onClick(View v) {
//        downloadFile(httpPath, "M-DAQ.apk");
//    }

    /**
     * 下载文件 （版本更新下载）
     *
     * @param httpPath 更新地址
     * @param name     更新文件名
     */
    private void downloadFile(String httpPath, String name) {
        if (httpPath == null || !httpPath.startsWith("http")) {
            Toast.makeText(context, "下载地址异常", Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), "下载地址异常", Toast.LENGTH_LONG).show();
            return;
        }
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (reference != 0) {
            if (!isDownSuccess(downloadManager)) {
                Toast.makeText(context, "下载中...", Toast.LENGTH_LONG).show();
                return;
            }
            DownloadManager.Query query = new DownloadManager.Query();
            //在广播中取出下载任务的id
            query.setFilterById(reference);
            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                //获取文件下载路径
                String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                Log.i(TAG, "onClick: fileName   " + fileName);
                //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                if (fileName != null && fileName.endsWith(".apk")) {
                    unregisterReceiver();
                    //安装 apk
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                    context.startActivity(install);
                }
            }
            return;
        }
        Uri uri = Uri.parse(httpPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //设置下载中通知栏提示的标题
//        request.setTitle("停简单");
        //表示下载进行中和下载完成的通知栏是否显示。默认只显示下载中通知。
        // VISIBILITY_VISIBLE_NOTIFY_COMPLETED表示下载完成后显示通知栏提示。
        // VISIBILITY_HIDDEN表示不显示任何通知栏提示，这个需要在AndroidMainfest中添加权限android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //表示设置下载地址为sd卡的Download文件夹，文件名为 停简单.apk。
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //在这里返回的reference变量是系统为当前的下载请求分配的一个唯一的ID，我们可以通过这个ID重新获得这个下载任务，进行一些自己想要进行的操作或者查询
        reference = downloadManager.enqueue(request);
        registerReceiver();
        Toast.makeText(context, "开始下载", Toast.LENGTH_LONG).show();
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(broadcast, filter);
    }

    //下载广播监听
    BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: ");
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                DownloadManager.Query query = new DownloadManager.Query();
                //在广播中取出下载任务的id
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (reference != id) return;
                query.setFilterById(id);
                Cursor c = manager.query(query);
                if (c.moveToFirst()) {
                    //获取文件下载路径
                    String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                    if (fileName != null && fileName.endsWith(".apk")) {
                        //安装 apk
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                        context.startActivity(install);
                    }
                }
                unregisterReceiver();
            }
        }
    };

    /**
     * 是否下载成功
     *
     * @param downManager DownloadManager
     * @return boolean
     */
    private boolean isDownSuccess(DownloadManager downManager) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor cursor = downManager.query(query);
        try {
            while (cursor.moveToNext()) {
                int downId = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                if (downId == reference) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return false;
    }

    /**
     * 取消注册广播接收器
     */
    protected void unregisterReceiver() {
        try {
            context.unregisterReceiver(broadcast);
        } catch (Exception e) {

        }
    }
}
