package com.example.carl.customqqstepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class QQStepView extends View {

    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;
    private int mRoundWidth;
    private int mRoundColor;
    private int mProgressColor;
    private int mStartAngle;
    private int mSweepAngle;
    private int mMaxStep;
    private int mProgressStep;
    private float mPrecent;
    private int mCenterX;
    private int mCenterY;
    private int mTextSize;
    private int mTextColor;
    private RectF oval;

    private String TAG = "chen";
    public QQStepView(Context context) {
        //super(context);
        this(context,null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        //super(context, attrs);
        this(context,attrs,0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.QQStepView);
        mRoundWidth = array.getInteger(R.styleable.QQStepView_RoundWidth,20);

        mStartAngle = array.getInteger(R.styleable.QQStepView_StartAngle,20);
        mSweepAngle =  array.getInteger(R.styleable.QQStepView_SweepAngle,20);
        mRoundColor = array.getColor(R.styleable.QQStepView_RoundColor, 0x00ff00);
        mProgressColor = array.getColor(R.styleable.QQStepView_ProgressColor, 0x00ff00);
        mProgressStep = array.getInteger(R.styleable.QQStepView_ProgressStep,50);
        mMaxStep =  array.getInteger(R.styleable.QQStepView_MaxStep,100);
        mTextColor = array.getColor(R.styleable.QQStepView_TextColor, 0x00ff00);
        mTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_TextSize,20);
        mPaint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        countMeasure();
        drawMaxArc(canvas);
        drawProgressAcr(canvas);
        drawText(canvas);
    }

    private void countMeasure(){
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        mCenterX = mViewWidth/ 2;
        mCenterY = mViewHeight / 2;
        // 半径
        int radius = (int) (mCenterX - mRoundWidth);
        // 计算当前百分比
        mPrecent = (float) mProgressStep/mMaxStep;
        Log.i(TAG,"radius:"+radius);
        oval = new RectF(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
    }
    private void drawMaxArc(Canvas canvas){
        // 2.画背景大圆弧
        // 设置圆弧画笔的宽度
        mPaint.setStrokeWidth(mRoundWidth);
        // 设置为 ROUND
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置线帽样式为圆头
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置两线之间的连接方式为圆头连接
        // 设置画笔颜色
        mPaint.setColor(mRoundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        // 画背景圆弧
        canvas.drawArc(oval, mStartAngle, mSweepAngle, false, mPaint);//
    }

    private void drawProgressAcr(Canvas canvas){
        // 画进度圆弧
        mPaint.setColor(mProgressColor);

        // 根据当前百分比计算圆弧扫描的角度
        canvas.drawArc(oval, mStartAngle, mPrecent*mSweepAngle, false, mPaint);
    }

    private void drawText(Canvas canvas){
        // 重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        String mStep = ((int)(mPrecent*mMaxStep)) + "";
        // 测量文字的宽高
        Rect textBounds = new Rect();
        mPaint.getTextBounds(mStep, 0, mStep.length(), textBounds);
        int dx = (getWidth() - textBounds.width()) / 2;
        // 获取画笔的FontMetrics
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        // 计算文字的基线
        int baseLine = (int) (getHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        // 绘制步数文字
        canvas.drawText(mStep, dx, baseLine, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // 设置当前最大步数
    public synchronized void setMaxStep(int maxStep) {
        if (maxStep < 0) {
            throw new IllegalArgumentException("max 不能小于0!");
        }
        this.mMaxStep = maxStep;
    }

    public synchronized int getMaxStep() {
        return mMaxStep;
    }
    // 设置进度
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress 不能小于0!");
        }
        this.mProgressStep = progress;
        // 重新刷新绘制 -> onDraw()
        invalidate();
    }

    public synchronized int getProgress() {
        return this.mProgressStep;
    }


}
