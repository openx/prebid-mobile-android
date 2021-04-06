package com.openx.apollo.networking.parameters;

import com.apollo.test.utils.WhiteBox;
import com.openx.apollo.bidding.data.bid.Prebid;
import com.openx.apollo.models.openrtb.BidRequest;
import com.openx.apollo.models.openrtb.bidRequests.App;
import com.openx.apollo.models.openrtb.bidRequests.Ext;
import com.openx.apollo.networking.targeting.Targeting;
import com.openx.apollo.sdk.ApolloSettings;
import com.openx.apollo.utils.helpers.AdIdManager;
import com.openx.apollo.utils.helpers.AppInfoManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AppInfoParameterBuilderTest {

    private String APP_NAME = "app";
    private String APP_BUNDLE = "bundle";
    private String ADVERTISING_ID = "123";
    private boolean ADVERTISING_ID_ENABLED = true;

    @Before
    public void setUp() throws Exception {
        AppInfoManager.setAppName(APP_NAME);
        AppInfoManager.setPackageName(APP_BUNDLE);
        AdIdManager.setAdId(ADVERTISING_ID);
        AdIdManager.setLimitAdTrackingEnabled(!ADVERTISING_ID_ENABLED);
    }

    @After
    public void cleanup() throws Exception {
        WhiteBox.method(Targeting.class, "clear").invoke(null);
    }

    @Test
    public void testAppendBuilderParameters() throws Exception {
        AppInfoParameterBuilder builder = new AppInfoParameterBuilder();
        AdRequestInput adRequestInput = new AdRequestInput();

        final String expectedStoreurl = "https://google.play.com";
        final String expectedPublisherName = "openx";

        Targeting.setPublisherName(expectedPublisherName);
        Targeting.setAppStoreMarketUrl(expectedStoreurl);

        builder.appendBuilderParameters(adRequestInput);

        BidRequest expectedBidRequest = new BidRequest();
        final App expectedApp = expectedBidRequest.getApp();
        expectedApp.name = APP_NAME;
        expectedApp.bundle = APP_BUNDLE;
        expectedApp.storeurl = expectedStoreurl;
        expectedApp.getPublisher().name = expectedPublisherName;
        expectedApp.getExt().put("prebid", Prebid.getJsonObjectForApp(BasicParameterBuilder.DISPLAY_MANAGER_VALUE, ApolloSettings.SDK_VERSION));

        assertEquals(expectedBidRequest.getJsonObject().toString(),
                     adRequestInput.getBidRequest().getJsonObject().toString());
    }

    @Test
    public void whenAppendParametersAndTargetingContextDataNotEmpty_ContextDataAddedToAppExt()
    throws JSONException {
        Targeting.addContextData("context", "contextData");

        AppInfoParameterBuilder builder = new AppInfoParameterBuilder();
        AdRequestInput adRequestInput = new AdRequestInput();
        builder.appendBuilderParameters(adRequestInput);

        Ext appExt = adRequestInput.getBidRequest().getApp().getExt();
        assertTrue(appExt.getMap().containsKey("data"));
        JSONObject appDataJson = (JSONObject) appExt.getMap().get("data");
        assertTrue(appDataJson.has("context"));
        assertEquals("contextData", appDataJson.getJSONArray("context").get(0));
    }
}
