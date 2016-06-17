package fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.windy.smartremotepc.AddItemActivity;
import com.example.windy.smartremotepc.KeyboardActivity;
import com.example.windy.smartremotepc.MainActivity;
import com.example.windy.smartremotepc.R;
import com.example.windy.smartremotepc.RemotePCActivity;
import com.example.windy.smartremotepc.SlideShowActivity;

import java.util.ArrayList;

import Mode.MenuItem;
import adapter.MenuItemAdapter;

/**
 * Created by Windy on 20/02/2015.
 */
public class MenuItemFragment extends Fragment{

    private ArrayList<MenuItem> arrayItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);
        GridView grid = (GridView) rootView.findViewById(R.id.gridView);
        arrayItem = MenuItem.readXml(getActivity(),getArguments().getString("name"));
        grid.setAdapter(new MenuItemAdapter
                (
                        getActivity(),
                        R.layout.item_menu,
                        arrayItem
                ));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickItem(position);
            }
        });
        return rootView;
    }

    void clickItem(int position){
        Intent i=new Intent();
        Class<?> cls = null;Log.i("test",""+arrayItem.get(position).getName());
        switch (arrayItem.get(position).getName()){
            case "Add":
                cls=AddItemActivity.class;
                break;
            case "Slide Show":
                cls=SlideShowActivity.class;
                break;
            case "Youtube":
                cls= RemotePCActivity.class;
                break;
            case "Keyboard":
                cls= KeyboardActivity.class;
                break;
            default: return;
        }
        i.setClass(getActivity(),cls);
        startActivity(i);
    }
}
