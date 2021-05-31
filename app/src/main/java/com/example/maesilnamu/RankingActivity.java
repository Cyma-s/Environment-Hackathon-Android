package com.example.maesilnamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
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

public class RankingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RankingAdapter adapter;
    private ArrayList<RankingItem> list = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private TextView rankingUserName, rankingUserPoint, rankingUserRanking;
    private boolean isLoading = false;
    private int currentSize, totalNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.ranking_recyclerview);

        loadRanking();
        initAdapter();


        rankingUserName = findViewById(R.id.ranking_user_name);
        rankingUserRanking = findViewById(R.id.ranking_number);
        rankingUserPoint = findViewById(R.id.ranking_accumulate_point);
    }

    private void initAdapter(){
        adapter = new RankingAdapter(list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
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
}