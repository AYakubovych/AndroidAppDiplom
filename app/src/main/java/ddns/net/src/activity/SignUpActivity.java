package ddns.net.src.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication.R;
import ddns.net.src.data.AndroidRepository;
import ddns.net.src.entities.UserData;
import ddns.net.src.data.payloads.SignUpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;
    private Button signUpButton;

    private AndroidRepository androidRepository;

    public SignUpActivity(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidRepository = AndroidRepository.getInstance();
        initView();

        signUpButton.setOnClickListener((e) -> {

                String password = passwordEditText.getText().toString();
                String passwordRepeat = passwordRepeatEditText.getText().toString();

                if(password.length() > 0 && password.equals(passwordRepeat)){

                    SignUpRequest signUpRequest = new SignUpRequest();
                    signUpRequest.setFirstName(firstNameEditText.getText().toString());
                    signUpRequest.setLastName(lastNameEditText.getText().toString());
                    signUpRequest.setEmail(emailEditText.getText().toString());
                    signUpRequest.setPassword(password);

                    request(signUpRequest);

                }else {
                    Toast.makeText(getApplicationContext(),
                            "Passwords do not match!",
                            Toast.LENGTH_SHORT).show();
                }
        });
    }

    /**
     *  Making registration request.
     *  if all ok, starting new activity.
     */
    private void request(SignUpRequest signUpRequest){
        androidRepository.getSignUpService().signUp(signUpRequest).enqueue(new Callback<SignUpRequest>() {
            @Override
            public void onResponse(Call<SignUpRequest> call, Response<SignUpRequest> response) {
                System.out.println("Request successful");

                if(response.body() != null && response.body().getId() == 0){
                    Toast.makeText(getApplicationContext(),
                            "Email already in use. Try another!",
                            Toast.LENGTH_SHORT).show();
                }else{

                    UserData.setId(response.body().getId());
                    UserData.setPassword(signUpRequest.getPassword());
                    UserData.setEmail(signUpRequest.getEmail());
                    System.out.println("Changing intend");
                    Intent logout = new Intent(SignUpActivity.this, KeyGenActivity.class);
                    startActivity(logout);
                    finish();

                }

            }
            @Override
            public void onFailure(Call<SignUpRequest> call, Throwable t) {
                System.out.println("Fail to make request");
            }
        });
    }

    private void initView(){
        setContentView(R.layout.activity_sign_up);

        firstNameEditText = findViewById(R.id.signUpFirstName);
        lastNameEditText = findViewById(R.id.signUpLastName);
        emailEditText = findViewById(R.id.signUpEmail);
        passwordEditText = findViewById(R.id.signUpPassword);
        passwordRepeatEditText = findViewById(R.id.signUpPasswordRepeat);
        signUpButton = findViewById(R.id.signUpButton);
    }
}