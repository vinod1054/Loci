package loci.vk.com.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import loci.vk.com.myapplication.R;

/**
 * Created by vinod on 25/2/16.
 */
public class CircleView extends View {

    int paddingLeft,paddingRight,paddingTop,paddingBottom;
    int width,height;
    int percentage=100;
    int percentageAngle=360;
    int startingAngle=270;
    float angle=0;
    int animationDelay=20;
    int primaryDark,primaryLight,accent;

    public int getAnimationDelay() {
        return animationDelay;
    }

    public void setAnimationDelay(int animationDelay) {
        this.animationDelay = animationDelay;
    }

    Paint paint=new Paint();
    RectF oval=new RectF();

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
        percentageAngle=(int)Math.ceil((double)percentage*360/100);
//        Log.i("vinod ","setPercentage "+percentageAngle);
        angle=0;
        setAnimationDelay(20);
        post(animate);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
        drawCircle(canvas);
    }

    public void drawCircle(Canvas canvas){
        paint.setColor(primaryLight);
        canvas.drawArc(oval,startingAngle,360,false,paint);
        paint.setColor(accent);
        canvas.drawArc(oval, startingAngle, angle, false, paint);
    }

    public void drawText(Canvas canvas){
        Paint mTextPaint=new Paint();
//        Log.i("vinod ","angle "+((int)(angle)*100/360+"%"));
        String text=(int)(angle)*100/360+"%";
        Rect result = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), result);
        mTextPaint.setTextSize(width/4);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(accent);
        canvas.drawText(text, (width)/2, (height+mTextPaint.getTextSize())/2-(width/14), mTextPaint);
    }

    Runnable animate=new Runnable() {
        @Override
        public void run() {
            boolean doIneedFrame=false;
            angle+=1;
//            Log.i("vinod ","angle in runnable "+angle+" "+percentageAngle);
            if(angle<=percentageAngle){
                postDelayed(animate,animationDelay);
                animationDelay/=2;
                doIneedFrame=true;
            }
            if(doIneedFrame)
                invalidate();
        }
    };

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        width = xNew;
        height = yNew;
        initPadding();
    }

    public void init(){
        primaryDark=getResources().getColor(R.color.colorPrimaryDark);
        primaryLight=getResources().getColor(R.color.colorPrimary);
        accent=getResources().getColor(R.color.colorAccent);
        paint.setColor(primaryLight);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(width/12);
        paint.setStyle(Paint.Style.STROKE);
        oval.set(width/12, width/12,width-(width/12) ,height-(width/12));
    }
    public void initPadding(){
        paddingLeft=getPaddingLeft();
        paddingRight=getPaddingRight();
        paddingTop=getPaddingTop();
        paddingBottom=getPaddingBottom();
        width=getWidth();
        height=getHeight();
//        Log.i("vinod ",paddingLeft+" "+paddingRight+" "+paddingTop+" "+paddingBottom);
//        Log.i("vinod ",width+" "+height);
        if(width<height)
            height=width;
        else
            width=height;
        init();
//        Log.i("vinod onSizeChanged",width+" "+height);
    }

}

