package Mode;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Windy on 16/02/2016.
 */
public class FunctionItem {
    private int icon;
    private LinearLayout panelKey;
    View[] viewKeys=new View[5];
    int[] keys=new int[5];

    public FunctionItem(int icon) {
        this.icon = icon;
    }
    public FunctionItem(int icon,int[] keys) {
        this.icon = icon;
        this.keys=keys;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public LinearLayout getPanelKey() {
        return panelKey;
    }

    public void setPanelKey(LinearLayout panelKey) {
        this.panelKey = panelKey;
    }

    public View[] getViewKeys() {
        return viewKeys;
    }

    public void setViewKeys(View[] viewKeys) {
        this.viewKeys = viewKeys;
    }

    public int[] getKeys() {
        return keys;
    }

    public void setKeys(int[] keys) {
        this.keys = keys;
    }
}
