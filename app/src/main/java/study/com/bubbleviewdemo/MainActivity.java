package study.com.bubbleviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import study.com.bubbleviewdemo.view.BubbleView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BubbleView bv = (BubbleView) findViewById(R.id.bv);
    }
}
