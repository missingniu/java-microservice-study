package org.ljz.gift.client;

import org.ljz.gift.domain.LoginUser;
import org.ljz.gift.util.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@FeignClient(value="gift-auth",fallbackFactory =AuthClientFactory.class )
public interface AuthClient {
    @PutMapping("/loginUser")
     AjaxResult addOrUpdate(@RequestBody LoginUser loginUser);

}
