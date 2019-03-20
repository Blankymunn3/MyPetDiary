package io.kong.mypetdiary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.SaveUserInfo;
import io.kong.mypetdiary.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetSignUpActivity extends AppCompatActivity {

    SignUpActivity signUpActivity;

    public static SharedPreferences appData;

    Retrofit retrofit;
    RetrofitService retrofitService;

    EditText edPetName;
    TextView txtPetBirth, txtPetCome;
    Spinner spPetKind;

    Button btnJoin, btnCancel;
    ImageButton imgBtnPetBirth, imgBtnPetCome;

    String stUserID, stUserPW, stUserSalt, stUserName, stUserBirth, stUserProfile, stUserArea, stPetName, stPetBirth, stPetCome, stPetKind;
    int kakao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_signup);

        init();

        spPetKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stPetKind = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgBtnPetBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(PetSignUpActivity.this, litener_birth, 2019, 0, 1);
                datePickerDialog.show();
            }
        });

        imgBtnPetCome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PetSignUpActivity.this, litener_come, 2019, 0, 1);
                datePickerDialog.show();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stPetName = edPetName.getText().toString();
                stPetBirth = txtPetBirth.getText().toString();
                stPetCome = txtPetCome.getText().toString();
                Call<ResponseBody> call_user = retrofitService.join(stUserID, stUserPW, stUserSalt, stUserName, stUserBirth, stUserProfile, stUserArea, kakao);
                call_user.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Call<ResponseBody> call_pet = retrofitService.pet_join(stUserID, stPetName, stPetBirth, stPetCome, stPetKind);
                            call_pet.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {

                                        SaveUserInfo.saveUserInfo(appData, true, stUserID, stUserPW, stUserSalt, stUserName, stUserBirth, stUserProfile,
                                                stUserArea, stPetName, stPetBirth, stPetCome, stPetKind);
                                        Toast.makeText(PetSignUpActivity.this, stUserName + "님 회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PetSignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        signUpActivity.finish();
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(PetSignUpActivity.this, "Pet error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(PetSignUpActivity.this, "User error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kakao == 1) {
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Intent intent = new Intent(PetSignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Intent intent = new Intent(PetSignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
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
        signUpActivity = (SignUpActivity) SignUpActivity.signUpActivity;

        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        edPetName = findViewById(R.id.ed_petName);
        txtPetBirth = findViewById(R.id.txt_petBirth);
        txtPetCome = findViewById(R.id.txt_petCome);
        spPetKind = findViewById(R.id.sp_petKind);
        btnJoin = findViewById(R.id.btn_pet_singup);
        btnCancel = findViewById(R.id.btn_pet_cancel);
        imgBtnPetBirth = findViewById(R.id.imgBtn_petBirth);
        imgBtnPetCome = findViewById(R.id.imgBtn_petCome);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.pet_kind, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPetKind.setAdapter(adapter);


        Intent intent = getIntent();
        stUserID = intent.getExtras().getString("user_id");
        stUserPW = intent.getExtras().getString("user_pw");
        stUserSalt = intent.getExtras().getString("user_salt");
        stUserName = intent.getExtras().getString("user_name");
        stUserBirth = intent.getExtras().getString("user_birth");
        stUserArea = intent.getExtras().getString("user_area");
        stUserProfile = intent.getExtras().getString("user_profile");
        kakao = intent.getExtras().getInt("kakao");

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

    }

}
