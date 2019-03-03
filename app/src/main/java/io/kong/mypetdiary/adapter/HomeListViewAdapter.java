package io.kong.mypetdiary.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.HomeListViewItem;

public class HomeListViewAdapter extends BaseAdapter {
    private ArrayList<HomeListViewItem> listViewItemList;

    String stDay;

    public HomeListViewAdapter(ArrayList<HomeListViewItem> listViewItemList) {
        if (listViewItemList == null) {
            this.listViewItemList = new ArrayList<HomeListViewItem>();
        } else {
            this.listViewItemList = listViewItemList;
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


        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        String stToday = sdf.format(date);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_listview_item, parent, false);
        }


        ImageView iconImageView = convertView.findViewById(R.id.img_list_post);
        TextView txtTitle = convertView.findViewById(R.id.txt_list_title);
        TextView txtContent = convertView.findViewById(R.id.txt_list_content);
        TextView txtWeek = convertView.findViewById(R.id.txt_list_week);
        TextView txtDay = convertView.findViewById(R.id.txt_list_day);

        HomeListViewItem listViewItem = listViewItemList.get(pos);


        if (listViewItem.getDay() < 10) {
            stDay = "0" + Integer.toString(listViewItem.getDay());
        }


        Glide.with(convertView.getContext()).load(listViewItem.getImgUrl()).into(iconImageView);

        if (stToday.equals(stDay)) convertView.setBackground(ContextCompat.getDrawable(convertView.getContext(), R.drawable.back_today_listview));
        else convertView.setBackground(ContextCompat.getDrawable(convertView.getContext(), R.drawable.back_listview));

        iconImageView.getLayoutParams().width = listViewItem.getWidth();
        iconImageView.getLayoutParams().height = listViewItem.getHeight();

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

    public void addItem(String imgUrl, String title, String content, String week, int day, int width, int height) {
        HomeListViewItem item = new HomeListViewItem();

        item.setImgUrl(imgUrl);
        item.setTitle(title);
        item.setContent(content);
        item.setWeek(week);
        item.setDay(day);
        item.setWidth(width);
        item.setHeight(height);

        listViewItemList.add(item);
    }
}