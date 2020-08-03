package ddns.net.src.data;

import ddns.net.src.data.payloads.KeyRequest;
import ddns.net.src.data.payloads.KeySendRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KeyGenService {

    @GET("key/generate/{id}")
    Call<KeyRequest> generateKey(@Path("id") long id);

    @POST("key/send")
    Call<Boolean> sendKey(@Body KeySendRequest request);

}
