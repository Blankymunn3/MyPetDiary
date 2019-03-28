package io.kong.mypetdiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.fragment.HomeFragment;
import io.kong.mypetdiary.fragment.MyPageFragment;
import io.kong.mypetdiary.item.PetItem;
import io.kong.mypetdiary.item.UserItem;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity mainActivity;

    public SharedPreferences appData;
    boolean saveLoginData;

    private final static int FRAGMENT_HOME = 1;
    private final static int FRAGMENT_MY_PAGE = 2;
    private final static int ALL_PERMISSIONS_RESULT = 107;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissions = new ArrayList<>();

    private TextView txt_home, txt_addPost, txt_my_page;

    int EXTRA_FRAG, EXTRA_SIGN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        askPermissions();
    }

    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void init() {
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        EXTRA_FRAG = intent.getIntExtra("TAG_FRAG", 1);
        EXTRA_SIGN = intent.getIntExtra("EXTRA_SIGN", 0);

        if(EXTRA_FRAG == 1) callFragment(FRAGMENT_HOME);
        else if(EXTRA_FRAG == 2) callFragment(FRAGMENT_MY_PAGE);

        if(EXTRA_SIGN == 1) load();

        final Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);

        mainActivity = this;

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        txt_addPost = findViewById(R.id.txt_today_diary);
        txt_home = findViewById(R.id.txt_main_month);
        txt_my_page = findViewById(R.id.txt_my_page);

        txt_home.setText(Integer.toString(month + 1) + "월");

        txt_home.setOnClickListener(this);
        txt_addPost.setOnClickListener(this);
        txt_my_page.setOnClickListener(this);
    }

    public void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);

        String stUserID = appData.getString("user_id", "");
        String stUserPW = appData.getString("user_pw", "");
        String stUserName = appData.getString("user_name", "");
        String stUserProfile = appData.getString("user_profile", "");
        String stUserArea = appData.getString("user_area","");
        String stUserBirth = appData.getString("user_birth", "");

        String stPetName = appData.getString("pet_name", "");
        String stPetBirth = appData.getString("pet_birth", "");
        String stPetCome = appData.getString("pet_come", "");
        int petKind = appData.getInt("pet_kind", 0);

        UserItem userItem = new UserItem();
        PetItem petItem = new PetItem();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_main_month:
                callFragment(FRAGMENT_HOME);
                break;
            case R.id.txt_today_diary:
                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.txt_my_page:
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
