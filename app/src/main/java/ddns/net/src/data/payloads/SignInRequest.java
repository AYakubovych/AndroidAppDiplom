package ddns.net.src.data.payloads;

import com.google.gson.annotations.SerializedName;

public class SignInRequest {

    @SerializedName("id")
    private long id;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public SignInRequest(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
