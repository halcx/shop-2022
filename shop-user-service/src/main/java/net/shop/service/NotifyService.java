package net.shop.service;

import net.shop.enums.SendCodeEnum;
import net.shop.utils.JsonData;

public interface NotifyService {

    JsonData sendCode(SendCodeEnum sendCodeEnum,String to);
}
