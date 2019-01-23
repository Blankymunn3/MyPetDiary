package io.kong.mypetdiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.kakao.auth.Session;

import io.kong.mypetdiary.R;

public class SplashActivity extends Activity {

    public static SharedPreferences appData;

    String stUserID, stUserPW, stUserName, stUserBirth, stUserArea, stUserProfile, stPetName, stPetBirth, stPetCome, stPetKind;

    boolean saveLoginData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView gif = findViewById(R.id.img_splash);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(gif);
        Glide.with(this).load(R.drawable.giphy).into(gifImage);

        Handler handler = new Handler();
        handler.postDelayed(new splashHandler(), 3000);

    }

    private class splashHandler implements Runnable {
        public void run() {
            init();
            load();

            if (Session.getCurrentSession().checkAndImplicitOpen() || saveLoginData){
                final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void load() {


        stUserID = appData.getString("user_id", "");
        stUserPW = appData.getString("user_pw", "");
        stUserName = appData.getString("user_name", "");
        stUserProfile = appData.getString("user_profile", "");
        stUserArea = appData.getString("user_area","");
        stUserBirth = appData.getString("user_birth", "");

        stPetName = appData.getString("pet_name", "");
        stPetBirth = appData.getString("pet_birth", "");
        stPetCome = appData.getString("pet_come", "");
        stPetKind = appData.getString("pet_kind", "");

    }

    private void init() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
    }
}
