package com.openx.apollo.views.browser;

import android.content.Intent;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.VideoView;

import com.apollo.test.utils.WhiteBox;
import com.openx.apollo.views.interstitial.InterstitialManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.openx.apollo.views.browser.AdBrowserActivity.EXTRA_URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class AdBrowserActivityTest {
    private AdBrowserActivity mAdBrowserActivity;
    private Intent mIntent;

    @Before
    public void setUp() throws Exception {
        mIntent = new Intent();
        mIntent.putExtra(AdBrowserActivity.EXTRA_IS_VIDEO, false);
        mIntent.putExtra(EXTRA_URL, "tel:123123");
        mAdBrowserActivity = Robolectric.buildActivity(AdBrowserActivity.class, mIntent).create().get();
    }

    @Test
    public void initTest() {
        //verify that activity creates without an Exception
        mAdBrowserActivity = Robolectric.buildActivity(AdBrowserActivity.class, mIntent).create().get();

        mIntent.putExtra(AdBrowserActivity.EXTRA_IS_VIDEO, true);
        mAdBrowserActivity = Robolectric.buildActivity(AdBrowserActivity.class, mIntent).create().get();
    }

    @Test
    public void onKeyDownTest() throws IllegalAccessException {
        WebView mockWebView = mock(WebView.class);
        WhiteBox.field(AdBrowserActivity.class, "mWebView").set(mAdBrowserActivity, mockWebView);

        mAdBrowserActivity.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(0, 0));
        verify(mockWebView).goBack();
    }

    @Test
    public void onPauseTest() throws IllegalAccessException {
        VideoView mockView = mock(VideoView.class);
        WhiteBox.field(AdBrowserActivity.class, "mVideoView").set(mAdBrowserActivity, mockView);

        mAdBrowserActivity.onPause();
        verify(mockView).suspend();
    }

    @Test
    public void onResumeTest() throws IllegalAccessException {
        VideoView mockView = mock(VideoView.class);
        WhiteBox.field(AdBrowserActivity.class, "mVideoView").set(mAdBrowserActivity, mockView);

        mAdBrowserActivity.onResume();
        verify(mockView).resume();
    }

    @Test
    public void onDestroyTest() throws IllegalAccessException {
        mIntent.putExtra(AdBrowserActivity.EXTRA_IS_VIDEO, true);
        mAdBrowserActivity = Robolectric.buildActivity(AdBrowserActivity.class, mIntent).create().get();

        WebView mockWebView = mock(WebView.class);
        VideoView mockVideoView = mock(VideoView.class);
        WhiteBox.field(AdBrowserActivity.class, "mWebView").set(mAdBrowserActivity, mockWebView);
        WhiteBox.field(AdBrowserActivity.class, "mVideoView").set(mAdBrowserActivity, mockVideoView);

        mAdBrowserActivity.onDestroy();
        verify(mockWebView).destroy();
        verify(mockVideoView).suspend();
    }

    @Test
    public void browserControlsTest() throws IllegalAccessException {
        WebView mockWebView = mock(WebView.class);
        when(mockWebView.getUrl()).thenReturn("url");
        when(mockWebView.canGoForward()).thenReturn(true);
        when(mockWebView.canGoBack()).thenReturn(true);

        InterstitialManager mockManager = mock(InterstitialManager.class);
        when(mockManager.getInterstitialDisplayDelegate()).thenReturn(null);

        WhiteBox.field(AdBrowserActivity.class, "mShouldFireEvents").set(mAdBrowserActivity, true);
        WhiteBox.field(AdBrowserActivity.class, "mWebView").set(mAdBrowserActivity, mockWebView);
        BrowserControlsEventsListener eventsListener = ((BrowserControls) WhiteBox.field(AdBrowserActivity.class, "mBrowserControls").get(mAdBrowserActivity)).getBrowserControlsEventsListener();

        eventsListener.onRelaod();
        verify(mockWebView).reload();

        eventsListener.onGoForward();
        verify(mockWebView).goForward();

        eventsListener.onGoBack();
        verify(mockWebView).goBack();

        assertEquals("url", eventsListener.getCurrentURL());

        assertTrue(eventsListener.canGoBack());

        assertTrue(eventsListener.canGoForward());

        // TODO: 2/10/21 verify broadcast
        eventsListener.closeBrowser();
    }
}