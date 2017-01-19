package app.com.bongdadayroi.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vmax.android.ads.api.VmaxAdView;
import com.vmax.android.ads.api.VmaxSdk;
import com.vmax.android.ads.common.VmaxAdListener;

import app.com.bongdadayroi.R;
import bolts.AppLinks;
import vn.amobi.util.ads.AdEventInterface;
import vn.amobi.util.ads.AmobiAdView;

public class TestAppLink extends AppCompatActivity implements AdEventInterface {

    private VmaxAdView mVmaxAdView;

    private AmobiAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_app_link);

        VmaxSdk.init(this);
        VmaxSdk.getSDKVersion();
        setUpVmaxAd();

    }

    private void setUpAmobiAd() {
        adView = (AmobiAdView) findViewById(R.id.amobiAdview);

        if (adView != null) {
            adView.setEventListener(this);
            adView.loadAd(AmobiAdView.WidgetSize.SMALL); }
    }

    @Override
    public void onAdViewLoaded() {
        adView.setVisibility(View.VISIBLE);
        Log.d("loadAds","finish");
    }

    @Override
    public void onAdViewClose() {
        Log.d("loadAds","close");
    }

    @Override
    public void onLoadAdError(ErrorCode errorCode) {
        Log.d("loadAds","error");
    }

    private void setUpVmaxAd() {
        mVmaxAdView = (VmaxAdView) findViewById(R.id.banner_adview);

        mVmaxAdView.setAdListener(new VmaxAdListener() {
            @Override
            public VmaxAdView didFailedToLoadAd(String s) {
                Log.i("vmax", "vmax: didFailedToLoadAd, " + s);
                setUpAmobiAd();
                return null;
            }

            @Override
            public VmaxAdView didFailedToCacheAd(String s) {
                Log.i("vmax", "vmax: didFailedToCacheAd, " + s);
                setUpAmobiAd();
                return null;
            }

            @Override
            public void adViewDidLoadAd(VmaxAdView adView) {
                Log.i("vmax", "vmax: adViewDidLoadAd");
                mVmaxAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void adViewDidCacheAd(VmaxAdView adView) {
                Log.i("vmax", "vmax: adViewDidCacheAd");
                setUpAmobiAd();
            }

            @Override
            public void didInteractWithAd(VmaxAdView adView) {
                Log.i("vmax", "vmax: didInteractWithAd");
            }

            @Override
            public void willDismissAd(VmaxAdView adView) {
                mVmaxAdView.setVisibility(View.GONE);
            }

            @Override
            public void willPresentAd(VmaxAdView adView) {
                mVmaxAdView.setVisibility(View.VISIBLE);

            }

            @Override
            public void willLeaveApp(VmaxAdView adView) {
            }

            @Override
            public void onVideoView(boolean b, int i, int i1) {

            }

            @Override
            public void onAdExpand() {

            }

            @Override
            public void onAdCollapsed() {

            }


        });

        mVmaxAdView.loadAd();
    }
}
