package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuestTutorialActivity extends AppCompatActivity {
    private Button missionButton1, missionButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_tutorial);

        missionButton1 = (Button) findViewById(R.id.mission1);
        missionButton2 = (Button) findViewById(R.id.mission2);

        missionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tutorialCategory = 0;
                Intent intent = new Intent(QuestTutorialActivity.this, QuestTutorialPostActivity.class);
                intent.putExtra("category", tutorialCategory);
                startActivity(intent);
            }
        });
        missionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tutorialCategory = 1;
                Intent intent = new Intent(QuestTutorialActivity.this, QuestTutorialPostActivity.class);
                intent.putExtra("category", tutorialCategory);
                startActivity(intent);
            }
        });
    }
}