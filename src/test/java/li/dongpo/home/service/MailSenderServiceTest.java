package li.dongpo.home.service;

import li.dongpo.home.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dongpo.li
 * @date 2021/6/1
 */
public class MailSenderServiceTest extends BaseTest {

    @Autowired
    private MailSenderService mailSenderService;

    @Test
    public void test() {
        mailSenderService.sendSimpleMailMessage("测试邮件", "这是一封测试邮件");
    }

}
