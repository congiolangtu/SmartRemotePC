package myItem;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
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
    public FunctionListView(Context context, AbsListView.LayoutParams params) {
        super(context);
        this.setLayoutParams(params);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null){
            inflater.inflate(R.layout.choose_function_button, this);}
        popupKey= (LinearLayout) this.findViewById(R.id.popupKeyCFB);
        icon=(ImageView) this.findViewById(R.id.imgFunctionCFB);
    }

}
