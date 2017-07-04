package app.com.bongdadayroi.views;

import app.com.bongdadayroi.R;

/**
 * Created by tuan on 23/02/2016.
 */
public enum CustomPagerEnum {
    RED(R.string.red, R.layout.comment),
    BLUE(R.string.blue, R.layout.relate);
    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
