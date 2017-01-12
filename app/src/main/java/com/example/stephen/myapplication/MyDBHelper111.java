package com.example.stephen.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stephen on 2017/1/7.
 */

public class MyDBHelper111 extends SQLiteOpenHelper {

    private static final String database ="mydata.db";
    private static final int version=1;

    public MyDBHelper111(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version){
        super(context,name,factory,version);
    }

    public MyDBHelper111(Context context){
        this(context,database,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE myTable(_id integer primary key autoincrement,"+
                "title text no null,"+
                "price text no null,"+
                "qq text no null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS myTable");
        onCreate(db);
    }
}
