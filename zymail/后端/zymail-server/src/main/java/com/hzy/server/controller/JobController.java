package com.hzy.server.controller;

import com.hzy.server.service.JobService;
import com.hzy.server.service.QuartzService;
import com.hzy.server.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @title: JobController
 * @Author zxwyhzy
 * @Date: 2023/10/4 17:19
 * @Version 1.0
 */
@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private QuartzService quartzService;

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
}
