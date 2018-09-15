package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.AppStateManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.Application;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.BuildConfig;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.helpers.SharedPrefHelper;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.utils.Utils;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdsManager {

    private static AdsManager manager;
    private InterstitialAd interstitialAd = null;
    private com.facebook.ads.InterstitialAd fbInterstitialAd = null;

    private final String TAG = AdsManager.class.getName();

    private AdsManager() {
        if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
            Context context = Application.getContext();
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit));
            fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.interstitial_facebook));
            // load the ads and cache them for later use
            loadInterstitialAd();
            loadFacebookInterstitialAd();
        }
    }

    public static AdsManager getInstance() {
        if (manager == null) {
            manager = new AdsManager();
        }
        return manager;
    }

    private AdRequest prepareAdRequest() {
        AdRequest adRequest;
        Context context = Application.getContext();
        if (SharedPrefHelper.readBoolean(context, context.getString(R.string.npa))) {
            Bundle bundle = new Bundle();
            bundle.putString(context.getString(R.string.npa), "1");
            Log.d(TAG, "consent status: npa");
            adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
        } else {
            Log.d(TAG, "consent status: pa");
            adRequest = new AdRequest.Builder().build();
        }
        return adRequest;
    }

    public void showBanner(final AdView banner) {
        if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
            if (banner != null) {
                if (BuildConfig.DEBUG) {
                    banner.loadAd(new AdRequest.Builder().addTestDevice("8F14986600C19D5CB98F0125581FBBF4").build());
                } else {
                    banner.loadAd(prepareAdRequest());
                }
                banner.setAdListener(new AdListener() {

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.d(TAG, "AdMob BannerAd -> onAdFailedToLoad");
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.d(TAG, "AdMob BannerAd -> onAdLoaded");
                        if (banner.getVisibility() == View.GONE) {
                            banner.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }

    private void loadInterstitialAd() {
        if (interstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                if (BuildConfig.DEBUG) {
                    interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("8F14986600C19D5CB98F0125581FBBF4").build());
                } else {
                    interstitialAd.loadAd(prepareAdRequest());
                }
                interstitialAd.setAdListener(new AdListener() {

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.d(TAG, "AdMob InterstitialAd -> onAdClosed");
                        loadInterstitialAd();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.d(TAG, "AdMob InterstitialAd -> onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.d(TAG, "AdMob InterstitialAd -> onAdFailedToLoad");
                    }
                });
            }
        }
    }

    public void showInterstitialAd() {
        if (interstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                loadInterstitialAd();
            }
        }
    }

    private void loadFacebookInterstitialAd() {
        if (fbInterstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                fbInterstitialAd.setAdListener(new AbstractAdListener() {

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Ad error callback
                        Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        super.onAdLoaded(ad);
                        Log.d(TAG, "Facebook InterstitialAd -> onAdLoaded");
                        // Interstitial ad is loaded and ready to be displayed
                        // Show the ad
                    }

                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        super.onInterstitialDisplayed(ad);
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        super.onInterstitialDismissed(ad);
                        loadFacebookInterstitialAd();
                    }
                });
                fbInterstitialAd.loadAd();
            }
        }
    }

    public void showFacebookInterstitialAd() {
        if (fbInterstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && fbInterstitialAd.isAdLoaded()) {
                fbInterstitialAd.show();
            } else {
                loadFacebookInterstitialAd();
                showInterstitialAd();
            }
        }
    }

}