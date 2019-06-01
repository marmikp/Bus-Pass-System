package com.blackhackweb.marmik.buspass;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

public class HomePage extends AppCompatActivity {

    EditText username,password;
    Button login,signup;
    String str,user,pass;
    CheckBox faculty,admin;
    TextView textInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login = findViewById(R.id.loginBTN);
        signup = findViewById(R.id.signupBTN);

        faculty=findViewById(R.id.faculty);
        admin = findViewById(R.id.admin);

        //Alertbox end

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (faculty.isChecked()) {
                    if (admin.isChecked()) {
                        Toast.makeText(HomePage.this, "Please Check Only One CheckBox!!", Toast.LENGTH_SHORT).show();
                    } else {

                        FacultyLoginClass facultyLoginClass = new FacultyLoginClass();
                        user = username.getText().toString();
                        pass = password.getText().toString();

                        String str = null;


                        try {
                            str = facultyLoginClass.execute().get();
                            Log.d("strReceive", str);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (str.equals("false")) {
                            Toast.makeText(HomePage.this, "Invalid Username or Password!!", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(getApplicationContext(), FacultyLogedin.class);
                            intent.putExtra("user", user);
                            intent.putExtra("id", str);
                            startActivity(intent);
                        }

                    }
                }

                else if (admin.isChecked()){
                    AdminLogin adminLogin= new AdminLogin();
                    user = username.getText().toString();
                    pass = password.getText().toString();

                    String str = null;


                    try {
                        str = adminLogin.execute().get();
                        Log.d("strReceive",str);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (str.equals("false")) {
                        Toast.makeText(HomePage.this, "Invalid Username or Password!!", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent(getApplicationContext(), AdminLogedin.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                }

                else {

                    LoginClass loginClass = new LoginClass();
                    user = username.getText().toString();
                    pass = password.getText().toString();
                    try {
                        Boolean str = loginClass.execute().get();
                        if (str) {
                            Intent intent = new Intent(getApplicationContext(), Logedin.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        } else {
                            Toast.makeText(HomePage.this, "Invalid Username or Password!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupClass.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {

        return;
    }

    class LoginClass extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            String response = new String();
            try {
                response = getString(R.string.url);
                response = response+"login.php";
                URL url=new URL(response);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                Log.d("print---","printe1");
                PrintStream ps = new PrintStream(con.getOutputStream());
                Log.d("print---","printed2");
                ps.print("&user="+ user);
                ps.print("&pass="+ pass);
                Log.d("print---","printed3");

                int responsecode = con.getResponseCode();

                if (responsecode==HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line="";
                    while ((line=bufferedReader.readLine())!=null){
                        Log.d("response",line);
                        sb.append(line);
                    }
                    str = sb.toString();
                }

                Log.d("String---->",str);
                if (str.equals("true")){
                    return true;
                }else{
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


    class FacultyLoginClass extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String response = new String();
            try {
                response = getString(R.string.url);
                response = response+"collegeLogin.php";
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
                ps.print("&user="+ user);
                Log.d("user",user);
                Log.d("pass",pass);
                ps.print("&pass="+ pass);
                Log.d("print---","printed3");

                int responsecode = con.getResponseCode();

                if (responsecode==HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line="";
                    while ((line=bufferedReader.readLine())!=null){
                        Log.d("response",line);
                        sb.append(line);
                    }
                    str = sb.toString();
                }

                String[] check = str.split(",");
                Log.d("str---",str);
                if (check[0].equals("true")){
                    return check[1];
                }else{
                    return "false";
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class AdminLogin extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String response = new String();
            try {
                response = getString(R.string.url);
                response = response+"adminLogin.php";
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
                ps.print("&username="+ user);
                Log.d("user",user);
                Log.d("pass",pass);
                ps.print("&password="+ pass);
                Log.d("print---","printed3");

                int responsecode = con.getResponseCode();

                if (responsecode==HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line="";
                    while ((line=bufferedReader.readLine())!=null){
                        Log.d("response",line);
                        sb.append(line);
                    }
                    str = sb.toString();
                }

                String[] check = str.split(",");
                Log.d("str---",str);
                if (check[0].equals("true")){
                    return check[1];
                }else{
                    return "false";
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
