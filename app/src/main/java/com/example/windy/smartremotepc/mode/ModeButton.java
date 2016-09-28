package com.example.windy.smartremotepc.mode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.example.windy.smartremotepc.R;

import java.io.Serializable;

/**
 * Created by Windy on 05/03/2015.
 */
public abstract class ModeButton extends View implements Serializable {
    //thông tin vị trí kích thước theo tỉ lệ màn hình của button
    protected float x,y,height,width,xPer,yPer,heightPer,widthPer;
    protected String name="";
    //context chứa button
    //protected AbsoluteActivity context=null;
    protected Context context=null;
    //các biến sử dụng cho touch listen
    protected float x1TouchSave, y1TouchSave, x2RawView, y2RawView, x3Last, y3Last,x5,y5;
    protected float y4lastHeight, x4lastWidth;
    //các chế độ chạm và chế độ hiển thị
    protected int modeTouch=0;
    protected float mode=1;
    //Hình ảnh hoặc màu cho icon chạm và k chạm
    protected Drawable icon1=null,icon2=null;
    //chỉ số alpha và màu viền chạm và không chạm
    protected int alpha1=255,alpha2=255,colorBound1=0xffffffff,colorBound2=0xffffffff;
    //kiểm tra hiển thị các chế độ
    protected boolean isViewBound=true;
    protected int typeViewControl=0;
    //kích thước chạm góc để nhận chỉnh kích thước
    static int sizeTouchResize=50;



    //hình ảnh cho chỉnh kích thước
    protected NinePatchDrawable resizeButton=null;
    //kích thước layout hiển thị button
    public int heightPad = 480,widthPad=800;
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

    //chế độ điều khiển
    protected int modeViewControl=0;
    final static public int V_NORMAL=0;
    final static public int V_LEFT=1;
    final static public int V_RIGHT=2;
    final static public int V_TOP=3;
    final static public int V_BOTTOM=4;

    //loại view
    final static public int TYPE_BUTTON=0;
    final static public int TYPE_ANALOG=1;

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeightButton() {
        return (int)height;
    }

    public int getWidthButton() {
        return (int)width;
    }

    public float getxPer() {
        return xPer;
    }

    public float getyPer() {
        return yPer;
    }

    public float getHeightPer() {
        return heightPer;
    }

    public float getWidthPer() {
        return widthPer;
    }

    public boolean isViewBound() {
        return isViewBound;
    }

    public void setViewBound(boolean viewBound) {
        isViewBound = viewBound;
        invalidate();
    }

    public abstract void setTypeViewControl(int add);

    public void setPad(int width, int height){
        widthPad=width;
        heightPad=height;
    }

    public ModeButton(Context context) {
        super(context);
        this.context=context;
        if(resizeButton==null)resizeButton= (NinePatchDrawable)getResources().getDrawable(R.drawable.resize_button);
        icon1= new ColorDrawable(Color.WHITE);
        icon2=new ColorDrawable(0xff7f7f7f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setTranslationZ(50);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.restore();
        canvas.saveLayerAlpha(0,0,canvas.getWidth(),canvas.getHeight(),255,Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        resizeButton.setBounds(new Rect(0,0,(int)width,(int)height));
        resizeButton.draw(canvas);
    }

    /**
     * Xử lý chạm chung cho các loại view điều khiển
     * @param event
     * @return
     */
    public boolean touchListen(MotionEvent event){
        //resize
        if(mode>4){
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case  MotionEvent.ACTION_DOWN:
                    modeTouch=CLICK;
                    if(checkTouchResize(event))break;
                    modeResize=MODE_RESIZE_MOVE;
                    x3Last =getX();y3Last =getY();
                    x1TouchSave =event.getRawX();
                    y1TouchSave =event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(modeResize>1){
                        touchResize(event);
                    }
                    else if(modeResize==MODE_RESIZE_MOVE&&mode==MODE_VIEW_RESIZE_EDIT){
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

    /**
     * Khởi tạo view
     */
    public abstract void createView();
    /**
     * Kiểm tra xem đã chạm vào đúng control chưa
     * @param event
     * @return
     */
    public abstract boolean checkTouch(MotionEvent event);

    public void setIcon1(Drawable icon1) {
        this.icon1 = icon1;
    }

    public void setIcon2(Drawable icon2) {
        this.icon2 = icon2;
    }

    public void setAlpha1(int alpha1) {
        this.alpha1 = alpha1;
    }

    public void setAlpha2(int alpha2) {
        this.alpha2 = alpha2;
    }

    public void setColorBound1(int colorBound1) {
        this.colorBound1 = colorBound1;
    }

    public void setColorBound2(int colorBound2) {
        this.colorBound2 = colorBound2;
    }

    public void setX(float x) {
        super.setX(x);
        this.xPer = x/widthPad;
        this.x=x;
    }
    public void setPerX(float x){
        xPer=x;
        this.x=x*widthPad;
        super.setX(this.x);
    }

    public void setY(float y) {
        super.setY(y);
        this.yPer = y/heightPad;
        this.y=y;
    }
    public void setPerY(float y){
        yPer=y;
        this.y=y*heightPad;
        super.setY(this.y);
    }

    public void setHeight(int height) {
        this.heightPer = height/heightPad;
        this.height=height;
        this.getLayoutParams().height=height;
    }
    public void setPerHeight(float height) {
        this.heightPer = height;
        this.height=height*heightPad;
        this.getLayoutParams().height=(int)this.height;
    }

    public void setWidth(int width) {
        this.widthPer = width/widthPad;
        this.width=width;
        this.getLayoutParams().width=width;
    }

    /**
     *
     * @param width
     */
    public void setPerWidth(float width) {
        this.widthPer = width;
        this.width=width*widthPad;
        this.getLayoutParams().width=(int)this.width;
    }

    public void setMode(int mode){
        this.mode=mode;
    }

    /**
     * Kiểm tra xem có chạm vào vùng chỉnh kích thước không
     * @param event
     * @return
     */
    protected boolean checkTouchResize(MotionEvent event){
        float x=event.getX();float y=event.getY();float i=2048,a,b;
        //Kiểm tra 1 góc và kiểm tra các góc còn lại xem chạm gần góc nào nhất
        if(x>width-sizeTouchResize&&y>height-sizeTouchResize){
            modeResize=MODE_RESIZE_BOTTOM_RIGHT;
            x1TouchSave =width-x;
            y1TouchSave =height-y;
            i=(x1TouchSave*x1TouchSave)+(y1TouchSave*y1TouchSave);
            x2RawView =event.getRawX()-width/2+ x1TouchSave;
            y2RawView =event.getRawY()-height/2+ y1TouchSave;
        }
        if(x<sizeTouchResize&&y>height-sizeTouchResize){
            b=height-y;a=(x*x)+(b*b);
            if(a<i) { //tìm xem chạm gần góc nào nhất
                i=a;
                modeResize = MODE_RESIZE_BOTTOM_LEFT;
                x1TouchSave = x; y1TouchSave = b;
                x2RawView = event.getRawX() + width / 2 - x;
                y2RawView = event.getRawY() - height / 2 + y1TouchSave;
            }
        }
        if(x<sizeTouchResize&&y<sizeTouchResize){
            a=(x*x)+(y*y);
            if(a<i) {
                i=a;
                modeResize = MODE_RESIZE_TOP_LEFT;
                x1TouchSave = x;y1TouchSave = y;
                x2RawView = event.getRawX() + width / 2 - x;
                y2RawView = event.getRawY() + height / 2 - y;
            }
        }
        if(x>width-sizeTouchResize&&y<sizeTouchResize){
            b=width-x;a=(b*b)+(y*y);
            if(a<i) {
                i=a;
                modeResize = MODE_RESIZE_TOP_RIGHT;
                x1TouchSave = b;y1TouchSave = y;
                x2RawView = event.getRawX() - width / 2 + x1TouchSave;
                y2RawView = event.getRawY() + height / 2 - y;
            }
        }
        if(i==2048) modeResize=0;
        if(modeResize!=0) { //nếu chạm chỉnh kích thước
            x3Last =getX();
            y3Last =getY();
            y4lastHeight = height/2;
            x4lastWidth = width/2;
            return true;
        }
        return false;
    }

    /**
     * Hàm xử lý chạm chỉnh khích thước view theo ngón tay
     * @param event
     */
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
            if(modeResize==MODE_RESIZE_BOTTOM_RIGHT){
                deltaY=y- y2RawView + y1TouchSave;deltaX=x- x2RawView + x1TouchSave;//khoảng cách góc view con trỏ tới tâm
                newHeight=(deltaY+ y4lastHeight);
                newWidth=(deltaX+ x4lastWidth);
                deltaX=0;deltaY=0;
            }
            else if(modeResize==MODE_RESIZE_BOTTOM_LEFT){
                deltaY=y- y2RawView + y1TouchSave;deltaX= x2RawView -x+ x1TouchSave;//khoảng cách góc view con trỏ tới tâm
                newHeight=(deltaY+ y4lastHeight);
                newWidth=(deltaX+ x4lastWidth);
                deltaX=-deltaX+ x4lastWidth;
                deltaY=0;
            }
            else if(modeResize==MODE_RESIZE_TOP_LEFT){
                deltaY= y2RawView -y+ y1TouchSave;deltaX= x2RawView -x+ x1TouchSave;//khoảng cách góc view con trỏ tới tâm
                newHeight=(deltaY+ y4lastHeight);
                newWidth=(deltaX+ x4lastWidth);
                deltaX=-deltaX+ x4lastWidth;
                deltaY=-deltaY+ y4lastHeight;
            }
            else if(modeResize==MODE_RESIZE_TOP_RIGHT){
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
        this.setWidth((int) newWidth);
        requestLayout();
        this.invalidate();
    }

    /**
     * Lấy bitmap oval
     * @param bitmap
     * @param rectIn
     * @return
     */
//    public Bitmap drawCircleBitmap(Canvas canvas, Rect rectIn) {
//
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(Color.RED);
//        canvas.drawOval(new RectF(rectIn), paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        Matrix matrix=new Matrix();
//        matrix.setRectToRect(new RectF(rect), new RectF(rectIn), Matrix.ScaleToFit.CENTER);
//        canvas.drawBitmap(bitmap,matrix, paint);
//
//        bitmap.recycle();
//
//        return output;
//    }

}
