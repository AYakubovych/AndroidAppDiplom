package ddns.net.src.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

import ddns.net.src.entities.UserData;

public class StartActivity extends AppCompatActivity {

    private static int TIME_OUT = 2500;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        runtime_permission();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(UserData.getId() > 0){
                    Intent i = new Intent(StartActivity.this, LogOutActivity.class);
                    startActivity(i);
                    finish();

                }else{
                    Intent i = new Intent(StartActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, TIME_OUT);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isLoggedIn",true);

    }

    //Ask permissions
    private boolean runtime_permission(){
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission
                        .ACCESS_FINE_LOCATION)

                !=
                PackageManager.PERMISSION_GRANTED

                &&
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                !=
                PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET},
                        100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permisions, @NonNull int[] grantResssults ){
        super.onRequestPermissionsResult(requestCode,permisions,grantResssults);
        if(requestCode == 100){
            if(grantResssults[0] == PackageManager.PERMISSION_GRANTED && grantResssults[1]
            == PackageManager.PERMISSION_GRANTED){

            }else{
                runtime_permission();
            }
        }
    }

}

