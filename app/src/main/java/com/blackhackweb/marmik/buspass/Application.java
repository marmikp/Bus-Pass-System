package com.blackhackweb.marmik.buspass;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class Application extends AppCompatActivity {

    Button submit;
    Spinner district,district_to,taluka,taluka_to,vilage,vilage_to;


    String[] country = { "District","Mehsana"};
    String[] Mehsana={"Taluka","mehsana","visnagar","unjha","becharaji","kadi","kheralu","satlasana","vadnagar","vijapur"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        submit=findViewById(R.id.Submit);
        district=findViewById(R.id.district);
        district_to=findViewById(R.id.district_to);
        taluka=findViewById(R.id.taluka);
        taluka_to=findViewById(R.id.taluka_to);
        vilage=findViewById(R.id.vilage);
        vilage_to=findViewById(R.id.vilage_to);

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,country);
        district.setAdapter(arrayAdapter);

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((district.getSelectedItem().toString())!="District"){
                    Toast.makeText(Application.this, district.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                    ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,Mehsana);
                    taluka.setAdapter(aa);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        taluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((taluka.getSelectedItem().toString())!="Taluka"){
                    GetVilage getVilage = new GetVilage();
                    getVilage.execute(taluka.getSelectedItem().toString());
                    try {
                        String[] arrayDistrict = getVilage.get();
                        ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayDistrict);
                        vilage.setAdapter(aa);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final ArrayAdapter<String> arrayAdapter_to=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,country);
        district_to.setAdapter(arrayAdapter_to);

        district_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((district_to.getSelectedItem().toString())!="District"){
                    Toast.makeText(Application.this, district_to.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                    ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,Mehsana);
                    taluka_to.setAdapter(aa);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        taluka_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((taluka_to.getSelectedItem().toString())!="Taluka"){
                    GetVilage getVilage = new GetVilage();
                    getVilage.execute(taluka_to.getSelectedItem().toString());
                    try {
                        String[] arrayDistrict = getVilage.get();
                        ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayDistrict);
                        vilage_to.setAdapter(aa);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ApplicationDate.class);
                GetDistance getDistance = new GetDistance();
                getDistance.execute(vilage.getSelectedItem().toString(),vilage_to.getSelectedItem().toString());
                try {
                    String distance = getDistance.get();
                    intent.putExtra("distance",distance);
                    intent.putExtra("origin",vilage.getSelectedItem().toString());
                    intent.putExtra("destination",vilage_to.getSelectedItem().toString());
                    intent.putExtra("user",getIntent().getStringExtra("user"));
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

    }

    class GetVilage extends AsyncTask<String,Void,String[]>{


        @Override
        protected String[] doInBackground(String... strings) {
            String urls = getString(R.string.url)+"getVilage.php";
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(urls);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                String sb = new String();
                con.setDoInput(true);
                con.setDoOutput(true);

                PrintStream ps = new PrintStream(con.getOutputStream());
                String passValue = strings[0];
                ps.print("&string="+ passValue);
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String receiveLine;
                int i=0;
                if (con.getResponseCode()==HttpURLConnection.HTTP_OK){
                    while((receiveLine=br.readLine())!=null){
                        stringBuilder = stringBuilder.append(receiveLine);
                    }
                }

                sb = stringBuilder.toString();
                String[] words=sb.split(",");


                return words;

            } catch (MalformedURLException e) {
                Log.d("exception","1");
            } catch (IOException e) {
                Log.d("exception","1");
                e.printStackTrace();
            }

            return new String[0];
        }
    }

    public class GetDistance extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String origin = strings[0];
            String distance = strings[1];

            String url = "http://www.marmik.tk/distance.php?origin=" + origin + "&distance=" + distance;
            Log.d("url",url);

            try {
                URL url1 = new URL(url);
                HttpURLConnection con = (HttpURLConnection) url1.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }

                String x;
                x = sb.toString();
                Log.d("Distance", String.valueOf(x));

                return x;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
