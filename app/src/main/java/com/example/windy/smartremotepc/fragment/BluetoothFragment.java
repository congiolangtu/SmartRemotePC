package com.example.windy.smartremotepc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.windy.smartremotepc.R;

import com.example.windy.smartremotepc.adapter.ConnectListAdapter;

/**
 * Created by Windy on 11/03/2015.
 */
public class BluetoothFragment extends Fragment{

    String[] listNameDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list,container,false);
        ListView list= (ListView) rootView.findViewById(R.id.listView);
        list.setAdapter(new ConnectListAdapter(
                getActivity(),
                R.layout.item_connect,
                listNameDevice,
                ConnectListAdapter.Type_Bluetooth
        ));
        return rootView;
    }

}


