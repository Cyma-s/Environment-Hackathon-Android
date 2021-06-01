package com.example.maesilnamu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QuestPostWriteActivity extends AppCompatActivity {
    private EditText postWriteTitle, postWriteContent;
    private ImageView postWriteButton, addImageButton;
    private RecyclerView recyclerView;
    private final int CODE_ALBUM_REQUEST = 111;
    private Bitmap bitmap;
    private ArrayList<String> writePictures;
    private int photoProgressInt = 0;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_post_write);
        postWriteButton = (ImageView) findViewById(R.id.postWriteButton);
        postWriteTitle = (EditText) findViewById(R.id.postWriteTitle);
        postWriteContent = (EditText) findViewById(R.id.postWriteContent);
        addImageButton = (ImageView) findViewById(R.id.addImageButton);
        recyclerView = findViewById(R.id.imageRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        postWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String writeTitle, writeContent, questName, picture;
                writeTitle = postWriteTitle.getText().toString();
                writeContent = postWriteContent.getText().toString();
                questName = "뭐지";
                try {
                    sendToServer(questName, writeTitle, writeContent);
                    Intent intent = new Intent(QuestPostWriteActivity.this, QuestCommunityActivity.class);
                    finish();
                    startActivity(intent);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writePictures = new ArrayList<>();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, CODE_ALBUM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String bitmapString = "";
        if(requestCode == CODE_ALBUM_REQUEST && resultCode == RESULT_OK && data != null){
            ArrayList<Uri> uriList = new ArrayList<>();
            if(data.getClipData() != null){
                ClipData clipData = data.getClipData();
                if(clipData.getItemCount() > 3) {
                    Toast.makeText(QuestPostWriteActivity.this, "사진은 3개까지 선택가능합니다",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if(clipData.getItemCount() == 1){
                    Uri filePath = clipData.getItemAt(0).getUri();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        bitmapString = bitmapToString(bitmap);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    //String imgPath = getRealPathFromUri(filePath);
                    writePictures.add(bitmapString);
                    uriList.add(filePath);
                } else if(clipData.getItemCount() > 1 && clipData.getItemCount() <= 3){
                    for(int i = 0; i<clipData.getItemCount(); i++){
                        String imgPath = getRealPathFromUri(clipData.getItemAt(i).getUri());
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), clipData.getItemAt(i).getUri());
                            bitmapString = bitmapToString(bitmap);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        writePictures.add(bitmapString);
                        uriList.add(clipData.getItemAt(i).getUri());
                    }
                }
            }
            UriImageAdapter adapter = new UriImageAdapter(uriList, QuestPostWriteActivity.this);
            recyclerView.setAdapter(adapter);
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

    private void sendToServer(String questName, String title, String content) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/auth-posting";
        RequestQueue queue = Volley.newRequestQueue(QuestPostWriteActivity.this);

        JSONObject questPost = new JSONObject();
        questPost.put("questName", questName);
        questPost.put("postTitle", title);
        questPost.put("postContent", content);

        /*
        JSONArray questPostPicture = new JSONArray();
        for(int i = 0; i<writePictures.size(); i++){
            questPostPicture.put(writePictures.get(i));
        }
        for(int i = writePictures.size(); i<3; i++){
            questPostPicture.put("");
        }

        questPost.put("picture", questPostPicture);
           */
        // 일단 글만 서버로 보냄
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, questPost,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            userId = response.get("id").toString(); // 글 보내고 서버에서 id 받아온다
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(QuestPostWriteActivity.this, "내부 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<writePictures.size(); i++){ // 서버로 입력받은 사진 개수만큼 보내기
                    try {
                        sendImageToServer(photoProgressInt);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, 1000);


    }

    private void sendImageToServer(int i) throws JSONException {  // 서버로 입력받은 사진 개수만큼 보내기
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("postingId", userId);
        jsonObject.put("image", writePictures.get(i));
        Log.i("image", writePictures.get(i));
        photoProgressInt++;

        String url = getString(R.string.url) + "/auth-posting/image"; // 사진 보내는 api

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(QuestPostWriteActivity.this, "내부 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }
}