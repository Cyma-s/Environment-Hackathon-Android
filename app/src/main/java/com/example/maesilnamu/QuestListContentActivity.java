package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuestListContentActivity extends AppCompatActivity {
    private String postId;
    private TextView questName, nickName, postTitle, postContent;
    private ImageView userImage;
    private RecyclerView pictureRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list_content_acitivity);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        questName = (TextView) findViewById(R.id.content_questName);
        nickName = (TextView) findViewById(R.id.content_userName);
        postTitle = (TextView) findViewById(R.id.content_postTitle);
        postContent = (TextView) findViewById(R.id.content_postContent);
        pictureRecyclerView = (RecyclerView) findViewById(R.id.content_recyclerviewContent);
        userImage = (ImageView) findViewById(R.id.content_userImage);
        setContent();
    }

    private void getQuestContent(){
        RequestQueue queue = Volley.newRequestQueue(this);

    }

    private void setContent() {
        String contentUrl = getString(R.string.url) + "/";
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        RequestQueue queue = Volley.newRequestQueue(QuestListContentActivity.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, contentUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 인증 글 가져오기
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
}