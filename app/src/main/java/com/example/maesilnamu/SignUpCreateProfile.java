package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameText.getText().toString();
                String location = locationText.getText().toString();
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
    }
}