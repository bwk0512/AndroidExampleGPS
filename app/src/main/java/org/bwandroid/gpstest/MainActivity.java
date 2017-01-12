package org.bwandroid.gpstest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{

    GPSManager gps;

    private TimerTask myTimer;
    Handler handler;

    private TextView textviewLon,
            textviewLat,
            textviewDistance,
            textviewTime,
            textviewSpeed,
            textviewUserState;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textviewLon = (TextView)findViewById( R.id.textview_Lon );
        textviewLat = (TextView)findViewById( R.id.textview_Lat);
        textviewDistance = (TextView)findViewById( R.id.textview_Distance );
        textviewTime = (TextView)findViewById( R.id.textview_Time );
        textviewSpeed = (TextView)findViewById( R.id.textview_Speed );
        textviewUserState = (TextView)findViewById( R.id.textview_UserState );

        gps = new GPSManager(this, this);

        gps.getLocation();
        gps.initialization(gps.getLongitude(), gps.getLatitude());

        myTimer = new TimerTask() {
            @Override
            public void run() {
                Log.d("tag", "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzrun()");

                // 외부 쓰래드(time)를 이용해서 UI를 변경시키면 오류가 나기 때문에 핸들러를 사용
                // 핸들러는 메인스레드와 서브스레드 간의 통신을 위해 사용한다
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };

        // handler 안에 들어갈 코드
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if(gps.getData() == 0)
                    textviewUserState.setText("제자리");
                else
                    textviewUserState.setText("이동 중");

                textviewLon.setText("" + gps.getLongitude());
                textviewLat.setText("" + gps.getLatitude());
                textviewDistance.setText("" + gps.getDistance());
                textviewTime.setText("" + gps.getTime());
                textviewSpeed.setText("" + gps.getSpeed());
            }

        };


        Timer timer = new Timer();
        timer.schedule(myTimer,5000,5000); // App 시작 5초 이후에 5초마다 실행


        findViewById( R.id.button_Re ).setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                    textviewLon.setText("Reflash");
                    textviewLat.setText("Reflash");
                    textviewDistance.setText("Reflash");
                    textviewTime.setText("Reflash");
                    textviewSpeed.setText("Reflash");

                    //gps.getLocation();
                    gps.onLocationChanged(gps.location);
                }
            }
        );
    }

}
