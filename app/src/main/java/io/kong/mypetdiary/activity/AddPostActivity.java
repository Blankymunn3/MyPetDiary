package io.kong.mypetdiary.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.PetItem;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    PetItem petItem;

    ImageButton btnSun, btnBlur, btnRain, btnSnow;
    Button btnUpImage;
    TextView txtYear, txtMonth, txtDay, txtWeek, txtTodayComment;

    String stWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        petItem = new PetItem();

        init();
    }

    private void init() {
        setContentView(R.layout.activity_addpost);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        final String stPetName = petItem.getStPetName();

        txtYear = findViewById(R.id.txt_home_year);
        txtMonth = findViewById(R.id.txt_home_month);
        txtDay = findViewById(R.id.txt_home_day);
        txtWeek = findViewById(R.id.txt_home_week);

        btnSun = findViewById(R.id.imgBtn_home_sun);
        btnBlur = findViewById(R.id.imgBtn_home_blur);
        btnRain = findViewById(R.id.imgBtn_home_rain);
        btnSnow = findViewById(R.id.imgBtn_home_snow);

        txtTodayComment = findViewById(R.id.txt_today_comment);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

        switch (day_of_week) {
            case 1:
                stWeek = "일";
                break;
            case 2:
                stWeek = "월";
                break;
            case 3:
                stWeek = "화";
                break;
            case 4:
                stWeek = "수";
                break;
            case 5:
                stWeek = "목";
                break;
            case 6:
                stWeek = "금";
                break;
            case 7:
                stWeek = "토";
                break;
        }

        txtYear.setText(Integer.toString(year));
        txtMonth.setText(Integer.toString(month + 1));
        txtDay.setText(Integer.toString(day));
        txtWeek.setText(stWeek);

        txtTodayComment.setText("오늘의 " + stPetName + "에게 하고싶은 한마디는?");


        btnSun.setOnClickListener(this);
        btnBlur.setOnClickListener(this);
        btnRain.setOnClickListener(this);
        btnSnow.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_home_sun:
                btnSun.setImageResource(R.drawable.baseline_check_black_18dp);
                btnBlur.setImageResource(0);
                btnRain.setImageResource(0);
                btnSnow.setImageResource(0);
                break;
            case R.id.imgBtn_home_blur:
                btnSun.setImageResource(0);
                btnBlur.setImageResource(R.drawable.baseline_check_black_18dp);
                btnRain.setImageResource(0);
                btnSnow.setImageResource(0);
                break;
            case R.id.imgBtn_home_rain:
                btnSun.setImageResource(0);
                btnBlur.setImageResource(0);
                btnRain.setImageResource(R.drawable.baseline_check_black_18dp);
                btnSnow.setImageResource(0);
                break;
            case R.id.imgBtn_home_snow:
                btnSun.setImageResource(0);
                btnBlur.setImageResource(0);
                btnRain.setImageResource(0);
                btnSnow.setImageResource(R.drawable.baseline_check_black_18dp);
                break;
        }
    }
}
