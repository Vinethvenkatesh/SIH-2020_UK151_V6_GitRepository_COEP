package com.example.atsproject;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CompoundButton;
    import android.widget.Switch;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;


    public class home extends AppCompatActivity {

        Switch s1,s2;
        Button bt3,bt4;
        FirebaseDatabase myfirebasedb;
        FirebaseAuth myfirebaseauth;
        FirebaseAuth.AuthStateListener mauthstatelistener;
        DatabaseReference myref;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            myfirebasedb = FirebaseDatabase.getInstance();
            myfirebaseauth = FirebaseAuth.getInstance();

            s1 = findViewById(R.id.switch1);
            s2 = findViewById(R.id.switch2);
            bt3 = findViewById(R.id.button3);
            bt4 = findViewById(R.id.button4);


            s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        myref = myfirebasedb.getReference("command");
                        myref.setValue("1");
                    }else{
                        myref = myfirebasedb.getReference("command");
                        myref.setValue("2");
                    }
                }
            });

            s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        myref = myfirebasedb.getReference("command");
                        myref.setValue("3");
                    }else{
                        myref = myfirebasedb.getReference("command");
                        myref.setValue("4");
                    }
                }
            });

            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myref = myfirebasedb.getReference("command");
                    myref.setValue("5");
                }

            });

            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myref = myfirebasedb.getReference("command");
                    myref.setValue("6");
                    Intent intogeo = new Intent(home.this, mapact.class);
                    startActivity(intogeo);
                }
            });


        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.logout:{
                   myfirebaseauth.signOut();
                   finish();
                   FirebaseUser currentuser = myfirebaseauth.getCurrentUser();
                   if(currentuser == null){
                       Intent intolog = new Intent(home.this, MainActivity.class);
                       intolog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intolog);
                   }
                }

            }
            return super.onOptionsItemSelected(item);
        }

    }
