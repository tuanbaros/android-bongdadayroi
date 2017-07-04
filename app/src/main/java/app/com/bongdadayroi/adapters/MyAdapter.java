package app.com.bongdadayroi.adapters;

/**
 * Created by tuan on 27/03/2016.
 */
public class MyAdapter {
    public static volatile MyAdapter myAdapter = null;

    private MyAdapter() {
    }

    TWVAdapter newAdapter, mostAdapter;
    MyListAdapter myListAdapter;
    CommentAdapter commentAdapter;

    public CommentAdapter getCommentAdapter() {
        return commentAdapter;
    }

    public void setCommentAdapter(CommentAdapter commentAdapter) {
        this.commentAdapter = commentAdapter;
    }

    public MyListAdapter getMyListAdapter() {
        return myListAdapter;
    }

    public void setMyListAdapter(MyListAdapter myListAdapter) {
        this.myListAdapter = myListAdapter;
    }

    public TWVAdapter getNewAdapter() {
        return newAdapter;
    }

    public void setNewAdapter(TWVAdapter newAdapter) {
        this.newAdapter = newAdapter;
    }

    public TWVAdapter getMostAdapter() {
        return mostAdapter;
    }

    public void setMostAdapter(TWVAdapter mostAdapter) {
        this.mostAdapter = mostAdapter;
    }

    public static MyAdapter getInstance() {
        if (myAdapter == null) {
            synchronized (MyAdapter.class) {
                if (myAdapter == null) {
                    myAdapter = new MyAdapter();
                }
            }
        }
        return myAdapter;
    }
}
