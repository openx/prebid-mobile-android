package org.prebid.mobile.rendering.bidding.listeners;

import org.prebid.mobile.rendering.bidding.parallel.RewardedAdUnit;
import org.prebid.mobile.rendering.errors.AdException;

/**
 * Listener interface representing OXBRewardedAdUnit events.
 * All methods will be invoked on the main thread.
 */
public interface RewardedAdUnitListener {
    /**
     * Executed when the ad is loaded and is ready for display.
     *
     * @param rewardedAdUnit view of the corresponding event. Contains reward instance inside. Prebod reward is always null.
     */
    void onAdLoaded(RewardedAdUnit rewardedAdUnit);

    /**
     * Executed when the ad is displayed on screen.
     *
     * @param rewardedAdUnit view of the corresponding event.
     */
    void onAdDisplayed(RewardedAdUnit rewardedAdUnit);

    /**
     * Executed when an error is encountered on initialization / loading or display step.
     *
     * @param rewardedAdUnit view of the corresponding event.
     * @param exception  exception containing detailed message and error type.
     */
    void onAdFailed(RewardedAdUnit rewardedAdUnit, AdException exception);

    /**
     * Executed when rewardedAdUnit is clicked.
     *
     * @param rewardedAdUnit view of the corresponding event.
     */
    void onAdClicked(RewardedAdUnit rewardedAdUnit);

    /**
     * Executed when rewardedAdUnit is closed.
     *
     * @param rewardedAdUnit view of the corresponding event.
     */
    void onAdClosed(RewardedAdUnit rewardedAdUnit);

    /**
     * Executed when user receives reward.
     *
     * @param rewardedAdUnit view of the corresponding event. Contains reward instance inside. Prebid reward is always null.
     */
    void onUserEarnedReward(RewardedAdUnit rewardedAdUnit);
}
