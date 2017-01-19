package app.com.bongdadayroi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.lucasr.twowayview.TwoWayView;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.activities.AdsActivity;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.views.EnumLayout;

/**
 * Created by tuan on 24/03/2016.
 */
public class HomeAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;

    public HomeAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return EnumLayout.values().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(EnumLayout.values()[position].getMain_layout(), null);
            viewHolder = new ViewHolder();
            viewHolder.twoWayView = (TwoWayView)convertView.findViewById(R.id.twv);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.tvHomeCategory);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        MyAdapter myAdapter = MyAdapter.getInstance();
        if (position < 2){
            if (position == 0){
                viewHolder.twoWayView.setAdapter(myAdapter.getNewAdapter());
            }
            if (position == 1){
                viewHolder.twoWayView.setAdapter(myAdapter.getMostAdapter());
            }

            viewHolder.textView.setText(EnumLayout.values()[position].getTitle());
        }else{
            viewHolder.textView.setVisibility(View.GONE);
        }

        viewHolder.twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), AdsActivity.class);
                intent.putExtra("video", (MyVideo)viewHolder.twoWayView.getAdapter().getItem(position));
                parent.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TwoWayView twoWayView;
        TextView textView;
    }

}
