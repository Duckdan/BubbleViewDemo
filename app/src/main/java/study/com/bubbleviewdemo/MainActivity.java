package study.com.bubbleviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import study.com.bubbleviewdemo.adapter.MsgAdapter;
import study.com.bubbleviewdemo.bean.Msg;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BubbleView bv = (BubbleView) findViewById(R.id.bv);
//        bv.setText(99 + "");
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<Msg> msgList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            msgList.add(new Msg("标题" + i, i));
        }
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setAdapter(new MsgAdapter(msgList, this));
    }
}
