package ddns.net.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import ddns.net.src.data.AndroidRepository;
import ddns.net.src.data.payloads.KeyRequest;
import ddns.net.src.data.payloads.KeySendRequest;
import ddns.net.src.entities.UserData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeyGenActivity extends AppCompatActivity {

    private AndroidRepository androidRepository;

    private TextView keyField;
    private EditText emailField;
    private Button sendButton;
    private Button nextButton;


    public KeyGenActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keygen);

        androidRepository = AndroidRepository.getInstance();

        initView();
        keyRequest();
        //SendButton click
        sendButton.setOnClickListener((e) -> {
                sendKey();
        });
        //NextButton click
        nextButton.setOnClickListener((e) -> {
                Intent logout = new Intent(KeyGenActivity.this, LogOutActivity.class);
                startActivity(logout);
                finish();
        });

    }

    private void keyRequest(){
        androidRepository.getKeyGenService().generateKey(UserData.getId()).enqueue(new Callback<KeyRequest>() {
            @Override
            public void onResponse(Call<KeyRequest> call, Response<KeyRequest> response) {

                System.out.println("Request ok. Key is: " + response.body().getKey());
                //Changing fields
                keyField.setText(response.body().getKey());
                UserData.setKey(response.body().getKey());
            }

            @Override
            public void onFailure(Call<KeyRequest> call, Throwable t) {
                System.out.println("Request failed " + t);
            }
        });
    }

    private void sendKey(){

        KeySendRequest request = new KeySendRequest();
        request.setId(UserData.getId());
        request.setEmail(emailField.getText().toString());
        request.setPassword(UserData.getPassword());

        System.out.println(UserData.getId() + " " + UserData.getEmail() + " " + UserData.getPassword());

        androidRepository.getKeyGenService().sendKey(request).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()){
                    Toast.makeText(getApplicationContext(),
                            "Email sended!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Sorry, email not sended!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("Fail");
            }
        });
    }

    private void initView(){
        keyField = findViewById(R.id.key_field);
        emailField = findViewById(R.id.email_field);
        sendButton = findViewById(R.id.send_to_email_button);
        nextButton = findViewById(R.id.next_button);
    }

}
