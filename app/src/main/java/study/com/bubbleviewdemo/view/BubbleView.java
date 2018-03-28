package study.com.bubbleviewdemo.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Administrator on 2018/3/23.
 */

public class BubbleView extends View {
    //画笔--画圆
    private Paint paint;
    //画笔--字
    private Paint wordPaint;
    //固定圆的半径
    private float fixationRadius = 30f;
    //固定圆的初始的半径
    private float originFixationRadius = fixationRadius;
    //动圆的半径
    private float mobilizeRadius = 45f;
    //固定圆的外切圆
    private RectF fixationRectF;
    //动圆的外切圆
    private RectF mobilizeRectF;
    //路径
    private Path path;
    //上下文环境
    private Context context;
    //状态栏高度
    private int statusBarHeight;
    //固定圆的最小半径
    private float minFixationRadius = 10f;
    //两个圆的初始距离
    private float originDistanceValue = 0;
    //时间距离和初始距离的百分比
    private float percent = 0;
    //当前事件是否是抬起事件
    private boolean isActionUp = false;

    private PointF fixationPointF = new PointF(300, 300); //固定圆圆心
    private PointF mobilizePointF = new PointF(600, 600); //拖动圆圆心
    private PointF controlPointF; //控制点

    //事件的横坐标
    private float actionRawX = 0;
    //事件的纵坐标
    private float actionRawY = 0;
    //事件发生的点
    private PointF eventPointF = new PointF();
    //事件发生的位置和定圆位置之间的距离
    private float distanceValue = 0;


    public BubbleView(Context context) {
        this(context, null);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        fixationRectF = new RectF();
        mobilizeRectF = new RectF();

        wordPaint = new Paint();
        wordPaint.setColor(Color.WHITE);
        wordPaint.setAntiAlias(true);
        wordPaint.setTextSize(36);
        wordPaint.setTextAlign(Paint.Align.CENTER);

        path = new Path();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        statusBarHeight = getStatusBarHeight(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, -getStatusBarHeight(this));

        //当初始距离为0时，对初始化距离的值赋值
        if (originDistanceValue == 0) {
            originDistanceValue = GeometryUtil.getDistanceBetween2Points(fixationPointF, mobilizePointF);
        }

        //初始距离和事件距离之间的百分比
        percent = originDistanceValue / distanceValue;

        if (percent <= 1) { //当事件距离大于初始距离时
            fixationRadius = percent * originFixationRadius;
        } else {
            fixationRadius = originFixationRadius;
        }

        //不是抬起事件
        if (!isActionUp) {
            if (distanceValue <= 1.5 * originDistanceValue) {
                canvas.drawCircle(fixationPointF.x, fixationPointF.y, fixationRadius, paint);
                //获取控制点坐标
                controlPointF = GeometryUtil.getMiddlePoint(fixationPointF, mobilizePointF);
                //获取附着点
                double lineK = (mobilizePointF.y - fixationPointF.y) / (mobilizePointF.x - fixationPointF.x);
                PointF[] fixationP = GeometryUtil.getIntersectionPoints(fixationPointF, fixationRadius, lineK);
                PointF[] mobilizeP = GeometryUtil.getIntersectionPoints(mobilizePointF, mobilizeRadius, lineK);

                path.moveTo(fixationP[0].x, fixationP[0].y);
                path.quadTo(controlPointF.x, controlPointF.y, mobilizeP[0].x, mobilizeP[0].y);
                path.lineTo(mobilizeP[1].x, mobilizeP[1].y);
                path.quadTo(controlPointF.x, controlPointF.y, fixationP[1].x, fixationP[1].y);
                path.lineTo(fixationP[0].x, fixationP[0].y);
                canvas.drawPath(path, paint);
            }
            canvas.drawCircle(mobilizePointF.x, mobilizePointF.y, mobilizeRadius, paint);
            if (!TextUtils.isEmpty(text)) {
                canvas.drawText(text, mobilizePointF.x, mobilizePointF.y + textHeight * 1.0f / 2, wordPaint);
            }
        } else {

        }
        path.reset();
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isActionUp = false;
                actionRawX = event.getRawX();
                actionRawY = event.getRawY();
                eventPointF.set(actionRawX, actionRawY);
                distanceValue = GeometryUtil.getDistanceBetween2Points(eventPointF, fixationPointF);
                mobilizePointF.set(actionRawX, actionRawY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                actionRawX = event.getRawX();
                actionRawY = event.getRawY();
                eventPointF.set(actionRawX, actionRawY);
                distanceValue = GeometryUtil.getDistanceBetween2Points(eventPointF, fixationPointF);
                mobilizePointF.set(actionRawX, actionRawY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                actionRawX = event.getRawX();
                actionRawY = event.getRawY();
                eventPointF.set(actionRawX, actionRawY);
                distanceValue = GeometryUtil.getDistanceBetween2Points(eventPointF, fixationPointF);
                //当抬起事件发生时，两圆距离小于初始距离的1.5倍时isActionUp为false
                isActionUp = distanceValue <= 1.5 * originDistanceValue ? false : true;
                if (!isActionUp) {

                    ValueAnimator va = ValueAnimator.ofFloat(distanceValue, 0);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            //获取当前动画更新的值
                            float fraction = animation.getAnimatedFraction();
                            float currentValue = (float) animation.getAnimatedValue();
                            Log.e("point::", "两圆之间的比例：" + fraction + "=当前值：" + currentValue + "=总值：" + distanceValue);
                            PointF percentPointF = GeometryUtil.getPointByPercent(eventPointF, fixationPointF, fraction);
                            Log.e("point::", "动圆：" + percentPointF.x + "===" + percentPointF.y);
                            mobilizePointF.set(percentPointF.x, percentPointF.y);
                            invalidate();
                        }
                    });
                    va.setInterpolator(new OvershootInterpolator(5));
                    va.setDuration(2000);
                    va.start();
                }
                invalidate();
                break;
        }
        return true;
    }

    //测量文字的矩形框
    private Rect bounds = new Rect();
    //文本
    private String text;
    //文本的高度
    private int textHeight;

    /**
     * 设置文本
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        wordPaint.getTextBounds(text, 0, text.length(), bounds);
        textHeight = bounds.height();
    }


    /**
     * 设置气泡位置
     *
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        fixationPointF.set(x, y);
        mobilizePointF.set(x, y);
        invalidate();
    }

    /**
     * 获取状态栏高度
     *
     * @param view
     * @return
     */
    public int getStatusBarHeight(View view) {
        Rect outRectF = new Rect();
        view.getWindowVisibleDisplayFrame(outRectF);
        return outRectF.top;
    }

}
