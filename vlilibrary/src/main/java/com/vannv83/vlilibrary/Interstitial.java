package com.vannv83.vlilibrary;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.vannv83.prebidmobile_core.mobile.AdUnit;
import com.vannv83.prebidmobile_core.mobile.Host;
import com.vannv83.prebidmobile_core.mobile.InterstitialAdUnit;
import com.vannv83.prebidmobile_core.mobile.OnCompleteListener;
import com.vannv83.prebidmobile_core.mobile.PrebidMobile;
import com.vannv83.prebidmobile_core.mobile.ResultCode;


public class Interstitial {
    private int refreshCount = 0;
    private AdUnit adUnit;
    private ResultCode resultCode;
    private PublisherAdRequest request;
    private Context context;
    private int autoRefresh;
    private AdListeners adListeners;

    private PublisherInterstitialAd amInterstitial;


    public void loadAd() {
        setupPBInterstitial();
        setupAMInterstitial();
        loadInterstitial();
    }

    public void setMillisAutoRefres(int autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public int getRefreshCount() {
        return refreshCount;
    }

    private void setupPBInterstitial() {
        PrebidMobile.setPrebidServerHost(Host.APPNEXUS);
        PrebidMobile.setPrebidServerAccountId(Constants.PBS_ACCOUNT_ID);
//
//        Host.CUSTOM.setHostUrl("https://s2s.valueimpression.com/openrtb2/auction");
//        PrebidMobile.setPrebidServerHost(Host.CUSTOM);
//        PrebidMobile.setPrebidServerAccountId("valueimpression_global");
    }

    public void setAdUnit(String adUnitID) {
        this.adUnit = new InterstitialAdUnit(adUnitID);
    }

    public Interstitial(Context context) {
        this.context = context;
    }

    ;

    private void setupAMInterstitial() {

        this.amInterstitial = new PublisherInterstitialAd(context);
        amInterstitial.setAdUnitId(Constants.DFP_BANNER_ADUNIT_ID_300x250);
        amInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                amInterstitial.show();
                adUnit.stopAutoRefresh();
                Log.d("AD_STATUS", "LOADED");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adListeners.onAdClicked();
                Log.d("AD_STATUS", "CLICKED");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adListeners.onAdClosed();
                Log.d("AD_STATUS", "CLOSED");
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
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adUnit.stopAutoRefresh();
                loadInterstitial();
                adListeners.onAdFailedToLoad(i);
                Log.d("AD_STATUS", "FAILED" + i);
            }
        });
    }

    private void loadInterstitial() {

        adUnit.setAutoRefreshPeriodMillis(autoRefresh);
        PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();
        request = builder.build();
        adUnit.fetchDemand(request, new OnCompleteListener() {
            @Override
            public void onComplete(ResultCode resultCode) {
                Interstitial.this.resultCode = resultCode;
                amInterstitial.loadAd(request);
                refreshCount++;
            }
        });
    }

    public void setAdlistenners(final AdListeners adlistenners) {
        this.adListeners = adlistenners;
    }

    public void stopAutoRefresh() {
        if (adUnit != null) {
            adUnit.stopAutoRefresh();
        }
    }

    public void startAutoRefresh() {
        loadInterstitial();
    }

    public void desTroy() {
        if (adUnit != null) {
            adUnit.stopAutoRefresh();
            adUnit = null;
        }
    }
}
