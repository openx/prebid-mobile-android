package com.openx.internal_test_app.uiAutomator.pages.factory;

import androidx.test.uiautomator.UiDevice;

import com.openx.internal_test_app.uiAutomator.pages.PageFactory;
import com.openx.internal_test_app.uiAutomator.pages.bidding.gam.GamNativeStylesPage;
import com.openx.internal_test_app.uiAutomator.pages.bidding.mopub.MoPubNativeStylesPage;
import com.openx.internal_test_app.uiAutomator.pages.bidding.ppm.PpmNativePage;
import com.openx.internal_test_app.uiAutomator.pages.bidding.ppm.PpmNativeStylesPage;

public class NativePageFactory extends PageFactory{
    public NativePageFactory(UiDevice device) {
        super(device);
    }

    public MoPubNativeStylesPage goToMoPubNativeStyles(String example) {
        findListItem(example);
        return new MoPubNativeStylesPage(device);
    }

    public GamNativeStylesPage goToGamNativeStyles(String example) {
        findListItem(example);
        return new GamNativeStylesPage(device);
    }

    public PpmNativeStylesPage goToPpmNativeStyles(String example) {
        findListItem(example);
        return new PpmNativeStylesPage(device);
    }

    public PpmNativePage goToPpmNative(String example) {
        findListItem(example);
        return new PpmNativePage(device);
    }

    public GamNativePage goToGamNative(String example) {
        findListItem(example);
        return new GamNativePage(device);
    }
}
