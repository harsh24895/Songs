package com.example.songs;

import android.Manifest;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Spinner spinner;
    DatabaseReference db;


    TextView textViewartist;
    TextView textViewartistGenre;
    ListView listView;


    List<Atrist> atrists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.name);
        spinner=findViewById(R.id.spinner);

        listView=findViewById(R.id.listView);


        //connection for db

        db=FirebaseDatabase.getInstance().getReference("music");
        atrists=new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //create listener for data

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clear the data from list
                atrists.clear();
                for(DataSnapshot postSnapshot1:dataSnapshot.getChildren()){
                    Atrist atrist= postSnapshot1.getValue(Atrist.class);
                    atrists.add(atrist);

                    //it will create the data adapter
                    Artistlist artistAdapter=new Artistlist(MainActivity.this,atrists);
                    listView.setAdapter(artistAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void searchlist(View view){

        String artistname=editText.getText().toString();
        String artistGenre=spinner.getSelectedItem().toString();

        //to validate
        if(!TextUtils.isEmpty(artistname)) {

            //it will create object and ID first
            String artistId=db.push().getKey();
            Atrist atrist=new Atrist(artistId,artistname,artistGenre);



            //save to database
            db.child(artistId).setValue(atrist);
            editText.setText("");


        }
        else{
            Toast.makeText(this,"please enter the name",Toast.LENGTH_LONG).show();
        }
        }






}
