package io.kong.mypetdiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.fragment.MyPageFragment;
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

import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_BIRTH;
import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_COME;
import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_KIND;
import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_NAME;
import static io.kong.mypetdiary.item.MyPageListViewItem.EXTRA_PET_URL;

public class SelectMyPetActivity extends Activity {

    public static SharedPreferences appData;

    ProgressDialog dialog = null;

    Retrofit retrofit;
    RetrofitService retrofitService;

    UserItem userItem;

    ImageView imgMyPet;
    ImageButton imgBtnPetBirth, imgBtnPetCome;
    Button btnModify, btnCancel;
    EditText edMyPetName;
    TextView txtPetBirth, txtPetCome;
    Spinner spMyPetKind;

    int petKind;
    String stUserID, stPetUrl, stPetName, stPetBirth, stPetCome;

    Uri resultUri;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();


        imgMyPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Uri uri = Uri.parse("content://media/external/images/media");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        imgBtnPetBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectMyPetActivity.this, litener_birth, 2019, 0, 1);
                datePickerDialog.show();
            }
        });

        imgBtnPetCome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectMyPetActivity.this, litener_come, 2019, 0, 1);
                datePickerDialog.show();
            }
        });

        spMyPetKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                petKind = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(SelectMyPetActivity.this, "", "Uploading file...", true);
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SelectMyPetActivity.this, "UploadImage....", Toast.LENGTH_SHORT).show();
                            }
                        });
                        uploadFile();
                    }
                }).start();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private DatePickerDialog.OnDateSetListener litener_birth = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            if (monthOfYear < 10 && dayOfMonth < 10) {
                txtPetBirth.setText(year + "년" + 0 + monthOfYear + "월" + 0 + dayOfMonth + "일");
            } else if (monthOfYear < 10) {
                txtPetBirth.setText(year + "년" + 0 + monthOfYear + "월" + dayOfMonth + "일");
            } else if (dayOfMonth < 10) {
                txtPetBirth.setText(year + "년" + monthOfYear + "월" + 0 + dayOfMonth + "일");
            }

        }
    };


    private DatePickerDialog.OnDateSetListener litener_come = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            if (monthOfYear < 10 && dayOfMonth < 10) {
                txtPetCome.setText(year + "년" + 0 + monthOfYear + "월" + 0 + dayOfMonth + "일");
            } else if (monthOfYear < 10) {
                txtPetCome.setText(year + "년" + 0 + monthOfYear + "월" + dayOfMonth + "일");
            } else if (dayOfMonth < 10) {
                txtPetCome.setText(year + "년" + monthOfYear + "월" + 0 + dayOfMonth + "일");
            }

        }
    };

    private void init() {
        setContentView(R.layout.activity_selectmypet);

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        userItem = new UserItem();
        stUserID = userItem.getStUserID();

        Intent intent = getIntent();
        stPetUrl = intent.getStringExtra(EXTRA_PET_URL);
        stPetName = intent.getStringExtra(EXTRA_PET_NAME);
        stPetBirth = intent.getStringExtra(EXTRA_PET_BIRTH);
        stPetCome = intent.getStringExtra(EXTRA_PET_COME);
        petKind = intent.getIntExtra(EXTRA_PET_KIND, 0);

        imgMyPet = findViewById(R.id.imv_new_pet);
        btnModify = findViewById(R.id.btn_select_pet_modify);
        btnCancel = findViewById(R.id.btn_select_pet_cancel);
        edMyPetName = findViewById(R.id.ed_my_pet_name);
        txtPetBirth = findViewById(R.id.txt_my_pet_birth);
        txtPetCome = findViewById(R.id.txt_my_pet_come);
        spMyPetKind = findViewById(R.id.sp_my_pet_kind);
        imgBtnPetBirth = findViewById(R.id.imgBtn_my_pet_birth);
        imgBtnPetCome = findViewById(R.id.imgBtn_my_pet_come);

        if (stPetUrl.equals("null")) {
            imgMyPet.setImageResource(R.drawable.face);
        } else {
            Glide.with(this).load(stPetUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgMyPet);
            imgMyPet.setBackground(null);
        }

        edMyPetName.setText(stPetName);
        txtPetBirth.setText(stPetBirth);
        txtPetCome.setText(stPetCome);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arr_pet_kind, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        spMyPetKind.setAdapter(adapter);
        spMyPetKind.setSelection(petKind);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) return;
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 0) {
            Uri selPhotoUri = intent.getData();
            CropImage.activity(selPhotoUri)
                    .start(this);
            Glide.with(getApplicationContext()).load(selPhotoUri).into(imgMyPet);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(intent);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    Glide.with(getApplicationContext()).load(resultUri).into(imgMyPet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("ExceptionError::", error.getLocalizedMessage());
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void uploadFile() {

        Bitmap resizeBitmap = resize(this, resultUri, 1024);
        final File file = new File(saveBitmapToJpeg(this, resizeBitmap, stUserID));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        stPetName = edMyPetName.getText().toString();
        stPetBirth = txtPetBirth.getText().toString();
        stPetCome = txtPetCome.getText().toString();

        Call<ResponseBody> call = retrofitService.updateMyPet(body, name, stUserID, stPetName, stPetBirth, stPetCome, petKind);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    dialog.dismiss();
                    MyPageFragment.adapter.notifyDataSetChanged();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "애완동물 정보 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    public static String saveBitmapToJpeg(Context context, Bitmap bitmap, String name){
        File storage = context.getCacheDir();
        String fileName = name + ".jpg";
        File tempFile = new File(storage,fileName);
        try{
            tempFile.createNewFile();
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile.getAbsolutePath();
    }

    private Bitmap resize(Context context,Uri uri,int resize){
        Bitmap resizeBitmap=null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while (true) {
                if (width / 2 < resize || height / 2 < resize)
                    break;
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }

            options.inSampleSize = samplesize;
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
            resizeBitmap=bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resizeBitmap;
    }

    @Override
    public void  onDestroy(){
        super.onDestroy();

    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
