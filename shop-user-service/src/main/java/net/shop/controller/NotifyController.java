package net.shop.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Api(tags = "通知模块")
@RestController
@RequestMapping("/api/user/v1")
@Slf4j
public class NotifyController {

    @Autowired
    private Producer kaptchaProduer;

    @GetMapping("/kapthca")
    public void getKapcher(HttpServletRequest request, HttpServletResponse response) {
        String kaptchaProduerText = kaptchaProduer.createText();
        log.info("图形验证码:{}",kaptchaProduerText);
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
}
