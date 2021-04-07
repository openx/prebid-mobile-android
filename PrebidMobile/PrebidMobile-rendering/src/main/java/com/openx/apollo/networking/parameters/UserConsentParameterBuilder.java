package com.openx.apollo.networking.parameters;

import com.openx.apollo.models.openrtb.BidRequest;
import com.openx.apollo.sdk.ManagersResolver;
import com.openx.apollo.sdk.deviceData.managers.UserConsentManager;
import com.openx.apollo.utils.helpers.Utils;

public class UserConsentParameterBuilder extends ParameterBuilder {

    private static final String GDPR = "gdpr";
    private static final String US_PRIVACY = "us_privacy";
    private static final String CONSENT = "consent";

    private UserConsentManager mUserConsentManager;

    public UserConsentParameterBuilder() {
        mUserConsentManager = ManagersResolver.getInstance().getUserConsentManager();
    }

    @Override
    public void appendBuilderParameters(AdRequestInput adRequestInput) {
        BidRequest bidRequest = adRequestInput.getBidRequest();

        appendGdprParameter(bidRequest);
        appendCcpaParameter(bidRequest);
    }

    private void appendGdprParameter(BidRequest bidRequest) {
        String isSubjectToGdpr = mUserConsentManager.getSubjectToGdpr();

        if (!Utils.isBlank(isSubjectToGdpr)) {
            Integer gdprValue = "1".equals(isSubjectToGdpr) ? 1 : 0;
            bidRequest.getRegs().getExt().put(GDPR, gdprValue);

            String userConsentString = mUserConsentManager.getUserConsentString();
            if (!Utils.isBlank(userConsentString)) {
                bidRequest.getUser().getExt().put(CONSENT, userConsentString);
            }
        }
    }

    private void appendCcpaParameter(BidRequest bidRequest) {
        String usPrivacyString = mUserConsentManager.getUsPrivacyString();

        if (!Utils.isBlank(usPrivacyString)) {
            bidRequest.getRegs().getExt().put(US_PRIVACY, usPrivacyString);
        }
    }
}
