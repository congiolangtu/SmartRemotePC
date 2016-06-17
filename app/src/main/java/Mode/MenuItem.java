package Mode;

import android.app.Activity;

import com.example.windy.smartremotepc.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Windy on 22/02/2015.
 */
public class MenuItem {
    private String name;
    private String icon;
    public MenuItem(String name,String icon){
        this.name=name;
        this.icon=icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    /**
    Đọc file save xml các item đưa vào Arraylist
     */
    public static ArrayList<MenuItem> readXml(Activity context,String nameList){
        ArrayList<MenuItem> listItem = new ArrayList<MenuItem>();

        FileInputStream xml= null;
        try {
            xml=context.openFileInput(nameList+".xml") ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XmlPullParser parser=null;
            /*
            Kiểm tra xem đã có save trong dữ liệu chưa?
            chưa thì lấy form mặc định trong xml.
             */
        try {
            boolean skip=false;
            if (xml != null) {
                XmlPullParserFactory fc = XmlPullParserFactory.newInstance();
                parser = fc.newPullParser();
                parser.setInput(xml, "utf-8");
            } else if (nameList.equals(context.getString(R.string.title_form))) {
                parser = context.getResources().getXml(R.xml.form);
            } else skip=true;
            if(!skip) {
                int eventType = -1;
                String nodeName;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) {
                        nodeName = parser.getName();
                        if (nodeName.equals("item")) {
                            listItem.add(new MenuItem(parser.getAttributeValue(0),
                                    parser.getAttributeValue(1)));
                        }
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!nameList.equals(context.getString(R.string.title_form)))
            listItem.add(new MenuItem(context.getString(R.string.title_add),"0"));
        return listItem;

    }

    public final int getIdIcon(){
        int a=Integer.parseInt(icon);
        a=a+R.drawable.menu0;
        return a;
    }
}
