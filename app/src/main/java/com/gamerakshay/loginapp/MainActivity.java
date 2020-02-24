package com.gamerakshay.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText name,password;
    private TextView info;
    private Button login;
    private  int counter=5;
    private TextView userregister;
    private FirebaseAuth firebaseauth;
    private ProgressDialog progressDialog;
    private TextView forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText)findViewById(R.id.etname);
        forgotpassword=(TextView)findViewById(R.id.tvforgotpassword);
        password=(EditText)findViewById(R.id.etmpassword);
        info=(TextView)findViewById(R.id.tvinfo);
        login=(Button)findViewById(R.id.btlogin);
        userregister=(TextView)findViewById(R.id.tvregister);
        firebaseauth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        FirebaseUser user =firebaseauth.getCurrentUser();

        if(user!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,secondactivity.class));
        }

        userregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,registrationactivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(),password.getText().toString());
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgotpasswordactivity.class));
            }
        });
    }

    private void validate(String username,String password)
    {
        progressDialog.setMessage("logging in");
        progressDialog.show();
        firebaseauth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                   // Toast.makeText(MainActivity.this,"Log in successfull",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this,secondactivity.class));
                    checkemailverification();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Log in failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    counter--;
                    info.setText("No. of attempts remaining: "+counter);
                    if(counter==0)
                    {
                        login.setEnabled(false);
                    }
                }
            }
        });
    }
    private void checkemailverification()
    {
        FirebaseUser firebaseUser=firebaseauth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();
        if(emailflag)
        {
            startActivity(new Intent(MainActivity.this,secondactivity.class));
        }
        else
        {
            Toast.makeText(this,"Verify your email",Toast.LENGTH_SHORT).show();
            firebaseauth.signOut();
        }
    }
}
