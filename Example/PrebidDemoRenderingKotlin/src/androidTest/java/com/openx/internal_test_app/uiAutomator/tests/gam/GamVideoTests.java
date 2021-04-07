package com.openx.internal_test_app.uiAutomator.tests.gam;

import com.openx.internal_test_app.R;
import com.openx.internal_test_app.uiAutomator.pages.AdBasePage;
import com.openx.internal_test_app.uiAutomator.utils.BaseUiAutomatorTest;

import org.junit.Test;

import static com.openx.internal_test_app.uiAutomator.pages.bidding.gam.GamInterstitialPage.VIDEO_DURATION_TIMEOUT;

public class GamVideoTests extends BaseUiAutomatorTest {
    @Test
    public void testGamOutstreamAppEvent() {
        homePage.getBannerPageFactory()
                .goToGamBannerExample(getStringResource(R.string.demo_bidding_gam_video_oustream_app_event))
                .openxViewShouldBePresent()
                .checkCommonEvents();
    }

    @Test
    public void testGamOutstreamNoBids() {
        homePage.setUseMockServer(false)
                .getBannerPageFactory()
                .goToGamBannerExample(getStringResource(R.string.demo_bidding_gam_video_outstream_no_bids))
                .gamVideoViewShouldBePresent()
                .checkCommonEvents();
    }

    @Test
    public void testGamOutstreamRandom() {
        homePage.getBannerPageFactory()
                .goToGamBannerExample(getStringResource(R.string.demo_bidding_gam_video_outstream_random))
                .checkCommonEvents();
    }

    @Test
    public void testGamVideoInterstitial320x480AppEvent() throws InterruptedException {
        verifyGamVideoInterstitialExample(R.string.demo_bidding_gam_interstitial_video_320_480_app_event);
    }

    @Test
    public void testGamVideoInterstitial320x480NoBids() throws InterruptedException {
        homePage.setUseMockServer(false);
        verifyGamVideoInterstitialExample(R.string.demo_bidding_gam_interstitial_video_320_480_no_bids);
    }

    @Test
    public void testGamVideoInterstitial320x480Random() {
        verifyGamVideoInterstitialRandom(R.string.demo_bidding_gam_interstitial_video_320_480_random);
    }

    @Test
    public void testGamVideoRewardedEndCard320x480Metadata() {
        verifyGamVideoRewardedInterstitialExample(R.string.demo_bidding_gam_video_rewarded_end_card_320_480_metadata);
    }

    @Test
    public void testGamVideoRewardedEndCard320x480NoBids() {
        homePage.setUseMockServer(false);
        verifyGamVideoRewardedInterstitialExample(R.string.demo_bidding_gam_video_rewarded_end_card_320_480_no_bids);
    }

    @Test
    public void testGamVideoRewardedEndCard320x480Random() {
        verifyGamVideoRewardedInterstitialExample(R.string.demo_bidding_gam_video_rewarded_end_card_320_480_random);
    }

    @Test
    public void testGamRewardedVideo() throws InterruptedException {
        verifyGamVideoInterstitialExample(R.string.demo_bidding_gam_video_rewarded_320_480_metadata);
    }

    @Test
    public void testApolloGamOutstreamAppEvent() {
        homePage.setUseMockServer(false)
                .getBannerPageFactory()
                .goToGamBannerExample(getStringResource(R.string.demo_bidding_gam_video_oustream_app_event))
                .openxViewShouldBePresent()
                .checkCommonEvents();
    }

    @Test
    public void testApolloGamRewardedVideo() throws InterruptedException {
        String exampleName = getStringResource(R.string.demo_bidding_gam_video_rewarded_end_card_320_480_metadata);
        homePage.setUseMockServer(false)
                .getInterstitialPageFactory()
                .goToGamInterstitialExample(exampleName)
                .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdLoaded)
                .sdkEventShouldNotBePresent(AdBasePage.SdkEvent.onAdFailed);
    }

    private void verifyGamVideoInterstitialExample(int stringResId) throws InterruptedException {
        String exampleName = getStringResource(stringResId);

        homePage.getInterstitialPageFactory()
                .goToGamInterstitialExample(exampleName)
                .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdLoaded)
                .showPrebidInterstitial()
                .gamOrOpenXVideoCreativeShouldBePresent()
                .closeEndCard()
                .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdDisplayed, VIDEO_DURATION_TIMEOUT) // wait until video is finished
                .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdClosed)
                .sdkEventShouldNotBePresent(AdBasePage.SdkEvent.onAdFailed);
    }

    private void verifyGamVideoRewardedInterstitialExample(int stringResId) {
        String exampleName = getStringResource(stringResId);

        try {
            homePage.getInterstitialPageFactory()
                    .goToGamInterstitialExample(exampleName)
                    .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdLoaded)
                    .showPrebidInterstitial()
                    .closeEndCard()
                    .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdDisplayed)
                    .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdClosed)
                    .sdkEventShouldNotBePresent(AdBasePage.SdkEvent.onAdFailed);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void verifyGamVideoInterstitialRandom(int stringResId) {
        final String exampleName = getStringResource(stringResId);

        homePage.getInterstitialPageFactory()
                .goToGamInterstitialExample(exampleName)
                .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdLoaded)
                .showPrebidInterstitial()
                .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdDisplayed, VIDEO_DURATION_TIMEOUT) // wait until video is finished
                .sdkEventShouldBePresent(AdBasePage.SdkEvent.onAdClosed)
                .sdkEventShouldNotBePresent(AdBasePage.SdkEvent.onAdFailed);
    }
}
