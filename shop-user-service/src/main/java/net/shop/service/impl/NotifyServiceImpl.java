package net.shop.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.shop.constent.CacheKey;
import net.shop.enums.BizCodeEnum;
import net.shop.enums.SendCodeEnum;
import net.shop.component.MailService;
import net.shop.service.NotifyService;
import net.shop.utils.CheckUtil;
import net.shop.utils.CommonUtils;
import net.shop.utils.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
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

    /**
     * 验证码过期时间
     */
    private static int CODE_EXPIRED = 60*1000*10;


    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 前置操作：判断是否重复发送
     *
     * 1、存储验证码到缓存
     * 2、发送邮箱验证码
     *
     * 后置操作：存储发送记录
     * @param sendCodeEnum
     * @param to
     * @return
     */
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {

        //封装存储key
        String cacheKey = String.format(CacheKey.CHECK_CODE_KEY,sendCodeEnum.name(),to);

        String cacheValue = redisTemplate.opsForValue().get(cacheKey);

        //如果不为空则判断60s内是否重复发送
        //如果为空则肯定是没法送的
        if(StringUtils.isNotBlank(cacheValue)){
            //拿到时间戳
            long ttl = Long.parseLong(cacheValue.split("_")[1]);
            //当前时间戳-验证码发送时间戳，如果<60s则不给重复发送
            if(CommonUtils.getCurrentTimestamp()-ttl<1000*60){
                log.info("重复发送验证码，时间间隔：{}秒",(CommonUtils.getCurrentTimestamp()-ttl)/1000);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }

        //拼接验证码：验证码+时间戳
        String code = String.format(CONTENT, CommonUtils.getRandomCode(6));

        String value = code+"_"+CommonUtils.getCurrentTimestamp();

        redisTemplate.opsForValue().set(cacheKey,value,CODE_EXPIRED, TimeUnit.MILLISECONDS);

        if (CheckUtil.isEmail(to)){
            mailService.sendMail(to,SUBJECT, code);
            return JsonData.buildSuccess();
        }else if (CheckUtil.isPhone(to)){
            return JsonData.buildError("暂时不接受手机验证码");
        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }
}
