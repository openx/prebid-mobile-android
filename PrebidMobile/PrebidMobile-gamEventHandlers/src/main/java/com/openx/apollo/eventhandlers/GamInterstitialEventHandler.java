package com.openx.apollo.eventhandlers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.openx.apollo.bidding.data.bid.Bid;
import com.openx.apollo.bidding.interfaces.InterstitialEventHandler;
import com.openx.apollo.bidding.listeners.InterstitialEventListener;
import com.openx.apollo.errors.AdException;
import com.openx.apollo.eventhandlers.global.Constants;
import com.openx.apollo.utils.logger.OXLog;

public class GamInterstitialEventHandler implements InterstitialEventHandler, GamAdEventListener {
    private static final String TAG = GamInterstitialEventHandler.class.getSimpleName();

    private static final long TIMEOUT_APP_EVENT_MS = 600;

    private PublisherInterstitialAdWrapper mRequestInterstitial;

    private final Context mApplicationContext;
    private final String mGamAdUnitId;

    private InterstitialEventListener mInterstitialEventListener;
    private Handler mAppEventHandler;

    private boolean mIsExpectingAppEvent;
    private boolean mDidNotifiedBidWin;

    public GamInterstitialEventHandler(Context applicationContext, String gamAdUnitId) {
        mApplicationContext = applicationContext.getApplicationContext();
        mGamAdUnitId = gamAdUnitId;
    }

    //region ==================== GAM AppEventsListener Implementation
    @Override
    public void onEvent(AdEvent adEvent) {
        switch (adEvent) {
            case APP_EVENT_RECEIVED:
                handleAppEvent();
                break;
            case CLOSED:
                mInterstitialEventListener.onAdClosed();
                break;
            case FAILED:
                handleAdFailure(adEvent.getErrorCode());
                break;
            case DISPLAYED:
                mInterstitialEventListener.onAdDisplayed();
                break;
            case CLICKED:
                mInterstitialEventListener.onAdClicked();
                break;
            case LOADED:
                primaryAdReceived();
                break;
        }
    }
    //endregion ==================== GAM AppEventsListener Implementation

    //region ==================== OX EventHandler Implementation
    @Override
    public void show() {
        if (mRequestInterstitial != null && mRequestInterstitial.isLoaded()) {
            mRequestInterstitial.show();
        }
        else {
            mInterstitialEventListener.onAdFailed(new AdException(AdException.THIRD_PARTY, "GAM SDK - failed to display ad."));
        }
    }

    @Override
    public void setInterstitialEventListener(
        @NonNull
            InterstitialEventListener interstitialEventListener) {
        mInterstitialEventListener = interstitialEventListener;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void requestAdWithBid(Bid bid) {
        mIsExpectingAppEvent = false;
        mDidNotifiedBidWin = false;

        initPublisherInterstitialAd();

        if (bid != null && bid.getPrice() > 0) {
            mIsExpectingAppEvent = true;
        }

        if (mRequestInterstitial == null) {
            handleAdFailure(Constants.ERROR_CODE_INTERNAL_ERROR);
            return;
        }

        mRequestInterstitial.loadAd(bid);
    }

    @Override
    public void trackImpression() {

    }

    @Override
    public void destroy() {
        cancelTimer();
    }
    //endregion ==================== OX EventHandler Implementation

    private void initPublisherInterstitialAd() {
        if (mRequestInterstitial != null) {
            mRequestInterstitial = null;
        }

        mRequestInterstitial = PublisherInterstitialAdWrapper.newInstance(mApplicationContext, mGamAdUnitId, this);
    }

    private void primaryAdReceived() {
        if (mIsExpectingAppEvent) {
            if (mAppEventHandler != null) {
                OXLog.debug(TAG, "primaryAdReceived: AppEventTimer is not null. Skipping timer scheduling.");
                return;
            }

            scheduleTimer();
        }
        else if (!mDidNotifiedBidWin) {
            mInterstitialEventListener.onAdServerWin();
        }
    }

    private void handleAppEvent() {
        if (!mIsExpectingAppEvent) {
            OXLog.debug(TAG, "appEventDetected: Skipping event handling. App event is not expected");
            return;
        }

        cancelTimer();
        mIsExpectingAppEvent = false;
        mDidNotifiedBidWin = true;
        mInterstitialEventListener.onOXBSdkWin();
    }

    private void scheduleTimer() {
        cancelTimer();

        mAppEventHandler = new Handler(Looper.getMainLooper());
        mAppEventHandler.postDelayed(this::handleAppEventTimeout, TIMEOUT_APP_EVENT_MS);
    }

    private void cancelTimer() {
        if (mAppEventHandler != null) {
            mAppEventHandler.removeCallbacksAndMessages(null);
        }
        mAppEventHandler = null;
    }

    private void handleAppEventTimeout() {
        cancelTimer();
        mIsExpectingAppEvent = false;
        mInterstitialEventListener.onAdServerWin();
    }

    private void handleAdFailure(int errorCode) {
        switch (errorCode) {
            case Constants.ERROR_CODE_INTERNAL_ERROR:
                mInterstitialEventListener.onAdFailed(new AdException(AdException.THIRD_PARTY, "GAM SDK encountered an internal error."));
                break;
            case Constants.ERROR_CODE_INVALID_REQUEST:
                mInterstitialEventListener.onAdFailed(new AdException(AdException.THIRD_PARTY, "GAM SDK - invalid request error."));
                break;
            case Constants.ERROR_CODE_NETWORK_ERROR:
                mInterstitialEventListener.onAdFailed(new AdException(AdException.THIRD_PARTY, "GAM SDK - network error."));
                break;
            case Constants.ERROR_CODE_NO_FILL:
                mInterstitialEventListener.onAdFailed(new AdException(AdException.THIRD_PARTY, "GAM SDK - no fill."));
                break;
            default:
                mInterstitialEventListener.onAdFailed(new AdException(AdException.THIRD_PARTY, "GAM SDK - failed with errorCode: " + errorCode));
        }
    }
}
