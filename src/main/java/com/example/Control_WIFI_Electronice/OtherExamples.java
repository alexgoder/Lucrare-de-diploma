package com.example.Control_WIFI_Electronice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.trial1_1.R;
import androidx.appcompat.app.AppCompatActivity;

public class OtherExamples extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button homeBtn,aerCondBtn,semaforBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_examples);
        homeBtn=findViewById(R.id.buttonHomeOthers);
        semaforBtn=findViewById(R.id.semaforBtn);
        aerCondBtn=findViewById(R.id.AerConditionatBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        aerCondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(),AerConditionat.class);
                startActivity(intent);
            }
        });
        semaforBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(),SemaforExemplu.class);
                startActivity(intent);
            }
        });
    }
}
