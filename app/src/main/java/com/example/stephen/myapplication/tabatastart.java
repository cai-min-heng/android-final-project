package com.example.stephen.myapplication;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class tabatastart extends AppCompatActivity
{
    TextView tabataloopint;
    TextView tabatasetint;
    TextView tabataalltime;
    private TextView tabataclock;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabatastart);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        String tabatadata1 = bundle.getString("td1_key");
        String tabatadata2 = bundle.getString("td2_key");

        int data1 = Integer.parseInt(tabatadata1);
        int data2 = Integer.parseInt(tabatadata2);

        tabataalltime = (TextView) findViewById(R.id.tabatalefttime);
        tabataloopint = (TextView) findViewById(R.id.tabataleftloop);
        tabatasetint = (TextView) findViewById(R.id.tabataleftset);
        tabataclock = (TextView) findViewById(R.id.tabataclock);

        tabataloopint.setText("每次" + tabatadata1+ "秒");
        tabatasetint.setText("共" + tabatadata2+ "組");
        tabataalltime.setText("總共有" + String.valueOf((data1*data2)/60)+"分鐘"+String.valueOf((data1*data2)%60)+"秒");

        int a = data1;
        int b = data2;

        final int countLimit=b;

        new CountDownTimer(a*1000+500 , 500)//d2 old =1000
        {
            int countForCountDown = 0;

            @Override
            public void onFinish()
            {
                tabataclock.setText("完成了");
                countForCountDown= countForCountDown+1;
                if(countForCountDown <countLimit )
                {
                    this.start();
                }
            }

            @Override
            public void onTick(long millisUntil)
            {
                tabataclock.setText("剩下" + millisUntil / 1000 + "秒");
            }

        }.start();
    }
}
