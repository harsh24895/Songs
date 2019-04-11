package com.example.songs;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppBarActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuthdb;
    TextView textView;
    Toolbar toolbar;

    //for Image
    ImageView imageView;
    ImageButton imageButton;
    String currentPhotopath;
    static final int REQUEST_TAKE_PHOTO=1;
    static final int REQUEST_LOAD_IMAGE = 101;

    FirebaseUser user;

   // static final int REQUEST_IMAGE_CAPTURE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);
    firebaseAuthdb=FirebaseAuth.getInstance();
    user=firebaseAuthdb.getCurrentUser();

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
        //ensure that packmanager will get this action
        if(takeIntent.resolveActivity(getPackageManager())!=null){
            File photofile=null;
            try {
                photofile = createImageFile();

        //if we get the file back get the external storage  location of photo
            if(photofile!=null){

                Uri photoUri= FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", photofile);

                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takeIntent,REQUEST_TAKE_PHOTO);
            }
            }
            catch (IOException ex){
                Toast.makeText(AppBarActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        // for file with the current data and time
        String time=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

       //Check here if CAMERA directory is their in DCIM if not create it

        File strong=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Camera");

        //imp
        if(!strong.exists()){
            strong.mkdirs();
        }
        File image=File.createTempFile(time,".jpg",strong);

        //now set the path to return the image
        currentPhotopath="file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode==REQUEST_TAKE_PHOTO&&resultCode==RESULT_OK){
            Uri imageurl=Uri.parse(currentPhotopath);
           // File file=new File(imageurl.getPath());

            imageView.setImageURI(imageurl);
            saveProfile(imageurl);
        }

        //if you want load it from device and set the same image you selected as an profic picture
        else if(requestCode==REQUEST_LOAD_IMAGE&&resultCode==RESULT_OK){

            Uri selectedImage=data.getData();
            imageView.setImageURI(selectedImage);
            saveProfile(selectedImage);

        }
    }

    public void Gallery(View view) {


        //this intent will select from browse device log
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_LOAD_IMAGE);


    }


    public void saveProfile(Uri imageurl){
        FirebaseUser user=firebaseAuthdb.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(imageurl)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AppBarActivity.this, "Image saved", Toast.LENGTH_LONG).show();
                                                    }
                    }
                });
    }



    private String saveToInternalStorage(Bitmap bitmap){
        ContextWrapper cw= new ContextWrapper(getApplication());

        //Here the private directory is setup which only used in our app
        File directory=cw.getDir("imageDir", Context.MODE_PRIVATE);

        // the local file will always called "profile.jpg"  and overwrite
        File myPath=new File(directory,"profile.jpg");
        FileOutputStream fo=null;

        try{
            fo=new FileOutputStream(myPath);
        }
        catch(Exception e){
            Toast.makeText(AppBarActivity.this,"Could not save profile",Toast.LENGTH_LONG).show();

        }
        finally {
            try {
                fo.close();
            }
            catch (IOException e){
                Toast.makeText(AppBarActivity.this,"Could not save profile",Toast.LENGTH_LONG).show();

            }
        }
    return directory.getAbsolutePath();
    }


    private void LoadImageFromStorage(String path){

        try{

            //ithis will grab the imageDIr file and called profile.jpg
            File file=new File(path,"profile.jpg");

            //read it as Bitmap
            Bitmap bitmap= BitmapFactory.decodeStream(new FileInputStream(file));

            //render the image in ImageView
            imageView.setImageBitmap(bitmap);

        }
        catch (FileNotFoundException e){
            Toast.makeText(AppBarActivity.this,"Could not Load profile",Toast.LENGTH_LONG).show();

        }
    }
}
