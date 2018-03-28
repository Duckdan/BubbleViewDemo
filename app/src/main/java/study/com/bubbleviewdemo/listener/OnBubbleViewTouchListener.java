package study.com.bubbleviewdemo.listener;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import study.com.bubbleviewdemo.view.BubbleView;

/**
 * Created by Administrator on 2018/3/28.
 */

public class OnBubbleViewTouchListener implements TextView.OnTouchListener {
    private final WindowManager.LayoutParams params;
    private Context context;
    private final WindowManager windowManager;
    private final BubbleView bubbleView;

    public OnBubbleViewTouchListener(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        bubbleView = new BubbleView(context);

        params = new WindowManager.LayoutParams();
        //宽度和高度是
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;//类型是透明
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float actionRawX = event.getRawX();
                float actionRawY = event.getRawY();
                TextView tv = (TextView) v;
                String text = tv.getText().toString().trim();
                bubbleView.setText(text);
                bubbleView.setPosition(actionRawX, actionRawY);
                windowManager.addView(bubbleView, params);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
