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

public class StoryIntroActivity2 extends AppCompatActivity {
    private LinearLayout introLayout;
    private TextView introText, nextButton;

    private String tempIntroText1="", tempIntroText2="", tempIntroText3, tempIntroText4;
    private String introString1 = "", introString2 = "", introString3 = "", introString4 = "";
    private int count = 0;
    private boolean toggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_intro);

        introLayout = (LinearLayout) findViewById(R.id.intro_Layout);
        introText = (TextView) findViewById(R.id.intro_text);
        nextButton = (TextView) findViewById(R.id.nextbutton);

        introLayout.setBackgroundResource(R.drawable.intro_image_3);
        introText.setMovementMethod(new ScrollingMovementMethod());
        this.setIntroText();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count < introString1.length()) {
                            introLayout.setBackgroundResource(R.drawable.intro_image);
                            tempIntroText1 += introString1.charAt(count);

                            introText.setText(tempIntroText1);
                            count++;
                        }
                        else if (count < introString1.length() + introString2.length()){
                            if(count == introString1.length()) {
                                introLayout.setBackgroundResource(R.drawable.intro_image_2);
                                nextButton.setVisibility(View.VISIBLE);
                            }

                            tempIntroText2 += introString2.charAt(count - introString1.length());
                            introText.setText(tempIntroText2);
                            count++;
                        }
                        else {
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
                Intent intent = new Intent(StoryIntroActivity2.this, QuestTutorialActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setIntroText() {
        introString1 = "매실아이는 빛에게 말합니다. 저 사람들이 사소하지만 너무나 중요한 분리수거를 제대로 하게 해달라고 말합니다. " +
                "빛은 크게 갑자기 크게 빛나더니 매실아이를 감싸기 시작합니다" +
                "                      ";
        introString2 = "아마도 이 빛은 매실아이의 세상과 관련이 있어 보입니다. 빛 속에서의 사람들은 분리수거를 하기 시작했고, 매실아이의 옆에는 작지만 초록색의 새싹이 돋아났기 때문이죠. " +
                "매실아이는 이 새싹을 더 소중하게, 더 예쁘게 키우고 싶어 합니다. 다시 매실아이는 그 빛 속을 내다 보았습니다." +
                "                      ";
    }
}