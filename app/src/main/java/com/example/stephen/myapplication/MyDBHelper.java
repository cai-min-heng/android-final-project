package com.example.stephen.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2017/1/9.
 */
public class MyDBHelper extends SQLiteOpenHelper//繼承SQLiteOpenHelper類別
{
    private static final String database="mydata2.db";//資料庫名稱
    private static final int version=1;//資料庫版本
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,name,factory,version);
    }
    public MyDBHelper(Context context)
    {
        this(context,database,null,version);
    }//自建的建構子
    @Override
    public void onCreate(SQLiteDatabase db2)
    {
        db2.execSQL("CREATE TABLE myTable2(_id integer primary key autoincrement,"+"place text no null,"+"sports text no null,"+"time text no null)");
    }//建立myTable2資料表,其中有place,sports,time字串欄位
    @Override
    public void onUpgrade(SQLiteDatabase db2,int oldVersion,int newVersion)
    {
        db2.execSQL("DROP TABLE IF EXISTS myTable2");
        onCreate(db2);
    }//更新資料表
}
