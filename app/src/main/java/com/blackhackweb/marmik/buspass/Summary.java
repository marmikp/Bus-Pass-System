package com.blackhackweb.marmik.buspass;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Summary extends AppCompatActivity {

    TextView route,validity,distance;
    CheckBox accept;
    Button submit;
    String user,distanceString,validityString,routeString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Toast.makeText(this, "Summary", Toast.LENGTH_SHORT).show();

        route = findViewById(R.id.route);
        validity = findViewById(R.id.validity);
        accept=findViewById(R.id.accept);
        submit=findViewById(R.id.Submit);
        distance = findViewById(R.id.distance);

        user = getIntent().getStringExtra("user");

        routeString = getIntent().getStringExtra("route");
        validityString = getIntent().getStringExtra("startDate")+" to "+getIntent().getStringExtra("endDate");
        distanceString = getIntent().getStringExtra("distance");


        route.setText(routeString);
        validity.setText(validityString);
        distance.setText(distanceString);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accept.isChecked()){
                    ApplyForm applyForm=new ApplyForm();
                    applyForm.execute();
                    try {
                        if (applyForm.get()){
                            Toast.makeText(Summary.this, "Application is submitted", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),Logedin.class);
                            startActivity(intent);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Summary.this, "Please Accept Tearm.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class ApplyForm extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            String urls = getString(R.string.url)+"applyForm.php";
            try {
                URL url = new URL(urls);
                StringBuilder sb = new StringBuilder();
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);

                PrintStream ps = new PrintStream(con.getOutputStream());
                String query = "INSERT INTO "+user+"(route,username,validity,co_status,ad_status,distance) VALUES ('"+routeString+"','"+user+"','"+validityString+"',0,0,"+distanceString+")";
                Log.d("query--",query);

                String query1 = "UPDATE `bus_student_login` SET `current_req`='1' WHERE username='"+user+"'";
                Log.d("query1--",query1);
                ps.print("&query1="+query1);
                ps.print("&query="+query);



                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String str = null;
                if (con.getResponseCode()==HttpURLConnection.HTTP_OK){

                    while ((str=br.readLine())!=null) {
                        sb.append(str);
                    }
                }

                if (sb.toString().equals("done")){
                    return true;
                }
                else {
                    return false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
