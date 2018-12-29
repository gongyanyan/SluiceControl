package com.example.pc.sluicecontrol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Xml;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pc.sluicecontrol.common.BaseActivity;
import com.example.pc.sluicecontrol.common.adapter.BillDetailSetDialogAdapter;
import com.example.pc.sluicecontrol.common.config.Contances;
import com.example.pc.sluicecontrol.common.utils.XmlUtil;
import com.lidroid.xutils.ViewUtils;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

public class RepairTetsActivty extends BaseActivity{

    Button btn1,btn2,btn3,btn4,btn5;
    private static final int ChooseMS = 10;   //选择通行模式
    XmlUtil xmlUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //设置横屏
        setMainContentLayout(R.layout.repairtest);

        initView();
        initData();
    }



    @Override
    protected void initView() {

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);

        xmlUtil = new XmlUtil(this);

    }

    @Override
    protected void initData() {
        setMiddleTitle("设备维修");
        //setRightTitle("设置模式");
    }

    @Override
    protected void initClickListener(View v) {

        switch (v.getId()){
            case R.id.btn1:
                Intent intent = new Intent(RepairTetsActivty.this,GateTetsActivty.class);
                startActivity(intent);


                break;

            case R.id.btn2:

                Intent intent1 = new Intent(RepairTetsActivty.this,TrafficTetsActivty.class);
                startActivity(intent1);
                break;

            case R.id.btn3:

                //showMyDialog(ChooseMS);
                Intent intent3 = new Intent(RepairTetsActivty.this,DeviceSetActivty.class);
                startActivity(intent3);

                break;

            case R.id.btn4:

                Intent intent4 = new Intent(RepairTetsActivty.this,AppManageActivty.class);
                startActivity(intent4);

                break;

            case R.id.btn5:

                Intent intent5 = new Intent(RepairTetsActivty.this,ParamsSetActivty.class);
                startActivity(intent5);

                break;
        }

    }

    /**
     * 弹框选择
     */
    private void showMyDialog(final int type) {
        // 弹出自定义dialog
        LayoutInflater inflater = LayoutInflater.from(RepairTetsActivty.this);
        View view = inflater.inflate(R.layout.dialog_chose, null);

        // 对话框
        final Dialog dialog = new Dialog(RepairTetsActivty.this);
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


        final ListView grid1 = (ListView) view.findViewById(R.id.grid1);
       if(type==ChooseMS){
            grid1.setAdapter(new BillDetailSetDialogAdapter(this, Contances.LIST_BILL_MS));
        }
        grid1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(type == ChooseMS){

                    int mode =position+1;
                    //xmlUtil.savexml(mode,Contances.LIST_BILL_MS.get(position));
                }
                dialog.cancel();
            }
        });
    }

    @Override
    public void onKeyDownBack() {

    }


}
