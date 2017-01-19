package app.com.bongdadayroi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuan on 28/03/2016.
 */
public class Data implements Serializable{

    @SerializedName("data")
    private MyVideo[] data;

    public MyVideo[] getData() {
        return data;
    }

    public void setData(MyVideo[] data) {
        this.data = data;
    }
}
