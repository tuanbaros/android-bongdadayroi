package app.com.bongdadayroi.activities;

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

import com.facebook.AccessToken;
import com.facebook.Profile;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.interfaces.SplashError;
import app.com.bongdadayroi.models.FacebookUser;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.networks.MyRequest;
import app.com.bongdadayroi.networks.TFirebaseAnalytics;
import app.com.bongdadayroi.push.PushActivity;
import bolts.AppLinks;

public class SplashActivity extends AppCompatActivity implements
        SplashError{

    private ProgressBar progressBar;

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

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
                getData(SplashActivity.this);
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

        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
            String url = targetUrl.toString();
            int start = url.lastIndexOf("-")+2;
            int end = url.lastIndexOf(".");
            String post_id = url.substring(start, end);
            if (post_id!=null&&!post_id.equals("")&&isInteger(post_id)){
                Intent intent = new Intent(this, PushActivity.class);
                intent.putExtra("post_id", post_id);
                startActivity(intent);
                finish();
            }else{
                getData(this);
            }
            Log.i("uri", "uri: "+ post_id);
        }else {
            getData(this);
        }
        
    }

    public void getData(SplashError splashError){
        synchronized (MyRequest.class){
            MyRequest.requestHomeAPI(this, splashError);
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

    @Override
    public void showButtonTryAgain() {
        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
