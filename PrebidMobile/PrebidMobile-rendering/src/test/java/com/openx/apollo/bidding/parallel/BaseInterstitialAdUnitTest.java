package com.openx.apollo.bidding.parallel;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.Nullable;

import com.openx.apollo.bidding.data.bid.Bid;
import com.openx.apollo.errors.AdException;
import com.openx.apollo.models.AdConfiguration;
import com.openx.apollo.models.AdPosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class BaseInterstitialAdUnitTest {

    private BaseInterstitialAdUnit mBaseInterstitialAdUnit;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.buildActivity(Activity.class).create().get();
        mBaseInterstitialAdUnit = new BaseInterstitialAdUnit(context) {
            @Override
            void requestAdWithBid(
                @Nullable
                    Bid bid) {

            }

            @Override
            void showGamAd() {

            }

            @Override
            void notifyAdEventListener(AdListenerEvent adListenerEvent) {

            }

            @Override
            void notifyErrorListener(AdException exception) {

            }
        };
        final AdConfiguration adUnitConfiguration = new AdConfiguration();
        mBaseInterstitialAdUnit.init(adUnitConfiguration);
        assertEquals(AdPosition.FULLSCREEN.getValue(), adUnitConfiguration.getAdPositionValue());
    }

    @Test
    public void addUpdateRemoveClearContextData_EqualsGetContextDataDictionary() {
        Map<String, Set<String>> expectedMap = new HashMap<>();
        HashSet<String> value1 = new HashSet<>();
        value1.add("value1");
        HashSet<String> value2 = new HashSet<>();
        value2.add("value2");
        expectedMap.put("key1", value1);
        expectedMap.put("key2", value2);

        // add
        mBaseInterstitialAdUnit.addContextData("key1", "value1");
        mBaseInterstitialAdUnit.addContextData("key2", "value2");

        assertEquals(expectedMap, mBaseInterstitialAdUnit.getContextDataDictionary());

        // update
        HashSet<String> updateSet = new HashSet<>();
        updateSet.add("value3");
        mBaseInterstitialAdUnit.updateContextData("key1", updateSet);
        expectedMap.replace("key1", updateSet);

        assertEquals(expectedMap, mBaseInterstitialAdUnit.getContextDataDictionary());

        // remove
        mBaseInterstitialAdUnit.removeContextData("key1");
        expectedMap.remove("key1");
        assertEquals(expectedMap, mBaseInterstitialAdUnit.getContextDataDictionary());

        // clear
        mBaseInterstitialAdUnit.clearContextData();
        assertTrue(mBaseInterstitialAdUnit.getContextDataDictionary().isEmpty());
    }

    @Test
    public void addRemoveContextKeywords_EqualsGetContextKeyWordsSet() {
        HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("key1");
        expectedSet.add("key2");

        // add
        mBaseInterstitialAdUnit.addContextKeyword("key1");
        mBaseInterstitialAdUnit.addContextKeyword("key2");

        assertEquals(expectedSet, mBaseInterstitialAdUnit.getContextKeywordsSet());

        // remove
        mBaseInterstitialAdUnit.removeContextKeyword("key2");
        expectedSet.remove("key2");
        assertEquals(expectedSet, mBaseInterstitialAdUnit.getContextKeywordsSet());

        // clear
        mBaseInterstitialAdUnit.clearContextKeywords();
        assertTrue(mBaseInterstitialAdUnit.getContextKeywordsSet().isEmpty());

        // add all
        mBaseInterstitialAdUnit.addContextKeywords(expectedSet);
        assertEquals(expectedSet, mBaseInterstitialAdUnit.getContextKeywordsSet());
    }
}