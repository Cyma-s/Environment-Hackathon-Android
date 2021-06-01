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
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MypageActivity extends AppCompatActivity {
    private ImageView userImage;
    private ImageView backButton;
    private TextView userName, ranking, tokenNum;
    private String imageFile;
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
        getMyQuest();
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
                Intent intent = new Intent(MypageActivity.this, QuestListContentActivity.class);
                startActivity(intent);
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

    private void getMyQuest() {
        RequestQueue queue = Volley.newRequestQueue(MypageActivity.this);
        String url = getString(R.string.url) + ""; // 퀘스트 가져오는 api

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // 각 내용 받아와서 MyQuest에 뿌려주기
                            adapter.notifyItemInserted(list.size() - 1);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MypageActivity.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
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
                    imageFile = response.get("image").toString();
                    Log.i("image", imageFile);
                    bitmap = StringToBitmap(imageFile);
                    userImage.setImageBitmap(bitmap);
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

    /*

    private void setMyQuest(){ // 퀘스트 정보 가져오기
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
    }

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