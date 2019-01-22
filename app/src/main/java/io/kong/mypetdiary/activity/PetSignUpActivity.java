package io.kong.mypetdiary.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetSignUpActivity extends AppCompatActivity {


    Retrofit retrofit;
    RetrofitService retrofitService;

    EditText edPetName;
    TextView txtPetBirth, txtPetCome;
    Spinner spPetKind;

    Button btnJoin, btnCancel;

    String stUserID, stUserPW, stUserName, stUserBirth, stUserArea, stPetName, stPetBirth, stPetCome, stPetKind;
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

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stPetName = edPetName.getText().toString();
                stPetBirth = txtPetBirth.getText().toString();
                stPetCome = txtPetCome.getText().toString();
                Call<ResponseBody> call_user = retrofitService.join(stUserID, stUserPW, stUserName, stUserBirth, stUserArea, kakao);
                call_user.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Call<ResponseBody> call_pet = retrofitService.pet_join(stUserID, stPetName, stPetBirth, stPetCome, stPetKind);
                            call_pet.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(PetSignUpActivity.this, stUserName + "님 회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PetSignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
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
            }
        });
    }

    private void init() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        edPetName = findViewById(R.id.ed_petName);
        txtPetBirth = findViewById(R.id.txt_petBirth);
        txtPetCome = findViewById(R.id.txt_petCome);
        spPetKind = findViewById(R.id.sp_petKind);
        btnJoin = findViewById(R.id.btn_pet_singup);
        btnCancel = findViewById(R.id.btn_pet_cancel);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.pet_kind, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPetKind.setAdapter(adapter);



        Intent intent = getIntent();
        stUserID = intent.getExtras().getString("user_id");
        stUserPW = intent.getExtras().getString("user_pw");
        stUserName = intent.getExtras().getString("user_name");
        stUserBirth = intent.getExtras().getString("user_birth");
        stUserArea = intent.getExtras().getString("user_area");
        kakao = intent.getExtras().getInt("kakao");

        Toast.makeText(PetSignUpActivity.this, stUserArea, Toast.LENGTH_SHORT).show();


        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

}
