package com.openx.apollo.utils.helpers;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.VisibleForTesting;

import com.openx.apollo.utils.logger.OXLog;

/*
 *  Class that deals with all the refresh for mopub through OpenX
 */
public class RefreshTimerTask {
    private static final String TAG = RefreshTimerTask.class.getSimpleName();
    private Handler mRefreshHandler;

    //for unit testing only
    private boolean mRefreshExecuted;

    private RefreshTriggered mRefreshTriggerListener;

    private final Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRefreshTriggerListener == null) {
                OXLog.error(TAG, "Failed to notify mRefreshTriggerListener. mRefreshTriggerListener instance is null");
                return;
            }

            mRefreshTriggerListener.handleRefresh();
            mRefreshExecuted = true;
        }
    };

    public RefreshTimerTask(RefreshTriggered refreshTriggered) {
        mRefreshHandler = new Handler(Looper.getMainLooper());
        mRefreshTriggerListener = refreshTriggered;
    }

    /**
     * Cancels previous timer (if any) and creates a new one with the given interval in ms
     *
     * @param interval value in milliseconds
     */
    public void scheduleRefreshTask(int interval) {
        cancelRefreshTimer();

        if (interval > 0) {
            queueUIThreadTask(interval);
        }
    }

    public void cancelRefreshTimer() {
        if (mRefreshHandler != null) {
            mRefreshHandler.removeCallbacksAndMessages(null);
        }
    }

    public void destroy() {
        cancelRefreshTimer();
        mRefreshHandler = null;
        mRefreshExecuted = false;
    }

    /**
     * Queue new task that should be performed in UI thread.
     */
    private void queueUIThreadTask(long interval) {
        if (mRefreshHandler != null) {
            mRefreshHandler.postDelayed(mRefreshRunnable, interval);
        }
    }

    @VisibleForTesting
    boolean isRefreshExecuted() {
        return mRefreshExecuted;
    }
}