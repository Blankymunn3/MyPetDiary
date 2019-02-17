package io.kong.mypetdiary.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Calendar;

import io.kong.mypetdiary.adapter.HomeListViewAdapter;
import io.kong.mypetdiary.R;

public class HomeFragment extends Fragment {

    HomeListViewAdapter adapter;
    ListView mainListView;

    String stWeek;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mainListView = rootView.findViewById(R.id.main_listview);
        adapter = new HomeListViewAdapter();
        mainListView.setAdapter(adapter);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);



        for(int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            cal.set(year, month, i);
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
            switch (day_of_week) {
                case 1: stWeek = "일요일"; break;
                case 2: stWeek = "월요일"; break;
                case 3: stWeek = "화요일"; break;
                case 4: stWeek = "수요일"; break;
                case 5: stWeek = "목요일"; break;
                case 6: stWeek = "금요일"; break;
                case 7: stWeek = "토요일"; break;
            }
            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "title1", "content1", stWeek, Integer.toString(i));

        }





        return rootView;
    }

}
