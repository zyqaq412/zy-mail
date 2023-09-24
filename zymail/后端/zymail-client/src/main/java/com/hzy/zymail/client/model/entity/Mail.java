package com.hzy.zymail.client.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (Mail)表实体类
 *
 * @author makejava
 * @since 2023-09-24 16:48:46
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail implements Serializable {
    // 邮件id@TableId
    private Long emilId;

    // 调度源(发件人)
    private String source;
    // 收件人
    private String toUser;
    // 邮件主题
    private String subject;
    // 邮件内容
    private String content;
    // 是否被保存为模板
    private Integer isTemplate;
    // 发送时间
    private Date sendTime;

    // 下面的字段数据库不存在 用于定时任务
    /**
     * 是否定时
     */

    private Boolean timer;

    /**
     * 开始时间
     */

    private Date startTime;

    /**
     * 结束时间
     */

    private Date endTime;

    /**
     * crontab表达式
     */

    private String crontab;

}
