package org.ljz.gift.client;

import feign.hystrix.FallbackFactory;
import org.ljz.gift.domain.LoginUser;
import org.ljz.gift.util.AjaxResult;
import org.springframework.stereotype.Component;

@Component
public class AuthClientFactory implements FallbackFactory<AuthClient> {
    @Override
    public AuthClient create(Throwable throwable) {
        return loginUser -> {
            throwable.printStackTrace();
            return AjaxResult.me().setSuccess(false).setCode(500)
                    .setMessage(throwable.getMessage());
        };
    }
}
