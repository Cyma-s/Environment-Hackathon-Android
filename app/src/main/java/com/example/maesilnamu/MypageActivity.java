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
import android.os.Handler;
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

import org.json.JSONArray;
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
    private RecyclerView questRecyclerView, myPostRecyclerView;
    private LinearLayoutManager questLayoutManager, myPostLayoutManager;
    private ArrayList<MyQuest> list = new ArrayList<>();
    private ArrayList<QuestPost> post = new ArrayList<>();
    private MyQuestAdapter adapter;
    private QuestPostListAdapter listAdapter;
    private boolean isLoading = false;
    private int currentSize, len, cnt = 1, sum = 0;


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
        myPostRecyclerView = (RecyclerView) findViewById(R.id.mypage_mypost_recyclerview);

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
        getMyPostList();
        initMyPostAdapter();
        initMyPostScrollListener();
        initQuestAdapter();
        initQuestScrollListener();

    }

    private void initMyPostAdapter(){
        listAdapter = new QuestPostListAdapter(post);
        myPostLayoutManager = new LinearLayoutManager(this);
        myPostRecyclerView.setAdapter(listAdapter);
        myPostRecyclerView.setLayoutManager(myPostLayoutManager);
        listAdapter.setItemClickListener(new OnQuestPostItemClickListener() {
            @Override
            public void OnItemClick(QuestPostListAdapter.ItemViewHolder holder, View view, int position) {
                QuestPost questPost = listAdapter.getItem(position);
                Intent intent = new Intent(MypageActivity.this, QuestListContentActivity.class);
                intent.putExtra("post", questPost);
                startActivity(intent);
            }
        });
    }

    private void initMyPostScrollListener(){
        myPostRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(!isLoading) {
                    if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == post.size()) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore(){
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/auth-posting/myPostings/" + cnt;
        Log.i("url", url);
        post.add(null);
        listAdapter.notifyItemInserted(post.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                post.remove(post.size() - 1);
                int scrollPosition = post.size();
                listAdapter.notifyItemRemoved(scrollPosition);
                currentSize = scrollPosition;

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(len == 0) {
                                        Toast.makeText(MypageActivity.this, "마지막 글입니다", Toast.LENGTH_SHORT).show();
                                    } else {
                                        len = (int) response.get("num");
                                        JSONArray posts = response.getJSONArray("postings");

                                        int nextLimit = currentSize + len;

                                        for (int i = 0; i<len; i++){
                                            if(currentSize - 1 >= nextLimit) break;
                                            JSONObject morePosts = posts.getJSONObject(i);
                                            post.add(new QuestPost(morePosts.get("postingId").toString(), morePosts.get("questName").toString(), morePosts.get("postTitle").toString(), morePosts.get("postContent").toString(),
                                                    morePosts.get("picture").toString(), morePosts.get("date").toString(), morePosts.get("writerName").toString(), morePosts.get("writerCode").toString(),
                                                    morePosts.getInt("authNum"), morePosts.getInt("pictureNum"), morePosts.getInt("reviewNum"), morePosts.get("type").toString()));
                                            currentSize++;
                                            if(i == len - 1) cnt += 1;
                                        }
                                        listAdapter.notifyItemInserted(post.size() - 1);
                                        listAdapter.notifyDataSetChanged();
                                        isLoading = false;
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MypageActivity.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> heads = new HashMap<String, String>();
                        heads.put("Authorization", "Bearer " + token);
                        return heads;
                    }
                };

                queue.add(jsonObjectRequest);
            }
        }, 1000);
    }

    private void getMyPostList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/auth-posting/myPostings/" + cnt;
        Log.i("url", url);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("response", response.toString());
                            len = (int) response.get("num");
                            JSONArray posts = response.getJSONArray("postings");

                            for(int i = 0; i<len; i++){
                                JSONObject object = posts.getJSONObject(i);
                                QuestPost questPost = new QuestPost(object.get("postingId").toString(), object.get("questName").toString(), object.get("postTitle").toString(), object.get("postContent").toString(),
                                        "", object.get("date").toString(), object.get("writerName").toString(), object.get("writerCode").toString(),
                                        object.getInt("authNum"), object.getInt("pictureNum"), object.getInt("reviewNum"), object.get("type").toString());
                                post.add(questPost);
                                if(i == len - 1) cnt += 1;
                            }
                            sum += len;
                            listAdapter.notifyItemInserted(post.size() - 1);
                            listAdapter.notifyDataSetChanged();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MypageActivity.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
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


    private void initQuestAdapter(){
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

    private void initQuestScrollListener() {
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
        return new MyQuest(questName, point, explanation, condition, image, true);
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