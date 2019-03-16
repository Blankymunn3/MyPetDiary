package io.kong.mypetdiary.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import de.hdodenhof.circleimageview.CircleImageView;
import io.kong.mypetdiary.activity.LoginActivity;
import io.kong.mypetdiary.activity.SetImageActivity;
import io.kong.mypetdiary.adapter.MyPageListViewAdapter;
import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.UserItem;

import static android.content.Context.MODE_PRIVATE;


public class MyPageFragment extends Fragment {

    static final int TAG_GETIMAGESETTING = 1001;

    UserItem userItem;
    public SharedPreferences appData;

    MyPageListViewAdapter adapter;
    ListView myPageListView;

    String getImageUrl;

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
                        editor.clear();
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

        getImageUrl = userItem.getStUserProfile();

        TextView txtMyPageName = rootView.findViewById(R.id.txt_my_page_name);
        CircleImageView imvMyPageUser = rootView.findViewById(R.id.imv_my_page_user);
        String userID = userItem.getStUserID();
        String userProfile = userItem.getStUserProfile();

        if(userProfile.equals("null")) imvMyPageUser.setImageResource(R.drawable.face);
        else Glide.with(getActivity()).load("http://13.209.93.19:3000/download?user_id=" + userID).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imvMyPageUser);


        imvMyPageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), SetImageActivity.class);
                startActivityForResult(intent,TAG_GETIMAGESETTING);
            }
        });

        final String stNickName = userItem.getStUserName();
        txtMyPageName.setText(stNickName);

        return rootView;
    }

}
