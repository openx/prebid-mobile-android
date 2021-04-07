package com.openx.apollo.views.browser;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.openx.apollo.utils.logger.OXLog;
import com.openx.apollo.utils.url.UrlHandler;
import com.openx.apollo.utils.url.action.DeepLinkAction;
import com.openx.apollo.utils.url.action.DeepLinkPlusAction;
import com.openx.apollo.utils.url.action.UrlAction;

class AdBrowserActivityWebViewClient extends WebViewClient {
    private static final String TAG = AdBrowserActivityWebViewClient.class.getSimpleName();

    private AdBrowserWebViewClientListener mAdBrowserWebViewClientListener;

    private boolean mUrlHandleInProgress;

    AdBrowserActivityWebViewClient(AdBrowserWebViewClientListener adBrowserWebViewClientListener) {
        mAdBrowserWebViewClientListener = adBrowserWebViewClientListener;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mAdBrowserWebViewClientListener != null) {
            mAdBrowserWebViewClientListener.onPageFinished();
        }
        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        UrlHandler urlHandler = createUrlHandler();
        if (mUrlHandleInProgress) {
            return false;
        }
        else {
            mUrlHandleInProgress = true;
            return urlHandler
                .handleResolvedUrl(view.getContext(),
                                   url,
                                   null,
                                   true); // navigation is performed by user
        }
    }

    private UrlHandler createUrlHandler() {
        return new UrlHandler.Builder()
            .withDeepLinkPlusAction(new DeepLinkPlusAction())
            .withDeepLinkAction(new DeepLinkAction())
            .withResultListener(new UrlHandler.UrlHandlerResultListener() {
                @Override
                public void onSuccess(String url, UrlAction urlAction) {
                    mUrlHandleInProgress = false;
                    if (mAdBrowserWebViewClientListener != null) {
                        mAdBrowserWebViewClientListener.onUrlHandleSuccess();
                    }
                }

                @Override
                public void onFailure(String url) {
                    OXLog.debug(TAG, "Failed to handleUrl: " + url);
                    mUrlHandleInProgress = false;
                }
            })
            .build();
    }

    public interface AdBrowserWebViewClientListener {
        void onPageFinished();

        void onUrlHandleSuccess();
    }
}
