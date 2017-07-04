package app.com.bongdadayroi.views;

import android.content.Context;
import android.util.AttributeSet;

import com.androidnetworking.widget.ANImageView;

/**
 * Created by tuan on 24/03/2016.
 */
public class MyImage extends ANImageView {
    private int myWidth, myHeight;

    public int getMyWidth() {
        return myWidth;
    }

    public void setMyWidth(int myWidth) {
        this.myWidth = myWidth;
    }

    public int getMyHeight() {
        return myHeight;
    }

    public void setMyHeight(int myHeight) {
        this.myHeight = myHeight;
    }

    public void setSize() {
        measure(myWidth, myHeight);
    }

    public MyImage(Context context) {
        super(context);
    }

    public MyImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(myWidth, myHeight);
    }
}
