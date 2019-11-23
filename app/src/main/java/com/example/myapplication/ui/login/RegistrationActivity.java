package com.example.myapplication.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private final static String LOGIN_URL = "http:/10.0.2.2:8080/ayakubovych_war/android_registration";  // login url
    private int id;
    private FullData fullData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullData = FullData.getFullData();

        setContentView(R.layout.activity_registration);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.registration);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 0;
                registration(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
                if(id >0){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Tracking started" + id ,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                    Intent logout = new Intent(RegistrationActivity.this,LogOutActivity.class);
                    startActivity(logout);
                    finish();

                    //background tracking here
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error, no id returned" ,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }

    private void registration(String userName,String pass) {
        String urlssufix = "?username=" + userName + "&pass=" + pass;

        class RegisterUser extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(LOGIN_URL + s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferedReader.readLine();
                    if(!result.equals("NULL")){
                        return result;
                    }else{
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
                return null;
            }
        }

        RegisterUser ur = new RegisterUser();

        ur.execute(urlssufix);

        try{
           id = Integer.parseInt(ur.get());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(id != 0){
            fullData.setId(id);
            fullData.setPass(pass);
        }
    }

}