package io.kong.mypetdiary.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.HomeListViewItem;

public class HomeListViewAdapter extends BaseAdapter {
    private ArrayList<HomeListViewItem> listViewItemList = new ArrayList<HomeListViewItem>();

    public HomeListViewAdapter() {

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

        if(String.valueOf(listViewItem.getDay()).equals("1")) {
            iconImageView.getLayoutParams().width = 0;
            iconImageView.getLayoutParams().height = 0;
        }
        iconImageView.setImageDrawable(listViewItem.getIcon());
        txtTitle.setText(listViewItem.getTitle());
        txtContent.setText(listViewItem.getContent());
        txtWeek.setText(listViewItem.getWeek());
        txtDay.setText(listViewItem.getDay());


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

    public void addItem(Drawable icon, String title, String content, String week, String day) {
        HomeListViewItem item = new HomeListViewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setContent(content);
        item.setWeek(week);
        item.setDay(day);

        listViewItemList.add(item);
    }
}