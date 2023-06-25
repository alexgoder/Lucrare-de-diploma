package com.example.Control_WIFI_Electronice;

import static com.example.Control_WIFI_Electronice.CentralaControl.getFromInputS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trial1_1.R;

import java.io.IOException;

public class AerConditionat extends AppCompatActivity {

    HttpClient httpClient =new HttpClient();
    public static int intensity=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aer_conditional);
        Button acasaBtn,updateBtn;
        ImageButton refreshBtn;
        ImageButton up,down;
        TextView temp,wind,crtTemp;
        SeekBar setTempBar;
        crtTemp=findViewById(R.id.textViewCrtTemp);
        refreshBtn=findViewById(R.id.imageButtonRefresh);
        wind=findViewById(R.id.textViewWind);
        setTempBar=findViewById(R.id.seekBarClima);
        acasaBtn=findViewById(R.id.buttonAcasa);
        updateBtn=findViewById(R.id.buttonSend);
        temp=findViewById(R.id.textViewAC);
        up=findViewById(R.id.imageButtonUp);
        down=findViewById(R.id.imageButtonDown);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String smth=getFromInputS(httpClient.getReq1("temperature"));
                    crtTemp.setText(smth);
                } catch (IOException e) {
                    Toast.makeText(AerConditionat.this, "Nu sunteti conectat la ESP8266-Access-Point", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTempBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AerConditionat.this, "Se trimit datele", Toast.LENGTH_SHORT).show();
            }
        });
        acasaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intensity++;
                if(intensity>8)
                    intensity = 8;
                wind.setText(String.valueOf(intensity));

            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intensity--;
                if(intensity<0)
                    intensity = 0;
                wind.setText(String.valueOf(intensity));
            }
        });


    }
}
