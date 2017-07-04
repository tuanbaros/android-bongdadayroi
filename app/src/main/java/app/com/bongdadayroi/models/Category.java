package app.com.bongdadayroi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuan on 27/03/2016.
 */
public class Category implements Serializable {
    private static volatile Category category = null;

    private Category() {
    }

    public static Category getInstance() {
        if (category == null) {
            synchronized (Category.class) {
                if (category == null) {
                    category = new Category();
                }
            }
        }
        return category;
    }

    @SerializedName("data")
    private CategoryItem[] data;

    public CategoryItem[] getData() {
        return data;
    }

    public void setData(CategoryItem[] data) {
        this.data = data;
    }
}
