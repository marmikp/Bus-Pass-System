package com.blackhackweb.marmik.buspass;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SignupClass extends AppCompatActivity {

    EditText username,password,col_id;
    Button check,submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_class);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        col_id=findViewById(R.id.col_id);
        check=findViewById(R.id.check);
        submit=findViewById(R.id.submit);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((username.getText().toString())!=null){

                CheckUsername checkUsername=new CheckUsername();
                checkUsername.execute();
                try

                    {
                        if (checkUsername.get()) {
                            username.setBackgroundColor(Color.GREEN);
                        } else {
                            username.setBackgroundColor(Color.RED);
                        }
                    } catch(
                    InterruptedException e)

                    {
                        e.printStackTrace();

                    } catch(
                    ExecutionException e)

                    {
                        e.printStackTrace();
                    }
                }
                else {
                    username.setBackgroundColor(Color.RED);
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((username.getText().toString()) != null & (password.getText().toString()) != null & (col_id.getText().toString()) != null) {
                    CheckUsername checkUsername = new CheckUsername();
                    checkUsername.execute();
                    try {
                        if (checkUsername.get()) {
                            SubmitSignup submitSignup = new SubmitSignup();
                            submitSignup.execute();
                            if (submitSignup.get()){
                                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                Toast.makeText(SignupClass.this, "Data Submitted!!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(SignupClass.this, "Data Can't Submit!", Toast.LENGTH_SHORT).show();
                            }
                            
                        } else {
                            username.setBackgroundColor(Color.RED);
                            Toast.makeText(SignupClass.this, "Please Use Other Username!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(SignupClass.this, "Please Fill All Field!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    class CheckUsername extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {

            String urls = getString(R.string.url)+"usernameCheck.php";
            try {
                URL url = new URL(urls);
                StringBuilder sb = new StringBuilder();
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);

                PrintStream ps = new PrintStream(con.getOutputStream());
                ps.print("&user="+username.getText().toString());
                String line;
                if (con.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line=br.readLine())!=null){
                        sb.append(line);
                    }
                }

                if ((sb.toString()).equals("true")){
                    return true;
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

    class SubmitSignup extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            String urls = getString(R.string.url)+"signup.php";
            try {
                URL url = new URL(urls);
                StringBuilder sb = new StringBuilder();
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);

                PrintStream ps = new PrintStream(con.getOutputStream());
                ps.print("&user="+username.getText().toString().toLowerCase());
                ps.print("&pass="+password.getText().toString());
                ps.print("&col_id="+col_id.getText().toString());
                String line;
                if (con.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line=br.readLine())!=null){
                        sb.append(line);
                    }
                }

                if ((sb.toString()).equals("true")){
                    return true;
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
