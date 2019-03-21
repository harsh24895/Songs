package com.example.songs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AppBarActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuthdb;
    TextView textView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);
    firebaseAuthdb=FirebaseAuth.getInstance();
    textView=findViewById(R.id.appId);
    textView.setText(firebaseAuthdb.getCurrentUser().getEmail());

    //toolbar
    toolbar=findViewById(R.id.apppToolbar);
    setSupportActionBar(toolbar);


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
}
