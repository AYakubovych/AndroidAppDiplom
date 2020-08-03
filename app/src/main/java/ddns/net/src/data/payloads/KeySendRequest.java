package ddns.net.src.data.payloads;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KeySendRequest implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
