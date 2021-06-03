package com.example.maesilnamu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SignUpCreateProfile extends AppCompatActivity {
    private Button signupButton;
    private ImageView backButton, validityCheckButton, signup_user_image;
    private TextView nicknameConfigure;
    private EditText nicknameText, locationText;
    private Spinner locationSpin;
    private String nickname;
    private final int CODE_ALBUM_REQUEST = 111;
    private Bitmap bitmap;
    private String bitmapString = "";
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
        signup_user_image = (ImageView) findViewById(R.id.signup_user_image);


        Intent prev_intent = getIntent();
        String emailText = prev_intent.getExtras().getString("emailText");
        String passwordText = prev_intent.getExtras().getString("password");

        locationText.setBackgroundResource(R.drawable.blackborder);
        locationSpin.setBackgroundResource(R.drawable.blackborder);

        signup_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, CODE_ALBUM_REQUEST);
            }
        });



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer(emailText, passwordText, bitmapString);
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

                //System.out.println(url);

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
                        //Toast.makeText(SignUpCreateProfile.this, "오류입니다.", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_ALBUM_REQUEST && resultCode == RESULT_OK && data != null){
            if(data.getClipData() != null){
                ClipData clipData = data.getClipData();
                Uri filePath = clipData.getItemAt(0).getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    bitmapString = bitmapToString(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                signup_user_image.setImageBitmap(bitmap);
            }
        }
    }

    String getRealPathFromUri(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.getEncoder().encodeToString(imageBytes);
        return imageString;
    }

    private void sendToServer(String emailText, String passwordText, String bitmapString){
        String nicknameAuthentic = nickname;
        String location = locationText.getText().toString();

        if(checkNickname && checkLocation) {
            JSONObject infoForSignup = new JSONObject();
            try {
                infoForSignup.put("eMail", emailText);
                infoForSignup.put("password", passwordText);
                infoForSignup.put("name", nicknameAuthentic);
                infoForSignup.put("image", bitmapString);
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
                    Toast.makeText(SignUpCreateProfile.this, "오류입니다", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonObjectRequest);
            Intent intent = new Intent(SignUpCreateProfile.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}