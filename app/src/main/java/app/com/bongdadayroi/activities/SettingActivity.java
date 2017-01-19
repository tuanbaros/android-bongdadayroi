package app.com.bongdadayroi.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.vmax.android.ads.api.VmaxAdSettings;
import com.vmax.android.ads.api.VmaxAdSize;
import com.vmax.android.ads.api.VmaxAdView;
import com.vmax.android.ads.api.VmaxSdk;

import java.util.HashMap;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.adapters.SettingAdapter;
import app.com.bongdadayroi.features.ad.banner.BannerAdPresenter;
import app.com.bongdadayroi.features.ad.banner.BannerAdView;
import app.com.bongdadayroi.networks.TFirebaseAnalytics;
import vn.amobi.util.ads.AmobiAdView;

public class SettingActivity extends AppCompatActivity implements BannerAdView {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VmaxSdk.init(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        mBannerAdPresenter = new BannerAdPresenter(this);
        setUpAmobiAd();
        setUpVmaxAd();

        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            setUpActionBar();
        }

        setUpListView();

        TFirebaseAnalytics.setAnalytic(this);
    }

    private void setUpListView() {
        ListView list = (ListView)findViewById(R.id.listview);
        list.setAdapter(new SettingAdapter(this));
    }

    private void setUpActionBar() {
        if (actionBar != null) {
            actionBar.setTitle("Cài đặt");
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

    private VmaxAdView mVmaxAdView ;

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
        mVmaxAdView  = (VmaxAdView ) findViewById(R.id.banner_adview);
//        mVmaxAdView.setLayoutParams(new RelativeLayout.LayoutParams(320, 50));
        HashMap tempAdSettings = new HashMap<>();

        tempAdSettings.put(VmaxAdSettings.AdSettings_sbd, VmaxAdSize.AdSize_320x50);
        //Scale is optional to further scale above mentioned size

        mVmaxAdView .setAdSettings(tempAdSettings);
        mVmaxAdView .setAdListener(mBannerAdPresenter);
//        mVmaxAdView.loadAd();
        mBannerAdPresenter.loadVmaxAd(mVmaxAdView );
    }

    @Override
    public void onHadVmaxBanner() {
        mAmobiAdView.setVisibility(View.GONE);
        mVmaxAdView .setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoVmaxBanner() {
        mVmaxAdView.setVisibility(View.GONE);
        mBannerAdPresenter.loadAmobiAd(mAmobiAdView, AmobiAdView.WidgetSize.SMALL);
    }

    @Override
    public void onHadAmobiBanner() {
        if(mVmaxAdView.getVisibility()==View.GONE){
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
