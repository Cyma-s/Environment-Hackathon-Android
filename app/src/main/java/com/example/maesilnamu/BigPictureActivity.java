package com.example.maesilnamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Base64;

public class BigPictureActivity extends AppCompatActivity {
    private ImageView bigImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);

        bigImage = (ImageView) findViewById(R.id.bigPicture);

        Intent intent = getIntent();
        String bitmapString = intent.getStringExtra("picture");
        Bitmap bitmap = StringToBitmap(bitmapString);
        bigImage.setImageBitmap(bitmap);
    }

    private Bitmap StringToBitmap(String encodedString){  /** 이미지 비트맵으로 복구 */
        try {
            byte[] encodeByte = Base64.getDecoder().decode(encodedString);
            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return decodeBitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}