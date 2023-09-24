package com.hzy.server.service.impl;

import com.hzy.server.api.ZymailClientApi;
import com.hzy.server.job.LocalSendMailJob;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.QuartzService;
import com.hzy.server.service.RemoteMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @title: remoteMailServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:08
 * @Version 1.0
 */
@Service
public class RemoteMailServiceImpl implements RemoteMailService {

    @Autowired
    private ZymailClientApi zymailClientApi;
    @Autowired
    private QuartzService quartzService;

    @Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()){
            try {
                zymailClientApi.sendEmail(mail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            // TODO 远程调度源定时发送
            createSendMailJob(mail);
        }
    }

    // 创建 定时任务
    private void createSendMailJob(Mail mail) {
        Map<String, Mail> params = new HashMap<>();
        params.put("mail", mail);
        String uniKey = UUID.randomUUID().toString();
        // 组设置为调度源appId
        quartzService.addJob("sendMail-" + uniKey,
                mail.getSource(),
                "sendMail-" + uniKey,
                mail.getSource(),
                LocalSendMailJob.class,
                mail.getCron(),
                mail.getStartTime(),
                mail.getEndTime(),
                params);
    }
}
