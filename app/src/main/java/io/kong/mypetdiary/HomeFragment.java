package io.kong.mypetdiary;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends ListFragment {

    HomeListViewAdapter adapter;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new HomeListViewAdapter();
        setListAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test1", "test123");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test2", "test123");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background) , "test3", "test123");
        adapter.addItem(null, "test", "test");
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
