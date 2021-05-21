package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpCreateProfile extends AppCompatActivity {
    private Button signupButton;
    private ImageView backButton, validityCheckButton;
    private EditText nicknameText, locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_create_profile);

        signupButton = (Button) findViewById(R.id.signup_button);
        backButton = (ImageView) findViewById(R.id.back_button);
        validityCheckButton = (ImageView) findViewById(R.id.signup_validity_check_button);
        nicknameText = (EditText) findViewById(R.id.signup_nickname);
        locationText = (EditText) findViewById(R.id.signup_location);

        Intent prev_intent = getIntent();
        String emailText = prev_intent.getExtras().getString("emailText");
        String passwordText = prev_intent.getExtras().getString("password");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameText.getText().toString();
                String location = locationText.getText().toString();

                JSONObject infoForSignup = new JSONObject();
                try {
                    infoForSignup.put("eMail", emailText);
                    infoForSignup.put("password", passwordText);
                    infoForSignup.put("name", nickname);
                    infoForSignup.put("location", location);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }

                String url = getString(R.string.url) + "/user/signUp";
                RequestQueue queue = Volley.newRequestQueue(SignUpCreateProfile.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, infoForSignup, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            saveToken(response);
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                        signUp();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpCreateProfile.this, "회원가입이 정상적으로 되지 않았습니다. 입력하신 사용자 정보를 다시 확인해주세요 :)", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsonObjectRequest);
            }

            private void signUp() {

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

            }
        });
    }
}