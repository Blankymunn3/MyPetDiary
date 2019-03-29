package io.kong.mypetdiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.service.MyPageListViewItem;

public class MyPageListViewAdapter extends BaseAdapter {
    private ArrayList<MyPageListViewItem> listViewItemList = new ArrayList<MyPageListViewItem>();

    private static final int ITEM_TYPE_INFO = 0;
    private static final int ITEM_TYPE_ADD = 1;
    private static final int ITEM_TYPE_MAX = 2;

    Context context;
    private LayoutInflater mInflater;

    private class ViewHolder {
        ImageView imvPet;
        TextView txtPetName;
        TextView txtBirth;
    }

    public MyPageListViewAdapter(ArrayList<MyPageListViewItem> listViewItemList, Context getContext) {
        if (listViewItemList == null) {
            this.listViewItemList = new ArrayList<MyPageListViewItem>();
        } else {
            this.listViewItemList = listViewItemList;
        }
        this.context = getContext;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_MAX;
    }

    @Override
    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType();
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int viewType = getItemViewType(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            switch (viewType) {
                case ITEM_TYPE_INFO:
                    convertView = mInflater.inflate(R.layout.mypage_listview_item, parent, false);

                    viewHolder.imvPet = convertView.findViewById(R.id.imv_page_pet);
                    viewHolder.txtPetName = convertView.findViewById(R.id.txt_page_name);
                    viewHolder.txtBirth = convertView.findViewById(R.id.txt_page_birth);
                    convertView.setTag(viewHolder);

                    MyPageListViewItem listViewItem = listViewItemList.get(position);

                    int year = Integer.parseInt(listViewItem.getStPetBirth().substring(0, 4));
                    int month = Integer.parseInt(listViewItem.getStPetBirth().substring(5,7));
                    int day = Integer.parseInt(listViewItem.getStPetBirth().substring(8,10));

                    int d_day = caldate(year, month, day);


                    Glide.with(context).load(listViewItem.getImgPetUri()).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(viewHolder.imvPet);
                    viewHolder.txtPetName.setText(listViewItem.getStPetName());
                    viewHolder.txtBirth.setText(Integer.toString(d_day) + "일 째");

                    break;
                case ITEM_TYPE_ADD:
                    convertView = mInflater.inflate(R.layout.mypage_listview_last, parent, false);
                    convertView.setTag(viewHolder);

                    break;
            }

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

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

    public void addItem(int type, String imgPetUri, String petName, String petBirth, String petCome, int petKind) {
        MyPageListViewItem item = new MyPageListViewItem();

        item.setType(ITEM_TYPE_INFO);
        item.setImgPetUri(imgPetUri);
        item.setStPetName(petName);
        item.setStPetBirth(petBirth);
        item.setStPetCome(petCome);
        item.setStPetKind(petKind);

        listViewItemList.add(item);
    }

    public void addItem(int type) {
        MyPageListViewItem item = new MyPageListViewItem();
        item.setType(ITEM_TYPE_ADD);

        listViewItemList.add(item);
    }


    public int caldate(int myear, int mmonth, int mday) {
        try {

            GregorianCalendar cal = new GregorianCalendar();
            long currentTime = cal.getTimeInMillis() / (1000*60*60*24);
            cal.set(myear,mmonth - 1 , mday);
            long birthTime = cal.getTimeInMillis() / (1000*60*60*24);
            int interval = (int)( birthTime - currentTime );

            interval = interval - interval - interval + 1;

            return interval;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}