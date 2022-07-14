package net.shop.service.impl;

import net.shop.enums.BizCodeEnum;
import net.shop.enums.SendCodeEnum;
import net.shop.component.MailService;
import net.shop.service.NotifyService;
import net.shop.utils.CheckUtil;
import net.shop.utils.CommonUtils;
import net.shop.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MailService mailService;

    /**
     * 验证码标题
     */
    private static final String SUBJECT = "shop验证码";

    /**
     * 验证码内容
     */
    private static final String CONTENT = "您的验证码是%s,有效时间是60秒！";

    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        if (CheckUtil.isEmail(to)){
            String code = String.format(CONTENT, CommonUtils.getRandomCode(6));
            mailService.sendMail(to,SUBJECT, code);
            return JsonData.buildSuccess();
        }else if (CheckUtil.isPhone(to)){
            return JsonData.buildError("暂时不接受手机验证码");
        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }
}
