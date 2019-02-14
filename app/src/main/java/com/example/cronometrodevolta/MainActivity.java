package com.example.cronometrodevolta;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnStart, btnPause, btnLap;
    private TextView txtTimer;
    private Handler handler = new Handler();
    private LinearLayout linearLayout;
    private long startTime = 0L, tempoEmMilissegundos = 0L, timeSwapBuff = 0L, updateTime = 0L;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tempoEmMilissegundos = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + tempoEmMilissegundos;
            int segundos = (int) (updateTime / 1000);
            int minutos = segundos / 60;
            segundos %= 60;
            int milissegundos = (int) (updateTime % 1000);
            txtTimer.setText("" + minutos + ":" + String.format("%02d", segundos) +
                    ":" + String.format("%03d", milissegundos));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLap = findViewById(R.id.btnLap);
        btnPause = findViewById(R.id.btnPause);
        btnStart = findViewById(R.id.btnStart);
        txtTimer = findViewById(R.id.txtTimeValue);
        linearLayout = findViewById(R.id.container);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += tempoEmMilissegundos;
                handler.removeCallbacks(runnable);
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.row, null);
                TextView txtValue = (TextView) view.findViewById(R.id.txtContent);
                txtValue.setText(txtTimer.getText());
                linearLayout.addView(view);
            }
        });
    }
}
