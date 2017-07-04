package app.com.bongdadayroi.features.ad.banner;

import android.util.Log;

import com.vmax.android.ads.api.VmaxAdView;
import com.vmax.android.ads.common.VmaxAdListener;

import app.com.bongdadayroi.myapp.Config;
import vn.amobi.util.ads.AdEventInterface;
import vn.amobi.util.ads.AmobiAdView;

/**
 * Created by tuan on 09/08/2016.
 */
public class BannerAdPresenter extends VmaxAdListener
    implements AdEventInterface {
    private BannerAdView mBannerAdView;

    public BannerAdPresenter(BannerAdView bannerAdView) {
        mBannerAdView = bannerAdView;
    }

    public void loadAmobiAd(AmobiAdView amobiAdView, AmobiAdView.WidgetSize widgetSize) {
        amobiAdView.setEventListener(this);
        amobiAdView.loadAd(widgetSize);
    }

    public void loadVmaxAd(VmaxAdView vmaxAdView) {
        vmaxAdView.setAdSpotId(Config.BANNER_ADSPOTID);
        Log.i("AdSpotId", "AdSpotId: " + vmaxAdView.getAdSpotId());
        vmaxAdView.loadAd();
    }

    @Override
    public void onAdViewLoaded() {
        mBannerAdView.onHadAmobiBanner();
    }

    @Override
    public void onAdViewClose() {
        mBannerAdView.onNoAmobiBanner();
    }

    @Override
    public void onLoadAdError(ErrorCode errorCode) {
        mBannerAdView.onNoAmobiBanner();
    }

    @Override
    public VmaxAdView didFailedToLoadAd(String s) {
        mBannerAdView.onNoVmaxBanner();
        return null;
    }

    @Override
    public VmaxAdView didFailedToCacheAd(String s) {
        mBannerAdView.onNoVmaxBanner();
        return null;
    }

    @Override
    public void adViewDidLoadAd(VmaxAdView adView) {
        mBannerAdView.onHadVmaxBanner();
    }

    @Override
    public void adViewDidCacheAd(VmaxAdView adView) {
        mBannerAdView.onNoVmaxBanner();
    }

    @Override
    public void didInteractWithAd(VmaxAdView adView) {
        Log.i("tuan", "tuan");
    }

    @Override
    public void willDismissAd(VmaxAdView adView) {
        mBannerAdView.onAdDismiss();
    }

    @Override
    public void willPresentAd(VmaxAdView adView) {
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
        Log.i("tuan", "tuan");
    }
}
