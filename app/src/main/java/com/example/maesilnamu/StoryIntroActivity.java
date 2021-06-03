package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StoryIntroActivity extends AppCompatActivity {
    private LinearLayout introLayout;
    private TextView introText, nextButton;

    private String tempIntroText1, tempIntroText2, tempIntroText3, tempIntroText4;
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

        this.setIntroText();

        /** 여기서부터는 애니메이션으로 한 글자씩 스토리 내용 출력 */
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
                            if(count == introString1.length())
                                introLayout.setBackgroundResource(R.drawable.intro_image_2);

                            tempIntroText2 += introString2.charAt(count - introString1.length());
                            introText.setText(tempIntroText2);
                            count++;
                        }
                        else if (count < introString1.length() + introString2.length() + introString3.length()) {
                            if(count == introString1.length() + introString2.length())
                                introLayout.setBackgroundResource(R.drawable.intro_image_3);

                            tempIntroText3 += introString3.charAt(count - introString1.length() - introString2.length());
                            introText.setText(tempIntroText3);
                            count++;
                        }
                        else if (count < introString1.length() + introString2.length() + introString3.length() + introString4.length()) {
                            if(count == introString1.length() + introString2.length() + introString3.length()) {
                                introLayout.setBackgroundResource(R.drawable.intro_image_4);
                                nextButton.setVisibility(View.VISIBLE);
                            }

                            tempIntroText4 += introString4.charAt(count - introString1.length() - introString2.length() - introString3.length());
                            introText.setText(tempIntroText4);
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
                Intent intent = new Intent(StoryIntroActivity.this, StoryIntroActivity2.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setIntroText() {
        introString1 = "20XX년에는 연속적인 사람들의 탄소 배출과, 각종 재난들로 인해서, 대기 중 이산화탄소가 많아졌습니다. " +
                "인구수는 이전보다 현저히 줄어들었고, 극한 기후변화 때문에 생물들의 모습 또한 많이 변했습니다. " +
                "이런 세상에도, 감성을 가지고 있는 어린아이가 있었습니다." +
                "                      ";
        introString2 = "아이는 한적한 폐기물 강을 건너다가, 과거의 사람들이 울창한 숲에서 모두 같이 행복하게 지내는 사진을 보게 되었어요. " +
                "낯설기만 하지만 아름답고 따뜻한 사진을 보면서, 아이는 작은 원망감과 부러움을 가지게 되었어요." +
                "                      ";
        introString3 = "울고 있는 이 아이의 이름은 매실아이입니다. 친구와 가족이 모두 떠난 자신의 모습과 너무 달라 보이는 과거를 보고, 서글퍼합니다. " +
                "이 아이가 생각하는 것은 과거의 사람들을 향한 원망일까요... 혹은 사진을 보고 행복해 보이는 사람들과 다른 자신을 향한 혐오감일까요...?" +
                "                      ";
        introString4 = "이때, 슬퍼 보이는 매실아이에게 어떤 빛이 다가왔어요. 매실아이는 신기해하면서 빛에게 말을 걸어보기도 하고, 빛을 만져보려고도 합니다. " +
                "그러다가, 빛을 들여다본 매실아이는 과거의 사람들의 모습을 보게 됩니다.";
    }
}