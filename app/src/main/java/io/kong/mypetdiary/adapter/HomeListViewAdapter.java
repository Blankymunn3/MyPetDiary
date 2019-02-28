package io.kong.mypetdiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.HomeListViewItem;

public class HomeListViewAdapter extends BaseAdapter {
    private ArrayList<HomeListViewItem> listViewItemList;

    public HomeListViewAdapter(ArrayList<HomeListViewItem> listViewItemList) {
        if (listViewItemList == null) {
            this.listViewItemList = new ArrayList<HomeListViewItem>() ;
        } else {
            this.listViewItemList = listViewItemList ;
        }
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_listview_item, parent, false);
        }

        ImageView iconImageView = convertView.findViewById(R.id.img_list_post);
        TextView txtTitle = convertView.findViewById(R.id.txt_list_title);
        TextView txtContent = convertView.findViewById(R.id.txt_list_content);
        TextView txtWeek = convertView.findViewById(R.id.txt_list_week);
        TextView txtDay = convertView.findViewById(R.id.txt_list_day);

        HomeListViewItem listViewItem = listViewItemList.get(position);

        if (listViewItem.getImgUrl() == null) {
            iconImageView.getLayoutParams().width = 0;
            iconImageView.getLayoutParams().height = 0;
        } else {
            String imgUrl = "http://13.209.93.19:3000/downloadDiary?user_id=" + listViewItem.getStUserID() + "&diary_date=" + listViewItem.getDate();
            Glide.with(convertView).load(imgUrl).into(iconImageView);
        }

        txtTitle.setText(listViewItem.getTitle());
        txtContent.setText(listViewItem.getContent());
        txtWeek.setText(listViewItem.getWeek() + "요일");
        txtDay.setText(Integer.toString(listViewItem.getDay()));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(String userID, String imgUrl, String title, String content, String week, int day, String date) {
        HomeListViewItem item = new HomeListViewItem();

        item.setStUserID(userID);
        item.setImgUrl(imgUrl);
        item.setTitle(title);
        item.setContent(content);
        item.setWeek(week);
        item.setDay(day);
        item.setDate(date);

        listViewItemList.add(item);
    }
}