package org.ljz.gift.service;

import org.ljz.gift.dto.RegisterDTO;
import org.ljz.gift.util.AjaxResult;

public interface IVerifyCodeService {
   AjaxResult sendVerifyCode(String mobilePhone);
   String getVerifyCode(String phone);

}
