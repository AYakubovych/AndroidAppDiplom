package ddns.net.src.data;

import ddns.net.src.data.payloads.SignUpRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUpService {

    @POST("signup")
    Call<SignUpRequest> signUp(@Body SignUpRequest signUpRequest);

}
