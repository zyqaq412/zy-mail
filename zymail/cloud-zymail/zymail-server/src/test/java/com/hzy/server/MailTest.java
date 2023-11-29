package com.hzy.server;


import com.hzy.zyamil.common.constant.SystemConstant;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.server.service.LocalMailService;
import com.hzy.zyamil.server.service.MailService;
import com.hzy.zyamil.server.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * @title: MailTest
 * @Author zxwyhzy
 * @Date: 2023/10/2 21:19
 * @Version 1.0
 */
@SpringBootTest
public class MailTest {
    @Autowired
    private MailService mailService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private LocalMailService localMailService;
    @Test
    public void getAllJobs() throws SchedulerException {
        Mail mail = new Mail();
        mail.setSubject("cs");
        mail.setCron("0 0 12 * * ?");
        mail.setToUser("3296137356@qq.com");
        mail.setContent("cscs");
        mail.setStartTime(new Date());
        mail.setTimer(true);
        localMailService.sendMail(mail);
        GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.anyTriggerGroup();
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(groupMatcher);
        System.out.println(groupMatcher);
    }
    @Test
    public void getMailById(){
        Mail byId = mailService.getById(1708836545793941505l);
        System.out.println(byId);
    }
    @Test
    public void getKeys(){
        Collection<String> keys = redisCache.getHashHkeys(SystemConstant.SOURCES_KEY);
        for (String s : keys){
            System.out.println(s);
        }
    }
    @Test
    public void create(){
        String[] toUsers = {"2791517764@qq.com","3296137356@qq.com"};
        for (int i = 0; i < 10; i++) {
            Mail mail = new Mail();
            mail.setSubject("cs");
            mail.setSource(toUsers[1]);
            mail.setContent("dsdsaas");
            mail.setToUser(toUsers[0]);

            mail.setSendTime(new Date());
            mailService.save(mail);
        }
    }
}
