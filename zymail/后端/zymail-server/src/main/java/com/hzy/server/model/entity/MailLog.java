package com.hzy.server.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (MailLog)表实体类
 *
 * @author makejava
 * @since 2023-10-05 21:26:35
 */
@SuppressWarnings("serial")
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
