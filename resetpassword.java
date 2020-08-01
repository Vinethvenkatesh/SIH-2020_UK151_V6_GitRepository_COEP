package com.example.atsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetpassword extends AppCompatActivity {

    EditText ed8;
    Button bt6;
    String resetmail;
    FirebaseAuth myfirebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        ed8 = findViewById(R.id.editText8);
        bt6 = findViewById(R.id.button6);
        myfirebaseauth = FirebaseAuth.getInstance();

        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetmail = ed8.getText().toString();
                if(resetmail.isEmpty()){
                    Toast.makeText(resetpassword.this, "Please enter your email Id", Toast.LENGTH_SHORT).show();
                }
                else {
                    myfirebaseauth.sendPasswordResetEmail(resetmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(resetpassword.this, "password reset link is sent to your mail", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intom = new Intent(resetpassword.this, MainActivity.class);
                                intom.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intom);
                            }
                            else{
                                Toast.makeText(resetpassword.this, "your email is not yet registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
