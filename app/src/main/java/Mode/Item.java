package Mode;

import com.example.windy.smartremotepc.R;

import java.io.Serializable;

/**
 * Created by Windy on 03/03/2015.
 */
public class Item implements Serializable {
    String name;
    int rotateScreen=0;
    int icon= R.drawable.menu1;
    boolean whiteTheme=true,
        fullScreen=false,
        menuBar=false,
        layout=false,
        isConfig=true,
        isChangeControl=true,
        isTitleBar=true;

    public static final int AUTO_ROTATE =0;
    public static final int PORTRAIT=1;
    public static final int LANDSCAPE =2;

    public boolean isTitleBar() {
        return isTitleBar;
    }

    public void setTitleBar(boolean isTitleBar) {
        this.isTitleBar = isTitleBar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getRotateScreen() {
        return rotateScreen;
    }

    public void setRotateScreen(int rotateScreen) {
        this.rotateScreen = rotateScreen;
    }

    public boolean isWhiteTheme() {
        return whiteTheme;
    }

    public void setWhiteTheme(boolean whiteTheme) {
        this.whiteTheme = whiteTheme;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isMenuBar() {
        return menuBar;
    }

    public void setMenuBar(boolean menuBar) {
        this.menuBar = menuBar;
    }

    public boolean isLayout() {
        return layout;
    }

    public void setLayout(boolean layout) {
        this.layout = layout;
    }

    public boolean isConfig() {
        return isConfig;
    }

    public void setConfig(boolean isConfig) {
        this.isConfig = isConfig;
    }

    public boolean isChangeControl() {
        return isChangeControl;
    }

    public void setChangeControl(boolean isChangeControl) {
        this.isChangeControl = isChangeControl;
    }
}
