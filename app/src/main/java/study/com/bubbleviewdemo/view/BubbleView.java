package study.com.bubbleviewdemo.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/3/23.
 */

public class BubbleView extends View {
    //画笔
    private Paint paint;
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

    public BubbleView(Context context) {
        super(context, null);
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

        path = new Path();
    }

    private PointF pointF1 = new PointF(300, 270);
    private PointF pointF2 = new PointF(300, 255);
    private PointF pointF3 = new PointF(300, 345);
    private PointF pointF4 = new PointF(100, 130);
    private PointF fixationPointF = new PointF(300, 300); //固定圆圆心
    private PointF mobilizePointF = new PointF(600, 600); //拖动圆圆心
    private PointF controlPointF;

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


        percent = originDistanceValue / distanceValue;
        Log.e("point::", "初始化距离:" + originDistanceValue + "==事件距离：" + distanceValue + "==百分比：" + percent);

        if (percent <= 1) { //当事件距离大于初始距离时
            fixationRadius = percent * originFixationRadius;
        } else {
            fixationRadius = originFixationRadius;
        }

        Log.e("point::", "固定圆的半径：" + fixationRadius);

        if (distanceValue < 1.5 * originDistanceValue) {
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

        path.reset();
        canvas.restore();
    }

    private float actionRawX = 0;
    private float actionRawY = 0;
    private PointF eventPointF = new PointF();
    private float distanceValue = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
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
                break;
        }
        return true;
    }

    public int getStatusBarHeight(View view) {
        Rect outRectF = new Rect();
        view.getWindowVisibleDisplayFrame(outRectF);
        return outRectF.top;
    }
}
