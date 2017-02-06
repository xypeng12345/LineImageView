package com.xyp.meyki_bear.imageviewtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * 项目名称：ImageViewTest
 * 类描述：
 * 创建人：meyki-bear
 * 创建时间：2017/2/4 11:05
 * 修改人：meyki-bear
 * 修改时间：2017/2/4 11:05
 * 修改备注：
 */

public class LineChartViewGroup extends RelativeLayout implements View.OnClickListener {
    private List<Integer> list;
    private Path mPath;
    private int lineWidth;// 列间距，每一列的间距
    private int pointRadius = 20; //点的大小
    private float lineLength;//折线的总体长度
    private Paint mPaint; //画线的画笔
    private Paint mPointPaint; //画点的画笔
    private PathMeasure pathMeasure;
    private Path dst;//要绘制的路径
    private float[] pos;
    private float[] tan;
    private PointView pv;
    private PointView[] pvs;

    public LineChartViewGroup(Context context) {
        super(context);
        initView();
    }

    public LineChartViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LineChartViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private int pointIndex = 1;

    public void setPhase(float phase) {
        dst.reset();
        pathMeasure.getSegment(0, phase * pathMeasure.getLength(), dst, false);
        pathMeasure.getPosTan(phase * pathMeasure.getLength(), pos, tan);
        float v = pos[0] / lineWidth;

        if (v > pointIndex) {
            if (pvs[pointIndex] != null) {
                return;
            }
            pv = new PointView(getContext()); //创建一个标点控件，通过代码来设置控件的位置与尺寸
            pv.setMaxPointRadius(pointRadius);
            pv.setWidth(pointRadius * 5);
            pv.setHeight(pointRadius * 5);
            LayoutParams layoutParams = new LayoutParams(pointRadius * 5, pointRadius * 5);
            layoutParams.leftMargin = (int) (pointIndex * lineWidth - pointRadius * 2.5);
            layoutParams.topMargin = (int) (getHeight() - list.get(pointIndex - 1) - pointRadius * 2.5);
            addView(pv, layoutParams);
            pv.setText("" + list.get(pointIndex - 1));
            pvs[pointIndex] = pv;
            pointIndex++;
        }
        invalidate();
    }

    public void setList(final List<Integer> list) {

        this.list = list;
        pvs = new PointView[list.size()];
        if (mPath == null) {
            mPath = new Path();
        } else {
            mPath.reset();
        }
        post(new Runnable() {
            @Override
            public void run() {
                mPath.moveTo(0, 0);
                lineWidth = getMeasuredWidth() / 9;
                for (int i = 0; i < list.size(); i++) {
                    mPath.lineTo((i + 1) * lineWidth, list.get(i));
                }
                pathMeasure = new PathMeasure(mPath, false);
                lineLength = pathMeasure.getLength();
                ObjectAnimator animator = ObjectAnimator.ofFloat(LineChartViewGroup.this, "phase", 0.0f, 1.0f);
                animator.setDuration(3000);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        for (int i = 1; i < pvs.length; i++) {
                            pvs[i].setOnClickListener(LineChartViewGroup.this);
                        }
                    }
                });
                animator.start();
            }
        });
    }

    private void initView() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false); //viewGroup默认不执行onDraw方法，所以需要修改默认情况
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);//抗锯齿

        mPointPaint = new Paint();
        mPointPaint.setColor(Color.BLACK);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setStrokeWidth(5);
        mPointPaint.setAntiAlias(true);//抗锯齿

        dst = new Path();
        pos = new float[2];
        tan = new float[2];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.argb(0xff, 0xaa, 0x88, 00));
        if (dst.isEmpty()) {
            return;
        }//
        canvas.save();
        canvas.scale(1, -1, getWidth() / 2, getHeight() / 2);
        canvas.drawPath(dst, mPaint);
        canvas.restore();
    }

    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("yupeng", "触发了点击事件");
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("yupeng", "触发了移动事件");
                Log.d("yupeng", "event.getX()-downX" + (event.getX() - lastX));
                scrollBy((int) -(event.getX() - lastX), 0);
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected void dispatchVisibilityChanged(View changedView, int visibility) {
        super.dispatchVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public int getWindowSystemUiVisibility() {
        return super.getWindowSystemUiVisibility();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private int index;

    @Override
    public void onClick(View v) {
        final PointView pv = (PointView) v;
        for (int i = 1; i < pvs.length; i++) {
            pvs[i].stopChooseAnim();
            if (pvs[i] == pv) {
                index = i;
                pvs[i].drawText();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    pvs[i].startChooseAnim();
                } else {
                    pvs[i].setPointColor(Color.WHITE);
                }
            } else {
                pvs[i].setPointColor(Color.BLACK);
                pvs[i].notDrawText();
            }
        }
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(1);
        pv.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (pvs[index] == pv) {
                    pv.drawText();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
