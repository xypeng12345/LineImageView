package com.xyp.meyki_bear.imageviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private LineChartViewGroup lcv;
    private List<Integer> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lcv= (LineChartViewGroup) findViewById(R.id.lcv_1);
        initData();
    }


    private void initData() {
        list=new ArrayList<>();
        Random random=new Random();
        random.nextInt(10);
        for (int i=0;i<10;i++){
            list.add(random.nextInt(100));
        }
        lcv.setList(list);
    }
}
