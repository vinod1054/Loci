package loci.vk.com.myapplication.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by vinod on 22/2/16.
 */
public class AnimateCircleView extends View {


    public AnimateCircleView(Context context) {
        super(context);
    }

    public AnimateCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF33B5E5);
        paint.setStrokeWidth(4);
        getHeight();
        canvas.drawCircle(getX(),getY(),30,paint);
    }



}
