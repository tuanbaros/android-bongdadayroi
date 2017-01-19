package app.com.bongdadayroi.push;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.gson.Gson;

import org.json.JSONObject;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.activities.EXOMediaActivity;
import app.com.bongdadayroi.activities.SplashActivity;
import app.com.bongdadayroi.models.FacebookUser;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.networks.MyRequest;
import app.com.bongdadayroi.networks.TFirebaseAnalytics;
import app.com.bongdadayroi.utils.API;

public class PushActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_push);

        String post_id = getIntent().getStringExtra("post_id");

        if(post_id == null || post_id.equals("")){
            Intent intent = getIntent();
            Uri uri = intent.getData();
            Log.i("uri", "uri: "+ uri);
            String url = uri.toString();
            try {
                int start = url.lastIndexOf("-")+2;
                int end = url.lastIndexOf(".");
                post_id = url.substring(start, end);
                Log.i("uri", "uri: "+ post_id);
            }catch (Exception e){

            }


        }

        if (post_id==null){
            Log.i("postnull", "postnull");
            Intent intent = new Intent(PushActivity.this, SplashActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            TFirebaseAnalytics.setAnalytic(this);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            ScreenSize.HEIGHT = displaymetrics.heightPixels;
            ScreenSize.WIDTH = displaymetrics.widthPixels;

            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            linearLayout = (LinearLayout)findViewById(R.id.llTry);
            Button button = (Button) findViewById(R.id.btRetry);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    requestData(getIntent().getStringExtra("post_id"));
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    Profile profile = Profile.getCurrentProfile();
                    if(accessToken!=null){
                        Log.i("token", "mytoken: " + accessToken.getToken());
                        MyRequest.requestGetToken(getApplicationContext(), accessToken.getToken(), null);
                        if(profile!=null){
                            FacebookUser facebookUser = FacebookUser.getInstance();
                            facebookUser.setInformation(profile);
                        }
                    }
                }
            });

            Log.i("post_id", "post_id: " + getIntent().getStringExtra("post_id"));

            requestData(post_id);
        }




    }



    @Override
    public void onResume(){
        super.onResume();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Profile profile = Profile.getCurrentProfile();
        if(accessToken!=null){
            Log.i("token", "mytoken: " + accessToken.getToken());
            MyRequest.requestGetToken(getApplicationContext(), accessToken.getToken(), null);
            if(profile!=null){
                FacebookUser facebookUser = FacebookUser.getInstance();
                facebookUser.setInformation(profile);
            }
        }


    }


    private void requestData(String post_id){

        AndroidNetworking.get(API.VIDEO_DETAIL + post_id)
                .setPriority(Priority.MEDIUM)
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Push push = gson.fromJson(response.toString(), Push.class);

                        Intent intent = new Intent(PushActivity.this, EXOMediaActivity.class);
                        intent.putExtra("video", push.getData());
                        intent.putExtra("push", 1);
                        if(getIntent().getIntExtra("checkPushComment", 0)>0){
                            intent.putExtra("checkPushComment", 1);
                        }
                        startActivity(intent);
                        PushActivity.this.finish();
                    }

                    @Override
                    public void onError(ANError ANError) {
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                });
    }



    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

}
