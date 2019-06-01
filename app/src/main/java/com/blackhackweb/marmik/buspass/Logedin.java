package com.blackhackweb.marmik.buspass;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Logedin extends AppCompatActivity {

    String user;
    Button getDetails,apply_new,upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logedin);

        user=getIntent().getStringExtra("user");

        getDetails = findViewById(R.id.pass_detail);
        apply_new = findViewById(R.id.apply_new);
        upload = findViewById(R.id.upload);

        getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(),PassDetails.class);
                intent.putExtra("user",user);
                startActivity(intent);


            }
        });

        apply_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Application.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
