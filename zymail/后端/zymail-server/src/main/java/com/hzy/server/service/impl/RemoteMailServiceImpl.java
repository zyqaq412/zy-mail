package com.hzy.server.service.impl;

import com.hzy.server.api.ZymailClientApi;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.RemoteMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

        }
    }
}
