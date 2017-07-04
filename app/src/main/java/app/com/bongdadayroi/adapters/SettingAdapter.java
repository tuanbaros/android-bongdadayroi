package app.com.bongdadayroi.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.bongdadayroi.R;

/**
 * Created by Nguyen Thanh Tuan on 30/04/2016.
 */
public class SettingAdapter extends BaseAdapter {
    private AppCompatActivity appCompatActivity;
    private String[] nameSetting;
    private LayoutInflater layoutInflater;

    public SettingAdapter(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        layoutInflater = LayoutInflater.from(this.appCompatActivity);
        Resources res = appCompatActivity.getResources();
        nameSetting = res.getStringArray(R.array.setting);
    }

    @Override
    public int getCount() {
        return nameSetting.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_setting_list, parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        final ImageView ivSwitch = (ImageView) convertView.findViewById(R.id.ivSwitch);
        tvName.setText(nameSetting[position]);
        SharedPreferences sharedPreferences = appCompatActivity.getSharedPreferences(
            "tuannt", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean check = sharedPreferences.getBoolean(nameSetting[position], true);
        if (check) {
            ivSwitch.setImageResource(R.drawable.switch_yes);
            ivSwitch.setTag(R.drawable.switch_yes);
        } else {
            ivSwitch.setImageResource(R.drawable.switch_no);
            ivSwitch.setTag(R.drawable.switch_no);
        }
        ivSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) ivSwitch.getTag() == R.drawable.switch_no) {
                    ivSwitch.setImageResource(R.drawable.switch_yes);
                    ivSwitch.setTag(R.drawable.switch_yes);
                    editor.putBoolean(nameSetting[position], true);
                    editor.commit();
                } else {
                    ivSwitch.setImageResource(R.drawable.switch_no);
                    ivSwitch.setTag(R.drawable.switch_no);
                    editor.putBoolean(nameSetting[position], false);
                    editor.commit();
                }
            }
        });
        return convertView;
    }
}
