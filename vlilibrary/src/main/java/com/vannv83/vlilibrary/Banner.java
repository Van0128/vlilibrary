package com.vannv83.vlilibrary;


import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import com.vannv83.prebidmobile_core.mobile.AdUnit;
import com.vannv83.prebidmobile_core.mobile.BannerAdUnit;
import com.vannv83.prebidmobile_core.mobile.Host;
import com.vannv83.prebidmobile_core.mobile.OnCompleteListener;
import com.vannv83.prebidmobile_core.mobile.PrebidMobile;
import com.vannv83.prebidmobile_core.mobile.ResultCode;
import com.vannv83.prebidmobile_core.mobile.addendum.AdViewUtils;
import com.vannv83.prebidmobile_core.mobile.addendum.PbFindSizeError;

public final class Banner {
    private int refresCount = 0;
    private AdUnit adUnit;
    private PublisherAdView amBanner;
    private com.vannv83.vlilibrary.AdSize adsize;
    private Context context;
    private int autoRefresh = 30000;
    private ResultCode resultCode;
    private AdListeners adListeners;
    private PublisherAdRequest request;

    public void setMillisAutoRefres(int autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public int getRefresCount() {
        return refresCount;
    }

    private void SetupPB(){
        Host.CUSTOM.setHostUrl("https://s2s.valueimpression.com/openrtb2/auction");
        PrebidMobile.setPrebidServerHost(Host.CUSTOM);
        PrebidMobile.setPrebidServerAccountId("valueimpression_global");

//
//        PrebidMobile.setPrebidServerHost(Host.APPNEXUS);
//        PrebidMobile.setPrebidServerAccountId(Constants.PBS_ACCOUNT_ID);

    }

    public void setSize(int with, int height){
        this.adsize = new com.vannv83.vlilibrary.AdSize(with, height);

    }

    public void setAdUnit(String adUnitID){
        this.adUnit = new BannerAdUnit(adUnitID,adsize.getWith(),adsize.getHeight());
    }

    public Banner(Context context){
        this.context = context;
    }


    private void setUpBanner(final FrameLayout adFrame){
        this.amBanner = new PublisherAdView(context);
        amBanner.setAdUnitId(Constants.DFP_BANNER_ADUNIT_ID_300x250);
        amBanner.setAdSizes(new AdSize(adsize.getWith(),adsize.getHeight()));
        adFrame.removeAllViews();
        adFrame.addView(amBanner);
        amBanner.setAdListener(new AdListener(){
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adListeners.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adListeners.onAdClosed();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                adListeners.onAdImpression();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                adListeners.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                adListeners.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                AdViewUtils.findPrebidCreativeSize(amBanner, new AdViewUtils.PbFindSizeListener() {
                    @Override
                    public void success(int width, int height) {
                        amBanner.setAdSizes(new AdSize(width, height));
                    }

                    @Override
                    public void failure(@NonNull PbFindSizeError error) {
                        Log.d("MyTag", "error: " + error);
                    }
                });
                adListeners.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adUnit.stopAutoRefresh();
                loadBanner();
                adListeners.onAdFailedToLoad(i);
            }
        });
    }

    private void loadBanner(){
        final PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();
        request = builder.build();
        //region PrebidMobile Mobile API 1.0 usage
        adUnit.setAutoRefreshPeriodMillis(autoRefresh);
        adUnit.fetchDemand(request, new OnCompleteListener() {
            @Override
            public void onComplete(ResultCode resultCode) {
                Banner.this.resultCode = resultCode;
                amBanner.loadAd(request);
                refresCount++;
            }
        });

    }

    public void loadAd(FrameLayout layout){
        SetupPB();
        setUpBanner(layout);
        loadBanner();
    }

    public void setAdlistenners(final AdListeners adlistenners){
        this.adListeners = adlistenners;
    }

    public void stopAutoRefresh() {
        if (adUnit != null) {
            adUnit.stopAutoRefresh();
        }
    }

    public void startAutoRefresh(){
        loadBanner();
    }

    public void desTroy(){
        if (adUnit != null) {
            adUnit.stopAutoRefresh();
            adUnit = null;
        }
    }
}
