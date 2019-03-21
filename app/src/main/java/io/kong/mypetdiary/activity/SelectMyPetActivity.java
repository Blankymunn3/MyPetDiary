package io.kong.mypetdiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
    TextView txtPetName, txtPetBirth, txtPetCome, txtPetKind;

    String stPetUrl, stPetName, stPetBirth, stPetCome, stPetKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }

    private void init() {
        setContentView(R.layout.activity_selectmypet);

        Intent intent = getIntent();
        stPetUrl = intent.getStringExtra(EXTRA_PET_URL);
        stPetName = intent.getStringExtra(EXTRA_PET_NAME);
        stPetBirth = intent.getStringExtra(EXTRA_PET_BIRTH);
        stPetCome = intent.getStringExtra(EXTRA_PET_COME);
        stPetKind = intent.getStringExtra(EXTRA_PET_KIND);

        imgMyPet = findViewById(R.id.imv_new_pet);
        txtPetName = findViewById(R.id.txt_select_pet_name);
        txtPetBirth = findViewById(R.id.txt_select_pet_birth);
        txtPetCome = findViewById(R.id.txt_select_pet_come);
        txtPetKind = findViewById(R.id.txt_select_pet_kind);

        txtPetName.setText(stPetName);
        txtPetBirth.setText(stPetBirth);
        txtPetCome.setText(stPetCome);
        txtPetKind.setText(stPetKind);

    }

}
