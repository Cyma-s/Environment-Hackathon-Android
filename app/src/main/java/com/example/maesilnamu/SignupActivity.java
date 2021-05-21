package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    private Button nextButton;
    private ImageView backButton;
    private EditText emailText, passwordText, passwordConfigureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nextButton = (Button) findViewById(R.id.nextbutton);
        backButton = (ImageView) findViewById(R.id.back_button);
        emailText = (EditText) findViewById(R.id.signup_email);
        passwordText = (EditText) findViewById(R.id.signup_password);
        passwordConfigureText = (EditText) findViewById(R.id.signup_passwordconfigure);

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
    }
}