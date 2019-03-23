package io.kong.mypetdiary.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.kong.mypetdiary.R;
import io.kong.mypetdiary.item.PetItem;
import io.kong.mypetdiary.item.UserItem;
import io.kong.mypetdiary.service.RetrofitService;
import retrofit2.Retrofit;

import static io.kong.mypetdiary.service.MyPageListViewItem.EXTRA_PET_BIRTH;
import static io.kong.mypetdiary.service.MyPageListViewItem.EXTRA_PET_COME;
import static io.kong.mypetdiary.service.MyPageListViewItem.EXTRA_PET_KIND;
import static io.kong.mypetdiary.service.MyPageListViewItem.EXTRA_PET_NAME;
import static io.kong.mypetdiary.service.MyPageListViewItem.EXTRA_PET_URL;

public class SelectMyPetActivity extends Activity {

    Retrofit retrofit;
    RetrofitService retrofitService;

    UserItem userItem;
    PetItem petItem;

    ImageView imgMyPet;
    ImageButton imgBtnPetBirth, imgBtnPetCome;
    EditText edMyPetName;
    TextView txtPetBirth, txtPetCome;
    Spinner spMyPetKind;

    int petKind;
    String stPetUrl, stPetName, stPetBirth, stPetCome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

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

        Intent intent = getIntent();
        stPetUrl = intent.getStringExtra(EXTRA_PET_URL);
        stPetName = intent.getStringExtra(EXTRA_PET_NAME);
        stPetBirth = intent.getStringExtra(EXTRA_PET_BIRTH);
        stPetCome = intent.getStringExtra(EXTRA_PET_COME);
        petKind = intent.getIntExtra(EXTRA_PET_KIND, 0);

        imgMyPet = findViewById(R.id.imv_new_pet);
        edMyPetName = findViewById(R.id.ed_my_pet_name);
        txtPetBirth = findViewById(R.id.txt_my_pet_birth);
        txtPetCome = findViewById(R.id.txt_my_pet_come);
        spMyPetKind = findViewById(R.id.sp_my_pet_kind);
        imgBtnPetBirth = findViewById(R.id.imgBtn_my_pet_birth);
        imgBtnPetCome = findViewById(R.id.imgBtn_my_pet_come);

        if (stPetUrl.equals("null")) imgMyPet.setImageResource(R.drawable.face);
        else Glide.with(this).load("http://13.209.93.19:3000/download?user_id=" + stPetUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgMyPet);

        edMyPetName.setText(stPetName);
        txtPetBirth.setText(stPetBirth);
        txtPetCome.setText(stPetCome);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arr_pet_kind, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        spMyPetKind.setAdapter(adapter);
        spMyPetKind.setSelection(petKind);

        imgMyPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
