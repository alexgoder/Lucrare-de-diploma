package com.example.Control_WIFI_Electronice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.trial1_1.R;

enum Router{
    RGB,
    Centrala,
    Semafor,
    Default
};

public class MainActivity extends AppCompatActivity {

    public static Router router=Router.Default;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button rgbBtn,centralaBtn,otherBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        centralaBtn=findViewById(R.id.sendToCentralaBtn);
        rgbBtn=findViewById(R.id.sendToRGBBtn);
        otherBtn=findViewById(R.id.goToOtherObjectBtn);
        otherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                router=Router.Default;
                Intent intent=new Intent(v.getContext(),OtherExamples.class);
                startActivity(intent);
            }
        });
        centralaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                router=Router.Centrala;
                Intent intent=new Intent(v.getContext(),WifiSearch.class);
                startActivity(intent);
            }
        });
        rgbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                router=Router.RGB;
                Intent intent=new Intent(v.getContext(),WifiSearch.class);
                startActivity(intent);
            }
        });
    }
}