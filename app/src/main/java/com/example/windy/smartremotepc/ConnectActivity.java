package com.example.windy.smartremotepc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Windy on 10/03/2015.
 */
public class ConnectActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ActionBar actionBar = getSupportActionBar();
    }
}

//public class ConnectPager extends FragmentPagerAdapter{
//
//    @Override
//    public Fragment getItem(int i) {
//        return null;
//    }
//}
