package io.kong.mypetdiary.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.kong.mypetdiary.item.KakaoUserItem;
import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.PetItem;
import io.kong.mypetdiary.item.SaveUserInfo;
import io.kong.mypetdiary.item.UserItem;
import io.kong.mypetdiary.service.RetrofitService;
import io.kong.mypetdiary.service.SHA256Util;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kakao.auth.Session.getCurrentSession;

public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences appData;

    private KakaoUserItem kakaoUserItem;
    private UserItem userItem;
    private PetItem petItem;

    Retrofit retrofit;
    RetrofitService retrofitService;

    private SessionCallback callback;

    Button btnSignup;
    Button btnLogin;
    EditText edLoginID;
    EditText edLoginPW;

    String stUserID, stUserPW, newPassword, stUserSalt, stUserName, stUserArea, stUserBirth, stUserProfile, stPetName, stPetBirth, stPetCome, stPetKind;
    int kakao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);
        init();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.putExtra("kakao", 0);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stUserID = edLoginID.getText().toString();
                stUserPW = edLoginPW.getText().toString();

                Call<ResponseBody> saltCall = retrofitService.saltLogin(stUserID);
                saltCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray("salt_table");
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i< jsonArray.length(); i++) {
                                            JSONObject item = jsonArray.getJSONObject(i);
                                            stUserSalt = item.getString("user_salt");
                                            newPassword = SHA256Util.getEncrypt(stUserPW, stUserSalt);
                                        }
                                        Call<ResponseBody> lgoinCall = retrofitService.login(stUserID, newPassword);
                                        lgoinCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()) {
                                                    try {
                                                        String result = response.body().string();
                                                        saveUserInfo(result);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    Toast.makeText(LoginActivity.this, "아이디 및 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

    }

    private void init() {
        setContentView(R.layout.activity_login);

        kakaoUserItem = new KakaoUserItem();
        userItem = new UserItem();
        petItem = new PetItem();

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));


        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_move_signup);
        edLoginID = findViewById(R.id.ed_loginID);
        edLoginPW = findViewById(R.id.ed_loginPW);

        callback = new SessionCallback();
        getCurrentSession().addCallback(callback);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }


        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    protected void redirectSignupActivity() {
        UserManagement.getInstance().requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("SessionCallback::", "onSessionClosed : " + errorResult.getErrorMessage());
            }

            @Override
            public void onNotSignedUp() {
                Log.e("SessionCallback :: ", "onNotSignedUp");
            }

            @Override
            public void onSuccess(final UserProfile result) {

                kakaoUserItem.setNickName(result.getNickname());
                kakaoUserItem.setEmail(result.getEmail());
                kakaoUserItem.setProfileImagePath(result.getProfileImagePath());
                kakaoUserItem.setThumnailPath(result.getThumbnailImagePath());
                kakaoUserItem.setUserId(String.valueOf(result.getId()));

                Call<ResponseBody> saltCall = retrofitService.saltLogin(kakaoUserItem.getEmail());
                saltCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                String resultBody = response.body().string();
                                try {

                                    JSONObject jsonObject = new JSONObject(resultBody);
                                    JSONArray jsonArray = jsonObject.getJSONArray("salt_table");

                                    if (jsonArray.length() == 0) {
                                        final Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                        intent.putExtra("kakao", 1);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        for (int i = 0; i< jsonArray.length(); i++) {
                                            JSONObject item = jsonArray.getJSONObject(i);
                                            stUserSalt = item.getString("user_salt");
                                            newPassword = SHA256Util.getEncrypt(String.valueOf(result.getId()), stUserSalt);
                                        }

                                        Call<ResponseBody> loginCall = retrofitService.login(kakaoUserItem.getEmail(), newPassword);
                                        loginCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()) {
                                                    try {
                                                        String result = response.body().string();
                                                        kakao = 1;
                                                        saveUserInfo(result);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

    }

    protected void saveUserInfo(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("user_table");

            if (jsonArray.length() != 0) {
                for (int i = 0; i< jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    stUserID = item.getString("user_id");
                    stUserPW = item.getString("user_pw");
                    stUserSalt = item.getString("user_salt");
                    stUserName = item.getString("user_name");
                    stUserBirth = item.getString("user_birth");
                    stUserProfile = item.getString("user_profile");
                    stUserArea = item.getString("user_area");

                    userItem.setStUserID(stUserID);
                    userItem.setStUserPW(stUserPW);
                    userItem.setStUserSalt(stUserSalt);
                    userItem.setStUserName(stUserName);
                    userItem.setStUserBirth(stUserBirth);
                    userItem.setStUserProfile(stUserProfile);
                    userItem.setStUserArea(stUserArea);
                }
                Call<ResponseBody> callPet = retrofitService.pet_login(stUserID);
                callPet.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray =  jsonObject.getJSONArray("pet_table");

                                    if (jsonArray.length() != 0 ) {
                                        for (int i = 0; i < jsonArray.length(); i ++) {
                                            JSONObject item = jsonArray.getJSONObject(i);


                                            stPetName = item.getString("pet_name");
                                            stPetBirth = item.getString("pet_birth");
                                            stPetCome = item.getString("pet_come");
                                            stPetKind = item.getString("pet_kind");

                                            petItem.setStPetName(stPetName);
                                            petItem.setStPetBirth(stPetBirth);
                                            petItem.setStPetCome(stPetCome);
                                            petItem.setStPetKind(stPetKind);

                                        }
                                        SaveUserInfo.saveUserInfo(appData, true, stUserID, stUserPW, stUserSalt, stUserName, stUserBirth, stUserProfile,
                                                stUserArea, stPetName, stPetBirth, stPetCome, stPetKind);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            } else if(jsonArray.length() == 0 && kakao == 1){
                    final Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.putExtra("kakao", 1);
                    startActivity(intent);
                    finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
