package com.example.windy.smartremotepc.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.windy.smartremotepc.mode.ModeButton;

/**
 * Created by Windy on 08/06/2015.
 */
public class MyButton extends ModeButton implements View.OnTouchListener{
    protected boolean isSticky=false;
    protected boolean sticky=false;
    protected boolean isCircle=false;
    protected boolean isTouch=false;
    float sizeText=0;
    protected Bitmap btDraw=null;
    protected Bitmap btDrawClick =null;

    static int TYPE_VIEW_CIRCLE=1;
    static int TYPE_VIEW_RECT=0;
    static int TYPE_VIEW_RECTCIRCLE=2;

    Handler myHandler= new Handler(){
        public void handleMessage(Message m) {
            if (modeTouch == CLICK) {
                Log.i("touch", "click press");
                modeTouch=NONE;
            }
        }
    };


    public MyButton(Context context) {
        super(context);
    }

    @Override
    public void createView() {

    }

    @Override
    public boolean checkTouch(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        //nút hình tròn
        if(isCircle) {
            x=x+width/2;y=y+height/2;
            if(((x*x)/(width*width)+(y*y)/(height*height)<=4))return true;
        }
        // nút hình vuông
        else{
            if(!(x<0||y<0||x>width||y>height))return true;
        }
        return false;
    }

    public void setupView(){
        //setup Text size
        if(btDraw==null){
            Paint paint = new Paint();
            paint.setTextSize(height*0.65f);
            Rect rt= new Rect();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable icon=icon1;Log.i("draw","canvas");
        boolean isIcon= !ColorDrawable.class.isInstance(icon);
        int alpha=alpha1;
        int px = context.getResources().getDisplayMetrics().densityDpi / 80;
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        //this.setLayerType(LAYER_TYPE_SOFTWARE,null);
        int psize=2*px;
        int rf=psize+psize;
        RectF rectF= new RectF(psize,psize,width-psize,height-psize);
        canvas.saveLayerAlpha(0,0,canvas.getWidth(),canvas.getHeight(),alpha,Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        //draw circle bitmap
        if(isIcon) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xffffffff);
            drawShape(canvas,rectF,paint,rf);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }

        //draw icon
        if(isIcon){
            Bitmap bit=((BitmapDrawable)icon).getBitmap();
            canvas.drawBitmap(bit,new Rect(0,0,bit.getWidth(),bit.getHeight()),rectF,paint);
        }
        //draw name
        else {
            if(name=="")name="BUTTON";
            paint.setColor(((ColorDrawable)icon).getColor());
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(height*0.6f);
            paint.setShadowLayer(px*2,0,0, 0x55000000);
            Rect rT=new Rect();
            paint.getTextBounds(name,0,name.length(),rT);
            if(rT.width()>(width*0.6f))paint.setTextSize(paint.getTextSize()*(width*0.6f)/rT.width());
            paint.getTextBounds(name,0,name.length(),rT);
            canvas.drawText(name,width/2,height/2+rT.height()/2,paint);
        }
        //draw bound
        if(isViewBound&&isIcon) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(px);
            paint.setXfermode(null);
            paint.setColor(colorBound1);
            rectF.set(psize,psize,width-psize,height-psize);
            drawShape(canvas,rectF,paint,rf);
            //shadow
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            paint.setStrokeWidth(1);
            alpha=px*3/2+1;float pf=32/alpha;psize=0;
            for(int i=0;i<=alpha;i++){
                paint.setColor(psize<<24);
                rectF.set(i,i,width-i,height-i);
                drawShape(canvas,rectF,paint,rf);
                i=rf-i;
                rectF.set(i,i,width-i,height-i);
                drawShape(canvas,rectF,paint,rf);
                psize+=pf;i=rf-i;
            }
        }

        super.onDraw(canvas);
    }
    void drawShape(Canvas canvas,RectF rectF,Paint paint,int rf){
        if(typeViewControl==0) canvas.drawRoundRect(rectF,rf,rf,paint);
        else if(typeViewControl==1)canvas.drawOval(rectF, paint);
        else {
            if(rectF.width()>rectF.height())rf=(int)rectF.height()/2;
            else rf=(int)rectF.width()/2;
            canvas.drawRoundRect(rectF,rf,rf,paint);
        }
    }

    @Override
    public void setTypeViewControl(int add) {
        typeViewControl+=add;
        if(typeViewControl>2)typeViewControl=0;
        else if(typeViewControl<0)typeViewControl=2;
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return touchListen(event);
    }
}
