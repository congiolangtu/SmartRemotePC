package com.example.windy.smartremotepc.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import com.example.windy.smartremotepc.mode.FunctionItem;
import com.example.windy.smartremotepc.item.FunctionListView;

/**
 * Created by Windy on 13/02/2016.
 */
public class ListFunctionAdapter extends ArrayAdapter<FunctionItem> {

    Activity context=null;
    int layoutId;
    ArrayList<FunctionItem> listFunction=null;

    public ListFunctionAdapter(Activity context,int layoutID,ArrayList<FunctionItem> list){
        super(context,layoutID,list);
        this.context=context;
        this.layoutId=layoutID;
        this.listFunction=list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = new FunctionListView(context,parent.getWidth());
            FunctionItem item = listFunction.get(position);
            ((FunctionListView) convertView).icon.setImageResource(item.getIcon());
            item.setPanelKey(((FunctionListView) convertView).popupKey);
        }
        return convertView;
    }
}
