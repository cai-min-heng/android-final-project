package com.example.stephen.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class notice extends AppCompatActivity
{
    EditText editplace, editsports, edithour, editminute;
    TextView texplace, textime, texsports;
    Button add, edit, delete, query, return1;
    RadioGroup rg1;
    String excecise = "測速跑步:";
    SQLiteDatabase dbrw2;
    AlarmManager alarmManager;
    PendingIntent pi;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        editplace = (EditText) findViewById(R.id.editplace);
        editsports = (EditText) findViewById(R.id.editsports);
        edithour = (EditText) findViewById(R.id.edithour);
        editminute = (EditText) findViewById(R.id.editminute);
        texplace = (TextView) findViewById(R.id.texplace);
        textime = (TextView) findViewById(R.id.textime);
        texsports = (TextView) findViewById(R.id.texsports);
        add = (Button) findViewById(R.id.add);
        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);
        query = (Button) findViewById(R.id.query);
        rg1 = (RadioGroup) findViewById(R.id.rg1);
        return1 = (Button) findViewById(R.id.return1);
        MyDBHelper dbhelper = new MyDBHelper(this);
        dbrw2 = dbhelper.getWritableDatabase();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedld)
            {
                switch (checkedld)//選擇運動項目
                {
                    case R.id.radioButton:
                        excecise = "測速跑步:";
                        break;
                    case R.id.radioButton2:
                        excecise = "Tabata:";
                        break;
                }
            }
        });
        if (add != null)//新增通知時間
        {
            add.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {//建立對話視窗
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(notice.this);
                    dialog.setTitle("確定要新增嗎?");
                    dialog.setMessage("請選擇是或否");
                    dialog.setNeutralButton("否", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            //不動作
                        }
                    });
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            Bundle b = new Bundle();
                            b.putString("excecise_level", excecise);//把運動項目利用Bundle方法丟進sports欄位中
                            newPlace();//執行副程式
                        }
                    });
                    dialog.show();//顯示對話視窗

                }
            });
        }
        if (edit != null)//修改通知
        {
            edit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {//建立對話視窗
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(notice.this);
                    dialog.setTitle("確定要修改嗎?");
                    dialog.setMessage("請選擇是或否");
                    dialog.setNeutralButton("否", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            //不動作
                        }
                    });
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            Bundle b = new Bundle();
                            b.putString("excecise_level", excecise);//把運動項目利用Bundle方法丟進sports欄位中
                            renewPlace();//執行副程式
                        }
                    });
                    dialog.show();//顯示對話視窗
                }
            });
        }
        if (delete != null)//刪除通知
        {
            delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {//建立對話視窗
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(notice.this);
                    dialog.setTitle("確定要刪除嗎?");
                    dialog.setMessage("請選擇是或否");
                    dialog.setNeutralButton("否", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            //不動作
                        }
                    });
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            deletePlace();//執行副程式
                        }
                    });
                    dialog.show();//顯示對話視窗
                }
            });
        }
        query.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                queryPlace();
            }
        });//查詢通知
        return1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });//返回主頁面
    }
    /*Thread stop = new Thread(new Runnable()//中斷thread
    {
        public void run()
        {
            int d=0;
            try
            {
                if(d==DialogInterface.BUTTON_POSITIVE)
                {
                    thread.interrupt();
                }
            }
            catch (InterruptedException e)
            {
            }
        }
    });
    stop.start();*/
    public void newPlace()//新增通知的副程式
    {
        if (editplace.getText().toString().equals("") || editsports.getText().toString().equals("") || edithour.getText().toString().equals("") || editminute.getText().toString().equals(""))
            Toast.makeText(this, "輸入資料不完全", Toast.LENGTH_SHORT).show();
        else
        {
            /////////////////                                     判斷輸入地點是否等於資料庫內的地點                                       //////////////////////////
            String place = "地點\n", sports = "運動\n", time = "時間\n";//存放結果的字串
            String[] colum = {"place", "sports", "time"};//新增要存放的欄位
            Cursor c;
            //把要新增的地點放入c
            c = dbrw2.query("myTable2", colum, "place=" + "'" + editplace.getText().toString() + "'", null, null, null, null);
            if (c.getCount() > 0) //判斷資料庫內是否有此地點
            {
                c.moveToFirst();//從第一筆開始
                for (int i = 0; i < c.getCount(); i++)//直到最後一筆時跳出迴圈
                {
                    place += c.getString(0) + "\n";//取得place內容
                    sports += c.getString(1) + "\n";//取得sports內容
                    time += c.getString(2) + "\n";//取得time內容
                    c.moveToNext();//下一筆
                }
                Toast.makeText(this, "不能輸入一樣的地點", Toast.LENGTH_SHORT).show();
                editplace.setText("");
                queryPlace2();//顯示資料庫所有的內容
            }
            /////////////////                                     判斷輸入地點是否等於資料庫內的地點                                       //////////////////////////
            else//c.getCount() 等於0表示並無相同的地點則可以新增地點
            {
                int hour = Integer.parseInt(edithour.getText().toString());//設定時間
                int minute = Integer.parseInt(editminute.getText().toString());//設定時間
                ContentValues cv = new ContentValues();//存放新的資料
                cv.put("place", editplace.getText().toString());//新增place的內容
                cv.put("sports", excecise + editsports.getText().toString());//新增sports的內容
                if(hour<=12 && hour>=0)//上午時間
                {
                    if (minute <= 9 && minute >= 0)//判斷minute是否在0到9之間
                    {
                        cv.put("time", "上午" + hour + ":" + "0" + minute);//新增time的內容並在minute前面加上0
                        dbrw2.insert("myTable2", null, cv);//新增資料
                        Toast.makeText(this, "新增地點" + editplace.getText().toString() + "運動:" + editsports.getText().toString() + "時間:" + "上午" + hour + ":" + "0" + minute, Toast.LENGTH_SHORT).show();
                        Thread thread = new Thread(new Runnable()//跑秒數的執行續
                        {
                            public void run()
                            {
                                try
                                {
                                    int hour = Integer.parseInt(edithour.getText().toString());//設定時間,在執行續內必須再擷取一次
                                    int minute = Integer.parseInt(editminute.getText().toString());//設定時間,在執行續內必須再擷取一次
                                    int sec, sec1, sec2, hour2, minute2;
                                    Calendar ca = Calendar.getInstance();//擷取系統時間
                                    ca.setTimeInMillis(System.currentTimeMillis());//擷取系統時間
                                    hour2 = ca.get(Calendar.HOUR_OF_DAY);//系統小時
                                    minute2 = ca.get(Calendar.MINUTE);//系統分鐘
                                    sec1 = (hour * 3600) + (minute * 60);//把設定時間換算成秒數
                                    sec2 = (hour2 * 3600) + (minute2 * 60);//把系統時間換算成秒數
                                    sec = sec1 - sec2;//算出兩個時間的差距
                                    sec = Math.abs(sec);//取絕對值
                                    if(sec2>sec1)//系統時間大於設定時間的話就在用24個小時減掉差距
                                    {
                                        sec=86400-sec;
                                    }
                                    Intent intent=new Intent();
                                    intent.setClass(notice.this,Clock.class);//指定要intent的class
                                    Thread.sleep(sec * 1000);//開始倒數
                                    startActivityForResult(intent,0);//啟動通知
                                }
                                catch (InterruptedException e)//接取例外
                                {
                                    e.printStackTrace();//顯示例外
                                }
                            }
                        });
                        thread.start();//啟動執行續
                    }
                    else if (minute >= 10 && minute <= 59)
                    {
                        cv.put("time", "上午" + hour + ":" + minute);//新增time的內容
                        dbrw2.insert("myTable2", null, cv);//新增資料
                        Toast.makeText(this, "新增地點" + editplace.getText().toString() + "運動:" + editsports.getText().toString() + "時間:" + "上午" + hour + ":" + minute, Toast.LENGTH_SHORT).show();
                        Thread thread=new Thread(new Runnable()//跑秒數的執行續
                        {
                            public void run()
                            {
                                try
                                {
                                    int hour = Integer.parseInt(edithour.getText().toString());//設定時間,在執行續內必須再擷取一次
                                    int minute = Integer.parseInt(editminute.getText().toString());//設定時間,在執行續內必須再擷取一次
                                    int sec, sec1, sec2, hour2, minute2;
                                    Calendar ca = Calendar.getInstance();//擷取系統時間
                                    ca.setTimeInMillis(System.currentTimeMillis());//擷取系統時間
                                    hour2 = ca.get(Calendar.HOUR_OF_DAY);//系統小時
                                    minute2 = ca.get(Calendar.MINUTE);//系統分鐘
                                    sec1 = (hour * 3600) + (minute * 60);//把設定時間換算成秒數
                                    sec2 = (hour2 * 3600) + (minute2 * 60);//把系統時間換算成秒數
                                    sec = sec1 - sec2;//算出兩個時間的差距
                                    sec = Math.abs(sec);//取絕對值
                                    if(sec2>sec1)//系統時間大於設定時間的話就在用24個小時減掉差距
                                    {
                                        sec=86400-sec;
                                    }
                                    Intent intent=new Intent();
                                    intent.setClass(notice.this,Clock.class);//指定要intent的class
                                    Thread.sleep(sec * 1000);//開始倒數
                                    startActivityForResult(intent,0);//啟動通知
                                }
                                catch (InterruptedException e)//接取例外
                                {
                                    e.printStackTrace();//顯示例外
                                }
                            }
                        });
                        thread.start();//啟動執行續
                    }
                    else//minute>59
                    {
                        Toast.makeText(this, "輸入的分鐘不正確", Toast.LENGTH_SHORT).show();
                        editminute.setText("");//請重新輸入一次
                    }
                }
                else if (hour<=23 && hour>=13)//下午時間
                {
                    hour=hour-12;//取用12小時制
                    if (minute <= 9 && minute >= 0)//判斷minute是否在0到9之間
                    {
                        cv.put("time", "下午" + hour + ":" + "0" + minute);//新增time的內容並在minute前面加上0
                        dbrw2.insert("myTable2", null, cv);//新增資料
                        Toast.makeText(this, "新增地點" + editplace.getText().toString() + "運動:" + editsports.getText().toString() + "時間:"+"下午" +hour+ ":"+"0" +minute, Toast.LENGTH_SHORT).show();
                        Thread thread = new Thread(new Runnable()//用來跑秒數的執行續
                        {
                            public void run()
                            {
                                try
                                {
                                    int hour = Integer.parseInt(edithour.getText().toString());//設定時間,在執行續內必須再擷取一次
                                    int minute = Integer.parseInt(editminute.getText().toString());//設定時間,在執行續內必須再擷取一次
                                    int sec,sec1,sec2,hour2,minute2;
                                    Calendar ca = Calendar.getInstance();//擷取系統時間
                                    ca.setTimeInMillis(System.currentTimeMillis());//擷取系統時間
                                    hour2 = ca.get(Calendar.HOUR_OF_DAY);//系統小時
                                    minute2 = ca.get(Calendar.MINUTE);//系統分鐘
                                    sec1 = (hour * 3600) + (minute * 60);//把設定時間換算成秒數
                                    sec2 = (hour2 * 3600) + (minute2 * 60);//把系統時間換算成秒數
                                    sec = sec1 - sec2;//算出兩個時間的差距
                                    sec = Math.abs(sec);//取絕對值
                                    if(sec2>sec1)//系統時間大於設定時間的話就在用24個小時減掉差距
                                    {
                                        sec=86400-sec;
                                    }
                                    Intent intent=new Intent();
                                    intent.setClass(notice.this,Clock.class);//指定要intent的class
                                    Thread.sleep(sec * 1000);//開始倒數
                                    startActivityForResult(intent,0);//啟動通知
                                }
                                catch (InterruptedException e)//接取例外
                                {
                                    e.printStackTrace();//顯示例外
                                }
                            }
                        });
                        thread.start();//啟動執行續
                    }
                    else if(minute>=10 && minute<=59)
                    {
                        cv.put("time","下午"+hour + ":"+minute);//新增time的內容
                        Toast.makeText(this, "新增地點" + editplace.getText().toString() + "運動:" + editsports.getText().toString() + "時間:"+"下午" +hour+ ":" +minute, Toast.LENGTH_SHORT).show();
                        dbrw2.insert("myTable2", null, cv);//新增資料
                        Thread thread = new Thread(new Runnable()//用來跑秒數的執行續
                        {
                            public void run()
                            {
                                try
                                {
                                    int hour = Integer.parseInt(edithour.getText().toString());//設定時間,在執行續內必須再擷取一次
                                    int minute = Integer.parseInt(editminute.getText().toString());//設定時間,在執行續內必須再擷取一次
                                    int sec,sec1,sec2,hour2,minute2;
                                    Calendar ca = Calendar.getInstance();//擷取系統時間
                                    ca.setTimeInMillis(System.currentTimeMillis());//擷取系統時間
                                    hour2 = ca.get(Calendar.HOUR_OF_DAY);//系統小時
                                    minute2 = ca.get(Calendar.MINUTE);//系統分鐘
                                    sec1 = (hour * 3600) + (minute * 60);//把設定時間換算成秒數
                                    sec2 = (hour2 * 3600) + (minute2 * 60);//把系統時間換算成秒數
                                    sec = sec1 - sec2;//算出兩個時間的差距
                                    sec = Math.abs(sec);//取絕對值
                                    if(sec2>sec1)//系統時間大於設定時間的話就在用24個小時減掉差距
                                    {
                                        sec=86400-sec;
                                    }
                                    Intent intent=new Intent();
                                    intent.setClass(notice.this,Clock.class);//指定要intent的class
                                    Thread.sleep(sec * 1000);//開始倒數
                                    startActivityForResult(intent,0);//啟動通知
                                }
                                catch (InterruptedException e)//接取例外
                                {
                                    e.printStackTrace();//顯示例外
                                }
                            }
                        });
                        thread.start();//啟動執行續
                    }
                    else//minute>59
                    {
                        Toast.makeText(this, "輸入的分鐘不正確", Toast.LENGTH_SHORT).show();
                        editminute.setText("");//請重新輸入一次
                    }
                }
                else if (minute>=60 && hour>=24)//hour,minute超出範圍
                {
                    Toast.makeText(this, "輸入的小時和分鐘不正確", Toast.LENGTH_SHORT).show();
                    edithour.setText("");//請重新輸入一次
                    editminute.setText("");//請重新輸入一次

                }
                else
                {
                    Toast.makeText(this, "輸入的小時不正確", Toast.LENGTH_SHORT).show();//hour超出範圍
                    edithour.setText("");//請重新輸入一次
                }
                editplace.setText("");
                editsports.setText("");
                edithour.setText("");
                editminute.setText("");
                queryPlace2();//新增後重新顯示資料庫內容
            }
        }
    }

    public void renewPlace()//修改資料的副程式,有些灰色部分是因為還找不出停止執行續的方法所以時間修改的部分暫時不用
    {
        if (editplace.getText().toString().equals("") || editsports.getText().toString().equals("")/* || edithour.getText().toString().equals("") || editminute.getText().toString().equals("")*/)
            Toast.makeText(this, "輸入修改的資料不完整", Toast.LENGTH_SHORT).show();
        else
        {
            String[] colum = {"place", "sports"/*, "time"*/};//新增要取得的欄位
            Cursor c;
            //把要修改的地點放入c
            c = dbrw2.query("myTable2", colum, "place=" + "'" + editplace.getText().toString() + "'", null, null, null, null);
            if (c.getCount() > 0)//判斷資料庫是否有此地點
            {
                //int hour = Integer.parseInt(edithour.getText().toString());
                // int minute = Integer.parseInt(editminute.getText().toString());
                ContentValues cv = new ContentValues();//存放修改的資料
                cv.put("sports", excecise + editsports.getText().toString());//修改sports的新內容
               /* if(hour<=12 && hour>=0)
                {
                    if(minute<=9 && minute>=0)
                    {
                        cv.put("time","上午"+hour + ":"+"0"+minute);
                        dbrw2.update("myTable2", cv, "place=" + "'" + editplace.getText().toString() + "'", null);
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        editplace.setText("");
                        editsports.setText("");
                        edithour.setText("");
                        editminute.setText("");
                    }
                    else if(minute>=10 && minute<=59)
                    {
                        cv.put("time","上午"+hour + ":"+minute);
                        dbrw2.update("myTable2", cv, "place=" + "'" + editplace.getText().toString() + "'", null);
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        editplace.setText("");
                        editsports.setText("");
                        edithour.setText("");
                        editminute.setText("");
                    }
                   else
                    {
                        Toast.makeText(this, "輸入的分鐘不正確", Toast.LENGTH_SHORT).show();
                        editminute.setText("");
                    }
                }*/
               /* else if (hour<=23 && hour>=13)
                {
                    hour=hour-12;
                    if(minute<=9 && minute>=0)
                    {
                        cv.put("time","下午"+hour + ":"+"0"+minute);
                        dbrw2.update("myTable2", cv, "place=" + "'" + editplace.getText().toString() + "'", null);
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        editplace.setText("");
                        editsports.setText("");
                        edithour.setText("");
                        editminute.setText("");
                    }
                    else if(minute>=10 && minute<=59)
                    {
                        cv.put("time","下午"+hour + ":"+minute);
                        dbrw2.update("myTable2", cv, "place=" + "'" + editplace.getText().toString() + "'", null);
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        editplace.setText("");
                        editsports.setText("");
                        edithour.setText("");
                        editminute.setText("");
                    }
                    else
                    {
                        Toast.makeText(this, "輸入的分鐘不正確", Toast.LENGTH_SHORT).show();
                        editminute.setText("");
                    }
                }*/
                /*else if (minute>=60 && hour>=24)
                {
                    Toast.makeText(this, "輸入的小時和分鐘不正確", Toast.LENGTH_SHORT).show();
                    edithour.setText("");
                    editminute.setText("");
                }
                else
                {
                    Toast.makeText(this, "輸入的小時不正確", Toast.LENGTH_SHORT).show();
                    edithour.setText("");
                }*/
                //修改資料
                dbrw2.update("myTable2", cv, "place=" + "'" + editplace.getText().toString() + "'", null);
                editplace.setText("");
                editsports.setText("");
                queryPlace2();//修改後重新顯示資料庫內容
            }
            else//查詢不到此地點
                Toast.makeText(this, "此" + editplace.getText().toString() + "通知未創立,所以無法修改", Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePlace()//刪除資料的副程式
    {
        if (editplace.getText().toString().equals(""))
            Toast.makeText(this, "請輸入要刪除的地點", Toast.LENGTH_SHORT).show();
        else//只需輸入地點就可刪除通知
        {
            String[] colum = {"place", "sports", "time"};//新增要取得的欄位
            Cursor c;
            //把要刪除的地點放入c
            c = dbrw2.query("myTable2", colum, "place=" + "'" + editplace.getText().toString() + "'", null, null, null, null);
            if (c.getCount() > 0)//判斷資料庫是否有此地點
            {
                //刪除此地點及通知
                dbrw2.delete("myTable2", "place=" + "'" + editplace.getText().toString() + "'", null);
                Toast.makeText(this, "刪除成功", Toast.LENGTH_SHORT).show();
                editplace.setText("");
                editsports.setText("");
                edithour.setText("");
                editminute.setText("");
                queryPlace2();//刪除後重新顯示資料庫內容
            }
            else//查詢不到此地點
                Toast.makeText(this, "此" + editplace.getText().toString() + "地點未創立,所以無法刪除", Toast.LENGTH_SHORT).show();
        }
    }
    public void queryPlace()//查詢資料的副程式
    {
        String place = "地點\n", sports = "運動\n", time = "時間\n";//存放結果的字串
        String[] colum = {"place", "sports", "time"};//新增要存放的欄位
        Cursor c;
        if (editplace.getText().toString().equals(""))//判斷editplace是否為空字串否的話則顯示資料庫所有內容
        {
            //把資料庫所有內容放入c
            c = dbrw2.query("myTable2", colum, null, null, null, null, null);
            if (c.getCount() > 0)//判斷是否資料庫內有無資料
            {
                c.moveToFirst();//從第一筆開始
                for (int i = 0; i < c.getCount(); i++)//直到最後一筆時跳出迴圈
                {
                    place += c.getString(0) + "\n";//取得place內容
                    sports += c.getString(1) + "\n";//取得sports內容
                    time += c.getString(2) + "\n";//取得time內容
                    c.moveToNext();//下一筆
                }
                texplace.setText(place);//顯示內容
                textime.setText(time);//顯示內容
                texsports.setText(sports);//顯示內容
                Toast.makeText(this, "共有" + c.getCount() + "筆通知", Toast.LENGTH_SHORT).show();
            }
            else//表示資料庫內並無通知
            {
                texplace.setText(place);
                textime.setText(time);
                texsports.setText(sports);
                Toast.makeText(this, "無通知", Toast.LENGTH_SHORT).show();
            }
        }
        else//顯示要查詢的地點
        {
            //把要查詢的地點放入c
            c = dbrw2.query("myTable2", colum, "place=" + "'" + editplace.getText().toString() + "'", null, null, null, null);
            if (c.getCount() > 0) //判斷資料庫內是否有此地點
            {
                c.moveToFirst();//從第一筆開始
                for (int i = 0; i < c.getCount(); i++)//直到最後一筆時跳出迴圈
                {
                    place += c.getString(0) + "\n";//取得place內容
                    sports += c.getString(1) + "\n";//取得sports內容
                    time += c.getString(2) + "\n";//取得time內容
                    c.moveToNext();//下一筆
                }
                texplace.setText(place);//顯示內容
                textime.setText(time);//顯示內容
                texsports.setText(sports);//顯示內容
                Toast.makeText(this, "查詢成功", Toast.LENGTH_SHORT).show();
            }
            else//表示要查詢的地點並無創立
                Toast.makeText(this, "此" + editplace.getText().toString() + "通知未創立", Toast.LENGTH_SHORT).show();
        }
    }

    public void queryPlace2()//此副程式用於新增,修改,刪除後顯示資料庫所有內容
    {
        String place = "地點\n", sports = "運動\n", time = "時間\n";//存放結果的字串
        String[] colum = {"place", "sports", "time"};//新增要取得的欄位
        Cursor c;
        //把資料庫所有內容放入c
        c = dbrw2.query("myTable2", colum, null, null, null, null, null);
        if (c.getCount() > 0)//判斷是否資料庫內有無資料
        {
            c.moveToFirst();//從第一筆開始
            for (int i = 0; i < c.getCount(); i++)//直到最後一筆時跳出迴圈
            {
                place += c.getString(0) + "\n";//取得place內容
                sports += c.getString(1) + "\n";//取得sports內容
                time += c.getString(2) + "\n";//取得time內容
                c.moveToNext();//下一筆
            }
            texplace.setText(place);//顯示內容
            textime.setText(time);//顯示內容
            texsports.setText(sports);//顯示內容
        }
        else//表示資料庫內並無通知
        {
            texplace.setText(place);
            textime.setText(time);
            texsports.setText(sports);
        }
    }
}