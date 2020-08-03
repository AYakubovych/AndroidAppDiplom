package ddns.net.src.data.payloads;

import com.google.gson.annotations.SerializedName;

public class KeyRequest {
    @SerializedName("key")
    private String key;

    public KeyRequest(){}

    public KeyRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
