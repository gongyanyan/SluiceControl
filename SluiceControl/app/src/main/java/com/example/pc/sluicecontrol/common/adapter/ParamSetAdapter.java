package com.example.pc.sluicecontrol.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.sluicecontrol.R;
import com.example.pc.sluicecontrol.common.SluiceParamBean;

import java.util.List;

public class ParamSetAdapter extends BaseAdapter{

    private Context context;
    private List<SluiceParamBean> sluiceParamBeanList;
    private LayoutInflater inflater;

    public ParamSetAdapter(Context context, List<SluiceParamBean> sluiceParamBeanList) {
        this.context = context;
        this.sluiceParamBeanList = sluiceParamBeanList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return sluiceParamBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return sluiceParamBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.listitem, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_unit = (TextView) view.findViewById(R.id.tv_unit);
            viewHolder.tv_value = (TextView) view.findViewById(R.id.tv_value);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.tv_name.setText(sluiceParamBeanList.get(i).getName());
        viewHolder.tv_unit.setText(sluiceParamBeanList.get(i).getUnit());
        viewHolder.tv_value.setText(sluiceParamBeanList.get(i).getValue()+"");


        return view;
    }


    class ViewHolder {
        private TextView tv_name,tv_unit,tv_value;

    }
}
