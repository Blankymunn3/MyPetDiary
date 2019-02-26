package io.kong.mypetdiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.PetItem;
import io.kong.mypetdiary.item.UserItem;

public class AddPostActivity extends Activity implements View.OnClickListener {

    Bitmap mBitmap;
    UserItem userItem;
    PetItem petItem;

    ImageButton btnSun, btnBlur, btnRain, btnSnow, btnUpImage;
    Button btnSave;
    EditText edTodayComment, edContent;
    TextView txtYear, txtMonth, txtDay, txtWeek, txtTodayComment;

    String stUserID, stYear, stMonth, stDay, stWeek, stWeather, stTodayComment, stContent, absolutePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        btnUpImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void saveDiary() {
        stUserID = userItem.getStUserID();
        stYear = txtYear.getText().toString();
        stMonth = txtMonth.getText().toString();
        stDay = txtDay.getText().toString();
        stTodayComment = edTodayComment.getText().toString();
        stContent = edContent.getText().toString();
    }

    private void init() {
        setContentView(R.layout.activity_addpost);
        userItem = new UserItem();
        petItem = new PetItem();

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
        btnUpImage = findViewById(R.id.btn_img_upload);

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
        Intent intent = new Intent();
        switch (view.getId()) {
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
                saveDiary();
                break;
            case R.id.btn_img_upload:
                Uri uri = Uri.parse("content://media/external/images/media");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
                break;
        }
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;

        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;

        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
            }
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) return;
        super.onActivityResult(requestCode, resultCode, intent);

        Uri selPhotoUri = intent.getData();
        String filepath = getImageFilePath(intent);

        try {
            Bitmap image = BitmapFactory.decodeFile(filepath);

            ExifInterface exif = new ExifInterface(filepath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            image = rotate(image, exifDegree);

            btnUpImage.setImageBitmap(image);
        } catch (Exception e) {
            Toast.makeText(AddPostActivity.this, "오류발생: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }


        Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
        c.moveToNext();
        absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

}
