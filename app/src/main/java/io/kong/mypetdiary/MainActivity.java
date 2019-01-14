package io.kong.mypetdiary;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int FRAGMENT_HOME = 1;
    private final int FRAGMENT_MY_PAGE = 2;

    private Button btn_home, btn_my_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_home = findViewById(R.id.btn_fragment_home);
        btn_my_page = findViewById(R.id.btn_fragment_my_page);

        btn_home.setOnClickListener(this);
        btn_my_page.setOnClickListener(this);

        callFragment(FRAGMENT_HOME);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_home:
                callFragment(FRAGMENT_HOME);
                break;
            case R.id.btn_fragment_my_page:
                callFragment(FRAGMENT_MY_PAGE);
                break;
        }
    }

    private void callFragment(int fragment_no) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment_no) {
            case 1:
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.fragment_container, homeFragment);
                transaction.commit();
                break;
            case 2:
                MyPageFragment myPageFragment = new MyPageFragment();
                transaction.replace(R.id.fragment_container, myPageFragment);
                transaction.commit();
                break;
        }
    }
}
