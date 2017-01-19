package app.com.bongdadayroi.models;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tuan on 27/03/2016.
 */
public class CategoryItem implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
