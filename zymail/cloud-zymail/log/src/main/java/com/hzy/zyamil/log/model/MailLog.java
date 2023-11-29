package com.hzy.zyamil.log.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @title: MailLog
 * @Author zxwyhzy
 * @Date: 2023/11/29 20:19
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mail_log")
public class MailLog  {
    @TableId(type = IdType.AUTO)
    private Long id;

    // 调度源 记录者
    private String source;
    // 日志级别
    private Integer level;
    // 日志内容
    private String content;
    private Date createTime;
}
