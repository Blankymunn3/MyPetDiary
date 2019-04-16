package io.kong.mypetdiary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.activity.AddPetActivity;
import io.kong.mypetdiary.activity.SelectMyPetActivity;
import io.kong.mypetdiary.item.MyPageListViewItem;

import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_BIRTH;
import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_COME;
import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_KIND;
import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_NAME;
import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_URL;

public class MyPageListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_INFO = 0;
    private static final int ITEM_TYPE_ADD = 1;

    private ArrayList<MyPageListViewItem> listViewItems;

    Context context;

    String getImageUrl, stPetName, stPetBirth, stPetUrl, stPetCome;
    int petKind;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_INFO) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_listview_item, parent, false);
            return new ViewHolder(v);
        } else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_listview_last, parent, false);
            return new ViewHolderLast(v);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imvPet;
        TextView txtPetName;
        TextView txtBirth;
        ViewHolder(View itemView) {
            super(itemView);
            imvPet = itemView.findViewById(R.id.imv_page_pet);
            txtPetName = itemView.findViewById(R.id.txt_page_name);
            txtBirth = itemView.findViewById(R.id.txt_page_birth);
        }
    }

    public static class ViewHolderLast extends RecyclerView.ViewHolder {
        ImageView imvAdd;
        public ViewHolderLast(View itemView) {
            super(itemView);
            imvAdd = itemView.findViewById(R.id.imv_add);
        }
    }

    public MyPageListViewAdapter(ArrayList<MyPageListViewItem> listViewItemList, Context getContext) {
        if (listViewItemList == null) {
            this.listViewItems = new ArrayList<MyPageListViewItem>();
        } else {
            this.listViewItems = listViewItemList;
        }
        this.listViewItems = listViewItemList;
        this.context = getContext;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final MyPageListViewItem item = listViewItems.get(position);

        if (holder instanceof ViewHolder) {
            int year = Integer.parseInt(item.getStPetBirth().substring(0, 4));
            int month = Integer.parseInt(item.getStPetBirth().substring(5,7));
            int day = Integer.parseInt(item.getStPetBirth().substring(8,10));

            int d_day = caldate(year, month, day);

            Glide.with(context).load(item.getImgPetUri()).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(((ViewHolder) holder).imvPet);
            ((ViewHolder) holder).txtPetName.setText(item.getStPetName());
            ((ViewHolder) holder).txtBirth.setText(Integer.toString(d_day) + "일 째");

            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stPetUrl = item.getImgPetUri();
                    stPetName = item.getStPetName();
                    stPetBirth = item.getStPetBirth();
                    stPetCome = item.getStPetCome();
                    petKind = item.getStPetKind();

                    Intent intent = new Intent(context, SelectMyPetActivity.class);
                    intent.putExtra(EXTRA_PET_URL, stPetUrl);
                    intent.putExtra(EXTRA_PET_NAME, stPetName);
                    intent.putExtra(EXTRA_PET_BIRTH, stPetBirth);
                    intent.putExtra(EXTRA_PET_COME, stPetCome);
                    intent.putExtra(EXTRA_PET_KIND, petKind);
                    context.startActivity(intent);

                }
            });
        } else {
            ((ViewHolderLast) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddPetActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listViewItems.get(position).getType() == 0) {
            return ITEM_TYPE_INFO;
        } else {
            return ITEM_TYPE_ADD;
        }
    }

    public void addItem(MyPageListViewItem data) {
        listViewItems.add(data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listViewItems.size();
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