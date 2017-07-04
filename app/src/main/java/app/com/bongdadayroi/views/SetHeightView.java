package app.com.bongdadayroi.views;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Tranhoa on 3/17/2016.
 */
public class SetHeightView {
    public static int setListViewHeightBasedOnChildren(ListView listView, int baseHeight) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
//        int count = listAdapter.getCount();
        if (listAdapter == null)
            return 0;
        int desiredWidth =
            View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = listAdapter.getView(0, null, listView);
        view.setLayoutParams(
            new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//        view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//        totalHeight = view.getMeasuredHeight() * count;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(
                    new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height =
            totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + baseHeight;
        Log.i("height", "height: " + params.height);
        return params.height;
    }
}
