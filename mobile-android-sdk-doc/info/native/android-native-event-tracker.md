NativeEventTracker class
========================

`NativeEventTracker` class is used to setup 'eventtrackers' field for Native OpenRTB request. Event trackers object specifies the types of events the bidder can request to be tracked in  
the	bid	response, and which	types of tracking are available	for	each event type, and is	included as	an array in	the	request.

Constructor parameters
----------------------
| Name                 | Type                                                   | Description                                                  |
|:---------------------|:-------------------------------------------------------|:-------------------------------------------------------------|
| eventType            | [EventType](#eventtype)                                | Type of event available for tracking                         |
| eventTrackingMethods | ArrayList<[EventTrackingMethod](#eventtrackingmethod)> | Array of the types of tracking available for the given event |

Methods
-------

| Method                                                                            | Description                                                                                  |
|:----------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------|
| getEventType(): [EventType](#eventtype)                                           | Returns the [EventType](#eventtype) passed in constructor                                    |
| getEventTrackingMethods(): ArrayList<[EventTrackingMethod](#eventtrackingmethod)> | Returns the ArrayList of [EventTrackingMethods](#eventtrackingmethod)> passed in constructor |
| setExt(ext: Ext)                                                                  | Set the event tracker's extension object                                                     |
| getExt(): Ext                                                                     | Returns the event tracker's extension object                                                 |

Enums
-----
### EventType
| Name             | ID   | Description                                                                    |
|:-----------------|:-----|:-------------------------------------------------------------------------------|
| IMPRESSION       | 1    | Impression                                                                     |
| VIEWABLE_MRC50   | 2    | Visible impression using MRC definition at 50% in view for 1second             |
| VIEWABLE_MRC100  | 3    | 100% in view for 1 second (ie GroupM  standard)                                |
| VIEWABLE_VIDEO50 | 4    | Visible impression for video using MRC definition at 50% in view for 2 seconds |
| CUSTOM           | 500+ | Exchange specific                                                              |

### EventTrackingMethod
| Name   | ID   | Description                                                                                                                               |
|:-------|:-----|:------------------------------------------------------------------------------------------------------------------------------------------|
| IMAGE  | 1    | Image-pixel tracking - URL provided will be inserted as a 1x1 pixel at the time of the event                                              |
| JS     | 2    | Javascript-based tracking - URL provided will be inserted as a js tag at the time of the event                                            |
| CUSTOM | 500+ | Could include custom  measurement companies such as moat, double verify, IAS, etc - in this case additional elements will often be passed |
