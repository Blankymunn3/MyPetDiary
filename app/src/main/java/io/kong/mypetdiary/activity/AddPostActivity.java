package io.kong.mypetdiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.PetItem;
import io.kong.mypetdiary.item.UserItem;
import io.kong.mypetdiary.service.RetrofitService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPostActivity extends Activity implements View.OnClickListener {


    Retrofit retrofit;
    RetrofitService retrofitService;

    ProgressDialog dialog;

    UserItem userItem;
    PetItem petItem;

    Uri resultUri;

    ImageButton btnSun, btnBlur, btnRain, btnSnow,btnBack;
    ImageView btnUpImage;
    Button btnSave;
    EditText edTodayComment, edContent;
    TextView txtYear, txtMonth, txtDay, txtWeek, txtTodayComment;

    String stUserID, stYear, stMonth, stDay, stDate, stWeek, stWeather = "sun", stTodayComment, stContent, stPhoto;

    int month ,day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void saveDiary() {
        stYear = txtYear.getText().toString();
        stTodayComment = edTodayComment.getText().toString();
        stContent = edContent.getText().toString();

        if (month + 1 < 10) stMonth = "0" + Integer.toString(month + 1);
        else stMonth = Integer.toString(month + 1);
        if (day < 10) stDay = "0" + Integer.toString(day);
        else stDay = Integer.toString(day);

        stDate = stYear + stMonth + stDay;



        File file = new File(getRealPathFromURI(resultUri));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        Call<ResponseBody> call = retrofitService.uploadDiary(body, name, stUserID, stTodayComment, stContent, stDate, stWeather, stWeek);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    dialog.dismiss();
                    finishActivity();
                } else {
                    dialog.dismiss();
                    Toast.makeText(AddPostActivity.this, "일기쓰기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AddPostActivity.this, "일기쓰기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        setContentView(R.layout.activity_addpost);
        userItem = new UserItem();
        petItem = new PetItem();

        Intent intent = getIntent();
        String getStDate = intent.getStringExtra("diary_date");
        int getDay = intent.getIntExtra("diary_day", 0);

        stUserID = userItem.getStUserID();

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);


        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        final String stPetName = petItem.getStPetName();

        txtYear = findViewById(R.id.txt_home_year);
        txtMonth = findViewById(R.id.txt_home_month);
        txtDay = findViewById(R.id.txt_home_day);
        txtWeek = findViewById(R.id.txt_home_week);

        edContent = findViewById(R.id.ed_content);
        edTodayComment = findViewById(R.id.ed_today_comment);

        btnSave = findViewById(R.id.btn_save);
        btnUpImage = findViewById(R.id.img_upload);

        btnBack = findViewById(R.id.btn_back);
        btnSun = findViewById(R.id.imgBtn_home_sun);
        btnBlur = findViewById(R.id.imgBtn_home_blur);
        btnRain = findViewById(R.id.imgBtn_home_rain);
        btnSnow = findViewById(R.id.imgBtn_home_snow);

        txtTodayComment = findViewById(R.id.txt_today_comment);

        final Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DATE);
        cal.set(year, month, getDay);
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

        if (month + 1 < 10) stMonth = "0" + Integer.toString(month + 1);
        else stMonth = Integer.toString(month + 1);
        if (day < 10) stDay = "0" + Integer.toString(day);
        else stDay = Integer.toString(day);


        stDate = Integer.toString(year) + stMonth + stDay;
        Call<ResponseBody> call = null;

        txtWeek.setText(stWeek);

        txtTodayComment.setText("오늘의 " + stPetName + "에게 하고싶은 한마디는?");

        btnSun.setOnClickListener(this);
        btnBlur.setOnClickListener(this);
        btnRain.setOnClickListener(this);
        btnSnow.setOnClickListener(this);
        btnUpImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        if (getStDate == null) {
            call = retrofitService.selectDiary(stUserID, stDate);
            txtYear.setText(Integer.toString(year));
            txtMonth.setText(Integer.toString(month + 1));
            txtDay.setText(Integer.toString(day));
        } else if (getStDate.equals(stDate)) {
            call = retrofitService.selectDiary(stUserID, stDate);
            txtYear.setText(Integer.toString(year));
            txtMonth.setText(Integer.toString(month + 1));
            txtDay.setText(Integer.toString(day));
        } else {
            String stYear = getStDate.substring(0, 4);
            String stMonth = getStDate.substring(4, 6);
            if (stMonth.substring(0, 1).equals("0")) stMonth = stMonth.substring(1, 2);
            String stDay = getStDate.substring(6, 8);
            if (stDay.substring(0, 1).equals("0")) stDay = stDay.substring(1, 2);
            call = retrofitService.selectDiary(stUserID, getStDate);

            txtYear.setText(stYear);
            txtMonth.setText(stMonth);
            txtDay.setText(stDay);

            btnSave.setTextColor(R.color.txt_block_color);

            edTodayComment.setFocusable(false);
            edTodayComment.setClickable(false);
            edContent.setFocusable(false);
            edContent.setClickable(false);

            btnUpImage.setClickable(false);
            btnSave.setText("");
            btnSun.setClickable(false);
            btnBlur.setClickable(false);
            btnRain.setClickable(false);
            btnSnow.setClickable(false);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("diary_table");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    stTodayComment = item.getString("diary_today_comment");
                                    stContent = item.getString("diary_content");
                                    stWeather = item.getString("diary_weather");
                                    stPhoto = item.getString("diary_photo");

                                    edTodayComment.setText(stTodayComment);
                                    edContent.setText(stContent);
                                    Glide.with(AddPostActivity.this).load(stPhoto).into(btnUpImage);

                                    edTodayComment.setFocusable(false);
                                    edTodayComment.setClickable(false);
                                    edContent.setFocusable(false);
                                    edContent.setClickable(false);
                                    btnUpImage.setClickable(false);
                                    btnSave.setClickable(false);
                                    btnSave.setTextColor(R.color.txt_block_color);

                                    btnSun.setClickable(false);
                                    btnBlur.setClickable(false);
                                    btnRain.setClickable(false);
                                    btnSnow.setClickable(false);

                                    if (stWeather.equals("sun")) {
                                        btnSun.setImageResource(R.drawable.checked);
                                        btnBlur.setImageResource(0);
                                        btnRain.setImageResource(0);
                                        btnSnow.setImageResource(0);
                                    } else if (stWeather.equals("blur")) {
                                        btnSun.setImageResource(0);
                                        btnBlur.setImageResource(R.drawable.checked);
                                        btnRain.setImageResource(0);
                                        btnSnow.setImageResource(0);
                                    } else if (stWeather.equals("rain")) {
                                        btnSun.setImageResource(0);
                                        btnBlur.setImageResource(0);
                                        btnRain.setImageResource(R.drawable.checked);
                                        btnSnow.setImageResource(0);

                                    } else if (stWeather.equals("snow")) {
                                        btnSun.setImageResource(0);
                                        btnBlur.setImageResource(0);
                                        btnRain.setImageResource(0);
                                        btnSnow.setImageResource(R.drawable.checked);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed(){
        finishActivity();
    }

    protected void finishActivity() {
        Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_back:
                finishActivity();
                break;
            case R.id.imgBtn_home_sun:
                stWeather = "sun";
                btnSun.setImageResource(R.drawable.checked);
                btnBlur.setImageResource(0);
                btnRain.setImageResource(0);
                btnSnow.setImageResource(0);
                break;
            case R.id.imgBtn_home_blur:
                stWeather = "blur";
                btnSun.setImageResource(0);
                btnBlur.setImageResource(R.drawable.checked);
                btnRain.setImageResource(0);
                btnSnow.setImageResource(0);
                break;
            case R.id.imgBtn_home_rain:
                stWeather = "rain";
                btnSun.setImageResource(0);
                btnBlur.setImageResource(0);
                btnRain.setImageResource(R.drawable.checked);
                btnSnow.setImageResource(0);
                break;
            case R.id.imgBtn_home_snow:
                stWeather = "snow";
                btnSun.setImageResource(0);
                btnBlur.setImageResource(0);
                btnRain.setImageResource(0);
                btnSnow.setImageResource(R.drawable.checked);
                break;
            case R.id.btn_save:
                dialog = ProgressDialog.show(AddPostActivity.this, "", "Uploading Diary...", true);
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AddPostActivity.this, "UploadImage....", Toast.LENGTH_SHORT).show();
                            }
                        });
                        saveDiary();
                    }
                }).start();
                break;
            case R.id.img_upload:
                Uri uri = Uri.parse("content://media/external/images/media");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) return;
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 0) {
            Uri selPhotoUri = intent.getData();
            CropImage.activity(selPhotoUri)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(intent);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    btnUpImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("ExceptionError::", error.getLocalizedMessage());
            }
        }

    }

}
