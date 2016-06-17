package Mode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.windy.smartremotepc.EditButtonActivity;
import com.example.windy.smartremotepc.R;

import java.io.Serializable;

/**
 * Created by Windy on 05/03/2015.
 */
public abstract class ModeButton extends View implements Serializable {
    //thông tin vị trí kích thước theo tỉ lệ màn hình của button
    protected float x,y,height,width,xPer,yPer,heightPer,widthPer;
    protected String name="BUTTON";
    //context chứa button
    protected Context context=null;
    //các biến sử dụng cho touch listen
    protected float x1TouchSave, y1TouchSave, x2RawView, y2RawView, x3Last, y3Last,x5,y5;
    protected float y4lastHeight, x4lastWidth;
    protected int modeTouch=0;
    protected float mode=1;
    //public static Bitmap resizeButton=null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected NinePatchDrawable resizeButton=null;
    public static int heightPad = 1,widthPad=1;
    protected int modeResize;

    //các kiểu touch ở chế độ resize
    final static public int MODE_RESIZE_TOP_LEFT=2;
    final static public int MODE_RESIZE_TOP_RIGHT=3;
    final static public int MODE_RESIZE_BOTTOM_RIGHT=4;
    final static public int MODE_RESIZE_BOTTOM_LEFT=5;
    final static public int MODE_RESIZE_MOVE=1;

    //các chế độ hiển thị button
    final static public int MODE_VIEW_RESIZE_EDIT = 5;
    final static public int MODE_VIEW_PLAY = 1;
    final static public int MODE_VIEW_EDIT_FUNCTION = 2;
    final static public int MODE_VIEW_RESIZE_CREATE = 6;

    //các kiểu chạm
    final static public int TIME_GESTURE=10;
    final static public int NONE=0;
    final static public int CLICK=1;
    final static public int GESTURE=2;

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public int getHeightButton() {
        return (int)height;
    }

    public int getWidthButton() {
        return (int)width;
    }

    public ModeButton(Context context) {
        super(context);
        this.context=context;

        if(resizeButton==null)resizeButton= (NinePatchDrawable)getResources().getDrawable(R.drawable.resize_button);
    }

    public boolean touchListen(MotionEvent event){
        //resize
        if(true){
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case  MotionEvent.ACTION_DOWN:
                    modeTouch=CLICK;
                    if(checkTouchResize(event))break;
                    modeResize=MODE_RESIZE_MOVE;
                    x3Last =getX();y3Last =getY();
                    x1TouchSave =event.getRawX();
                    y1TouchSave =event.getRawY();
                    //context.xFTouch=event.getX();
                    //context.yFTouch=event.getY();
                    // context.timeTouch=System.currentTimeMillis();
                    //Message m = new Message();
                    //myHandler.sendMessageDelayed(m, TIME_GESTURE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(modeResize>1){
                        touchResize(event);
                    }
                    else if(modeResize==MODE_RESIZE_MOVE){
                        float x,y;
                        x=event.getRawX();y=event.getRawY();
                        setX(x- x1TouchSave + x3Last);
                        setY(y- y1TouchSave + y3Last);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    modeResize=0;
                    return false;
                //GESTURE



            }
            return true;
        }
        return false;
    }
    public abstract void createView();
    public abstract boolean checkTouch(MotionEvent event);

    public void setX(float x) {
        super.setX(x);
        this.xPer = x/widthPad;
        this.x=x;
    }


    public void setY(float y) {
        super.setY(y);
        this.yPer = y/heightPad;
        this.y=y;
    }

    public void setHeight(int height) {
       // this.heightPer = height/heightPad;
        if(this.getLayoutParams()!=null)this.getLayoutParams().height=height;
        this.height=height;
    }

    public void setWidth(int width) {
      //  this.widthPer = width/widthPad;
        if(this.getLayoutParams()!=null)this.getLayoutParams().width=width;
        this.width=width;
    }

    public void setMode(int mode){
        this.mode=mode;
    }

    protected boolean checkTouchResize(MotionEvent event){
        float x=event.getX();float y=event.getY();float i=2048,a,b;
        if(x>width-32&&y>height-32){Log.i("test touch","1");
            modeResize=MODE_RESIZE_BOTTOM_RIGHT;
            x1TouchSave =width-x;
            y1TouchSave =height-y;
            i=(x1TouchSave*x1TouchSave)+(y1TouchSave*y1TouchSave);
            x2RawView =event.getRawX()-width/2+ x1TouchSave;
            y2RawView =event.getRawY()-height/2+ y1TouchSave;
        }
        if(x<32&&y>height-32){Log.i("test touch","2");
            b=height-y;a=(x*x)+(b*b);
            if(a<i) {
                i=a;Log.i("test touch","2");
                modeResize = MODE_RESIZE_BOTTOM_LEFT;
                x1TouchSave = x; y1TouchSave = b;
                x2RawView = event.getRawX() + width / 2 - x;
                y2RawView = event.getRawY() - height / 2 + y1TouchSave;
            }
        }
        if(x<32&&y<32){Log.i("test touch","3");
            a=(x*x)+(y*y);
            if(a<i) {
                i=a;Log.i("test touch","3");
                modeResize = MODE_RESIZE_TOP_LEFT;
                x1TouchSave = x;y1TouchSave = y;
                x2RawView = event.getRawX() + width / 2 - x;
                y2RawView = event.getRawY() + height / 2 - y;
            }
        }
        if(x>width-32&&y<32){Log.i("test touch","4");
            b=width-x;a=(b*b)+(y*y);
            if(a<i) {
                i=a;Log.i("test touch","4");
                modeResize = MODE_RESIZE_TOP_RIGHT;
                x1TouchSave = b;y1TouchSave = y;
                x2RawView = event.getRawX() - width / 2 + x1TouchSave;
                y2RawView = event.getRawY() + height / 2 - y;
            }
        }
        if(i==2048) modeResize=0;
        if(modeResize!=0) {Log.i("test touch","5");
            x3Last =getX();
            y3Last =getY();
            y4lastHeight = height/2;
            x4lastWidth = width/2;
            return true;
        }Log.i("test touch","6");
        return false;
    }

    protected void touchResize(MotionEvent event){
        float x=event.getRawX();float y=event.getRawY();
        float newHeight=0; float newWidth=0;
        float deltaX=0,deltaY=0;
        //Layout editbutton
        if(mode==MODE_VIEW_RESIZE_CREATE) {
            newWidth=((Math.abs(x- x2RawView)+ x1TouchSave)*2);
            newHeight=((Math.abs(y- y2RawView)+ y1TouchSave)*2);
            if((((View)this.getParent()).getWidth()-newWidth)<10)newWidth=(int)width;
            if((((View)this.getParent()).getHeight()-newHeight)<10)newHeight=(int)height;
        }
        //Layout play edit resize
        else{
            if(modeResize==MODE_RESIZE_BOTTOM_RIGHT){Log.i("test touch","br");
                deltaY=y- y2RawView + y1TouchSave;deltaX=x- x2RawView + x1TouchSave;//khoảng cách góc view con trỏ tới tâm
                newHeight=(deltaY+ y4lastHeight);
                newWidth=(deltaX+ x4lastWidth);
                deltaX=0;deltaY=0;
            }
            else if(modeResize==MODE_RESIZE_BOTTOM_LEFT){Log.i("test touch","bl");
                deltaY=y- y2RawView + y1TouchSave;deltaX= x2RawView -x+ x1TouchSave;//khoảng cách góc view con trỏ tới tâm
                newHeight=(deltaY+ y4lastHeight);
                newWidth=(deltaX+ x4lastWidth);
                deltaX=-deltaX+ x4lastWidth;
                deltaY=0;
            }
            else if(modeResize==MODE_RESIZE_TOP_LEFT){Log.i("test touch","tl");
                deltaY= y2RawView -y+ y1TouchSave;deltaX= x2RawView -x+ x1TouchSave;//khoảng cách góc view con trỏ tới tâm
                newHeight=(deltaY+ y4lastHeight);
                newWidth=(deltaX+ x4lastWidth);
                deltaX=-deltaX+ x4lastWidth;
                deltaY=-deltaY+ y4lastHeight;
            }
            else if(modeResize==MODE_RESIZE_TOP_RIGHT){Log.i("test touch","tr");
                deltaY= y2RawView -y+ y1TouchSave;deltaX=x- x2RawView + x1TouchSave;//khoảng cách góc view con trỏ tới tâm
                newHeight=(deltaY+ y4lastHeight);
                newWidth=(deltaX+ x4lastWidth);
                deltaY=-deltaY+ y4lastHeight;
                deltaX=0;
            }

            if(newWidth<16||newHeight<16){
                return;
                //x1TouchSave=0;y1TouchSave=0;
            }
            else if(deltaX!=0||deltaY!=0){setX(x3Last +deltaX);setY(y3Last +deltaY);}
        }
        this.setHeight((int) newHeight);
        this.setWidth((int) newWidth);//Log.i("test",deltaX+" - "+deltaY);
        requestLayout();
        this.invalidate();
    }

    public Bitmap getCircleBitmap(Bitmap bitmap, Rect rectIn) {
        final Bitmap output = Bitmap.createBitmap(rectIn.width(),rectIn.height(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.RED);
        canvas.drawOval(new RectF(rectIn), paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Matrix matrix=new Matrix();
        matrix.setRectToRect(new RectF(rect), new RectF(rectIn), Matrix.ScaleToFit.CENTER);
        canvas.drawBitmap(bitmap,matrix, paint);

        bitmap.recycle();

        return output;
    }
}
