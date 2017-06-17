package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2017/4/19.
 */

public class BezierView extends View {
    //设置起始点(x,y)
    private float startPoint_X;
    private float startPoint_Y;
    //设置控制点(x,y)
    private float controlPoint_X;
    private float controlPoint_Y;
    //设置结束点(x,y)
    private float endPoint_X;
    private float endPoint_Y;

    //设置画笔和路径
    private Paint mPaint;
    private Path mPath;

    //设置贝塞尔曲线和贝塞尔的点和线
    private Paint mLinePaint;
    private Path mLinePath;

    public BezierView(Context context) {
        super(context);
        initPaint();
        initLinePaint();
    }

    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initLinePaint();
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initLinePaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        mPath = new Path();
    }

    private void initLinePaint(){
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setTextSize(30);

        mLinePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startPoint_X = w/4;
        startPoint_Y = h/2;

        endPoint_X = w*3/4;
        endPoint_Y = h/2;

        controlPoint_X = w/2;
        controlPoint_Y = h/2-300;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPath != null){
            mPath.reset();
        }
        if (mLinePath != null){
            mLinePath.reset();
        }

        //贝塞尔曲线
        mPath.moveTo(startPoint_X,startPoint_X);
        mPath.quadTo(controlPoint_X,controlPoint_Y,endPoint_X,endPoint_X);
        canvas.drawPath(mPath,mPaint);
//        mPath.cubicTo(startPoint_X,startPoint_X,controlPoint_X,controlPoint_Y,endPoint_X,endPoint_X);
//        mLinePath.lineTo(controlPoint_X,controlPoint_Y);
//        mLinePath.lineTo(endPoint_X,endPoint_Y);
        //辅助线
//        mLinePath.lineTo(controlPoint_X,controlPoint_Y);
//        mLinePath.lineTo(endPoint_X,endPoint_Y);
        canvas.drawLine(startPoint_X,startPoint_X,controlPoint_X,controlPoint_Y,mLinePaint);
        canvas.drawLine(controlPoint_X,controlPoint_Y,endPoint_X,endPoint_X,mLinePaint);
        //辅助点
        canvas.drawPoint(startPoint_X,startPoint_Y,mLinePaint);
        canvas.drawText("P0",startPoint_X-20,startPoint_Y,mLinePaint);
        canvas.drawPoint(controlPoint_X,controlPoint_Y,mLinePaint);
        canvas.drawText("P1",controlPoint_X,controlPoint_Y-10,mLinePaint);
        canvas.drawPoint(endPoint_X,endPoint_Y,mLinePaint);
        canvas.drawText("P2",endPoint_X+20,endPoint_Y,mLinePaint);
        canvas.drawPath(mLinePath,mLinePaint);
    }
}
