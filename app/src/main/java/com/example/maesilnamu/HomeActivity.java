package com.example.maesilnamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private Button missionConnect, writeConnect;
    private ImageView sidebar;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private ImageButton whiteground;
    private TextView mypagebutton, rankbutton, shopbutton, postbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //getBreedingInfo();


        // exp Up
        Button expUp = (Button) findViewById(R.id.expUp);
        expUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.sidebar_open);
        sidebar = (ImageView)findViewById(R.id.sidebar);
        whiteground = (ImageButton)findViewById(R.id.backgroundwhite);
        mypagebutton = (TextView)findViewById(R.id.btn_mypage);
        rankbutton = (TextView)findViewById(R.id.btn_rank);
        shopbutton = (TextView)findViewById(R.id.btn_shop);
        postbutton = (TextView)findViewById(R.id.btn_post);

        missionConnect = (Button) findViewById(R.id.mission_connect);
        writeConnect = (Button) findViewById(R.id.mission_write);
        missionConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MypageActivity.class);
                //finish();
                startActivity(intent);
            }
        });
        writeConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuestPostWriteActivity.class);
                //finish();
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
            rankbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, RankingActivity.class);
                    //finish();
                    startActivity(intent);
                }
            });
            shopbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    finish();
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

}