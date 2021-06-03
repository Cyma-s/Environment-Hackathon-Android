package com.example.maesilnamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MypageActivity extends AppCompatActivity {
    private ImageView userImage;
    private ImageView backButton;
    private TextView userName, ranking, tokenNum;
    private String imageFile, questName, questContent, questCondition, questImage, questPoint, questNumber;
    private Bitmap bitmap;
    private RecyclerView questRecyclerView;
    private LinearLayoutManager questLayoutManager;
    private ArrayList<MyQuest> list = new ArrayList<>();
    private MyQuestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        userImage = (ImageView) findViewById(R.id.mypage_user_image);
        backButton = (ImageView) findViewById(R.id.back_button);
        questRecyclerView = (RecyclerView) findViewById(R.id.mypage_quest_recyclerview);
        userName = (TextView) findViewById(R.id.mypage_user_name);
        ranking = (TextView) findViewById(R.id.mypage_user_ranking);
        tokenNum = (TextView) findViewById(R.id.mypage_user_token_num);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(MypageActivity.this, MainActivity.class);
                finish();
                startActivity(back_intent);
            }
        });
        setMyProfile();
        setMyQuest();
        initAdapter();
        initScrollListener();
    }

    private void initAdapter(){
        adapter = new MyQuestAdapter(list);
        questLayoutManager = new LinearLayoutManager(this);
        questRecyclerView.setAdapter(adapter);
        questRecyclerView.setLayoutManager(questLayoutManager);
        adapter.setQuestItemClickListener(new OnMyQuestItemClickListener() {
            @Override
            public void OnItemClick(MyQuestAdapter.MyQuestItemViewHolder holder, View view, int position) {
                MyQuest quest = adapter.getItem(position);
                if(quest.isComplete()) {
                    Toast.makeText(MypageActivity.this, "이미 완료된 퀘스트입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    questName = quest.getQuestName();
                    questCondition = quest.getCondition();
                    questContent = quest.getExplanation();
                    questImage = quest.getImage();
                    questPoint = quest.getPoint();

                    switch (position) {
                        case 0 :
                            questNumber = "first";
                            break;
                        case 1:
                            questNumber = "second";
                            break;
                        case 2:
                            questNumber = "third";
                            break;
                    }
                    Intent intent = new Intent(MypageActivity.this, QuestLoadActivity.class);

                    intent.putExtra("questNumber", questNumber);
                    intent.putExtra("questName", questName);
                    intent.putExtra("questCondition", questCondition);
                    intent.putExtra("questExplanation", questContent);
                    intent.putExtra("questImage", questImage);
                    intent.putExtra("questPoint", questPoint);
                    startActivity(intent);
                }
            }
        });
    }

    private void initScrollListener() {
        questRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            }
        });
    }

    private void setMyProfile(){
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/user/ranking/myRanking";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String myName, myTokenNum, myRanking;
                    myName = response.get("name").toString();
                    myRanking = "랭킹 " + response.get("ranking").toString() + " 위";
                    myTokenNum = "보유 매실토큰:" + response.get("token").toString() + " 개";
                    imageFile = response.get("image").toString();
                    Log.i("image", imageFile);
                    bitmap = StringToBitmap(imageFile);
                    userImage.setImageBitmap(bitmap);
                    userName.setText(myName);
                    tokenNum.setText(myTokenNum);
                    ranking.setText(myRanking);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };

        queue.add(jsonObjectRequest);
    }

    private Bitmap StringToBitmap(String encodedString){
        try {
            byte[] encodeByte = Base64.getDecoder().decode(encodedString);
            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return decodeBitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void setMyQuest(){ // 퀘스트 정보 가져오기
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/user/myQuest";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject first, second, third;
                    first = (JSONObject)response.get("firstQuest");
                    second = (JSONObject) response.get("secondQuest");
                    third = (JSONObject) response.get("thirdQuest");
                    list.add(setQuest(first.get("questName").toString(), first.get("point").toString(), first.get("explanation").toString(), first.get("condition").toString(), first.get("image").toString()));
                    list.add(setQuest(second.get("questName").toString(), second.get("point").toString(), second.get("explanation").toString(), second.get("condition").toString(), second.get("image").toString()));
                    list.add(setQuest(third.get("questName").toString(), third.get("point").toString(), third.get("explanation").toString(), third.get("condition").toString(), third.get("image").toString()));
                    adapter.notifyItemInserted(list.size() - 1);
                    adapter.notifyDataSetChanged();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };

        queue.add(jsonObjectRequest);
    }

    private MyQuest setQuest(String questName, String point, String explanation, String condition, String image) {
        return new MyQuest(questName, point, explanation, condition, image, false);
    }
/*
    private void setMyPosts(){ // 내 글 보기
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/user/ranking/myRanking";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };

        queue.add(jsonObjectRequest);
    }*/
}