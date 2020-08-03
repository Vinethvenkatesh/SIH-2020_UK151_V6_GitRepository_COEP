package com.example.atsproject;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;

    import android.Manifest;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CompoundButton;
    import android.widget.Switch;
    import android.widget.Toast;

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
        private static final int My_per_req = 0;


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

                    if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.RECEIVE_SMS)
                            + ContextCompat.checkSelfPermission(home.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(home.this, Manifest.permission.RECEIVE_SMS) &&
                                ActivityCompat.shouldShowRequestPermissionRationale(home.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                            Toast.makeText(home.this,"permissions are denied..pls give permission", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, My_per_req);
                        }
                        else{
                            ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, My_per_req);
                        }
                    }
                    else{
                        Intent intomp = new Intent(home.this, mapact.class);
                        startActivity(intomp);
                    }
                }
            });


        }
        @Override
        public  void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
            switch(requestCode){
                case My_per_req:{
                    if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(home.this, "thank you for permitting!!!", Toast.LENGTH_LONG).show();
                        Intent intomap = new Intent(home.this, mapact.class);
                        startActivity(intomap);
                    }
                    else{
                        Toast.makeText(home.this, "pls give permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
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
                       Intent intomain = new Intent(home.this, MainActivity.class);
                       intomain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intomain);
                   }
                }

            }
            return super.onOptionsItemSelected(item);
        }

    }
