package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private ImageView backButton;
    private EditText loginText, passwordText;
    private int[] locationPmGrade;
    private int[] locationNum;
    private int cnt = 1;
    private String[] location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginbutton);
        backButton = (ImageView) findViewById(R.id.back_button);
        loginText = (EditText) findViewById(R.id.login_email);
        passwordText = (EditText) findViewById(R.id.login_password);
        location  = getResources().getStringArray(R.array.location_short);
        locationNum = new int[18];
        for(int i = 0; i<18; i++){
            locationNum[i] = 1;
        }
        locationPmGrade = new int[18];
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginText.getText().toString();
                String password = passwordText.getText().toString();

                JSONObject infoForLogin = new JSONObject();
                try{
                    infoForLogin.put("eMail", email);
                    infoForLogin.put("password", password);
                } catch (JSONException exception){
                    exception.printStackTrace();
                }

                String url = getString(R.string.url) + "/auth/login";
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, infoForLogin, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            saveToken(response);
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                        logIn();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "등록되지 않은 아이디/비밀번호입니다. 다시 확인해주세요 :)", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsonObjectRequest);

                for(int i = 0; i<20; i++){
                    getMicroDust();
                }
                sendToServerArray();
            }

            private void saveToken(JSONObject response) throws JSONException {
                SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Authorization", response.get("access_token").toString());
                editor.apply();
            }

            private void logIn(){
                SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                String token = sharedPreferences.getString("Authorization", "");
                String url = getString(R.string.url) + "/profile";
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "오류입니다", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError{
                        Map<String, String> heads = new HashMap<String, String>();
                        heads.put("Authorization", "Bearer " + token);
                        return heads;
                    }
                };
                queue.add(jsonObjectRequest);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(back_intent);
            }
        });
    }

    private void getMicroDust() {
        String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=ZaEuGtM8LYExIc%2FxBYwBYjrB%2BB4Lmetl1CRgp%2FPrJGfJRYGQec%2Fr2mqMRAaDuoRUuolev3%2BO%2FmLtvl34LS%2Be2A%3D%3D&returnType=json&sidoName=전국&pageNo=" + cnt;
        Log.i("dataUrl", url);
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject body = response.getJSONObject("response");
                            JSONArray array = body.getJSONArray("items");
                            for (int i = 0; i<array.length(); i++) {
                                for(int j = 0; j<17; j++){
                                    JSONObject object = array.getJSONObject(i);
                                    if(object.get("sidoName").equals(location[j])){
                                        locationPmGrade[i] += Integer.parseInt(object.get("pm10Grade").toString());
                                        locationNum[i]++;
                                    }
                                }
                            }
                            cnt++;
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }

    private void sendToServerArray() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.url) + "";
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i<17 ;i++){
            JSONObject object = new JSONObject();
            try {
                object.put("sido", location[i]);
                object.put("avg", locationPmGrade[i] / locationNum[i]);
                jsonArray.put(object);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Log.i("array", jsonArray.toString());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}