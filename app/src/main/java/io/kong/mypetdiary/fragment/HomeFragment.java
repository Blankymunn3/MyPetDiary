package io.kong.mypetdiary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.kong.mypetdiary.activity.AddPostActivity;
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

    TextView txtTitle, txtSubTitle;

    String stWeek, stDate, stUserID, dbDay, dbWeek, dbImgUrl, dbTitle, dbContent, stMonth, stDay;

    ArrayList<HomeListViewItem> itemList = new ArrayList<HomeListViewItem>();

    int diaryCnt;
    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new HomeListViewAdapter(itemList, getContext());
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mainListView = rootView.findViewById(R.id.main_listview);

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        userItem = new UserItem();

        txtTitle = rootView.findViewById(R.id.txt_home_title);
        txtSubTitle = rootView.findViewById(R.id.txt_home_subTitle);
        imgPost = rootView.findViewById(R.id.img_list_post);


        mainListView.setAdapter(adapter);

        final Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
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

            if(month+1 < 10) stMonth = "0" + Integer.toString(month+1);
            else stMonth = Integer.toString(month+1);
            if(i < 10) stDay = "0" + Integer.toString(i);
            else stDay = Integer.toString(i);

            stDate = Integer.toString(year) + stMonth + stDay;

            final String subTitle = Integer.toString(cal.getActualMaximum(Calendar.DAY_OF_MONTH)) + "일 중 우리의 추억 ";

            Call<ResponseBody> call = retrofitService.selectDiary(stUserID, stDate);
            final int finalI = i;
            final String finalWeek = stWeek;
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

                                        adapter.addItem(dbImgUrl, dbTitle, dbContent, finalWeek, stDate, finalI,
                                                (int) getResources().getDimension(R.dimen.home_list_width), (int) getResources().getDimension(R.dimen.home_list_height));

                                        Comparator<HomeListViewItem> textAsc = new Comparator<HomeListViewItem>() {
                                            @Override
                                            public int compare(HomeListViewItem item1, HomeListViewItem item2) {
                                                return (item1.getDay() - item2.getDay()) ;
                                            }
                                        };

                                        Collections.sort(itemList, textAsc);
                                        adapter.notifyDataSetChanged();
                                        diaryCnt += 1;
                                    }
                                    txtSubTitle.setText(subTitle + Integer.toString(diaryCnt) + "개");
                                } else {
                                    adapter.addItem(null, null, null, finalWeek, stDate, finalI, 0, 0);
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
                return (item1.getDay() - item2.getDay());
            }
        };

        Collections.sort(itemList, textAsc);
        adapter.notifyDataSetChanged();

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String stToday = sdf.format(date);

        mainListView.setSelection(10);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(rootView.getContext(), AddPostActivity.class);
                i += 1;
                String stDay;

                if (i < 10) stDay = "0" + Integer.toString(i);
                else stDay = Integer.toString(i);

                String stDate =  Integer.toString(year) + stMonth + stDay;

                intent.putExtra("diary_date", stDate);
                intent.putExtra("diary_day", i);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }

}
