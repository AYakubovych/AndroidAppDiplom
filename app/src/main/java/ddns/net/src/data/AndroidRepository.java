package ddns.net.src.data;

import ddns.net.src.properties.ApiProperties;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class AndroidRepository {
    private static AndroidRepository instance;

    private SignUpService signUpService;
    private SignInService signInService;
    private KeyGenService keyGenService;

    public static AndroidRepository getInstance(){
        if(instance == null){
            instance = new AndroidRepository();
        }
        return instance;
    }

    public AndroidRepository(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiProperties.API_URL + ":" + ApiProperties.API_PORT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        signUpService = retrofit.create(SignUpService.class);
        signInService = retrofit.create(SignInService.class);
        keyGenService = retrofit.create(KeyGenService.class);
    }

    public SignUpService getSignUpService(){
        return signUpService;
    }

    public SignInService getSignInService() {
        return signInService;
    }

    public KeyGenService getKeyGenService() {
        return keyGenService;
    }
}
