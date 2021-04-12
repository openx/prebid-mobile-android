package org.prebid.mobile.rendering.mraid.methods;

import org.prebid.mobile.rendering.models.HTMLCreative;
import org.prebid.mobile.rendering.models.internal.MraidEvent;
import org.prebid.mobile.rendering.utils.logger.OXLog;
import org.prebid.mobile.rendering.views.webview.PrebidWebViewBanner;
import org.prebid.mobile.rendering.views.webview.PrebidWebViewBase;
import org.prebid.mobile.rendering.views.webview.WebViewBase;

import java.lang.ref.WeakReference;

public class TwoPartExpandRunnable implements Runnable {
    private static final String TAG = TwoPartExpandRunnable.class.getSimpleName();

    private WeakReference<HTMLCreative> mWeakHtmlCreative;
    private MraidEvent mMraidEvent;
    private WebViewBase mOldWebViewBase;
    private MraidController mMraidController;

    TwoPartExpandRunnable(HTMLCreative htmlCreative, MraidEvent mraidEvent,
                          WebViewBase oldWebViewBase, MraidController mraidController) {
        mWeakHtmlCreative = new WeakReference<>(htmlCreative);
        mMraidEvent = mraidEvent;
        mOldWebViewBase = oldWebViewBase;
        mMraidController = mraidController;
    }

    // NOTE: handleMRAIDEventsInCreative ACTION_EXPAND is invoked from a background thread.
    // This means the webview instantiation should be performed from a UI thread.
    @Override
    public void run() {
        HTMLCreative htmlCreative = mWeakHtmlCreative.get();
        if (htmlCreative == null) {
            OXLog.error(TAG, "HTMLCreative object is null");
            return;
        }

        PrebidWebViewBase prebidWebViewBanner = new PrebidWebViewBanner(mOldWebViewBase.getContext(), mMraidController.mInterstitialManager);
        //inject mraid.js & load url here, for 2part expand
        prebidWebViewBanner.setOldWebView(mOldWebViewBase);
        prebidWebViewBanner.initTwoPartAndLoad(mMraidEvent.mraidActionHelper);
        prebidWebViewBanner.setWebViewDelegate(htmlCreative);
        prebidWebViewBanner.setCreative(htmlCreative);
        //Set a view before handling any action.
        htmlCreative.setCreativeView(prebidWebViewBanner);
        htmlCreative.setTwoPartNewWebViewBase(prebidWebViewBanner);
        mMraidController.expand(mOldWebViewBase, prebidWebViewBanner, mMraidEvent);
    }
}
