package io.kong.mypetdiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    static final int TAG_CAMERA = 2001;

    UserItem userItem;
    Bitmap mBitmap;

    RetrofitService retrofitService;
    public static SharedPreferences appData;

    ProgressDialog dialog = null;

    Button btnClosePop, btnCameraPop, btnGalleryPop;

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
        btnCameraPop = findViewById(R.id.btn_camera_pop);
        btnGalleryPop = findViewById(R.id.btn_gallery_pop);
        btnClosePop = findViewById(R.id.btn_close_pop);

        btnCameraPop.setOnClickListener(this);
        btnGalleryPop.setOnClickListener(this);
        btnClosePop.setOnClickListener(this);
    }

    final int REQ_SELECT = 0;

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.btn_camera_pop:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAG_CAMERA);
                break;
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
        String filepath = getImageFilePath(intent);

        mBitmap = BitmapFactory.decodeFile(filepath);
        mBitmap = resize(mBitmap);

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

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
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

    @SuppressLint("NewApi")
    private Bitmap resize(Bitmap bm){

        Configuration config=getResources().getConfiguration();
        if(config.smallestScreenWidthDp>=600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if(config.smallestScreenWidthDp>=400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if(config.smallestScreenWidthDp>=360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);

        return bm;

    }

    @SuppressLint("LongLogTag")
    private void uploadFile(String sourceFileUri) {
        File file = new File(sourceFileUri);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        String stUserID = userItem.getStUserID();
        Call<ResponseBody> req = retrofitService.uploadPhoto(body, name, stUserID);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if(response.code() == 200) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            downloadImage(response.body());
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                        dialog.dismiss();
                    } catch (InterruptedException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
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
    private void downloadImage(ResponseBody body){
        try {
            Log.d("DownloadImage", "Reading and writing file");

            InputStream in = null;
            FileOutputStream out = null;



            try {
                in = body.byteStream();
                String stUserImage = getExternalFilesDir(null) + File.separator + "getUserImage.jpg";
                out = new FileOutputStream(getExternalFilesDir(null) + File.separator + "getUserImage.jpg");

                userItem.setStUserProfile(stUserImage);
                SharedPreferences.Editor editor = appData.edit();
                editor.putString("user_profile", stUserImage);
                editor.apply();

                Call<ResponseBody> call =  retrofitService.profile_update(userItem.getStUserID(), stUserImage);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200) {
                            dialog.dismiss();
                            finish();
                            Intent intent = new Intent(SetImageActivity.this, MainActivity.class);
                            intent.putExtra("TAG_FRAG", 2);
                            startActivity(intent);
                        } else Toast.makeText(SetImageActivity.this, "이미지 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dialog.dismiss();
                    }
                });

                int c;
                byte[] buf = new byte[1024];

                while ((c = in.read(buf)) != -1) {
                    out.write(buf, 0, c);
                }


            }
            catch (IOException e) {
                Log.d("DownloadImage",e.toString());
            }
            finally {
                if (in != null) in.close();
                if (out != null) out.close();

            }

        } catch (IOException e) {
            dialog.dismiss();
            Log.d("DownloadImage",e.toString());
        }
    }
}
