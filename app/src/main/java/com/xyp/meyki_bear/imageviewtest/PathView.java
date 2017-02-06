package com.xyp.meyki_bear.imageviewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * 项目名称：ImageViewTest
 * 类描述：Path的实验类
 * 创建人：meyki-bear
 * 创建时间：2017/2/3 16:17
 * 修改人：meyki-bear
 * 修改时间：2017/2/3 16:17
 * 修改备注：
 */

public class PathView extends View {
    private Paint mDefaultPaint; //画笔工具
    private float currentValue; //用于记录当前位置，取值范围[0,1]映射Path的整个长度
    private float[] pos; //当前点的实际位置
    private float[] tan; //当前点的tangent值，用于计算图片所需旋转的角度。
    private Bitmap mBitmap; //箭头图片
    private Matrix mMatrix; //矩阵，用于对图片进行一系列变换操作

    public PathView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        pos=new float[2];
        tan=new float[2];
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=8; //缩放图片
        mBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.one,options);
        mMatrix=new Matrix();

        mDefaultPaint=new Paint();
        mDefaultPaint.setColor(Color.BLACK);
        mDefaultPaint.setStrokeWidth(5);
        mDefaultPaint.setStyle(Paint.Style.STROKE);
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth()/2,getHeight()/2);
        Path path=new Path();
        path.addCircle(0,0,200, Path.Direction.CW);
        PathMeasure pathMeasure=new PathMeasure(path,false);
        currentValue+=0.005;//计算当前的位置在总长度上的比例
        if(currentValue>=1){
            currentValue=0;
        }
        mMatrix.reset(); //重置Matrix
        pathMeasure.getMatrix(currentValue*pathMeasure.getLength(),mMatrix,PathMeasure.POSITION_MATRIX_FLAG|PathMeasure.TANGENT_MATRIX_FLAG);
        mMatrix.preTranslate(pos[0]-mBitmap.getWidth()/2,pos[1]-mBitmap.getHeight()/2);//将图标的绘制中心与当前点进行重合
        canvas.drawPath(path,mDefaultPaint);
        canvas.drawBitmap(mBitmap,mMatrix,mDefaultPaint);
        invalidate(); //递归调用本身来实现循环动画
    }
}
