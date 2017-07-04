package app.com.bongdadayroi.views;

import app.com.bongdadayroi.R;

/**
 * Created by tuan on 15/03/2016.
 */
public enum EnumLayout {
    NEW("Mới cập nhật", R.layout.two_way_view),
    POPULAR1("Xem nhiều", R.layout.two_way_view),
    POPULAR2("Xem gần đây", R.layout.two_way_view),
    POPULAR3("Yêu thích", R.layout.two_way_view);
    private String title;
    private int main_layout;

    EnumLayout(String title, int main_layout) {
        this.title = title;
        this.main_layout = main_layout;
    }

    public String getTitle() {
        return title;
    }

    public int getMain_layout() {
        return main_layout;
    }
}
