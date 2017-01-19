package app.com.bongdadayroi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuan on 27/03/2016.
 */
public class Test implements Serializable{

    @SerializedName("data")
    private CategoryItem[] data;

    public CategoryItem[] getData() {
        return data;
    }

    public void setData(CategoryItem[] data) {
        this.data = data;
    }
}
