package net.shop.component.impl;

import lombok.extern.slf4j.Slf4j;
import net.shop.component.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    /**
     * springboot提供的简单抽象方法，直接注入
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * springboot自动注入配置文件内容
     */
    @Value("${spring.mail.from}")
    private String from;

    /**
     * 1.创建一个邮箱消息对象
     * 2.配置邮箱发送人
     * 3.配置邮件
     * 4.发送邮件
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendMail(String to, String subject, String content) {
        //这里后面可以扔到一个队列里面异步发送
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        log.info("邮件发送成功:{}",message.toString());
    }
}
