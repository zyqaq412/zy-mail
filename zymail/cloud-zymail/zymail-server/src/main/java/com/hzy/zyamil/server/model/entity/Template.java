package com.hzy.zyamil.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Template)表实体类
 *
 * @author makejava
 * @since 2023-10-04 23:38:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("template")
public class Template {
    @TableId(type = IdType.AUTO)
    private Long id;

    // 调度源
    private String source;
    private String name;
    // 模板内容
    private String content;

    private Date createTime;


}
