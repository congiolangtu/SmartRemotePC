package adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.windy.smartremotepc.R;

import java.util.ArrayList;

import Mode.FunctionItem;
import myItem.FunctionListView;

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
