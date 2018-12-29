package com.example.pc.sluicecontrol.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * author:wudu
 * <p/>
 * 2016/1/7.
 * <p/>
 * <p/>
 * sharedpreference保存list集合
 */
public class SharedListString {
    public static final String SHARED_LIST_STRING = "shared_list_string";

    /**
     * 集合转为string
     *
     * @param SceneList
     * @return
     * @throws IOException
     */
    public static String SceneList2String(List<Object> SceneList)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }


    /**
     * String转集合
     *
     * @param SceneListString
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static List String2SceneList(String SceneListString) throws IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List SceneList = (List) objectInputStream
                .readObject();
        objectInputStream.close();
        return SceneList;
    }


    /**
     * 保存集合
     *
     * @param context
     * @param list
     */
    public static void saveList2String(Context context, List list, String preferenceKey) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHARED_LIST_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mySharedPreferences.edit();
        try {
            String liststr = SceneList2String(list);
            edit.putString(preferenceKey, liststr);
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取集合
     *
     * @param context
     * @return
     */
    public static List getStirng2List(Context context, String preferenceKey) {
        List list = new ArrayList();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_LIST_STRING, Context.MODE_PRIVATE);
        String liststr = sharedPreferences.getString(preferenceKey, "");
        try {
            list = String2SceneList(liststr);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 去除重复数据，并保持顺序
     *
     * @param list
     */
    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

}
