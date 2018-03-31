package study.com.bubbleviewdemo.listener;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import study.com.bubbleviewdemo.bean.Msg;
import study.com.bubbleviewdemo.view.BubbleView;

/**
 * Created by Administrator on 2018/3/28.
 */

public class OnBubbleViewTouchListener implements TextView.OnTouchListener, BubbleView.OnBubbleViewListener {
    private final WindowManager.LayoutParams params;
    private final List<Msg> msgList;
    private Context context;
    private final WindowManager windowManager;
    private final BubbleView bubbleView;
    private TextView tv;

    public OnBubbleViewTouchListener(List<Msg> msgList, Context context) {
        this.context = context;
        this.msgList = msgList;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        bubbleView = new BubbleView(context);
        bubbleView.setOnBubbleViewListener(this);

        params = new WindowManager.LayoutParams();
        //宽度和高度是
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;//类型是透明
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //用于请求RecyclerView不拦截事件
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float actionRawX = event.getRawX();
                float actionRawY = event.getRawY();
                tv = (TextView) v;
                String text = tv.getText().toString().trim();
                //将当前的tv变为不可视
                tv.setVisibility(View.INVISIBLE);
                bubbleView.setText(text);
                bubbleView.setPosition(actionRawX, actionRawY);
                windowManager.addView(bubbleView, params);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        //将事件传递给BubbleView
        bubbleView.onTouchEvent(event);
        return true;
    }

    @Override
    public void clearView() {
        ViewParent parent = bubbleView.getParent();
        if (parent != null) {
            windowManager.removeView(bubbleView);
            Integer tag = (Integer) tv.getTag();
            msgList.get(tag).unReadMsgCount=0;
        }
    }

    @Override
    public void resetView() {
        ViewParent parent = bubbleView.getParent();
        if (parent != null) {
            windowManager.removeView(bubbleView);
            tv.setVisibility(View.VISIBLE);
        }
    }
}
