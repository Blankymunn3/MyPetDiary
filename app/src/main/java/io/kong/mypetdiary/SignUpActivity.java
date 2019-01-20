package io.kong.mypetdiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

public class SignUpActivity extends AppCompatActivity {

    String stUserID, stUserPW, stUserRePW, stUserName, stUserYear, stUserMonth, stUserDay, stUserArea;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();
        int kakao = intent.getExtras().getInt("kakao");

        EditText edUserID = findViewById(R.id.ed_id);
        EditText edUserPW = findViewById(R.id.ed_pw);
        EditText edUserRePW = findViewById(R.id.ed_Re_pw);
        EditText edUserName = findViewById(R.id.ed_name);
        Spinner spUserYear = findViewById(R.id.sp_year);
        Spinner spUserMonth = findViewById(R.id.sp_month);
        Spinner spUserDay = findViewById(R.id.sp_day);
        Spinner spUserArea = findViewById(R.id.sp_area);


    }
}
