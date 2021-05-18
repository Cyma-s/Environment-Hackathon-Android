package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private String treeName;
    private double treeExp, maxExp;
    private int treeLevel, maxLevel;
    private Button missionConnect, writeConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getBreedingInfo();

        // gifImage
        ImageView tree = (ImageView) findViewById(R.id.treeImage);
        TextView tree_Name = (TextView) findViewById(R.id.treeName);
        TextView tree_Exp = (TextView) findViewById(R.id.treeExp);
        TextView tree_Level = (TextView) findViewById(R.id.treeLevel);
        TextView max_Exp = (TextView) findViewById(R.id.maxExp);
        TextView max_Level = (TextView) findViewById(R.id.maxLevel);

        if(treeName == null) {
            tree.setImageResource(R.drawable.logo);
        }
        else {
            GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(tree);
            Glide.with(this).load(R.drawable.seed).into(gifImage);
        }

        tree_Name.setText(tree_Name.getText() + ": " + treeName);
        tree_Exp.setText(tree_Exp.getText() + ": " + String.valueOf(treeExp));
        tree_Level.setText(tree_Level.getText() + ": " + String.valueOf(treeLevel));
        max_Exp.setText(max_Exp.getText() + ": " + String.valueOf(maxExp));
        max_Level.setText(max_Level.getText() + ": " + String.valueOf(maxLevel));

        missionConnect = (Button) findViewById(R.id.mission_connect);
        writeConnect = (Button) findViewById(R.id.mission_write);
        missionConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestPostWriteActivity.class);
                finish();
                startActivity(intent);
            }
        });
        writeConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestPostWriteActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void getBreedingInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        String url = getString(R.string.url) + "/tree/breeding";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    treeName = (String) response.get("treeName");
                    treeExp = (double)response.get("treeExp");
                    treeLevel = (int)response.get("treeLevel");
                    maxExp = (double)response.get("maxExp");
                    maxLevel = (int)response.get("maxLevel");
                    JSONArray posts = response.getJSONArray("postings");

                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
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