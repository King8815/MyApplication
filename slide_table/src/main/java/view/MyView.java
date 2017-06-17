package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by user on 2017/4/18.
 * 以一个圆为例
 */

public class MyView extends View {
    //设置View的宽度和高度
    private int width;
    private int height;

    //圆的半径
    private int radius;
    //设置画笔变量
    Paint mPaint;

    /**
     * 自定义View的时候有4个构造函数
     * 若自定义的View是通过java代码new出来的，则调用第一个MyView(Context context)
     * 若自定义的View是通过.xml中声明的，但是没有指定style,则调用第二个MyView(Context context, AttributeSet attrs)
     * 若自定义的View是在xml创建但是有指定style的时候被调用MyView(Context context, AttributeSet attrs, int defStyleAttr)
     * @param context
     */
    public MyView(Context context) {
        super(context);
        // 初始化画笔
        initPaint();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 设置画笔的变量，如颜色，宽度，透明度等
     */
    private void initPaint() {
        //创建画笔
        mPaint = new Paint();
        //创建画笔颜色
        mPaint.setColor(Color.BLUE);
        //创建透明度
        mPaint.setAlpha(12);
        //设置画笔宽度
        mPaint.setStrokeWidth(5f);
        //设置画笔模式style
        //类型1：Paint.Style.FILLANDSTROKE（描边+填充）
        //类型2：Paint.Style.FILL（只填充不描边）
        //类型3：Paint.Style.STROKE（只描边不填充）
        mPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        //半径 = 宽,高最小值的2分之1
        radius = Math.min(width,height)/4;

        //开始绘制
        canvas.drawCircle(width/2, height/2, radius, mPaint);
    }
}
