package com.example.myapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class LogOutActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private Button logoutButton;
    private FullData fullData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullData = FullData.getFullData();

        Intent intent = new Intent(LogOutActivity.this,TrackingService.class);
        startService(intent);

        setContentView(R.layout.activity_logout);

        passwordEditText = findViewById(R.id.logout_password);
        logoutButton = findViewById(R.id.logout_button);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passwordEditText.getText().toString();
                System.out.println("asd");
                if(pass.equals(fullData.getPass())){
                    Intent intent = new Intent(LogOutActivity.this,TrackingService.class);
                    stopService(intent);
                    Intent start = new Intent(LogOutActivity.this, StartActivity.class);
                    startActivity(start);
                    finish();
                    System.out.println("asd");
                }

            }
        });


    }
}
