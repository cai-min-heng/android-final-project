package com.example.stephen.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import android.media.MediaPlayer;
public class MainActivity extends AppCompatActivity {
    Button notice;
    Button tabata;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaPlayer mp = MediaPlayer.create(this, R.raw.ccc);
        mp.start();
        notice=(Button) findViewById(R.id.notice);
        tabata=(Button) findViewById(R.id.tabata);
        notice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent i=new Intent();
                i.setClass(MainActivity.this,notice.class);
                startActivityForResult(i,0);
            }
        });
        tabata.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent();
                i.setClass(MainActivity.this,Tabata.class);
                startActivityForResult(i,0);
            }
        });
        Button abc=(Button)findViewById(R.id.button2);
        abc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("value","fuck");
                bundle.putString("value1","fuck");
                Intent intent=new Intent(MainActivity.this,bbb.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button abc11=(Button)findViewById(R.id.button);
        abc11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,aaa.class);
                startActivity(intent);
            }
        });
    }
    final Runnable runnable = new Runnable() {
        public void run() {
            try{
                Thread.sleep(300);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }};
}
