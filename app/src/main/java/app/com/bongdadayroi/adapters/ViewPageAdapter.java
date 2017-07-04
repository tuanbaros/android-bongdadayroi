package app.com.bongdadayroi.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.AccessToken;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.interfaces.GetInfoVideo;
import app.com.bongdadayroi.interfaces.MyLogin;
import app.com.bongdadayroi.models.Data;
import app.com.bongdadayroi.models.FacebookUser;
import app.com.bongdadayroi.models.MyComment;
import app.com.bongdadayroi.models.MyData;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.Config;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.utils.API;
import app.com.bongdadayroi.views.CustomPagerEnum;
import app.com.bongdadayroi.views.SetHeightView;

/**
 * Created by tuan on 23/02/2016.
 */
public class ViewPageAdapter extends PagerAdapter {
    private Context mContext;
    private GetInfoVideo getInfoVideo;
    private ListView listviewRelate, listviewComment;
    private ProgressBar progressBarRelate, progressBarComment;
    private LinearLayout llTryRelate, llTryComment;
    private Button buttonRelate, buttonComment;
    private String video_id;
    public static Button btLogin;
    public static RelativeLayout rlComment;
    private boolean checkComment = false;
    Handler mHandler, myHandler;
    private MyLogin myLogin;

    public ViewPageAdapter(Context context, GetInfoVideo getInfoVideo, String video_id,
                           MyLogin myLogin) {
        mContext = context;
        this.getInfoVideo = getInfoVideo;
        this.video_id = video_id;
        this.myLogin = myLogin;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout =
            (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
        switch (position) {
            case 0:
                setUpListRelateVideo(layout, video_id);
                break;
            case 1:
                setUpListComment(layout, video_id);
                break;
        }
        collection.addView(layout);
        return layout;
    }

    private void setUpListRelateVideo(final ViewGroup layout, final String video_id) {
        listviewRelate = (ListView) layout.findViewById(R.id.lvResult);
        progressBarRelate = (ProgressBar) layout.findViewById(R.id.progressBar);
        llTryRelate = (LinearLayout) layout.findViewById(R.id.llTry);
        buttonRelate = (Button) layout.findViewById(R.id.btRetry);
        requestVideoRelate(video_id);
        buttonRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVideoRelate(video_id);
            }
        });
        listviewRelate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyVideo myVideo = (MyVideo) listviewRelate.getAdapter().getItem(position);
                ViewPageAdapter.this.video_id = myVideo.getPost_id();
                getInfoVideo.showVideo(myVideo);
                requestVideoRelate(ViewPageAdapter.this.video_id);
                requestComment(ViewPageAdapter.this.video_id);
            }
        });
    }

    private void setUpListComment(final ViewGroup layout, final String video_id) {
        listviewComment = (ListView) layout.findViewById(R.id.lvResult);
        btLogin = (Button) layout.findViewById(R.id.login_button);
        ScreenSize.BASE_HEIGHT = btLogin.getHeight();
        rlComment = (RelativeLayout) layout.findViewById(R.id.rlComment);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            btLogin.setVisibility(View.GONE);
            rlComment.setVisibility(View.VISIBLE);
            Log.i("avatar", "avatar: " + FacebookUser.getInstance().getUser_avatar_uri());
        } else {
            btLogin.setVisibility(View.VISIBLE);
            rlComment.setVisibility(View.GONE);
        }
        final EditText etCOmment = (EditText) layout.findViewById(R.id.etCOmment);
        ImageView ivMySend = (ImageView) layout.findViewById(R.id.ivMySend);
        ivMySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etCOmment.getText().toString();
                if (content.equals("")) {
                    Toast
                        .makeText(mContext, "Vui lòng nhập nội dung bình luận!", Toast.LENGTH_SHORT)
                        .show();
                } else {
                    comment(content);
                    progressBarComment.setVisibility(View.VISIBLE);
                    etCOmment.setText("");
                    InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etCOmment.getWindowToken(), 0);
                }
            }
        });
        etCOmment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myLogin.pauseYoutube();
                return false;
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLogin.doLogin();
            }
        });
        progressBarComment = (ProgressBar) layout.findViewById(R.id.progressBar);
        llTryComment = (LinearLayout) layout.findViewById(R.id.llTry);
        buttonComment = (Button) layout.findViewById(R.id.btRetry);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestComment(video_id);
            }
        });
        requestComment(video_id);
    }

    private synchronized void requestVideoRelate(final String video_id) {
        progressBarRelate.setVisibility(View.VISIBLE);
        llTryRelate.setVisibility(View.GONE);
        listviewRelate.setVisibility(View.GONE);
        AndroidNetworking.get(API.VIDEO_RELATE_URL + video_id)
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    MyData myData = MyData.getInstance();
                    Data data = gson.fromJson(response.toString(), Data.class);
                    myData.getArrayRelate().clear();
                    Collections.addAll(myData.getArrayRelate(), data.getData());
                    progressBarRelate.setVisibility(View.GONE);
                    llTryRelate.setVisibility(View.GONE);
                    listviewRelate.setVisibility(View.VISIBLE);
                    if (myData.getArrayRelate().size() > 0) {
                        MyAdapter.getInstance()
                            .setMyListAdapter(new MyListAdapter(mContext, myData.getArrayRelate()));
                        listviewRelate.setAdapter(MyAdapter.getInstance().getMyListAdapter());
                        ScreenSize.RELATE_HEIGHT =
                            SetHeightView.setListViewHeightBasedOnChildren(listviewRelate, 0);
                        getInfoVideo.setHeightView(ScreenSize.RELATE_HEIGHT);
                    }
                    requestComment(video_id);
                }

                @Override
                public void onError(ANError ANError) {
                    progressBarRelate.setVisibility(View.GONE);
                    llTryRelate.setVisibility(View.VISIBLE);
                    listviewRelate.setVisibility(View.GONE);
                }
            });
    }

    private synchronized void requestComment(final String video_id) {
        progressBarComment.setVisibility(View.VISIBLE);
        llTryComment.setVisibility(View.GONE);
        listviewComment.setVisibility(View.GONE);
        AndroidNetworking.get(API.LIST_COMMENT + video_id)
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    MyData myData = MyData.getInstance();
                    MyComment data = gson.fromJson(response.toString(), MyComment.class);
                    myData.getArrayComment().clear();
                    Collections.addAll(myData.getArrayComment(), data.getInfo());
                    progressBarComment.setVisibility(View.GONE);
                    llTryComment.setVisibility(View.GONE);
                    listviewComment.setVisibility(View.VISIBLE);
                    int baseHeight = 0;
                    if (btLogin.getVisibility() == View.VISIBLE) {
                        baseHeight = btLogin.getHeight() + 80;
                    }
                    if (myData.getArrayComment().size() > 0) {
                        MyAdapter.getInstance().setCommentAdapter(
                            new CommentAdapter(mContext, myData.getArrayComment()));
                        listviewComment.setAdapter(MyAdapter.getInstance().getCommentAdapter());
                        if (rlComment.getVisibility() == View.VISIBLE) {
                            baseHeight = rlComment.getHeight() + 80;
                        }
                        getInfoVideo.setHeightView(SetHeightView
                            .setListViewHeightBasedOnChildren(listviewComment, baseHeight));
                    } else {
                        getInfoVideo.setHeightView(baseHeight + 200);
                    }
                }

                @Override
                public void onError(ANError ANError) {
                    progressBarComment.setVisibility(View.GONE);
                    llTryComment.setVisibility(View.VISIBLE);
                    listviewComment.setVisibility(View.GONE);
                }
            });
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return CustomPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

    private synchronized void comment(String comment) {
        postComment(comment, video_id);
    }

    private void postComment(final String comment, final String postID) {
        final FacebookUser facebookUser = FacebookUser.getInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("content", comment);
        params.put("app_id", Config.APP_ID);
        params.put("post_id", postID);
        AndroidNetworking.post("http://content.amobi.vn/api/comment/comment")
            .addHeaders("Cookie", "user_token=" + facebookUser.getAuth_token())
            .addQueryParameter(params)
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build()
            .getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("\"success\":\"true\"")) {
                        requestComment(video_id);
                    } else {
                        showNetworkError();
                        progressBarComment.setVisibility(View.GONE);
                        Toast.makeText(mContext, "Network error!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ANError ANError) {
                    showNetworkError();
                    progressBarComment.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Network error!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void showNetworkError() {
        new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();
    }
}
