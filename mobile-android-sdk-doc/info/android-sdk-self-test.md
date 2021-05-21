Testing and troubleshooting
===========================

After you have integrated the Prebid Rendering Mobile Android SDK, you should test
your implementation.

> **Important:** For troubleshooting and diagnostic purposes, please [install an HTTP
debugging proxy](#using-charles-web-proxy) such as Charles Web Proxy. You may need to
provide logs to Prebid.

General testing requirements
----------------------------------------

Please make sure that you:

-   Have a good communications connection on the device, such as
    reliable wireless network signal. If you can\'t reproduce the
    problem, it could be a communications issue.
-   Have a valid user agent if you are expecting live ads. Testing with a real device that does not have ad
    tracking limited will help buyers bid on the inventory.
-   Have embedded the SDK in your app as instructed in [Importing the
    Android SDK](../info/android-sdk-integration.md).
-   Have updated the manifest as instructed in [Integrating the SDK with
    your project](../info/android-sdk-integration.md).
-   Have set the appropriate [listener
    methods](../info/android-in-app-bidding-listeners.md):
    -   `onAdLoaded` (highly recommended)
    -   `onAdFailed` (highly recommended)
    -   Other listener methods as needed
-   Have turned on the SDK logging by setting the
    [PrebidRenderingSettings](../info/android-sdk-parameters.md#prebidrenderingsettings) `logLevel`
    parameter to `DEBUG` on app `init`.
    > **Tip:** You may want to copy and save your on-screen log messages in case you need to include them in your resolution assistance request
-   Are using the latest version of the SDK (to check for the latest
    version, see [Release notes](../info/android-in-app-bidding-release-notes.md)).
-   Are [using Charles](../info/android-sdk-self-test.md#using-charles-web-proxy) to verify network requests are
    passing correctly.

    >**Important:** If you encounter issues, see a list of [possible problems and
solutions](../info/android-sdk-self-test.md#troubleshooting) below. If you still can\'t resolve your issues,
please request resolution assistance through the [Prebid Support
Community](https://docs.prebid.org/support/index.html) and make sure to include your
Charles logs and any SDK error message.

Using Charles Web Proxy
------------------------------------

An HTTP debugging proxy, such as Charles Web Proxy, allows you to
observe network traffic coming from Android simulators and devices and
diagnose issues with your Prebid Rendering Mobile Android SDK implementation. You
can download and install Charles from <https://www.charlesproxy.com/>.

Use Charles to verify network requests are passing correctly. For
example, to verify that you are sending a valid ad request, copy the ad
request URL from Charles and paste it in your browser and see if you are
getting a valid response.

> **Tip:** Consider using a filter such as \"prebid\" to avoid sifting through a lot
of unrelated traffic.

Troubleshooting
----------------------------

See also examples of common [ad exceptions and error log messages](../info/android-sdk-self-test.md#ad-exception-types-and-examples)
below.

  | Problem                                                      | Possible reasons                                             | What to do                                                   |
  | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
  | Ad not displayed.                                            | No fill (most common).                                       | - Make sure the location permissions are added to your Prebid Mobile Android SDK's manifest as described in [Integrating the SDK with your project](../info/android-sdk-integration.md).<br />- Make sure your app checks and requests runtime permissions appropriately.<br />- Enrich with more user data. See [Parameters](../info/android-sdk-parameters.md). |
  |                                                              | Incorrect delivery domain.                                   | Make sure you are using the Prebid delivery domain provided by Prebid. |
  |                                                              | Incorrect ad unit ID.                                        | Make sure you are using the ad unit ID provided by Prebid.    |
  |                                                              | Issues with the creative.                                    | Check where creative is coming from. You may need to block certain advertisers, for example. |
  | No navigation occurs when user taps on ad, or user sees blank white screen after clicking. | - Creative does not have a click-through link.<br />- Another UI element is laying over the ad.<br />- Creative redirecting to a server that is not set up properly. | Check your creative and your UI view hierarchy to make sure they are set up as expected. |
  | Ad not displaying correctly, such as a landscape ad appearing for a portrait ad unit. | Line item configuration issue.                               | On your ad server, make sure the line item is trafficking the same orientation as the ad unit. |
  | No geo data or other expected auto-detected data.            | - The location permissions may not be added to the Prebid Mobile Android SDK's manifest.<br />- User may not be allowing data to be shared within the app settings. | - Make sure the location permissions are added to your Prebid Mobile Android SDK's manifest as described in [Integrating the SDK with your project](../info/android-sdk-integration.md).<br />- Make sure your app checks and requests runtime permissions appropriately.<br />- Make sure the user is allowing data to be shared within the app settings. |
  | No user enrichment data.                                     | - You may not have set all of the user enrichment data that you expected<br />- User may not be allowing data to be shared within the app settings. | - Make sure you have set all desired user data to be included in ad requests as described in [Targeting](../info/android-sdk-parameters.md#targeting).<br />- Make sure the user is allowing data to be shared within the app settings. |
  | No impression or no click tracking recorded.                 |                                                              | Request resolution assistance through the [Prebid Support Community](https://docs.prebid.org/support/index.html). |


Ad exception types and examples
--------------------------------------

Exceptions can inform you of configuration issues or possible issues
that need to be resolved by Prebid. The following table summarizes the
types of exceptions that may occur and provides examples of common log
messages.

| Error type      | Message example                                              | Reason                                                       | What to do                                                   |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| INVALID_REQUEST | "Invalid request: No domain was specified"<br />"Invalid request: No ad unit ID was specified" | There could be a problem with your configuration.            | Check your configuration. Invalid request errors usually inform you of a missing required setting, such as no ad unit ID specified, no creative specified, or no domain specified. If you need help with troubleshooting configuration errors, please visit the [Prebid Support Community](https://docs.prebid.org/support/index.html). |
| INIT_ERROR      | "Internet permission not granted"<br />"Failed to fetch advertisement ID" | There is a problem with permissions or initialization of system services in the SDK. | Check your AndroidManifest.xml and make sure you have followed the instructions in [Integrating the SDK with your project](../info/android-sdk-integration.md). |
| INTERNAL_ERROR  | "SDK internal error: Invalid ad response: Invalid VAST Response: less than 100 characters." | There could be a problem with your SDK implementation or with Prebid. For example, the video ad unit may not be returning the right VAST response. | Please request resolution assistance through the [Prebid Support Community](https://docs.prebid.org/support/index.html) and make sure to include the error message. |
| SERVER_ERROR    | "Bad server response - **"HTTP Response code of %s"**           | Indicates a problem that needs to be resolved by Prebid.      | Please complete follow the [testing guidelines](../info/android-sdk-self-test.md#general-testing-requirements) above. If you cannot resolve the issue, please request resolution assistance through the [Prebid Support Community](https://docs.prebid.org/support/index.html) and make sure to include the error message. |

Below are examples of an `onAdFailed` and error messages that
appear when you use incorrect delivery domain or ad unit ID.

-   If the *delivery domain is incorrect*:

    ```
    SDK internal error: Invalid ad response: (server status code)
    ```

-   If the *delivery domain is correct*, but the *ad unit is incorrect*:

    ```
    SDK internal error: Bad server response - [Adunits field empty in JSON response]
    ```
