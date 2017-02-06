package com.xyp.meyki_bear.imageviewtest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * 项目名称：HealthRecord
 * 类描述：折线图控件
 * 创建人：meyki-bear
 * 创建时间：2017/2/3 11:10
 * 修改人：meyki-bear
 * 修改时间：2017/2/3 11:10
 * 修改备注：
 */

public class LineChartView extends View {
    private List<Integer> list;
    private Path mPath;
    private int lineWidth;// 列间距，每一列的间距
    private int pointRadius = 10; //点的大小
    private float lineLength;//折线的总体长度
    private Paint mPaint; //画线的画笔
    private Paint mPointPaint; //画点的画笔
    private PathMeasure pathMeasure;
    private Path dst;//要绘制的路径
    private float[] pos;
    private float[] tan;
    public void setPhase(float phase) {
        dst.reset();
        pathMeasure.getSegment(0,phase*pathMeasure.getLength(),dst,false);
        pathMeasure.getPosTan(phase*pathMeasure.getLength(),pos,tan);
        invalidate();//will calll onDraw
    }


    public void setList(final List<Integer> list) {
        this.list = list;
        if (mPath == null) {
            mPath = new Path();
        } else {
            mPath.reset();
        }
        post(new Runnable() {
            @Override
            public void run() {
                mPath.moveTo(0, 0);
                lineWidth=getMeasuredWidth()/8;
                for (int i = 0; i < list.size(); i++) {
                    mPath.lineTo((i + 1) * lineWidth, list.get(i));
                }
                pathMeasure = new PathMeasure(mPath, false);
                lineLength = pathMeasure.getLength();
                ObjectAnimator animator = ObjectAnimator.ofFloat(LineChartView.this, "phase", 0.0f, 1.0f);
                animator.setDuration(3000);
                animator.start();
            }
        });

    }

    public LineChartView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);//抗锯齿

        mPointPaint = new Paint();
        mPointPaint.setColor(Color.BLACK);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setStrokeWidth(5);
        mPointPaint.setAntiAlias(true);//抗锯齿

        ValueAnimator valueAnimator=new ObjectAnimator();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });
        dst=new Path();
        pos=new float[2];
        tan=new float[2];
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dst.isEmpty()) {
            return;
        }
        canvas.save();
        canvas.scale(1, -1, getWidth() / 2, getHeight() / 2);
        canvas.drawPath(dst, mPaint);
        for (int i=0;i<list.size();i++){
           if(pos[0]>(i + 1) * lineWidth){
                canvas.drawCircle((i + 1) * lineWidth,list.get(i),pointRadius,mPointPaint);
            }
        }
        canvas.restore();
    }
}
