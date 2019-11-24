package com.example.myapplication.ui.login;

import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogInActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private final static String LOGIN_URL = "http://10.0.2.2:8080/ayakubovych_war/android_login";
    private String id;
    private FullData fullData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullData = FullData.getFullData();
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.registration);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Tracking started " + id,
                        Toast.LENGTH_SHORT);
                toast.show();
                Intent logout = new Intent(LogInActivity.this,LogOutActivity.class);
                startActivity(logout);
                finish();
            }
        });

    }

    private void login(String userName,String pass) {
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
                        id = result;
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
            id = ur.get();

        }catch (Exception e){
            e.printStackTrace();
        }
        if(id != null && !id.equals("0")){
            fullData.setPass(pass);
            fullData.setId(Integer.parseInt(id));
        }
    }

}