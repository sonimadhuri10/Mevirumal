package com.webbeansvipul.psstore.Madhu;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.webbeansvipul.psstore.R;
import com.webbeansvipul.psstore.api.ApiClient;
import com.webbeansvipul.psstore.api.PSApiService;

import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llCamera , llGallery ;
    Button btnUpload;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    ProgressDialog pd ;
    PSApiService apiInterface ;
    ImageView imgPres ;
    String  encImage= "" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_layout);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Prescription");

        apiInterface = ApiClient.getInstance().create(PSApiService.class);


        llCamera = (LinearLayout)findViewById(R.id.llCamera);
        llGallery = (LinearLayout)findViewById(R.id.llGallery);
        btnUpload = (Button)findViewById(R.id.btnUpload);
        imgPres = (ImageView)findViewById(R.id.imgPrescription);

        btnUpload.setOnClickListener(this);
        llCamera.setOnClickListener(this);
        llGallery.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
      switch (view.getId()){
              case R.id.llCamera:
                  if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                  {
                      requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                  }
                  else
                  {
                      Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                      startActivityForResult(cameraIntent, CAMERA_REQUEST);
                  }
                  break;
              case R.id.llGallery:
                  Intent i = new Intent(
                          Intent.ACTION_PICK,
                          android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  startActivityForResult(i, RESULT_LOAD_IMAGE);
                  break;
              case R.id.btnUpload:
                    if(encImage.equals("")){
                        Toast.makeText(this, "Please capture or select your prescription", Toast.LENGTH_LONG).show();
                    }else{
                        updateProfile(encImage,"1","1","1");
                    }
                  break;
              default:

                  break;

      }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if  (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                encImage = picturePath ;
                cursor.close();


                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage1 = BitmapFactory.decodeStream(imageStream);
                imgPres.setVisibility(View.VISIBLE);
                imgPres.setImageBitmap(selectedImage1);



            }else  if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
            {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                encImage = picturePath ;
                cursor.close();


                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage1 = BitmapFactory.decodeStream(imageStream);
                imgPres.setVisibility(View.VISIBLE);
                imgPres.setImageBitmap(selectedImage1);


            }

        } catch (Exception e) {
            Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void updateProfile(String photo ,String pescription, String product_ids, String days) {
        try {

            RequestBody requestBody ;
            MultipartBody.Part fileToUpload ;
            RequestBody filename ;

            if(photo.equals("")){
                fileToUpload = null;
                filename = null ;
            }else{
                File file = new File(photo);
                requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            }

            RequestBody pre1 = RequestBody.create(MediaType.parse("text/plain"), pescription);
            RequestBody ids = RequestBody.create(MediaType.parse("text/plain"), product_ids);
            RequestBody day = RequestBody.create(MediaType.parse("text/plain"), days);

            pd.show();
            Call<LabModel> call = apiInterface.getUploAdPres(fileToUpload,filename,pre1,ids,day);
            call.enqueue(new Callback<LabModel>() {
                @Override
                public void onResponse(Call<LabModel> call, retrofit2.Response<LabModel> response) {
                    LabModel resource = response.body();
                    pd.dismiss();

                    if (resource.status.equalsIgnoreCase("Success")) {
                        Toast.makeText(UploadActivity.this, "Prescription Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UploadActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LabModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(UploadActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });

        }catch (Exception e){
            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
