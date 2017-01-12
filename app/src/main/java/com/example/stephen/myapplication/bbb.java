package com.example.stephen.myapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class bbb extends AppCompatActivity {


    ListView no;
    SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbb);

        Button aaa=(Button)findViewById(R.id.button3);
        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(bbb.this,MainActivity.class);
                startActivity(intent);
            }
        });


        no=(ListView)findViewById(R.id.listView);
        MyDBHelper111 dbHelper=new MyDBHelper111(this);
        dbrw=dbHelper.getWritableDatabase();


        Bundle bundle=getIntent().getExtras();
        if(bundle.getString("value").equals("fuck")==false) {
            newbook(bundle.getString("value"), bundle.getString("value1"));
        }

        querybook();


    }

    public void newbook(String qq,String aa)
    {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()) ;


        ContentValues cv =new ContentValues();
        cv.put("qq",formatter.format(curDate));
        cv.put("title",qq);
        cv.put("price",aa);

        dbrw.insert("myTable",null,cv);

        querybook();

    }

    public void deletbook(int qq)
    {
        String[] colum={"title","price","qq"};
        Cursor c;

        c=dbrw.query("myTable",colum,null,null,null,null,null);

        String[] index=new String[c.getCount()];

        if(c.getCount()>0) {
            c.moveToFirst();
            for (int i = (c.getCount() - 1); i >= 0; i--) {
                index[i] = c.getString(2);
                c.moveToNext();
            }
        }


        dbrw.delete("myTable","qq="+"'"+index[qq]+"'",null);

        Toast.makeText(this,"刪除成功",Toast.LENGTH_SHORT).show();
        querybook();


    }

    public void querybook()
    {
        String[] colum={"title","price","qq"};
        String qqq="                              ";

        Cursor c;

        c=dbrw.query("myTable",colum,null,null,null,null,null);

        String[] index=new String[c.getCount()];

        if(c.getCount()>0)
        {
            c.moveToFirst();
            for(int i=(c.getCount()-1);i>=0;i--)
            {

                index[i] =c.getString(2)+"\n"+c.getString(0) +qqq.substring((c.getString(0).length())*2) + c.getString(1);
                c.moveToNext();
            }

            ArrayAdapter<String> Arrayindex=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,index);

            no.setAdapter(Arrayindex);

            no.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    // deletbook(position);
                    selece(position);
                }
            });

        }
    }

    public void selece(final int num)
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(bbb.this);
        dialog.setTitle("刪除確認");
        dialog.setMessage("您確定要刪除這筆紀錄嗎");
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(bbb.this, "取消",Toast.LENGTH_SHORT).show();
            }

        });
        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deletbook(num);
            }

        });

        dialog.show();

    }

}
