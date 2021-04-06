package com.openx.apollo.models.openrtb;

import com.openx.apollo.models.openrtb.bidRequests.App;
import com.openx.apollo.models.openrtb.bidRequests.Device;
import com.openx.apollo.models.openrtb.bidRequests.Imp;
import com.openx.apollo.models.openrtb.bidRequests.Regs;
import com.openx.apollo.models.openrtb.bidRequests.User;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class BidRequestTest {

    @Test
    public void getJsonObject() throws Exception {
        BidRequest bidReq = new BidRequest();

        App app = new App();
        app.id = "auid";
        bidReq.setApp(app);
        Device device = new Device();
        device.h = 1111;
        bidReq.setDevice(device);

        Imp imp = new Imp();
        imp.instl = 0;
        ArrayList<Imp> imps = new ArrayList<>();
        imps.add(imp);
        bidReq.setImp(imps);
        Regs regs = new Regs();
        regs.coppa = 0;
        bidReq.setRegs(regs);

        User user = new User();
        user.keywords = "q, o";
        bidReq.setUser(user);

        JSONObject actualObj = bidReq.getJsonObject();
        String expectedString = "{\"app\":{\"id\":\"auid\"},\"regs\":{\"coppa\":0},\"imp\":[{\"instl\":0}],\"device\":{\"h\":1111},\"user\":{\"keywords\":\"q, o\"}}";
        assertEquals("got: " + actualObj.toString(), expectedString, actualObj.toString());
        bidReq.getJsonObject();
    }
}