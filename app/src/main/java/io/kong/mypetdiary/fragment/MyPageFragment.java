package io.kong.mypetdiary.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;
import io.kong.mypetdiary.activity.SetImageActivity;
import io.kong.mypetdiary.adapter.MyPageListViewAdapter;
import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.UserItem;
import io.kong.mypetdiary.item.MyPageListViewItem;
import io.kong.mypetdiary.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyPageFragment extends Fragment {

    static final int TAG_GETIMAGESETTING = 1001;


    Retrofit retrofit;
    RetrofitService retrofitService;

    UserItem userItem;

    public static MyPageListViewAdapter adapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    TextView txtPetCnt, txtDiaryCnt;

    String getImageUrl, stPetName, stPetBirth, stPetUrl, stPetCome;
    int petCnt = 0, diaryCnt = 0, petKind;

    ArrayList<MyPageListViewItem> itemList = new ArrayList<MyPageListViewItem>();
    MyPageListViewItem myPageListViewItem;

    public MyPageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        mRecyclerView = rootView.findViewById(R.id.myPageListView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        userItem = new UserItem();

        adapter = new MyPageListViewAdapter(itemList, getContext());
        mRecyclerView.setAdapter(adapter);


        getImageUrl = userItem.getStUserProfile();

        TextView txtMyPageName = rootView.findViewById(R.id.txt_my_page_name);
        CircleImageView imvMyPageUser = rootView.findViewById(R.id.imv_my_page_user);
        String userID = userItem.getStUserID();
        String userProfile = userItem.getStUserProfile();

        txtPetCnt = rootView.findViewById(R.id.txt_my_pet_cnt);
        txtDiaryCnt = rootView.findViewById(R.id.txt_month_diary_cnt);

        if (userProfile.equals("null")) imvMyPageUser.setImageResource(R.drawable.face);
        else {
            Glide.with(getActivity()).load("http://13.209.93.19:3000/download?user_id=" + userID)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imvMyPageUser);
        }

        imvMyPageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), SetImageActivity.class);
                startActivityForResult(intent, TAG_GETIMAGESETTING);
            }
        });

        final String stNickName = userItem.getStUserName();
        txtMyPageName.setText(stNickName);

        Call<ResponseBody> diaryCall = retrofitService.diaryCnt(userID);
        diaryCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("diary_table");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    diaryCnt += 1;
                                }
                            }
                            txtDiaryCnt.setText(Integer.toString(diaryCnt));
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

        Call<ResponseBody> petCall = retrofitService.selectPet(userID);
        petCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("pet_list");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i <= jsonArray.length(); i++) {
                                    if (jsonArray.length() == i) {
                                        myPageListViewItem = new MyPageListViewItem(1, stPetUrl, stPetName, stPetBirth, stPetCome, petKind);
                                    } else {
                                        JSONObject item = jsonArray.getJSONObject(i);
                                        stPetUrl = item.getString("pet_image");
                                        stPetName = item.getString("pet_name");
                                        stPetBirth = item.getString("pet_birth");
                                        stPetCome = item.getString("pet_come");
                                        petKind = item.getInt("pet_kind");

                                        myPageListViewItem = new MyPageListViewItem(0, stPetUrl, stPetName, stPetBirth, stPetCome, petKind);

                                        petCnt += 1;
                                    }

                                    adapter.addItem(myPageListViewItem);

                                }
                            } else {
                                myPageListViewItem = new MyPageListViewItem(1, stPetUrl, stPetName, stPetBirth, stPetCome, petKind);
                                adapter.addItem(myPageListViewItem);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Comparator<MyPageListViewItem> textAsc = new Comparator<MyPageListViewItem>() {
                        @Override
                        public int compare(MyPageListViewItem item1, MyPageListViewItem item2) {
                            return (item1.getType() - item2.getType());
                        }
                    };

                    Collections.sort(itemList, textAsc);
                    adapter.notifyDataSetChanged();

                    txtPetCnt.setText(Integer.toString(petCnt));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return rootView;
    }
}
