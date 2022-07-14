package net.shop_2022.biz;

import lombok.extern.slf4j.Slf4j;
import net.shop.UserApplication;
import net.shop.component.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class MailTest {

    @Autowired
    private MailService mailService;

    @Test
    public void testSendMail(){
        mailService.sendMail("1278571538@qq.com","hello","hello,world");
    }

}
