package com.openx.apollo.mraid.methods;

import com.openx.apollo.models.HTMLCreative;
import com.openx.apollo.models.internal.MraidEvent;
import com.openx.apollo.utils.logger.OXLog;
import com.openx.apollo.views.webview.OpenXWebViewBanner;
import com.openx.apollo.views.webview.OpenXWebViewBase;
import com.openx.apollo.views.webview.WebViewBase;

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

        OpenXWebViewBase openXWebViewBanner = new OpenXWebViewBanner(mOldWebViewBase.getContext(), mMraidController.mInterstitialManager);
        //inject mraid.js & load url here, for 2part expand
        openXWebViewBanner.setOldWebView(mOldWebViewBase);
        openXWebViewBanner.initTwoPartAndLoad(mMraidEvent.mraidActionHelper);
        openXWebViewBanner.setWebViewDelegate(htmlCreative);
        openXWebViewBanner.setCreative(htmlCreative);
        //Set a view before handling any action.
        htmlCreative.setCreativeView(openXWebViewBanner);
        htmlCreative.setTwoPartNewWebViewBase(openXWebViewBanner);
        mMraidController.expand(mOldWebViewBase, openXWebViewBanner, mMraidEvent);
    }
}
