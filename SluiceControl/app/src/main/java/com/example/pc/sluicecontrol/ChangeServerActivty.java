package com.example.pc.sluicecontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pc.sluicecontrol.common.BaseActivity;
import com.example.pc.sluicecontrol.common.config.Config;
import com.example.pc.sluicecontrol.common.utils.Tools;

public class ChangeServerActivty extends BaseActivity{

    Button btnsave1,btnsave2,btnchange1,btnchange2;
    EditText et_url1,et_port1,et_url2,et_port2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置横屏
        setMainContentLayout(R.layout.changeserver);

        initView();
        initData();
    }



    @Override
    protected void initView() {
        btnsave1 = findViewById(R.id.btnsave1);
        btnsave2 = findViewById(R.id.btnsave2);
        btnchange1 = findViewById(R.id.btnchange1);
        btnchange2 = findViewById(R.id.btnchange2);
        btnsave1.setOnClickListener(this);
        btnsave2.setOnClickListener(this);
        btnchange1.setOnClickListener(this);
        btnchange2.setOnClickListener(this);

        et_url1 = findViewById(R.id.et_url1);
        et_url2 = findViewById(R.id.et_url2);
        et_port1 = findViewById(R.id.et_port1);
        et_port2 = findViewById(R.id.et_port2);

    }

    @Override
    protected void initData() {
        setMiddleTitle("切换服务地址");

        //开发环境
        if (!Tools.isNull(Config.Config(this).getURLKF())){
            et_url1.setText(Config.Config(this).getURLKF());
        }else {
            Config.Config(this).setURLKF(et_url1.getText().toString().trim());   //设置默认的
        }

        if (!Tools.isNull(Config.Config(this).getPORTKF())){
            et_port1.setText(Config.Config(this).getPORTKF());
        }else {
            Config.Config(this).setPORTKF(et_port1.getText().toString().trim());   //设置默认的
        }

        //生产环境
        if (!Tools.isNull(Config.Config(this).getURLSC())){
            et_url2.setText(Config.Config(this).getURLSC());
        }else {
            Config.Config(this).setURLSC(et_url2.getText().toString().trim());   //设置默认的
        }

        if (!Tools.isNull(Config.Config(this).getPORTSC())){
            et_port2.setText(Config.Config(this).getPORTSC());
        }else {
            Config.Config(this).setPORTSC(et_port2.getText().toString().trim());   //设置默认的
        }


        ///////设备真实使用的
        if(Tools.isNull(Config.Config(this).getURL())){   //为空，则使用默认的 (默认为开发)
            Config.Config(this).setURL(Config.Config(this).getURLKF());
        }

        if(Tools.isNull(Config.Config(this).getPORT())){   //为空，则使用默认的 (默认为开发)
            Config.Config(this).setPORT(Config.Config(this).getPORTKF());
        }

        Config.Config(this).setEVTNAME("开发环境");

    }

    @Override
    protected void initClickListener(View v) {
        switch (v.getId()){
            case R.id.btnsave1:   //开发保存

                if(Tools.isNull(et_url1)){  //地址为空
                    toastMsg("开发网址为空");
                }else if(Tools.isNull(et_port1)){
                    toastMsg("开发网址端口为空");
                }else {
                    Config.Config(this).setURLKF(et_url1.getText().toString().trim());
                    Config.Config(this).setPORTKF(et_port1.getText().toString().trim());
                }

                break;

            case R.id.btnchange1:   //开发切换

                Config.Config(this).setURL(Config.Config(this).getURLKF());
                Config.Config(this).setPORT(Config.Config(this).getPORTKF());
                Config.Config(this).setEVTNAME("开发环境");

                finishPage();

                break;

            case R.id.btnsave2:   //保存

                if(Tools.isNull(et_url2)){  //地址为空
                    toastMsg("生产网址为空");
                }else if(Tools.isNull(et_port2)){
                    toastMsg("生产网址端口为空");
                }else {
                    Config.Config(this).setURLSC(et_url2.getText().toString().trim());
                    Config.Config(this).setPORTSC(et_port2.getText().toString().trim());
                }

                break;

            case R.id.btnchange2:   //切换

                Config.Config(this).setURL(Config.Config(this).getURLSC());
                Config.Config(this).setPORT(Config.Config(this).getPORTSC());
                Config.Config(this).setEVTNAME("生产环境");

                break;
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
