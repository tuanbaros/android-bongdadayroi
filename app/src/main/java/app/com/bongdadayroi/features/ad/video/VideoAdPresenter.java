package app.com.bongdadayroi.features.ad.video;

import android.view.View;
import android.widget.FrameLayout;

import com.vmax.android.ads.api.VmaxAdView;
import com.vmax.android.ads.common.VmaxAdListener;

import app.com.bongdadayroi.myapp.Config;
import vn.amobi.util.ads.video.AmobiVideoAd;
import vn.amobi.util.ads.video.AmobiVideoAdListener;

/**
 * Created by Tuan on 8/15/2016.
 */
public class VideoAdPresenter
    implements AmobiVideoAdListener {
    private VideoAdView mVideoAdView;

    public VideoAdPresenter(VideoAdView videoAdView) {
        mVideoAdView = videoAdView;
    }

    public void loadAmobiVideoAd() {
        AmobiVideoAd.getInstance().setVideoAdListener(this);
    }

    public void loadVmaxVideoAd(final FrameLayout aVideoAdFrameLayout) {
        final VmaxAdView vmaxAdView =
            new VmaxAdView(aVideoAdFrameLayout.getContext(), Config.VIDEO_ADSPOTID,
                VmaxAdView.UX_INTERSTITIAL);
        vmaxAdView.setAdListener(new VmaxAdListener() {
            @Override
            public VmaxAdView didFailedToLoadAd(String s) {
                mVideoAdView.onNoVmaxVideo();
                return null;
            }

            @Override
            public VmaxAdView didFailedToCacheAd(String s) {
                mVideoAdView.onNoVmaxVideo();
                return null;
            }

            @Override
            public void adViewDidLoadAd(VmaxAdView adView) {
                aVideoAdFrameLayout.removeAllViews();
                aVideoAdFrameLayout.addView(vmaxAdView);
                aVideoAdFrameLayout.setVisibility(View.VISIBLE);
                vmaxAdView.showAd();
            }

            @Override
            public void adViewDidCacheAd(VmaxAdView adView) {
            }

            @Override
            public void didInteractWithAd(VmaxAdView adView) {
            }

            @Override
            public void willDismissAd(VmaxAdView adView) {
                aVideoAdFrameLayout.setVisibility(View.GONE);
                mVideoAdView.onVideoAdFinish();
            }

            @Override
            public void willPresentAd(VmaxAdView adView) {
                aVideoAdFrameLayout.setVisibility(View.VISIBLE);
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
        vmaxAdView.cacheAd();
    }

    @Override
    public void onAdAvailable() {
        mVideoAdView.onHadAmobiVideo();
    }

    @Override
    public void onPrepareError() {
        mVideoAdView.onNoAmobiVideo();
    }

    @Override
    public void onAdStarted() {
    }

    @Override
    public void onAdFinished() {
        mVideoAdView.onVideoAdFinish();
    }
}
