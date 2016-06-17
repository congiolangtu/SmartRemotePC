package myItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.windy.smartremotepc.R;

import Mode.ModeButton;

/**
 * Created by Windy on 08/06/2015.
 */
public class MyButton extends ModeButton implements View.OnTouchListener{
    protected boolean isSticky=false;
    protected boolean sticky=false;
    protected boolean isCircle=true;
    protected boolean isTouch=false;
    protected boolean isViewBound=true;
    float sizeText=0;
    protected Bitmap btDraw=null;
    protected Bitmap btDrawClick =null;
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
        if(btDraw!=null) {
            if (isCircle) {
                btDraw = BitmapFactory.decodeResource(context.getResources(), R.drawable.bt_circle);
                btDrawClick = BitmapFactory.decodeResource(context.getResources(), R.drawable.bt_circle_fill);
            } else {
                btDraw = BitmapFactory.decodeResource(context.getResources(), R.drawable.bt_rec);
                btDrawClick = BitmapFactory.decodeResource(context.getResources(), R.drawable.bt_rec_fill);
            }
        }
        setBackgroundColor(Color.BLUE);
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
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        RectF rectF= new RectF(2,2,width-2,height-2);
        //draw icon
        if(btDraw!=null){
            if(width>height)rectF.set(2,2,width-2,height-2);
            else rectF.set(0,0,width,width);
            canvas.drawBitmap(btDraw,null,rectF,null);
        }
        //draw name
        else {
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(height*0.6f);
            paint.setShadowLayer(2,0,0,Color.BLACK);
            Rect rT=new Rect();
            paint.getTextBounds(name,0,name.length(),rT);
            if(rT.width()>(width*0.6f))paint.setTextSize(paint.getTextSize()*(width*0.6f)/rT.width());
            paint.getTextBounds(name,0,name.length(),rT);
            canvas.drawText(name,width/2,height/2+rT.height()/2,paint);
        }
        //draw Background
        //if(!isTouch) canvas.drawBitmap(btDraw,null,rectF,null);
        //else canvas.drawBitmap(btDrawClick,null,rectF,null);Log.i("MyButton","3");
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        canvas.drawOval(rectF,paint);
        Matrix matrix=new Matrix();matrix.setTranslate(0,0);
        resizeButton.setBounds(new Rect(0,0,(int)width,(int)height));
        resizeButton.draw(canvas);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return touchListen(event);
    }
}
