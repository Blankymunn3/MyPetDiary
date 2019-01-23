package io.kong.mypetdiary.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.InputStream;
import java.net.URL;

import io.kong.mypetdiary.activity.LoginActivity;
import io.kong.mypetdiary.adapter.MyPageListViewAdapter;
import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.UserItem;

import static android.content.Context.MODE_PRIVATE;


public class MyPageFragment extends Fragment {

    UserItem userItem;
    public static SharedPreferences appData;

    MyPageListViewAdapter adapter;
    ListView myPageListView;

    String getImageUrl;
    Bitmap bm;

    Handler handler = new Handler();

    public MyPageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userItem = new UserItem();

        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_my_page, container, false);

        final DrawerLayout drawerLayout = rootView.findViewById(R.id.drawerLayout);

        final View drawerView = rootView.findViewById(R.id.drawer);

        ImageButton btnOpenDrawer = rootView.findViewById(R.id.btn_my_page_menu);
        ImageButton btnCloseDrawer = rootView.findViewById(R.id.btn_my_page_menu_close);

        Button btnLogOut = rootView.findViewById(R.id.btn_logout);

        btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        btnCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        appData = getContext().getSharedPreferences("APPDATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = appData.edit();
                        editor.remove("SAVE_LOGIN_DATA");
                        editor.remove("user_id");
                        editor.remove("user_pw");
                        editor.remove("user_name");
                        editor.remove("user_profile");
                        editor.remove("user_area");
                        editor.remove("user_birth");

                        editor.remove("pet_name");
                        editor.remove("pet_birth");
                        editor.remove("pet_come");
                        editor.remove("pet_kind");
                        editor.apply();

                        Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });

        myPageListView = rootView.findViewById(R.id.myPageListView);
        adapter = new MyPageListViewAdapter();
        myPageListView.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test1", "test123");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test2", "test123");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test3", "test123");

        getImageUrl = userItem.getStUserProfile();

        final TextView txtMyPageName = rootView.findViewById(R.id.txt_my_page_name);
        final ImageView imvMyPageUser = rootView.findViewById(R.id.imv_my_page_user);

        final String stNickName = userItem.getStUserName();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(getImageUrl);
                    InputStream is = url.openStream();
                    bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imvMyPageUser.setImageBitmap(bm);
                        }
                    });

                } catch (Exception e) {
                    Log.e("Thread Error ::", e.getMessage());
                }
            }
        });

        thread.start();
        try {
            thread.join();
            txtMyPageName.setText(stNickName);
            imvMyPageUser.setImageBitmap(bm);
        } catch (InterruptedException e) {

        }

        return rootView;
    }

}
