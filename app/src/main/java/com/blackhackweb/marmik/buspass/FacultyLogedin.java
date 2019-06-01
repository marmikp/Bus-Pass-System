package com.blackhackweb.marmik.buspass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FacultyLogedin extends AppCompatActivity {

    String user;
    Button viewProfile,approve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_logedin);

        approve = findViewById(R.id.approve);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Approve.class);
                intent.putExtra("user",getIntent().getStringExtra("user"));
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });

    }
}
