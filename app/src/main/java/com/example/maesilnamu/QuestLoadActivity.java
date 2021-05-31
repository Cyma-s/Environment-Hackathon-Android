package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class QuestLoadActivity extends AppCompatActivity {
    private Button participateButton;
    private ImageView backButton, questImage;
    private TextView questDetail, questCondition, questPoint;
    private String questDetailData, questConditionData, questPointData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_load);

        participateButton = (Button) findViewById(R.id.quest_participation);
        backButton = (ImageView) findViewById(R.id.back_button);
        questImage = (ImageView) findViewById(R.id.quest_image);
        questDetail = (TextView) findViewById(R.id.quest_detail);
        questPoint = (TextView) findViewById(R.id.quest_point);
        questCondition = (TextView) findViewById(R.id.quest_condition);

        this.getQuestDetail();

        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(QuestLoadActivity.this, QuestPostWriteActivity.class);
                finish();
                startActivity(nextIntent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(QuestLoadActivity.this, Mypage.class);
                finish();
                startActivity(back_intent);
            }
        });
    }

    private void getQuestDetail() {
        String url = getString(R.string.url)+"미션 정보 받는 이메일 주소";

        System.out.println(url);

        /** 미션 정보 받아오는 코드 삽입 */ /*
        RequestQueue queue = Volley.newRequestQueue(QuestLoadActivity.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Intent next_intent = new Intent(QuestLoadActivity.this, QuestPostWriteActivity.class);

                        startActivity(next_intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuestLoadActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
            }
        }); */

        /**받아온 미션 정보는 다음 변수에 저장*/
        questDetailData = "미션 정보";
        questConditionData = "미션 확인 방법 정보";
        questPointData = "환경 포인트 : " + "포인트 숫자" + "pt 제공";
        questDetail.setText(questDetailData);
        questPoint.setText(questPointData);
        questCondition.setText(questDetailData);
    }
}