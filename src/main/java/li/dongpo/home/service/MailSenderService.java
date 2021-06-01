package li.dongpo.home.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author dongpo.li
 * @date 2021/6/1
 */
@Service
public class MailSenderService {
    private static final Logger logger = LoggerFactory.getLogger(MailSenderService.class);

    @Value("${mail.from}")
    private String from;
    @Value("${mail.to}")
    private String to;

    @Autowired
    private MailSender mailSender;

    public void sendSimpleMailMessage(String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(subject);
        message.setTo(to);
        message.setText(text);

        mailSender.send(message);
    }


}
