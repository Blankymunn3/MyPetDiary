package io.kong.mypetdiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.kong.mypetdiary.R;

public class SetImageActivity extends Activity implements View.OnClickListener {

    static final int TAG_CAMERA = 2001;
    static final int TAG_GALLERY = 2002;

    Button btnClosePop, btnCameraPop, btnGalleryPop;
    ProgressDialog dialog;

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

    final int REQ_SELECT = 0;

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.btn_camera_pop:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAG_CAMERA);
                break;
            case R.id.btn_gallery_pop:
                Uri uri = Uri.parse("content://media/external/images/media");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_SELECT);
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

    String urlString;
    String absolutePath;
    int serverResponseCode;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) return;
        super.onActivityResult(requestCode, resultCode, intent);

        Uri selPhotoUri = intent.getData();

        urlString = "http://13.209.93.19:3000/api/photo";

        Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
        c.moveToNext();
        absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

        dialog = ProgressDialog.show(SetImageActivity.this, "", "Uploading file...", true);

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SetImageActivity.this, "UploadImage....", Toast.LENGTH_SHORT).show();
                    }
                });
                uploadFile(absolutePath);
            }
        }).start();
    }

    private void uploadFile(String sourceFileUri) {
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :" + absolutePath);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(SetImageActivity.this, "Source File not exist :"
                            + absolutePath, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(urlString);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);


                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()),8192);
                    final StringBuilder response = new StringBuilder();
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                dialog.dismiss();
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            dialog.dismiss();
        }
    }
}
