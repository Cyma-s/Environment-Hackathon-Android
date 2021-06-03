package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestTutorialPostActivity extends AppCompatActivity {
    private ImageView backButton, writeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_tutorial_post);

        backButton = (ImageView) findViewById(R.id.back_button);
        writeButton = (ImageView) findViewById(R.id.postWriteButton);

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestTutorialPostActivity.this, StoryIntroActivity3.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestTutorialPostActivity.this, QuestTutorialActivity.class);
                startActivity(intent);
            }
        });
    }
}