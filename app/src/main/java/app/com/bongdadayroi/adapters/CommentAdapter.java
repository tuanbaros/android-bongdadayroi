package app.com.bongdadayroi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.models.Comment;
import app.com.bongdadayroi.utils.ConvertTime;
import app.com.bongdadayroi.views.RoundImageView;


public class CommentAdapter extends BaseAdapter {

    private ArrayList<Comment> arrayList;

    LayoutInflater layoutInflater;

    public CommentAdapter(Context context, ArrayList<Comment> arrayList){
        layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_comment_list, null);
            viewHolder = new ViewHolder();
            viewHolder.ivAvatar = (RoundImageView) convertView.findViewById(R.id.ivAvatar);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tvContent);
            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Comment comment = arrayList.get(position);
        viewHolder.tvName.setText(arrayList.get(position).getUser_name());
        viewHolder.tvContent.setText(comment.getContent());
        viewHolder.tvDate.setText(ConvertTime.getTimeAgoTest(comment.getDate(), parent.getContext()));
        viewHolder.ivAvatar.setDefaultImageResId(R.drawable.no_image);
        viewHolder.ivAvatar.setErrorImageResId(R.drawable.no_image);
        viewHolder.ivAvatar.setImageUrl(comment.getAvatar());

        return convertView;
    }

    static class ViewHolder{
        RoundImageView ivAvatar;

        TextView tvName, tvContent, tvDate;
    }
}
