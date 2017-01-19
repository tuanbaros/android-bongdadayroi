package app.com.bongdadayroi.models;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tuan on 02/04/2016.
 */
public class MyAccount implements Serializable {

    @SerializedName("success")
    private String success;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("user_id")
    private String user_id;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
