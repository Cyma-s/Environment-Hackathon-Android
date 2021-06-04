package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class StoryIntroActivity3 extends AppCompatActivity {
    private TextView endMessage;
    private Button endButton;
    private Animation fadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_intro3);

        endMessage = (TextView) findViewById(R.id.tutorial_end_message);
        endButton = (Button) findViewById(R.id.tutorial_end_button);
        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transparent);

        endMessage.startAnimation(fadein);

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoryIntroActivity3.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}