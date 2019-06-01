package com.blackhackweb.marmik.buspass;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class ApplicationDate extends AppCompatActivity {

    DatePicker startDate, endDate;
    Button submit;
    String distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_date);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        submit = findViewById(R.id.submit);

        final String route = getIntent().getStringExtra("origin") + " to " + getIntent().getStringExtra("destination");
        distance = getIntent().getStringExtra("distance");

        Log.d("Distance",distance);




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringBuilder builder=new StringBuilder();
                builder.append((startDate.getMonth() + 1)+"/");//month is 0 based
                builder.append(startDate.getDayOfMonth()+"/");
                builder.append(startDate.getYear());



                final StringBuilder builder1=new StringBuilder();
                builder1.append((endDate.getMonth() + 1)+"/");//month is 0 based
                builder1.append(endDate.getDayOfMonth()+"/");
                builder1.append(endDate.getYear());

                Log.d("EndDate",builder1.toString());
                Intent intent = new Intent(getApplicationContext(), Summary.class);
                intent.putExtra("distance", distance);
                intent.putExtra("startDate",builder.toString());
                intent.putExtra("endDate", builder1.toString());
                intent.putExtra("route", route);
                intent.putExtra("user", getIntent().getStringExtra("user"));
                startActivity(intent);
            }
        });

    }


}