package com.vannv83.libraryprebid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.vannv83.vlilibrary.AdListeners;
import com.vannv83.vlilibrary.Banner;
import com.vannv83.vlilibrary.Constants;
import com.vannv83.vlilibrary.Interstitial;

public class MainActivity extends AppCompatActivity {
    Button button, buttonin;
    FrameLayout adView;
    Banner banner;
    Interstitial interstitial;

    int dem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btnad);
        buttonin = findViewById(R.id.btnadinter);
        adView = findViewById(R.id.adView);

        banner = new Banner(MainActivity.this);
        banner.setSize(300, 250);
        banner.setAdUnit("valueimpression_imps");
        banner.setMillisAutoRefres(40000);

        interstitial = new Interstitial(MainActivity.this);
        interstitial.setAdUnit(Constants.PBS_CONFIG_ID_INTERSTITIAL);
        interstitial.setMillisAutoRefres(40000);
        dem = 0;

        buttonin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitial.loadAd();
                interstitial.setAdlistenners(new AdListeners() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        if (dem < 1) {
                            interstitial.loadAd();
                            dem++;
                        }
                    }
                });
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                banner.loadAd(adView);
                banner.setAdlistenners(new AdListeners() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.e("Listenners-AdLoaded", "OK");
                        System.out.println("onAdLoaded() CALLBACK");
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        Log.e("Listenners-AdClicked", "OK");
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e("Listenners-AdFailed", "OK");
                        System.out.println("onAdFailedToLoad() CALLBACK code: " + i);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                        Log.e("Listenners-AdLeft", "OK");
                    }
                });
            }

        });
    }
}