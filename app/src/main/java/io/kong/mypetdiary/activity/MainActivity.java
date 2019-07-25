package io.kong.mypetdiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.fragment.HomeFragment;
import io.kong.mypetdiary.fragment.MyPageFragment;
import io.kong.mypetdiary.item.PetItem;
import io.kong.mypetdiary.item.UserItem;
import io.kong.mypetdiary.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity mainActivity;

    private final static int FRAGMENT_HOME = 1;
    private final static int FRAGMENT_MY_PAGE = 2;
    private final static int ALL_PERMISSIONS_RESULT = 107;

    Retrofit retrofit;
    RetrofitService retrofitService;

    public SharedPreferences appData;
    boolean saveLoginData;

    UserItem userItem;

    FragmentManager fragmentManager = getSupportFragmentManager();
    HomeFragment homeFragment;
    MyPageFragment myPageFragment;
    Calendar cal;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissions = new ArrayList<>();

    private TextView txt_home, txt_addPost, txt_my_page, txtTitle, txtSubTitle;
    Button btnLogOut;
    ImageButton btnOpenDrawer, btnCloseDrawer;
    DrawerLayout drawerLayout;
    View drawerView, mainTitleBar, subTitleBar;

    String stUserID, stUserPW, stMonth, stDay;
    int EXTRA_FRAG, diaryCnt, getFrag;

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

        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        userItem = new UserItem();

        mainTitleBar = findViewById(R.id.main_titlebar);
        subTitleBar = findViewById(R.id.sub_titlebar);


        EXTRA_FRAG = intent.getIntExtra("TAG_FRAG", 1);

        if (EXTRA_FRAG == 1) {
            load();
            homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            mainTitleBar();
        } else if (EXTRA_FRAG == 2) {
            callFragment(FRAGMENT_MY_PAGE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            subTitleBar();
                        }
                    });

                }
            }).start();

        }

        getFrag = 0;

        cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);

        mainActivity = this;

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        txt_addPost = findViewById(R.id.txt_today_diary);
        txt_home = findViewById(R.id.txt_main_month);
        txt_my_page = findViewById(R.id.txt_my_page);


        btnOpenDrawer = findViewById(R.id.btn_my_page_menu);
        btnCloseDrawer = findViewById(R.id.btn_my_page_menu_close);

        txt_home.setText(Integer.toString(month + 1) + "월");

        txt_home.setOnClickListener(this);
        txt_addPost.setOnClickListener(this);
        txt_my_page.setOnClickListener(this);
    }

    public void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);

        stUserID = appData.getString("user_id", "");
        stUserPW = appData.getString("user_pw", "");
        String stUserName = appData.getString("user_name", "");
        String stUserProfile = appData.getString("user_profile", "");
        String stUserArea = appData.getString("user_area", "");
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
        if (getFrag == 0) {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else if (drawerLayout.isDrawerOpen(drawerView) && getFrag == 1) {
            drawerLayout.closeDrawer(drawerView);
        } else {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_main_month:
                getFrag = 0;
                callFragment(FRAGMENT_HOME);
                mainTitleBar();
                break;
            case R.id.txt_today_diary:
                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_my_page:
                getFrag = 1;
                callFragment(FRAGMENT_MY_PAGE);
                subTitleBar();
                break;
            case R.id.btn_my_page_menu:
                drawerLayout.openDrawer(drawerView);
                break;
            case R.id.btn_my_page_menu_close:
                drawerLayout.closeDrawer(drawerView);
                break;
            case R.id.btn_logout:
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        SharedPreferences.Editor editor = appData.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
        }
    }

    private void callFragment(int fragment_no) {
        switch (fragment_no) {
            case 1:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment).commit();
                }
                if (homeFragment != null)
                    fragmentManager.beginTransaction().show(homeFragment).commit();
                if (myPageFragment != null)
                    fragmentManager.beginTransaction().hide(myPageFragment).commit();
                break;
            case 2:
                if (myPageFragment == null) {
                    myPageFragment = new MyPageFragment();
                    fragmentManager.beginTransaction().add(R.id.fragment_container, myPageFragment).commit();
                }
                if (homeFragment != null)
                    fragmentManager.beginTransaction().hide(homeFragment).commit();
                if (myPageFragment != null)
                    fragmentManager.beginTransaction().show(myPageFragment).commit();
                break;
        }
    }

    private void mainTitleBar() {

        mainTitleBar.setVisibility(View.VISIBLE);
        subTitleBar.setVisibility(View.GONE);


        txtTitle = mainTitleBar.findViewById(R.id.txt_home_title);
        txtSubTitle = mainTitleBar.findViewById(R.id.txt_home_subTitle);

        cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        txtTitle.setText(Integer.toString(year) + "년 " + Integer.toString(month + 1) + "월");

        diaryCnt = 0;
        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            if (month + 1 < 10) stMonth = "0" + Integer.toString(month + 1);
            else stMonth = Integer.toString(month + 1);
            if (i < 10) stDay = "0" + Integer.toString(i);
            else stDay = Integer.toString(i);


            final String subTitle = Integer.toString(cal.getActualMaximum(Calendar.DAY_OF_MONTH)) + "일 중 우리의 추억 ";
            String stDate = Integer.toString(year) + stMonth + stDay;

            Call<ResponseBody> call = retrofitService.selectDiary(stUserID, stDate);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("diary_table");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    diaryCnt += 1;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        txtSubTitle.setText(subTitle + Integer.toString(diaryCnt) + "개");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
    }

    private void subTitleBar() {

        subTitleBar.setVisibility(View.VISIBLE);
        mainTitleBar.setVisibility(View.GONE);

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerView = findViewById(R.id.drawer);

        btnOpenDrawer.setOnClickListener(this);
        btnCloseDrawer.setOnClickListener(this);

        btnLogOut = findViewById(R.id.btn_logout);
        btnLogOut.setOnClickListener(this);
    }
}
