package io.kong.mypetdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HomeFragment extends Fragment {

    HomeListViewAdapter adapter;
    ListView mainListView;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);


        mainListView = rootView.findViewById(R.id.main_listview);
        adapter = new HomeListViewAdapter();
        mainListView.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test1", "test123");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test2", "test123");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test3", "test123");
        
        return rootView;
    }

}
