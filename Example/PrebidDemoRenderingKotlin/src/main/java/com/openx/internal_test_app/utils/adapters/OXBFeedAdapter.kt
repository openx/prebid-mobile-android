package com.openx.internal_test_app.utils.adapters

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.openx.apollo.bidding.data.AdSize
import com.openx.apollo.bidding.enums.VideoPlacementType
import com.openx.apollo.bidding.parallel.BannerView

private const val TAG = "FeedAdapter"

open class OXBFeedAdapter(context: Context,
                          val width: Int,
                          val height: Int,
                          val configId: String) : BaseFeedAdapter(context) {

    protected var videoView: BannerView? = null

    override fun destroy() {
        Log.d(TAG, "Destroying adapter")
        videoView?.destroy()
    }

    override fun initAndLoadAdView(parent: ViewGroup?, container: FrameLayout): View? {
        if (videoView == null) {
            videoView = BannerView(container.context, configId, AdSize(width, height))
            videoView?.videoPlacementType = VideoPlacementType.IN_FEED
            val layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            layoutParams.gravity = Gravity.CENTER
            videoView?.layoutParams = layoutParams
        }
        videoView?.loadAd()
        return videoView
    }
}