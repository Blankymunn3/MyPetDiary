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

    public MyPageListViewAdapter(ArrayList<MyPageListViewItem> listViewItemList, Context getContext) {
        if (listViewItemList == null) {
            this.listViewItemList = new ArrayList<MyPageListViewItem>();
        } else {
            this.listViewItemList = listViewItemList;
        }
        context = getContext;
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
        final Context context = parent.getContext();
        int viewType = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            MyPageListViewItem listViewItem = listViewItemList.get(position);

            switch (viewType) {
                case ITEM_TYPE_INFO:
                    convertView = inflater.inflate(R.layout.mypage_listview_item, parent, false);

                    ImageView imvPet = convertView.findViewById(R.id.imv_page_pet);
                    TextView txtPetName = convertView.findViewById(R.id.txt_page_name);
                    TextView txtBirth = convertView.findViewById(R.id.txt_page_birth);


                    txtPetName.setText(listViewItem.getStPetName());
                    txtBirth.setText(listViewItem.getStPetBirth());
                    break;
                case ITEM_TYPE_ADD:
                    convertView = inflater.inflate(R.layout.mypage_listview_last, parent, false);

                    break;
            }
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