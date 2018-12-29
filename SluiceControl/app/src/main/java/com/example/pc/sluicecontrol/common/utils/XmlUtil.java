package com.example.pc.sluicecontrol.common.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.example.pc.sluicecontrol.RepairTetsActivty;
import com.example.pc.sluicecontrol.common.ModeXMLBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XmlUtil {

    private Context context;

    public XmlUtil (Context context)  {
        this.context=context;
    }

    /**
     * 向SD卡写入一个XML文件
     *
     * @param
     */
    public void savexml(ModeXMLBean modeXMLBean) {

        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "txms.xml");
            Log.i("123","路径：："+Environment.getExternalStorageDirectory());
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "modes");
     //       for (int i = 1; i < 10; i++) {
                serializer.startTag(null, "mode");
                serializer.attribute(null, "id", String.valueOf(modeXMLBean.getId()));
                // 写寄存器 DIRA(受控模式)
                serializer.startTag(null, "name");
                serializer.text(modeXMLBean.getName());
                serializer.endTag(null, "name");
                // 写闸机物理类型
                serializer.startTag(null, "wllx");
                serializer.text(modeXMLBean.getWllx());
                serializer.endTag(null, "wllx");
                // 写闸机类型
                serializer.startTag(null, "type");
                serializer.text(modeXMLBean.getType());
                serializer.endTag(null, "type");

                serializer.endTag(null, "mode");
       //     }
            serializer.endTag(null, "modes");
            serializer.endDocument();
            fos.close();
            Toast.makeText(context, "写入成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "写入失败", Toast.LENGTH_SHORT).show();
        }

    }



    /**
     * 读取SD卡中的XML文件,使用pull解析
     *
     * @param
     */
    public ModeXMLBean readxml() {

        String id =null;      //模式id
        String name = null;   //闸机模式
        String wllx = null;   //闸机物理类型  (进站)
        String type = null;   //闸机类型    (进站主机)
        ModeXMLBean modeXMLBean = new ModeXMLBean();

        try {
            File path = new File(Environment.getExternalStorageDirectory(),
                    "txms.xml");
            FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");

            int eventType = parser.getEventType(); // 获得事件类型

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称

                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点 <person>
                        if ("modes".equals(tagName)) { // <modes>
                        } else if ("mode".equals(tagName)) { // <mode id="1">
                            id = parser.getAttributeValue(null, "id");
                        } else if ("name".equals(tagName)) { // <name>
                            name = parser.nextText();
                        } else if ("wllx".equals(tagName)) { // <wllx>
                            wllx = parser.nextText();
                        } else if ("type".equals(tagName)) { // <type>
                            type = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // </modes>
                        if ("mode".equals(tagName)) {
                            Log.i("123", "id---" + id);
                            Log.i("123", "name---" + name);
                            modeXMLBean.setId(id);
                            modeXMLBean.setName(name);
                            modeXMLBean.setWllx(wllx);
                            modeXMLBean.setType(type);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return modeXMLBean;
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists()) {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

}
