package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private Button nextButton;
    private ImageView backButton;
    private EditText emailText, passwordText, passwordConfigureText;
    private TextView emailError, passwordError, passwordErrorConfigure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nextButton = (Button) findViewById(R.id.nextbutton);
        backButton = (ImageView) findViewById(R.id.back_button);
        emailText = (EditText) findViewById(R.id.signup_email);
        passwordText = (EditText) findViewById(R.id.signup_password);
        passwordConfigureText = (EditText) findViewById(R.id.signup_passwordconfigure);
        emailError = (TextView) findViewById((R.id.error_email));
        passwordError = (TextView) findViewById((R.id.error_password));
        passwordErrorConfigure = (TextView) findViewById((R.id.error_password_configure));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String passwordConfigure = passwordConfigureText.getText().toString();

                if(password.equals(passwordConfigure)) {
                    Intent next_intent = new Intent(SignupActivity.this, SignUpCreateProfile.class);
                    next_intent.putExtra("emailText", email);
                    next_intent.putExtra("password", password);
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
                    emailError.setText("이메일 형식으로 입력해주세요");
                    emailText.setBackgroundResource(R.drawable.redborder);
                }
                else {
                    emailError.setText("");
                    emailText.setBackgroundResource(R.drawable.whiteborder);
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
                String password = emailText.getText().toString();

                if(!(password.length() < 7) && !(Pattern.matches("[a-z0-9]]", password))) {
                    emailError.setText("비밀번호는 영문, 숫자 포함 7자리 이상입니다.");
                    emailText.setBackgroundResource(R.drawable.redborder);
                }
                else {
                    emailError.setText("");
                    emailText.setBackgroundResource(R.drawable.whiteborder);
                }
            }
        });

        passwordConfigureText.addTextChangedListener(new TextWatcher() {
            String password = emailText.getText().toString();
            String passwordConfigure = emailText.getText().toString();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(password.equals(passwordConfigure))) {
                    emailError.setText("비밀번호가 같지 않습니다.");
                    emailText.setBackgroundResource(R.drawable.redborder);
                }
                else {
                    emailError.setText("");
                    emailText.setBackgroundResource(R.drawable.whiteborder);
                }
            }
        });
    }
}