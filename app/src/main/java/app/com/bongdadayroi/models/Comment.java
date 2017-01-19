package app.com.bongdadayroi.models;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tuan on 05/04/2016.
 */
public class Comment implements Serializable{

    @SerializedName("content")
    private String content;

    @SerializedName("date")
    private String date;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("user_name")
    private String user_name;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
