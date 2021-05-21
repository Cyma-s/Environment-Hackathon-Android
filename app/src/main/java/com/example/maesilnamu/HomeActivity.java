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
    public String treeName, treeExp, maxExp, userExp, treeLevel, maxLevel, userToken;
    private Button missionConnect, writeConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getBreedingInfo();

        // exp Up
        Button expUp = (Button) findViewById(R.id.expUp);
        expUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                    treeName = (String)response.get("treeName");

                    treeExp = (String)response.get("treeExp");
                    treeLevel = (String)response.get("treeLevel");
                    maxExp = (String)response.get("maxExp");
                    maxLevel = (String)response.get("maxLevel");
                    userExp = (String)response.get("userExp");
                    userToken = (String)response.get("userToken");

                    // gifImage
                    ImageView tree = (ImageView) findViewById(R.id.treeImage);
                    TextView tree_Name = (TextView) findViewById(R.id.treeName);
                    TextView tree_Exp = (TextView) findViewById(R.id.treeExp);
                    TextView tree_Level = (TextView) findViewById(R.id.treeLevel);
                    TextView max_Exp = (TextView) findViewById(R.id.maxExp);
                    TextView max_Level = (TextView) findViewById(R.id.maxLevel);
                    TextView user_Exp = (TextView) findViewById(R.id.userExp);
                    TextView user_Token = (TextView) findViewById(R.id.userToken);

                    if(!treeName.equals("")) {
                        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(tree);
                        if (Double.parseDouble(maxExp) / 4 > Double.parseDouble(userExp))
                            Glide.with(HomeActivity.this).load(R.drawable.seed).into(gifImage);

                        else if (Double.parseDouble(maxExp) * 2 / 4 > Double.parseDouble(userExp))
                            Glide.with(HomeActivity.this).load(R.drawable.sprout).into(gifImage);

                        else if (Double.parseDouble(maxExp) * 3 / 4 > Double.parseDouble(userExp))
                            Glide.with(HomeActivity.this).load(R.drawable.little_leaf).into(gifImage);

                        else if (Double.parseDouble(maxExp) > Double.parseDouble(userExp))
                            Glide.with(HomeActivity.this).load(R.drawable.small_tree).into(gifImage);

                        else {
                            if (treeName == "BasicTree")
                                Glide.with(HomeActivity.this).load(R.drawable.basic_tree).into(gifImage);
                            else if (treeName == "WinterTree")
                                Glide.with(HomeActivity.this).load(R.drawable.winter_tree).into(gifImage);
                        }
                    }
                        tree_Name.setText(tree_Name.getText() + ": " + treeName);
                        tree_Exp.setText(tree_Exp.getText() + ": " + treeExp);
                        tree_Level.setText(tree_Level.getText() + ": " + treeLevel);
                        max_Exp.setText(max_Exp.getText() + ": " + maxExp);
                        max_Level.setText(max_Level.getText() + ": " + maxLevel);
                        user_Exp.setText(user_Exp.getText() + ": " + userExp);
                        user_Token.setText(user_Token.getText() + ": " + userToken);


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

    private void earnExp() {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        String url = getString(R.string.url) + "/tree/breeding";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    /*

                    treeName = (String) response.get("treeName");
                    treeExp = (String)response.get("treeExp");
                    treeLevel = (String)response.get("treeLevel");
                    maxExp = (String)response.get("maxExp");
                    maxLevel = (String)response.get("maxLevel");*/
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