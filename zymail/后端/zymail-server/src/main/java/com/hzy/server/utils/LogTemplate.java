package com.hzy.server.utils;

import java.util.Date;
import java.util.Optional;

/**
 * @title: LogTemplate
 * @Author zxwyhzy
 * @Date: 2023/10/5 21:51
 * @Version 1.0
 */
public class LogTemplate {
    /**
     *
     * @param jobName 工作名
     * @param jobGroupName 工作组 调度源
     * @param jobClass 工作类型 （本地，远程）
     * @param cron 表达式
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static String startJobTemplate(String jobName, String jobGroupName, Class jobClass, String cron, Date startTime, Date endTime) {
        startTime = Optional.ofNullable(startTime).orElse(new Date());
        String endTimeStr = endTime == null ? "持续" : String.valueOf(endTime);
        return String.format("<div>" +
                        "<p>工作名：%s</p>" +
                        "<p>工作组：%s</p>" +
                        "<p style=\"color: green\">启动！</p>" +
                        "<p>工作类型：%s</p>" +
                        "<p>定时任务表达式：%s</p>" +
                        "<p>调度时间：%s —— %s</p>" +
                        "<div>",
                jobName, jobGroupName, jobClass.getSimpleName(), cron, startTime, endTimeStr);
    }
    public static String resumeJobTemplate(String jobName, String jobGroupName) {
        return String.format("<div>" +
                        "<p>工作名：%s</p>" +
                        "<p>工作组：%s</p>" +
                        "<p style=\"color: green\">恢复！</p>" +
                        "</div>",
                jobName, jobGroupName);
    }
    public static String pauseJobTemplate(String jobName, String jobGroupName) {
        return String.format("<div>" +
                        "<p>工作名：%s</p>" +
                        "<p>工作组：%s</p>" +
                        "<p style=\"color: yellow\">暂停！</p>" +
                        "</div>",
                jobName, jobGroupName);
    }
    public static String delJobTemplate(String jobName, String jobGroupName) {
        return String.format("<div>" +
                        "<p>工作名：%s</p>" +
                        "<p>工作组：%s</p>" +
                        "<p style=\"color: red\">移除！</p>" +
                        "</div>",
                jobName, jobGroupName);
    }

    public static String endJobTemplate(String jobName, String jobGroupName, Class jobClass) {
        return String.format("<div>" +
                        "<p>工作名：%s</p>" +
                        "<p>工作组：%s</p>" +
                        "<p style=\"color: green\">完成！</p>" +
                        "<p>工作类型：%s</p>" +
                        "</div>",
                jobName, jobGroupName, jobClass.getSimpleName());
    }
    public static String modifyJobTemplate(String jobName, String jobGroupName, String param) {
        return String.format("<div>" +
                        "<p>工作名：%s</p>" +
                        "<p>工作组：%s</p>" +
                        "<p style=\"color: yellow\">修改！</p>" +
                        "<p>修改内容：%s</p>" +
                        "</div>",
                jobName, jobGroupName, param);
    }

}
