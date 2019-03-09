package com.example.songs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RatingBar ratingBar;
    Button button;
    String artistId;
    DatabaseReference db;
    List<Track> tracks;
    ListView viewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        editText=findViewById(R.id.edit_Text);
        ratingBar=findViewById(R.id.ratingBar);
        viewAdapter=findViewById(R.id.listt);


        //to get the textView for it
        Intent intent=getIntent();
        textView=findViewById(R.id.textView);
        textView.setText(intent.getStringExtra("artistName"));
        artistId=intent.getStringExtra("artistId");

        //db connection
        /**
         * Imp part is this that the new child is created as "Track" and it is set in artist ID;
         *
         */
        db= FirebaseDatabase.getInstance().getReference("music/"+artistId+"/tracks");
        tracks=new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tracks.clear();

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        //create a new track for each document in query and add memory array
                        Track track=dataSnapshot1.getValue(Track.class);
                        tracks.add(track);

                    }
                //bind in-memory array to

                Tracklist tracklist=new Tracklist(ArtistActivity.this,tracks);
                viewAdapter.setAdapter(tracklist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addtrack(View view) {
        String trackname=editText.getText().toString().trim();
        //convert the float to int
        int rating=(int)ratingBar.getRating();
        /**
         * Declare simple validation to it
         */
        if(TextUtils.isEmpty(trackname)){
            Toast.makeText(this,"enter the name",Toast.LENGTH_LONG).show();
        }else{
            //it generate a new id in Track collection
            String id=db.push().getKey();
            Track track=new Track(id,trackname,rating);
            db.child(id).setValue(track);
            Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
            editText.setText("");
            ratingBar.setRating(0);
        }
    }
}
