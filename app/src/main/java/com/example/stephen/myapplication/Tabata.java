package com.example.stephen.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Tabata extends AppCompatActivity {
    Button return4;
    Button tabatastart;
    EditText tabatasend_sporttime;
    EditText tabatasend_settime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata);

        tabatastart = (Button)findViewById(R.id.tabatago);
        tabatastart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Tabata.this, tabatastart.class);

                tabatasend_sporttime = (EditText) findViewById(R.id.tabatasporttime);
                String temp1 = tabatasend_sporttime.getText().toString();

                tabatasend_settime = (EditText) findViewById(R.id.tabatasettime);
                String temp2 = tabatasend_settime.getText().toString();

                Bundle bundle = new Bundle();

                bundle.putString("td1_key", temp1);
                bundle.putString("td2_key", temp2);
                getIntent().putExtra("key",bundle);

                i.putExtras(bundle);
                startActivity(i);
            }
        });

        return4=(Button) findViewById(R.id.return4);
        return4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
}
