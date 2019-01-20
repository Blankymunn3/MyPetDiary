package io.kong.mypetdiary.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import io.kong.mypetdiary.item.KakaoUserItem;
import io.kong.mypetdiary.R;

import static com.kakao.auth.Session.getCurrentSession;

public class LoginActivity extends AppCompatActivity{

    private KakaoUserItem kakaoUserItem;

    private SessionCallback callback;
    private LoginButton btn_kakao_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kakaoUserItem = new KakaoUserItem();
        if(Session.getCurrentSession().checkAndImplicitOpen()) {
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_login);

            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

            btn_kakao_login = findViewById(R.id.btn_kakao_login);
            callback = new SessionCallback();
            getCurrentSession().addCallback(callback);
        }



        Button btnLogin = findViewById(R.id.btn_login);
        Button btnSignup = findViewById(R.id.btn_move_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.putExtra("kakao", 0);
                startActivity(intent);
            }
        });
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
            if(exception != null) {
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
            public void onSuccess(UserProfile result) {
                kakaoUserItem.setNickName(result.getNickname());
                kakaoUserItem.setEmail(result.getEmail());
                kakaoUserItem.setProfileImagePath(result.getProfileImagePath());
                kakaoUserItem.setThumnailPath(result.getThumbnailImagePath());
                kakaoUserItem.setUUID(result.getUUID());
                kakaoUserItem.setUserId(result.getId());
            }
        });

        final Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("kakao",1);
        startActivity(intent);
        finish();
    }
}