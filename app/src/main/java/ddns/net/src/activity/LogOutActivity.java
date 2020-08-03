package ddns.net.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

import ddns.net.src.entities.UserData;
import ddns.net.src.services.TrackingService;

public class LogOutActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private Button logoutButton;
    public static boolean isTracking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        startService();

        logoutButton.setOnClickListener((e) ->{

                String pass = passwordEditText.getText().toString();

                if(pass.equals(UserData.getPassword())){

                    stopService();
                    UserData.clear();

                    Intent start = new Intent(LogOutActivity.this, SignInActivity.class);
                    startActivity(start);
                    finish();
                }

            }
        );
    }


    private void startService(){
        System.out.println("startService");
        if(!isTracking){
            System.out.println("Started");
            Intent serviceIntent = new Intent(this, TrackingService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
            ContextCompat.startForegroundService(this, serviceIntent);
            isTracking = true;
            TrackingService.stopped.set(false);
        }
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, TrackingService.class);
        stopService(serviceIntent);
        isTracking = false;
        TrackingService.stopped.set(true);

    }

    private void initView(){
        setContentView(R.layout.activity_logout);

        passwordEditText = findViewById(R.id.logout_password);
        logoutButton = findViewById(R.id.logout_button);
    }
}
