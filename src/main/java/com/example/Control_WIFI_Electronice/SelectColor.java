package com.example.Control_WIFI_Electronice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import com.example.trial1_1.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;

public class SelectColor extends AppCompatActivity {

    int rint=0,gint=0,bint=0;

    TextView colorTxt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_color);
        Button homeBtn,sendBtn;
        TextView red,green,blue;
        SeekBar redBar,greenBar,blueBar;
        red=findViewById(R.id.RedTxtValue);
        green=findViewById(R.id.GreenTxtValue);
        blue=findViewById(R.id.BlueTxtValue);
        colorTxt=findViewById(R.id.resultColorTxt);

        redBar = findViewById(R.id.seekBarRed);
        greenBar=findViewById(R.id.seekBarGreen);
        blueBar=findViewById(R.id.seekBarBlue);


        int progress=0;
        /* initialize textviews*/
        red.setText(String.valueOf(progress));
        green.setText(String.valueOf(progress));
        blue.setText(String.valueOf(progress));
        //assign functionality to seekbars
        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rint=progress;
                red.setText(String.valueOf(rint));
                updateColor(rint,gint,bint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gint=progress;
                green.setText(String.valueOf(gint));
                updateColor(rint,gint,bint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bint=progress;
                blue.setText(String.valueOf(bint));
                updateColor(rint,gint,bint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        homeBtn=findViewById(R.id.GoHomeBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        sendBtn=findViewById(R.id.ChangeColorBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="Se trimite culoarea...";

                HttpClient httpClient =new HttpClient();
                try {
                    httpClient.setBlue(bint);
                    httpClient.setGreen(gint);
                    httpClient.setRed(rint);
                    httpClient.sendRequest();
                } catch (URISyntaxException e) {
                    text="Nu sunteti conectat la ESP8266-Access-Point";
                }

                Toast.makeText(SelectColor.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateColor(int r,int g,int b){
        colorTxt.setBackgroundColor(Color.rgb(r,g,b));
    }
}
