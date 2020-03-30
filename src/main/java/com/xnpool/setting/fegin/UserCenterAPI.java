package com.xnpool.setting.fegin;

import net.sf.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "user-center")
public interface UserCenterAPI {
    @RequestMapping(value = "/mine/oauth/userId/token", method = RequestMethod.POST)
    JSONObject authorizeToken(@RequestParam("userID") Long userID);

}
