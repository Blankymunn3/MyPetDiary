package io.kong.mypetdiary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.KakaoUserItem;
import io.kong.mypetdiary.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    Retrofit retrofit;
    RetrofitService retrofitService;

    KakaoUserItem kakaoUserItem;
    InputMethodManager inputMethodManager;

    String stUserID, stUserPW, stUserRePW, stUserName, stUserBirth, stUserArea;
    int kakao;

    Button btnDoubleCheck;
    Button btnSignUp;
    Button btnCancel;

    EditText edUserID;
    EditText edUserPW;
    EditText edUserRePW;
    EditText edUserName;
    TextView txtUserBirth;
    Spinner spUserArea;
    ImageButton imgBtnSelectBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();


        btnDoubleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(edUserID.getWindowToken(), 0);
                stUserID = edUserID.getText().toString();

                Call<ResponseBody> call = retrofitService.doubleCheck(stUserID);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray("user_table");
                                    if (jsonArray.length() == 0) {
                                        Toast.makeText(SignUpActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "아이디가 중복됩니다.", Toast.LENGTH_SHORT).show();
                                        edUserID.setText("");
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
        });

        imgBtnSelectBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this, litener, 2019, 0, 1);
                datePickerDialog.show();

            }
        });

        spUserArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stUserArea = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stUserID = edUserID.getText().toString();
                stUserPW = edUserPW.getText().toString();
                stUserRePW = edUserRePW.getText().toString();
                stUserName = edUserName.getText().toString();
                stUserBirth = txtUserBirth.getText().toString();


                if(!stUserID.equals("") && !stUserPW.equals("") && !stUserRePW.equals("") && !stUserName.equals("")) {
                    insertUser();
                } else {
                    Toast.makeText(SignUpActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private DatePickerDialog.OnDateSetListener litener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            if(monthOfYear < 10 && dayOfMonth < 10) {
                txtUserBirth.setText(year + "-" + 0 + monthOfYear + "-" + 0 + dayOfMonth);
            } else if(monthOfYear < 10) {
                txtUserBirth.setText(year + "-" + 0 + monthOfYear + "-" + dayOfMonth);
            } else if(dayOfMonth < 10) {
                txtUserBirth.setText(year + "-" + monthOfYear + "-" + 0 + dayOfMonth);
            }

        }
    };

    protected void init() {
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        kakaoUserItem = new KakaoUserItem();

        edUserID = findViewById(R.id.ed_id);
        edUserPW = findViewById(R.id.ed_pw);
        edUserRePW = findViewById(R.id.ed_Re_pw);
        edUserName = findViewById(R.id.ed_name);
        txtUserBirth = findViewById(R.id.txt_birth);
        spUserArea = findViewById(R.id.sp_area);
        imgBtnSelectBirth = findViewById(R.id.imgBtn_birth);

        btnDoubleCheck = findViewById(R.id.btn_doubleCheck);
        btnSignUp = findViewById(R.id.btn_signup_next);
        btnCancel = findViewById(R.id.btn_signup_cancel);

        Intent intent = getIntent();
        kakao = intent.getExtras().getInt("kakao");

        ArrayAdapter areaArrayAdapter = ArrayAdapter.createFromResource(SignUpActivity.this, R.array.user_area, android.R.layout.simple_spinner_dropdown_item);
        areaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUserArea.setAdapter(areaArrayAdapter);

        if (kakao == 1) {
            edUserID.setEnabled(false);
            edUserID.setBackgroundResource(R.drawable.background_line_disable);
            edUserID.setText(kakaoUserItem.getEmail());

            edUserPW.setEnabled(false);
            edUserPW.setBackgroundResource(R.drawable.background_line_disable);
            edUserRePW.setEnabled(false);
            edUserRePW.setBackgroundResource(R.drawable.background_line_disable);

            edUserName.setEnabled(false);
            edUserName.setBackgroundResource(R.drawable.background_line_disable);
            edUserName.setText(kakaoUserItem.getNickName());

            stUserID = kakaoUserItem.getEmail();
            stUserPW = kakaoUserItem.getUUID();
            stUserRePW = kakaoUserItem.getUUID();
            stUserName = kakaoUserItem.getNickName();
            stUserBirth = null;

            insertUser();
        }

    }

    protected void insertUser() {
        if(!stUserPW.equals(stUserRePW)) {
            Toast.makeText(SignUpActivity.this, "비밀번호를 확인해주세요..", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(SignUpActivity.this, PetSignUpActivity.class);
            intent.putExtra("user_id", stUserID);
            intent.putExtra("user_pw", stUserPW);
            intent.putExtra("user_area", stUserArea);
            intent.putExtra("user_birth", stUserBirth);
            intent.putExtra("user_name", stUserName);
            intent.putExtra("kakao", kakao);
            startActivity(intent);
        }
    }
}
