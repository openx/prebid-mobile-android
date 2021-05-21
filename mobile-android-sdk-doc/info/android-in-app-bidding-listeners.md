# In-App Bidding Listeners


The tables below list the listener methods that In-App Bidding SDK supports for banner, interstitial and rewarded ads in **Google Ad Manager** and **Pure In-App Bidding** facades.

## BannerViewListener interface

| Listener method        | Invoked when                                             |
| ---------------------- | ------------------------------------------------------------ |
| `onAdLoaded`                   | An ad is loaded though not necessarily shown. |
| `onAdDisplayed`           | Executed when the ad is displayed on screen. |
| `onAdFailed`  | The load process fails to produce a viable ad. |
| `onAdClicked`               | Notifies listener that the banner view was clicked, as a result of user interaction. |
| `onAdClosed`                | Notifies listener that the banner view has dismissed the modal (e.g. browser) on top of the current ad view.|


## InterstitialAdUnitListener interface


| Listener method        |Invoked when                                             |
| ---------------------- |------------------------------------------------------------ |
| `onAdLoaded`                     | An ad is loaded though not necessarily shown. |
| `onAdFailed`    | The load process fails to produce a viable ad.|
| `onAdDisplayed`                    | Called when the interstitial view is displayed.|
| `onAdClosed`                     | Called when the interstitial is closed by the user.|
| `onAdClicked`                       | A user clicks an ad, an in-app browser is opened. |

## RewardedAdUnitListener interface


| Listener method        |Invoked when                                             |
| ---------------------- |------------------------------------------------------------ |
| `onAdLoaded`                     | An ad is loaded.|
| `onUserEarnedReward`             |Called when user is able to receive a reward from the app. |
| `onAdFailed`    | The load process fails to produce a viable ad.|
| `onAdDisplayed`                    | Called when the interstitial view is displayed.|
| `onAdClosed`                     | Called when the interstitial is closed. |
| `onAdClicked`                       | A user clicks an ad, an in-app browser is opened. |

