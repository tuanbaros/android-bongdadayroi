package app.com.bongdadayroi.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Random;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.utils.Variables;
import app.com.bongdadayroi.views.MyImage;

/**
 * Created by Nguyen Thanh Tuan on 20/05/2016.
 */
public class ListFavoriteAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;

    public ListFavoriteAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
        databaseAdapter.open();
        int count = databaseAdapter.getAllRows().getCount();
        databaseAdapter.close();
        return count;
    }

    @Override
    public Object getItem(int position) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
        databaseAdapter.open();
        Cursor c = databaseAdapter.getAllRows();
        c.moveToPosition(position);
        Gson gson = new Gson();
        return gson.fromJson(c.getString(Variables.KEY_JSON), MyVideo.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_my_list, null);
            viewHolder = new ViewHolder();
            viewHolder.ivAvatar = (MyImage) convertView.findViewById(R.id.ivAvatar);
            viewHolder.ivAvatar.setMyWidth(ScreenSize.WIDTH * 3 / 7);
            viewHolder.ivAvatar.setMyHeight(viewHolder.ivAvatar.getMyWidth() * 9 / 16);
            viewHolder.ivAvatar.setSize();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvNumberLike = (TextView) convertView.findViewById(R.id.tvNumberLike);
            viewHolder.tvNumberSeen = (TextView) convertView.findViewById(R.id.tvNumberSeen);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
        databaseAdapter.open();
        Cursor c = databaseAdapter.getAllRows();
        c.moveToPosition(position);
        Gson gson = new Gson();
        MyVideo myVideo = gson.fromJson(c.getString(Variables.KEY_JSON), MyVideo.class);
        viewHolder.tvTitle.setText(myVideo.getTitle());
        viewHolder.tvTitle.setLines(2);
        viewHolder.ivAvatar.setDefaultImageResId(R.drawable.no_image);
        viewHolder.ivAvatar.setErrorImageResId(R.drawable.no_image);
        viewHolder.ivAvatar.setImageUrl(myVideo.getAvatar());
        viewHolder.tvNumberSeen.setText(myVideo.getNum_view());
        if (myVideo.getNum_like().equals("0")) {
            Random random = new Random();
            int a = 0;
            if (!myVideo.getNum_view().equals("0")) {
                a = random.nextInt(Integer.parseInt(myVideo.getNum_view()));
            }
            viewHolder.tvNumberLike.setText("" + a);
        } else {
            viewHolder.tvNumberLike.setText(myVideo.getNum_like());
        }
        return convertView;
    }

    static class ViewHolder {
        MyImage ivAvatar;
        TextView tvTitle, tvNumberLike, tvNumberSeen;
    }
}
