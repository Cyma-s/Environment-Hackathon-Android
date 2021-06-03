package com.example.maesilnamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    private Button missionConnect, writeConnect;
    private Button missionButton1, missionButton2, missionButton3;
    private ImageView sidebar, storyImage, sidebarUserImage;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private ImageButton whiteground;
    private TextView mypagebutton, rankbutton, shopbutton, postbutton, sidebarName;
    private TextView storyTitle, storyContent1, storyContent2;

    private String storyTitleString = "Chapter 1 - 스토리 챕터 제목 or 번호를 추가하세요;";
    private String storyString1 = "여기에는 스토리 내용을 추가해줘요";
    private String storyString2 = "여기에는 스토리 내용을 더 넣거나 넣을 내용이 없으면 컴포넌트를 제거해줘요";
    private String tempTitleString = "";
    private String tempString1 = "";
    private String tempString2 = "";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.sidebar_open);
        sidebar = (ImageView)findViewById(R.id.sidebar);
        storyImage = (ImageView) findViewById(R.id.story_image);
        whiteground = (ImageButton)findViewById(R.id.backgroundwhite);
        mypagebutton = (TextView)findViewById(R.id.btn_mypage);
        rankbutton = (TextView)findViewById(R.id.btn_rank);
        shopbutton = (TextView)findViewById(R.id.btn_shop);
        postbutton = (TextView)findViewById(R.id.btn_post);
        storyTitle = (TextView) findViewById(R.id.story_title);
        storyContent1 = (TextView) findViewById(R.id.story_content_1);
        storyContent2 = (TextView) findViewById(R.id.story_content_2);
        missionButton1 = (Button) findViewById(R.id.mission1);
        missionButton2 = (Button) findViewById(R.id.mission2);
        missionButton3 = (Button) findViewById(R.id.mission3);
        sidebarUserImage = (ImageView) findViewById(R.id.sidebar_profile);
        sidebarName = (TextView) findViewById(R.id.sidebar_userName);
        this.getStoryContents();
        setSidebarUserImage();

        /** 여기서부터는 애니메이션으로 한 글자씩 스토리 내용 출력 */
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count >= storyString1.length() + storyString2.length())
                            return;
                        else if (count < storyString1.length()){
                            tempString1 += storyString1.charAt(count);
                            storyContent1.setText(tempString1);
                            count++;
                        }
                        else {
                            tempString2 += storyString2.charAt(count - storyString1.length());
                            storyContent2.setText(tempString2);
                            count++;
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 100);


        missionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestLoadActivity.class);

                startActivity(intent);
            }
        });
        missionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestLoadActivity.class);

                startActivity(intent);
            }
        });
        missionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestLoadActivity.class);

                startActivity(intent);
            }
        });

        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        drawerLayout.setDrawerListener(listener);
    }

    private void setSidebarUserImage(){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.url) + "/user/userProfile";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String userImageString = response.getString("profile");
                            String userName = response.getString("name");
                            Bitmap bitmap = StringToBitmap(userImageString);
                            sidebarUserImage.setImageBitmap(bitmap);
                            sidebarName.setText(userName);
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

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull @NotNull View drawerView) {
            whiteground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawers();
                }
            });
            mypagebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, MypageActivity.class);
                    //finish();
                    startActivity(intent);
                }
            });
            /** 상점 기능 구현 완료 후 연결 */ /*
            shopbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, ShopActivity.class);
                    //finish();
                    startActivity(intent);
                }
            }); */
            rankbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, RankingActivity.class);
                    //finish();
                    startActivity(intent);
                }
            });
            postbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, QuestCommunityActivity.class);
                    //finish();
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onDrawerClosed(@NonNull @NotNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    private void getStoryContents() {
        String url = getString(R.string.url)+"스토리 정보 받는 이메일 주소";

        System.out.println(url);

        /**받아온 스토리 내용 정보는 다음 변수에 저장 */
        storyTitleString = "스토리 챕터 번호 or 챕터 제목";
        storyString1 = "스토리 내용 텍스트 1";
        storyString2 = "스토리 내용 텍스트 2";
        storyTitle.setText(storyTitleString);

        // 여기는 스토리 이미지를 불러와서 저장
        storyImage.setImageResource(R.drawable.little_leaf);
    }
}