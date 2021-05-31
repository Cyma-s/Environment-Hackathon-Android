package com.example.maesilnamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RankingActivity extends AppCompatActivity {
    private RecyclerView recyclerView, myRankingRecyclerView;
    private RankingAdapter adapter, myRankingAdapter;
    private ArrayList<RankingItem> list = new ArrayList<>();
    private ArrayList<RankingItem> myList = new ArrayList<>();
    private LinearLayoutManager layoutManager, myRankingLayoutManager;
    private TextView rankingUserName, rankingUserPoint, rankingUserRanking;
    private boolean isLoading = false;
    private int totalNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.ranking_recyclerview);
        myRankingRecyclerView = findViewById(R.id.mypage_myranking_recyclerview);

        loadRanking();
        loadMyRanking();
        initAdapter();
        initScrollListener();

        rankingUserName = findViewById(R.id.ranking_user_name);
        rankingUserRanking = findViewById(R.id.ranking_number);
        rankingUserPoint = findViewById(R.id.ranking_accumulate_point);
    }

    private void initAdapter(){
        adapter = new RankingAdapter(list);
        myRankingAdapter = new RankingAdapter(myList);
        layoutManager = new LinearLayoutManager(this);
        myRankingLayoutManager= new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        myRankingRecyclerView.setAdapter(myRankingAdapter);
        recyclerView.setLayoutManager(layoutManager);
        myRankingRecyclerView.setLayoutManager(myRankingLayoutManager);
    }

    private void initScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadRanking(){
        RequestQueue queue = Volley.newRequestQueue(RankingActivity.this);
        String url = getString(R.string.url) + "/user/ranking";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    totalNum = (int) response.get("totalNum");
                    JSONArray ranking = response.getJSONArray("ranking");

                    for(int i = 0; i<totalNum; i++){
                        JSONObject rank = ranking.getJSONObject(i);
                        list.add(new RankingItem(rank.get("name").toString(), rank.get("rank").toString(), rank.get("exp").toString() + " pt"));
                    }

                    adapter.notifyItemInserted(list.size()-1);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RankingActivity.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void loadMyRanking(){
        RequestQueue queue = Volley.newRequestQueue(RankingActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/user/ranking/myRanking";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    myList.add(new RankingItem(response.get("name").toString(), response.get("ranking").toString(), response.get("exp").toString() + " pt"));
                    myRankingAdapter.notifyItemInserted(myList.size()-1);
                    myRankingAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RankingActivity.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
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