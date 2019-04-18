package io.kong.mypetdiary.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.activity.AddPostActivity;
import io.kong.mypetdiary.activity.MainActivity;
import io.kong.mypetdiary.item.HomeListViewItem;
import io.kong.mypetdiary.item.UserItem;
import io.kong.mypetdiary.service.RetrofitService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    Retrofit retrofit;
    RetrofitService retrofitService;

    UserItem userItem;
    String stDay, stUserID, stDate;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView linearLayout;
        ImageView iconImageView;
        TextView txtTitle;
        TextView txtContent;
        TextView txtWeek;
        TextView txtDay;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.home_linear);
            iconImageView = itemView.findViewById(R.id.img_list_post);
            txtTitle = itemView.findViewById(R.id.txt_list_title);
            txtContent = itemView.findViewById(R.id.txt_list_content);
            txtWeek = itemView.findViewById(R.id.txt_list_week);
            txtDay = itemView.findViewById(R.id.txt_list_day);
        }
    }

    private ArrayList<HomeListViewItem> listViewItemList;
    public HomeListViewAdapter(ArrayList<HomeListViewItem> listViewItemList, Context getContext) {
        if (listViewItemList == null) {
            this.listViewItemList = new ArrayList<HomeListViewItem>();
        } else {
            this.listViewItemList = listViewItemList;
        }
        this.listViewItemList = listViewItemList;
        this.context = getContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listview_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
        userItem = new UserItem();

        final int pos = position;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String stToday = sdf.format(date);

        HomeListViewItem listViewItem = listViewItemList.get(pos);

        stUserID = userItem.getStUserID();
        stDate =listViewItem.getDate();

        if (listViewItem.getDay() < 10) stDay = "0" + Integer.toString(listViewItem.getDay());
        else stDay = Integer.toString(listViewItem.getDay());

        Glide.with(context).load(listViewItem.getImgUrl()).into(viewHolder.iconImageView);

        if (stToday.equals(stDay)) viewHolder.linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.back_today_listview));
        else viewHolder.linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.back_listview));
        if (listViewItem.getWeek().equals("일")) {
            viewHolder.txtDay.setTextColor(Color.parseColor("#FFF44336"));
            viewHolder.txtWeek.setTextColor(Color.parseColor("#FFF44336"));
        } else if(listViewItem.getWeek().equals("토")) {
            viewHolder.txtDay.setTextColor(Color.parseColor("#FF2196F3"));
            viewHolder.txtWeek.setTextColor(Color.parseColor("#FF2196F3"));
        } else {
            viewHolder.txtDay.setTextColor(Color.BLACK);
            viewHolder.txtWeek.setTextColor(Color.BLACK);
        }

        viewHolder.iconImageView.getLayoutParams().width = listViewItem.getWidth();
        viewHolder.iconImageView.getLayoutParams().height = listViewItem.getHeight();

        viewHolder.txtTitle.setText(listViewItem.getTitle());
        viewHolder.txtContent.setText(listViewItem.getContent());
        viewHolder.txtWeek.setText(listViewItem.getWeek() + "요일");
        viewHolder.txtDay.setText(Integer.toString(listViewItem.getDay()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = new MainActivity();
                Intent intent = new Intent(context, AddPostActivity.class);
                int getPos = pos;
                getPos += 1;
                String stMonth, stDay;

                final Calendar cal = Calendar.getInstance();
                final int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);

                if(month+1 < 10) stMonth = "0" + Integer.toString(month+1);
                else stMonth = Integer.toString(month+1);

                if (getPos < 10) stDay = "0" + Integer.toString(getPos);
                else stDay = Integer.toString(getPos);

                String stDate =  Integer.toString(year) + stMonth + stDay;

                intent.putExtra("diary_date", stDate);
                intent.putExtra("diary_day", pos);
                context.startActivity(intent);
                mainActivity.finish();
            }
        });
    }

    public void addItem(HomeListViewItem data) {
        listViewItemList.add(data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listViewItemList.size();
    }


}