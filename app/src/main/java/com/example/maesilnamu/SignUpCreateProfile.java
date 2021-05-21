package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpCreateProfile extends AppCompatActivity {
    private Button signupButton;
    private ImageView backButton, validityCheckButton;
    private TextView nicknameConfigure;
    private EditText nicknameText, locationText;
    private Spinner locationSpin;
    private String nickname;

    private boolean checkNickname = false, checkLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_create_profile);

        signupButton = (Button) findViewById(R.id.signup_button);
        backButton = (ImageView) findViewById(R.id.back_button);
        validityCheckButton = (ImageView) findViewById(R.id.signup_validity_check_button);
        nicknameConfigure = (TextView) findViewById(R.id.nickname_validation);
        nicknameText = (EditText) findViewById(R.id.signup_nickname);
        locationText = (EditText) findViewById(R.id.signup_location);
        locationSpin = (Spinner) findViewById(R.id.location_spinner);


        Intent prev_intent = getIntent();
        String emailText = prev_intent.getExtras().getString("emailText");
        String passwordText = prev_intent.getExtras().getString("password");

        locationText.setBackgroundResource(R.drawable.blackborder);
        locationSpin.setBackgroundResource(R.drawable.blackborder);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nicknameAuthentic = nickname;
                String location = locationText.getText().toString();

                System.out.println("checkNickname: " + checkNickname);
                System.out.println("checkLocation: " + checkLocation);
                System.out.println("location: " + location);
                System.out.println("nickname: " + nicknameAuthentic);
                System.out.println("email: " + emailText);
                System.out.println("password: " + passwordText);

                if(checkNickname && checkLocation) {
                    JSONObject infoForSignup = new JSONObject();
                    try {
                        infoForSignup.put("eMail", emailText);
                        infoForSignup.put("password", passwordText);
                        infoForSignup.put("name", nicknameAuthentic);
                        infoForSignup.put("image", "");
                        infoForSignup.put("location", location);
                        infoForSignup.put("exp", "0");
                        infoForSignup.put("token", "50");
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }

                    String url = getString(R.string.url) + "/user/signUp";
                    RequestQueue queue = Volley.newRequestQueue(SignUpCreateProfile.this);
                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, infoForSignup,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
    //                        Toast.makeText(SignUpCreateProfile.this, "오류입니다", Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(jsonObjectRequest);
                    Intent intent = new Intent(SignUpCreateProfile.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(SignUpCreateProfile.this, SignupActivity.class);
                finish();
                startActivity(back_intent);
            }
        });

        validityCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = nicknameText.getText().toString();

                String url = getString(R.string.url) + "/user/validation/email/" + nickname;

                System.out.println(url);

                RequestQueue queue = Volley.newRequestQueue(SignUpCreateProfile.this);
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("false")) {
                                    Toast.makeText(SignUpCreateProfile.this, "이미 사용중인 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                    nicknameConfigure.setVisibility(View.VISIBLE);
                                    nicknameConfigure.setText("인증번호를 다시 확인하세요.");
                                } else {
                                    nicknameConfigure.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpCreateProfile.this, "오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(stringRequest);
                checkNickname = true;
            }
        });

        locationSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationText.setText(parent.getItemAtPosition(position).toString());
                checkLocation = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                locationText.setText("지역을 선택하세요.");
            }
        });
    }
}