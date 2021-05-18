package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button missionConnect, writeConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        missionConnect = (Button) findViewById(R.id.mission_connect);
        writeConnect = (Button) findViewById(R.id.mission_write);
        missionConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestPostWriteActivity.class);
                finish();
                startActivity(intent);
            }
        });
        writeConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestPostWriteActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}