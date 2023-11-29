package com.hzy.zyamil.job.controller;

import com.hzy.zyamil.common.model.dto.JobDto;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.job.jobs.SendMailJob;
import com.hzy.zyamil.job.service.JobService;
import com.hzy.zyamil.job.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @title: JobController
 * @Author zxwyhzy
 * @Date: 2023/11/29 16:00
 * @Version 1.0
 */
@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private QuartzService quartzService;

    @PostMapping("/add")
    public Result addJob(@RequestBody Mail mail){
        Map<String, Mail> params = new HashMap<>();
        params.put("mail", mail);
        String uniKey = UUID.randomUUID().toString();

        quartzService.addJob("sendMail-" + uniKey,
                mail.getSource(),
                "sendMail-" + uniKey,
                mail.getSource(),
                SendMailJob.class,
                mail.getCron(),
                mail.getStartTime(),
                mail.getEndTime(),
                params);
        return Result.okResult();
    }



    // 获取全部任务
    @GetMapping
    public Result getAllJobs() {
        return jobService.getAllJobs();
    }

    // 获取指定调度源的任务
    @GetMapping("/{appId}")
    public Result getJobsByAppId(@PathVariable String appId) {
        return jobService.getJobsByAppId(appId);
    }

    // 恢复任务
    @PutMapping("/resume/{jobName}/{jobGroupName}")
    public Result resumeJob(@PathVariable String jobName, @PathVariable String jobGroupName) {
        quartzService.resumeJob(jobName, jobGroupName);
        return Result.okResult();
    }

    // 暂停任务
    @PutMapping("/pause/{jobName}/{jobGroupName}")
    public Result pauseJob(@PathVariable String jobName, @PathVariable String jobGroupName) {
        quartzService.pauseJob(jobName, jobGroupName);
        return Result.okResult();
    }

    // 删除任务
    @DeleteMapping("/{jobName}/{jobGroupName}")
    public Result delJob(@PathVariable String jobName, @PathVariable String jobGroupName) {
        quartzService.removeJob(jobName, jobGroupName, jobName, jobGroupName);
        return Result.okResult();
    }

    // TODO 调度器操作需要每一个实列都执行
    // 使用 消息队列 发送消息实现
    // 获取调度器状态
    @GetMapping("/scheduler/status")
    public Result getSchedulerStatus() {
        return Result.okResult(quartzService.getSchedulerStatus());
    }

    // 启动调度器
    @PutMapping("/scheduler/start")
    public Result startScheduler() {
        quartzService.startScheduler();
        return Result.okResult();
    }

    // 暂停调度器
    @PutMapping("/scheduler/pause")
    public Result pauseScheduler() {
        quartzService.pauseScheduler();
        return Result.okResult();
    }

    @PutMapping("/modify/{radio}")

    public Result modifyJob(@PathVariable Integer radio, @RequestBody JobDto job) {
        return quartzService.modifyJob(radio, job);

    }
}
