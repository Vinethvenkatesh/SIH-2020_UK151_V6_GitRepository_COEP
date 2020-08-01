package com.example.atsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {

    EditText eds1, eds2, eds3, eds4;
    TextView t7;
    Button bt2;
    FirebaseAuth myfirebaseauth;
    FirebaseDatabase myfirebasedb;
    DatabaseReference myref;
    String smail, spass, sname, sphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        myfirebaseauth = FirebaseAuth.getInstance();
        myfirebasedb = FirebaseDatabase.getInstance();

        eds1 = findViewById(R.id.editText3);
        eds2 = findViewById(R.id.editText4);
        eds3 = findViewById(R.id.editText5);
        eds4 = findViewById(R.id.editText6);

        bt2 = findViewById(R.id.button2);
        t7 = findViewById(R.id.textView7);


        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sname = eds1.getText().toString();
                sphone = eds2.getText().toString();
                smail = eds3.getText().toString();
                spass = eds4.getText().toString();
                if (sname.isEmpty()) {
                    eds1.setError("please enter your name");
                    eds1.requestFocus();
                } else if (sphone.isEmpty()) {
                    eds2.setError("please enter your phone no");
                    eds2.requestFocus();
                } else if (smail.isEmpty()) {
                    eds3.setError("please enter your email");
                    eds3.requestFocus();
                } else if (spass.isEmpty()) {
                    eds4.setError("please enter your password");
                    eds4.requestFocus();
                } else if (spass.length() < 6) {
                    eds4.setError("password must be atleast six characters");
                    eds4.requestFocus();
                } else if (!(TextUtils.isEmpty(sname) && TextUtils.isEmpty(sphone) && TextUtils.isEmpty(smail) && TextUtils.isEmpty(spass))) {
                    myfirebaseauth.createUserWithEmailAndPassword(smail, spass).addOnCompleteListener(registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(registration.this, "verification is sent to your mail Id...", Toast.LENGTH_SHORT).show();
                                myfirebaseauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(registration.this, "registered successfully", Toast.LENGTH_SHORT).show();

                                            helperclass helpclass = new helperclass(sname,smail,sphone);
                                            myref = myfirebasedb.getReference("user");
                                            myref.setValue(helpclass);

                                            Intent intolog = new Intent(registration.this, MainActivity.class);
                                            startActivity(intolog);

                                            eds1.setText(" ");
                                            eds2.setText(" ");
                                            eds3.setText(" ");
                                            eds4.setText(" ");

                                        }else{
                                            Toast.makeText(registration.this, "error occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(registration.this, "registration unsuccessfull..pls try again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Toast.makeText(registration.this, "error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intomain = new Intent(registration.this, MainActivity.class);
                startActivity(intomain);
            }
        });
    }
}
