package com.nowcoder.community.mapper;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @Desc:描述
 * @Author：yyf
 * @Filename：MapperTest
 * @Date：2023/12/6 21:29
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MapperTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testMail(){
        mailClient.sendMail("1147646462@qq.com","test","Welcome");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "sunday");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("1147646462@qq.com","test",content);
    }
}
