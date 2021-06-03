package com.example.maesilnamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestCommunityActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestPostListAdapter adapter;
    private ArrayList<QuestPost> list = new ArrayList<>();
    private int len, cnt = 1, sum = 0;
    private String postType = "any", id;
    private int currentSize;
    private LinearLayoutManager layoutManager;
    private CheckBox check;

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_community);
        recyclerView = findViewById(R.id.post_recyclerview);
        check = findViewById(R.id.auth_check);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPostType();
                list.clear();
                cnt = 1;
                getPostList();
                initAdapter();
                initScrollListener();
            }
        });

        getPostList();
        initAdapter();
        initScrollListener();

    }

    private void setPostType(){
        if(check.isChecked()){
            postType = "unAuth";
        } else {
            postType = "any";
        }
    }

    private void initAdapter() {
        adapter = new QuestPostListAdapter(list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setItemClickListener(new OnQuestPostItemClickListener() {
            @Override
            public void OnItemClick(QuestPostListAdapter.ItemViewHolder holder, View view, int position) {
                QuestPost post = adapter.getItem(position);
                Intent intent = new Intent(QuestCommunityActivity.this, QuestListContentActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == list.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        RequestQueue queue = Volley.newRequestQueue(QuestCommunityActivity.this);
        String url = getString(R.string.url) + "/auth-posting/postings/" + postType + "/" + cnt;
        list.add(null);
        adapter.notifyItemInserted(list.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.remove(list.size() - 1);
                int scrollPosition = list.size();
                adapter.notifyItemRemoved(scrollPosition);
                currentSize = scrollPosition;

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(len == 0) {
                                Toast.makeText(QuestCommunityActivity.this, "마지막 글입니다", Toast.LENGTH_SHORT).show();
                            } else {
                                len = (int) response.get("num");
                                JSONArray posts = response.getJSONArray("postings");

                                int nextLimit = currentSize + len;

                                for(int i = 0; i<len; i++){
                                    if(currentSize - 1 >= nextLimit) break;
                                    JSONObject more_posts = posts.getJSONObject(i);
                                    list.add(new QuestPost(more_posts.get("postingId").toString(), more_posts.get("questName").toString(), more_posts.get("postTitle").toString(), more_posts.get("postContent").toString(),
                                            more_posts.get("picture").toString(), more_posts.get("date").toString(), more_posts.get("writerName").toString(), more_posts.get("writerCode").toString(),
                                            more_posts.getInt("authNum"), more_posts.getInt("pictureNum"), more_posts.getInt("reviewNum"), more_posts.get("type").toString()));
                                    currentSize++;
                                    if(i == len - 1) cnt += 1;
                                }
                                adapter.notifyItemInserted(list.size() - 1);
                                adapter.notifyDataSetChanged();
                                isLoading = false;
                            }
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QuestCommunityActivity.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonObjectRequest);

            }
        }, 1000);
    }

    private void getPostList() {
        RequestQueue queue = Volley.newRequestQueue(QuestCommunityActivity.this);
        String url = getString(R.string.url) + "/auth-posting/postings/" + postType + "/" + cnt;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    len = (int) response.get("num");
                    JSONArray posts = response.getJSONArray("postings");

                    for(int i = 0; i<len; i++){
                        if(cnt == 1){
                            JSONObject object = posts.getJSONObject(i);
                            QuestPost questPost = new QuestPost(object.get("postingId").toString(), object.get("questName").toString(), object.get("postTitle").toString(), object.get("postContent").toString(),
                                    "", object.get("date").toString(), object.get("writerName").toString(), object.get("writerCode").toString(),
                                    object.getInt("authNum"), object.getInt("pictureNum"), object.getInt("reviewNum"), object.get("type").toString());
                            list.add(questPost);
                            if(i == len - 1) cnt += 1;
                        }
                    }
                    sum += len;
                    adapter.notifyItemInserted(list.size() - 1);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuestCommunityActivity.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

}