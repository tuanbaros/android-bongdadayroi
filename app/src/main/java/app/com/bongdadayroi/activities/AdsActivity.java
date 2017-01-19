package app.com.bongdadayroi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.vmax.android.ads.api.VmaxAdSettings;
import com.vmax.android.ads.api.VmaxAdSize;
import com.vmax.android.ads.api.VmaxAdView;

import java.util.HashMap;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.features.ad.banner.BannerAdPresenter;
import app.com.bongdadayroi.features.ad.banner.BannerAdView;
import app.com.bongdadayroi.features.ad.video.VideoAdPresenter;
import app.com.bongdadayroi.features.ad.video.VideoAdView;
import app.com.bongdadayroi.networks.TFirebaseAnalytics;
import vn.amobi.util.ads.AmobiAdView;
import vn.amobi.util.ads.video.AmobiShowVideoAdRequest;
import vn.amobi.util.ads.video.AmobiVideoAd;

public class AdsActivity extends AppCompatActivity implements
        BannerAdView, VideoAdView {

    private boolean isStarted = false;

    private AmobiAdView adView;

    private VideoAdPresenter mVideoAdPresenter;

    private BannerAdPresenter mBannerAdPresenter;

    private VmaxAdView mVmaxAdView;

    private FrameLayout vmaxVideoFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ads);

        adView = (AmobiAdView) findViewById(R.id.main_menu_adView);

        TFirebaseAnalytics.setAnalytic(this);

        mVideoAdPresenter = new VideoAdPresenter(this);

        mBannerAdPresenter = new BannerAdPresenter(this);

        AmobiVideoAd.getInstance().prepare(this);

        mVideoAdPresenter.loadAmobiVideoAd();

    }

    private void startDetailActivity(){
        if(!isStarted){
            Intent intent = new Intent(this, EXOMediaActivity.class);
            intent.putExtra("video", getIntent().getSerializableExtra("video"));
            startActivity(intent);
            isStarted = true;
            finish();
        }
    }

    //begin ads/video/VideoAdView
    @Override
    public void onHadVmaxVideo() {

    }

    @Override
    public void onNoVmaxVideo() {
        setUpVmaxBannerAd();
    }

    @Override
    public void onHadAmobiVideo() {
        AmobiVideoAd.getInstance().showAd(new AmobiShowVideoAdRequest());
    }

    @Override
    public void onNoAmobiVideo() {
        vmaxVideoFrameLayout = (FrameLayout)
                findViewById(R.id.vmax_video_frame_layout);
        mVideoAdPresenter.loadVmaxVideoAd(vmaxVideoFrameLayout);
    }

    private void setUpVmaxBannerAd() {
        mVmaxAdView  = (VmaxAdView ) findViewById(R.id.banner_adview);
        HashMap tempAdSettings = new HashMap<>();

        tempAdSettings.put(VmaxAdSettings.AdSettings_sbd, VmaxAdSize.AdSize_300x250);

        mVmaxAdView .setAdSettings(tempAdSettings);
        mVmaxAdView .setAdListener(mBannerAdPresenter);
        mBannerAdPresenter.loadVmaxAd(mVmaxAdView );
    }

    @Override
    public void onVideoAdFinish() {
        startDetailActivity();
    }
    //end ads/video/VideoAdView

    //begin ads/banner/BannerAdView
    @Override
    public void onHadVmaxBanner() {
        mVmaxAdView.setVisibility(View.VISIBLE);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNoVmaxBanner() {
        mBannerAdPresenter.loadAmobiAd(adView, AmobiAdView.WidgetSize.FULL_SCREEN);
    }

    @Override
    public void onHadAmobiBanner() {
        adView.setVisibility(View.VISIBLE);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNoAmobiBanner() {
        startDetailActivity();
    }

    @Override
    public void onAdDismiss() {
        startDetailActivity();
    }
    //end ads/banner/BannerAdView

    @Override
    protected void onPause() {
        if (mVmaxAdView != null) {

            mVmaxAdView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mVmaxAdView != null) {

            mVmaxAdView.onResume();
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
            Log.i("vmax", "dev finish");

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
