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
import android.os.Parcelable;
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

import java.io.ByteArrayOutputStream;
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
    private boolean isChanged = false;

    private String storyTitleString = "Chapter 1 - 스토리 챕터 제목 or 번호를 추가하세요";
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
        setSidebarUserImage();


        Intent intent = getIntent();
        isChanged = intent.getBooleanExtra("change", false);
        if(isChanged) {
            storyTitleString = "매실아이와 함께 하는 도전";
            storyString1 = "매실아이는 점점 깨끗한 하늘이 매연으로 뒤덮이는 과거의 세상을 봅니다.\n 매실아이는 과거가 자신이 있는 미래와 점점 닮아가는 것이 마음이 아픕니다. 매실아이는 다시 빛을 꼭 쥐고, 소원을 하나 빕니다.";
            storyString2 = "(1) 가까운 거리를 자전거로 이동하는 사진을 찍어 인증 게시판에 올린다. \n(2) 자가용이 아닌 대중교통을 이용하는 자신의 모습을 인증하여 인증 게시판에 올린다.";
            storyTitle.setText(storyTitleString);
            storyImage.setImageResource(R.drawable.happyimage);
        } else {
            this.getStoryContents();
        }


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
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aricond);
                String imageFile = bitmapToString(bitmap);

                intent.putExtra("questName", "에어컨 적정온도 지키기");
                intent.putExtra("questExplanation", "에어컨의 사용은 지구온난화를 악화시킵니다. 에어컨의 온도를 낮게 설정하는 것 보다 에어컨의 온도를 적정온도로 사용하는 것이 환경에 도움이 될 것입니다.");
                intent.putExtra("questCondition", "실내 에어컨 적정온도인 24~26도로 에어컨을 사용하는 사진을 찍어서 올립니다. 온도가 표시되는 에어컨의 표면이나, 에어컨의 리모컨의 사용사진을 올려서 인증을 받습니다.");
                intent.putExtra("questImage", imageFile);
                intent.putExtra("questNumber", "story");
                intent.putExtra("questPoint", "20");
                intent.putExtra("questComplete", false);
                finish();
                startActivity(intent);

            }
        });
        missionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestLoadActivity.class);

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aricond);
                String imageFile = bitmapToString(bitmap);

                intent.putExtra("questName", "에어컨 대신 선풍기 사용하기");
                intent.putExtra("questExplanation", "지구온난화를 막기 위해서, 전력소모량이 큰 에어컨을 사용하는 것 보다 선풍기를 사용하는 것이 많은 도움이 됩니다.");
                intent.putExtra("questCondition", "선풍기를 사용하여 무더운 여름을 시원하게 보내는 자신의 생활 모습을 찍어서 인증합니다.");
                intent.putExtra("questImage", imageFile);
                intent.putExtra("questNumber", "story");
                intent.putExtra("questPoint", "20");
                intent.putExtra("questComplete", false);
                finish();
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

    public String bitmapToString(Bitmap bitmap){ /** Bitmap을 String 으로 변환 */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.getEncoder().encodeToString(imageBytes);
        return imageString;
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
            shopbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, ShopActivity.class);
                    //finish();
                    startActivity(intent);
                }
            });
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

        /**받아온 스토리 내용 정보는 다음 변수에 저장 */
        storyTitleString = "매실아이의 시작";
        storyString1 = "매실아이는 또 그들을 바꾸고 싶어 합니다.";
        storyString2 = "(1) 에어컨 온도를 적정온도로 사용하는 사진을 인증하여 인증게시판에 올린다. \n(2) 에어컨이 아닌 선풍기를 사용하는 사진을 인증하여 인증게시판에 올린다.";
        storyTitle.setText(storyTitleString);

        // 여기는 스토리 이미지를 불러와서 저장
        storyImage.setImageResource(R.drawable.little_leaf);
    }
}