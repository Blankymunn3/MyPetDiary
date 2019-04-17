package io.kong.mypetdiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.PetItem;
import io.kong.mypetdiary.item.UserItem;

public class SplashActivity extends Activity {

    public SharedPreferences appData;
    boolean saveLoginData;

    private UserItem userItem;
    private PetItem petItem;

    String stUserID, stUserPW, stUserName, stUserBirth, stUserArea, stUserProfile, stPetName, stPetBirth, stPetCome;
    int petKind;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);

        init();
        load();

        if (saveLoginData){
            final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }


    public void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);

        stUserID = appData.getString("user_id", "");
        stUserPW = appData.getString("user_pw", "");
        stUserName = appData.getString("user_name", "");
        stUserProfile = appData.getString("user_profile", "");
        stUserArea = appData.getString("user_area","");
        stUserBirth = appData.getString("user_birth", "");

        stPetName = appData.getString("pet_name", "");
        stPetBirth = appData.getString("pet_birth", "");
        stPetCome = appData.getString("pet_come", "");
        petKind = appData.getInt("pet_kind", 0);


        userItem.setStUserID(stUserID);
        userItem.setStUserPW(stUserPW);
        userItem.setStUserName(stUserName);
        userItem.setStUserProfile(stUserProfile);
        userItem.setStUserArea(stUserArea);
        userItem.setStUserBirth(stUserBirth);

        petItem.setStPetName(stPetName);
        petItem.setStPetBirth(stPetBirth);
        petItem.setStPetCome(stPetCome);
        petItem.setStPetKind(petKind);
    }

    private void init() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        userItem = new UserItem();
        petItem = new PetItem();

    }
}
