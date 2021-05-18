package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class QuestPostWriteActivity extends AppCompatActivity {
    private EditText postWriteTitle, postWriteContent;
    private ImageView postWriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_post_write);
        postWriteButton = (ImageView) findViewById(R.id.postWriteButton);
        postWriteTitle = (EditText) findViewById(R.id.postWriteTitle);
        postWriteContent = (EditText) findViewById(R.id.postWriteContent);

        postWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String writeTitle, writeContent, questName, picture;
                writeTitle = postWriteTitle.getText().toString();
                writeContent = postWriteContent.getText().toString();
                questName = "퀘스트임";
                picture = "picture 아직 안 만듬";
                try {
                    sendToServer(questName, writeTitle, writeContent, picture);
                    Intent intent = new Intent(QuestPostWriteActivity.this, QuestCommunityActivity.class);
                    finish();
                    startActivity(intent);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendToServer(String questName, String title, String content, String picture) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/auth-posting";
        RequestQueue queue = Volley.newRequestQueue(QuestPostWriteActivity.this);

        JSONObject questPost = new JSONObject();
        questPost.put("questName", questName);
        questPost.put("postTitle", title);
        questPost.put("postContent", content);
        questPost.put("picture", picture);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, questPost,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(QuestPostWriteActivity.this, "내부 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
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