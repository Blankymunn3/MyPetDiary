package io.kong.mypetdiary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.KakaoUserItem;

public class SignUpActivity extends AppCompatActivity {

    KakaoUserItem kakaoUserItem;

    String stUserID, stUserPW, stUserRePW, stUserName, stUserBirth, stUserArea;

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

        imgBtnSelectBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this, litener, 2019, 0, 1);
                datePickerDialog.show();

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

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));

        kakaoUserItem = new KakaoUserItem();

        edUserID = findViewById(R.id.ed_id);
        edUserPW = findViewById(R.id.ed_pw);
        edUserRePW = findViewById(R.id.ed_Re_pw);
        edUserName = findViewById(R.id.ed_name);
        txtUserBirth = findViewById(R.id.txt_birth);
        spUserArea = findViewById(R.id.sp_area);
        imgBtnSelectBirth = findViewById(R.id.imgBtn_birth);

        btnDoubleCheck = findViewById(R.id.btn_doubleCheck);
        btnSignUp = findViewById(R.id.btn_singup);
        btnCancel = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        int kakao = intent.getExtras().getInt("kakao");



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
        }

        ArrayAdapter areaArrayAdapter = ArrayAdapter.createFromResource(SignUpActivity.this, R.array.user_area, android.R.layout.simple_spinner_dropdown_item);
        areaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUserArea.setAdapter(areaArrayAdapter);
    }
}
