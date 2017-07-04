package app.com.bongdadayroi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vmax.android.ads.api.VmaxAdSettings;
import com.vmax.android.ads.api.VmaxAdSize;
import com.vmax.android.ads.api.VmaxAdView;
import com.vmax.android.ads.api.VmaxSdk;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.features.ad.banner.BannerAdPresenter;
import app.com.bongdadayroi.features.ad.banner.BannerAdView;
import app.com.bongdadayroi.interfaces.GetInfoAccount;
import app.com.bongdadayroi.models.FacebookUser;
import app.com.bongdadayroi.myapp.Config;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.networks.MyRequest;
import app.com.bongdadayroi.networks.TFirebaseAnalytics;
import app.com.bongdadayroi.utils.API;
import app.com.bongdadayroi.views.MyImage;
import vn.amobi.util.ads.AmobiAdView;

public class AccountActivity extends AppCompatActivity implements GetInfoAccount,
    BannerAdView {
    private ActionBar actionBar;
    private MyImage myImage;
    private TextView tvName;
    private CallbackManager callbackManager;
    private Button btLogin;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            MyRequest.requestGetToken(getApplicationContext(), "" + accessToken.getToken(),
                AccountActivity.this);
            Profile profile = Profile.getCurrentProfile();
            FacebookUser facebookUser = FacebookUser.getInstance();
            facebookUser.setInformation(profile);
        }

        @Override
        public void onCancel() {
            btLogin.setText(R.string.dang_nhap_facebook);
            Toast.makeText(getBaseContext(), "Cancel", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            btLogin.setText(R.string.dang_nhap_facebook);
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VmaxSdk.init(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account);
        mBannerAdPresenter = new BannerAdPresenter(this);
        setUpAmobiAd();
        setUpVmaxAd();
        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            setUpActionBar();
        }
        setUpView();
        TFirebaseAnalytics.setAnalytic(this);
    }

    private void setUpView() {
        myImage = (MyImage) findViewById(R.id.ivAvatar);
        if (myImage != null) {
            myImage.setMyWidth(ScreenSize.WIDTH / 3);
            myImage.setMyHeight(ScreenSize.WIDTH / 3);
            myImage.setSize();
        }
        tvName = (TextView) findViewById(R.id.tvName);
        callbackManager = CallbackManager.Factory.create();
        btLogin = (Button) findViewById(R.id.login_button);
        if (AccessToken.getCurrentAccessToken() != null) {
            btLogin.setText(R.string.dang_xuat_facebook);
        } else {
            btLogin.setText(R.string.dang_nhap_facebook);
        }
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btLogin.getText().toString().equals("Đăng nhập với Facebook")) {
                    LoginManager.getInstance().registerCallback(callbackManager, callback);
                    LoginManager.getInstance().logInWithReadPermissions(AccountActivity.this,
                        Arrays.asList("public_profile", "user_friends"));
                    btLogin.setText(R.string.dang_xuat_facebook);
                } else {
                    logout();
                    LoginManager.getInstance().logOut();
                }
            }
        });
    }

    private void setUpActionBar() {
        if (actionBar != null) {
            actionBar.setTitle("Tài khoản");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("login", "login1: ");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showInfoUser() {
        FacebookUser user = FacebookUser.getInstance();
        tvName.setText(user.getUser_name());
        myImage.setDefaultImageResId(R.drawable.no_image);
        myImage.setErrorImageResId(R.drawable.no_image);
        myImage.setImageUrl(user.getUser_avatar_uri());
        tvName.setVisibility(View.VISIBLE);
        myImage.setVisibility(View.VISIBLE);
    }

    private void logout() {
        final FacebookUser facebookUser = FacebookUser.getInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("app_id", Config.APP_ID);
        AndroidNetworking.post(API.LOGOUT)
            .addHeaders("Cookie", "user_token=" + facebookUser.getAuth_token())
            .addQueryParameter(params)
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    setViewGone();
                }

                @Override
                public void onError(ANError ANError) {
                }
            });
        final Handler myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                setViewGone();
            }
        };
    }

    private void setViewGone() {
        tvName.setVisibility(View.GONE);
        myImage.setVisibility(View.GONE);
        btLogin.setText(R.string.dang_nhap_facebook);
        FacebookUser facebookUser = FacebookUser.getInstance();
        facebookUser.reset();
    }

    private VmaxAdView mVmaxAdView;
    private AmobiAdView mAmobiAdView;
    private BannerAdPresenter mBannerAdPresenter;

    private void setUpAmobiAd() {
        mAmobiAdView = (AmobiAdView) findViewById(R.id.amobiAdview);
//        mAmobiAdView.setLayoutParams(new RelativeLayout.LayoutParams(320, 50));
        if (mAmobiAdView != null) {
            mAmobiAdView.setEventListener(mBannerAdPresenter);
        }
    }

    private void setUpVmaxAd() {
        mVmaxAdView = (VmaxAdView) findViewById(R.id.banner_adview);
//        mVmaxAdView.setLayoutParams(new RelativeLayout.LayoutParams(320, 50));
        HashMap tempAdSettings = new HashMap<>();
        tempAdSettings.put(VmaxAdSettings.AdSettings_sbd, VmaxAdSize.AdSize_320x50);
        //Scale is optional to further scale above mentioned size
        mVmaxAdView.setAdSettings(tempAdSettings);
        mVmaxAdView.setAdListener(mBannerAdPresenter);
//        mVmaxAdView.loadAd();
        mBannerAdPresenter.loadVmaxAd(mVmaxAdView);
    }

    @Override
    public void onHadVmaxBanner() {
        mAmobiAdView.setVisibility(View.GONE);
        mVmaxAdView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoVmaxBanner() {
        mVmaxAdView.setVisibility(View.GONE);
        mBannerAdPresenter.loadAmobiAd(mAmobiAdView, AmobiAdView.WidgetSize.SMALL);
    }

    @Override
    public void onHadAmobiBanner() {
        if (mVmaxAdView.getVisibility() == View.GONE) {
            mAmobiAdView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNoAmobiBanner() {
        mAmobiAdView.setVisibility(View.GONE);
        mBannerAdPresenter.loadVmaxAd(mVmaxAdView);
    }

    @Override
    public void onAdDismiss() {
        mBannerAdPresenter.loadVmaxAd(mVmaxAdView);
    }

    @Override
    protected void onPause() {
        if (mVmaxAdView != null) {
            mVmaxAdView.onPause();
        }
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        if (mVmaxAdView != null) {
            mVmaxAdView.onResume();
        }
        AppEventsLogger.activateApp(this);
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            this.showInfoUser();
        } else {
            FacebookUser facebookUser = FacebookUser.getInstance();
            facebookUser.reset();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mVmaxAdView != null) {
            mVmaxAdView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        if (mVmaxAdView != null) {
            mVmaxAdView.finish();
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (mVmaxAdView != null) {
            mVmaxAdView.onBackPressed();
        }
        super.onBackPressed();
    }
}
