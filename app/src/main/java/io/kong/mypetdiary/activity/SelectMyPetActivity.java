package io.kong.mypetdiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;

import io.kong.mypetdiary.R;
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
    String stUserID, stPetUrl, stPetName, stPetBirth, stPetCome, absolutePath;

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
                        uploadFile(absolutePath);
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

        if (stPetUrl.equals("null")) imgMyPet.setImageResource(R.drawable.face);
        else Glide.with(this).load(stPetUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgMyPet);

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

        Uri selPhotoUri = intent.getData();
        Glide.with(getApplicationContext()).load(selPhotoUri).into(imgMyPet);

        Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
        c.moveToNext();
        absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
    }

    @SuppressLint("LongLogTag")
    private void uploadFile(String sourceFileUri) {
        final File file = new File(sourceFileUri);

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
                    MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;
                    mainActivity.finish();
                    finish();
                    Intent intent = new Intent(SelectMyPetActivity.this, MainActivity.class);
                    intent.putExtra("TAG_FRAG", 2);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "애완동물 정보 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

}
