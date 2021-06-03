package com.example.maesilnamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sun.mail.imap.IMAPBodyPart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QuestListContentActivity extends AppCompatActivity {
    private String postId;
    private QuestPost post;
    private TextView questName, nickName, postTitle, postContent, postIsAuth;
    private ImageView userImage, authButton, firstAuthImg, secondAuthImg, thirdAuthImg;
    private RecyclerView pictureRecyclerView;
    private ArrayList<String> postPictures;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ContentImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list_content_acitivity);

        Intent intent = getIntent();
        post = (QuestPost) intent.getSerializableExtra("post");

        questName = (TextView) findViewById(R.id.content_questName);
        nickName = (TextView) findViewById(R.id.content_userName);
        postTitle = (TextView) findViewById(R.id.content_postTitle);
        postContent = (TextView) findViewById(R.id.content_postContent);
        postIsAuth = (TextView) findViewById(R.id.post_content_isAuth);
        pictureRecyclerView = (RecyclerView) findViewById(R.id.content_recyclerviewContent);
        userImage = (ImageView) findViewById(R.id.content_userImage);
        authButton = (ImageView) findViewById(R.id.auth_button);
        firstAuthImg = (ImageView) findViewById(R.id.first_auth_image);
        secondAuthImg = (ImageView) findViewById(R.id.second_auth_image);
        thirdAuthImg = (ImageView) findViewById(R.id.third_auth_image);


        postId = post.getPostingId();
        setQuestContent();
        getPostPhotos(postId);
        initAdapters();
        initScrollListener();


        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authQuest(post);
            }
        });
    }

    private void initAdapters(){
        pictureRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        adapter = new ContentImageAdapter(bitmaps);
        pictureRecyclerView.setAdapter(adapter);

        adapter.setPictureItemClickListener(new OnPictureClickListener() {
            @Override
            public void OnItemClick(ContentImageAdapter.ContentImageViewHolder holder, View view, int position) {
                Bitmap bitmap = adapter.getItem(position);
                String bitmapString = bitmapToString(bitmap);
                Intent intent = new Intent(QuestListContentActivity.this, BigPictureActivity.class);
                intent.putExtra("picture", bitmapString);
                startActivity(intent);
            }
        });
    }

    public String bitmapToString(Bitmap bitmap){ /** Bitmap을 String 으로 변환 */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.getEncoder().encodeToString(imageBytes);
        return imageString;
    }

    private void initScrollListener() {
        pictureRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void getPostPhotos(String postId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.url) + "/auth-posting/pictures/" + postId;
        Log.i("content url", url);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray pictures = response.getJSONArray("pictures");
                            Log.i("picturesArray", pictures.toString());
                            int num = response.getInt("num");
                            String image;
                            for(int i = 0; i<num; i++) {
                                JSONObject object = pictures.getJSONObject(i);
                                image = object.getString("image");
                                Log.i("image", image);
                                bitmaps.add(StringToBitmap(image));
                                adapter.notifyItemInserted(bitmaps.size() - 1);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
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

    private void setQuestContent(){  /** 퀘스트 내용 set */
        questName.setText(post.getQuestName());
        nickName.setText(post.getUserName());
        postTitle.setText(post.getPostTitle());
        postContent.setText(post.getPostContent());
        setAuthImages(post.getAuthNum());
        setUserImage();
    }

    private void setUserImage() {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.url) + "/auth-posting/userProfile/" + postId;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String userImageString = response.getString("profile");
                            Bitmap bitmap = StringToBitmap(userImageString);
                            userImage.setImageBitmap(bitmap);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void setAuthImages(int num){ /** 인증여부에 따라 인증 이미지로 바꿔주기*/
        if(num >= 3) {
            postIsAuth.setText("인증 완료");
            postIsAuth.setTextColor(Color.parseColor("#4D4D4D"));
            firstAuthImg.setImageResource(R.drawable.authcheck);
            secondAuthImg.setImageResource(R.drawable.authcheck);
            thirdAuthImg.setImageResource(R.drawable.authcheck);
        }
        else {
            if(num == 1) {
                firstAuthImg.setImageResource(R.drawable.authcheck);
            }
            else if(num == 2) {
                firstAuthImg.setImageResource(R.drawable.authcheck);
                secondAuthImg.setImageResource(R.drawable.authcheck);
            }
            postIsAuth.setText("인증 필요");
            postIsAuth.setTextColor(Color.parseColor("#EC2424"));
        }
    }

    private Bitmap StringToBitmap(String encodedString){  /** 서버에서 받아온 이미지 비트맵으로 복구 */
        try {
            byte[] encodeByte = Base64.getDecoder().decode(encodedString);
            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return decodeBitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void getQuestImage(){ // 퀘스트 인증 글 이미지 가져오기
        RequestQueue queue = Volley.newRequestQueue(this);
        String contentUrl = getString(R.string.url) + "/";
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        //RequestQueue queue = Volley.newRequestQueue(QuestListContentActivity.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, contentUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    private void authQuest(QuestPost post) {  /** 퀘스트 인증 버튼 눌렀을 경우 인증 진행사항 서버 통신 */
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/auth-posting/validation/" + post.getWriterCode() + "/" + post.getPostingId();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serverResponse = response.get("response").toString();
                            if(serverResponse.equals("처리되었습니다")) {
                                setAuthImages(post.getAuthNum() + 1);
                            }
                            Snackbar.make(findViewById(R.id.questContentLayout), serverResponse, Snackbar.LENGTH_SHORT).show();

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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