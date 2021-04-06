package com.openx.apollo.utils.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.openx.apollo.listeners.AdIdFetchListener;
import com.openx.apollo.utils.logger.OXLog;

import java.lang.ref.WeakReference;

public class AdIdManager {
    private static final String TAG = AdIdManager.class.getSimpleName();

    /**
     * Timeout for getting adId & lmt values from google play services in an asynctask
     * Default - 3000
     */
    private static final long AD_ID_TIMEOUT_MS = 3000;

    private static volatile String sAdId = null;
    private static boolean sLimitAdTrackingEnabled;

    private AdIdManager() {

    }

    // Wrap method execution in try / catch to avoid crashes in runtime if publisher didn't include identifier dependencies
    public static void initAdId(final Context context, final AdIdFetchListener listener) {
        try {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
            if (resultCode == ConnectionResult.SUCCESS) {
                final FetchAdIdInfoTask getAdIdInfoTask = new FetchAdIdInfoTask(context, listener);
                getAdIdInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //wait for a max of 3 secs and cancel the task if it's still running.
                //continue with adIdFetchFailure() where we will just log this as warning
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (getAdIdInfoTask.getStatus() == AsyncTask.Status.RUNNING) {
                        OXLog.debug(TAG, "Cancelling FetchAdIdInfoTask");
                        getAdIdInfoTask.cancel(true);
                        listener.adIdFetchFailure();
                    }
                }, AD_ID_TIMEOUT_MS);
            }
            else {
                listener.adIdFetchCompletion();
            }
        }
        catch (Throwable throwable) {
            OXLog.error(TAG, "Failed to initAdId: " + Log.getStackTraceString(throwable) + "\nDid you add necessary dependencies?");
        }
    }

    /**
     * @return Advertiser id, from gms-getAdvertisingIdInfo
     */
    public static String getAdId() {
        return sAdId;
    }

    public static boolean isLimitAdTrackingEnabled() {
        return sLimitAdTrackingEnabled;
    }

    @VisibleForTesting
    public static void setLimitAdTrackingEnabled(boolean limitAdTrackingEnabled) {
        sLimitAdTrackingEnabled = limitAdTrackingEnabled;
    }

    @VisibleForTesting
    public static void setAdId(String adId) {
        sAdId = adId;
    }

    private static class FetchAdIdInfoTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContextWeakReference;
        private final AdIdFetchListener mAdIdFetchListener;

        public FetchAdIdInfoTask(Context context, AdIdFetchListener listener) {
            mContextWeakReference = new WeakReference<>(context);

            // All listeners provided are created as local method variables; If these listeners
            // are ever moved to a class member variable, this needs to be changed to a WeakReference
            mAdIdFetchListener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Context context = mContextWeakReference.get();

            if (isCancelled()) {
                return null;
            }

            if (context == null) {
                return null;
            }

            try {
                AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                sAdId = adInfo.getId();
                sLimitAdTrackingEnabled = adInfo.isLimitAdTrackingEnabled();
            }
            catch (Throwable e) {
                OXLog.error(TAG, "Failed to get advertising id and LMT: " + Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mAdIdFetchListener != null) {
                mAdIdFetchListener.adIdFetchCompletion();
            }
        }
    }
}
