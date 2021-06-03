package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class StoryLoadActivity extends AppCompatActivity {

    private TextView storyTitle, storyContent1, storyContent2;
    private ImageView backButton, storyImage;
    private Button configureButton;
    private String storyTitleString = "Chapter 1 - 스토리 챕터 제목 or 번호를 추가하세요;";

    private String storyString1 = "여기에는 스토리 내용을 추가해줘요";
    private String storyString2 = "여기에는 스토리 내용을 더 넣거나 넣을 내용이 없으면 컴포넌트를 제거해줘요";
    private String tempTitleString = "";
    private String tempString1 = "";
    private String tempString2 = "";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_load);

        backButton = (ImageView) findViewById(R.id.back_button);
        configureButton = (Button) findViewById(R.id.story_configuration);
        storyImage = (ImageView) findViewById(R.id.story_image);
        storyTitle = (TextView) findViewById(R.id.story_title);
        storyContent1 = (TextView) findViewById(R.id.story_content_1);
        storyContent2 = (TextView) findViewById(R.id.story_content_2);

        this.getStoryContents();

        /** 여기서부터는 애니메이션으로 한 글자씩 스토리 내용 출력 */
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count >= storyString1.length() + storyString2.length())
                            return;
                        else if (count < storyString1.length()){
                            tempString1 += storyString1.charAt(count);
                            storyContent1.setText(tempString1);
                            count++;
                        }
                        else {
                            tempString2 += storyString2.charAt(count - storyString1.length());
                            storyContent2.setText(tempString2);
                            count++;
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 100);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(StoryLoadActivity.this, MypageActivity.class);
                finish();
                startActivity(back_intent);
            }
        });

        configureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(StoryLoadActivity.this, QuestLoadActivity.class);
                finish();
                startActivity(back_intent);
            }
        });
    }

    private void getStoryContents() {
        String url = getString(R.string.url)+"스토리 정보 받는 이메일 주소";

        System.out.println(url);

        /** 미션 정보 받아오는 코드 삽입 */ /*
        RequestQueue queue = Volley.newRequestQueue(StoryLoadActivity.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });  */

        /**받아온 미션 정보는 다음 변수에 저장 */
        storyTitleString = "스토리 챕터 번호 or 챕터 제목";
        storyString1 = "스토리 내용 텍스트 1";
        storyString2 = "스토리 내용 텍스트 2";
        storyTitle.setText(storyTitleString);

        // 여기는 스토리 이미지를 불러와서 저장
        storyImage.setImageResource(R.drawable.little_leaf);
    }
}