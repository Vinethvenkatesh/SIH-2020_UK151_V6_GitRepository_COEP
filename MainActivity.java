package com.example.atsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText ed1,ed2;
    Button bt1;
    TextView t3,t4;
    FirebaseAuth myfirebaseauth;
    FirebaseAuth.AuthStateListener mauthstatelistener;
    private  static  final int My_per_req = 0;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        myfirebaseauth.addAuthStateListener(mauthstatelistener);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myfirebaseauth = FirebaseAuth.getInstance();
        mauthstatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentuser = myfirebaseauth.getCurrentUser();
                if (currentuser != null && currentuser.isEmailVerified()) {
                    Intent inth = new Intent(MainActivity.this, home.class);
                    startActivity(inth);
                }
            }
        };


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECEIVE_SMS) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(MainActivity.this,"permissions are denied!!!", Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, My_per_req);
            }
        }



        ed1 = findViewById(R.id.editText1);
        ed2 = findViewById(R.id.editText2);
        bt1 = findViewById(R.id.button);
        t3 = findViewById(R.id.textView3);
        t4 = findViewById(R.id.textView4);



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed1.getText().toString();
                String pass = ed2.getText().toString();

                if (email.isEmpty()) {
                    ed1.setError("please enter your emailID");
                    ed1.requestFocus();
                }
                else if (pass.isEmpty()) {
                    ed2.setError("please enter your password");
                    ed2.requestFocus();
                }
                else if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
                    Toast.makeText(MainActivity.this, "Fields are empty!!!", Toast.LENGTH_SHORT).show();
                }
                else if (!(TextUtils.isEmpty(email) && TextUtils.isEmpty(pass))){
                    myfirebaseauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                if (Objects.requireNonNull(myfirebaseauth.getCurrentUser()).isEmailVerified()) {

                                    Intent intohome = new Intent(MainActivity.this, home.class);
                                    startActivity(intohome);

                                    ed1.setText(" ");
                                    ed2.setText(" ");
                                }else {
                                    Toast.makeText(MainActivity.this, "your email ID is not verified", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intoreg = new Intent(MainActivity.this, registration.class);
                startActivity(intoreg);
            }
        });

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intoreset = new Intent(MainActivity.this, resetpassword.class);
                startActivity(intoreset);
            }
        });

    }

    @Override
    public  void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case My_per_req:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, "thank you for permitting!!!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "pls give permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
