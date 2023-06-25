package com.example.Control_WIFI_Electronice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trial1_1.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CentralaControl extends AppCompatActivity {
    Button backBtn,updateBtn,crtDataBtn;
    SeekBar tempBar,timeBar,UmidBar;
    TextView tempTxt,timeTxt,umidTxt;
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


        setContentView(R.layout.centrala_control);
        tempBar=findViewById(R.id.seekBarTemperatura);
        timeBar=findViewById(R.id.seekBarTimp);
        backBtn=findViewById(R.id.centralaBackButton);
        updateBtn=findViewById(R.id.sendCentralaButton);
        tempTxt=findViewById(R.id.getTempTxtView);
        timeTxt=findViewById(R.id.getTimeTxtView);
        umidTxt=findViewById(R.id.UmidityTxt);
        crtDataBtn=findViewById(R.id.dateCrtBtn);
        UmidBar=findViewById(R.id.seekBarUmid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String textToBe=String.valueOf(timeBar.getProgress())+" h";
            tempTxt.setText(String.valueOf(tempBar.getMin()));
            timeTxt.setText(textToBe);
            timeTxt.setBackgroundColor(255);
            umidTxt.setText("20%");
        }

        crtDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempr,humid;
                try {
                    tempTxt.setText(getTemp());
                    umidTxt.setText(getHumidity());
                } catch (URISyntaxException | IOException e) {
                    //throw new RuntimeException(e);
                    Toast.makeText(CentralaControl.this, "Nu sunteti conectat la ESP8266-Access-Point", Toast.LENGTH_SHORT).show();
                }
            }
        });

        UmidBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text=String.valueOf(progress)+"%";
                umidTxt.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String actualTime = "";

                if (progress < 2) {
                    actualTime += "30 mins";
                } else {
                    actualTime += String.valueOf(progress / 2);
                    if (progress % 2 == 1) {
                        actualTime +="h 30 mins";
                    }
                    else{
                        actualTime+="h";
                    }
                }
                timeTxt.setText(actualTime);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tempBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempTxt.setText(String.valueOf(progress)+"°C");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CentralaControl.this, "Se trimit datele...", Toast.LENGTH_SHORT).show();
                HttpClient httpClientnew=new HttpClient();
                httpClientnew.sendReqCentrala(tempBar.getProgress());
            }
        });
        //init();
    }

    public void init(){
        try {
            getTemp();
            getHumidity();
        } catch (URISyntaxException | IOException e) {
            Toast.makeText(CentralaControl.this, "Nu sunteti conectat la ESP8266-Access-Point", Toast.LENGTH_SHORT).show();
        }
    }

    public String getTemp() throws URISyntaxException, IOException {
        HttpClient httpClient =new HttpClient();
        String smth=getFromInputS(httpClient.getReq1("temperature"));
        Log.d("msg",smth);
        tempBar.setProgress(Integer.parseInt(smth));
        return smth+"°C";//dummy.getRequest("temperature");
    }

    public String getHumidity() throws URISyntaxException, IOException {
        HttpClient httpClient =new HttpClient();
        String smth=getFromInputS(httpClient.getReq1("humidity"));
        Log.d("msg",smth);
        UmidBar.setProgress(Integer.parseInt(smth));
        return smth+"%";
    }

    public static String getFromInputS(InputStream is){

        String text="";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            text += new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        }
        return text;
    }
}
