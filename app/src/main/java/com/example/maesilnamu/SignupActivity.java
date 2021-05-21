package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private Button nextButton;
    private ImageView backButton, emailSendButton, codeVerificationButton;
    private EditText emailText, emailConfigureText, passwordText, passwordConfigureText;
    private TextView emailError, codeVerificationConfigure, passwordError, passwordErrorConfigure;

    private boolean checkEmail = false, checkPassword = false, checkCode = false;
    private String address, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        nextButton = (Button) findViewById(R.id.nextbutton);
        backButton = (ImageView) findViewById(R.id.back_button);
        emailSendButton = (ImageView) findViewById(R.id.signup_email_validation_button);
        codeVerificationButton = (ImageView) findViewById(R.id.signup_code_verification_button);
        emailText = (EditText) findViewById(R.id.signup_email);
        emailConfigureText = (EditText) findViewById(R.id.signup_code_verification);
        passwordText = (EditText) findViewById(R.id.signup_password);
        passwordConfigureText = (EditText) findViewById(R.id.signup_passwordconfigure);
        codeVerificationConfigure = (TextView) findViewById(R.id.code_validation);
        passwordError = (TextView) findViewById((R.id.error_password));
        passwordErrorConfigure = (TextView) findViewById((R.id.error_password_configure));


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAuthentic = address;
                String password = passwordText.getText().toString();
                String passwordConfigure = passwordConfigureText.getText().toString();

                String url = getString(R.string.url)+"/user/validation/email/"+address;

                System.out.println(url);

                if(checkCode && checkPassword && password.equals(passwordConfigure)) {
                    RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                    final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>(){
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("false")) {
                                        Toast.makeText(SignupActivity.this, "이미 사용중인 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Intent next_intent = new Intent(SignupActivity.this, SignUpCreateProfile.class);
                                        next_intent.putExtra("emailText", emailAuthentic);
                                        next_intent.putExtra("password", password);
                                        startActivity(next_intent);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignupActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(stringRequest);
                }
                else {
                    Toast.makeText(SignupActivity.this, "아이디, 비밀번호를 다시 확인해주세요 :)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(SignupActivity.this, MainActivity.class);
                finish();
                startActivity(back_intent);
            }
        });

        emailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = emailText.getText().toString();

                if(checkEmail) {
                    emailConfigureText.setVisibility(View.VISIBLE);
                    codeVerificationButton.setVisibility(View.VISIBLE);

                    SendMail mailServer = new SendMail();
                    mailServer.sendSecurityCode(getApplicationContext(), address);
                    code = mailServer.getCode();
                }
                else {
                    Toast.makeText(SignupActivity.this, "이메일 형식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        codeVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeInput = emailConfigureText.getText().toString();

                if(codeInput.equals(code)) {
                    codeVerificationConfigure.setVisibility(View.VISIBLE);
                    checkCode = true;
                }
            }
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    checkEmail = true;
                }
            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordText.getText().toString();

                if((password.length() < 7) || !(Pattern.matches("^[a-zA-Z0-9]*$", password))) {
                    passwordError.setText("비밀번호는 영문, 숫자 포함 7자리 이상입니다.");
                    passwordText.setBackgroundResource(R.drawable.redborder);
                }
                else {
                    passwordError.setText("");
                    passwordText.setBackgroundResource(R.drawable.whiteborder);
                    checkPassword = true;
                }
            }
        });

        passwordConfigureText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordText.getText().toString();
                String passwordConfigure = passwordConfigureText.getText().toString();

                if(!(password.equals(passwordConfigure))) {
                    passwordErrorConfigure.setText("비밀번호가 같지 않습니다.");
                    passwordConfigureText.setBackgroundResource(R.drawable.redborder);
                }
                else {
                    passwordErrorConfigure.setText("");
                    passwordConfigureText.setBackgroundResource(R.drawable.whiteborder);
                }
            }
        });
    }
}