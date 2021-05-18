package com.example.maesilnamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class QuestCommunityActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private QuestPostListAdapter adapter;
    private ArrayList<QuestPost> list = new ArrayList<>();

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_community);
        recyclerView = findViewById(R.id.post_recyclerview);
        insertData();
        initAdapter();
        initScrollListener();
    }

    private void insertData(){
        for(int i = 0; i<20; i++){
            list.add(new QuestPost("", "이건 제목" + i, "안녕?", "오늘",
                    "나야", "인증됨", 3));
        }
    }

    private void initAdapter() {
        adapter = new QuestPostListAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(!isLoading){
                    if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == list.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        list.add(null);
        adapter.notifyItemInserted(list.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.remove(list.size() - 1);
                int scrollPosition = list.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 20;

                while(currentSize - 1 < nextLimit) {
                    list.add(new QuestPost("", "이건 제목" + currentSize, "안녕?", "오늘",
                            "나야", "인증됨", 3));
                    currentSize++;
                }

                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

}