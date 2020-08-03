package ddns.net.src.data;

import ddns.net.src.data.payloads.SignInRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInService {

    @POST("signin")
    Call<SignInRequest> signIn(@Body SignInRequest signInRequestRequest);
}
