package app.com.bongdadayroi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.TextView;

import app.com.bongdadayroi.myapp.ScreenSize;

/**
 * Created by tuan on 07/04/2016.
 */
public class MyTextView extends TextView{
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(ScreenSize.WIDTH*3/7, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
