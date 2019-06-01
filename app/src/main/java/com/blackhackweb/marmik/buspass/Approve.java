package com.blackhackweb.marmik.buspass;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Approve extends AppCompatActivity {

    ListView listView;
    String user;
    String id;
    ArrayAdapter adapter;
    String[] toarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        listView = findViewById(R.id.listView);
        id = getIntent().getStringExtra("id");
        user = getIntent().getStringExtra("user");

        Log.d("id----",id);

        GetAprovalData getAprovalData = new GetAprovalData();
        getAprovalData.execute();
        try {
            String array = getAprovalData.get();
            toarray = array.split(",");
            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < toarray.length; ++i) {
                list.add(toarray[i]);
            }
            adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Approved.class);
                intent.putExtra("student",toarray[position]);
                intent.putExtra("user",getIntent().getStringExtra("user"));
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });



    }

    class GetAprovalData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String response;
            try {
                response = getString(R.string.url);
                response = response+"approveData.php";
                Log.d("url--",response);
                URL url=new URL(response);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                Log.d("print---","printe1");
                PrintStream ps = new PrintStream(con.getOutputStream());
                Log.d("print---","printed2");
                ps.print("&user="+ id);
                String line;
                if (con.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while((line = br.readLine())!=null){
                        sb.append(line);
                    }
                }

                Log.d("receive---",sb.toString());

                if (sb.toString()!=null){
                    return sb.toString();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
