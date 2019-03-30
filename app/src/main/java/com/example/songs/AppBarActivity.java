package com.example.songs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.net.URI;

public class AppBarActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuthdb;
    TextView textView;
    Toolbar toolbar;

    //for Image
    ImageView imageView;
    ImageButton imageButton;
    String currentPhotopath;
    static final int REQUEST_TAKE_PHOTO=1;
    static final int REQUEST_IMAGE_CAPTURE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);
    firebaseAuthdb=FirebaseAuth.getInstance();
    textView=findViewById(R.id.appId);
    textView.setText(firebaseAuthdb.getCurrentUser().getEmail());


    //imageview

        imageView=findViewById(R.id.profileImage);
        imageButton=findViewById(R.id.img_btn);

        //to allow the permission from user
        //so if not granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            imageButton.setEnabled(true);
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }




    //toolbar
    toolbar=findViewById(R.id.apppToolbar);
    setSupportActionBar(toolbar);


    }




    /*
    IF the both the permission granted by user then we enable the device for photos
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grandResults){
        if(requestCode==0){
            if(grandResults.length>0 && grandResults[0]==PackageManager.PERMISSION_GRANTED && grandResults[1]== PackageManager.PERMISSION_GRANTED){
                imageButton.setEnabled(true);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_main:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

                return true;
            case R.id.action_person:
                        return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Logout(View view) {

        firebaseAuthdb.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));

    }

    public void Camera(View view) {

        //implement the intent for camera
        Intent takeIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takeIntent.resolveActivity(getPackageManager())!=null){
            File photofile=null;
            photofile=createImageFile();

        //if we get the file back get the current location of photo
            if(photofile!=null){

                Uri photoUri= FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", photofile);

                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takeIntent,REQUEST_TAKE_PHOTO);
            }


        }


    }

    private File createImageFile() {



        return File;
    }

    public void Gallery(View view) {
    }
}
