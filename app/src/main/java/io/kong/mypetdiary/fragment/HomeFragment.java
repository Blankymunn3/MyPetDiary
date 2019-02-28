package io.kong.mypetdiary.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import io.kong.mypetdiary.adapter.HomeListViewAdapter;
import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.HomeListViewItem;
import io.kong.mypetdiary.item.UserItem;
import io.kong.mypetdiary.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {


    Retrofit retrofit;
    RetrofitService retrofitService;

    UserItem userItem;

    HomeListViewAdapter adapter;
    ListView mainListView;
    ImageView imgPost;

    TextView txtTitle;

    String stWeek, stDate, stUserID, dbDay, dbWeek, dbImgUrl, dbTitle, dbContent;

    ArrayList<HomeListViewItem> itemList = new ArrayList<HomeListViewItem>();

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mainListView = rootView.findViewById(R.id.main_listview);

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        userItem = new UserItem();

        txtTitle = rootView.findViewById(R.id.txt_home_title);
        imgPost = rootView.findViewById(R.id.img_list_post);

        adapter = new HomeListViewAdapter(itemList);
        mainListView.setAdapter(adapter);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        txtTitle.setText(Integer.toString(year) + "년 " + Integer.toString(month + 1) + "월");

        stUserID = userItem.getStUserID();

        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            cal.set(year, month, i);
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
            switch (day_of_week) {
                case 1:
                    stWeek = "일";
                    break;
                case 2:
                    stWeek = "월";
                    break;
                case 3:
                    stWeek = "화";
                    break;
                case 4:
                    stWeek = "수";
                    break;
                case 5:
                    stWeek = "목";
                    break;
                case 6:
                    stWeek = "금";
                    break;
                case 7:
                    stWeek = "토";
                    break;
            }

            stDate = Integer.toString(year) + Integer.toString(month + 1) + Integer.toString(i);

            Call<ResponseBody> call = retrofitService.selectDiary(stUserID, stDate);
            final int finalI = i;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("diary_table");
                                if (jsonArray.length() != 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject item = jsonArray.getJSONObject(j);
                                        dbTitle = item.getString("diary_today_comment");
                                        dbContent = item.getString("diary_content");
                                        dbDay = item.getString("diary_date");
                                        dbImgUrl = item.getString("diary_photo");
                                        dbWeek = item.getString("diary_week");

                                        adapter.addItem(stUserID, dbImgUrl, dbTitle, dbContent, dbWeek, finalI, dbDay);

                                        Comparator<HomeListViewItem> textAsc = new Comparator<HomeListViewItem>() {
                                            @Override
                                            public int compare(HomeListViewItem item1, HomeListViewItem item2) {
                                                return (item1.getDay() - item2.getDay()) ;
                                            }
                                        };

                                        Collections.sort(itemList, textAsc);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    adapter.addItem(stUserID, null, null, null, stWeek, finalI, stDate);
                                    Comparator<HomeListViewItem> textAsc = new Comparator<HomeListViewItem>() {
                                        @Override
                                        public int compare(HomeListViewItem item1, HomeListViewItem item2) {
                                            return (item1.getDay() - item2.getDay()) ;
                                        }
                                    };
                                    Collections.sort(itemList, textAsc);
                                    adapter.notifyDataSetChanged();
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

        Comparator<HomeListViewItem> textAsc = new Comparator<HomeListViewItem>() {
            @Override
            public int compare(HomeListViewItem item1, HomeListViewItem item2) {
                return (item1.getDay() - item2.getDay()) ;
            }
        };

        Collections.sort(itemList, textAsc);
        adapter.notifyDataSetChanged();

        return rootView;
    }

}
