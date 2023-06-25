package com.example.Control_WIFI_Electronice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.trial1_1.R;
import androidx.appcompat.app.AppCompatActivity;

public class SemaforExemplu extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semafor_exemplu);
        Button backBtn,sendBtn;
        EditText red,green,yellow;
        red=findViewById(R.id.editTextRed);
        green=findViewById(R.id.editTextGreen);
        yellow=findViewById(R.id.editTextYellow);
        backBtn=findViewById(R.id.semaforBacbBtn);
        sendBtn=findViewById(R.id.semaforSendBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpClient httpClient =new HttpClient();
                String redString=red.getText().toString();
                String greenString=green.getText().toString();
                String yellowString=yellow.getText().toString();
                httpClient.sendReqSemafor(Integer.parseInt(redString),Integer.parseInt(greenString),Integer.parseInt(yellowString));
                Toast.makeText(SemaforExemplu.this, "Se trimit datele pentru semafor...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
