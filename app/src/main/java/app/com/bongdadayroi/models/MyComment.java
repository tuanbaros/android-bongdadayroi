package app.com.bongdadayroi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuan on 05/04/2016.
 */
public class MyComment implements Serializable{

    @SerializedName("success")
    private String success;

    @SerializedName("info")
    private Comment[] info;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Comment[] getInfo() {
        return info;
    }

    public void setInfo(Comment[] info) {
        this.info = info;
    }
}
