package com.openx.apollo.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.openx.apollo.errors.AdException;
import com.openx.apollo.listeners.VideoCreativeViewListener;
import com.openx.apollo.video.ExoPlayerView;
import com.openx.apollo.views.interstitial.InterstitialManager;
import com.openx.apollo.views.webview.OpenXWebViewBanner;
import com.openx.apollo.views.webview.OpenXWebViewInterstitial;
import com.openx.apollo.views.webview.mraid.Views;

import java.util.ArrayList;

import static com.openx.apollo.models.AdConfiguration.AdUnitIdentifierType;

public class ViewPool {
    @SuppressLint("StaticFieldLeak")
    private static ViewPool sInstance = null;
    private ArrayList<View> mOccupiedViews = new ArrayList<>();
    private ArrayList<View> mUnoccupiedViews = new ArrayList<>();

    private ViewPool() {

    }

    public static ViewPool getInstance() {
        if (sInstance == null) {
            sInstance = new ViewPool();
        }
        return sInstance;
    }

    protected int sizeOfOccupied() {
        return mOccupiedViews.size();
    }

    protected int sizeOfUnoccupied() {
        return mUnoccupiedViews.size();
    }

    //This will add views into occupied bucket
    public void addToOccupied(View view) {
        if (!mOccupiedViews.contains(view) && !mUnoccupiedViews.contains(view)) {
            mOccupiedViews.add(view);
        }
    }

    public void addToUnoccupied(View view) {
        if (!mUnoccupiedViews.contains(view) && !mOccupiedViews.contains(view)) {
            mUnoccupiedViews.add(view);
        }
    }

    //This will swap from occupied to unoccupied(after windowclose) and removes it from occupied bucket
    public void swapToUnoccupied(View view) {
        if (!mUnoccupiedViews.contains(view)) {
            mUnoccupiedViews.add(view);

            Views.removeFromParent(view);
        }
        mOccupiedViews.remove(view);
    }

    //This will swap from unoccupied to occupied(after showing/displaying) and removes it from unoccupied bucket
    private void swapToOccupied(View view) {
        if (!mOccupiedViews.contains(view)) {
            mOccupiedViews.add(view);
        }

        mUnoccupiedViews.remove(view);
    }

    //This only clears the bucketlist. It does not actually remove the lists. Means (size becomes 0 but list still exists)
    public void clear() {
        mOccupiedViews.clear();
        mUnoccupiedViews.clear();
        plugPlayView = null;
    }

    private View plugPlayView;

    //Q: why are we keeping it in occupied? Should we not put/get from unoccupied directly?
    //A: Because, when a videoCreativeView is created, we will have to, anyways, add the view to the occupied bucket as it is going to be given to adView.
    //So, do that step here itself.(distribution of work!)
    public View getUnoccupiedView(Context context, VideoCreativeViewListener videoCreativeViewListener, AdUnitIdentifierType adType, InterstitialManager interstitialManager)
    throws AdException {
        if (context == null) {
            throw new AdException(AdException.INTERNAL_ERROR, "Context is null");
        }
        if (mUnoccupiedViews != null && mUnoccupiedViews.size() > 0) {

            View view = mUnoccupiedViews.get(0);

            Views.removeFromParent(view);

            //get item from unoccupied & add it to occupied
            swapToOccupied(view);
            return mOccupiedViews.get(mOccupiedViews.size() - 1);
        }
        //create a new one

        //add it to occupied

        switch (adType) {
            case BANNER:
                plugPlayView = new OpenXWebViewBanner(context, interstitialManager);
                break;
            case INTERSTITIAL:
                plugPlayView = new OpenXWebViewInterstitial(context, interstitialManager);
                //add it to occupied
                break;
            case VAST:
                plugPlayView = new ExoPlayerView(context, videoCreativeViewListener);
                break;
        }
        addToOccupied(plugPlayView);
        return plugPlayView;
    }
}
