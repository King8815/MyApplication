package view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.slide_table.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2017/4/20.
 * 实现思路（X轴方向重复，Y轴方向延伸）整个View，然后水平移动图片，就可以得到波浪效果了
 * 绘制一个波形图，填充到View里，移动波形图。
 */

public class WaveView extends View {
    //屏幕宽度和高度
    private int mScreenWidth;
    private int mScreenHeight;

    //水位高度线
    private int mLevelHeight;

    //波浪起伏的振幅Amplitude
    private int mWaveHeight;

    //波浪的波长
    private int mWaveWidth;

    //被隐藏的最左边的波形
    private int mLeftWaveMoveLength;//波平移的距离，用来控制波的起点位置
    private int mLeftHideHeight;

    //波形水平移动的速度
    private static final float WAVE_SPEED = 2;

    private int mProgress;

    private Paint mWavePaint;//波浪画笔
    private Paint mCirclePaint;//圆环画笔
    private Paint mTextPaint;//文字画笔
    private Rect rect;//圆环范围属性

    private Path mPath;

    /**
     * 采用两个波，由9个点来确定图形
     */
    private List<MyPoint> mPoints;//点的集合

    private boolean isMeasured = false;//是否已经测量

    private String view_title = getResources().getString(R.string.wave_title);//测试

    private MyTimerTask mTask;
    private Timer timer;
    private WaveThread waveThread;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /**
         * 画笔初始化
         */
        //波浪轨迹画笔初始化
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setColor(Color.DKGRAY);
        mWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //圆环画笔初始化
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setAntiAlias(true);//抗锯齿
        mCirclePaint.setStyle(Paint.Style.STROKE);//设置属性为只描边
        mCirclePaint.setStrokeWidth(10);//设置描边宽度

        //文字画笔初始化
        mTextPaint = new Paint();
        mTextPaint.setTextSize(12);
        mCirclePaint.setStyle(Paint.Style.STROKE);//设置属性为只描边
        mCirclePaint.setStrokeWidth(15);//设置描边宽度
        mTextPaint.setColor(Color.RED);

        //新建画线变量
        mPath = new Path();

        /**
         * 波浪中需要的点的初始化
         */
        mPoints = new ArrayList<MyPoint>();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("test","isMeasured = " + isMeasured);
        Log.e("test","高度 = " + getMeasuredHeight());
        Log.e("test","宽度 = " + getMeasuredWidth());

        if (!isMeasured) {
            mScreenWidth = getMeasuredWidth();
            mScreenHeight = getMeasuredHeight();
            // 水位线从最底下开始上升
            mLevelHeight = mScreenHeight;//初始化波形的基准线

            {
                mLevelHeight = mScreenHeight * (100 - mProgress) / 100;
                if (mLevelHeight < 0) {
                    mLevelHeight = 0;
                }
            }
            //计算波峰值
            //波长等于四倍View宽度也就是View中只能看到四分之一个波形，这样可以使起伏更明显
            mWaveWidth = getMeasuredWidth();
            // 根据View宽度计算波形峰值
            mWaveHeight = mScreenHeight / 2;

            //计算所有的点 这里取宽度为整个波长  往左再延伸一个波长 两个波长则需要9个点,然后将这些点放到list中
            for (int i = 0; i < 9; i++) {
                int x = i*mWaveWidth/4 - mWaveWidth;
                int y = 0;
                switch (i % 4) {
                    //零点位于水平线上
                    case 0:
                        y = mScreenHeight;
                        break;
                    //往下的控制点
                    case 1:
                        y = mScreenHeight + mWaveHeight;
                        break;
                    //零点位于水平线上
                    case 2:
                        y = mScreenHeight;
                        break;
                    //往上的控制点
                    case 3:
                        y = mScreenHeight - mWaveHeight;
                        break;
                }

//                MyPoint point = new MyPoint(-mWaveWidth + i * mWaveWidth / 4, y);
                mPoints.add(new MyPoint(x, y));
                Log.e("test","P"+ i +": (" + x + ", " + y + ")");
            }
            isMeasured = true;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制线条
        mPath.reset();
        int i = 0;
        Log.e("test","list大小 = " + mPoints.size());
        Log.e("test","坐标 X：" + mPoints.get(0).getX());
        Log.e("test","坐标 Y：" + mPoints.get(0).getY());
        mPath.moveTo(mPoints.get(0).getX() + mLeftWaveMoveLength, mPoints.get(0).getY() - mScreenHeight * mProgress / 100);
        //绘制贝塞尔二阶线条
        for (; i < mPoints.size()-2; i += 2) {
            mPath.quadTo(mPoints.get(i + 1).getX() + mLeftWaveMoveLength, mPoints.get(i + 1).getY() - mScreenHeight * mProgress / 100,
                    mPoints.get(i + 2).getX() + mLeftWaveMoveLength, mPoints.get(i + 2).getY() - mScreenHeight * mProgress / 100);
        }
        mPath.lineTo(mPoints.get(i).getX()+mLeftWaveMoveLength, mScreenHeight);
        mPath.lineTo(mPoints.get(0).getX()+mLeftWaveMoveLength, mScreenHeight);
        mPath.close();
        //绘制轨迹
        canvas.drawPath(mPath,mWavePaint);

        canvas.drawCircle(mScreenWidth/4, mScreenHeight/2,mScreenHeight/4,mCirclePaint);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

    }

    /**
     * 波浪 波动的线程
     */
    private final class WaveThread extends Thread{
        @Override
        public void run() {
            super.run();
            handler.sendEmptyMessage(20);

        }
    }
    /**
     * 消息处理
     * 利用handler实现波动的效果
     */

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initWaveMove();
        }
    };

    /**
     * 初始化波形的移动
     */
    private void initWaveMove() {
        mLeftWaveMoveLength += WAVE_SPEED;//波浪向右移动的距离
        if (mLeftWaveMoveLength >= mWaveWidth) {
            mLeftWaveMoveLength = 0;
        }
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        mLevelHeight = (100 - mProgress) * mScreenHeight / 100;
    }
    class MyTimerTask extends TimerTask
    {
        Handler handler;

        public MyTimerTask(Handler handler)
        {
            this.handler = handler;
        }

        @Override
        public void run()
        {
            handler.sendMessage(handler.obtainMessage());
        }

    }

}
