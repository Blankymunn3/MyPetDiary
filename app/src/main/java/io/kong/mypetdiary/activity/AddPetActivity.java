package io.kong.mypetdiary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import io.kong.mypetdiary.R;

public class AddPetActivity extends Activity {

    EditText edPetName;
    TextView txtPetBirth, txtPetCome;
    Spinner spPetKind;
    ImageButton imgBtnPetBirth, imgBtnPetCome;

    String stPetName, stPetBirth, stPetCome, stPetKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }

    private void init() {
        setContentView(R.layout.activity_addpet);

        edPetName = findViewById(R.id.ed_new_pet_name);
        txtPetBirth = findViewById(R.id.txt_new_pet_birth);
        txtPetCome = findViewById(R.id.txt_new_pet_come);
        spPetKind = findViewById(R.id.sp_new_pet_kind);
        imgBtnPetBirth = findViewById(R.id.imgBtn_new_pet_birth);
        imgBtnPetCome = findViewById(R.id.imgBtn_new_pet_come);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arr_pet_kind, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        spPetKind.setAdapter(adapter);
    }
}
