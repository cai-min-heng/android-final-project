package com.example.stephen.myapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Clock extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        new AlertDialog.Builder(Clock.this).setTitle("運動").setMessage("通知").
                setPositiveButton("關閉", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {//建立對話視窗
                        finish();//回到notice
                    }
                }).show();//顯示對話視窗
    }
}
