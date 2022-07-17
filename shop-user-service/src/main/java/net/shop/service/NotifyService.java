package net.shop.service;

import net.shop.enums.SendCodeEnum;
import net.shop.utils.JsonData;

public interface NotifyService {

    /**
     * 发送验证码
     * @param sendCodeEnum
     * @param to
     * @return
     */
    JsonData sendCode(SendCodeEnum sendCodeEnum,String to);

    /**
     * 验证验证码
     * @param sendCodeEnum
     * @param to
     * @param code
     * @return
     */
    boolean checkCode(SendCodeEnum sendCodeEnum,String to,String code);
}
