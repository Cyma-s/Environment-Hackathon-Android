package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Base64;

public class QuestLoadActivity extends AppCompatActivity {
    private Button participateButton;
    private ImageView backButton, questImage;
    private TextView questDetail, questCondition, questPoint, questTitle;
    private String questDetailData, questConditionData, questPointData, questName, questDetailImage, questNumber;
    private boolean isQuestComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_load);

        participateButton = (Button) findViewById(R.id.quest_participation);
        backButton = (ImageView) findViewById(R.id.back_button);
        questImage = (ImageView) findViewById(R.id.quest_image);
        questDetail = (TextView) findViewById(R.id.quest_detail);
        questPoint = (TextView) findViewById(R.id.quest_point);
        questTitle = (TextView) findViewById(R.id.quest_title);
        questCondition = (TextView) findViewById(R.id.quest_condition);

        Intent intent = getIntent();

        getQuestDetail(intent);
        setQuestDetail();

        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questNumber.equals("story")) {
                    Intent intent1 = new Intent(QuestLoadActivity.this, NextStoryActivity.class);
                    startActivity(intent1);
                }
                if(isQuestComplete) {
                    Toast.makeText(QuestLoadActivity.this, "이미 완료된 퀘스트입니다", Toast.LENGTH_SHORT).show();
                } else {
                    Intent nextIntent = new Intent(QuestLoadActivity.this, QuestPostWriteActivity.class);
                    nextIntent.putExtra("questNumber", questNumber);
                    nextIntent.putExtra("questName", questName);

                    startActivity(nextIntent);
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(QuestLoadActivity.this, HomeActivity.class);
                finish();
                startActivity(back_intent);
            }
        });
    }

    private void getQuestDetail(Intent intent) { /** 퀘스트 상세 정보 set */
        questName = intent.getStringExtra("questName");
        questDetailData = intent.getStringExtra("questExplanation");
        questConditionData = intent.getStringExtra("questCondition");
        questDetailImage = intent.getStringExtra("questImage");
        questNumber = intent.getStringExtra("questNumber");
        questPointData = intent.getStringExtra("questPoint");
        isQuestComplete = intent.getBooleanExtra("questComplete", false);
    }

    private Bitmap StringToBitmap(String encodedString){  /** 서버에서 받아온 이미지 비트맵으로 복구 */
        try {
            byte[] encodeByte = Base64.getDecoder().decode(encodedString);
            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return decodeBitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private void setQuestDetail() {
        /**받아온 미션 정보는 다음 변수에 저장*/
        questPointData = "환경 포인트 : " + questPointData + "pt 제공";
        questTitle.setText(questName);
        questDetail.setText(questDetailData);
        questPoint.setText(questPointData);
        questCondition.setText(questConditionData);
        Bitmap bitmap = StringToBitmap(questDetailImage);
        questImage.setImageBitmap(bitmap);
    }
}