# Prebid Rendering Android SDK

Prebid Rendering SDK is a part of Prebid platform which implements an In-App Bidding approach and gives opportunities to buyers to compete on your inventory at parallel bidding auctions.

The current SDK version is **x.x.x**.
Go to [release notes](info/android-in-app-bidding-release-notes.md) for all SDK versions.

## Quick Start


#### Gradle Integration

To add the Prebid Rendering SDK dependency, open your project and update the app module’s build.gradle to have the following repositories and dependencies:

```
allprojects {
    repositories {
      ...
      mavenCentral()
      ...
    }
}

// ...

dependencies {
    ...
    implementation('org.prebid:prebid-mobile-sdk-rendering:x.x.x')
    ...
}
```

## Prebid Rendering SDK Overview

For requirements and integration overview, see [Getting started with In-App Bidding](info/android-in-app-bidding-getting-started.md).

Here are key capabilities of the Android In-App Bidding SDK:

-   **Integration Scenarios**
    - [Google Ad Manager](info/integration-gam/android-in-app-bidding-gam-info.md)
    - [MoPub](info/integration-mopub/android-in-app-bidding-mopub-info.md)
    - [Pure In-App Bidding](info/integration-prebid/android-in-app-bidding-pb-info.md)

-   **Support of these premium ad formats:**
    -   Banner
    -   Interstitial
    -   [**NEW**] [Native](info/android-in-app-bidding-native-guidelines-info.md) 
    -   Rich media and MRAID 3.0 support
    -   Video Interstitial
    -   Rewarded Video
    -   Outstream Video
-  **Open Measurement Support.**
-   **Direct SDK integration**. Allows you to pass first-party app data,
    user data, device data, and location data.  
-   **Privacy Regulation Compliance**. The In-App Bidding SDK meets **GDPR**, **CCPA**, **COPPA** requirements according to the IAB specifications.
-   **App targeting campaigns**. With the [support of deeplink+](info/android-sdk-deeplinkplus.md) SDK is able to manage the ads with premium UX for retargeting campaigns.
-    **Targeting**. Use [custom ad parameters](info/android-sdk-parameters.md) to increase the chance to win an impression and to improve ad views' UX.
-   **Tracking of render impression**. Prebid In-App Bidding SDK tracks [render impressions](info/android-sdk-impression-tracking.md) according to the IAB Measurement Guidelines for all managed ads. Ads rendered by Primary Ad Server SDK track an impression beacon according to the internal algorithms.
-   **Fast and seamless integration.**


## Contact Us

If you have any questions or need help, go to [Prebid Support](https://docs.prebid.org/support/index.html).
