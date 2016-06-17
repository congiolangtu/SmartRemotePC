package adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.example.windy.smartremotepc.R;

import java.util.Locale;

import fragment.MenuItemFragment;

/**
 * Created by Windy on 05/02/2015.
 */
public class MenuItemPager extends FragmentPagerAdapter {

    public MenuItemPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        String name=null;
        switch (index) {
            case 0:
                name = "app";
                break;
            case 1:
                name = "form";
                break;
            case 2:
                name = "game";
                break;
        }
        Bundle bundle=new Bundle();
        bundle.putString("name",name);
        MenuItemFragment fragment = new MenuItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
