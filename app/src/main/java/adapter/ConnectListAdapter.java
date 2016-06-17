package adapter;

import android.app.Activity;
import android.text.StaticLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windy.smartremotepc.R;

/**
 * Created by Windy on 11/03/2015.
 */
public class ConnectListAdapter extends ArrayAdapter<String> {

    int layoutId;
    int type_Connect;
    String[] list;
    Activity context;
    TextView state;
    public static final int Type_Wifi = 1;
    public static final int Type_Bluetooth =2;

    public ConnectListAdapter(Activity context, int resource, String[] list,int typeConnect) {
        super(context, resource, list);
        this.layoutId=resource;
        this.list=list;
        this.context=context;
        this.type_Connect=typeConnect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=context.getLayoutInflater();
            convertView=inflater.inflate(layoutId,null);
        }
        ImageView icon= (ImageView) convertView.findViewById(R.id.imageIC);
        TextView name= (TextView) convertView.findViewById(R.id.textNameIC);
        state= (TextView) convertView.findViewById(R.id.textStateIC);
        if(type_Connect==Type_Wifi)
            icon.setImageResource(R.drawable.wifi);
        else
            icon.setImageResource(R.drawable.bluetooth);
        name.setText(getItem(position));
        return convertView;
    }

    public void setState(String states){
        state.setText(states);
    }
}
