package com.example.stephen.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.text.DecimalFormat;

public class aaa extends AppCompatActivity {

    // --------------- //time----------------------
    private Button btn_start;
    private TextView run_time;
    // --------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aaa);

        Button tt=(Button)findViewById(R.id.button4);
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(aaa.this,MainActivity.class);
                startActivity(i);
            }
        });
        //---------------------------------------------------------------------------------------- //time--------------------------------------------------------------------------------------------------------------------
        btn_start = (Button) findViewById(R.id.send);
        run_time = (TextView) findViewById(R.id.message);
        btn_start.setOnLongClickListener(new View.OnLongClickListener() {//////////////////////長按按鈕開始計時///////////////////////
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(aaa.this, "跑步開始", Toast.LENGTH_SHORT)
                        .show();
                //-----------------------------------------------------
                //runThread();         //更新目的座標位置
                firstcatchlocation();     //更新起始座標位置
                //-----------------------------------------------------
                // --------------------------------------------------------------------------------------

                return false;
            }
        });


        //---------------------------------------------------------------------------------map------------------------------------------------------------------------------------------------------------------------

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()             //連接元件
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {                     //執行map非同步
            @Override
            public void onMapReady(GoogleMap googleMap) {                     //取得google map
                if (ActivityCompat.checkSelfPermission(aaa.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(aaa.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;                         //權限檢查
                }
                googleMap.setMyLocationEnabled(true);         //顯示目前位置按鈕

                //  -------------longitude--------------------------------latitude-----------------------------------------------------------------------

                com.example.gpstracking.GPSTracker gps;
                gps = new com.example.gpstracking.GPSTracker(aaa.this);

                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();            //取得緯度
                    double longitude = gps.getLongitude();         //取得經度

                    //-----------------------------------------------------------------------------------
                    MarkerOptions m1 = new MarkerOptions();
                    m1.position(new LatLng(latitude, longitude));    //緯度，經度，標記
                    m1.title("起點");
                    m1.draggable(true);
                    googleMap.addMarker(m1);

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));           //移動鏡頭，初始畫面位置，畫面深度
                    //-----------------------------------------------------------------------------------
                }

                //  -------------longitude--------------------------------latitude------------------------------------------------------------------------

            }
        });

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //--------------------------類別-----------------------------------------------------------------類別-------------------------------------------------類別------------------------------------------------

    //--------------------------runThread()重複更新座標-----------------------------------------longitude--------------------------------latitude-------------------------------------------------------

    int count = 1;

    void runThread(final double lon, final double lat) {
        count = 1;

        new Thread() {
            public void run() {
                do {
                    Message msg = new Message();

                    //--------------起始座標放入bundle----------------------------
                    Bundle b = new Bundle();
                    b.putDouble("lon",lon);
                    b.putDouble("lat",lat);
                    msg.setData(b);              //放入msg

                    //--------------------------------------------------------------------

                    msg.what = 1;                       //代號
                    mHandler.sendMessage(msg);         //透過sendMessage傳遞訊息

                    //--------------------延遲---------------------
                    try{
                        Thread.sleep(1000);              //延遲1秒，1秒更新一次座標位置
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    //--------------------延遲---------------------
                } while(count==1);
            }
        }.start();
    }

    int countt=0;
    private android.os.Handler mHandler=new android.os.Handler(){

        @Override
        public void handleMessage(Message msg){          //執行於main thread
            switch (msg.what){           //判斷代號
                case 1:
                    //-------------------time-----------------------
                    countt++;
                    run_time.setText(countt / 60 + "分" + (countt - ((countt / 60) * 60)) + "秒");    //顯示分和秒
                    //-------------------time-----------------------

                    //  -------------longitude--------------------------------latitude----------------------------------------------------------------
                    //---------------------------------打開bundle讀取資料--------------------
                    Bundle b = msg.getData();
                    final double lonstart = b.getDouble("lon");                    //初始值座標位置
                    final double latstart = b.getDouble("lat");
                    //------------------------------------------------------------------------------

                    TextView aalongitude = (TextView) findViewById(R.id.textView4);
                    TextView bblatitude1 = (TextView) findViewById(R.id.textView5);
                    TextView aabbdistance = (TextView) findViewById(R.id.textView2);


                    com.example.gpstracking.GPSTracker gps;
                    gps = new com.example.gpstracking.GPSTracker(aaa.this);

                    if (gps.canGetLocation()) {

                        final double longitude = gps.getLongitude();         //取得經度
                        final double latitude = gps.getLatitude();       //取得緯度

                        aalongitude.setText(String.valueOf(longitude));
                        bblatitude1.setText(String.valueOf(latitude));

                        aabbdistance.setText(String.valueOf(DistanceText(Distance(longitude, latitude, lonstart, latstart))));

                        //  -------------longitude--------------------------------latitude------------------------------------------------------------------


                        //  ------------------------------------------長按結束跑步按鈕------------------------------------------儲存資料傳換頁面--------------------------------------------------
                        Button sendlonlat = (Button) findViewById(R.id.button);
                        sendlonlat.setOnLongClickListener(new View.OnLongClickListener() {         //-----------長按結束跑步按鈕----------
                            @Override
                            public boolean onLongClick(View v) {

                                TextView a=(TextView)findViewById(R.id.message);      //連結計數的textView

                                String value1=String.valueOf(a.getText());       //getText取出值，String.valueOf轉換成String

                                String value=String.valueOf(DistanceText(Distance(longitude, latitude, lonstart, latstart)));   //距離

                                Bundle bundle=new Bundle();                 //bundle準備傳送儲存資料
                                bundle.putString("value",value);          //距離
                                bundle.putString("value1",value1);          //時間
                                Intent intent=new Intent(aaa.this,bbb.class);
                                intent.putExtras(bundle);

                                startActivity(intent);          //轉換頁面

                                return false;
                            }
                        });
                        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

                    } else {
                        Toast.makeText(aaa.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //-------------------------firstcatchlocation()抓取第一次座標位置-----------------------------------------longitude--------------------------------latitude-------------------------------------------------------

    void firstcatchlocation() {

        //  -------------longitude--------------------------------latitude-----------------------------------------------------------------------
        TextView longitude1 = (TextView) findViewById(R.id.longitude);
        TextView latitude1 = (TextView) findViewById(R.id.latitude);

        com.example.gpstracking.GPSTracker gps;
        gps = new com.example.gpstracking.GPSTracker(aaa.this);

        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();       //取得緯度
            double longitude = gps.getLongitude();         //取得經度

            longitude1.setText(String.valueOf(longitude));
            latitude1.setText(String.valueOf(latitude));
            //-----------------------------------------------------------------------------------
            runThread(longitude,latitude);         //更新目的座標位置，//傳出初始座標
            //-----------------------------------------------------------------------------------
        }
        //  -------------longitude--------------------------------latitude-----------------------------------------------------------------

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //--------------------------帶入距離回傳字串----------------------------------------longitude--------------------------------latitude-----------------------------------------------------------------

    private String DistanceText(double distance)   //帶入距離回傳字串 (距離小於一公里以公尺呈現，距離大於一公里以公里呈現並取小數點兩位)
    {
        if(distance < 1000 ) return String.valueOf((int)distance) + "m" ;
        else return new DecimalFormat("#.00").format(distance/1000) + "km" ;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //-------------------------帶入使用者及景點店家經緯度可計算出距離----------------------------------------longitude--------------------------------latitude-----------------------------------

    public double Distance(double longitude1, double latitude1, double longitude2,double latitude2)     //帶入經緯度計算出距離
    {
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;

        return distance ;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

}