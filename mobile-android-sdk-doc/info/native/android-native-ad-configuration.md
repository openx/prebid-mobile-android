NativeAdConfiguration class
=============================

`NativeAdConfiguration` class provides an ability to set assets, event trackers and other OpenRTB parameters required for Native Ads.

Parameters
----------

| Parameter      | Method                                                                     | Default | Required    | Description                                                                                                                                                                                                                                                                                                                |
|:---------------|:---------------------------------------------------------------------------|:--------|:------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| context        | setContextType(type: [ContextType](#contexttype))                          | -       | recommended | The context in  which the ad appears                                                                                                                                                                                                                                                                                       |
| contextsubtype | setContextSubType(subType: [ContextSubType](#contextsubtype))              | -       | optional    | A more detailed context in which the ad appears                                                                                                                                                                                                                                                                            |
| plcmttype      | setPlacementType(type: [PlacementType](#placementtype))                    | -       | recommended | The design/format/layout of the ad unit being offered                                                                                                                                                                                                                                                                      |
| seq            | setSeq(seq: Integer)                                                       | -       | optional    | 0 for the first ad, 1 for the second ad, and so on. Note this would generally NOT be used in combination with plcmtcnt - either you are auctioning multiple identical placements (in which case plcmtcnt>1, seq=0) or you are holding separate auctions for distinct items in the feed (in which case plcmtcnt=1, seq=>=1) |
| assets         | addAsset(asset: NativeAsset)                                               | -       | required    | An array of Asset Objects.                                                                                                                                                                                                                                                                                                 |
| eventtrackers  | addTracker(tracker: [NativeEventTracker](android-native-event-tracker.md)) | -       | optional    | Specifies what type of event tracking is supported                                                                                                                                                                                                                                                                         |
| privacy        | setPrivacy(privacy: Boolean)                                               | false   | recommended | Set to 1 when the native ad supports buyer-specific privacy notice. Set to 0 when the native ad doesn’t support custom  privacy links or if support is unknown                                                                                                                                                             |
| ext            | setExt(ext: Ext)                                                           | -       | optional    | This object is a placeholder that may contain custom  JSON                                                                                                                                                                                                                                                                 |

> **Note:** `plcmtcnt`, `aurlsupport` and `durlsupport` OpenRTB fields are not supported

Enums
-----
### ContextType
| Name            | ID   | Description                                                                                  |
|:----------------|:-----|:---------------------------------------------------------------------------------------------|
| CONTENT_CENTRIC | 1    | Content-centric context such as news feed, article, image gallery, video gallery, or similar |
| SOCIAL_CENTRIC  | 2    | Social-centric context such as social network feed, email, chat, or similar                  |
| PRODUCT         | 3    | Product context such as product listings, details, recommendations, reviews, or similar      |
| CUSTOM          | 500+ | To be defined by the exchange                                                                                             |

### ContextSubType
| Name                 | ID   | Description                                                                                  |
|:---------------------|:-----|:---------------------------------------------------------------------------------------------|
| GENERAL              | 10   | General or mixed content                                                                     |
| ARTICLE              | 11   | Primarily article content (which of course could include images, etc as part of the article) |
| VIDEO                | 12   | Primarily video content                                                                                             |
| AUDIO                | 13   | Primarily audio content                                                                                             |
| IMAGE                | 14   | Primarily image content                                                                                             |
| USER_GENERATED       | 15   | User-generated content - forums, comments, etc                                                                                             |
| GENERAL_SOCIAL       | 20   | General social content such as a general social network                                                                                             |
| EMAIL                | 21   | Primarily email content                                                                                             |
| CHAT_IM              | 22   | Primarily chat/IM content                                                                                             |
| SELLING              | 30   | Content focused on selling products, whether digital or physical                                                                                             |
| APPLICATION_STORE    | 31   | Application store/marketplace                                                                                             |
| PRODUCT_REVIEW_SITES | 32   | Product reviews site primarily (which may sell product secondarily)                                                                                             |
| CUSTOM               | 500+ | To be defined by the exchange                                                                                             |

### PlacementType
| Name                  | ID   | Description                                                                                                                    |
|:----------------------|:-----|:-------------------------------------------------------------------------------------------------------------------------------|
| CONTENT_FEED          | 1    | In the feed of content - for example as an item inside the organic feed/grid/listing/carousel                                  |
| CONTENT_ATOMIC_UNIT   | 2    | In the atomic unit of the content - IE in the article page or single image page                                                |
| OUTSIDE_CORE_CONTENT  | 3    | Outside the core content - for example in the ads section on the right rail, as a banner-style placement near the content, etc |
| RECOMMENDATION_WIDGET | 4    | Recommendation widget, most commonly presented below the article content                                                       |
| CUSTOM                | 500+ | To be defined by the exchange                                                                                                                               |
