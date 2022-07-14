package net.shop.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.shop.enums.BizCodeEnum;
import net.shop.enums.SendCodeEnum;
import net.shop.service.NotifyService;
import net.shop.utils.CommonUtils;
import net.shop.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Api(tags = "通知模块")
@RestController
@RequestMapping("/api/user/v1")
@Slf4j
public class NotifyController {

    @Autowired
    private Producer kaptchaProduer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NotifyService notifyService;

    /**
     * 图形验证码有效期十分钟
     */
    private static final long KAPTCHA_CODE_EXPRIED = 60*1000*10;

    @GetMapping("/kapthca")
    public void getKapcher(HttpServletRequest request, HttpServletResponse response) {
        String kaptchaProduerText = kaptchaProduer.createText();
        log.info("图形验证码:{}",kaptchaProduerText);

        //存储
        redisTemplate.opsForValue().set(getKapthcaKey(request),kaptchaProduerText,KAPTCHA_CODE_EXPRIED, TimeUnit.MICROSECONDS);

        BufferedImage bufferedImage = kaptchaProduer.createImage(kaptchaProduerText);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage,"jpg",outputStream);
            //输出流关闭先flush
            outputStream.flush();
            //再close
            outputStream.close();
        }catch (IOException e){
            log.error("获取图形验证码异常：{}",e);
            e.printStackTrace();
        }
    }


    /**
     * 1、匹配图形验证码是否正常
     * 2、发送验证码
     * @param to
     * @param kaptcha
     * @return
     */
    @ApiOperation("发送邮箱验证码")
    @GetMapping("/send_code")
    JsonData sendRegisterCode(@RequestParam(value="to",required = true)String to,
                              @RequestParam(value = "kaptcha",required = true) String kaptcha,
                              HttpServletRequest request){
        String kapthcaKey = getKapthcaKey(request);
        String cacheKey = redisTemplate.opsForValue().get(kapthcaKey);
        log.info(cacheKey);
        if (cacheKey != null&&kaptcha!=null&&cacheKey.equalsIgnoreCase(kaptcha)) {
            //成功
            //验证码要失效掉
            redisTemplate.delete(cacheKey);
            JsonData jsonData = notifyService.sendCode(SendCodeEnum.USER_REGISTER, to);
            return jsonData;

        }else {
            return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
        }
    }

    /**
     * 获取缓存的key
     * @param request
     * @return
     */
    private String getKapthcaKey(HttpServletRequest request){
        String ipAddr = CommonUtils.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        String key = "user-service:kaptcha:"+CommonUtils.MD5(ipAddr+userAgent);
        log.info("ip={}",ipAddr);
        log.info("user-agent={}",userAgent);
        log.info("key={}",key);
        return key;

    }

}
