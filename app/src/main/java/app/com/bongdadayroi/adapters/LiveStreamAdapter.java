package app.com.bongdadayroi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.views.MyImage;

/**
 * Created by Nguyen Thanh Tuan on 11/07/2016.
 */
public class LiveStreamAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<String> arrListLinkLiveStream;

    private LayoutInflater layoutInflater;

    public LiveStreamAdapter(Context context, ArrayList<String> arrListLinkLiveStream) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.arrListLinkLiveStream = arrListLinkLiveStream;
    }

    @Override
    public int getCount() {
        return arrListLinkLiveStream.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = layoutInflater.inflate(R.layout.item_twv_livestream, null);
            viewHolder = new ViewHolder();
            viewHolder.tvItemLink = (TextView) view.findViewById(R.id.tvLink);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.tvItemLink.setText("Link " + ++i);

        return view;
    }

    static class ViewHolder{
        TextView tvItemLink;
    }
}
