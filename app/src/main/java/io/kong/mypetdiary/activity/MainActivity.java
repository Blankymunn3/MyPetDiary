package io.kong.mypetdiary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.fragment.HomeFragment;
import io.kong.mypetdiary.fragment.MyPageFragment;
import io.kong.mypetdiary.item.UserItem;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity mainActivity;

    private final int FRAGMENT_HOME = 1;
    private final int FRAGMENT_MY_PAGE = 2;

    private static final int REQUEST_CAMERA = 1;

    UserItem userItem;

    private ImageButton btn_home, btn_addPost, btn_my_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        permission_init();
    }

    private void init() {
        setContentView(R.layout.activity_main);
        mainActivity = this;

        userItem = new UserItem();

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        btn_home = findViewById(R.id.btn_fragment_home);
        btn_addPost = findViewById(R.id.btn_fragment_add);
        btn_my_page = findViewById(R.id.btn_fragment_my_page);

        btn_home.setOnClickListener(this);
        btn_addPost.setOnClickListener(this);
        btn_my_page.setOnClickListener(this);

        callFragment(FRAGMENT_HOME);
    }

    private void permission_init() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }

        } else {

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_home:
                callFragment(FRAGMENT_HOME);
                break;
            case R.id.btn_fragment_add:
                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_fragment_my_page:
                callFragment(FRAGMENT_MY_PAGE);
                break;
        }
    }

    private void callFragment(int fragment_no) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment_no) {
            case 1:
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.fragment_container, homeFragment);
                transaction.commit();
                break;
            case 2:
                MyPageFragment myPageFragment = new MyPageFragment();
                transaction.replace(R.id.fragment_container, myPageFragment);
                transaction.commit();
                break;
        }
    }
}
