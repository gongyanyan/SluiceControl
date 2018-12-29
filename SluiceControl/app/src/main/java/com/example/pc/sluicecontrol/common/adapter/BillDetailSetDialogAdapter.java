package com.example.pc.sluicecontrol.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.pc.sluicecontrol.R;

import java.util.List;


/**
 * Created by Administrator on 2016/7/27.
 */
public class BillDetailSetDialogAdapter extends BaseAdapter {


    private Context mycontent;
    private List<String> beanList;
    private LayoutInflater inflater;
    public BillDetailSetDialogAdapter(Context mycontent, List<String> beanList){
        this.mycontent = mycontent;
        this.beanList = beanList;
        this.inflater = LayoutInflater.from(mycontent);

    }
    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int i) {
        return beanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_system_set_code, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_code = (TextView) view.findViewById(R.id.tv_code);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_code.setText(beanList.get(i));
        return view;
    }
    class ViewHolder
    {
        private TextView tv_code;
    }
}
