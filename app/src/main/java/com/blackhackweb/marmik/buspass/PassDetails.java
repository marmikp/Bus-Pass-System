package com.blackhackweb.marmik.buspass;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PassDetails extends AppCompatActivity {

    TextView name,route,validity,pass_status;
    ImageView image;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_details);
        user = getIntent().getStringExtra("user");
        name = findViewById(R.id.name);
        route = findViewById(R.id.route);
        validity = findViewById(R.id.validity);
        pass_status = findViewById(R.id.pass_status);

        image = findViewById(R.id.image);

        PassFetch passFetch = new PassFetch();
        passFetch.execute();
        try {
            String string = passFetch.get().toString();
            String str[] = string.split(",");
            name.setText("Name: "+str[0]);
            route.setText("Route: "+str[1]);
            validity.setText("Validity: "+str[2]);

            if (str.length==4){
                Picasso.with(this).load(getString(R.string.url)+"upload/"+str[3]).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
            }

            String splitStr[] = str[2].split(" ");
            Date date = new Date(splitStr[2]);

            Log.d("date is:",date.toString());

            //SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date dateComp = new Date();
            Log.d("datecomp is:",dateComp.toString());

            if (date.before(dateComp) || date.equals(dateComp)){
                pass_status.setText("Pass Is Not Valid");
                pass_status.setTextColor(Color.RED);
            }
            else{
                pass_status.setText("Pass Is Valid");
                pass_status.setTextColor(Color.GREEN);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //PassFetch passFetch =



    }

    class PassFetch extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {

            String url = getString(R.string.url) + "passFetch.php";

            try {
                URL url1 = new URL(url);
                HttpURLConnection con = (HttpURLConnection) url1.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                PrintStream ps = new PrintStream(con.getOutputStream());
                ps.print("&username=" + user);

                StringBuilder sb = new StringBuilder();
                String line = "";
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }

                String str = sb.toString();

                Log.d("PassString:",str);

                return str;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
