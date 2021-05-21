package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class QuestListContentActivity extends AppCompatActivity {
    private String postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list_content_acitivity);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        String contentUrl = getString(R.string.url) + "/";
        RequestQueue queue = Volley.newRequestQueue(QuestListContentActivity.this);

    }
}