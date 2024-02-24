package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {
    private EditText edtPassword,edtEmail,edtUsername;
    private Button btnSubmit;
    private TextView txtLogininfo;
    private boolean isSigningUp = true ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtLogininfo = findViewById(R.id.txtLogininfo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            JobScheduler jobScheduler = getSystemService(JobScheduler.class);
            ComponentName componentName = new ComponentName(this, AndroidSchedular.class);
            JobInfo.Builder info = new JobInfo.Builder(123, componentName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                info.setRequiresBatteryNotLow(true);
            }
            info.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
            info.setPeriodic(30*60*1000);
            if (jobScheduler!=null){
                int result = jobScheduler.schedule(info.build());
                if (result==JobScheduler.RESULT_SUCCESS){
                    Toast.makeText(this, "database went online", Toast.LENGTH_SHORT).show();
                }


                }
            }

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,Friends.class));
            finish();
        }








        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                    if (isSigningUp && edtUsername.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this,"invalid input",Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
                if(isSigningUp){
                    handleSIgnup();
                }else{
                    handleLogin();
                }

            }
        });

       txtLogininfo.setOnClickListener(new  View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(isSigningUp){
                   isSigningUp = false ;
                   edtUsername.setVisibility(View.GONE);

                   btnSubmit.setText("Log in");
                   txtLogininfo.setText("Dont have an account ? sign up ");


               }else{
                   isSigningUp = true ;


                   edtUsername.setVisibility(View.VISIBLE);
                   btnSubmit.setText("SIGN UP");
                   txtLogininfo.setText("Already Have an account ? Log in ");

           }
       }

    });



    }
    public void handleSIgnup(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(edtUsername.getText().toString(),edtEmail.getText().toString(),""));
                    startActivity(new Intent(MainActivity.this,Friends.class));
                    Toast.makeText(MainActivity.this,"Sign up successfully",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void handleLogin(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,Friends.class));
                    Toast.makeText(MainActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}

