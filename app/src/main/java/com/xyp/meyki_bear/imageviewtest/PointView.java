package com.xyp.meyki_bear.imageviewtest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * 项目名称：ImageViewTest
 * 类描述：
 * 创建人：meyki-bear
 * 创建时间：2017/2/4 11:24
 * 修改人：meyki-bear
 * 修改时间：2017/2/4 11:24
 * 修改备注：
 */

public class PointView extends View {
    private int maxPointRadius;
    private int pointRadius;
    private Paint mPaint;
    private String text;
    private int pointColor=Color.BLACK;

    /**
     * 代码添加的控件，无法进行测量，
     * 当控件在父布局中部分可见时拿不到测量尺寸，这会导致绘制原点时圆心坐标出错，
     * 所以增加一个set方法，把父布局里设置的半径尺寸直接用代码设置进来，不再调用getMeasuredWidth/Height进行获取
     */
    private int height;
    private int width;
    private ValueAnimator animator;

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setMaxPointRadius(int maxPointRadius) {
        this.maxPointRadius = maxPointRadius;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public PointView(Context context) {
        super(context);
        init();
    }

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true); //抗锯齿


    }

    public PointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }



    public void startAnim() {
        animator = ValueAnimator.ofFloat(0, maxPointRadius);
        animator.setDuration(600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                pointRadius = value.intValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startChooseAnim(){
        animator = ValueAnimator.ofArgb(Color.BLACK, Color.WHITE);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                pointColor = value.intValue();
                postInvalidate();
            }
        });
        animator.start();
    }
    public void stopChooseAnim(){
        animator.cancel();
    }
    public void drawText(){
        isDrawText=true;
        invalidate();
    }
    public void notDrawText(){
        isDrawText=false;
        invalidate();
    }

    private boolean isDrawText;
    @Override
    protected void onDraw(Canvas canvas) {
        if(isDrawText){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(3);
            paint.setTextSize(30);
            paint.setColor(Color.WHITE);
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text,width/2,height/2-maxPointRadius-5,paint);
        }
        mPaint.setColor(pointColor);
        canvas.drawCircle(width / 2, height / 2, pointRadius, mPaint);
    }


}
