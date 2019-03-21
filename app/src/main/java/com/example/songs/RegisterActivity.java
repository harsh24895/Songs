package com.example.songs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


//firebase auth
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth authdb;
    EditText email,passwordd,repassword;
    Button btn;
    ProgressDialog progressDialog;

    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //to get the firebase auth
        authdb=FirebaseAuth.getInstance();

        email=findViewById(R.id.editText);
        passwordd=findViewById(R.id.password);
        repassword=findViewById(R.id.re_password);
        textView=findViewById(R.id.termstext);

        //it enable the scrolling in the textview
        textView.setMovementMethod(new ScrollingMovementMethod());

        progressDialog=new ProgressDialog(this);


    }

    public void Submit(View view) {

        //step1:get user inputs

        String emai_l=email.getText().toString();
        String password=passwordd.getText().toString();
        String re_password=repassword.getText().toString();


        //step2:validate inputs

        if(TextUtils.isEmpty(emai_l)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();


        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter the password",Toast.LENGTH_LONG).show();

        }
        if (!password.equals(re_password)){
            Toast.makeText(this,"please enter matching password",Toast.LENGTH_LONG).show();
            return;
        }



        //step3:if valid user
        progressDialog.setMessage("Createing User");
        progressDialog.show();
        authdb.createUserWithEmailAndPassword(emai_l,password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //here is a method like TASK to check the email & password
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"Successfully Register",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);


                        }else {
                            Toast.makeText(RegisterActivity.this,"Registeration Error",Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }
}
