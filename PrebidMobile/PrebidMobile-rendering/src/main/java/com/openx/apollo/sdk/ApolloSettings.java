package com.openx.apollo.sdk;

import android.content.Context;
import android.util.Log;

import com.openx.apollo.BuildConfig;
import com.openx.apollo.errors.AdException;
import com.openx.apollo.mraid.MraidEnv;
import com.openx.apollo.networking.BaseNetworkTask;
import com.openx.apollo.sdk.deviceData.listeners.SdkInitListener;
import com.openx.apollo.session.manager.OmAdSessionManager;
import com.openx.apollo.utils.helpers.AppInfoManager;
import com.openx.apollo.utils.logger.OXLog;

import java.util.concurrent.atomic.AtomicInteger;

public class ApolloSettings {
    private static final String TAG = ApolloSettings.class.getSimpleName();

    public static final String SCHEME_HTTPS = "https";
    public static final String SCHEME_HTTP = "http";

    /**
     * SDK version
     */
    public static final String SDK_VERSION = "1.2.0";

    /**
     * SDK name provided for MRAID_ENV in {@link MraidEnv}
     */
    public static final String SDK_NAME = "openx-apollo-sdk";
    /**
     * Currently implemented MRAID version.
     */
    public static final String MRAID_VERSION = "3.0";

    /**
     * Currently implemented Native Ads version.
     */
    public static final String NATIVE_VERSION = "1.2";

    /**
     * Maximum refresh interval allowed. 120 seconds
     */
    public static final int AUTO_REFRESH_DELAY_MAX = 120000;

    /**
     * Default refresh interval. 60 seconds
     * Used when the refresh interval is not in the AUTO_REFRESH_DELAY_MIN & AUTO_REFRESH_DELAY_MAX range.
     */
    public static final int AUTO_REFRESH_DELAY_DEFAULT = 60000;

    /**
     * Minimum refresh interval allowed. 15 seconds
     */
    public static final int AUTO_REFRESH_DELAY_MIN = 15000;

    // Avoid compiler inlining by making the value a result of a method call
    private static final String BID_SERVER_HOST = String.valueOf("https://prebid.openx.net/openrtb2/auction");

    private static final AtomicInteger INIT_SDK_TASK_COUNT = new AtomicInteger();
    private static final int MANDATORY_TASK_COUNT = 3;

    /**
     * Loglevels for easy development
     * Default - No sdks logs
     * Refer - LogLevel
     */
    public static LogLevel logLevel = LogLevel.NONE;

    //If true, the SDK sends "af=3,5", indicating support for MRAID
    public static boolean sendMraidSupportParams = true;
    public static boolean isCoppaEnabled = false;
    public static boolean useExternalBrowser = false;

    private static SdkInitListener sInitSdkListener;

    private static String sAccountId;

    private static int sConnectionTimeout = BaseNetworkTask.TIMEOUT_DEFAULT;

    private static boolean sIsSdkInitialized = false;
    private static boolean sUseHttps = false;

    /**
     * First call, before using any adview APIs
     * To initialize the SDK, before making an adRequest
     *
     * @param context         application OR activity context
     * @param sdkInitListener will be notified as soon as SDK finishes initializing
     */

    public static void initializeSDK(Context context, final SdkInitListener sdkInitListener)
    throws AdException {
        Log.d(TAG, "Initializing OpenX SDK");
        if (context == null) {
            throw new AdException(AdException.INIT_ERROR, "OpenXSDK initialization failed. Context is null");
        }

        if (sIsSdkInitialized) {
            return;
        }

        sInitSdkListener = sdkInitListener;
        INIT_SDK_TASK_COUNT.set(0);

        if (logLevel != null) {
            //1
            initializeLogging();
        }
        AppInfoManager.init(context);
        //2
        initOpenMeasurementSDK(context);

        //3
        ManagersResolver.getInstance().prepare(context);
    }

    /**
     * Return 'true' if OpenX SDK is initialized completely
     */
    public static boolean isSdkInitialized() {
        return sIsSdkInitialized;
    }

    /**
     * Helper to know the last commit hash of OpenX SDK
     *
     * @return
     */
    public static String getSDKCommitHash() {
        return BuildConfig.GitHash;
    }

    public static int getTimeoutMillis() {
        return sConnectionTimeout;
    }

    public static void setTimeoutMillis(int millis) {
        sConnectionTimeout = millis;
    }

    public static String getBidServerHost() {
        return BID_SERVER_HOST;
    }

    public static String getAccountId() {
        return sAccountId;
    }

    public static void setAccountId(String accountId) {
        sAccountId = accountId;
    }

    /**
     * Set whether to use SCHEME_HTTP or SCHEME_HTTPS for WebView base urls.
     */
    public static void useHttpsWebViewBaseUrl(boolean useHttps) {
        sUseHttps = useHttps;
    }

    /**
     * Allow the publisher to use either http or https. This only affects WebView base urls.
     *
     * @return "https://" if sUseHttps is true; "http://" otherwise.
     */
    public static String getWebViewBaseUrlScheme() {
        return sUseHttps ? SCHEME_HTTPS : SCHEME_HTTP;
    }

    private static void initOpenMeasurementSDK(Context context) {
        OmAdSessionManager.activateOmSdk(context.getApplicationContext());
        increaseTaskCount();
    }

    private static void initializeLogging() {
        OXLog.setLogLevel(logLevel.getValue());//set to the publisher set value
        increaseTaskCount();
    }

    static void increaseTaskCount() {
        if (INIT_SDK_TASK_COUNT.incrementAndGet() >= MANDATORY_TASK_COUNT) {
            sIsSdkInitialized = true;
            OXLog.debug(TAG, "OpenX SDK " + SDK_VERSION + " Initialized");

            if (sInitSdkListener != null) {
                sInitSdkListener.onSDKInit();
            }
        }
    }

    /*
     * Loglevels for easy development
     * NONE - no sdk logs
     * DEBUG - sdk logs with debug level only. Noisy level
     * WARN - sdk logs with warn level only
     * ERROR - sdk logs with error level only
     */
    public enum LogLevel {
        NONE(-1), DEBUG(3), WARN(5), ERROR(6);

        private final int value;

        LogLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
