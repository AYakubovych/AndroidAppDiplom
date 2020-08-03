package ddns.net.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import ddns.net.src.data.AndroidRepository;
import ddns.net.src.entities.UserData;
import ddns.net.src.data.payloads.SignInRequest;
import retrofit2.Call;
import retrofit2.Callback;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button createNewAccountButton;

    private AndroidRepository androidRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidRepository = AndroidRepository.getInstance();
        initView();

        createNewAccountButton.setOnClickListener((e) -> {
            Intent logout = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(logout);
        });

        signInButton.setOnClickListener((e) -> {

            signInRequest();
        });
    }

    private void signInRequest(){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        SignInRequest signInRequest = new SignInRequest();

        signInRequest.setEmail(email);
        signInRequest.setPassword(password);

        System.out.println(email + " " + password);

        androidRepository.getSignInService().signIn(signInRequest).enqueue(new Callback<SignInRequest>() {
            @Override
            public void onResponse(Call<SignInRequest> call, Response<SignInRequest> response) {
                System.out.println("Request successful");

                if( response.body() != null && response.body().getId() == 0){
                    Toast.makeText(getApplicationContext(), "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println("Id is: " + response.body().getId());

                    UserData.setId(response.body().getId());
                    UserData.setPassword(password);
                    UserData.setEmail(email);

                    Intent logout = new Intent(SignInActivity.this, LogOutActivity.class);
                    startActivity(logout);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SignInRequest> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void initView(){
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signInButton = findViewById(R.id.signIn);
        createNewAccountButton = findViewById(R.id.createNewAccount);
    }

}
