package com.openx.apollo.bidding.parallel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openx.apollo.bidding.data.AdSize;
import com.openx.apollo.bidding.data.bid.Bid;
import com.openx.apollo.bidding.enums.AdUnitFormat;
import com.openx.apollo.bidding.interfaces.InterstitialEventHandler;
import com.openx.apollo.bidding.interfaces.StandaloneInterstitialEventHandler;
import com.openx.apollo.bidding.listeners.InterstitialAdUnitListener;
import com.openx.apollo.bidding.listeners.InterstitialEventListener;
import com.openx.apollo.errors.AdException;
import com.openx.apollo.models.AdConfiguration;
import com.openx.apollo.utils.logger.OXLog;

import static com.openx.apollo.bidding.parallel.BaseInterstitialAdUnit.InterstitialAdUnitState.READY_FOR_LOAD;
import static com.openx.apollo.bidding.parallel.BaseInterstitialAdUnit.InterstitialAdUnitState.READY_TO_DISPLAY_GAM;

public class InterstitialAdUnit extends BaseInterstitialAdUnit {
    private static final String TAG = InterstitialAdUnit.class.getSimpleName();

    private final InterstitialEventHandler mEventHandler;

    @Nullable
    private InterstitialAdUnitListener mInterstitialAdUnitListener;

    //region ==================== Listener implementation
    private final InterstitialEventListener mInterstitialEventListener = new InterstitialEventListener() {
        @Override
        public void onOXBSdkWin() {
            if (isBidInvalid()) {
                changeOxbInterstitialAdUnitState(READY_FOR_LOAD);
                notifyErrorListener(new AdException(AdException.INTERNAL_ERROR, "WinnerBid is null when executing onOXBSdkWin."));
                return;
            }

            loadOxbAd();
        }

        @Override
        public void onAdServerWin() {
            changeOxbInterstitialAdUnitState(READY_TO_DISPLAY_GAM);
            notifyAdEventListener(AdListenerEvent.AD_LOADED);
        }

        @Override
        public void onAdFailed(AdException exception) {
            if (isBidInvalid()) {
                changeOxbInterstitialAdUnitState(READY_FOR_LOAD);
                notifyErrorListener(exception);
                return;
            }

            onOXBSdkWin();
        }

        @Override
        public void onAdClicked() {
            notifyAdEventListener(AdListenerEvent.AD_CLICKED);
        }

        @Override
        public void onAdClosed() {
            notifyAdEventListener(AdListenerEvent.AD_CLOSE);
        }

        @Override
        public void onAdDisplayed() {
            changeOxbInterstitialAdUnitState(READY_FOR_LOAD);
            notifyAdEventListener(AdListenerEvent.AD_DISPLAYED);
        }
    };
    //endregion ==================== Listener implementation

    /**
     * Instantiates an InterstitialAdUnit for the given configurationId and adUnitType.
     */
    public InterstitialAdUnit(Context context, String configId, AdUnitFormat adUnitFormat) {
        this(context, configId, adUnitFormat, null, new StandaloneInterstitialEventHandler());
    }

    /**
     * Instantiates an HTML InterstitialAdUnit for the given configurationId and minimum size in percentage (optional).
     */
    public InterstitialAdUnit(Context context, String configId,
                              @Nullable
                                     AdSize minSizePercentage) {
        this(context, configId, AdUnitFormat.DISPLAY, minSizePercentage, new StandaloneInterstitialEventHandler());
    }

    /**
     * Instantiates an InterstitialAdUnit for HTML GAM prebid integration with given minimum size in percentage (optional).
     */
    public InterstitialAdUnit(Context context, String configId,
                              @Nullable
                                     AdSize minSizePercentage,
                              InterstitialEventHandler eventHandler) {
        this(context, configId, AdUnitFormat.DISPLAY, minSizePercentage, eventHandler);
    }

    /**
     * Instantiates an OXBInterstitialAdUnit for GAM prebid integration with given adUnitType.
     */
    public InterstitialAdUnit(Context context, String configId,
                              @NonNull
                                     AdUnitFormat adUnitFormat,
                              InterstitialEventHandler eventHandler) {
        this(context, configId, adUnitFormat, null, eventHandler);
    }

    private InterstitialAdUnit(Context context, String configId,
                               @NonNull
                                      AdUnitFormat adUnitFormat,
                               @Nullable
                                      AdSize minSizePercentage,
                               InterstitialEventHandler eventHandler) {
        super(context);
        mEventHandler = eventHandler;
        mEventHandler.setInterstitialEventListener(mInterstitialEventListener);

        AdConfiguration adUnitConfiguration = new AdConfiguration();
        adUnitConfiguration.setConfigId(configId);
        adUnitConfiguration.setMinSizePercentage(minSizePercentage);
        adUnitConfiguration.setAdUnitIdentifierType(mapOxbAdUnitTypeToAdConfigAdUnitType(adUnitFormat));

        init(adUnitConfiguration);
    }

    public void destroy() {
        super.destroy();
        if (mEventHandler != null) {
            mEventHandler.destroy();
        }
    }

    //region ==================== getters and setters
    public void setInterstitialAdUnitListener(
        @Nullable
            InterstitialAdUnitListener interstitialAdUnitListener) {
        mInterstitialAdUnitListener = interstitialAdUnitListener;
    }
    //endregion ==================== getters and setters

    @Override
    void requestAdWithBid(
        @Nullable
            Bid bid) {
        mEventHandler.requestAdWithBid(bid);
    }

    @Override
    void showGamAd() {
        mEventHandler.show();
    }

    @Override
    void notifyAdEventListener(AdListenerEvent adListenerEvent) {
        if (mInterstitialAdUnitListener == null) {
            OXLog.debug(TAG, "notifyAdEventListener: Failed. AdUnitListener is null. Passed listener event: " + adListenerEvent);
            return;
        }

        switch (adListenerEvent) {
            case AD_CLOSE:
                mInterstitialAdUnitListener.onAdClosed(InterstitialAdUnit.this);
                break;
            case AD_LOADED:
                mInterstitialAdUnitListener.onAdLoaded(InterstitialAdUnit.this);
                break;
            case AD_DISPLAYED:
                mInterstitialAdUnitListener.onAdDisplayed(InterstitialAdUnit.this);
                break;
            case AD_CLICKED:
                mInterstitialAdUnitListener.onAdClicked(InterstitialAdUnit.this);
                break;
        }
    }

    @Override
    void notifyErrorListener(AdException exception) {
        if (mInterstitialAdUnitListener != null) {
            mInterstitialAdUnitListener.onAdFailed(InterstitialAdUnit.this, exception);
        }
    }

    private AdConfiguration.AdUnitIdentifierType mapOxbAdUnitTypeToAdConfigAdUnitType(AdUnitFormat adUnitFormat) {
        switch (adUnitFormat) {
            case DISPLAY:
                return AdConfiguration.AdUnitIdentifierType.INTERSTITIAL;
            case VIDEO:
                return AdConfiguration.AdUnitIdentifierType.VAST;
            default:
                OXLog.debug(TAG, "setAdUnitIdentifierType: Provided AdUnitType [" + adUnitFormat + "] doesn't match any expected adUnitType.");
                return null;
        }
    }
}
