package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MypageActivity extends AppCompatActivity {
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private QuestListAdapter adapter1;
    private MyPostListAdapter adapter2;
    private int currentSize1;
    private int currentSize2;
    private String postType = "any";
    private int cnt = 1;
    private LinearLayoutManager layoutManager;


    private boolean isLoading1 = false;
    private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        backButton = (ImageView) findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(MypageActivity.this, MainActivity.class);
                finish();
                startActivity(back_intent);
            }
        });

    }


}