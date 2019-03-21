package io.kong.mypetdiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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

                    viewHolder.txtPetName.setText(listViewItem.getStPetName());
                    viewHolder.txtBirth.setText(listViewItem.getStPetBirth());

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

    public void addItem(int type, String imgPetUri, String petName, String petBirth) {
        MyPageListViewItem item = new MyPageListViewItem();

        item.setType(ITEM_TYPE_INFO);
        item.setImgPetUri(imgPetUri);
        item.setStPetName(petName);
        item.setStPetBirth(petBirth);

        listViewItemList.add(item);
    }

    public void addItem(int type) {
        MyPageListViewItem item = new MyPageListViewItem();
        item.setType(ITEM_TYPE_ADD);

        listViewItemList.add(item);
    }
}