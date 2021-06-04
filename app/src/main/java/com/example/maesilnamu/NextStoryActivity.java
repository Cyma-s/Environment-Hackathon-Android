package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.VISIBLE;

public class NextStoryActivity extends AppCompatActivity {
    private LinearLayout introLayout;
    private TextView introText, nextButton;

    private String tempIntroText1="";
    private String introString1 = "";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_story);

        introText = (TextView) findViewById(R.id.next_text);
        nextButton = (TextView) findViewById(R.id.nextbutton);
        introText.setMovementMethod(new ScrollingMovementMethod());
        this.setNextStory();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count < introString1.length()) {
                            tempIntroText1 += introString1.charAt(count);
                            introText.setText(tempIntroText1);
                            count++;
                        } else {
                            nextButton.setVisibility(VISIBLE);
                            return;
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 100);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NextStoryActivity.this, HomeActivity.class);
                intent.putExtra("change", true);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setNextStory(){
        introString1 = "매실아이는 나무가 신기합니다. 이 생물은 무엇을 위해 존재하는지,  어떤 가능성을 가지고 있는지, " +
                "또, 이 빛은 계속 자신을 도와줄 수 있는지, 세상이 이런 작은 실천을 통해 바뀔 수 있는지 궁금해합니다. " +
                "여러분들의 작은 실천이 매실 아이의 현실을 바꿀 수 있습니다. " +
                "                      ";
    }
}