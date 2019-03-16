package io.kong.mypetdiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.UserItem;
import io.kong.mypetdiary.service.RetrofitService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SetImageActivity extends Activity implements View.OnClickListener {

    UserItem userItem;

    RetrofitService retrofitService;
    public static SharedPreferences appData;

    ProgressDialog dialog = null;

    Button btnClosePop, btnGalleryPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setimage);

        init();

    }

    private void init() {
        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient.Builder().build();
        retrofitService = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .client(client)
                .build()
                .create(RetrofitService.class);

        userItem = new UserItem();
        btnGalleryPop = findViewById(R.id.btn_gallery_pop);
        btnClosePop = findViewById(R.id.btn_close_pop);

        btnGalleryPop.setOnClickListener(this);
        btnClosePop.setOnClickListener(this);
    }

    final int REQ_SELECT = 0;

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.btn_gallery_pop:
                Uri uri = Uri.parse("content://media/external/images/media");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_SELECT);
                break;
            case R.id.btn_close_pop:
                setResult(RESULT_CANCELED, intent);
                Toast.makeText(SetImageActivity.this, " 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }


    String absolutePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) return;
        super.onActivityResult(requestCode, resultCode, intent);

        Uri selPhotoUri = intent.getData();

        Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
        c.moveToNext();
        absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

        dialog = ProgressDialog.show(SetImageActivity.this, "", "Uploading file...", true);
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SetImageActivity.this, "UploadImage....", Toast.LENGTH_SHORT).show();
                    }
                });
                uploadFile(absolutePath);
            }
        }).start();
    }

    @SuppressLint("LongLogTag")
    private void uploadFile(String sourceFileUri) {
        final File file = new File(sourceFileUri);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        String stUserID = userItem.getStUserID();
        Call<ResponseBody> req = retrofitService.uploadPhoto(body, name, stUserID);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if(response.code() == 200) {
                    userItem.setStUserProfile(file.getName());
                    SharedPreferences.Editor editor = appData.edit();
                    editor.putString("user_profile", file.getName());
                    editor.apply();

                    MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;
                    dialog.dismiss();
                    mainActivity.finish();
                    finish();
                    Intent intent = new Intent(SetImageActivity.this, MainActivity.class);
                    intent.putExtra("TAG_FRAG", 2);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "사진 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
