package app.com.bongdadayroi.adapters;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.models.CategoryItem;

/**
 * Created by tuan on 08/03/2016.
 */
public class ListAdapter extends BaseAdapter {

    private AppCompatActivity appCompatActivity;

    private CategoryItem[] categories;

    private String[] name;

    private LayoutInflater layoutInflater;

    public ListAdapter(AppCompatActivity appCompatActivity, CategoryItem[] categories){
        this.appCompatActivity = appCompatActivity;
        this.categories = categories;
        layoutInflater = LayoutInflater.from(this.appCompatActivity);
        Resources res = appCompatActivity.getResources();
        name = res.getStringArray(R.array.category);
    }

    @Override
    public int getCount() {
        if(categories != null){
            return categories.length + 3;
        }else{
            return 3;
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_list_category, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(position < 3){
            viewHolder.tvTitle.setText(name[position]);

        }else{
            viewHolder.tvTitle.setText(categories[position-3].getName());
        }



        return convertView;
    }

    static class ViewHolder{
        TextView tvTitle;
    }
}
