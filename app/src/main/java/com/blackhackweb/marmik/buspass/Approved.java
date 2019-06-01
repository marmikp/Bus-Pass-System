package com.blackhackweb.marmik.buspass;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class Approved extends AppCompatActivity {

    TextView name, route, enrollment, validity, distance;
    Button approve, unapprove;
    String student,id,user;
    String toString[],image;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved);

        name = findViewById(R.id.name);
        route = findViewById(R.id.route);
        enrollment = findViewById(R.id.enrollment);
        validity = findViewById(R.id.validity);
        distance = findViewById(R.id.distance);
        approve = findViewById(R.id.approve);
        unapprove = findViewById(R.id.unapprove);
        imageView=findViewById(R.id.image);

        student = getIntent().getStringExtra("student");
        id = getIntent().getStringExtra("id");
        user = getIntent().getStringExtra("user");

        ApprovedDataFetch approvedDataFetch = new ApprovedDataFetch();
        approvedDataFetch.execute();
        try {
            String received = approvedDataFetch.get();
            toString = received.split(",");

            name.setText("Name: " + toString[1]);
            route.setText("Route: " + toString[2]);
            validity.setText("Validity: " + toString[3]);
            distance.setText("Distance: " + toString[6]);


            if (toString.length==9) {
                image = toString[8];

                Log.d("image", getString(R.string.url) + "upload/" + image);


                Picasso.with(this).load(getString(R.string.url) + "upload/" + image).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Approve1 approve1 = new Approve1();
                approve1.execute();
                try {
                    Boolean response = approve1.get();
                    if (response){
                        Intent intent = new Intent(getApplicationContext(),Approve.class);
                        intent.putExtra("user",getIntent().getStringExtra("user"));
                        intent.putExtra("id",getIntent().getStringExtra("id"));
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    class ApprovedDataFetch extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String str = getString(R.string.url) + "approvedDataFetch.php";
            try {
                URL url = new URL(str);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                Log.d("print---", "printe1");
                PrintStream ps = new PrintStream(con.getOutputStream());
                Log.d("print---", "printed2");
                ps.print("&student=" + student);
                String line;
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }

                String finalString = sb.toString();
                Log.d("finalString--", finalString);
                return finalString;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class Approve1 extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            String str = getString(R.string.url) + "approveCollege.php";
            try {
                URL url = new URL(str);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                Log.d("print---", "printe1");
                PrintStream ps = new PrintStream(con.getOutputStream());
                Log.d("print---", "printed2");
                ps.print("&student=" + student);
                String line;
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }

                String finalString = sb.toString();
                if (finalString.equals("done")) {
                    return true;
                } else {
                    return false;
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
