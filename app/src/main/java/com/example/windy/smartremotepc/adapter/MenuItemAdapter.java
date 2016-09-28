package com.example.windy.smartremotepc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windy.smartremotepc.R;

import java.util.ArrayList;

import com.example.windy.smartremotepc.mode.MenuItem;

/**
 * Created by Windy on 22/02/2015.
 */
public class MenuItemAdapter extends ArrayAdapter<MenuItem>{

    Activity context=null;
    int layoutId;
    ArrayList<MenuItem> listItem=null;

    public MenuItemAdapter(Activity context,int layoutID,ArrayList<MenuItem> listItem){
        super(context,layoutID,listItem);
        this.context=context;
        this.layoutId=layoutID;
        this.listItem=listItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView=inflater.inflate(layoutId,null);
        }

        ImageView icon=(ImageView) convertView.findViewById(R.id.imageItem);
        TextView text= (TextView) convertView.findViewById(R.id.textItem);
        icon.setImageResource(getItem(position).getIdIcon());
        text.setText(getItem(position).getName());
        return convertView;
    }
}

