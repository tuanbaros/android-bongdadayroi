package app.com.bongdadayroi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nguyen Thanh Tuan on 02/06/2016.
 */
public class ListVideo implements Serializable {
    @SerializedName("link")
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
