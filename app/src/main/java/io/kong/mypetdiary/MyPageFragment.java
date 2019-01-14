package io.kong.mypetdiary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;


public class MyPageFragment extends Fragment {

    private KakaoUserItem kakaoUserItem;
    TextView txtMyPageName;
    String getImageUrl;

    Handler handler = new Handler();

    public MyPageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_my_page, container, false);
        kakaoUserItem = new KakaoUserItem();
        getImageUrl = kakaoUserItem.getProfileImagePath();

        final TextView txtMyPageName = rootView.findViewById(R.id.txt_my_page_name);
        final ImageView imvMyPageUser = rootView.findViewById(R.id.imv_my_page_user);

        final String stNickName = kakaoUserItem.getNickName();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(kakaoUserItem.getProfileImagePath());
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imvMyPageUser.setImageBitmap(bm);
                        }
                    });
                    txtMyPageName.setText(stNickName);
                    imvMyPageUser.setImageBitmap(bm);

                } catch (Exception e) {

                }
            }
        });

        thread.start();

        return rootView;
    }

}
