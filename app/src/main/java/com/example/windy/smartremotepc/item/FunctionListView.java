package com.example.windy.smartremotepc.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.windy.smartremotepc.R;

/**
 * Created by Windy on 24/03/2016.
 */
public class FunctionListView extends LinearLayout {

    public ImageView icon;
    public LinearLayout popupKey;
    public FunctionListView(Context context, int widthParent) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null){
            inflater.inflate(R.layout.choose_function_button, this);}
        popupKey= (LinearLayout) this.findViewById(R.id.popupKeyCFB);
        int height=(int)(widthParent/7+(12.17f*getResources().getDisplayMetrics().density));
        popupKey.getLayoutParams().height=height;
        icon=(ImageView) this.findViewById(R.id.imgFunctionCFB);
        icon.getLayoutParams().height=height;
        this.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height+2));
    }

}
