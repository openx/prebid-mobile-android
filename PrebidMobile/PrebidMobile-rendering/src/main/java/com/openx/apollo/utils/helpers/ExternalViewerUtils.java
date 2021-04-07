package com.openx.apollo.utils.helpers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.openx.apollo.listeners.OnBrowserActionResultListener;
import com.openx.apollo.listeners.OnBrowserActionResultListener.BrowserActionResult;
import com.openx.apollo.sdk.ApolloSettings;
import com.openx.apollo.utils.logger.OXLog;
import com.openx.apollo.utils.url.ActionNotResolvedException;
import com.openx.apollo.views.browser.AdBrowserActivity;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ExternalViewerUtils {

    public static final String TAG = ExternalViewerUtils.class.getSimpleName();

    public static boolean isBrowserActivityCallable(Context context) {
        if (context == null) {
            OXLog.debug(TAG, "isBrowserActivityCallable(): returning false. Context is null");
            return false;
        }

        Intent browserActivityIntent = new Intent(context, AdBrowserActivity.class);
        return isActivityCallable(context, browserActivityIntent);
    }

    /**
     * Checks if the intent's activity is declared in the manifest
     */
    public static boolean isActivityCallable(Context context, Intent intent) {
        if (context == null || intent == null) {
            OXLog.debug(TAG, "isActivityCallable(): returning false. Intent or context is null");
            return false;
        }

        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void startExternalVideoPlayer(Context context, String url) {
        if (context != null && url != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "video/*");
            context.startActivity(intent);
        }
    }

    public static void launchApplicationUrl(Context context, Uri uri)
    throws ActionNotResolvedException {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (!isActivityCallable(context, intent)) {
            throw new ActionNotResolvedException("launchApplicationUrl: Failure. No activity was found to handle action for " + uri);
        }

        launchApplicationIntent(context, intent);
    }

    public static void startBrowser(Context context, String url,
                                    boolean shouldFireEvents, @Nullable
                                        OnBrowserActionResultListener onBrowserActionResultListener) {
        startBrowser(context, url, -1, shouldFireEvents,  onBrowserActionResultListener);
    }

    public static void startBrowser(Context context, String url, int broadcastId,
                                    boolean shouldFireEvents, @Nullable
                                        OnBrowserActionResultListener onBrowserActionResultListener) {

        Intent intent = new Intent(context, AdBrowserActivity.class);
        intent.putExtra(AdBrowserActivity.EXTRA_URL, url);
        intent.putExtra(AdBrowserActivity.EXTRA_DENSITY_SCALING_ENABLED, false);
        intent.putExtra(AdBrowserActivity.EXTRA_ALLOW_ORIENTATION_CHANGES, true);
        intent.putExtra(AdBrowserActivity.EXTRA_SHOULD_FIRE_EVENTS, shouldFireEvents);
        intent.putExtra(AdBrowserActivity.EXTRA_BROADCAST_ID, broadcastId);

        if (!(context instanceof Activity)) {
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        }

        if (!ApolloSettings.useExternalBrowser && isActivityCallable(context, intent)) {
            context.startActivity(intent);
            notifyBrowserActionSuccess(BrowserActionResult.INTERNAL_BROWSER, onBrowserActionResultListener);
        }
        else {
            startExternalBrowser(context, url);
            notifyBrowserActionSuccess(BrowserActionResult.EXTERNAL_BROWSER, onBrowserActionResultListener);
        }
    }

    private static void startExternalBrowser(Context context, String url) {
        if (context == null || url == null) {
            OXLog.error(TAG, "startExternalBrowser: Failure. Context or URL is null");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (URLUtil.isValidUrl(url) || isActivityCallable(context, intent)) {
            context.startActivity(intent);
        }
        else {
            OXLog.error(TAG, "No activity available to handle action " + intent.toString());
        }
    }

    @VisibleForTesting
    static void launchApplicationIntent(
        @NonNull
        final Context context,
        @NonNull
        final Intent intent)
    throws ActionNotResolvedException {
        if (!(context instanceof Activity)) {
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        }

        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            throw new ActionNotResolvedException(e);
        }
    }

    private static void notifyBrowserActionSuccess(BrowserActionResult browserActionResult,
                                                   @Nullable
                                                       OnBrowserActionResultListener onBrowserActionResultListener) {

        if (onBrowserActionResultListener == null) {
            OXLog.debug(TAG, "notifyBrowserActionSuccess(): Failed. BrowserActionResultListener is null.");
            return;
        }

        onBrowserActionResultListener.onSuccess(browserActionResult);
    }
}
