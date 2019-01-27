package io.kong.mypetdiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.kong.mypetdiary.R;

public class SetImageActivity extends Activity implements View.OnClickListener {

    static final int TAG_CAMERA = 2001;
    static final int TAG_GALLERY = 2002;

    Button btnClosePop, btnCameraPop, btnGalleryPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setimage);

        init();

    }

    private void init() {
        btnCameraPop = findViewById(R.id.btn_camera_pop);
        btnGalleryPop = findViewById(R.id.btn_gallery_pop);
        btnClosePop = findViewById(R.id.btn_close_pop);

        btnCameraPop.setOnClickListener(this);
        btnGalleryPop.setOnClickListener(this);
        btnClosePop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.btn_camera_pop:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAG_CAMERA);
                break;
            case R.id.btn_gallery_pop:
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, TAG_GALLERY);
                break;
            case R.id.btn_close_pop:
                setResult(RESULT_CANCELED, intent);
                Toast.makeText(SetImageActivity.this, " 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    private Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();

        if (config.smallestScreenWidthDp >= 600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if (config.smallestScreenWidthDp >= 400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if (config.smallestScreenWidthDp >= 360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        Bitmap bm;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAG_CAMERA:
                    bm = (Bitmap) data.getExtras().get("data");
                    bm = resize(bm);
                    intent.putExtra("bitmap", bm);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case TAG_GALLERY:
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        bm = resize(bm);
                        intent.putExtra("bitmap", bm);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(getApplicationContext(), "이미지 용량이 너무 큽니다.", Toast.LENGTH_SHORT).show();
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                default:
                    setResult(RESULT_CANCELED, intent);
                    finish();
                    break;
            }
        } else {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }
}
